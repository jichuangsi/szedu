package cn.com.szedu.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "oplog")
public class OpLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String operatorName;
    private String opActionname;
    private String opAction;
    private long createdTime = new Date().getTime();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOpActionname() {
        return opActionname;
    }

    public void setOpActionname(String opActionname) {
        this.opActionname = opActionname;
    }

    public String getOpAction() {
        return opAction;
    }

    public void setOpAction(String opAction) {
        this.opAction = opAction;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public OpLog() {
    }

    public OpLog(String operatorName, String opActionname, String opAction) {
        this.operatorName = operatorName;
        this.opActionname = opActionname;
        this.opAction = opAction;
    }
}
