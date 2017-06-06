package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.spiderdt.common.notice.common.*;
import com.spiderdt.common.notice.dao.NoticeTasksDao;
import com.spiderdt.common.notice.dao.TrackRecodeDao;
import com.spiderdt.common.notice.entity.TrackRecodeEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by fivebit on 2017/6/3.
 * 提供URL服务。
 * 包括：生成跟踪URL
 * 解析加密URL。
 */
@Service("urlService")
public class UrlService {
    private long url_time = 864000; //十天
    @Resource
    private TrackRecodeDao trackRecodeDao;
    @Resource
    private NoticeTasksDao noticeTasksDao;
    @Resource
    private BitlyService bitlyService;
    /**
     * 批量制作track url
     * @param params
     * @return
     * params 每个map的key为 dest_url/rid/sc/ac.前两个必须。后两个可选
     * return Map<src_url:track_url>
     * 还包括两个逻辑：存数据库和redis
     */
    public Map<String,String> makeTrackUrlPair(List<Map<String,String>> params){

        return null;
    }
    public String makeShortUrl(String url){
        String short_url = bitlyService.getShortUrl(url);
        return short_url;
    }

    /**
     * 单独制作track url,其他逻辑同上
     * @param param
     * @return
     */
    public Map<String,String> makeTrackUrlPair(Map<String,String> param){
        Map<String,String> pair_ret = null;
        if(param.containsKey("dest_url") == false){
            Jlog.error("make rtrack url param error:"+param.toString());
            return pair_ret;
        }
        String dest_url = param.get("dest_url");
        param.remove("dest_url");
        String track_url_suffix = Jurl.makeTrackUrlSuffix(dest_url,param);
        pair_ret = Maps.newHashMap();
        pair_ret.put(dest_url,track_url_suffix);

        saveTrackPairUrlToRedis(dest_url,track_url_suffix,param);

        return pair_ret;

    }

    public Boolean saveTrackPairUrlToDb(String org_url,String track_url,Map<String,String> param){
        return true;

    }
    public Boolean saveTrackPairUrlToRedis(String org_url,String track_url_suffix,Map<String,String> param){
        String md5_org_url = Jencode.MD5(org_url);
        String org_url_redis_key = "track::org_url::"+md5_org_url;
        String track_url_redis_key = "track::track_url::"+track_url_suffix;
        param.put("track_url_suffix",track_url_suffix);
        JSONObject org_json_param = Utils.map2Json(param);
        param.remove("track_url");
        param.put("org_url",org_url);
        JSONObject track_json_param = Utils.map2Json(param);
        try {
            JredisClient.addString(org_url_redis_key, org_json_param.toJSONString(), url_time);
            JredisClient.addString(track_url_redis_key, track_json_param.toJSONString(), url_time);
        }catch (Exception ee){
            Jlog.error("save track pair url to redis error"+ee.getMessage());
            return false;
        }

        return true;

    }

    /**
     * 根据加密url之后的code，获取原始信息。
     * 首先从redis中查询，如果没有，则从数据库中查询
     * @param encrypt_code
     * @return
     */
    public Map<String,String> getTrackUrlOrgInfoByEncrypt(String encrypt_code){
        //redis key
        String encrypt_redis_key = "track::track_url::"+encrypt_code;
        String encrypt_org_info = "";
        Map<String,String> org_url_and_params = null;
        try {
            encrypt_org_info = JredisClient.getString(encrypt_redis_key);
            Jlog.debug("get from redis:"+encrypt_redis_key+" value:"+encrypt_org_info);
            if(encrypt_org_info != null) {
                JSONObject encrypt_org_json = JSONObject.parseObject(encrypt_org_info);
                org_url_and_params = Utils.json2map(encrypt_org_json);
            }
        }catch(Exception ee){
            Jlog.error("get org info by track url error:"+ee.getMessage());
        }
        if(org_url_and_params == null){
            TrackRecodeEntity trackRecodeEntity = getTrackRecodeByEncrypt(encrypt_code);
            org_url_and_params = trackRecodeEntity.getMapParams();
            org_url_and_params.put("org_url",trackRecodeEntity.getUrlOrg());
        }
        org_url_and_params.put("url_encode",encrypt_code);
        return org_url_and_params;
    }

    public TrackRecodeEntity getTrackRecodeByEncrypt(String encrypt_code){
        TrackRecodeEntity trackRecodeEntity = trackRecodeDao.getTrackRecodeInfoByEncrypt(encrypt_code);
        if(trackRecodeEntity != null){
            trackRecodeEntity.setMapParams(Utils.json2map(JSONObject.parseObject(trackRecodeEntity.getParams())));
        }
        return trackRecodeEntity;

    }

    public Boolean updateTaskResultByTrackInfo(Map<String,String> track_info){
        String track_url_suffix = track_info.get("track_url_suffix");
        String sc = track_info.get("sc");
        String ac = track_info.get("ac");
        trackRecodeDao.updateTrackRecodeStatus(track_url_suffix,1);
        noticeTasksDao.updateNoticeTaskTrackInfo(track_url_suffix,ac,"1",Jdate.getNowStrTime());
        return true;
    }


}
