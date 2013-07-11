package com.geek.exercise.transfer;

import com.geek.exercise.R;

/**
 * Created by Pequots34 on 7/11/13.
 */
public class Standing implements IStatus {

    public Standing() {
        super();
    }

    @Override
    public int getBanner() {
        return R.drawable.bg_standing;
    }

    @Override
    public int getColorState() {
        return R.color.standing_blue;
    }

    @Override
    public int getText() {
        return R.string.standing;
    }
}
