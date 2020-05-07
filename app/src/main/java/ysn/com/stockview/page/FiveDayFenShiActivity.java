package ysn.com.stockview.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ysn.com.stock.view.FenShiView;
import ysn.com.stock.view.FiveDayFenShiView;
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
public class FiveDayFenShiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_day_fen_shi);

        setTitle(R.string.text_five_day_fen_shi);

        FiveDayFenShiView fiveDayFenShiView = findViewById(R.id.five_day_fen_shi_activity_view);
        fiveDayFenShiView.setData1(JsonUtils.getData((this), ("json/fen_shi1.json"), FenShiTime.class));
        fiveDayFenShiView.setData2(JsonUtils.getData((this), ("json/fen_shi2.json"), FenShiTime.class));
        fiveDayFenShiView.setData3(JsonUtils.getData((this), ("json/fen_shi3.json"), FenShiTime.class));
        fiveDayFenShiView.setData4(JsonUtils.getData((this), ("json/fen_shi4.json"), FenShiTime.class));
        fiveDayFenShiView.setData5(JsonUtils.getData((this), ("json/fen_shi5.json"), FenShiTime.class));
    }
}