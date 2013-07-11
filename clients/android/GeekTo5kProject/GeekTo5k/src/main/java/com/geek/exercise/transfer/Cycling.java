package com.geek.exercise.transfer;

import com.geek.exercise.R;

/**
 * Created by Pequots34 on 7/11/13.
 */
public class Cycling implements IStatus {

    public Cycling() {
        super();
    }

    @Override
    public int getBannerResource() {
        return R.drawable.bg_cycling;
    }

    @Override
    public int getColorStateResource() {
        return R.color.cycling_green;
    }

    @Override
    public int getTextResource() {
        return R.string.cycling;
    }
}
