package cn.com.szedu.model;

import cn.com.szedu.entity.CourseWare;
import org.hibernate.validator.constraints.EAN;

import java.util.ArrayList;
import java.util.List;

public class CourseResouceModel {
    private List<CourseWare> ppt=new ArrayList<CourseWare>();
    private List<CourseWare> vedio=new ArrayList<CourseWare>();
    private List<CourseWare> shi=new ArrayList<CourseWare>();
    private List<CourseWare> pic=new ArrayList<CourseWare>();

    public List<CourseWare> getPpt() {
        return ppt;
    }

    public void setPpt(List<CourseWare> ppt) {
        this.ppt = ppt;
    }

    public List<CourseWare> getVedio() {
        return vedio;
    }

    public void setVedio(List<CourseWare> vedio) {
        this.vedio = vedio;
    }

    public List<CourseWare> getShi() {
        return shi;
    }

    public void setShi(List<CourseWare> shi) {
        this.shi = shi;
    }

    public List<CourseWare> getPic() {
        return pic;
    }

    public void setPic(List<CourseWare> pic) {
        this.pic = pic;
    }
}
