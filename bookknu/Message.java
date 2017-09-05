package myhome.bookknu;

/**
 * Created by sungw on 2017-08-28.
 */

public class Message {


    String m_send; //send
    String m_receive; //receive
    String m_content; //내용
    String m_date;//데이트

    public Message(String send,String receive,String content,String date)
    {
        this.m_send = send;
        this.m_receive = receive;
        this.m_content = content;
        this.m_date = date;

    }

    public String getM_send()
    {
        return m_send;
    }

    public String getM_receive()
    {
        return m_receive;
    }

    public String getM_content()
    {
        return m_content;
    }

    public String getM_date() { return  m_date;}


}
