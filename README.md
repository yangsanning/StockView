# StockView
[![](https://jitpack.io/v/yangsanning/StockView.svg)](https://jitpack.io/#yangsanning/StockView)

## 效果预览

<p><img src="images/image1.gif" height="512"/> 


## 主要文件
| 名字             | 摘要           |
| ---------------- | -------------- |
|FenShiView | 普通的分时图  |

### 1. 基本用法

```android
 <ysn.com.stock.view.FenShiView
        android:id="@+id/main_activity_fen_shi_view1"
        android:layout_width="match_parent"
        android:layout_height="230dp" />
```

### 2. 配置属性(Attributes)

|name|format|description|
|:---:|:---:|:---:|


### 3.添加方法

#### 3.1 添加仓库

在项目的 `build.gradle` 文件中配置仓库地址。

```android
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

#### 3.2 添加项目依赖

在需要添加依赖的 Module 下添加以下信息，使用方式和普通的远程仓库一样。

```android
implementation 'com.github.yangsanning:StockView:1.0.0'
```
