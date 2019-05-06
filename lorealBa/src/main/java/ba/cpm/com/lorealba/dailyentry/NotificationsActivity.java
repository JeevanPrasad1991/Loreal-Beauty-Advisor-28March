package ba.cpm.com.lorealba.dailyentry;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.gsonGetterSetter.NotificationData;
import ba.cpm.com.lorealba.gsonGetterSetter.NotificationsGetterSetter;

public class NotificationsActivity extends AppCompatActivity {
    NotificationsGetterSetter object = null;
    RecyclerView drawer_layout_recycle_store;
    ArrayList<NotificationsGetterSetter> offerlist = new ArrayList<>();
    ImageView img_home;
    Context context;
    Lorealba_Database db;
    RecyclerView notification_view;
    NotificationAdapter mAdapter;
    ArrayList<NotificationData> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        db = new Lorealba_Database(context);
        notification_view = (RecyclerView) findViewById(R.id.notification_view);
        createNotificationView();
    }

    private void createNotificationView() {
        db.open();
        notificationList = db.getNotificationList();
        if(notificationList.size() > 0) {
            mAdapter = new NotificationAdapter(notificationList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            notification_view.setLayoutManager(mLayoutManager);
            notification_view.setAdapter(mAdapter);
        }
    }

}
