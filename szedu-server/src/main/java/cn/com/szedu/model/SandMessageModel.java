package cn.com.szedu.model;

import java.util.List;

public class SandMessageModel {
    private Integer messageId;//消息id
    private List<IDNameModel> recipient;//接受者
    private String message;//消息内容
    private Integer reply;//回复消息id
    private String send;//是否发送
    private List<String> classId;

    public List<String> getClassId() {
        return classId;
    }

    public void setClassId(List<String> classId) {
        this.classId = classId;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public List<IDNameModel> getRecipient() {
        return recipient;
    }

    public void setRecipient(List<IDNameModel> recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReply() {
        return reply;
    }

    public void setReply(Integer reply) {
        this.reply = reply;
    }
}
