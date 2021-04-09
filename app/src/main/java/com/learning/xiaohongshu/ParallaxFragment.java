package com.learning.xiaohongshu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ParallaxFragment extends Fragment {

    private List<View> mParallaxViews = new ArrayList<>();

    public List<View> getParallaxViews() {
        return mParallaxViews;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ParallaxLayoutInflater myInflater = new ParallaxLayoutInflater(inflater,getActivity(),this);

        //布局id由参数传递过来
        int layoutId = getArguments().getInt("LayoutId");

        return myInflater.inflate(layoutId,null);
    }
}
