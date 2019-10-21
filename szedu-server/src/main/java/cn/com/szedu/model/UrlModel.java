package cn.com.szedu.model;

import cn.com.szedu.entity.UrlRelation;

import java.util.List;

public class UrlModel {
    private List<UrlRelation> relationList;

    public List<UrlRelation> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<UrlRelation> relationList) {
        this.relationList = relationList;
    }
}
