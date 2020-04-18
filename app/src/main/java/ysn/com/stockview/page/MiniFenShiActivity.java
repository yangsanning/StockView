package ysn.com.stockview.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ysn.com.stock.view.MiniFenShiView;
import ysn.com.stockview.R;
import ysn.com.stockview.bean.FenShiTime;
import ysn.com.stockview.utils.JsonUtils;
import ysn.com.stockview.widget.adapter.MiniFenShiAdapter;
import ysn.com.stockview.widget.view.FlipperRecyclerView;

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

        List<FenShiTime> fenShiList = new ArrayList<>();
        fenShiList.add(JsonUtils.getData((this), ("json/fen_shi2.json"), FenShiTime.class));
        fenShiList.add(JsonUtils.getData((this), ("json/fen_shi3.json"), FenShiTime.class));
        fenShiList.add(JsonUtils.getData((this), ("json/fen_shi4.json"), FenShiTime.class));
        fenShiList.add(JsonUtils.getData((this), ("json/fen_shi5.json"), FenShiTime.class));

        FlipperRecyclerView recyclerViewFlipper = findViewById(R.id.mini_fen_shi_activity_flipper_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewFlipper.setLayoutManager(layoutManager);
        recyclerViewFlipper.setAdapter(new MiniFenShiAdapter(fenShiList, this));

        MiniFenShiView miniFenShiView1 = findViewById(R.id.mini_fen_shi_activity_view1);
        TextView codeTextView1 = findViewById(R.id.mini_fen_shi_activity_code1);
        FenShiTime fenShiTime1 = fenShiList.get(0);
        miniFenShiView1.setNewData(fenShiTime1);
        codeTextView1.setText(fenShiTime1.getCode());

        MiniFenShiView miniFenShiView2 = findViewById(R.id.mini_fen_shi_activity_view2);
        TextView codeTextView2 = findViewById(R.id.mini_fen_shi_activity_code2);
        FenShiTime fenShiTime2 = fenShiList.get(1);
        miniFenShiView2.setNewData(fenShiTime2);
        codeTextView2.setText(fenShiTime2.getCode());

        MiniFenShiView miniFenShiView3 = findViewById(R.id.mini_fen_shi_activity_view3);
        TextView codeTextView3 = findViewById(R.id.mini_fen_shi_activity_code3);
        FenShiTime fenShiTime3 = fenShiList.get(2);
        miniFenShiView3.setNewData(fenShiTime3);
        codeTextView3.setText(fenShiTime3.getCode());

        MiniFenShiView miniFenShiView4 = findViewById(R.id.mini_fen_shi_activity_view4);
        TextView codeTextView4 = findViewById(R.id.mini_fen_shi_activity_code4);
        FenShiTime fenShiTime4 = fenShiList.get(3);
        miniFenShiView4.setNewData(fenShiTime4);
        codeTextView4.setText(fenShiTime4.getCode());
    }
}