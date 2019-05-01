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

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.gsonGetterSetter.NotificationsGetterSetter;

public class NotificationsActivity extends AppCompatActivity {
    NotificationsGetterSetter object = null;
    RecyclerView drawer_layout_recycle_store;
    ArrayList<NotificationsGetterSetter> offerlist = new ArrayList<>();
    ImageView img_home;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        context = this;
        img_home = (ImageView) findViewById(R.id.img_home);
        drawer_layout_recycle_store = (RecyclerView) findViewById(R.id.drawer_layout_recycle_store);

        validateAdapterData();

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                NotificationsActivity.this.finish();
            }
        });
    }

    void validateAdapterData() {
        object = new NotificationsGetterSetter();
        object.setDate("20 March 19");
        object.setNotifications("Product Training on New Product is on 10Th April At HO.");
        object.setSendar("Jeevan Rana");
        offerlist.add(object);

        object = new NotificationsGetterSetter();
        object.setDate("25 March 19");
        object.setNotifications("Target for the Month april of 50,000 INR.");
        object.setSendar("Santosh");
        offerlist.add(object);

        object = new NotificationsGetterSetter();
        object.setDate("29 March 19");
        object.setNotifications("Product training on new product is on 20 May at HO.");
        object.setSendar("Mukesh");
        offerlist.add(object);

        drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, offerlist));
        drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));

    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<NotificationsGetterSetter> data;

        public ValueAdapter(Context context, List<NotificationsGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.notifications_row, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder holder, final int position) {
            final NotificationsGetterSetter current = data.get(position);

            holder.text_visit_date.setText(current.getDate());
            holder.text_visit_date.setId(position);

            holder.notification_text.setText(current.getNotifications());
            holder.notification_text.setId(position);

            holder.sendar_txt.setText(current.getSendar());
            holder.sendar_txt.setId(position);


        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView text_visit_date, notification_text, sendar_txt;

            public MyViewHolder(View itemView) {
                super(itemView);

                notification_text = (TextView) itemView.findViewById(R.id.notification_text);
                text_visit_date = (TextView) itemView.findViewById(R.id.text_visit_date);
                sendar_txt = (TextView) itemView.findViewById(R.id.sendar_txt);
            }
        }
    }
}
