## ProfitLossView

## 效果预览

| [ProfitLossView]                    | 
| ------------------------------- | 
| <img src="images/profitlossview.gif" height="512" /> |


## 主要文件
| 名字             | 摘要           |
| ---------------- | -------------- |
| [ProfitLossView] | 盈亏额/盈亏率 |


### 1. 基本用法

#### 1.1 布局中添加
```android
   <ysn.com.stock.view.ProfitLossView
      android:id="@+id/profit_loss_activity_view2"
      android:layout_width="match_parent"
      android:layout_height="200dp"
      android:layout_marginTop="20dp"
      ysn:fsv_is_enabled_bottom_tab="true"
      ysn:fsv_is_enabled_slide="true" />
```

#### 1.2 设置数据
```android;
   profitLossView.setUnitInterceptor(new ProfitLossInterceptor())
         .setData(data.getAmount(), data.getDate());
```


### 2. 配置属性([Attributes])

#### ProfitLossView
|name|format|description|
|:---:|:---:|:---:|
| plv_line_color | color | 线颜色 |
| plv_text_color | color | 文本颜色 |
| plv_value_line_color | color | 值曲线的颜色 |
| plv_slide_text_color | color | 滑动区域文字颜色 |
| plv_slide_bg_color | dimension | 滑动区域背景颜色 |


[ProfitLossView]:https://github.com/yangsanning/StockView/blob/master/stock/src/main/java/ysn/com/stock/view/ProfitLossView.java
[Attributes]:https://github.com/yangsanning/StockView/blob/master/stock/src/main/res/values/attrs_profit_loss.xml

