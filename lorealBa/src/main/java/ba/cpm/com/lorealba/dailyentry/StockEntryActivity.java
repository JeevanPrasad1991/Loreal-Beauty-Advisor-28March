package ba.cpm.com.lorealba.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.StockGetterSetter;

public class StockEntryActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_home, img_eyes, img_lips, img_nail, img_face, img_sample_stock, img_tester_stock, img_damaged_stock, img_invert_stock;
    String store_cd, visit_date, user_type, username, stock_type;
    private SharedPreferences preferences;
    FloatingActionButton storePOSM_fab;
    List<StockGetterSetter> stockList = new ArrayList<>();
    StockGetterSetter object = null;
    Lorealba_Database db;
    Context context;
    RecyclerView stock_recycle;
    int minteger = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_entry);
        context = this;
        posmUiData();

        storePOSM_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Parinaam").setMessage(R.string.alertsaveData);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.open();
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        StockEntryActivity.this.finish();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

    }

    private void posmUiData() {
        stock_recycle = (RecyclerView) findViewById(R.id.stock_recycle);

        storePOSM_fab = (FloatingActionButton) findViewById(R.id.storeAudit_fab);
        img_home = (ImageView) findViewById(R.id.img_home);
        img_eyes = (ImageView) findViewById(R.id.img_eyes);

        img_lips = (ImageView) findViewById(R.id.img_lips);
        img_nail = (ImageView) findViewById(R.id.img_nail);
        img_face = (ImageView) findViewById(R.id.img_face);
        ImageView header_icon = (ImageView) findViewById(R.id.header_icon);

        img_sample_stock = (ImageView) findViewById(R.id.img_sample_stock);
        img_tester_stock = (ImageView) findViewById(R.id.img_tester_stock);
        img_damaged_stock = (ImageView) findViewById(R.id.img_damaged_stock);
        img_invert_stock = (ImageView) findViewById(R.id.img_invert_stock);
        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);


        img_home.setOnClickListener(this);
        img_eyes.setOnClickListener(this);
        img_lips.setOnClickListener(this);
        img_nail.setOnClickListener(this);
        img_face.setOnClickListener(this);
        img_sample_stock.setOnClickListener(this);
        img_tester_stock.setOnClickListener(this);
        img_damaged_stock.setOnClickListener(this);
        img_invert_stock.setOnClickListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_ID, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);

        stock_type = getIntent().getStringExtra(CommonString.KEY_STOCK_TYPE);

        if (stock_type.equals("1")) {
            textv_sample.setText(("Stock Entry"));
            header_icon.setImageDrawable(getResources().getDrawable(R.drawable.daily_stock36));
        } else if (stock_type.equals("2")) {
            textv_sample.setText(("Damaged Stock"));
            header_icon.setImageDrawable(getResources().getDrawable(R.drawable.damaged36));
        } else {
            textv_sample.setText(("Stock Inward"));
            header_icon.setImageDrawable(getResources().getDrawable(R.drawable.inward36));
        }

        db = new Lorealba_Database(context);
        db.open();
        prepareListData();

    }

    private void prepareListData() {
        db.open();
        object = new StockGetterSetter();
        if (stock_type.equals("2")) {
            object = new StockGetterSetter();
            object.setSku("Pure Clay Mask (Green)");
            object.setStock_entry_qty("");
            object.setPo_stock_qty("2");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("White Perfect Day cream 50 ml");
            object.setPo_stock_qty("3");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Baby Lips candy wow Cherry");
            object.setPo_stock_qty("4");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Baby Lips candy wow Raspberry");
            object.setPo_stock_qty("0");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Pure Clay Mask (Blue)");
            object.setPo_stock_qty("6");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Clay Mask EC + EC Clay Sh 175 ml");
            object.setPo_stock_qty("7");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("White Perfect Scrub 100 ml");
            object.setPo_stock_qty("10");
            object.setStock_entry_qty("");
            stockList.add(object);


            object = new StockGetterSetter();
            object.setSku("Pink Perfect Scrub 100 ml");
            object.setPo_stock_qty("0");
            object.setStock_entry_qty("");
            stockList.add(object);

        } else if (stock_type.equals("1")) {

            object = new StockGetterSetter();
            object.setSku("Pure Clay Mask (Green)");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("White Perfect Day cream 50 ml");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Baby Lips candy wow Cherry");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Baby Lips candy wow Raspberry");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Pure Clay Mask (Blue)");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Clay Mask EC + EC Clay Sh 175 ml");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("White Perfect Scrub 100 ml");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Pink Perfect Scrub 100 ml");
            object.setPo_stock_qty("0");
            object.setStock_entry_qty("");
            stockList.add(object);

        } else {

            object = new StockGetterSetter();
            object.setSku("Pure Clay Mask (Green)");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("White Perfect Day cream 50 ml");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Baby Lips candy wow Cherry");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Baby Lips candy wow Raspberry");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Pure Clay Mask (Blue)");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Clay Mask EC + EC Clay Sh 175 ml");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("White Perfect Scrub 100 ml");
            object.setStock_entry_qty("");
            stockList.add(object);

            object = new StockGetterSetter();
            object.setSku("Pink Perfect Scrub 100 ml");
            object.setPo_stock_qty("0");
            object.setStock_entry_qty("");
            stockList.add(object);
        }

        stock_recycle.setAdapter(new ValueAdapter(context, stockList, stock_type));
        stock_recycle.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_home:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                StockEntryActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;

            case R.id.img_sample_stock:
                startActivity(new Intent(context, SampleTesterStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                StockEntryActivity.this.finish();
                break;
            case R.id.img_tester_stock:
                startActivity(new Intent(context, SampleTesterStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                StockEntryActivity.this.finish();
                break;
            case R.id.img_damaged_stock:
                startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                StockEntryActivity.this.finish();
                break;

            case R.id.img_invert_stock:
                startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "3"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                StockEntryActivity.this.finish();
                break;

        }

    }


    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<StockGetterSetter> data;
        String report_tpe;

        public ValueAdapter(Context context, List<StockGetterSetter> data, String report_tpe) {
            inflator = LayoutInflater.from(context);
            this.report_tpe = report_tpe;
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.stock_row, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final StockGetterSetter current = data.get(position);

            if (stock_type.equals("1")) {
                holder.rl_stock_entry.setVisibility(View.VISIBLE);
                holder.rl_stock_entry.setId(position);

                holder.rl_stock_received.setVisibility(View.GONE);
                holder.rl_stock_received.setId(position);

                holder.sku_textv.setText(current.getSku());
                holder.sku_textv.setId(position);


                holder.stock_img_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minteger = minteger + 1;
                        holder.stock_text_value.setText("" + minteger);
                        holder.stock_text_value.setId(position);
                    }
                });

                holder.stock_img_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minteger = minteger - 1;
                        holder.stock_text_value.setText("" + minteger);
                        holder.stock_text_value.setId(position);
                    }
                });

            } else if (stock_type.equals("2")) {
                holder.rl_stock_entry.setVisibility(View.GONE);
                holder.rl_stock_entry.setId(position);

                holder.rl_stock_received.setVisibility(View.VISIBLE);
                holder.rl_stock_received.setId(position);

                holder.sku_text_recieved.setText(current.getSku());
                holder.sku_text_recieved.setId(position);

                holder.pos_stock.setText(current.getPo_stock_qty());
                holder.pos_stock.setId(position);


                holder.pos_stock_img_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minteger = minteger + 1;
                        holder.pos_stock_text_value.setText("" + minteger);
                        holder.pos_stock_text_value.setId(position);
                    }
                });

                holder.pos_stock_img_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minteger = minteger - 1;
                        holder.pos_stock_text_value.setText("" + minteger);
                        holder.pos_stock_text_value.setId(position);
                    }
                });


            } else {
                holder.rl_stock_entry.setVisibility(View.VISIBLE);
                holder.rl_stock_entry.setId(position);

                holder.rl_stock_received.setVisibility(View.GONE);
                holder.rl_stock_received.setId(position);

                holder.sku_textv.setText(current.getSku());
                holder.sku_textv.setId(position);

                holder.stock_img_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minteger = minteger + 1;
                        holder.stock_text_value.setText("" + minteger);
                        holder.stock_text_value.setId(position);
                    }
                });

                holder.stock_img_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minteger = minteger - 1;
                        holder.stock_text_value.setText("" + minteger);
                        holder.stock_text_value.setId(position);
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            Button stock_img_plus, stock_img_minus, pos_stock_img_plus, pos_stock_img_minus;
            TextView sku_textv, sku_text_recieved, pos_stock, stock_text_value, pos_stock_text_value;
            LinearLayout rl_stock_entry, rl_stock_received;
            CardView cardView;

            public MyViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                rl_stock_entry = (LinearLayout) itemView.findViewById(R.id.rl_stock_entry);
                rl_stock_received = (LinearLayout) itemView.findViewById(R.id.rl_stock_received);

                sku_textv = (TextView) itemView.findViewById(R.id.sku_textv);
                sku_text_recieved = (TextView) itemView.findViewById(R.id.sku_text_recieved);
                pos_stock = (TextView) itemView.findViewById(R.id.pos_stock);

                stock_text_value = (TextView) itemView.findViewById(R.id.stock_text_value);
                pos_stock_text_value = (TextView) itemView.findViewById(R.id.pos_stock_text_value);

                stock_img_plus = (Button) itemView.findViewById(R.id.stock_img_plus);
                stock_img_minus = (Button) itemView.findViewById(R.id.stock_img_minus);
                pos_stock_img_plus = (Button) itemView.findViewById(R.id.pos_stock_img_plus);
                pos_stock_img_minus = (Button) itemView.findViewById(R.id.pos_stock_img_minus);


            }
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        StockEntryActivity.this.finish();
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
}
