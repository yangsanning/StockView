package ysn.com.stock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import ysn.com.stock.R;
import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.manager.FiveDayFenShiDataManager;
import ysn.com.stock.utils.TimeUtils;

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

    /**
     * 每个数据格的宽(5份)
     */
    private int dataWidth;

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dataWidth = getViewWidth() / getColumnCount();
    }

    @Override
    protected void onTimeTextDraw(Canvas canvas) {
        super.onTimeTextDraw(canvas);
        textPaint.setColor(getColor(R.color.stock_text_title));

        // 绘制时间坐标
        for (int i = 0; i < getColumnCount(); i++) {
            String text = TimeUtils.getReduceDataString(dataManager.date, i);
            textPaint.getTextBounds(text, (0), text.length(), textRect);
            canvas.drawText(text, (dataWidth * i + (dataWidth - textRect.width()) / 2f), getTimeTextY(), textPaint);
        }
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);

        if (dataManager.dataManager5.isPriceEmpty()) {
            return;
        }

        // 绘制坐标峰值
        drawXYText(canvas);

        if (hasBottomTable()) {
            // 绘制下表格坐标
            drawBottomXYText(canvas);
        }
    }

    /**
     * 绘制坐标峰值
     */
    private void drawXYText(Canvas canvas) {
        // 价格最大值
        String text = decimalFormat.format(dataManager.maxPrice);
        textPaint.setColor(getColor(R.color.stock_red));
        textPaint.getTextBounds(text, (0), text.length(), textRect);
        float textMargin = getTextMargin();
        float y = (getTopTableMaxY() + textRect.height() + textMargin);
        canvas.drawText(text, textMargin, y, textPaint);

        // 增幅
        text = "+" + dataManager.percent;
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, (viewWidth - textRect.width() - textMargin), y, textPaint);

        // 价格最小值
        textPaint.setColor(getColor(R.color.stock_green));
        y = getTopTableMinY() - textMargin;
        canvas.drawText(decimalFormat.format(dataManager.minPrice), textMargin, y, textPaint);

        // 减幅
        text = "-" + dataManager.percent;
        textPaint.setColor(getColor(R.color.stock_green));
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, (viewWidth - textRect.width() - textMargin), y, textPaint);

        // 中间坐标
        textPaint.setColor(getColor(R.color.stock_text_title));
        text = decimalFormat.format(dataManager.lastClose);
        canvas.drawText(text, textMargin, (-(topTableHeight - textRect.height()) / 2f), textPaint);
    }

    /**
     * 绘制下表格坐标
     */
    private void drawBottomXYText(Canvas canvas) {
        // 下表格最大量
        textPaint.getTextBounds(dataManager.maxVolumeString, 0, dataManager.maxVolumeString.length(), textRect);
        float x = viewWidth - tableMargin - xYTextMargin - textRect.width();
        canvas.drawText(dataManager.maxVolumeString, x, (getBottomTableMinY() + textRect.height() + xYTextMargin), textPaint);

        // 下表格中间值
        textPaint.getTextBounds(dataManager.centreVolumeString, 0, dataManager.centreVolumeString.length(), textRect);
        canvas.drawText(dataManager.centreVolumeString, x, (getBottomTableMinY() + (bottomTableHeight + textRect.height()) / 2), textPaint);
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
