package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

public class PageConfig extends LitePalSupport {
    private int id;
    private String Type;
    private String Value;
    private String navBarBgColor;
    private String navBarTextColor;
    private TabBar tabBar;

    public String getNavBarBgColor() {
        return navBarBgColor;
    }

    public void setNavBarBgColor(String navBarBgColor) {
        this.navBarBgColor = navBarBgColor;
    }

    public String getNavBarTextColor() {
        return navBarTextColor;
    }

    public void setNavBarTextColor(String navBarTextColor) {
        this.navBarTextColor = navBarTextColor;
    }

    public TabBar getTabBar() {
        return tabBar;
    }

    public void setTabBar(TabBar tabBar) {
        this.tabBar = tabBar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

}
