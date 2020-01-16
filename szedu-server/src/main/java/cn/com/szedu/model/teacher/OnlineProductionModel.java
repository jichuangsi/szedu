package cn.com.szedu.model.teacher;

public class OnlineProductionModel {

    private String id;
    private String resourseType;//资源类型
    private String resourseTypeId;
    private String subjectId;//科目
    private String subject;//
    private String labbleId;//上传标签
    private String lablleName;
    private String  cover;//封面
    private String content;//简介
    private  String couseId;
    private String path;//打开路劲

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCouseId() {
        return couseId;
    }

    public void setCouseId(String couseId) {
        this.couseId = couseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLabbleId() {
        return labbleId;
    }

    public void setLabbleId(String labbleId) {
        this.labbleId = labbleId;
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
}
