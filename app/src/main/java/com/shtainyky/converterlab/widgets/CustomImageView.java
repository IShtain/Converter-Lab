package com.shtainyky.converterlab.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.shtainyky.converterlab.R;

public class CustomImageView extends android.support.v7.widget.AppCompatImageButton{
    private static final int[] STATE_INCREASED = {R.attr.state_increased};
    private boolean isIncreased;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIncrease(boolean isIncreased)
    {
        this.isIncreased = isIncreased;
        refreshDrawableState();
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace)
    {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isIncreased)
        {
            mergeDrawableStates(drawableState, STATE_INCREASED);
        }
        return drawableState;
    }




}