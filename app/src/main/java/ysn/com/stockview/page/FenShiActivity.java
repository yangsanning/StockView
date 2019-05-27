package ysn.com.stockview.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import ysn.com.stock.bean.FenShi;
import ysn.com.stock.bean.FenShiData;
import ysn.com.stock.view.FenShiView;
import ysn.com.stockview.R;
import ysn.com.stockview.bean.FenShiTime;
import ysn.com.stockview.utils.JsonUtils;

/**
 * @Author yangsanning
 * @ClassName FenShiActivity
 * @Description 分时图展示
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class FenShiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fen_shi);

        setTitle(R.string.text_fen_shi);

        FenShiView fenShiView1 = findViewById(R.id.fen_shi_activity_view1);
        fenShiView1.setData(timeToFenShi(JsonUtils.getData((this), ("json/fen_shi1.json"), FenShiTime.class)));

        FenShiView fenShiView2 = findViewById(R.id.fen_shi_activity_view2);
        fenShiView2.setData(timeToFenShi(JsonUtils.getData((this), ("json/fen_shi2.json"), FenShiTime.class)));
    }

    private FenShi timeToFenShi(FenShiTime time) {
        if (time == null) {
            return null;
        }
        FenShi fenShi = new FenShi();
        fenShi.setCode(time.getCode());
        fenShi.setLastClose(time.getSettlement());

        ArrayList<FenShiData> fenShiDataList = new ArrayList<>();
        for (FenShiTime.DataBean dataBean : time.getData()) {
            fenShiDataList.add(new FenShiData(dataBean.getDateTime(), dataBean.getTrade(),
                    dataBean.getAvgPrice(), dataBean.getVolume()));
        }
        fenShi.setData(fenShiDataList);
        return fenShi;
    }
}