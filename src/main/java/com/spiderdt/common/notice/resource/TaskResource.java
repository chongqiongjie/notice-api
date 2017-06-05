package com.spiderdt.common.notice.resource;

import com.spiderdt.common.notice.errorhander.AppException;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by fivebit on 2017/6/3.
 * 发送通知任务接口。包括创建任务，查看任务列表，查看任务详情
 */
@Component
@Path("/task")
@Produces({ MediaType.APPLICATION_JSON})
public class TaskResource {


    //@Path("")
    @POST
    public Response createTask() throws AppException {

    }
}
