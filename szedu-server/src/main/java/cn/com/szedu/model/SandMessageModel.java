package cn.com.szedu.model;

import java.util.List;

public class SandMessageModel {
    private Integer messageId;//消息id
    private List<IDNameModel> recipient;//接受者
    private String message;//消息内容
    private String reply;//回复消息id

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

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
