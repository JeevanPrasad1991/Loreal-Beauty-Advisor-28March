package ba.cpm.com.lorealba.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.StockPwpGwpDatum;

public class PwpGwpActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private SharedPreferences preferences;
    Lorealba_Database db;
    Context context;
    String store_cd = "1", visit_date, user_type, username, stock_type;
    String sigature_id;
    RecyclerView stock_recycle;
    ArrayList<StockPwpGwpDatum> stockList=new ArrayList<>();;
    private ValueAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwp_gwp);
        getUiId();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.open();

                db.insertPwpGwpData(store_cd, visit_date, stockList);
                finish();

            }
        });
    }

    private void getUiId() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        stock_recycle = (RecyclerView) findViewById(R.id.stock_recycle);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // store_cd = preferences.getString(CommonString.KEY_STORE_ID, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        stock_type = getIntent().getStringExtra(CommonString.KEY_STOCK_TYPE);
        sigature_id = getIntent().getStringExtra(CommonString.KEY_SIGNETURE_ID);
        db = new Lorealba_Database(context);
        db.open();

        stockList = db.getPwpGwpData(store_cd);

        if (stockList.size() > 0) {

            adapter = new ValueAdapter(getApplicationContext(), stockList, stock_type);
            stock_recycle.setAdapter(adapter);
            stock_recycle.setLayoutManager(new LinearLayoutManager(context));
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PwpGwpActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PwpGwpActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);

    }



    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<StockPwpGwpDatum> data;
        String report_tpe;

        public ValueAdapter(Context context, List<StockPwpGwpDatum> data, String report_tpe) {
            inflator = LayoutInflater.from(context);
            this.report_tpe = report_tpe;
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.pwp_gwplist, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder holder, final int position) {
            final StockPwpGwpDatum current = data.get(position);

            holder.sku_textv.setText(current.getProductName() + "");
            holder.stock_text_value.setText(" QTY = " + current.getStock() + "");

            holder.stock_img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int minteger = current.getStock();
                    ++minteger;
                    current.setStock(minteger);
                    stock_recycle.invalidate();
                    adapter.notifyDataSetChanged();

                }
            });

            holder.stock_img_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    stock_recycle.invalidate();
                    adapter.notifyDataSetChanged();
                    int minteger = current.getStock();
                    --minteger;
                    current.setStock(minteger);


                }
            });

            holder.pos_stock_text_value.setText(current.getStock() + "");

        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            Button stock_img_plus, stock_img_minus;
            TextView sku_textv, stock_text_value, pos_stock_text_value;

            CardView cardView;

            public MyViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                sku_textv = (TextView) itemView.findViewById(R.id.sku_textv);
                stock_text_value = (TextView) itemView.findViewById(R.id.stock_text_value);
                pos_stock_text_value = (TextView) itemView.findViewById(R.id.pos_stock_text_value);
                stock_img_plus = (Button) itemView.findViewById(R.id.stock_img_plus);
                stock_img_minus = (Button) itemView.findViewById(R.id.stock_img_minus);

            }
        }
    }

}
