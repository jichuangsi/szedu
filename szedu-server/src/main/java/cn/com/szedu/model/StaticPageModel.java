package cn.com.szedu.model;

public class StaticPageModel {
    private Integer pageId;
    private String pageName;
    private String pageUrl;
    private Integer parentNode;

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Integer getParentNode() {
        return parentNode;
    }

    public void setParentNode(Integer parentNode) {
        this.parentNode = parentNode;
    }
}
