package ba.cpm.com.lorealba.dailyentry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.InvoiceGetterSetter;

public class SaleTrackingActivity extends AppCompatActivity implements View.OnClickListener {
    String store_cd, visit_date, username;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    ArrayList<InvoiceGetterSetter> invoice_List = new ArrayList<>();
    ImageView btn_saletra_add_performa;
    Lorealba_Database db;
    Context context;
    RecyclerView sale_tracking_recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_tracking);
        context = this;
        db = new Lorealba_Database(context);
        db.open();
        iduserinterface();
    }

    private void iduserinterface() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_ID, "");
        store_cd = "2";
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, "");

        btn_saletra_add_performa = (ImageView) findViewById(R.id.btn_saletra_add_performa);
        sale_tracking_recycle = (RecyclerView) findViewById(R.id.sale_tracking_recycle);
        ImageView img_daily_sales = (ImageView) findViewById(R.id.img_daily_sales);
        ImageView img_promotion = (ImageView) findViewById(R.id.img_promotion);
        ImageView bottom_posm = (ImageView) findViewById(R.id.bottom_posm);
        ImageView img_home = (ImageView) findViewById(R.id.img_home);
        btn_saletra_add_performa.setOnClickListener(this);

        img_daily_sales.setOnClickListener(this);
        img_promotion.setOnClickListener(this);
        bottom_posm.setOnClickListener(this);
        img_home.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new Lorealba_Database(context);
        db.open();
        invoice_List = db.getsalestrackingList(store_cd, visit_date);
        if (invoice_List.size() > 0) {
            sale_tracking_recycle.setAdapter(new ValueAdapter(context, invoice_List));
            sale_tracking_recycle.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_saletra_add_performa:
                startActivity(new Intent(context, ProformaInvoiceActivity.class));
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                break;

            case R.id.img_home:
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                this.finish();
                break;

            case R.id.img_daily_sales:
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                this.finish();
                break;

            case R.id.bottom_posm:
                startActivity(new Intent(context, PromotionActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                this.finish();
                break;

            case R.id.img_promotion:
                startActivity(new Intent(context, PromotionActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                this.finish();
                break;
        }
    }


    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<InvoiceGetterSetter> data;

        public ValueAdapter(Context context, List<InvoiceGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.sale_trcking_adapter, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final InvoiceGetterSetter current = data.get(position);

            holder.sale_trcking_name.setText(current.getCustomer_name() + " - Mobile No. : " + current.getMobile_no());
            holder.sale_trcking_name.setId(position);

            holder.sales_count.setText("Count : " + current.getQuantity());
            holder.sales_count.setId(position);

            holder.sale_total_amount.setText("Total Amount : " + current.getTotal_amount());
            holder.sale_total_amount.setId(position);

            holder.sale_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView sale_trcking_name, sales_count, sale_total_amount;
            CardView sale_card;

            public MyViewHolder(View itemView) {
                super(itemView);
                sale_card = (CardView) itemView.findViewById(R.id.sale_card);
                sale_trcking_name = (TextView) itemView.findViewById(R.id.sale_trcking_name);
                sales_count = (TextView) itemView.findViewById(R.id.sales_count);
                sale_total_amount = (TextView) itemView.findViewById(R.id.sale_total_amount);
            }
        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        this.finish();
    }
}
