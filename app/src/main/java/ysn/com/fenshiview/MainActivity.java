package ysn.com.fenshiview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ysn.com.fenshiview.bean.Time;
import ysn.com.fenshiview.utils.JsonUtils;
import ysn.com.stock.bean.FenShi;
import ysn.com.stock.bean.FenShiData;
import ysn.com.stock.view.FenShiView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FenShiView fenShiView1 = findViewById(R.id.main_activity_fen_shi_view1);
        fenShiView1.setData(timeToFenShi(JsonUtils.getData((this), ("json/fen_shi1.json"))));

        FenShiView fenShiView2 = findViewById(R.id.main_activity_fen_shi_view2);
        fenShiView2.setData(timeToFenShi(JsonUtils.getData((this), ("json/fen_shi2.json"))));
    }

    private FenShi timeToFenShi(Time time) {
        FenShi fenShi = new FenShi();
        if (time == null) {
            return null;
        }
        fenShi.setCode(time.getCode());
        fenShi.setLastClose(time.getSettlement());

        ArrayList<FenShiData> fenShiDataList = new ArrayList<>();
        for (Time.DataBean dataBean : time.getData()) {
            fenShiDataList.add(new FenShiData(dataBean.getDateTime(), dataBean.getTrade(),
                    dataBean.getAvgPrice(), dataBean.getVolume()));
        }
        fenShi.setData(fenShiDataList);
        return fenShi;
    }
}