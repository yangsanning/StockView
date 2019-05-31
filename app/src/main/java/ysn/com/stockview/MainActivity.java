package ysn.com.stockview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ysn.com.stockview.page.CapitalActivity;
import ysn.com.stockview.page.FenShiActivity;
import ysn.com.stockview.page.MiniFenShiActivity;

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

        findViewById(R.id.main_activity_fen_shi).setOnClickListener(this);
        findViewById(R.id.main_activity_capital).setOnClickListener(this);
        findViewById(R.id.main_activity_mini_fen_shi).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_fen_shi:
                startActivity(new Intent((this), FenShiActivity.class));
                break;
            case R.id.main_activity_capital:
                startActivity(new Intent((this), CapitalActivity.class));
                break;
            case R.id.main_activity_mini_fen_shi:
                startActivity(new Intent((this), MiniFenShiActivity.class));
                break;
            default:
                break;
        }
    }
}