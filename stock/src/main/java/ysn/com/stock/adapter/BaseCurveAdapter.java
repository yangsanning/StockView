package ysn.com.stock.adapter;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @Author yangsanning
 * @ClassName BaseCurveAdapter
 * @Description 曲线绘制适配器基类
 * @Date 2020/7/20
 */
public abstract class BaseCurveAdapter {

    /**
     * 绘制曲线
     * 注意: 最后一个点不用传进来，否则会引起空指针
     *
     * @param canvas   画布
     * @param paint    画笔
     * @param position 当前 position
     */
    public void draw(Canvas canvas, Paint paint, int position) {
        // 仅绘制非0的点
        float value = getValue(position);
        if (value == 0) {
            return;
        }
        int nextPosition = position + 1;
        float nextValue = getValue(nextPosition);
        if (nextValue == 0) {
            return;
        }

        // 如果绘制的点不在表格范围内，不进行绘制(防止数据异常情况)
        float maxY = getMaxY(), minY = getMinY(), startY = getDrawY(value);
        if (isOutOfRange(maxY, minY, startY)) {
            return;
        }
        float stopY = getDrawY(nextValue);
        if (isOutOfRange(maxY, minY, stopY)) {
            return;
        }

        canvas.drawLine(getDrawX(position), startY, getDrawX(nextPosition), stopY, paint);
    }

    /**
     * 判断一个值是否不在范围值内
     *
     * @param max   最大值
     * @param min   最小值
     * @param value 需要判断的值
     * @return 是否不在范围值内
     */
    private boolean isOutOfRange(float max, float min, float value) {
        return value > max || value < min;
    }

    /**
     * 根据索引获取绘制的点的值
     *
     * @param position 索引
     * @return 需要绘制的点的值
     */
    protected abstract float getValue(int position);

    protected abstract float getMaxY();

    protected abstract float getMinY();

    /**
     * 获取目标索引的 x 坐标
     *
     * @param position 索引
     * @return 目标索引的 x 坐标
     */
    protected abstract float getDrawX(int position);

    protected abstract float getDrawY(float value);
}
