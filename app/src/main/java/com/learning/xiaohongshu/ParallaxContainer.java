package com.learning.xiaohongshu;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class ParallaxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "ParallaxContainer";
    private ViewPager mContainerViewPager = null;
    private List<ParallaxFragment> mfragments = new ArrayList<>();
    private ParallaxPageAdapter mViewPagerAdapter;
    private ImageView mPersonImage;

    public ParallaxContainer(@NonNull Context context) {
        this(context,null,0);
    }

    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setUp(int[] ids){
        ParallaxFragment tempFragment;
        for (int id : ids) {
            tempFragment = new ParallaxFragment();
            Bundle args = new Bundle();
            args.putInt("LayoutId",id);
            tempFragment.setArguments(args);
            mfragments.add(tempFragment);
        }

        MainActivity mainActivity = (MainActivity) getContext();
        mContainerViewPager = new ViewPager(getContext());
        //不设置id会报错  ViewPager with adapter requires a view id
        mContainerViewPager.setId(R.id.parallax_pager);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContainerViewPager.setLayoutParams(params);
        mViewPagerAdapter = new ParallaxPageAdapter(mainActivity.getSupportFragmentManager(), mfragments);
        mContainerViewPager.setAdapter(mViewPagerAdapter);
        mContainerViewPager.addOnPageChangeListener(this);

        addView(mContainerViewPager,0);
    }


    //在页面滚动的时候设置动效
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.e(TAG, "onPageScrolled: start ");
        int containerWidth = getWidth();

        ParallaxFragment outFragment = null;
        ParallaxFragment inFragment = null;
        try {
            outFragment = mfragments.get(position - 1);
        } catch (Exception e) {}

        try {
            inFragment = mfragments.get(position);
        } catch (Exception e) {}

        if(outFragment != null){
//            Log.e(TAG, "onPageScrolled: outFragment ");
            List<View> outViews = outFragment.getParallaxViews();

            if(outViews != null){
                for (View parallaxView : outViews) {
                    ParallaxViewTag tag = (ParallaxViewTag) parallaxView.getTag(R.id.parallax_view_tag);

                    if(tag == null) continue;


                    ViewHelper.setTranslationX(parallaxView,(containerWidth-positionOffsetPixels)*tag.xIn);
                    ViewHelper.setTranslationY(parallaxView,(containerWidth-positionOffsetPixels)*tag.yIn);
                }
            }
        }//end of if outFragment

        if(inFragment != null){
//            Log.e(TAG, "onPageScrolled: inFragment ");
            List<View> inViews = inFragment.getParallaxViews();

            if(inViews != null){
                for (View parallaxView : inViews) {
                    ParallaxViewTag tag = (ParallaxViewTag) parallaxView.getTag(R.id.parallax_view_tag);

                    if(tag == null){
                        continue;
                    }
                    Log.e(TAG, "onPageScrolled: "+ tag.xOut+""+tag.yOut);
                    ViewHelper.setTranslationX(parallaxView,0-positionOffsetPixels*tag.xOut);
                    ViewHelper.setTranslationY(parallaxView,0-positionOffsetPixels*tag.yOut);
                }
            }
        }

    }//end of onPageScrolled


    @Override
    public void onPageSelected(int position) {
        if(position == mfragments.size()-1){
            mPersonImage.setVisibility(GONE);
        }else{
            mPersonImage.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        AnimationDrawable animation = (AnimationDrawable) mPersonImage.getBackground();

        if(state == ViewPager.SCROLL_STATE_DRAGGING){
            animation.start();
            return;
        }

        if(state == ViewPager.SCROLL_STATE_IDLE){
            animation.stop();
        }
    }

    public void setPersonImage(ImageView image){
        mPersonImage = image;
    }
}
