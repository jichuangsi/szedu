package cn.com.szedu.model.teacher;

public class MyAllLessionModel {
     private String subjectId;
    private String lessionType;
    private String timeName;
    private long time;
    private int pageNum;
    private int pageSize;

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getLessionType() {
        return lessionType;
    }

    public void setLessionType(String lessionType) {
        this.lessionType = lessionType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
