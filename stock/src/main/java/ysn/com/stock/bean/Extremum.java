package ysn.com.stock.bean;

/**
 * @Author yangsanning
 * @ClassName Extremum
 * @Description 极值(最大值 、 最小值 、 极差)
 * @Date 2020/7/9
 */
public class Extremum implements IExtremum{

    /**
     * 最大值
     */
    private float maximum;

    /**
     * 最小值
     */
    private float minimum;

    /**
     * 极差
     */
    private float peek;

    /**
     * 初始化最大值以及最小值
     *
     * @param value 初始值
     */
    public void init(float value) {
        maximum = minimum = value;
    }

    /**
     * 计算极差
     */
    public void calculatePeek() {
        peek = maximum - minimum;
    }

    /**
     * 重置极值(最大值 、 最小值 、 极差)
     */
    public void reset() {
        maximum = minimum = peek = 0;
    }

    /**
     * 进行最大值和最小值比较
     */
    public void convert(float... values) {
        for (float value : values) {
            if (maximum < value) {
                maximum = value;
            } else if (minimum > value) {
                minimum = value;
            }
        }
    }

    @Override
    public float getMaximum() {
        return maximum;
    }

    @Override
    public float getMinimum() {
        return minimum;
    }

    @Override
    public float getPeek() {
        return peek;
    }
}