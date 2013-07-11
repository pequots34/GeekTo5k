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
    public int getBannerResource() {
        return R.drawable.bg_standing;
    }

    @Override
    public int getColorStateResource() {
        return R.color.standing_blue;
    }

    @Override
    public int getTextResource() {
        return R.string.standing;
    }
}
