package ba.cpm.com.lorealba.dailyentry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.delegates.NavMenuItemGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.JourneyPlan;

public class StoreEntryActivity extends AppCompatActivity {
    Lorealba_Database db;
    ValueAdapter adapter;
    RecyclerView recyclerView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_entry);
        context = this;
        uivalidate();
        db = new Lorealba_Database(this);
    }

    private void uivalidate() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.drawer_layout_recycle_store);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_ID, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        setTitle("Store Entry");
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        //brandMasterArrayList = db.getRspDetailData(store_cd);
        adapter = new ValueAdapter(getApplicationContext(), getdata());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        ////////////Start SERVICE for uploading Images
        //   Intent svc = new Intent(this, BackgroundService.class);
        //startService(svc);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        this.finish();
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<NavMenuItemGetterSetter> data = Collections.emptyList();

        public ValueAdapter(Context context, List<NavMenuItemGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.custom_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder viewHolder, final int position) {
            final NavMenuItemGetterSetter current = data.get(position);
            viewHolder.icon.setImageResource(current.getIconImg());
            viewHolder.icon_txtname.setText(current.getIconName());
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (current.getIconImg() == R.drawable.daily_stock) {
                        startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.damaged_stock) {
                        startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.inward_stock) {
                        startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "3"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.customer_wise_sales) {
                        startActivity(new Intent(context, SaleTrackingActivity.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.posm_tracking) {

                        startActivity(new Intent(context, PosmTracking.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.promotion_tracking) {
                        startActivity(new Intent(context, PosmTracking.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.sample_stock) {

                        startActivity(new Intent(context, SampleTesterStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.tester_stock) {
                        startActivity(new Intent(context, SampleTesterStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView icon_txtname;

            public MyViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.list_icon);
                icon_txtname = (TextView) itemView.findViewById(R.id.icon_txtname);
            }
        }

    }

    public List<NavMenuItemGetterSetter> getdata() {
        List<NavMenuItemGetterSetter> data = new ArrayList<>();
        int daily_stock, damaged_stock, inword_stock, posm_tracking, promotion_tracking, sample_stock, tester_stock, custumerwise_sales;

        daily_stock = R.drawable.daily_stock;
        damaged_stock = R.drawable.damaged_stock;
        inword_stock = R.drawable.inward_stock;
        posm_tracking = R.drawable.posm_tracking;
        promotion_tracking = R.drawable.promotion_tracking;
        sample_stock = R.drawable.sample_stock;
        tester_stock = R.drawable.tester_stock;
        custumerwise_sales = R.drawable.customer_wise_sales;

        int img[] = {daily_stock, damaged_stock, inword_stock, posm_tracking, promotion_tracking, sample_stock, tester_stock, custumerwise_sales};
        String name[] = {"Daily Stock", "Damaged Stock", "Inword Stock", "Posm Tracking", "Promotion Tracking", "Sample Stock", "Tester Stock", "Customerwise Sales"};
        for (int i = 0; i < img.length; i++) {
            NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
            recData.setIconImg(img[i]);
            recData.setIconName(name[i]);
            data.add(recData);
        }

        return data;
    }


}
