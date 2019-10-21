package cn.com.szedu.model;

import javax.validation.constraints.Pattern;

public class StudentModel  {

    private String id;
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$",message = "账号必须是4到16位（字母，数字，下划线，减号）")
    private String account;
    private String name;
    @Pattern(regexp = "^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$",message = "密码最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符")
    private String pwd;
    private String sex;
    private String status;
    private String phone;
    private String schoolName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
