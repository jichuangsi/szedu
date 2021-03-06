package cn.com.szedu.model;

import cn.com.szedu.entity.IntermediateTable.SchoolCurriculumRelation;
import cn.com.szedu.entity.IntermediateTable.SchoolUserRelation;

import java.util.Date;
import java.util.List;

public class SchoolInfoModel {
    private String id;
    private String schoolName;
    private String address;//地址
    private String schoolMotto;//校训
    private String type;
    private String contacts;//联系人
    private String contactsJob;//联系人职务
    private String phone;//联系方式
    private Date buyTime;//订购时间
    private String status;//使用状态
    private String remarks;//备注
    private List<SchoolCurriculumRelation> schoolCurriculumRelations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchoolMotto() {
        return schoolMotto;
    }

    public void setSchoolMotto(String schoolMotto) {
        this.schoolMotto = schoolMotto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsJob() {
        return contactsJob;
    }

    public void setContactsJob(String contactsJob) {
        this.contactsJob = contactsJob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<SchoolCurriculumRelation> getSchoolCurriculumRelations() {
        return schoolCurriculumRelations;
    }

    public void setSchoolCurriculumRelations(List<SchoolCurriculumRelation> schoolCurriculumRelations) {
        this.schoolCurriculumRelations = schoolCurriculumRelations;
    }
}
