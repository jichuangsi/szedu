package cn.com.szedu.model.teacher;

import java.util.List;

public class PushResourceModel {
    private String resourceId;
    private List<String> classId;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public List<String> getClassId() {
        return classId;
    }

    public void setClassId(List<String> classId) {
        this.classId = classId;
    }
}
