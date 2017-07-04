package com;

/**
 * Created by 易剑锋 on 2017/7/4.
 */

public class LogDeleted
{
    public String getMsgBody()
    {
        return mMsgBody;
    }

    public void setMsgBody(String msgBody)
    {
        mMsgBody = msgBody;
    }

    private String mMsgBody;

    public LogDeleted(String msgBody)
    {
        mMsgBody = msgBody;
    }
}
