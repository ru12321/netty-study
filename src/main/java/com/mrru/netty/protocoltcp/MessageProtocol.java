package com.mrru.netty.protocoltcp;

/**
 * @className: MessageProtocol
 * @author: 茹某
 * @date: 2021/7/29 15:24
 **/
public class MessageProtocol
{
    private int len;
    private byte[] content;

    public byte[] getContent()
    {
        return content;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    public int getLen()
    {
        return len;
    }

    public void setLen(int len)
    {
        this.len = len;
    }
}
