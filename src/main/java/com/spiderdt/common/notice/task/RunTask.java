package com.spiderdt.common.notice.task;

/**
 * Created by fivebit on 2017/5/23.
 */
public interface RunTask extends Runnable,Cloneable{
    public void setMsg(Object msg);
    public Object clone();
}
