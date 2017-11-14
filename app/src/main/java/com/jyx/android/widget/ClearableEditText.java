package com.jyx.android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jyx.android.R;

/**
 * Author : Tree
 * Date : 2016-01-23
 */
public class ClearableEditText extends EditText implements View.OnTouchListener,
        View.OnFocusChangeListener, TextWatcher {
    //	private static final String TAG = ClearableEditText.class.getSimpleName();
    private Drawable xD;
    private OnTouchListener l;
    private OnFocusChangeListener f;

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                    .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(isNotEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    private boolean isNotEmpty(Editable text) {
        if (text != null && text.toString().length() > 0) {
            return true;
        }
        return false;
    }

    private boolean isNotEmpty(CharSequence text) {
        if (text != null && text.length() > 0) {
            return true;
        }
        return false;
    }

    private void init() {
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = getResources().getDrawable(R.drawable.edit_clear);
        }
//		LogUtils.i(TAG, "height: " + xD.getIntrinsicHeight());
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable x = visible ? xD : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start,
                              int lengthBefore, int lengthAfter) {
        if (isFocused()) {
            setClearIconVisible(isNotEmpty(text));
        }
    }

}
