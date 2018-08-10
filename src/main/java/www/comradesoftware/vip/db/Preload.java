package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;

public class Preload extends DataSupport {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
