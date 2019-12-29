package cn.com.szedu.model.teacher;

public class MyAllLessionModel {
     private Integer subjectId;
    private String lessionType;
    private long time;
    private int pageNum;
    private int pageSize;

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
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
