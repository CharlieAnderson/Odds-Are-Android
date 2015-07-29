package com.example.charlesanderson.oddsare;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by charlesanderson on 6/26/15.
 */
public class OddsAreApp extends Application {
    static final String TAG = "OddsAre";
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Parse.initialize(this, "IXkfPyf1UKOPZIqV84ysnNu2agSgDWlP4xXpE4lB", "4vjrGGx9S0viKyobmMHwAHJ9zq6YYKN0BvT70gdG");
        ParseFacebookUtils.initialize(this);
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
