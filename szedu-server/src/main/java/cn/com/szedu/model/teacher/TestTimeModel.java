package cn.com.szedu.model.teacher;

public class TestTimeModel {
    private String id;
    private long startTime;
    private long endTime;
    private String timeLength;
    private String tiqian;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getTiqian() {
        return tiqian;
    }

    public void setTiqian(String tiqian) {
        this.tiqian = tiqian;
    }
}
