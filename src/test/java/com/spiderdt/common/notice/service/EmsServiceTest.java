package com.spiderdt.common.notice.service;

import com.spiderdt.common.AppTest;
import com.spiderdt.common.notice.entity.SmsTemplateEntity;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiong on 2017/6/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class EmsServiceTest extends TestCase
{
    @Resource
    UpdateSmsService updateSmsService;




    @Test
    public void insertSms(){
        SmsTemplateEntity  template = new SmsTemplateEntity();
        template.setMessage_type("sms");
        template.setUser_id("chong");
        template.setTemp_name("chong");
        template.setTemplate_content("【知助数据】XXX618年中狂欢购！6月9日运动户外低至五折，阿迪低至99元，领券更优惠！ 狂戳 dc.jd.com/dMmkQQ 回复BK退订");
       updateSmsService.insertSmsTemplate(template);
    }


    @Test
    public void getSms(){
        String type = "sms";
        String user_id = "qiong";
       // System.out.println(updateSmsService.getSmsTemplate(type,user_id));
        for(int i=0;i<updateSmsService.getSmsTemplate(type,user_id).size();i++){
            System.out.println(updateSmsService.getSmsTemplate(type,user_id).get(i).getTid());
            System.out.println(updateSmsService.getSmsTemplate(type,user_id).get(i).getTemplate_content());
            System.out.println(updateSmsService.getSmsTemplate(type,user_id).get(i).getTemp_name());
        }
    }


    @Test
    public void update(){
        int id = 15;
        String template_content = "【知助数据】";
        System.out.println(updateSmsService.updateSmsTemplate(id,template_content));
    }

    @Test
    public void delete(){
        Integer id = 14;
        System.out.println(updateSmsService.deleteSmsTemplate(id));
    }

}
