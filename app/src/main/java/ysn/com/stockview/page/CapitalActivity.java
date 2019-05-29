package ysn.com.stockview.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import ysn.com.stock.bean.Capital;
import ysn.com.stock.bean.CapitalData;
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

        Capital newData = convert(JsonUtils.getData((this), ("json/capital1.json"), CapitalTime.class));

        CapitalView capitalView1 = findViewById(R.id.capital_activity_view1);
        capitalView1.setDrawMainInFlow(true)
                .setDrawRetailInFlow(true)
                .setNewData(newData);

        capitalView2 = findViewById(R.id.capital_activity_view2);
        capitalView2.setNewData(newData);

        CheckBox checkBox1 = findViewById(R.id.capital_activity_check_box1);
        checkBox1.setOnCheckedChangeListener(this);

        CheckBox checkBox2 = findViewById(R.id.capital_activity_check_box2);
        checkBox2.setOnCheckedChangeListener(this);
    }

    private Capital convert(CapitalTime capitalTime) {
        if (capitalTime == null) {
            return null;
        }
        Capital capital = new Capital();
        ArrayList<CapitalData> capitalDataList = new ArrayList<>();
        for (CapitalTime.DataBean dataBean : capitalTime.getData()) {
            CapitalData data = new CapitalData(dataBean.getPrice(), dataBean.getFinanceInFlow(),
                    dataBean.getMainInFlow(), dataBean.getRetailInFlow());
            capitalDataList.add(data);
            capital.findPriceExtremum(dataBean.getPrice())
                    .checkInFlowExtremum(data.getFinanceInFlow(), data.getMainInFlow(), data.getRetailInFlow());
        }
        ArrayList<Float> priceCoordinate = new ArrayList<>();
        priceCoordinate.add(getPriceCoordinate(capital, (0)));
        priceCoordinate.add(getPriceCoordinate(capital, (1 / 4f)));
        priceCoordinate.add(getPriceCoordinate(capital, (2 / 4f)));
        priceCoordinate.add(getPriceCoordinate(capital, (3 / 4f)));
        priceCoordinate.add(getPriceCoordinate(capital, (1)));

        ArrayList<Float> inFlowCoordinate = new ArrayList<>();
        inFlowCoordinate.add(getInfoFlowCoordinate(capital, (0)));
        inFlowCoordinate.add(getInfoFlowCoordinate(capital, (1 / 4f)));
        inFlowCoordinate.add(getInfoFlowCoordinate(capital, (2 / 4f)));
        inFlowCoordinate.add(getInfoFlowCoordinate(capital, (3 / 4f)));
        inFlowCoordinate.add(getInfoFlowCoordinate(capital, (1)));
        capital.setPriceCoordinate(priceCoordinate).setInFlowCoordinate(inFlowCoordinate).setData(capitalDataList);
        return capital;
    }

    private Float getPriceCoordinate(Capital capital, float ratio) {
        return capital.getMixPrice() + (capital.getMaxPrice() - capital.getMixPrice()) * ratio;
    }

    private Float getInfoFlowCoordinate(Capital capital, float ratio) {
        return capital.getMixInFlow() + (capital.getMaxInFlow() - capital.getMixInFlow()) * ratio;
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