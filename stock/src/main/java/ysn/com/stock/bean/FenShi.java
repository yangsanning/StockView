package ysn.com.stock.bean;

import java.util.List;

/**
 * @Author yangsanning
 * @ClassName FenShi
 * @Description 分时bean
 * @Date 2019/5/4
 * @History 2019/5/4 author: description:
 */
public class FenShi {

    /**
     * code : sh000001
     * data : [{"dateTime":"201808140930","trade":2780.74,"volume":2693903},{"dateTime":"201808140931","trade":2782.15,"volume":1232986}]
     */

    private String code;
    private float lastClose;
    private List<FenShiData> data;

    public FenShi() {
    }

    public FenShi(String code, float lastClose, List<FenShiData> data) {
        this.code = code;
        this.lastClose = lastClose;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FenShiData> getData() {
        return data;
    }

    public void setData(List<FenShiData> data) {
        this.data = data;
    }

    public float getLastClose() {
        return lastClose;
    }

    public void setLastClose(float lastClose) {
        this.lastClose = lastClose;
    }
}
