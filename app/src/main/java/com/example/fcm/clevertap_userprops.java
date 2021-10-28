package com.example.fcm;

import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.sdk.CleverTapAPI;

import java.util.HashMap;

public class clevertap_userprops extends AppCompatActivity {
    CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

    public void Name(String name) {
        HashMap name_hm = new HashMap();
        name_hm.put("Name", name);

        clevertapDefaultInstance.onUserLogin(name_hm);

    }
}
