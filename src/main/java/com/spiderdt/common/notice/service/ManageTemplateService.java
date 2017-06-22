package com.spiderdt.common.notice.service;


import com.spiderdt.common.notice.dao.ManageTemplateDao;
import com.spiderdt.common.notice.entity.SmsTemplateEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by qiong on 2017/6/13.
 */
@Service("UpdateSmsService")
public class ManageTemplateService {
    @Resource
    ManageTemplateDao updateSmsTemplateDao;


    /**
     * 往数据增加模板
     */
    //@Transactional(propagation = Propagation.REQUIRED)
    public  boolean insertSmsTemplate(SmsTemplateEntity template){
            //Boolean valid = updateSmsTemplateDao.updateValid(template.getMessage_type(),template.getUser_id());
            Boolean temp = updateSmsTemplateDao.insertSmsTemplate(template);
            return temp;
           // return valid;
    }

    /**
     * 删除数据库里的模板，只将状态改成0，并不删除这条记录
     * @param tid
     */
        public boolean deleteSmsTemplate(Integer tid){
            boolean status = updateSmsTemplateDao.updateValid(tid);
            return status;
        }
//
    /**
     * 通过id更新短信文本
     */
     public boolean updateSmsTemplate(Integer id,String template_content){
         return updateSmsTemplateDao.updateSmsTemplate(id,template_content);
     }

    /**
     *通过id查询短信模板
     */
    public List<SmsTemplateEntity> getSmsTemplate(String message_type,String user_id){
        return updateSmsTemplateDao.getSmsTemplate(message_type,user_id);
    }
}
