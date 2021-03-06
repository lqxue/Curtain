#### 一个更简洁好用的高亮蒙层库：

- 一行代码完成某个View,或者多个View的高亮展示
- 支持基于AapterView(如ListView、GridView) 和RecyclerView 的item以及item中元素的高亮
- 自动识别圆角背景,也可以自定义高亮形状
- 高亮区域支持自定义大小、操作灵活
- 顺应变化,基于Android X
- 配置简单，导入方便

## Curtain 和 CurtainInflate的区别

- Curtain 将指引布局添加到fragmentDialog中
- CurtainInflate 将指引布局添加到Activity的DecorView中

## 使用：

```java
dependencies {
    implementation 'com.github.lqxue:Curtain:0.0.3'
}
```



- 仅仅是高亮某个View

```java
new Curtain(MainActivity.this)
    .with(findViewById(R.id.textView))
    .show();
```

- 如果你希望那个view的蒙层区域更大一些:

```java
new Curtain(MainActivity.this)
    .with(findViewById(R.id.textView))
    .withPadding(findViewById(R.id.textView),Padding.all(10))
    .show();
```

- 也可以同时高亮多个View:

```java
new Curtain(MainActivity.this)
    .with(findViewById(R.id.textView))
    .with(findViewById(R.id.imageView))
    .show();
```

- 如果你在蒙层上加上一些其他的元素,可以额外传入View布局:

```java
private void showCurtain(){
        new Curtain(MainActivity.this)
                .with(findViewById(R.id.textView))
                .setTopView(LayoutInflater.from(this).inflate(R.layout.nav_header_main, null))
                .show();
    }
```

![image](https://upload-images.jianshu.io/upload_images/11595074-35d1f98e309d52de.gif)

- 如果你想监听蒙层的展示或者消失的回调:

```java
new Curtain(MainActivity.this)
    .with(findViewById(R.id.imageView))
    .setCallBack(new Curtain.CallBack() {
        @Override
        public void onShow(IGuide iGuide) {
    
        }
    
        @Override
        public void onDismiss(IGuide iGuide) {
    
        }
    }).show();

```

- 默认会识别View的背景而生成相关高亮区域的形状,也可以自定形状:

```java
new Curtain(SimpleGuideActivity.this)
    .with(findViewById(R.id.btn_shape_custom))
    //圆角
    .withShape(findViewById(R.id.btn_shape_custom), new RoundShape(12))
    //椭圆形
    .withShape(findViewById(R.id.btn_shape_custom),new CircleShape())
    // 也可继承自 Shape 自己实现形状
    .withShape(item1.findViewById(R.id.image), new Shape(){
            @Override
            public void drawShape(Canvas canvas, Paint paint, HollowInfo info) {
                
            }
        })
    .show();
```

- 在ListView 或者GridView 中使用:

```java
/**
 * 高亮item
 */
private void showGuideInItem() {
    View item1 = ViewGetter.getFromAdapterView(listView, 5);
    View item2 = ViewGetter.getFromAdapterView(listView, 2);
    //如果你的View的位置不在屏幕中，返回值为null 需要判空处理
    if (null == item1 || null == item2) {
        return;
    }
    new Curtain(this)
            .with(item1)
            .with(item2)
            .show();
}

/**
 * 高亮item中的元素
 */
private void showGuideInItemChild() {
    View item1 = ViewGetter.getFromAdapterView(listView, 1);
    View item2 = ViewGetter.getFromAdapterView(listView, 3);
    //如果你的View的位置不在屏幕中，返回值为null 需要判空处理
    if (null == item1 || null == item2) {
        return;
    }
    new Curtain(this)
            .withShape(item1.findViewById(R.id.image), new CircleShape())
            .with(item2.findViewById(R.id.tv_text))
            .show();
}
```

效果:

![image](https://upload-images.jianshu.io/upload_images/11595074-3c8fc50488da539b.gif)

- 其他一些功能介绍:

```java
new Curtain(MainActivity.this)
    .with(findViewById(R.id.imageView))
    //是否允许回退关闭蒙层
    .setCancelBackPressed(false)
    // 设置蒙层背景颜色
    .setCurtainColor(0x88000000)
    // 设置蒙层出现的动画 默认渐隐
    .setAnimationStyle(R.style.testAnimation)
    .show();
```

#### CurtainFlow

如果你想按照一定的顺序去高亮一些列的View,可以方便的管理前进后退,减少方法的嵌套的场景下推荐使用:

1. 仅仅需要按照步骤的Id,和构建你想要高亮的Curtain对象,统一交给CurtianFlow来处理

```java
new CurtainFlow.Builder()
    .with(ID_STEP_1, getStepOneGuide())
    .with(ID_STEP_2, getStepTwoGuide())
    .with(ID_STEP_3, getStepThreeGuide())
    .create()
    .start(new CurtainFlow.CallBack() {
        @Override
        public void onProcess(int currentId, final CurtainFlowInterface curtainFlow) {
            switch (currentId) {
                case ID_STEP_2:
                    //回到上个
                    curtainFlow.findViewInCurrentCurtain(R.id.tv_to_last)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    curtainFlow.pop();
                                }
                            });
                    break;
                case ID_STEP_3:
                    curtainFlow.findViewInCurrentCurtain(R.id.tv_to_last)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    curtainFlow.pop();
                                }
                            });
                    //重新来一遍，即回到第一步
                    curtainFlow.findViewInCurrentCurtain(R.id.tv_retry)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    curtainFlow.toCurtainById(ID_STEP_1);
                                }
                            });
                    break;
            }
            //去下一个
            curtainFlow.findViewInCurrentCurtain(R.id.tv_to_next)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            curtainFlow.push();
                        }
                    });
        }
    });
```

2.效果

![image](https://upload-images.jianshu.io/upload_images/11595074-36db1fcb908deea8.gif)

3. APi细节上可以参考Demo

[设计原理详解](https://blog.csdn.net/u014626094/article/details/105430981)
