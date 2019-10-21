package cn.com.szedu.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "courseware")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class CourseWare {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String filename;//文件名
    private String teacherid;//老师id
    private String filegroup;//组名
    private String filepath;//路径

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getFilegroup() {
        return filegroup;
    }

    public void setFilegroup(String filegroup) {
        this.filegroup = filegroup;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
