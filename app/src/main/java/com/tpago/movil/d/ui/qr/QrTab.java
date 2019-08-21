package com.tpago.movil.d.ui.qr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpago.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrTab extends LinearLayout {
    @BindView(R.id.textView)
    TextView textView;

    public QrTab(Context context) {
        super(context);
    }

    public QrTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.qr_tab, this, true);
        ButterKnife.bind(this, view);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QrTab, 0, 0);

        boolean isSelected = a.getBoolean(R.styleable.QrTab_isSelected, false);

        setIsSelected(isSelected);

        textView.setText(a.getString(R.styleable.QrTab_text));
        a.recycle();
    }

    public void setIsSelected(boolean isSelected) {
        if (isSelected) {
            textView.setTextColor(Color.WHITE);
            this.setBackground(getContext().getResources().getDrawable(R.drawable.qr_code_button_background));
        } else {
            textView.setTextColor(getContext().getResources().getColor(R.color.my_qr_selected_tab_background));
            this.setBackground(getContext().getResources().getDrawable(R.drawable.qr_code_button_background_unselected));
        }
    }
}
