package com.spiderdt.common.notice.dao;

import com.spiderdt.common.notice.entity.SmsTemplateEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.ws.rs.PathParam;
import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiong on 2017/6/13.
 */
@Repository
public interface ManageTemplateDao {
    /**
     * 删除模板时将is_vaild设为0
     * @param tid
     * @return
     */
    public boolean updateValid(@Param("tid") Integer tid);

    /**
     * insert一条模板数据
     * @param smsTemplateEntity
     * @return
     */
    public boolean insertSmsTemplate(@Param("smsTemplateEntity") SmsTemplateEntity smsTemplateEntity);

    /**
     *
     * @param message_type
     * @param user_id
     * @return
     */
    public List<SmsTemplateEntity> getSmsTemplate(@Param("message_type") String message_type, @Param("user_id") String user_id);
    public boolean deleteSmsTemplate(@Param("tid") Integer tid);
    public boolean updateSmsTemplate(@Param("tid") Integer tid,@Param("template_content") String template_content);
    //通过类型和valid =1 查询正在使用的模板内容


    //通过类型和user_id查询用户的


}
