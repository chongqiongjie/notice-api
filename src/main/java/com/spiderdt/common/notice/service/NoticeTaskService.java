package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.spiderdt.common.notice.common.*;
import com.spiderdt.common.notice.dao.NoticeTasksDao;
import com.spiderdt.common.notice.dao.TasksResultDao;
import com.spiderdt.common.notice.dao.TrackRecodeDao;
import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import com.spiderdt.common.notice.entity.TrackRecodeEntity;
import com.spiderdt.common.notice.errorhander.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.spiderdt.common.notice.common.JhttpClient.httpGet;

/**
 * Created by fivebit on 2017/6/5.
 */
@SuppressWarnings("ALL")
@Service("noticeTaskService")
public class NoticeTaskService {
    @Resource
    private NoticeTasksDao noticeTasksDao;

    @Resource
    private TrackRecodeDao trackRecodeDao;

    @Resource
    private TasksResultDao tasksResultDao;

    @Resource
    private UrlService urlService;

    @Resource
    private SmsService smsService;

    @Resource
    private EmailService emailService;

    @Resource
    private ThreadPoolTaskExecutor taskPool;

    @Autowired
    Slog slog;

    @Autowired
    FileService fileService;

    @Autowired
    Utils utils;

    @Autowired
    Sredis sredis;

    @Value("${attachment.storePath}") String attachmentStorePath;

    public static final String EMAIL_TYPE = "email";
    public static final String SMS_TYPE = "sms";


    protected  String json_address_key = "address"; //phone/email
    protected  String json_riid_key = "riid";
    protected  String url_ac = "click";
    protected  String track_type = "click";
    protected  String image_track_type = "open";

    /**
     * 通过间隔的扫描数据库状态，获取任务,发送短息/email
     */
    public void sendNoticeTaskByDbScan() throws AppException {
        List<NoticeTasksResultEntity> task_list_new = tasksResultDao.getNewNoticeTaskResultsBatch(AppConstants.TASK_RESULT_STATUS_NEW);
        List<NoticeTasksResultEntity> task_list_failed = tasksResultDao.getNewNoticeTaskResultsBatch(AppConstants.TASK_RESULT_STATUS_AUTH_FAILED);
        List<NoticeTasksResultEntity> task_list = new ArrayList<>();
        task_list.addAll(task_list_new);
        task_list.addAll(task_list_failed);
        slog.info("get task list from db, count:"+task_list.size());
        Boolean sms_st = false;
        Boolean email_st = false;
        if(task_list.size() > 0){
            List<NoticeTasksResultEntity> sms_task_list = Lists.newArrayList();
            List<NoticeTasksResultEntity> email_task_list = Lists.newArrayList();
            for(NoticeTasksResultEntity item: task_list) {
                if (item.getTaskType().equals(AppConstants.SMS_TASK_TYPE) == true) {
                    sms_task_list.add(item);
                } else if (item.getTaskType().equals(AppConstants.EMAIL_TASK_TYPE) == true) {
                    email_task_list.add(item);
                }
            }

            Set<Integer> task_ids = Sets.newHashSet();
            List<String> riid_lists = Lists.newArrayList();
            for(NoticeTasksResultEntity item:task_list){
                task_ids.add(item.getTaskId());
                riid_lists.add(item.getRiid());
            }
            updateTaskStatusBatch(task_ids,AppConstants.TASK_STATUS_SENDING);
            updateTaskResultStatusBatch(riid_lists,AppConstants.TASK_RESULT_STATUS_SENDING);
            sms_st = smsService.sendSmsBatch(sms_task_list);
            email_st = emailService.sendEmailBatch(email_task_list);
        }
        slog.info("send notice end:sms:"+sms_st+" email:"+email_st);
    }
    /**
     *  创建一个通知task。包括初始化result_info表。
     *  区分不同的任务类型。如email／sms等
     * @param noticeTasksEntity
     * @return
     */
    public Boolean createNoticeTask(NoticeTasksEntity noticeTasksEntity) throws AppException {
        slog.debug("create notice task:"+noticeTasksEntity);
        //检测参数是否合法
        checkNoticeTaskParams(noticeTasksEntity);
        Integer task_id = 0;
        try {
            //设置地址和用户名 address 为　json 字符串，多个邮箱
            noticeTasksEntity.setAddresses(getAddressesFromJobId(noticeTasksEntity.getJobId(),noticeTasksEntity.getTaskType()));
            Integer count = noticeTasksDao.createNoticeTask(noticeTasksEntity);
            if(count !=  1 ){
                slog.error("create notice task entity error:"+noticeTasksEntity);
                throw new AppException("0","create notice task error");
            }
            task_id = noticeTasksEntity.getTaskId();
            //下面创建任务结果详情时有点慢，可以放到线程池中运行。
            slog.debug("create notce task task_id:"+task_id+" :"+noticeTasksEntity);

            Boolean st_result_info = createTaskResultInfo(task_id,noticeTasksEntity);
            if(st_result_info == false){
                slog.error("create notice task result info error:"+noticeTasksEntity);
            }

        }catch (Exception ee){
            slog.error("create notice task error:"+ee.getMessage());
            throw new AppException("0","create notice task error");
        }

        // 下载附件
        String taskId = noticeTasksEntity.getTaskId() + "";
        String taskFileDir = attachmentStorePath + "/" + taskId + "/" ;
        String attachmentJsonString = noticeTasksEntity.getAttachments();
        // path list 中可能有一个值，但是为 ""
        if (null != attachmentJsonString && !"".equals(attachmentJsonString)) {
            ArrayList<String> paths = new ArrayList<>();

            JSONArray jsonArray = JSON.parseArray(attachmentJsonString);
            for (Object jsonObject : jsonArray) {
                Map<String, String> attachmentAttributeMap = (Map<String, String>) jsonObject;
                String downloadUrl = attachmentAttributeMap.get("downloadUrl");
                String fileName = attachmentAttributeMap.get("fileName");
                fileService.download(downloadUrl, taskFileDir, fileName);
            }
        }
        slog.debug("create notice task end and update status to new");
        noticeTasksDao.updateNoticeTaskStatus(task_id,AppConstants.TASK_RESULT_STATUS_NEW,Jdate.getNowStrTime());       //全部创建成功之后，再修改成new的状态，否则会被另一个进程调度到。

        return true;
    }

    /**
     * 检测创建任务参数
     * @param noticeTasksEntity
     * @return
     */
    public Boolean checkNoticeTaskParams(NoticeTasksEntity noticeTasksEntity){

        return true;
    }

    /**
     * 通过job_id ,获取对应的用户的地址和用户名
     * @param job_id
     * @return
     * name,phone/email json string
     */
    public String getAddressesFromJobId(String job_id, String task_type){

////        String ret = "[{\"name\":\"test\",\"address\":\"13458555648\"}]";
//       // String ret = "[{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"}]";
//        String ret = "[{\"name\":\"test\",\"address\":\"chong.qiongjie@spiderdt.com\"},{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"}]";
////        String ret = "[{\"name\":\"test\",\"address\":\"ran.bo@spiderdt.com\"}, {\"name\":\"test2\",\"address\":\"13458555648@163.com\"}]";

        String dataSource = "latetime";
//        String clientUrl = "http://192.168.1.2:8095/";
//        String url = clientUrl + "jupiter-v1/jupiter/client_info/" + job_id + "?data_source=" + dataSource;

//        String clientUrl = "http://127.0.0.1:8080/";
        String clientUrl = "http://127.0.0.1:8080/jupiter-api/";
        String url = clientUrl + "jupiter/client_info/" + job_id + "?data_source=" + dataSource;

        JSONObject jsonObject = httpGet(url);
        // 伪造数据 使用数据库进行伪造数据

//        String testString = "{\"client_info\":[[{\"info_list\":\"[[\"ranbo\",\"13739440552\",\"四川省\",\"资阳市\",\"安岳县\",1,1,0,2]]\"}],[{\"e_mail\":\"1123582921@qq.com\",\"info_list\":\"[[\"马小美\",\"15248806556\",\"黑龙江省\",\"哈尔滨市\",\"道里区\",1,1,0,2]]\"}], [{\"e_mail\":\"1219632773@qq.com\",\"info_list\":\"[[\"吴文丹\",\"13926458551\",\"广东省\",\"广州市\",\"花都区\",1,1,0,2]]\"}],[{\"info_list\":\"[[\"杨丽\",\"18325274451\",\"重庆\",\"重庆市\",\"秀山土家族苗族自治县\",1,1,0,2]]\"}]],\"status\":\"success\"}";
//        JSONObject jsonObject = JSON.parseObject(testString);


        List<Object> clientInfo = (List<Object>) jsonObject.get("client_info");
        if(AppConstants.EMAIL_TASK_TYPE.equals(task_type)) {
            return getEmailAddresses(clientInfo);
        } else {
            return getSmsAddresses(clientInfo);
        }

    }

    public String getEmailAddresses(List<Object> clientInfo) {

        JSONArray personList = new JSONArray();
        for (int i = 0; i < clientInfo.size(); i++) {
            JSONObject person = new JSONObject();
            JSONArray mess_info = (JSONArray) clientInfo.get(i);
            JSONObject mess_item = (JSONObject) mess_info.get(0);

            String email = (String) mess_item.get("e_mail");
            String address = (String) mess_item.get("info_list");

            JSONArray items = JSONArray.parseArray(address);
            for (int j = 0; j < items.size(); j++) {
                JSONArray subItem = items.getJSONArray(j);
                String name = subItem.getString(0);

                if (email != null) {
                    person = getPerson(name, email, utils.getUUID());

                }
            }
            if(!person.isEmpty()){
                personList.add(person);
            }

        }

        return personList.toString();
    }


    public String getSmsAddresses(List<Object> clientInfo){

        String str = null;
        JSONArray personList = new JSONArray();

        for(int i = 0; i < clientInfo.size(); i++) {
            String riid = utils.getUUID();

            JSONArray mess_info = (JSONArray) clientInfo.get(i);
            JSONObject mess_item = (JSONObject) mess_info.get(0);
            String singleUserInfo = (String) mess_item.get("info_list");

            JSONArray singleUserInfoArray = JSON.parseArray(singleUserInfo);
            JSONArray jsonArray = (JSONArray) singleUserInfoArray.get(0);
            String name = (String) jsonArray.get(0);
            String phone = (String) jsonArray.get(1);
            JSONObject person = getPerson(name, phone, riid);
            if(!person.isEmpty()){
                personList.add(person);
            }

            // 将其他电话号码存入缓存
            singleUserInfoArray.remove(0);
            if (singleUserInfoArray.isEmpty()) {
                Jlog.info("该用户:" + name + " 只有一个电话号码，对应的 notice_tasks_result_info 的 riid 为:" + riid);
            } else {
                singleUserInfo = singleUserInfoArray.toJSONString();
                Jlog.info("该用户多个电话号码，存入缓存的剩下的电话信息为:" + singleUserInfo);
                sredis.addString(riid, singleUserInfo);
            }
        }
        return personList.toString();
    }

    /**
     * 单个客户的信息
     * @param name
     * @param address phone/email address
     * @param riid 唯一对应数据库的 notice_tasks_result
     * @return
     */
    public JSONObject getPerson(String name, String address, String riid) {
        JSONObject person = new JSONObject();
        person.put("name", name);
        person.put("address", address);
        person.put("riid", riid);
        return person;
    }


    /**
     * 通过任务信息，创建任务的具体信息表notice_tasks_result_info。
     * 存储的是每个地址对应发送的修改后的信息.
     * @param task_id
     * @param noticeTasksEntity
     * @return
     * @throws AppException
     */
    public Boolean createTaskResultInfo(Integer task_id,NoticeTasksEntity noticeTasksEntity) throws AppException {
        Jlog.debug("create task result info begin:"+noticeTasksEntity);
        /*
        DefaultCreateNoticeResultTask noticeResultRunTask = new DefaultCreateNoticeResultTask();
        noticeResultRunTask.setTaskId(task_id);
        noticeResultRunTask.setNoticeTasksEntity(noticeTasksEntity);
        Jlog.info("xxxxxx:"+noticeResultRunTask.toString());
        taskPool.execute(noticeResultRunTask);
        */
        try {
            List<NoticeTasksResultEntity> tasksResultEntities =  makeTaskResultInfo(task_id,noticeTasksEntity);
            slog.debug("get task result entities:"+tasksResultEntities);
            saveNoticeResultsBatch(tasksResultEntities);
        }catch ( Exception ee){
            slog.error("create notice task result error:"+ee.getMessage());
            updateNoticeTaskSatus(task_id, AppConstants.TASK_STATUS_FAILED);
            return false;
        }
        updateNoticeTaskSatus(task_id, AppConstants.TASK_STATUS_NEW);
        Jlog.debug("create task result info end :"+noticeTasksEntity);
        return true;
    }

    /**
     * 从数据库中存的jsonstring，转换成json object。检验格式。
     * @param addresses
     * @return
     */
    public List<Map<String,String>> getAddresssesFromJsonString(String addresses) throws AppException {
        JSONArray addes = null;
        try {
            addes = JSON.parseArray(addresses);
        }catch (Exception ee){
            slog.error("parse addresses error:"+addresses+" message:"+ee.getMessage());
            throw new AppException("0","parse addresses error:"+addresses);
        }
        List<Map<String, String>> rets = Lists.newArrayList();
        if(addes != null) {
            for (Object add : addes) {
                JSONObject add_json = JSONObject.parseObject(add.toString());
                Set<String> key_set = add_json.keySet();
                if (key_set.contains(this.json_address_key) == false) {
                    slog.error("addresses format is wrong:" + addresses);
                    throw new AppException("0", "address format is error:" + addresses);
                }
                Map<String, String> item = Maps.newHashMap();
                for (String key : key_set) {
                    item.put(key, add_json.getString(key));
                }
                rets.add(item);
            }
        }
        return rets;
    }

    /**
     * 根据notice_tasks_result_info的一条记录，生成对应的notice_tasks_track_recode entitys
     * @param noticeTasksResultEntity
     * @return
     */
    public List<TrackRecodeEntity> makeTrackRecodeInfo(NoticeTasksResultEntity noticeTasksResultEntity){
        //抽取message中所有的url。
        HashMap<Integer, String> orgUrlsOrderMap = Utils.getUrlsFromMessage(noticeTasksResultEntity.getMessage(),noticeTasksResultEntity.getTaskType());
        slog.debug("parse url:"+orgUrlsOrderMap);
        List<TrackRecodeEntity> tr_lists = Lists.newArrayList();
        for (Map.Entry<Integer, String> entry : orgUrlsOrderMap.entrySet()) {
            slog.debug("Key = " + entry.getKey() + ", Value = "  + entry.getValue());
            TrackRecodeEntity tr_entity = new TrackRecodeEntity();
            Map<String,String> trackUrlParam = Maps.newHashMap();
            trackUrlParam.put("dest_url",entry.getValue());
            trackUrlParam.put("task_id",String.valueOf(noticeTasksResultEntity.getTaskId()));
            trackUrlParam.put("riid",noticeTasksResultEntity.getRiid());
            trackUrlParam.put("sc",noticeTasksResultEntity.getTaskType());      //使用任务类型 sms/email
            trackUrlParam.put("ac",this.url_ac);
            //  针对多个 a 链接
            trackUrlParam.put("id", entry.getKey().toString());
            Map<String,String> urlPair = urlService.makeTrackUrlPair(trackUrlParam);
            tr_entity.setTrackUrlSuffix(urlPair.get(entry.getValue()));
            tr_entity.setTrackType(this.track_type);
            tr_entity.setUrlOrg(entry.getValue());
            tr_entity.setTaskId(noticeTasksResultEntity.getTaskId());
            tr_entity.setRiid(noticeTasksResultEntity.getRiid());
            tr_entity.setMessageOrg(noticeTasksResultEntity.getMessage());
            tr_entity.setParams(JSON.toJSONString(trackUrlParam));
            tr_lists.add(tr_entity);
        }

        if(noticeTasksResultEntity.getTaskType().equals(AppConstants.EMAIL_TASK_TYPE) == true){     //需要额外的打开跟踪链接
            TrackRecodeEntity tr_entity = new TrackRecodeEntity();
            Map<String,String> trackUrlParam = Maps.newHashMap();
            String dest_url = "img_track_url:"+noticeTasksResultEntity.getRiid();   //hack
            trackUrlParam.put("dest_url",dest_url);
            trackUrlParam.put("task_id",String.valueOf(noticeTasksResultEntity.getTaskId()));
            trackUrlParam.put("riid",noticeTasksResultEntity.getRiid());
            trackUrlParam.put("sc",noticeTasksResultEntity.getTaskType());      //使用任务类型 sms/email
            trackUrlParam.put("ac",this.url_ac);
            Map<String,String> urlPair = urlService.makeTrackUrlPair(trackUrlParam);
            tr_entity.setTrackUrlSuffix(urlPair.get(dest_url));
            tr_entity.setTrackType(this.image_track_type);
            tr_entity.setUrlOrg(dest_url);
            tr_entity.setTaskId(noticeTasksResultEntity.getTaskId());
            tr_entity.setRiid(noticeTasksResultEntity.getRiid());
            tr_entity.setMessageOrg(noticeTasksResultEntity.getMessage());
            tr_entity.setParams(JSON.toJSONString(trackUrlParam));
            tr_lists.add(tr_entity);
        }
        return tr_lists;
    }
    /**
     * 替换message里面的url为跟踪url
     * @param message
     * @param item
     * @return
     */
    public String replaceMessageUrlToTrackUrl(NoticeTasksResultEntity noticeTasksResultEntity,List<TrackRecodeEntity> recodes){
        String message = noticeTasksResultEntity.getMessage();
        for(TrackRecodeEntity recode: recodes){
            String url_org = recode.getUrlOrg();
            String track_url = urlService.getCompleteTrackUrl(recode.getTrackUrlSuffix());
            message = replaceUrlFromMessage(noticeTasksResultEntity.getTaskType(),message,url_org,track_url);
        }
        return message;
    }

    /**
     * 根据不同的任务类型，替换message中的url。sms，则直接替换。email，则需要保留原来链接作为text，link则变为track url
     * @param task_type
     * @param message
     * @param org_url
     * @param track_url
     * @return
     */
    public String replaceUrlFromMessage(String task_type,String message,String org_url,String track_url) {
        String short_url = "";
        if(task_type.equals(AppConstants.SMS_TASK_TYPE) == true){
            short_url = urlService.makeShortUrl(track_url);
        }else if (task_type.equals(AppConstants.EMAIL_TASK_TYPE) == true){
            short_url = track_url;
        }
        message = Utils.replaceUrlFromMessage(message,org_url,short_url);
        return message;
    }

    /**
     * 添加邮件点开时的跟踪链接,邮件逻辑需要实现
     * @param message
     * @return
     */
    public String addOpenTrackUrl(NoticeTasksResultEntity item, List<TrackRecodeEntity> trackRecodeEntities){
        if(item.getTaskType().equals(AppConstants.EMAIL_TASK_TYPE) == true){
            for(TrackRecodeEntity tr: trackRecodeEntities){
               if(tr.getTrackType().equals(this.image_track_type) == true){
                   String track_url = urlService.getCompleteTrackUrl(tr.getTrackUrlSuffix());
                   track_url = "<img src=\""+track_url+"\" width=\"0\" height=\"0\" style=\"display: none\"/>";
                   return item.getMessage()+ track_url;
               }
            }
        }
        return item.getMessage();
    }

    /**
     * 格式化task_result_info信息
     * 这里包括了创建tasks_track_recode信息
     * 通过 address 这个 json 获取多个 address 存入 task_result_info 信息
     * @param task_id
     * @param noticeTasksEntity
     * @return
     */
    public List<NoticeTasksResultEntity> makeTaskResultInfo(Integer task_id,NoticeTasksEntity noticeTasksEntity) throws AppException {

        List<Map<String,String>> addresses_map_list = getAddresssesFromJsonString(noticeTasksEntity.getAddresses());
        slog.debug("get address map list :"+addresses_map_list);
        List<NoticeTasksResultEntity> resultInfoList =  Lists.newArrayList();
        List<TrackRecodeEntity> all_trackRecodeEntities = Lists.newArrayList();
        int sizeOfAddressMapList = addresses_map_list.size();
        for(Map<String,String> address_map:addresses_map_list){

            NoticeTasksResultEntity item = new NoticeTasksResultEntity();
            String address = address_map.get(this.json_address_key);
            String riid = address_map.get(this.json_riid_key);
            String message = noticeTasksEntity.getMessage();
            String subject = noticeTasksEntity.getSubject();
            for(Map.Entry<String,String> entry : address_map.entrySet()){
                if (entry.getKey().equals(this.json_address_key) == true){
                    continue;
                }
                message = message.replace("#"+entry.getKey()+"#",entry.getValue());     //模版内容替换,URL还未替换
                subject = (subject== null || subject.isEmpty() == true) ? "":subject.replace("#"+entry.getKey()+"#",entry.getValue());
            }
//            item.setRiid(Utils.getUUID());
            item.setRiid(riid);
            item.setTaskId(task_id);
            //trick
            item.setTaskType(noticeTasksEntity.getTaskType());      //主要用于后续流程区分
            item.setAddress(address);
            item.setMessage(message);       //设置替换URL之前的message。用于传递参数给后面流程,后面会用替换trackurl后的message覆盖。
            item.setSubject(subject);       //如果是sms,可以不用设置。
            item.setSendStatus(AppConstants.TASK_RESULT_STATUS_NEW);
            item.setSubmitTime(Jdate.getNowStrTime());
            List<TrackRecodeEntity> trackRecodeEntities = makeTrackRecodeInfo(item);

            slog.debug("track recode info"+trackRecodeEntities);
            //更换原始URL to track url
            item.setMessage(replaceMessageUrlToTrackUrl(item,trackRecodeEntities));
            //添加结尾隐藏的跟踪链接.邮件需要实现
            item.setMessage( addOpenTrackUrl(item,trackRecodeEntities));
            resultInfoList.add(item );
            for (TrackRecodeEntity trackRecodeEntity: trackRecodeEntities) {
                trackRecodeEntity.setMessageReplace(item.getMessage());
                all_trackRecodeEntities.add(trackRecodeEntity);
            }
        }
        slog.debug("get NoticeTasksResultEntity:"+resultInfoList);
        try {
            //这里对 track recode 表数据保存起来。主要是因为参数生成要在这里，也不好传出去。
            saveTrackRecodeBatch(all_trackRecodeEntities);
        }catch(Exception ee){
            slog.error("create track recode batch error:"+ee.getMessage());
            throw new AppException("0","create track recoder error");
        }
        return resultInfoList;
    }
    /**
     * 保存各个address的详细记录到notice_tasks_result_info中
     * 默认batch :100
     * @param lists
     * @return
     */
    public Boolean saveNoticeResultsBatch(List<NoticeTasksResultEntity> lists) throws AppException {
        slog.debug("save notice result batch begin :"+lists);
        Boolean st = true;
        int lsize = lists.size();
        float fsize = 100f;
        int isize = 100;
        int count = (int) Math.ceil(lists.size()/fsize);
        try {
            for (int i = 0; i < count; i++) {
                int end = (i + 1) * isize;
                if (end > lsize) {
                    end = lsize;
                }
                List<NoticeTasksResultEntity> patch = lists.subList(i * isize, end);
                st = tasksResultDao.createNoticeTaskResultBatch(patch);
            }
        }catch (Exception ee){
            Jlog.error("save notice result error:"+ee.getMessage());
            throw new AppException("0","save notice result error:"+ee.getMessage());
        }
        slog.debug("save notice result batch end");
        return st;
    }
    /**
     * 批量存到数据表notice_tasks_track_recode
     * @param lists
     * @return
     * @throws AppException
     */
    public Boolean saveTrackRecodeBatch(List<TrackRecodeEntity> lists) throws AppException {
        slog.debug("save track recode batch:"+lists);
        Boolean st = true;
        int lsize = lists.size();
        float fsize = 100f;
        int isize = 100;
        int count = (int) Math.ceil(lists.size()/fsize);
        try {
            for (int i = 0; i < count; i++) {
                int end = (i + 1) * isize;
                if (end > lsize) {
                    end = lsize;
                }
                List<TrackRecodeEntity> patch = lists.subList(i * isize, end);
                Jlog.debug("patch:" + patch);
                trackRecodeDao.addTrackRecodeInfoBatch(patch);
            }
        }catch (Exception ee){
            Jlog.error("save notice result error:"+ee.getMessage());
            throw new AppException("0","save notice result error:"+ee.getMessage());
        }
        return st;
    }

    /**
     * 更新任务状态
     * @param task_id
     * @param status new/initing/inited/sending/sended/等
     * @return
     */
    public Boolean updateNoticeTaskSatus(Integer task_id, String status){
        Boolean st = true;
        String update_time = Jdate.getNowStrTime();
        try {
            noticeTasksDao.updateNoticeTaskStatus(task_id, status, update_time);
        }catch(Exception ee){
            st = false;
            Jlog.error("update notice status error:"+ee.getMessage());
        }
        return st;
    }

    /**
     * 通过 taskId 获取多个附件属性 json 包括　fileName、downloadUrl
     * @param taskId
     * @return
     */
    public NoticeTasksEntity getAttachmentByTaskId(int taskId) {
        return noticeTasksDao.getAttachmentByTaskId(taskId);
    };

    /**
     * 通过 taskId统计没有发送或登录失败的个数
     * @return
     */
    public int countUnfixedResultByTaskId(int taskId) {
        int count = tasksResultDao.countUnfixedResultByTaskId(taskId, AppConstants.TASK_RESULT_STATUS_NEW, AppConstants.TASK_RESULT_STATUS_AUTH_FAILED, AppConstants.TASK_RESULT_STATUS_SENDING);
        return count;
    }

    public List<NoticeTasksEntity> getInitNoticeTasks(){
        List<NoticeTasksEntity> task_lists = noticeTasksDao.getNoticeTasksByStatus(AppConstants.TASK_STATUS_INIT);
        return task_lists;
    }

    /**
     * 批量更新task的状态
     * @param task_ids
     * @param status
     */
    public void updateTaskStatusBatch(Set<Integer> task_ids,String status) throws AppException {
        String update_time = Jdate.getNowStrTime();
        try{
            noticeTasksDao.updateNoticeTaskStatusBatch(task_ids,status,update_time);
        }catch (Exception ee){
            slog.error("update task status batch error:"+ee.getMessage());
            throw new AppException("0","update task status error");
        }
    }

    /**
     * 批量更新task result 的发送状态
     * @param task_ids
     * @param status
     * @throws AppException
     */
    public void updateTaskResultStatusBatch(List<String> riid_list,String status) throws AppException {
        String update_time = Jdate.getNowStrTime();
        try{
            tasksResultDao.updateNoticeTaskResultStatusBatch(riid_list,status,update_time);
        }catch (Exception ee){
            slog.error("update task result status batch error:"+ee.getMessage());
            throw new AppException("0","update task resutl status error");
        }
    }

    /**
     * 电话号码有误,使用下一个电话号码更新当前号码
     * @param newAddress
     * @param riid
     * @return
     */
    public void updateResultWithNextClientInfo(String newAddress, String riid) {
        tasksResultDao.updateResultWithNextClientInfo(newAddress, riid);
    };
}
