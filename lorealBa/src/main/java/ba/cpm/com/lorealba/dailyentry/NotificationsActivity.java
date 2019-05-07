package ba.cpm.com.lorealba.dailyentry;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
        context=this;
        db = new Lorealba_Database(context);
        db.open();
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
    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

        ArrayList<NotificationData> notificationList = new ArrayList<>();

        public NotificationAdapter(ArrayList<NotificationData> notificationList) {
            this.notificationList = notificationList;
        }

        @NonNull
        @Override
        public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder viewHolder, final int position) {

            viewHolder.title_txt.setText(notificationList.get(position).getTitle());
            viewHolder.body_txt.setText(notificationList.get(position).getBody());
            viewHolder.path_txt.setText(notificationList.get(position).getPath());

            viewHolder.card_notification_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getApplicationContext(), NotificationDetailActivity.class);
                    in.putExtra("Type", notificationList.get(position).getType());
                    in.putExtra("Title", notificationList.get(position).getTitle());
                    in.putExtra("Body", notificationList.get(position).getBody());
                    in.putExtra("Path", notificationList.get(position).getPath());
                    startActivity(in);
                }
            });
        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title_txt,path_txt,body_txt;
            CardView card_notification_adapter;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title_txt = (TextView)itemView.findViewById(R.id.title_txt);
                path_txt  = (TextView)itemView.findViewById(R.id.path_txt);
                body_txt  = (TextView)itemView.findViewById(R.id.body_txt);
                card_notification_adapter  = (CardView) itemView.findViewById(R.id.card_notification_adapter);

            }
        }
    }
}
