package cn.com.szedu.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "system_message")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class SystemMessage {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    //@Column(length = 255)
    private String id;
    private String title;//系统消息标题
    private String content;//内容
    private String sendId;//管理员
    private String sendName;//
    private String schoolId;//学校id
   /* private String alreadyRead;//是否已读,True已读*/
    private Integer examine;//审核1未审核，2已审核，3审核未通过
    private long time=new Date().getTime();
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   /* public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

   /* public String getAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(String alreadyRead) {
        this.alreadyRead = alreadyRead;
    }*/

   /* public Integer getCheck() {
        return check;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }*/

    public Integer getExamine() {
        return examine;
    }

    public void setExamine(Integer examine) {
        this.examine = examine;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
