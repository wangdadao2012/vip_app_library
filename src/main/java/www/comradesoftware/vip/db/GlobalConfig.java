package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;

import java.util.List;

public class GlobalConfig extends DataSupport {
    private int id;
    private String Name;
    private String Value;
    private String version;
    private String launchPage;
    private List<Page> pages;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLaunchPage() {
        return launchPage;
    }

    public void setLaunchPage(String launchPage) {
        this.launchPage = launchPage;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
