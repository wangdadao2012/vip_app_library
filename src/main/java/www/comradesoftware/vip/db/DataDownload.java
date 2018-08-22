package www.comradesoftware.vip.db;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.sql.Date;

public class DataDownload extends LitePalSupport {
    private int id;
    private Date CreateTime;
    private int Downloaded;//1:下载成功
    private int Upgrade;//1: 解密成功，升级成功
    private String MD5="";
    private String ZipPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }

    public int getDownloaded() {
        return Downloaded;
    }

    public void setDownloaded(int downloaded) {
        Downloaded = downloaded;
    }

    public int getUpgrade() {
        return Upgrade;
    }

    public void setUpgrade(int upgrade) {
        Upgrade = upgrade;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getZipPath() {
        return ZipPath;
    }

    public void setZipPath(String zipPath) {
        ZipPath = zipPath;
    }
}
