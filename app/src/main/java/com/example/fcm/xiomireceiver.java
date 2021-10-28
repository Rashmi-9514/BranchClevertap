package com.example.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.Utils;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class xiomireceiver extends PushMessageReceiver {
    public static final String FCM_PARAM = "picture";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private int numMessages = 0;
    CleverTapAPI clevertapDefaultInstance;



    private void sendNotification(Context ctx, Bundle data) {
        Bundle bundle = new Bundle();

        bundle.putBundle("bundle",data);
        Intent intent = new Intent(ctx, SecondActivity.class);
        intent.putExtras(bundle);
        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(ctx);
        //	Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx, ctx.getString(R.string.notification_channel_id))
                .setContentTitle(data.getString("nm"))
                .setContentText(data.getString("nt"))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                .setContentIntent(pendingIntent)
                .setContentInfo("Hello")
                .setColor(ctx.getColor(R.color.colorAccent))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.ic_notification);


        try {
            String picture = data.getString("wzrk_bp");
            if (picture != null && !"".equals(picture)) {
                URL url = new URL(picture);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(data.getString("wzrk_nms"))
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ctx.getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

        try {
            String ctData = message.getContent();
            Bundle extras = Utils.stringToBundle(ctData);


                        CleverTapAPI.createNotification(context, extras);




            //	sendNotification(context,extras);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
String mRegId;
        // Log.d("MYPushtest", "Error parsing FCM message",miPushCommandMessage.getCategory().toString());
        Log.d("tester",
                "onReceiveRegisterResult is called. " + miPushCommandMessage.toString());
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;

                if(CleverTapAPI.getDefaultInstance(context) != null){
                    CleverTapAPI.getDefaultInstance(context) .pushXiaomiRegistrationId(mRegId,true);
                  //  Toast.makeText(context,mRegId,Toast.LENGTH_LONG).show();
                    Log.d("token",
                            "xiaomi token " +mRegId);
                }else{
                    Log.e("TAG","CleverTap is NULL");
                }
            } else {

            }
        } else {
            log = miPushCommandMessage.getReason();
        }
    }
}
