# StockView
[![](https://jitpack.io/v/yangsanning/StockView.svg)](https://jitpack.io/#yangsanning/StockView)
[![API](https://img.shields.io/badge/API-19%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=19)

## 效果预览

| [FenShiView] | [FiveDayFenShiView] | [CapitalView] |
| ------------ | ------------------- | ------------- |
| <img src="images/fenshiview.gif"/> | <img src="images/fivedayfenshiview.gif"/> | <img src="images/capitalview.gif"/> |

| [MiniFenShiView] | [ProfitLossView] | [ProfitLossView] |
| ------------ | ------------------- | ------------- |
| <img src="images/minifenshiview.gif"/> | <img src="images/profitlossview.gif"/> |  <img src="images/profitlossview.gif"/> |


## 主要文件
| 名字             | 摘要           |
| ---------------- | -------------- |
| [FenShiView] | 普通分时图  |
| [FiveDayFenShiView] | 五日分时图  |
| [CapitalView] | 资金趋势图  |
| [MiniFenShiView] | 迷你分时图  |
| [ProfitLossView] | 盈亏额/盈亏率  |


## 添加方法

### 1. 添加仓库

在项目的 `build.gradle` 文件中配置仓库地址。

```android
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

### 2. 添加项目依赖

在需要添加依赖的 Module 下添加以下信息，使用方式和普通的远程仓库一样。

```android
implementation 'com.github.yangsanning:StockView:1.2.0'
```

## 版本信息
[传送门](https://github.com/yangsanning/StockView/releases)

## 注
### 1. 其中走马灯效果，可参考我的 [RecyclerViewFlipper]

[FenShiView]:https://github.com/yangsanning/StockView/blob/master/readmes/FenShiView.md
[FiveDayFenShiView]:https://github.com/yangsanning/StockView/blob/master/readmes/FiveDayFenShiView.md
[CapitalView]:https://github.com/yangsanning/StockView/blob/master/readmes/CapitalView.md
[MiniFenShiView]:https://github.com/yangsanning/StockView/blob/master/readmes/MiniFenShiView.md
[ProfitLossView]:https://github.com/yangsanning/StockView/blob/master/readmes/ProfitLossView.md
[RecyclerViewFlipper]:https://github.com/yangsanning/RecyclerViewFlipper
