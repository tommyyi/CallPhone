package com;

/**
 * Created by 易剑锋 on 2017/7/4.
 */

public class EventLogDeleted
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

    public EventLogDeleted(String msgBody)
    {
        mMsgBody = msgBody;
    }
}
