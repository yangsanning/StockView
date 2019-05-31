package ysn.com.stockview.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import ysn.com.stock.bean.FenShi;
import ysn.com.stock.bean.FenShiData;
import ysn.com.stock.view.MiniFenShiView;
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
public class MiniFenShiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_fen_shi);

        setTitle(R.string.text_mini_fen_shi);

        MiniFenShiView miniFenShiView1 = findViewById(R.id.mini_fen_shi_activity_view1);
        miniFenShiView1.setNewData(timeToFenShi(JsonUtils.getData((this), ("json/fen_shi2.json"), FenShiTime.class)));

        MiniFenShiView miniFenShiView2 = findViewById(R.id.mini_fen_shi_activity_view2);
        miniFenShiView2.setNewData(timeToFenShi(JsonUtils.getData((this), ("json/fen_shi3.json"), FenShiTime.class)));

        MiniFenShiView miniFenShiView3 = findViewById(R.id.mini_fen_shi_activity_view3);
        miniFenShiView3.setNewData(timeToFenShi(JsonUtils.getData((this), ("json/fen_shi4.json"), FenShiTime.class)));

        MiniFenShiView miniFenShiView4 = findViewById(R.id.mini_fen_shi_activity_view4);
        miniFenShiView4.setNewData(timeToFenShi(JsonUtils.getData((this), ("json/fen_shi5.json"), FenShiTime.class)));
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