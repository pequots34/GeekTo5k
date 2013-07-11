package com.geek.exercise.utilities;

import android.content.Context;
import android.content.Intent;
import com.geek.exercise.ActivityStatusActivity;
import com.geek.exercise.MainActivity;

/**
 * Created by Pequots34 on 7/10/13.
 */
public class IntentUtils {

    private IntentUtils() {
        super();

        throw new InstantiationError();
    }

    public static Intent getMainIntent( Context context ) {
        Intent intent = new Intent( context, MainActivity.class );

        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        return intent;
    }

    public static Intent getActivityRecognitionIntent( Context context ) {
        Intent intent = new Intent( context, ActivityStatusActivity.class );

        return intent;
    }
}
