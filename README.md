# Keyboard
自定义的数字键盘，界面是一整张背景图片

## 介绍
这是一款精简的数字键盘，基于 KeyBoardView 的实现，键盘采用的是背景图的方式，美观修改方便
![keyboard.png](https://gitee.com/qice/Markdown-Photo/raw/master/keyboard_320.png)

### **使用步骤：**

#### 1、添加依赖
```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```java
compile 'com.github.qice:Keyboard:1.0'
```

#### 2.在Activity中添加如下代码：
```java
new KeyboardUtil(this).setEdit(etText).showKeyboard();
```
一句话搞定
