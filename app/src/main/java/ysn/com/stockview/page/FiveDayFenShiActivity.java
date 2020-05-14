package ysn.com.stockview.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import ysn.com.stock.view.FiveDayFenShiView;
import ysn.com.stockview.R;
import ysn.com.stockview.bean.FenShiTime;
import ysn.com.stockview.utils.JsonUtils;
import ysn.com.stockview.widget.interceptor.FiveDayFenShiInterceptor;

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

        List<FenShiTime> dataList = JsonUtils.getDataList((this), ("json/five_day_fen_shi_top_four.json"));
        dataList.add(JsonUtils.getData((this), ("json/five_day_fen_shi_last.json"), FenShiTime.class));

        FiveDayFenShiView fiveDayFenShiView1 = findViewById(R.id.five_day_fen_shi_activity_view1);
        FiveDayFenShiView fiveDayFenShiView2 = findViewById(R.id.five_day_fen_shi_activity_view2);

        fiveDayFenShiView1.setFenShiUnitInterceptor(new FiveDayFenShiInterceptor());
        fiveDayFenShiView1.setData(dataList);

        dataList.remove(0);
        fiveDayFenShiView2.setFenShiUnitInterceptor(new FiveDayFenShiInterceptor());
        fiveDayFenShiView2.setData(dataList);
    }
}