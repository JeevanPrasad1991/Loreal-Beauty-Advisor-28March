package ba.cpm.com.lorealba.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.InwardSalesPO;
import ba.cpm.com.lorealba.gsonGetterSetter.NonStockReason;
import ba.cpm.com.lorealba.gsonGetterSetter.ProductMaster;

public class InwardStockActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private SharedPreferences preferences;
    Lorealba_Database db;
    Context context;
    String store_cd = "1", visit_date, user_type, username, stock_type;
    //ArrayList<ProductMaster> productMasters = new ArrayList<>();
    String sigature_id;
  //  boolean checkflag = true;
   // List<ProductMaster> listDataHeader;
    // HashMap<ProductMaster, List<ProductMaster>> listDataChild;
    List<ProductMaster> questionList;
    //   ArrayList<Integer> checkHeaderArray = new ArrayList<>();

    RecyclerView stock_recycle;
    ArrayList<InwardSalesPO> stockList=new ArrayList<>();;
    ArrayList<InwardSalesPO> stockTesterList=new ArrayList<>();;
    private ValueAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inword_stock);

        getUiId();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db.open();
                db.UpdatInwardStockData(store_cd, stockList);
                db.UpdatInwardTesterStockData(store_cd, stockTesterList);
                db.insertInwardData(store_cd, visit_date, stockList);
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

        stockList = db.getInwordData(visit_date,"Sellable", store_cd);
        stockTesterList = db.getInwordData(visit_date,"Sellable", store_cd);

        if (stockList.size() > 0) {

            adapter = new ValueAdapter(getApplicationContext(), stockList, stock_type);
            stock_recycle.setAdapter(adapter);
            stock_recycle.setLayoutManager(new LinearLayoutManager(context));
        }

        //  prepareListData();


    }

/*
    private void prepareListData() {
        db.open();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        listDataHeader = db.getcategory_wise_brand_fromStockInword();
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {
                questionList = db.getStockInwordInsertedData(store_cd, listDataHeader.get(i).getSubAxeName());
                if (questionList.size() > 0) {
                    fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
                } else {
                    questionList = db.getbrand_wise_sku_fromStockInwordData(listDataHeader.get(i).getSubAxeName());
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }
*/

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InwardStockActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        InwardStockActivity.this.finish();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(InwardStockActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            InwardStockActivity.this.finish();
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

   /* boolean validateData(HashMap<ProductMaster, List<ProductMaster>> listDataChild2,
                         List<ProductMaster> listDataHeader2) {
        boolean flag = false;
        checkHeaderArray.clear();
        loop1:
        for (int i = 0; i < listDataHeader2.size(); i++) {

            for (int j = 0; j < listDataChild2.get(listDataHeader.get(i)).size(); j++) {
                int stock = listDataChild.get(listDataHeader.get(i)).get(j).getStock_receive();
                // if (stock == null || stock.equalsIgnoreCase("")) {
                if (stock == 0) {
                    if (!checkHeaderArray.contains(i)) {
                        checkHeaderArray.add(i);
                    }
                    flag = false;
                    break;
                } else {
                    flag = true;
                }

            }
            if (!flag) {
                break;
            }
        }
        if (flag) {
            return checkflag = true;
        } else {

            return checkflag = false;
        }

    }*/


    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<InwardSalesPO> data;
        String report_tpe;

        public ValueAdapter(Context context, List<InwardSalesPO> data, String report_tpe) {
            inflator = LayoutInflater.from(context);
            this.report_tpe = report_tpe;
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.stock_inwordlist, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final InwardSalesPO current = data.get(position);

            final int posSale = current.getQTY();
            holder.sku_textv.setText(current.getProductName() + "");
            holder.mrp.setText("  MRP = " + current.getMRP() + "");
            holder.stock_text_value.setText(" QTY = " + current.getQTY() + "");
           // current.getMRP();
            current.setMRP(current.getMRP());

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
            if (current.getStock() >posSale) {

                holder.pos_stock_text_value.setTextColor(Color.RED);
                holder.rl_stock_reason.setVisibility(View.GONE);
            } else if (current.getStock() <posSale){

                holder.rl_stock_reason.setVisibility(View.VISIBLE);
                holder.pos_stock_text_value.setTextColor(Color.BLACK);
            }else {
                holder.rl_stock_reason.setVisibility(View.GONE);
                holder.pos_stock_text_value.setTextColor(Color.BLACK);
            }


            final ArrayList<NonStockReason> reason_list = db.getNonStockData();
            NonStockReason non = new NonStockReason();
            non.setReason("-Select Reason-");
            non.setReasonId(0);
            reason_list.add(0, non);

            holder.sp_sppiner.setAdapter(new ReasonSpinnerAdapter(InwardStockActivity.this, R.layout.spinner_text_view, reason_list));
            for (int i = 0; i < reason_list.size(); i++) {
                if (reason_list.get(i).getReasonId().equals(current.getReasonId())) {
                    holder.sp_sppiner.setSelection(i);
                    break;
                }
            }
            holder.sp_sppiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    NonStockReason ans = reason_list.get(pos);
                    current.setReasonId(ans.getReasonId());
                    current.setReason(ans.getReason());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            Button stock_img_plus, stock_img_minus;
            TextView sku_textv, stock_text_value, pos_stock_text_value, mrp;
            LinearLayout rl_stock, rl_stock_reason;
            Spinner sp_sppiner;
            CardView cardView;

            public MyViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                sku_textv = (TextView) itemView.findViewById(R.id.sku_textv);
                stock_text_value = (TextView) itemView.findViewById(R.id.stock_text_value);
                pos_stock_text_value = (TextView) itemView.findViewById(R.id.pos_stock_text_value);
                mrp = (TextView) itemView.findViewById(R.id.mrp);
                stock_img_plus = (Button) itemView.findViewById(R.id.stock_img_plus);
                stock_img_minus = (Button) itemView.findViewById(R.id.stock_img_minus);
                rl_stock = (LinearLayout) itemView.findViewById(R.id.rl_stock);
                rl_stock_reason = (LinearLayout) itemView.findViewById(R.id.rl_stock_reason);
                sp_sppiner = (Spinner) itemView.findViewById(R.id.sp_sppiner);

            }
        }
    }

    public class ReasonSpinnerAdapter extends ArrayAdapter<NonStockReason> {
        List<NonStockReason> list;
        Context context;
        int resourceId;

        public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<NonStockReason> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            NonStockReason cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getReason());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            NonStockReason cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getReason());

            return view;
        }

    }


}
