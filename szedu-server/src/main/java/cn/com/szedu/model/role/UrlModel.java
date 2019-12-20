package cn.com.szedu.model.role;

import cn.com.szedu.entity.IntermediateTable.UrlRelation;

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
