package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

public class Preload extends LitePalSupport {
    private String value;

    @Override
    public String toString() {
        return "Preload{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
