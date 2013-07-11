package com.geek.exercise.transfer;

import com.geek.exercise.R;

/**
 * Created by Pequots34 on 7/11/13.
 */
public class OnFoot implements IStatus {

    public OnFoot() {
        super();
    }

    @Override
    public int getBannerResource() {
        return R.drawable.bg_on_foot;
    }

    @Override
    public int getColorStateResource() {
        return R.color.on_foot_orange;
    }

    @Override
    public int getTextResource() {
        return R.string.on_foot;
    }
}
