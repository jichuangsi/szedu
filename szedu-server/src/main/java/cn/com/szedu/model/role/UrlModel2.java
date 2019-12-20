package cn.com.szedu.model.role;

import java.util.Map;

public class UrlModel2 {
    private String nodeName;
    private Map<String,String> pageUrl;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Map<String, String> getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(Map<String, String> pageUrl) {
        this.pageUrl = pageUrl;
    }
}
