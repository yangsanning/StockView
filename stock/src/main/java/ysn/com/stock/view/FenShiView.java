package ysn.com.stock.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import ysn.com.stock.R;
import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.bean.IFenShiData;

/**
 * @Author yangsanning
 * @ClassName FenShiView
 * @Description 普通分时图
 * @Date 2019/5/4
 * @History 2019/5/4 author: description:
 */
public class FenShiView extends StockView {

    private static final String[] TIME_TEXT = new String[]{"09:30", "11:30/13:00", "15:00"};

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

    private List<Float> stockPriceList = new ArrayList<>();
    private List<Float> stockAvePriceList = new ArrayList<>();
    private List<String> timeList = new ArrayList<>();
    private float lastClose = 0.0f;
    private float maxStockPrice = 0.0f;
    private float minStockPrice = 0.0f;
    private String percent = " 100%";

    private Path pricePath;
    private Paint pricePaint;
    private Path avePricePath;
    private Paint avePricePaint;
    private Path priceAreaPath;
    private Paint priceAreaPaint;

    /**
     * isBeat: 是否跳动
     * beatFraction: 变化率
     */
    private boolean isBeat = false;
    private float beatFraction;
    private Paint heartPaint;
    private ValueAnimator beatAnimator;
    private Handler beatHandler = new Handler();
    private Runnable beatRunnable = new Runnable() {
        @Override
        public void run() {
            beatAnimator.start();
            invalidate();
            beatHandler.postDelayed(this, heartBeatRate);
        }
    };

    public FenShiView(Context context) {
        super(context);
    }

    public FenShiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FenShiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FenShiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FenShiView);

        priceStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.FenShiView_fsv_price_stroke_width, 2);

        heartRadius = typedArray.getDimensionPixelSize(R.styleable.FenShiView_fsv_heart_radius, 5);
        heartDiameter = typedArray.getDimensionPixelSize(R.styleable.FenShiView_fsv_heart_diameter, 40);
        heartInitAlpha = typedArray.getInteger(R.styleable.FenShiView_fsv_heart_init_alpha, 255);
        heartBeatRate = typedArray.getInteger(R.styleable.FenShiView_fsv_heart_beat_rate, 2000);
        heartBeatFractionRate = typedArray.getInteger(R.styleable.FenShiView_fsv_heart_beat_fraction_rate, 2000);

        typedArray.recycle();
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        pricePath = new Path();
        pricePaint = new Paint();
        pricePaint.setColor(getColor(R.color.stock_price_line));
        pricePaint.setAntiAlias(true);
        pricePaint.setStyle(Paint.Style.STROKE);
        pricePaint.setStrokeWidth(priceStrokeWidth);

        avePricePath = new Path();
        avePricePaint = new Paint();
        avePricePaint.setColor(getColor(R.color.stock_ave_price_line));
        avePricePaint.setAntiAlias(true);
        avePricePaint.setStyle(Paint.Style.STROKE);
        avePricePaint.setStrokeWidth(priceStrokeWidth);

        priceAreaPath = new Path();
        priceAreaPaint = new Paint();
        priceAreaPaint.setColor(getColor(R.color.stock_price_line));
        priceAreaPaint.setStyle(Paint.Style.FILL);
        priceAreaPaint.setStrokeWidth(2);
        priceAreaPaint.setAlpha(15);

        heartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        heartPaint.setAntiAlias(true);
        beatAnimator = ValueAnimator.ofFloat(0, 1f).setDuration(heartBeatFractionRate);
        beatAnimator.addUpdateListener(animation -> {
            beatFraction = (float) animation.getAnimatedValue();
            invalidate();
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 绘制时间坐标
     */
    @Override
    protected void onTimeTextDraw(Canvas canvas) {
        super.onTimeTextDraw(canvas);
        textPaint.setColor(getColor(R.color.stock_text_title));

        // 绘制开始区域时间值
        textPaint.getTextBounds(TIME_TEXT[0], (0), TIME_TEXT[0].length(), textRect);
        canvas.drawText(TIME_TEXT[0], tableMargin, getTimeTextY(), textPaint);

        // 绘制中间区域时间值
        textPaint.getTextBounds(TIME_TEXT[1], (0), TIME_TEXT[1].length(), textRect);
        canvas.drawText(TIME_TEXT[1], (((viewWidth - textRect.right) >> 1) - tableMargin), getTimeTextY(), textPaint);

        // 绘制结束区域时间值
        textPaint.getTextBounds(TIME_TEXT[2], (0), TIME_TEXT[2].length(), textRect);
        canvas.drawText(TIME_TEXT[2], (viewWidth - textRect.right - tableMargin), getTimeTextY(), textPaint);
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);

        if (stockPriceList.isEmpty()) {
            return;
        }

        // 绘制坐标峰值
        drawXYText(canvas);

        // 绘制价格、价格区域、均线、闪烁点
        drawPriceLine(canvas);
    }

    /**
     * 绘制坐标峰值
     */
    private void drawXYText(Canvas canvas) {
        // 价格最大值
        String text = decimalFormat.format(maxStockPrice);
        textPaint.setColor(getColor(R.color.stock_red));
        textPaint.getTextBounds(text, (0), text.length(), textRect);
        float textMargin = getTextMargin();
        float y = (getTopTableMaxY() + textRect.height() + textMargin);
        canvas.drawText(text, textMargin, y, textPaint);

        // 增幅
        text = "+" + percent;
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, (viewWidth - textRect.width() - textMargin), y, textPaint);

        // 价格最小值
        textPaint.setColor(getColor(R.color.stock_green));
        y = getTopTableMinY() - textMargin;
        canvas.drawText(decimalFormat.format(minStockPrice), textMargin, y, textPaint);

        // 减幅
        text = "-" + percent;
        textPaint.setColor(getColor(R.color.stock_green));
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, (viewWidth - textRect.width() - textMargin), y, textPaint);

        // 中间坐标
        textPaint.setColor(getColor(R.color.stock_text_title));
        text = decimalFormat.format(lastClose);
        canvas.drawText(text, textMargin, (-(topTableHeight - textRect.height()) / 2f), textPaint);
    }

    /**
     * 绘制价格、价格区域、均线、闪烁点
     */
    private void drawPriceLine(Canvas canvas) {
        float price = stockPriceList.get(0);
        pricePath.moveTo(tableMargin, getY(price));
        priceAreaPath.moveTo(tableMargin, getTopTableMinY());
        priceAreaPath.lineTo(tableMargin, getY(price));
        avePricePath.moveTo(tableMargin, getY(stockAvePriceList.get(0)));
        for (int i = 1; i < stockPriceList.size(); i++) {
            price = stockPriceList.get(i);
            pricePath.lineTo(getX(i), getY(price));
            priceAreaPath.lineTo(getX(i), getY(price));
            avePricePath.lineTo(getX(i), getY(stockAvePriceList.get(i)));

            if (isBeat && i == stockPriceList.size() - 1) {
                //绘制扩散圆
                heartPaint.setColor(getColor(R.color.stock_price_line));
                heartPaint.setAlpha((int) (heartInitAlpha - heartInitAlpha * beatFraction));
                canvas.drawCircle(getX(i), getY(price), (heartRadius + heartDiameter * beatFraction), heartPaint);
                // 绘制中心圆
                heartPaint.setAlpha(255);
                heartPaint.setColor(getColor(R.color.stock_price_line));
                canvas.drawCircle(getX(i), getY(price), heartRadius, heartPaint);
            }
        }
        priceAreaPath.lineTo(getX((stockPriceList.size() - 1)), getTopTableMinY());
        priceAreaPath.close();

        canvas.drawPath(pricePath, pricePaint);
        canvas.drawPath(priceAreaPath, priceAreaPaint);
        canvas.drawPath(avePricePath, avePricePaint);

        pricePath.reset();
        priceAreaPath.reset();
        avePricePath.reset();
    }

    /**
     * 获取价格线的y轴坐标
     *
     * @param price 当前价格
     * @return 价格线的y轴坐标
     */
    private float getY(float price) {
        return getY(price, minStockPrice, maxStockPrice);
    }

    public <T extends IFenShi> void setData(T fenShi) {
        if (fenShi != null) {
            stockPriceList.clear();
            stockAvePriceList.clear();
            timeList.clear();
            List<? extends IFenShiData> fenShiData = fenShi.getFenShiData();
            for (int i = 0; i < fenShiData.size(); i++) {
                addStockPrice(fenShiData.get(i).getFenShiPrice(), i);
                stockAvePriceList.add(fenShiData.get(i).getFenShiAvgPrice());
                timeList.add(fenShiData.get(i).getFenShiTime().substring(8, 10) + ":" + fenShiData.get(i).getFenShiTime().substring(10));
            }
            lastClose = fenShi.getFenShiLastClose();
            initData();
        }
        invalidate();
        startBeat();
    }

    private void addStockPrice(float trade, int position) {
        stockPriceList.add(trade);

        if (position == 0) {
            maxStockPrice = trade;
            minStockPrice = trade;
        }

        if (maxStockPrice < trade) {
            maxStockPrice = trade;
        } else if (minStockPrice > trade) {
            minStockPrice = trade;
        }
    }

    private void initData() {
        if (Math.abs(minStockPrice - lastClose) > Math.abs(maxStockPrice - lastClose)) {
            float temp = maxStockPrice;
            maxStockPrice = minStockPrice;
            minStockPrice = temp;
        }

        if (maxStockPrice > lastClose) {
            minStockPrice = lastClose * 2 - maxStockPrice;
        } else {
            minStockPrice = maxStockPrice;
            maxStockPrice = lastClose * 2 - maxStockPrice;
        }

        // 百分比坐标值
        percent = decimalFormat.format(((maxStockPrice - lastClose) / lastClose * 100)) + "%";
    }

    public void startBeat() {
        stopBeat();
        if (!timeList.isEmpty() && isBeatTime()) {
            isBeat = true;
            beatHandler.post(beatRunnable);
        }
    }

    private boolean isBeatTime() {
        return !"11:30".equals(timeList.get(timeList.size() - 1)) && !"15:00".equals(timeList.get(timeList.size() - 1));
    }

    public void stopBeat() {
        isBeat = false;
        beatHandler.removeCallbacks(beatRunnable);
    }
}
