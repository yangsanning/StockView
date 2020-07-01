package ysn.com.stock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import ysn.com.stock.config.ProfitLossConfig;
import ysn.com.stock.helper.ProfitLossSlideHelper;
import ysn.com.stock.interceptor.ProfitLossUnitInterceptor;
import ysn.com.stock.manager.ProfitLossDataManager;
import ysn.com.stock.paint.QuickPaint;

/**
 * @Author yangsanning
 * @ClassName ProfitLossView
 * @Description 盈亏额/盈亏率图
 * @Date 2020/6/1
 */
public class ProfitLossView extends View {

    protected Context context;

    /**
     * 参数配置
     */
    protected ProfitLossConfig config;
    protected QuickPaint quickPaint;

    /**
     * 数据管理
     */
    protected ProfitLossDataManager dataManager = new ProfitLossDataManager();

    /**
     * 单位转换拦截器
     */
    protected ProfitLossUnitInterceptor unitInterceptor;

    /**
     * 滑动事件管理
     */
    protected ProfitLossSlideHelper slideHelper;

    public ProfitLossView(Context context) {
        this(context, null);
    }

    public ProfitLossView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfitLossView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProfitLossView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        this.context = context;
        config = new ProfitLossConfig(context, attrs);
        quickPaint = config.quickPaint;
        slideHelper = new ProfitLossSlideHelper(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (slideHelper != null) {
            slideHelper.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (slideHelper != null) {
            return slideHelper.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        config.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(config.circleX, config.circleY);

        // 绘制左侧坐标
        drawYCoordinate(canvas);

        // 绘制横线
        drawRowLine(canvas);

        if (dataManager.isNotEmpty()) {
            // 绘制时间坐标
            drawTimeText(canvas);

            // 绘制曲线
            drawPriceLine(canvas);

            if (slideHelper != null) {
                slideHelper.draw(canvas);
            }
        }

        canvas.restore();
    }

    /**
     * 绘制左侧坐标
     */
    protected void drawYCoordinate(Canvas canvas) {
        quickPaint.setTextColor(config.textColor);
        for (int i = 0; i < dataManager.yCoordinateList.size(); i++) {
            Float yCoordinate = dataManager.yCoordinateList.get(i);
            drawYCoordinate(canvas, i, unitInterceptor == null ?
                    String.valueOf(yCoordinate) : unitInterceptor.yCoordinate(yCoordinate));
        }
    }

    /**
     * 绘制左侧坐标
     */
    private void drawYCoordinate(Canvas canvas, int position, String value) {
        float rowLineY = -config.rowSpacing * position;
        quickPaint.measure(value, measure ->
                measure.drawText(canvas, (-(config.leftTableWidth + measure.width()) / 2), (rowLineY + measure.height() / 2f)));
    }

    /**
     * 绘制横线
     */
    protected void drawRowLine(Canvas canvas) {
        quickPaint.setLineColor(config.lineColor)
                .setLineStrokeWidth(1f)
                .setLineStyle(Paint.Style.STROKE);
        int rowLineCount = ProfitLossConfig.TOP_ROW_COUNT + 1;
        for (int i = 0; i < rowLineCount; i++) {
            // 横线y轴坐标
            float rowLineY = -config.rowSpacing * i;
            quickPaint.drawLine(canvas, (0), rowLineY, (config.topTableWidth), rowLineY);
        }
    }

    /**
     * 绘制时间坐标
     */
    protected void drawTimeText(Canvas canvas) {
        quickPaint.setTextColor(config.textColor);

        // 绘制起始时间坐标
        quickPaint.measure(dataManager.getFistTime(),
                measure -> measure.drawText(canvas, (0), ((config.timeTableHeight + measure.height()) / 2f)));

        // 绘制结束时间坐标
        quickPaint.measure(dataManager.getLastTime(), measure ->
                measure.drawText(canvas, (config.topTableWidth - measure.width()), ((config.timeTableHeight + measure.height()) / 2f)));
    }

    /**
     * 绘制曲线
     */
    protected void drawPriceLine(Canvas canvas) {
        // 抽取第一个点确定Path的圆点
        quickPaint.moveTo(getX(0), getY(0));

        // 对后续点做处理
        for (int i = 1; i < dataManager.dataSize(); i++) {
            quickPaint.lineTo(getX(i), getY(i));
        }

        // 绘制曲线以及区域
        quickPaint.setLineStrokeWidth(2f)
                .setLineStyle(Paint.Style.STROKE)
                .setLineColor(config.valueLineColor)
                .drawPath(canvas)
                // 使用完后，重置画笔
                .resetPath();
    }


    /**
     * 获取x轴坐标
     *
     * @param position 当前position
     * @return x轴坐标
     */
    public float getX(int position) {
        // 点间距
        float xSpace = config.topTableWidth / (dataManager.dataSize() - 1);
        return xSpace * position;
    }


    /**
     * 根据当前索引获取相应y轴坐标
     *
     * @param position 索引
     * @return 当前索引的相应y轴坐标
     */
    public float getY(int position) {
        // (当前价格 - 圆点坐标价格)/(y坐标) = 坐标极值/高度
        return -(dataManager.getValue(position) - dataManager.coordinateMin) / (dataManager.coordinatePeak / config.topTableHeight);
    }

    /**
     * 获取参数配置
     */
    public ProfitLossConfig getConfig() {
        return config;
    }

    /**
     * 获取数据管理器
     */
    public ProfitLossDataManager getDataManager() {
        return dataManager;
    }

    /**
     * 获取单位拦截器
     */
    public ProfitLossUnitInterceptor getUnitInterceptor() {
        return unitInterceptor;
    }

    /**
     * 设置单位拦截器
     */
    public ProfitLossView setUnitInterceptor(ProfitLossUnitInterceptor unitInterceptor) {
        this.unitInterceptor = unitInterceptor;
        this.slideHelper.setUnitInterceptor(unitInterceptor);
        return this;
    }

    /**
     * 设置数据
     *
     * @param valueList 价格集合
     * @param timesList 时间集合
     */
    public void setData(@NonNull List<Float> valueList, @NonNull List<String> timesList) {
        if (!valueList.isEmpty()) {
            dataManager.setData(valueList, timesList);
        }
        invalidate();
    }
}
