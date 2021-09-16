package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.myapplication.R;

public class SlideAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.splash,
            R.drawable.splash1,
            R.drawable.splash3
    };

    public String[] slide_titles = {
            "Your gateway to great shopping",
            "Your gateway to great shopping",
            "Your gateway to great shopping"
    };

    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_screen, container, false);
        RelativeLayout bg = (RelativeLayout) view.findViewById(R.id.slideBG);
        TextView tv = (TextView) view.findViewById(R.id.slideTitle);
        bg.setBackgroundResource(slide_images[position]);
        tv.setText(slide_titles[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
