package ba.cpm.com.lorealba.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.dailyentry.DealerBoardActivity;

public class MessageService extends FirebaseMessagingService {

    String TAG = "FCM";
    public static  int  NOTIFICATION_ID = 1;
    String body="",path="",title="",visited_date="",type;
    public MessageService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        body  = remoteMessage.getData().get("body");
        title = remoteMessage.getData().get("title");
        path  = remoteMessage.getData().get("path");
        visited_date  = remoteMessage.getData().get("Currentdate");
        type  = remoteMessage.getData().get("type");
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data " + remoteMessage.getData());
            Lorealba_Database db = new Lorealba_Database(getApplicationContext());
            db.open();
            long value = db.insertNotificationData(title,body,path,visited_date,type);

           // scheduleJob(remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            showNotification(body, getApplicationContext(),title,path);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }



//    private void scheduleJob(Map<String, String> data) {
//        Bundle bundle = new Bundle();
//        for (Map.Entry<String, String> entry : data.entrySet()) {
//            bundle.putString(entry.getKey(), entry.getValue());
//        }
//
//        FirebaseJobDispatcher dispatcher =
//                new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(DataBase.class)
//                .setTag("deals-job")
//                .setExtras(bundle)
//                .build();
//        dispatcher.schedule(myJob);
//    }


    private void showNotification(String body, Context ctx, String title, String path) {


      /*  // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.,
                text, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(ctx, "Title", eventtext,
                contentIntent);

        // Send the notification.
        mNotificationManager.notify("Title", 0, notification);*/



//        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.image1);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "2")
//                .setSmallIcon(R.drawable.image1)
//                .setContentTitle("Promotion Image")
//                .setContentText(body)
//                .setLargeIcon(icon)
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(icon)
//                        .bigLargeIcon(null));
               /* .setSmallIcon(R.drawable.image1)
                .setContentTitle("title")
                .setContentText(eventtext)
                .setColor(Color.parseColor("#009add"))
                .setAutoCancel(true);*/
//
//        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(
//                NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel("1", "My_Channel", NotificationManager.IMPORTANCE_HIGH);
//
//            notificationManager.createNotificationChannel(mChannel);
//        }
//
//        notificationManager.notify(0, mBuilder.build());


        Intent intent = new Intent(this, DealerBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(CommonString.KEY_BODY,body);
        intent.putExtra(CommonString.KEY_PATH,path);
        intent.putExtra(CommonString.KEY_TITLE,title);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri  sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "2")
                .setSmallIcon(R.drawable.image1)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);

        if(NOTIFICATION_ID > 1787258476){
            NOTIFICATION_ID = 0;
        }

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }
}
