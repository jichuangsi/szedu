package cn.com.szedu.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "integralrecord")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class IntegralRecord {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String function;//大功能
    private String functionName;//小功能
    private String operatorId;//操作人id
    private String operatorName;//操作名字
    private Integer integra;//积分
  /*  private String message;//提示信息*/
    @Column(name = "createTime")
    private long createTime;//操作时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getIntegra() {
        return integra;
    }

    public void setIntegra(Integer integra) {
        this.integra = integra;
    }

  /*  public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }*/

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public IntegralRecord(String id,String function, String functionName, String operatorId, String operatorName, Integer integra, long createTime) {
        this.id=id;
        this.function = function;
        this.functionName = functionName;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.integra = integra;
        this.createTime = createTime;
    }

    public IntegralRecord() {
    }
}
