package cn.com.szedu.model.teacher;

import java.util.List;

public class PushResourceModel {
    private String resourceId;
    private List<String> classid;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public List<String> getClassid() {
        return classid;
    }

    public void setClassid(List<String> classid) {
        this.classid = classid;
    }
}
