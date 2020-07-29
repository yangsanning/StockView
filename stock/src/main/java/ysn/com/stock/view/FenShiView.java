package ysn.com.stock.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;

import ysn.com.stock.R;
import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.config.FenShiConfig;
import ysn.com.stock.helper.FenShiSlideHelper;
import ysn.com.stock.interceptor.FenShiUnitInterceptor;
import ysn.com.stock.manager.FenShiDataManager;
import ysn.com.stock.view.base.GridView;

/**
 * @Author yangsanning
 * @ClassName FenShiView
 * @Description 普通分时图
 * @Date 2019/5/4
 * @History 2019/5/4 author: description:
 */
public class FenShiView extends GridView {

    private FenShiConfig config;

    private Path pricePath;
    private Paint pricePaint;
    private Path avePricePath;
    private Paint avePricePaint;
    private Path priceAreaPath;
    private Paint priceAreaPaint;

    /**
     * bottomTableMaxY: 下表格最大宽度
     * pillarSpace: 柱状图间距
     * maxPillarHeight: 柱状图绘制最大高度
     */
    private float bottomTableMaxY;
    private Paint pillarPaint;
    private float maxPillarHeight;

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
            beatHandler.postDelayed(this, config.heartBeatRate);
        }
    };

    private FenShiDataManager fenShiDataManager;
    private FenShiSlideHelper fenShiSlideHelper;

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
    protected void init(AttributeSet attrs) {
        super.init(attrs);
        fenShiDataManager = new FenShiDataManager(decimalFormat);
        if (config.isEnabledSlide) {
            fenShiSlideHelper = new FenShiSlideHelper(this, fenShiDataManager);
        }
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        config = new FenShiConfig(context, attrs);
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        // 初始化价格
        pricePath = new Path();
        pricePaint = new Paint();
        pricePaint.setColor(getColor(R.color.stock_price_line));
        pricePaint.setAntiAlias(true);
        pricePaint.setStyle(Paint.Style.STROKE);
        pricePaint.setStrokeWidth(config.priceStrokeWidth);

        // 初始化均价
        avePricePath = new Path();
        avePricePaint = new Paint();
        avePricePaint.setColor(getColor(R.color.stock_ave_price_line));
        avePricePaint.setAntiAlias(true);
        avePricePaint.setStyle(Paint.Style.STROKE);
        avePricePaint.setStrokeWidth(config.priceStrokeWidth);

        // 初始化价格区域
        priceAreaPath = new Path();
        priceAreaPaint = new Paint();
        priceAreaPaint.setColor(getColor(R.color.stock_price_line));
        priceAreaPaint.setStyle(Paint.Style.FILL);
        priceAreaPaint.setStrokeWidth(2);
        priceAreaPaint.setAlpha(15);

        // 初始化扩散圆
        heartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        heartPaint.setAntiAlias(true);
        beatAnimator = ValueAnimator.ofFloat(0, 1f).setDuration(config.heartBeatFractionRate);
        beatAnimator.addUpdateListener(animation -> {
            beatFraction = (float) animation.getAnimatedValue();
            invalidate();
        });

        // 初始化柱形图
        pillarPaint = new Paint();
    }

    @Override
    public int getTotalCount() {
        return fenShiDataManager.totalCount == 0 ? super.getTotalCount() : fenShiDataManager.totalCount;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 这里对柱形图最大高度进行限制, 避免顶到时间表格难看
        maxPillarHeight = (bottomTableHeight - 1) * 0.95f;
        bottomTableMaxY = getBottomTableMaxY();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (fenShiSlideHelper != null) {
            fenShiSlideHelper.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (fenShiSlideHelper != null) {
            return fenShiSlideHelper.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);

        if (fenShiDataManager.isPriceEmpty()) {
            return;
        }

        // 绘制上表格坐标
        drawTopTableCoordinate(canvas);

        if (isEnabledBottomTable()) {
            // 绘制价格、价格区域、均线、闪烁点、柱形图
            drawPriceLineAndPillar(canvas);

            // 绘制下表格坐标
            drawBottomTableCoordinate(canvas);
        } else {
            // 绘制价格、价格区域、均线、闪烁点
            drawPriceLine(canvas);
        }

        // 绘制价格曲线
        drawPricePath(canvas);

        if (fenShiSlideHelper != null) {
            fenShiSlideHelper.draw(canvas);
        }
    }

    /**
     * 绘制上表格坐标
     */
    private void drawTopTableCoordinate(Canvas canvas) {
        float topRowSpacing = getTopRowSpacing();
        int partTopHorizontal = getPartTopHorizontal();
        lazyPaint.setTextColor(getColor(R.color.stock_red))
                .measure(decimalFormat.format(fenShiDataManager.maxPrice), lazyTextPaint -> {
                    // 价格最大值
                    float y2 = getTopCoordinateY(partTopHorizontal, getTopRowY(topRowSpacing, partTopHorizontal), lazyTextPaint);
                    lazyTextPaint.drawTableStartText(canvas, getTopTableMinX(), xYTextMargin, y2);
                })
                .measure(("+" + fenShiDataManager.percent), lazyTextPaint -> {
                    // 增幅
                    float y2 = getTopCoordinateY(partTopHorizontal, getTopRowY(topRowSpacing, partTopHorizontal), lazyTextPaint);
                    lazyTextPaint.drawTableEndText(canvas, getTopTableMaxX(), xYTextMargin, y2);
                })
                .setTextColor(getColor(R.color.stock_text_title))
                .measure(decimalFormat.format(fenShiDataManager.lastClose), lazyTextPaint -> {
                    // 中间坐标
                    int position = partTopHorizontal / 2;
                    float y2 = getTopCoordinateY(position, getTopRowY(topRowSpacing, position), lazyTextPaint);
                    lazyTextPaint.drawTableStartText(canvas, getTopTableMinX(), xYTextMargin, y2);
                })
                .setTextColor(getColor(R.color.stock_green))
                .measure(decimalFormat.format(fenShiDataManager.minPrice), lazyTextPaint -> {
                    // 价格最小值
                    float y2 = getTopCoordinateY(0, getTopRowY(topRowSpacing, 0), lazyTextPaint);
                    lazyTextPaint.drawTableStartText(canvas, getTopTableMinX(), xYTextMargin, y2);
                })
                .measure(("-" + fenShiDataManager.percent), lazyTextPaint -> {
                    // 减幅
                    float y2 = getTopCoordinateY(0, getTopRowY(topRowSpacing, 0), lazyTextPaint);
                    lazyTextPaint.drawTableEndText(canvas, getTopTableMaxX(), xYTextMargin, y2);
                });
    }

    /**
     * 绘制价格、价格区域、均线、闪烁点、柱形图
     * 相比于{@link #drawPriceLine}多了柱形图绘制, 之所以加多一个方法是为了减少循环耗时，以及避免没必要的判断
     */
    private void drawPriceLineAndPillar(Canvas canvas) {
        // pillarSpace= 宽 - 边距 - 柱子间距(1f)
        float pillarSpace = (viewWidth - (tableMargin * 2) - (getTotalCount() * 1f)) / getTotalCount();
        pillarPaint.setStrokeWidth(pillarSpace);

        // 设置价格圆点（第一个点）
        moveToPrice();

        // 绘制第一个点柱状图（第一个点要跟昨收做对比）
        drawFirstPillar(canvas, pillarSpace);

        for (int i = 1; i < fenShiDataManager.priceSize(); i++) {
            // 记录后续价格点
            lineToPrice(canvas, i);

            // 绘制后续柱形图
            drawPillar(canvas, i, pillarSpace);
        }
    }

    /**
     * 绘制价格、价格区域、均线、闪烁点
     */
    private void drawPriceLine(Canvas canvas) {
        // 抽取第一个点确定Path的圆点
        moveToPrice();

        // 对后续点做处理
        for (int i = 1; i < fenShiDataManager.priceSize(); i++) {
            lineToPrice(canvas, i);
        }
    }

    /**
     * 设置价格圆点（第一个点）
     */
    private void moveToPrice() {
        float priceX = getX(0);
        float priceY = getPriceY(fenShiDataManager.getPrice(0));
        pricePath.moveTo(priceX, priceY);
        priceAreaPath.moveTo(priceX, getTopTableMaxY());
        priceAreaPath.lineTo(priceX, priceY);
        avePricePath.moveTo(priceX, getPriceY(fenShiDataManager.getAvePrice(0)));
    }

    /**
     * 绘制第一个点柱状图（第一个点要跟昨收做对比）
     */
    private void drawFirstPillar(Canvas canvas, float pillarSpace) {
        float pillarX = getPillarX(0, pillarSpace);
        float pillarStopY = getPillarHeight(0);
        pillarPaint.setColor(getColor(fenShiDataManager.getPrice(0) >= fenShiDataManager.lastClose ? R.color.stock_red : R.color.stock_green));
        canvas.drawLine(pillarX, bottomTableMaxY, pillarX, pillarStopY, pillarPaint);
    }

    /**
     * 记录后续价格点
     */
    private void lineToPrice(Canvas canvas, int i) {
        float priceX = getX(i);
        float priceY = getPriceY(fenShiDataManager.getPrice(i));
        pricePath.lineTo(priceX, priceY);
        priceAreaPath.lineTo(priceX, priceY);
        avePricePath.lineTo(priceX, getPriceY(fenShiDataManager.getAvePrice(i)));

        if (isBeat && fenShiDataManager.isLastPrice(i)) {
            // 绘制扩散圆
            heartPaint.setColor(getColor(R.color.stock_price_line));
            heartPaint.setAlpha((int) (config.heartInitAlpha - config.heartInitAlpha * beatFraction));
            canvas.drawCircle(priceX, priceY, (config.heartRadius + config.heartDiameter * beatFraction), heartPaint);

            // 绘制中心圆
            heartPaint.setAlpha(255);
            heartPaint.setColor(getColor(R.color.stock_price_line));
            canvas.drawCircle(priceX, priceY, config.heartRadius, heartPaint);
        }
    }

    /**
     * 绘制后续柱形图
     */
    private void drawPillar(Canvas canvas, int i, float pillarSpace) {
        float pillarX;
        float pillarStopY;
        pillarPaint.setColor(getColor(fenShiDataManager.getPrice(i) >= fenShiDataManager.getPrice(i - 1) ? R.color.stock_red : R.color.stock_green));
        pillarX = getPillarX(i, pillarSpace);
        pillarStopY = getPillarHeight(i);
        canvas.drawLine(pillarX, bottomTableMaxY, pillarX, pillarStopY, pillarPaint);
    }

    /**
     * 绘制价格曲线
     */
    private void drawPricePath(Canvas canvas) {
        // 价格颜色区域需要进行闭合处理
        priceAreaPath.lineTo(getX(fenShiDataManager.getLastPricePosition()), getTopTableMaxY());
        priceAreaPath.close();

        // 绘制曲线以及区域
        canvas.drawPath(pricePath, pricePaint);
        canvas.drawPath(priceAreaPath, priceAreaPaint);
        canvas.drawPath(avePricePath, avePricePaint);

        // 使用完后，重置画笔
        pricePath.reset();
        priceAreaPath.reset();
        avePricePath.reset();
    }

    /**
     * 获取第i个柱状图的绘制位置（x坐标）
     *
     * @param i 第几个
     * @return 第i个柱状图的绘制位置（x坐标）
     */
    private float getPillarX(int i, float pillarSpace) {
        return tableMargin + (pillarSpace * i) + (i * 1f) + 1;
    }

    /**
     * 获取第i个柱状图的高度（stop y坐标）
     *
     * @param i 第几个
     * @return 第i个柱状图的高度（stop y坐标）
     */
    private float getPillarHeight(int i) {
        return bottomTableMaxY - (fenShiDataManager.getVolume(i) * maxPillarHeight) / fenShiDataManager.maxVolume;
    }

    /**
     * 绘制下表格坐标
     */
    private void drawBottomTableCoordinate(Canvas canvas) {
        // 下表格当前成交量
        textPaint.getTextBounds(fenShiDataManager.currentVolumeString, 0, fenShiDataManager.currentVolumeString.length(), textRect);
        canvas.drawText(fenShiDataManager.currentVolumeString, tableMargin + xYTextMargin,
                (getBottomTableMinY() + textRect.height() + xYTextMargin), textPaint);

        // 下表格最大成交量
        textPaint.getTextBounds(fenShiDataManager.maxVolumeString, 0, fenShiDataManager.maxVolumeString.length(), textRect);
        canvas.drawText(fenShiDataManager.maxVolumeString, (viewWidth - tableMargin - xYTextMargin - textRect.width()),
                (getBottomTableMinY() + textRect.height() + xYTextMargin), textPaint);

        // 下表格中间值
        textPaint.getTextBounds(fenShiDataManager.centreVolumeString, 0, fenShiDataManager.centreVolumeString.length(), textRect);
        canvas.drawText(fenShiDataManager.centreVolumeString, (viewWidth - tableMargin - xYTextMargin - textRect.width()),
                (getBottomTableMinY() + (bottomTableHeight + textRect.height()) / 2), textPaint);
    }

    /**
     * 根据当前价格值获取相应y轴坐标
     *
     * @param price 当前价格
     * @return 当前价格的相应y轴坐标
     */
    private float getPriceY(float price) {
        return getY(price, fenShiDataManager.minPrice, fenShiDataManager.maxPrice);
    }

    public <T extends IFenShi> void setData(T fenShi) {
        fenShiDataManager.setData(fenShi);
        invalidate();
        startBeat();
    }

    /**
     * 开始心跳
     */
    public void startBeat() {
        stopBeat();
        if (fenShiDataManager.isTimeNotEmpty() && isBeatTime()) {
            isBeat = true;
            beatHandler.post(beatRunnable);
        }
    }

    /**
     * 是否可以开启心跳
     */
    public boolean isBeatTime() {
        String lastTime = fenShiDataManager.getLastTime();
        return !"11:30".equals(lastTime) && !"15:00".equals(lastTime);
    }

    /**
     * 停止心跳
     */
    public void stopBeat() {
        isBeat = false;
        beatHandler.removeCallbacks(beatRunnable);
    }

    /**
     * 设置分时单位转换拦截器
     */
    public void setFenShiUnitInterceptor(FenShiUnitInterceptor fenShiUnitInterceptor) {
        fenShiDataManager.setFenShiUnitInterceptor(fenShiUnitInterceptor);
    }
}
