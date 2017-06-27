package com.spiderdt.common.notice.resource;

import com.spiderdt.common.notice.common.Slog;
import com.spiderdt.common.notice.common.Utils;
import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import com.spiderdt.common.notice.errorhander.AppException;
import com.spiderdt.common.notice.resource.interfactBean.TaskInput;
import com.spiderdt.common.notice.service.NoticeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
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
//    @Context
    //private HttpServletRequest request;
    @Autowired
    private NoticeTaskService noticeTaskService;

    @Autowired
    Slog slog;

    /**
     * 创建一个通知任务
     * @return
     * @throws AppException
     */
    //@Path("")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response createTask(TaskInput task_param ) throws AppException {
        //String authorization = request.getHeader("Authorization");
        slog.info("create task begin:"+task_param.toString());
        //get user info
        NoticeTasksEntity noticeTasksEntity = new NoticeTasksEntity();
        noticeTasksEntity.initByTaskInput(task_param);
        Boolean st = noticeTaskService.createNoticeTask(noticeTasksEntity);
        slog.info("create task end:st:"+st+" param:"+task_param.toString());
        return Response.status(Response.Status.CREATED)// 201
                .entity(Utils.getRespons()).build();

    }

    /**
     * 通过job_id获取用户名和address
     */
    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response getAddressesFromJobId(String job_id) throws AppException {
        String address = noticeTaskService.getAddressesFromJobId(job_id,"email");
        slog.info("getAddressesFromJobId:" + address);
        return Response.status(Response.Status.CREATED)
                .entity(Utils.getRespons()).build();

    }
}
