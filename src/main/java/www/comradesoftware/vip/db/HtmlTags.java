package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;

public class HtmlTags extends DataSupport {
    private String Tag="";

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
