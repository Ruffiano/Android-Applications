package com.example.user.demo.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 21.01.2016.
 */
public class RobotoBoldTextView extends TextView {

    public RobotoBoldTextView(Context context) {
        super(context);
        init();
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        try {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Bold.ttf");
            setTypeface(tf);
        } catch (Exception e) {
        }
    }
}
