## FenShiView

## 效果预览

| [FenShiView]                    | 
| ------------------------------- | 
| <img src="images/fenshiview.gif" height="512" /> |


## 主要文件
| 名字             | 摘要           |
| ---------------- | -------------- |
| [FenShiView] | 普通分时图 |


### 1. 基本用法

#### 1.1 布局中添加
```android
  <ysn.com.stock.view.FenShiView
            android:id="@+id/fen_shi_activity_view2"
            android:layout_width="match_parent"
            android:layout_height="280dp"
            android:layout_marginTop="20dp"
            ysn:fsv_is_enabled_bottom_tab="true"
            ysn:fsv_is_enabled_slide="true" />
```

#### 1.2 设置数据

##### 1.2.1 首先实现 [IFenShi] 以及 [IFenShiData] 
```android
public class FenShiTime implements IFenShi {

    @Override
    public String getFenShiCode() {
        return code;
    }

    @Override
    public List<? extends IFenShiData> getFenShiData() {
        return data;
    }

    @Override
    public float getFenShiLastClose() {
        return settlement;
    }

    public static class DataBean implements IFenShiData {
        @Override
        public String getFenShiTime() {
            return dateTime;
        }

        @Override
        public float getFenShiPrice() {
            return trade;
        }

        @Override
        public float getFenShiVolume() {
            return volume;
        }

        @Override
        public float getFenShiAvgPrice() {
            return avgPrice;
        }
    }
}

```
##### 1.2.2 然后再进行数据设置
```android
  fenShiView.setData(iFenShi);
```


### 2. 配置属性([Attributes])

#### FenShiView
|name|format|description|
|:---:|:---:|:---:|
| fsv_price_stroke_width | dimension|reference | 价格线宽度 |
| fsv_heart_radius | dimension|reference | 心脏半径 |
| fsv_heart_diameter | dimension|reference | 心脏直径 |
| fsv_heart_init_alpha | integer | 初始透明度 |
| fsv_heart_beat_rate | integer | 心率 |
| fsv_heart_beat_fraction_rate | integer | 心跳动画时间 |
| fsv_is_enabled_bottom_tab | boolean | 是否启用下表格 |
| fsv_is_enabled_slide | boolean | 是否启用滑动 |

[FenShiView]:https://github.com/yangsanning/StockView/blob/master/stock/src/main/java/ysn/com/stock/view/FenShiView.java
[IFenShi]:https://github.com/yangsanning/StockView/blob/master/stock/src/main/java/ysn/com/stock/bean/IFenShi.java
[IFenShiData]:https://github.com/yangsanning/StockView/blob/master/stock/src/main/java/ysn/com/stock/bean/IFenShiData.java
[Attributes]:https://github.com/yangsanning/StockView/blob/master/stock/src/main/res/values/attrs_fen_shi.xml

