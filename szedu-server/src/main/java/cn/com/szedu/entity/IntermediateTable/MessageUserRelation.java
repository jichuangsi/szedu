package cn.com.szedu.entity.IntermediateTable;

import javax.persistence.*;

@Entity
@Table(name = "messageuserrelation")
public class MessageUserRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//
    private String uId;//用户id
    private Integer mId;//聊天记录id
   /* private String alreadyRead;//是否已读,True已读*/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

   /* public String getAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(String alreadyRead) {
        this.alreadyRead = alreadyRead;
    }*/
}
