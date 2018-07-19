package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;

public class SystemSetting extends DataSupport {
    private int id;
    private String Name;
    private String Value;

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
