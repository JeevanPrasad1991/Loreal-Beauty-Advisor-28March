package ba.cpm.com.lorealba.dailyentry;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.gsonGetterSetter.NotificationData;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

        ArrayList<NotificationData> notificationList = new ArrayList<>();

        public NotificationAdapter(ArrayList<NotificationData> notificationList) {
            this.notificationList = notificationList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

            viewHolder.title_txt.setText(notificationList.get(position).getTitle());
            viewHolder.body_txt.setText(notificationList.get(position).getBody());
            viewHolder.path_txt.setText(notificationList.get(position).getPath());
        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title_txt,path_txt,body_txt;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title_txt = (TextView)itemView.findViewById(R.id.title_txt);
                path_txt  = (TextView)itemView.findViewById(R.id.path_txt);
                body_txt  = (TextView)itemView.findViewById(R.id.body_txt);

            }
        }
}
