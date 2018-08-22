package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class TabBar extends LitePalSupport {
    private String color;
    private String selectColor;
    private String position;
    private List<TabList> tabLists;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSelectColor() {
        return selectColor;
    }

    public void setSelectColor(String selectColor) {
        this.selectColor = selectColor;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<TabList> getTabLists() {
        return tabLists;
    }

    public void setTabLists(List<TabList> tabLists) {
        this.tabLists = tabLists;
    }
}
