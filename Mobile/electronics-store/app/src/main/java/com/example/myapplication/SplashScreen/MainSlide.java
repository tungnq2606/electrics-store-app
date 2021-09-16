package com.example.myapplication.SplashScreen;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapter.SlideAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Screen.BottomNav;
import com.example.myapplication.Screen.SignInScreen;

public class MainSlide extends AppCompatActivity {
    private SlideAdapter adapter;
    private ViewPager pager;
    private LinearLayout dotLayout;
    private TextView[] dots;
    private Button nextButton, backButton;
    private int currentPage;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_slide);
        pager = findViewById(R.id.slidePager);
        dotLayout = findViewById(R.id.dotLayout);
        nextButton = findViewById(R.id.slideNextButton);
        backButton = findViewById(R.id.slideBackButton);
        backButton.setVisibility(View.INVISIBLE);
        typeface = ResourcesCompat.getFont(this, R.font.sfprodisplaybold);
        adapter = new SlideAdapter(this);
        pager.setAdapter(adapter);
        addDotIndicator(0);
        pager.addOnPageChangeListener(viewListener);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(currentPage - 1);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < dots.length - 1) {
                    pager.setCurrentItem(currentPage + 1);
                } else {
                    Intent intent = new Intent(MainSlide.this, BottomNav.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void addDotIndicator(int position) {
        dots = new TextView[3];
        dotLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dots));

            dotLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);
            currentPage = position;

            if (currentPage == 0) {
                backButton.setVisibility(View.INVISIBLE);
                backButton.setEnabled(false);
                nextButton.setEnabled(true);
//
                nextButton.setText("Next");
                backButton.setText("");
                nextButton.setTypeface(typeface);
                backButton.setTypeface(typeface);
            } else if (currentPage == dots.length - 1) {
                backButton.setVisibility(View.VISIBLE);
                backButton.setEnabled(true);
                nextButton.setEnabled(true);
//
                nextButton.setText("get started");
                backButton.setText("back");
                nextButton.setTypeface(typeface);
                backButton.setTypeface(typeface);
            } else {
                backButton.setVisibility(View.VISIBLE);
                backButton.setEnabled(true);
                nextButton.setEnabled(true);
//
                nextButton.setText("Next");
                backButton.setText("Back");
                nextButton.setTypeface(typeface);
                backButton.setTypeface(typeface);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}