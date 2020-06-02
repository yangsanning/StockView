package ysn.com.stockview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import ysn.com.stockview.page.CapitalActivity;
import ysn.com.stockview.page.FenShiActivity;
import ysn.com.stockview.page.FiveDayFenShiActivity;
import ysn.com.stockview.page.MiniFenShiActivity;
import ysn.com.stockview.page.ProfitLossActivity;

/**
 * @Author yangsanning
 * @ClassName MainActivity
 * @Description 一句话概括作用
 * @Date 2019/5/4
 * @History 2019/5/4 author: description:
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout rootLayout = findViewById(R.id.main_activity_root);
        int childCount = rootLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            rootLayout.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_fen_shi:
                startActivity(new Intent((this), FenShiActivity.class));
                break;
            case R.id.main_activity_five_day_fen_shi:
                startActivity(new Intent((this), FiveDayFenShiActivity.class));
                break;
            case R.id.main_activity_capital:
                startActivity(new Intent((this), CapitalActivity.class));
                break;
            case R.id.main_activity_mini_fen_shi:
                startActivity(new Intent((this), MiniFenShiActivity.class));
                break;
            case R.id.main_activity_profit_loss:
                startActivity(new Intent((this), ProfitLossActivity.class));
                break;
            default:
                break;
        }
    }
}