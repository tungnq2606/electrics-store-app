package com.example.myapplication.Screen;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail extends AppCompatActivity {
    private List<String> detailList;
    private LinearLayout details;
    private ImageView back, favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        details = findViewById(R.id.listDetail);
        back = findViewById(R.id.detailBackBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        detailList = new ArrayList<>();
        detailList.add("38mm or 42mm case");
        detailList.add("Second-generation Retina OLED display, 1000 nits");
        detailList.add("Ion-X glass display");
        detailList.add("GPS model");
        detailList.add("S3 SiP with dual-core processor; W2 wireless chip");
        detailList.add("Digital Crown");
        detailList.add("Optical heart sensor");
        detailList.add("High and low heart rate notifications and irregular heart rhythm notification");
        detailList.add("Emergency SOS");
        detailList.add("Water resistant 50 meters");
        detailList.add("Wi-Fi and Bluetooth 4.2");

        for (int i = 0; i < detailList.size(); i++) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            TextView dot = new TextView(this);
            dot.setText(Html.fromHtml("&#8226;"));
            dot.setTextSize(16);
            dot.setPadding(0, 15, 20, 0);

            TextView detailTitle = new TextView(this);
            detailTitle.setText(detailList.get(i));
            detailTitle.setTextSize(14);
            detailTitle.setPadding(0, 15, 0, 0);

            layout.addView(dot);
            layout.addView(detailTitle);

            details.addView(layout);
        }

    }
}