package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;

public class TabList extends DataSupport{
    private String page;
    private String iconXe;
    private String text;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getIconXe() {
        return iconXe;
    }

    public void setIconXe(String iconXe) {
        this.iconXe = iconXe;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
