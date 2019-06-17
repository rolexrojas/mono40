package com.tpago.movil.d.ui.qr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpago.movil.R;

public class QrTab extends LinearLayout {
    public QrTab(Context context) {
        super(context);
    }

    public QrTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.qr_tab, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QrTab, 0, 0);

        boolean isSelected = a.getBoolean(R.styleable.QrTab_isSelected, false);
        if (isSelected) {
            this.setBackground(context.getResources().getDrawable(R.drawable.qr_code_button_background));
        } else {
            this.setBackground(context.getResources().getDrawable(R.drawable.qr_code_button_background_unselected));
        }

        TextView textView = findViewById(R.id.text);
        textView.setText(a.getString(R.styleable.QrTab_text));
        if (!isSelected) {
            textView.setTextColor(context.getResources().getColor(R.color.my_qr_selected_tab_background));
        } else {
            textView.setTextColor(Color.WHITE);
        }
        a.recycle();
    }
}
