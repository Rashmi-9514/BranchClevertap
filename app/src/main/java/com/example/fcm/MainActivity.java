package com.example.fcm;
import android.content.Context; //added by CleverTap Assistant

import com.clevertap.android.sdk.interfaces.OnInitCleverTapIDListener;
import com.example.getauditreport.ReportGenerate;                      //added by CleverTap Assistant

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;


public class MainActivity extends AppCompatActivity implements InAppNotificationButtonListener {

	private static final String TAG = "MainActivity";
	HashMap<String, Object> eventProperties = new HashMap<String, Object>();
	private String token;
	Object test=new Object();
	CleverTapAPI clevertapDefaultInstance;
	HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {

        Context context = getApplicationContext();   //added by CleverTap Assistant
		CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
		clevertapDefaultInstance.getCleverTapID(new OnInitCleverTapIDListener() {
			@Override
			public void onInitCleverTapID(final String cleverTapID) {
				// Callback on main thread
				Branch branch = Branch.getInstance();
				branch.setRequestMetadata("$clevertap_attribution_id",
						cleverTapID);
				Log.d("testingctid",cleverTapID);

				Branch.sessionBuilder(MainActivity.this).ignoreIntent(true).withCallback(branchReferralInitListener).init();
			}
		});
		eventProperties.put("Event Property_name ", "value");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        ReportGenerate.run(context);   //added by CleverTap Assistant
		CleverTapAPI.getDefaultInstance(this).setDebugLevel(CleverTapAPI.LogLevel.DEBUG);   //added by CleverTap Assistant
		CleverTapAPI.createNotificationChannel(getApplicationContext(),"Tester123","mychannel","lDescription",NotificationManager.IMPORTANCE_MAX,true);        //added by CleverTap Assistant

		Intent intent = getIntent();
		String id = intent.getStringExtra("extraKey1");
		String name = intent.getStringExtra("extraKey2");

		 clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(this);




		clevertapDefaultInstance.createNotificationChannel(this,"Tester","Push Template App Channel","Channel for Push Template App", NotificationManager.IMPORTANCE_HIGH,true);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String tmp = "";
			for (String key : bundle.keySet()) {
				Object value = bundle.get(key);
				tmp += key + ": " + value + "\n\n";
			}

		}


		Button bt=findViewById(R.id.event);
		bt.setText(id);
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);

		Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
	}
	private Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
		@Override
		public void onInitFinished(JSONObject linkProperties, BranchError error) {
			// do stuff with deep link data (nav to page, display content, etc)
		}
	};

	@Override
	public void onInAppButtonClick(HashMap<String, String> hashMap) {

		Toast.makeText(MainActivity.this,"Event ButtonClick is triggered ",Toast.LENGTH_LONG).show();
		if(hashMap != null){
			Log.d("hashmaptester",hashMap.toString());
		}
	}
	@Override
	protected void onStart() {
		super.onStart();

	}





}
