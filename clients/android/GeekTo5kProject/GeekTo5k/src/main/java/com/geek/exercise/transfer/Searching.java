package com.geek.exercise.transfer;

import com.geek.exercise.R;

/**
 * Created by Pequots34 on 7/11/13.
 */
public class Searching implements IStatus {

    public Searching() {
        super();
    }

    @Override
    public int getBannerResource() {
        return R.drawable.bg_searching;
    }

    @Override
    public int getColorStateResource() {
        return R.color.searching;
    }

    @Override
    public int getTextResource() {
        return R.string.searching;
    }
}
