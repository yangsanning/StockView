package ysn.com.stock.config;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import ysn.com.stock.R;
import ysn.com.stock.utils.ResUtils;

/**
 * @Author yangsanning
 * @ClassName CapitalConfig
 * @Description {@link ysn.com.stock.view.CapitalView} 的参数信息
 * @Date 2020/7/21
 */
public class CapitalConfig {

    public static final String[] TIME_TEXT = new String[]{"09:30", "11:30/13:00", "15:00"};
    public static final float DEFAULT_PRICE_STROKE_WIDTH = 2.5f;
    public static final String DEFAULT_LEFT_TITLE = "股价(元)";
    public static final String DEFAULT_RIGHT_TITLE = "今日资金净流入(元)";

    /**
     * inFlowUnit: 净流入单位
     * leftTitle: 左上角标题(用于标注左边价格坐标)
     * rightTitle: 右上角标题(用于标注右边inFlow坐标)
     */
    public String leftTitle;
    public String rightTitle;

    /**
     * priceColor: 价格曲线颜色
     * financeInFlowColor: 总资金净流入曲线颜色
     * mainInFlowColor: 主力净流入曲线颜色
     * retailInFlowColor: 散户净流入曲线颜色
     */
    public int priceColor;
    public int financeInFlowColor;
    public int mainInFlowColor;
    public int retailInFlowColor;

    /**
     * textColor: 文本颜色
     * bgColor: 背景颜色
     * columnLineColor: 竖线颜色
     * rowLineColor: 横线颜色
     */
    public int textColor;
    public int bgColor;

    public CapitalConfig(Context context, @Nullable AttributeSet attrs) {
        initAttr(context, attrs);
    }

    public void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CapitalView);

        leftTitle = typedArray.getString(R.styleable.CapitalView_leftTitle);
        rightTitle = typedArray.getString(R.styleable.CapitalView_rightTitle);

        priceColor = typedArray.getColor(R.styleable.CapitalView_priceColor, ResUtils.getColor(context, R.color.capital_price));
        financeInFlowColor = typedArray.getColor(R.styleable.CapitalView_financeInFlowColor, ResUtils.getColor(context, R.color.capital_finance_in_flow));
        mainInFlowColor = typedArray.getColor(R.styleable.CapitalView_mainInFlowColor, ResUtils.getColor(context, R.color.capital_main_in_flow));
        retailInFlowColor = typedArray.getColor(R.styleable.CapitalView_retailInFlowColor, ResUtils.getColor(context, R.color.capital_retail_in_flow));

        textColor = typedArray.getColor(R.styleable.CapitalView_textColor, ResUtils.getColor(context, R.color.capital_text));
        bgColor = typedArray.getColor(R.styleable.CapitalView_bgColor, ResUtils.getColor(context, R.color.capital_bg));

        typedArray.recycle();

        if (TextUtils.isEmpty(leftTitle)) {
            leftTitle = DEFAULT_LEFT_TITLE;
        }
        if (TextUtils.isEmpty(rightTitle)) {
            rightTitle = DEFAULT_RIGHT_TITLE;
        }
    }
}
