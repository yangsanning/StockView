package ysn.com.stock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import ysn.com.stock.R;
import ysn.com.stock.config.ProfitLossConfig;
import ysn.com.stock.interceptor.ProfitLossUnitInterceptor;
import ysn.com.stock.manager.ProfitLossDataManager;
import ysn.com.stock.utils.ResUtils;

/**
 * @Author yangsanning
 * @ClassName ProfitLossView
 * @Description 盈亏额/盈亏率图
 * @Date 2020/6/1
 */
public class ProfitLossView extends View {

    protected Context context;
    protected ProfitLossConfig config;
    protected ProfitLossDataManager dataManager = new ProfitLossDataManager();

    protected ProfitLossUnitInterceptor unitInterceptor;

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

        if (!dataManager.priceList.isEmpty()) {
            // 绘制时间坐标
            drawTimeText(canvas);

            // 绘制曲线
            drawPriceLine(canvas);
        }

        canvas.restore();
    }

    /**
     * 绘制左侧坐标
     */
    protected void drawYCoordinate(Canvas canvas) {
        config.textPaint.setColor(config.textColor);

        if (unitInterceptor == null) {
            for (int i = 0; i < ProfitLossDataManager.DEFAULT_Y_COORDINATES.length; i++) {
                drawYCoordinate(canvas, i, ProfitLossDataManager.DEFAULT_Y_COORDINATES[i]);
            }
        } else {
            for (int i = 0; i < dataManager.yCoordinateList.size(); i++) {
                drawYCoordinate(canvas, i, unitInterceptor.yCoordinate(dataManager.yCoordinateList.get(i)));
            }
        }
    }

    /**
     * 绘制左侧坐标
     */
    private void drawYCoordinate(Canvas canvas, int position, String value) {
        float rowLineY = -config.rowSpacing * position;
        config.textPaint.getTextBounds(value, (0), value.length(), config.textRect);
        config.textPaint.getTextBounds(value, 0, value.length(), config.textRect);
        canvas.drawText(value, -((config.leftTableWidth + config.textRect.width()) / 2),
                (rowLineY + config.textRect.height() / 2f), config.textPaint);
    }

    /**
     * 绘制横线
     */
    protected void drawRowLine(Canvas canvas) {
        config.linePaint.setColor(config.lineColor);
        int rowLineCount = ProfitLossConfig.TOP_ROW_COUNT + 1;
        for (int i = 0; i < rowLineCount; i++) {
            config.linePath.reset();
            // 横线y轴坐标
            float rowLineY = -config.rowSpacing * i;
            config.linePath.moveTo(0, rowLineY);
            config.linePath.lineTo((config.viewWidth), rowLineY);
            config.linePaint.setColor(ResUtils.getColor(context, R.color.stock_dotted_column_line));
            canvas.drawPath(config.linePath, config.linePaint);
        }
    }

    /**
     * 绘制时间坐标
     */
    protected void drawTimeText(Canvas canvas) {
        config.textPaint.setColor(config.textColor);

        String fistTime = dataManager.getFistTime();
        config.textPaint.getTextBounds(fistTime, (0), fistTime.length(), config.textRect);
        canvas.drawText(fistTime, 0, ((config.timeTableHeight + config.textRect.height()) / 2f), config.textPaint);

        String lastTime = dataManager.getLastTime();
        config.textPaint.getTextBounds(lastTime, (0), lastTime.length(), config.textRect);
        canvas.drawText(lastTime, (config.topTableWidth - config.textRect.width()),
                ((config.timeTableHeight + config.textRect.height()) / 2f), config.textPaint);
    }

    /**
     * 绘制曲线
     */
    protected void drawPriceLine(Canvas canvas) {
        float xSpace = config.topTableWidth / (dataManager.priceList.size()-1);

        // 抽取第一个点确定Path的圆点
        moveToPrice(xSpace);

        // 对后续点做处理
        for (int i = 1; i < dataManager.priceList.size(); i++) {
            lineToPrice(xSpace, i);
        }

        // 绘制曲线以及区域
        canvas.drawPath(config.priceLinePath, config.priceLinePaint);

        // 使用完后，重置画笔
        config.priceLinePath.reset();
    }

    /**
     * 设置价格圆点（第一个点）
     */
    private void moveToPrice(float xSpace) {
        float priceX = getX(xSpace, 0);
        float priceY = getPriceY(0);
        config.priceLinePath.moveTo(priceX, priceY);
    }

    /**
     * 记录后续价格点
     */
    private void lineToPrice(float xSpace, int i) {
        float priceX = getX(xSpace, i);
        float priceY = getPriceY(i);
        config.priceLinePath.lineTo(priceX, priceY);
    }

    /**
     * 获取x轴坐标
     *
     * @param xSpace   点间距
     * @param position 当前position
     * @return x轴坐标
     */
    private float getX(float xSpace, int position) {
        return xSpace * position;
    }


    /**
     * 根据当前索引获取相应y轴坐标
     *
     * @param position 索引
     * @return 当前索引的相应y轴坐标
     */
    private float getPriceY(int position) {
        float topTableMaxY = config.titleTableHeight - config.topTableHeight;
        return (topTableMaxY * (dataManager.getPrice(position) - dataManager.minValue)) / dataManager.peak;
    }

    public void setData(List<Float> priceList, List<String> timesList) {
        if (!priceList.isEmpty()) {
            dataManager.setData(priceList, timesList);
        }
        invalidate();
    }

    public ProfitLossView setUnitInterceptor(ProfitLossUnitInterceptor unitInterceptor) {
        this.unitInterceptor = unitInterceptor;
        return this;
    }
}
