package cn.com.szedu.model.teacher;

import cn.com.szedu.entity.TeacherInfo;

import java.util.List;

public class TeacherInfoModel{
       private String schoolIdCov;//学校封面
       private TeacherInfo teacherInfo;//老师基本信息
       private String teacherBirthday;

       public String getTeacherBirthday() {
              return teacherBirthday;
       }

       public void setTeacherBirthday(String teacherBirthday) {
              this.teacherBirthday = teacherBirthday;
       }

       private List<ClassModel> classModels;

       public List<ClassModel> getClassModels() {
              return classModels;
       }

       public void setClassModels(List<ClassModel> classModels) {
              this.classModels = classModels;
       }

       public String getSchoolIdCov() {
              return schoolIdCov;
       }

       public void setSchoolIdCov(String schoolIdCov) {
              this.schoolIdCov = schoolIdCov;
       }

       public TeacherInfo getTeacherInfo() {
              return teacherInfo;
       }

       public void setTeacherInfo(TeacherInfo teacherInfo) {
              this.teacherInfo = teacherInfo;
       }
       //List<>
}
