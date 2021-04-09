package com.learning.xiaohongshu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ParallaxLayoutInflater extends LayoutInflater {

    private static final String TAG = "ParallaxLayoutInflater";
    private ParallaxFragment mFragment;

    public ParallaxLayoutInflater(Context context) {
        super(context);
    }

    public ParallaxLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
    }

    //这里传递Fragment过来是为了将所有填充的view对象带回去
    public ParallaxLayoutInflater(LayoutInflater original, Context newContext, ParallaxFragment fragment) {
        super(original, newContext);
        this.mFragment = fragment;
        //createViewFromTag优先使用 mFactory2、mFactory、mPrivateFactory来创建 View，如果创建失败，则最终调用createView方法来创建。
        setFactory2(new ParallaxFactory(this));
    }

    /**
     * 由于LayoutInflater是单例模式，所以如果不克隆，后面的所有xml解析都会受到影响
     * @param newContext
     * @return
     */
    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new ParallaxLayoutInflater(this,newContext,mFragment);
    }


    /**
     * createViewFromTag优先使用 mFactory2、mFactory、mPrivateFactory来创建 View
     * 如果创建失败，则最终调用createView方法来创建。
     */
    class ParallaxFactory implements Factory2 {

        //通用的组件头
        private final String[] sClassPrefix = {
                "android.widget.",
                "android.view."
        };
        //自定义的属性列表
        int[] attrIds = {
                R.attr.a_in,
                R.attr.a_out,
                R.attr.x_in,
                R.attr.x_out,
                R.attr.y_in,
                R.attr.y_out};

        private LayoutInflater inflater;

        public ParallaxFactory(LayoutInflater inflater) {
//            Log.e(TAG, "ParallaxFactory: ");
            this.inflater = inflater;
        }

        @Nullable
        @Override
        public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
            Log.e(TAG, "onCreateView: ");

            View view = null;
            //先生成对应的View
            view = createMyView(name,context,attrs);

            //获取自定义属性
            if (view != null) {
                TypedArray a = context.obtainStyledAttributes(attrs, attrIds);
                if (a != null && a.length() > 0) {
                    //获取自定义属性的值
                    ParallaxViewTag tag = new ParallaxViewTag();
                    tag.alphaIn = a.getFloat(0, 0f);
                    tag.alphaOut = a.getFloat(1, 0f);
                    tag.xIn = a.getFloat(2, 0f);
                    tag.xOut = a.getFloat(3, 0f);
                    tag.yIn = a.getFloat(4, 0f);
                    tag.yOut = a.getFloat(5, 0f);

                    Log.e(TAG, "onCreateView: "+tag.toString());
                    //将自定义属性集合保存到tag中
                    view.setTag(R.id.parallax_view_tag,tag);
                }
                mFragment.getParallaxViews().add(view);
                a.recycle();
            }
            Log.e(TAG, "onCreateView: ");
            return view;
        }

        /**
         * 调用系统方法 创建view
         * @param name
         * @param context
         * @param attrs
         * @return
         */
        private View createMyView(String name, Context context, AttributeSet attrs) {
            View view = null;

            //包含. 则代表是自定义控件 eg: com.learning.xiaohongshu.myView
            if(name.contains(".")){
                return reflectView(name,null,context,attrs);
            }else{//系统自带控件
                for (String classPrefix : sClassPrefix) {
                    if((view = reflectView(name,classPrefix,context,attrs)) != null){
                        return view;
                    }
                }
            }
            return view;
        }

        private View reflectView(String name, String prefix, Context context, AttributeSet attrs) {
            try {
                //通过系统inflate去创建view
                return inflater.createView(name,prefix,attrs);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
            return null;
        }
    }
}
