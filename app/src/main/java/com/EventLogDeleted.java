package com;

/**
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
