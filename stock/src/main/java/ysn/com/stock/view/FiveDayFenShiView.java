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
import android.view.MotionEvent;

import java.util.List;
import java.util.Map;

import ysn.com.stock.R;
import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.helper.FiveDayFenShiSlideHelper;
import ysn.com.stock.interceptor.FenShiUnitInterceptor;
import ysn.com.stock.manager.FenShiDataManager;
import ysn.com.stock.manager.FiveDayFenShiDataManager;
import ysn.com.stock.utils.TimeUtils;
import ysn.com.stock.view.base.GridView;

/**
 * @Author yangsanning
 * @ClassName FiveDayFiveDayFenShiView
 * @Description 五日分时
 * @Date 2020/5/7
 */
public class FiveDayFenShiView extends GridView {

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

    private boolean isEnabledSlide;

    private Path pricePath;
    private Paint pricePaint;

    /**
     * 每个数据格的宽(5份)
     */
    private int dataWidth;

    /**
     * bottomTableMaxY: 下表格最大宽度
     * pillarSpace: 柱状图间距
     * maxPillarHeight: 柱状图绘制最大高度
     */
    private float bottomTableMaxY;
    private Paint pillarPaint;
    private float maxPillarHeight;

    public FiveDayFenShiDataManager fiveDayFenShiDataManager;
    FiveDayFenShiSlideHelper fiveDayFenShiSlideHelper;

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
        fiveDayFenShiDataManager = new FiveDayFenShiDataManager(getColumnCount(), decimalFormat);

        if (isEnabledSlide) {
            fiveDayFenShiSlideHelper = new FiveDayFenShiSlideHelper(this, fiveDayFenShiDataManager);
        }
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

        isEnabledSlide = typedArray.getBoolean(R.styleable.FiveDayFenShiView_fdfsv_is_enabled_slide, Boolean.FALSE);

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

        heartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        heartPaint.setAntiAlias(true);
        beatAnimator = ValueAnimator.ofFloat(0, 1f).setDuration(heartBeatFractionRate);
        beatAnimator.addUpdateListener(animation -> {
            beatFraction = (float) animation.getAnimatedValue();
            invalidate();
        });

        pillarPaint = new Paint();
    }

    @Override
    public boolean isEnabledBottomTable() {
        return Boolean.TRUE;
    }

    @Override
    protected int getColumnCount() {
        return 5;
    }

    public int getTotalCount(FenShiDataManager fenShiDataManager) {
        return fenShiDataManager == null || fenShiDataManager.totalCount == 0 ? getTotalCount() : fenShiDataManager.totalCount;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (fiveDayFenShiSlideHelper != null) {
            fiveDayFenShiSlideHelper.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (fiveDayFenShiSlideHelper != null) {
            return fiveDayFenShiSlideHelper.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dataWidth = getViewWidth() / getColumnCount();

        if (fiveDayFenShiSlideHelper != null) {
            fiveDayFenShiSlideHelper.dataWidth = dataWidth;
        }

        // 这里对柱形图最大高度进行限制, 避免顶到时间表格难看
        maxPillarHeight = (bottomTableHeight - 1) * 0.95f;
        bottomTableMaxY = getBottomTableMaxY();
    }

    @Override
    protected void onTimeTextDraw(Canvas canvas) {
        super.onTimeTextDraw(canvas);
        textPaint.setColor(getColor(R.color.stock_text_title));

        // 绘制时间坐标
        for (int i = 0; i < fiveDayFenShiDataManager.dateList.size(); i++) {
            Long time = fiveDayFenShiDataManager.dateList.get(i);
            if (time <= 0) {
                continue;
            }
            String text = TimeUtils.formatDay(time);
            textPaint.getTextBounds(text, (0), text.length(), textRect);
            canvas.drawText(text, (dataWidth * i + (dataWidth - textRect.width()) / 2f), getTimeTextY(), textPaint);
        }
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);

        if (fiveDayFenShiDataManager.dataManagerList.isEmpty()) {
            return;
        }

        // 绘制坐标峰值
        drawXYText(canvas);

        // 绘制柱形
        for (Map.Entry<Integer, FenShiDataManager> entry : fiveDayFenShiDataManager.dataManagerMap.entrySet()) {
            drawPriceLineAndPillar(entry.getValue(), canvas, entry.getKey());
        }

        // 绘制下表格坐标
        drawBottomTableCoordinate(canvas);

        if (fiveDayFenShiSlideHelper != null) {
            fiveDayFenShiSlideHelper.draw(canvas);
        }
    }

    /**
     * 绘制坐标峰值
     */
    private void drawXYText(Canvas canvas) {
        // 价格最大值
        String text = decimalFormat.format(fiveDayFenShiDataManager.maxPrice);
        textPaint.setColor(getColor(R.color.stock_red));
        textPaint.getTextBounds(text, (0), text.length(), textRect);
        float textMargin = getTextMargin();
        float y = (getTopTableMinY() + textRect.height() + textMargin);
        canvas.drawText(text, textMargin, y, textPaint);

        // 增幅
        text = "+" + fiveDayFenShiDataManager.percent;
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, (viewWidth - textRect.width() - textMargin), y, textPaint);

        // 价格最小值
        textPaint.setColor(getColor(R.color.stock_green));
        y = getTopTableMaxY() - textMargin;
        canvas.drawText(decimalFormat.format(fiveDayFenShiDataManager.minPrice), textMargin, y, textPaint);

        // 减幅
        text = "-" + fiveDayFenShiDataManager.percent;
        textPaint.setColor(getColor(R.color.stock_green));
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, (viewWidth - textRect.width() - textMargin), y, textPaint);

        // 中间坐标
        textPaint.setColor(getColor(R.color.stock_text_title));
        text = decimalFormat.format(fiveDayFenShiDataManager.lastClose);
        canvas.drawText(text, textMargin, (-(topTableHeight - textRect.height()) / 2f), textPaint);
    }

    /**
     * 绘制价格、价格区域、均线、闪烁点、柱形图
     */
    private void drawPriceLineAndPillar(FenShiDataManager fenShiDataManager, Canvas canvas, int position) {
        // pillarSpace= 宽  - 柱子间距(1f)
        float pillarSpace = (dataWidth - (getTotalCount(fenShiDataManager) * 1f)) / getTotalCount(fenShiDataManager);
        pillarPaint.setStrokeWidth(pillarSpace);

        // 设置价格圆点（第一个点）
        moveToPrice(fenShiDataManager, position);

        // 绘制第一个点柱状图（第一个点要跟昨收做对比）
        drawFirstPillar(fenShiDataManager, canvas, position, pillarSpace);

        for (int i = 1; i < fenShiDataManager.priceSize(); i++) {
            // 记录后续价格点
            lineToPrice(fenShiDataManager, canvas, position, i);

            // 绘制后续柱形图
            drawPillar(fenShiDataManager, canvas, position, i, pillarSpace);
        }

        // 绘制价格曲线
        drawPricePath(canvas);
    }

    /**
     * 设置价格圆点（第一个点）
     */
    private void moveToPrice(FenShiDataManager fenShiDataManager, int position) {
        float priceX = getPriceX(fenShiDataManager, 0, position);
        float priceY = getPriceY(fenShiDataManager.getPrice(0));
        pricePath.moveTo(priceX, priceY);
    }

    /**
     * 绘制第一个点柱状图（第一个点要跟昨收做对比）
     */
    private void drawFirstPillar(FenShiDataManager dataManager, Canvas canvas, int position, float pillarSpace) {
        float lineX = getPillarX(0, position, pillarSpace);
        float stopY = getPillarHeight(dataManager, 0);
        pillarPaint.setColor(getColor(dataManager.getPrice(0) >= dataManager.lastClose ? R.color.stock_red : R.color.stock_green));
        canvas.drawLine(lineX, getBottomTableMaxY(), lineX, stopY, pillarPaint);
    }

    /**
     * 记录后续价格点
     */
    private void lineToPrice(FenShiDataManager fenShiDataManager, Canvas canvas, int position, int i) {
        float priceX = getPriceX(fenShiDataManager, i, position);
        float priceY = getPriceY(fenShiDataManager.getPrice(i));
        pricePath.lineTo(priceX, priceY);

        if (position == getColumnCount() - 1 && isBeat && fenShiDataManager.isLastPrice(i)) {
            //绘制扩散圆
            heartPaint.setColor(getColor(R.color.stock_price_line));
            heartPaint.setAlpha((int) (heartInitAlpha - heartInitAlpha * beatFraction));
            canvas.drawCircle(priceX, priceY, (heartRadius + heartDiameter * beatFraction), heartPaint);
            // 绘制中心圆
            heartPaint.setAlpha(255);
            heartPaint.setColor(getColor(R.color.stock_price_line));
            canvas.drawCircle(priceX, priceY, heartRadius, heartPaint);
        }
    }

    /**
     * 绘制后续柱形图
     */
    private void drawPillar(FenShiDataManager dataManager, Canvas canvas, int position, int i, float pillarSpace) {
        float lineX;
        float stopY;
        pillarPaint.setColor(getColor(dataManager.getPrice(i) >= dataManager.getPrice(i - 1) ? R.color.stock_red : R.color.stock_green));
        lineX = getPillarX(i, position, pillarSpace);
        stopY = getPillarHeight(dataManager, i);
        canvas.drawLine(lineX, getBottomTableMaxY(), lineX, stopY, pillarPaint);
    }

    /**
     * 绘制价格曲线
     */
    private void drawPricePath(Canvas canvas) {
        canvas.drawPath(pricePath, pricePaint);
        pricePath.reset();
    }

    /**
     * 获取x轴坐标
     *
     * @param i 当前position
     * @return x轴坐标
     */
    public float getPriceX(FenShiDataManager fenShiDataManager, int i, int position) {
        return getColumnX(((dataWidth) / (float) getTotalCount(fenShiDataManager)), i) + dataWidth * position;
    }

    /**
     * 获取价格线的y轴坐标
     *
     * @param price 当前价格
     * @return 价格线的y轴坐标
     */
    private float getPriceY(float price) {
        return getY(price, fiveDayFenShiDataManager.minPrice, fiveDayFenShiDataManager.maxPrice);
    }

    /**
     * 绘制下表格坐标
     */
    private void drawBottomTableCoordinate(Canvas canvas) {
        // 下表格当前成交量
        textPaint.getTextBounds(fiveDayFenShiDataManager.currentVolumeString, 0, fiveDayFenShiDataManager.currentVolumeString.length(), textRect);
        canvas.drawText(fiveDayFenShiDataManager.currentVolumeString, tableMargin + xYTextMargin,
                (getBottomTableMinY() + textRect.height() + xYTextMargin), textPaint);

        // 下表格最大量
        textPaint.getTextBounds(fiveDayFenShiDataManager.maxVolumeString, 0, fiveDayFenShiDataManager.maxVolumeString.length(), textRect);
        canvas.drawText(fiveDayFenShiDataManager.maxVolumeString, (viewWidth - tableMargin - xYTextMargin - textRect.width()),
                (getBottomTableMinY() + textRect.height() + xYTextMargin), textPaint);

        // 下表格中间值
        textPaint.getTextBounds(fiveDayFenShiDataManager.centreVolumeString, 0, fiveDayFenShiDataManager.centreVolumeString.length(), textRect);
        canvas.drawText(fiveDayFenShiDataManager.centreVolumeString, (viewWidth - tableMargin - xYTextMargin - textRect.width()),
                (getBottomTableMinY() + (bottomTableHeight + textRect.height()) / 2), textPaint);
    }

    /**
     * 获取第i个柱状图的绘制位置（x坐标）
     *
     * @param i           第几个
     * @param pillarSpace
     * @return 第position个数据
     */
    private float getPillarX(int i, int position, float pillarSpace) {
        return tableMargin + (pillarSpace * i) + (i * 1f) + 1 + dataWidth * position;
    }

    /**
     * 获取第i个柱状图的高度（stop y坐标）
     *
     * @param i 第几个
     * @return 第i个柱状图的高度（stop y坐标）
     */
    private float getPillarHeight(FenShiDataManager dataManager, int i) {
        return bottomTableMaxY - (dataManager.getVolume(i) * maxPillarHeight) / dataManager.maxVolume;
    }

    public <T extends IFenShi> void setData(List<T> fenShiList) {
        fiveDayFenShiDataManager.setData(fenShiList);
        invalidate();
        startBeat();
    }

    public void startBeat() {
        stopBeat();
        if (fiveDayFenShiDataManager.getLastDataManager().isTimeNotEmpty() && isBeatTime()) {
            isBeat = true;
            beatHandler.post(beatRunnable);
        }
    }

    private boolean isBeatTime() {
        String lastTime = fiveDayFenShiDataManager.getLastDataManager().getLastTime();
        return !"11:30".equals(lastTime) && !"15:00".equals(lastTime);
    }

    public void stopBeat() {
        isBeat = false;
        beatHandler.removeCallbacks(beatRunnable);
    }

    /**
     * 设置分时单位转换拦截器
     */
    public void setFenShiUnitInterceptor(FenShiUnitInterceptor fenShiUnitInterceptor) {
        fiveDayFenShiDataManager.setFenShiUnitInterceptor(fenShiUnitInterceptor);
    }
}