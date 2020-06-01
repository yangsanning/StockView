package ysn.com.stockview.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ysn.com.stock.view.ProfitLossView;
import ysn.com.stockview.R;
import ysn.com.stockview.bean.ProfitLoss;
import ysn.com.stockview.utils.JsonUtils;
import ysn.com.stockview.widget.interceptor.ProfitLossInterceptor;

/**
 * @Author yangsanning
 * @ClassName ProfitLossActivity
 * @Description 一句话概括作用
 * @Date 2020/6/1
 */
public class ProfitLossActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_loss);

        setTitle(R.string.text_profit_loss);

        ProfitLoss data = JsonUtils.getData((this), ("json/profit_loss.json"), ProfitLoss.class);
        ProfitLossView profitLossView1 = findViewById(R.id.profit_loss_activity_view1);
        profitLossView1.setUnitInterceptor(new ProfitLossInterceptor())
                .setData(data.getAmount(), data.getDate());

        ProfitLossView profitLossView2 = findViewById(R.id.profit_loss_activity_view2);
        profitLossView2
                .setData(data.getRate(), data.getDate());
    }
}
