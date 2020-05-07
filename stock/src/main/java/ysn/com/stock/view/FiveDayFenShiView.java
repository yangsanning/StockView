package ysn.com.stock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import ysn.com.stock.R;
import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.manager.FiveDayFenShiDataManager;

/**
 * @Author yangsanning
 * @ClassName FiveDayFiveDayFenShiView
 * @Description 五日分时
 * @Date 2020/5/7
 */
public class FiveDayFenShiView extends StockView {

    /**
     * 价格线宽度
     */
    private int priceStrokeWidth;

    /**
     * heartRadius: 心脏半径
     * heartDiameter: 心脏直径
     * HEART_INIT_ALPHA: 初始透明度
     * HEART_BEAT_RATE: 心率
     * HEART_BEAT_FRACTION_RATE: 心跳动画时间
     */
    private int heartRadius;
    private int heartDiameter;
    private int heartInitAlpha;
    private long heartBeatRate;
    private long heartBeatFractionRate;

    private boolean isEnabledBottomTable;
    private boolean isEnabledSlide;

    FiveDayFenShiDataManager dataManager;

    public FiveDayFenShiView(Context context) {
        super(context);
    }

    public FiveDayFenShiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FiveDayFenShiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FiveDayFenShiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(AttributeSet attrs) {
        super.init(attrs);
        dataManager = new FiveDayFenShiDataManager(decimalFormat);
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FiveDayFenShiView);

        priceStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.FiveDayFenShiView_fdfsv_price_stroke_width, 2);

        heartRadius = typedArray.getDimensionPixelSize(R.styleable.FiveDayFenShiView_fdfsv_heart_radius, 5);
        heartDiameter = typedArray.getDimensionPixelSize(R.styleable.FiveDayFenShiView_fdfsv_heart_diameter, 40);
        heartInitAlpha = typedArray.getInteger(R.styleable.FiveDayFenShiView_fdfsv_heart_init_alpha, 255);
        heartBeatRate = typedArray.getInteger(R.styleable.FiveDayFenShiView_fdfsv_heart_beat_rate, 2000);
        heartBeatFractionRate = typedArray.getInteger(R.styleable.FiveDayFenShiView_fdfsv_heart_beat_fraction_rate, 2000);

        isEnabledBottomTable = typedArray.getBoolean(R.styleable.FiveDayFenShiView_fdfsv_is_enabled_bottom_tab, Boolean.FALSE);
        isEnabledSlide = typedArray.getBoolean(R.styleable.FiveDayFenShiView_fdfsv_is_enabled_slide, Boolean.FALSE);

        typedArray.recycle();
    }

    @Override
    public boolean hasBottomTable() {
        return isEnabledBottomTable;
    }

    @Override
    protected int getColumnCount() {
        return 5;
    }

    public <T extends IFenShi> void setData1(T fenShi) {
        dataManager.setData1(fenShi);
    }

    public <T extends IFenShi> void setData2(T fenShi) {
        dataManager.setData2(fenShi);
    }

    public <T extends IFenShi> void setData3(T fenShi) {
        dataManager.setData3(fenShi);
    }

    public <T extends IFenShi> void setData4(T fenShi) {
        dataManager.setData4(fenShi);
    }

    public <T extends IFenShi> void setData5(T fenShi) {
        dataManager.setData5(fenShi);
        invalidate();
    }
}
