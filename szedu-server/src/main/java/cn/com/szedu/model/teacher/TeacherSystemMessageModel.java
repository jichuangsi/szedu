package cn.com.szedu.model.teacher;
//老师系统信息
public class TeacherSystemMessageModel {

    private String oplogId;
    private String operatorName;//操作者
    private String opActionname;//大功能名字
    private String opAction;//小功能名字
    private long createdTime;

    public String getOplogId() {
        return oplogId;
    }

    public void setOplogId(String oplogId) {
        this.oplogId = oplogId;
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
}
