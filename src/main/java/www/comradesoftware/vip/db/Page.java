package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;

public class Page extends DataSupport {
    private String page;
    private String path;
    private String name;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
