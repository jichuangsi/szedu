package cn.com.szedu.entity.IntermediateTable;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "system_user")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class SystemUser {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String sid;
    private String uid;
    private String alreadyRead;//是否已读,True已读

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }*/
    public String getAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(String alreadyRead) {
        this.alreadyRead = alreadyRead;
    }
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
