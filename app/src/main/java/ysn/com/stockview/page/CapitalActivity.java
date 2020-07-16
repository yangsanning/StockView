package ysn.com.stockview.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import ysn.com.stock.view.CapitalView;
import ysn.com.stockview.R;
import ysn.com.stockview.bean.CapitalTime;
import ysn.com.stockview.utils.JsonUtils;

/**
 * @Author yangsanning
 * @ClassName CapitalActivity
 * @Description 资金趋势图展示
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class CapitalActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CapitalView capitalView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital);

        setTitle(R.string.text_capital);

        CapitalTime newData = JsonUtils.getData((this), ("json/capital1.json"), CapitalTime.class);

        CapitalView capitalView1 = findViewById(R.id.capital_activity_view1);
        capitalView1.setDrawMainInFlow(true)
                .setDrawRetailInFlow(true)
                .setNewData(newData.getData());

        capitalView2 = findViewById(R.id.capital_activity_view2);
        capitalView2.setNewData(newData.getData());

        CheckBox checkBox1 = findViewById(R.id.capital_activity_check_box1);
        checkBox1.setOnCheckedChangeListener(this);

        CheckBox checkBox2 = findViewById(R.id.capital_activity_check_box2);
        checkBox2.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.capital_activity_check_box1:
                capitalView2.setDrawMainInFlow(isChecked);
                break;
            case R.id.capital_activity_check_box2:
                capitalView2.setDrawRetailInFlow(isChecked);
                break;
            default:
                break;
        }
    }
}