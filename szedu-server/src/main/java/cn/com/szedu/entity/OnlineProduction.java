package cn.com.szedu.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "onlineProduction")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class OnlineProduction {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String operatorId;//操作人id
    private String operatorName;//操作名字
    private String resourseType;//资源类型
    private String resourseTypeId;
    private String subject;//科目
    private String lablleName;//上传标签
    private String  cover;//封面
    private String content;//简介
    private  String couseId;
    private String name;//名称资源类型+科目+标签+操作者
    private long updateTime;//修改时间
    private long createtIme;//新增时间
    private String path;//打开资源路劲

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getResourseType() {
        return resourseType;
    }

    public void setResourseType(String resourseType) {
        this.resourseType = resourseType;
    }

    public String getResourseTypeId() {
        return resourseTypeId;
    }

    public void setResourseTypeId(String resourseTypeId) {
        this.resourseTypeId = resourseTypeId;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLablleName() {
        return lablleName;
    }

    public void setLablleName(String lablleName) {
        this.lablleName = lablleName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCouseId() {
        return couseId;
    }

    public void setCouseId(String couseId) {
        this.couseId = couseId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreatetIme() {
        return createtIme;
    }

    public void setCreatetIme(long createtIme) {
        this.createtIme = createtIme;
    }
}
