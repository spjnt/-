package bar.barcode.db;

import org.litepal.crud.LitePalSupport;

public class UpHistory extends LitePalSupport {

    private String code;
    private String up_time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUp_time() {
        return up_time;
    }

    public void setUp_time(String up_time) {
        this.up_time = up_time;
    }
}
