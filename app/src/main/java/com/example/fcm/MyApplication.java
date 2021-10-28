package com.example.fcm;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.CleverTapInstanceConfig;
import com.clevertap.android.sdk.interfaces.OnInitCleverTapIDListener;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;
import com.clevertap.android.sdk.pushnotification.amp.CTPushAmpListener;


import org.json.JSONObject;

import java.util.HashMap;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class MyApplication extends Application   implements CTPushAmpListener,  CTPushNotificationListener {

    @Override
    public void onCreate() {
        CleverTapAPI.setDebugLevel(3);
        MainActivity ma=new MainActivity();

        ActivityLifecycleCallback.register(this);
        CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());

        cleverTapAPI.setCTPushAmpListener(this);



        super.onCreate();

        Branch.enableLogging();

        Branch.getAutoInstance(this);
    }
    private Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
        @Override
        public void onInitFinished(JSONObject linkProperties, BranchError error) {
            // do stuff with deep link data (nav to page, display content, etc)
        }
    };
    @Override
    public void onNotificationClickedPayloadReceived(HashMap<String, Object> payload) {
//        Log.d("prrrrrrr",payload.toString());
//        Intent i = new Intent(getApplicationContext(),SecondActivity.class);
//        startActivity(i);
        for (String key : payload.keySet()) {

            System.out.println(key + " : " + payload.get(key).toString());
            if(key.equals("wzrk_id")) {
                NotificationManager notifyMgr =
                        (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                notifyMgr.cancel(Integer.parseInt(payload.get(key).toString()) );
            }

        }

    }

@Override
public void onPushAmpPayloadReceived(Bundle bundle) {
    //write push notification rendering logic here
}
}
