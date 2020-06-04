## CapitalView

## 效果预览

| [CapitalView]                   | 
| ------------------------------- | 
| <img src="images/capitalview.gif" height="512" /> |


## 主要文件
| 名字             | 摘要           |
| ---------------- | -------------- |
| [CapitalView] | 资金趋势图 |


### 1. 基本用法

#### 1.1 布局中添加
```android
 <ysn.com.stock.view.CapitalView
        android:id="@+id/capital_activity_view2"
        android:layout_width="match_parent"
        android:layout_height="210dp"
        cv:cv_in_flow_digits="3"
        cv:cv_in_flow_unit="1000"
        cv:cv_price_digits="2"
        cv:cv_left_title="@string/capital_left_title"
        cv:cv_right_title="今日资金净流入(千元)"
        cv:cv_bg_color="@color/capital_bg"
        cv:cv_column_line_color="@color/capital_column_line"
        cv:cv_finance_in_flow_color="@color/capital_finance_in_flow"
        cv:cv_main_in_flow_color="@color/capital_main_in_flow"
        cv:cv_price_color="@color/capital_price"
        cv:cv_retail_in_flow_color="@color/capital_retail_in_flow"
        cv:cv_row_line_color="@color/capital_row_line"
        cv:cv_text_color="@color/capital_text"
        android:layout_marginTop="50dp" />
```

#### 1.2 设置数据

##### 1.2.1 将数据转换成 [Capital]
```android
   Capital capital = new Capital();
        ArrayList<CapitalData> capitalDataList = new ArrayList<>();
        for (CapitalTime.DataBean dataBean : capitalTime.getData()) {
            CapitalData data = new CapitalData(
                    dataBean.getPrice(),
                    dataBean.getFinanceInFlow(),
                    dataBean.getMainInFlow(),
                    dataBean.getRetailInFlow());
            capitalDataList.add(data);
            capital.findPriceExtremum(dataBean.getPrice())
                    .checkInFlowExtremum(data.getFinanceInFlow(), data.getMainInFlow(), data.getRetailInFlow());
        }
```
##### 1.2.2 然后再进行数据设置
```android
  capital.setData(capitalDataList);
```


### 2. 配置属性([Attributes])

#### FenShiView
|name|format|description|
|:---:|:---:|:---:|
| cv_in_flow_unit | integer | 净流入单位 |
| cv_price_digits | integer | 价格保留的位数 |
| cv_in_flow_digits | integer | 净流入保留的位数 |
| cv_left_title | string | 左上角标题(用于标注左边价格坐标) |
| cv_right_title | string | 右上角标题(用于标注右边inFlow坐标) |
| cv_price_color | color | 价格曲线颜色 |
| cv_finance_in_flow_color | color | 总资金净流入曲线颜色 |
| cv_main_in_flow_color | color | 主力净流入曲线颜色 |
| cv_retail_in_flow_color | color | 散户净流入曲线颜色 |
| cv_text_color | color | 文本颜色 |
| cv_bg_color | color | 背景颜色 |
| cv_column_line_color | color | 竖线颜色 |
| cv_row_line_color | color | 横线颜色 |


[CapitalView]:https://github.com/yangsanning/StockView/blob/master/stock/src/main/java/ysn/com/stock/view/CapitalView.java
[Capital]:https://github.com/yangsanning/StockView/blob/master/stock/src/main/java/ysn/com/stock/bean/Capital.java
[Attributes]:https://github.com/yangsanning/StockView/blob/master/stock/src/main/res/values/attrs_capital.xml

