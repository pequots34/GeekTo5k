package com.geek.exercise.transfer;

import com.geek.exercise.R;

/**
 * Created by Pequots34 on 7/11/13.
 */
public class Vehicle implements IStatus {

    public Vehicle() {
        super();
    }

    @Override
    public int getBanner() {
        return R.drawable.bg_vehicle;
    }

    @Override
    public int getColorState() {
        return R.color.vehicle_red;
    }

    @Override
    public int getText() {
        return R.string.vehicle;
    }
}
