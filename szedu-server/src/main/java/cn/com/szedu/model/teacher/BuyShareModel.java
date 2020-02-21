package cn.com.szedu.model.teacher;

import cn.com.szedu.entity.CourseWare;
import cn.com.szedu.entity.IntermediateTable.PurchasedResources;

import java.util.List;

public class BuyShareModel {//购买分享操作记录
    private List<PurchasedResources> purchasedResourcesList;//购买资源表
    private List<CourseWare> shareCourseList;//分享资源表

    public List<PurchasedResources> getPurchasedResourcesList() {
        return purchasedResourcesList;
    }

    public void setPurchasedResourcesList(List<PurchasedResources> purchasedResourcesList) {
        this.purchasedResourcesList = purchasedResourcesList;
    }

    public List<CourseWare> getShareCourseList() {
        return shareCourseList;
    }

    public void setShareCourseList(List<CourseWare> shareCourseList) {
        this.shareCourseList = shareCourseList;
    }
}
