###小红书平移动画效果

> 思路：这个动画效果的难点在于繁琐，每一屏界面上都有很多的View需要去平移，而且平移的速度都不同，
>按照我们平常的思维就是自定义控件然后给每个控件添加x,y轴方向的速度自定义属性，然后通过findviewbyid
>找到每个view添加平移效果，但是这么做很复杂，而且不便于拓展，可能后期我们还会去加更多的元素，加不同的
>View，所以这种方法可行但不切实际。
>我们xml布局都是通过LayoutInflate去填充到屏幕上的，那么我们其实可以直接在系统自带控件上写自定义属性，
>然后自定义LayoutInflate来获取我们的自定义属性，这样更加便于统一管理所有的控件。

#### LayoutInflate 自定义

1.LayoutInflate内部是单例模式，所以我们需要覆写cloneInContext()方法，使得我们的布局填充不会造成全局影响

2.我们通过实现Factory接口来个性化填充布局，并获取索性，而LayoutInflate内部内设了几个factory变量，
  其中优先级 mFactory2 > mFactory > mPrivateFactory ,如果创建失败，则最终调用createView方法来创建。