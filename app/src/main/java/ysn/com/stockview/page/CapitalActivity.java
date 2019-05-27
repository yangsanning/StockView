package ysn.com.stockview.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import ysn.com.stock.bean.Capital;
import ysn.com.stock.bean.CapitalData;
import ysn.com.stock.view.CapitalView;
import ysn.com.stockview.R;
import ysn.com.stockview.bean.CapitalTime;
import ysn.com.stockview.utils.JsonUtils;
import ysn.com.stockview.utils.NumberUtils;

/**
 * @Author yangsanning
 * @ClassName CapitalActivity
 * @Description 资金趋势图展示
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class CapitalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital);

        setTitle(R.string.text_capital);

        CapitalView capitalView1 = findViewById(R.id.capital_activity_view1);
        capitalView1.setNewData(convert(JsonUtils.getData((this), ("json/capital1.json"), CapitalTime.class)));

        CapitalView capitalView2 = findViewById(R.id.capital_activity_view2);
        capitalView2.setNewData(convert(JsonUtils.getData((this), ("json/capital2.json"), CapitalTime.class)));
    }

    private Capital convert(CapitalTime capitalTime) {
        if (capitalTime == null) {
            return null;
        }
        Capital capital = new Capital();
        ArrayList<CapitalData> capitalDataList = new ArrayList<>();
        for (CapitalTime.DataBean dataBean : capitalTime.getData()) {
            CapitalData data = new CapitalData(
                    NumberUtils.getNumberDecimalTwo(dataBean.getPrice()),
                    NumberUtils.getNumberDecimalTwo(dataBean.getFinanceInFlow()),
                    NumberUtils.getNumberDecimalTwo(dataBean.getMainInFlow()),
                    NumberUtils.getNumberDecimalTwo(dataBean.getRetailInFlow()));
            capitalDataList.add(data);
            capital.findInFlowExtremum(data.getFinanceInFlow(), data.getMainInFlow(), data.getRetailInFlow());
            capital.findPriceExtremum(dataBean.getPrice());
        }
        capital.setData(capitalDataList);
        return capital;
    }
}