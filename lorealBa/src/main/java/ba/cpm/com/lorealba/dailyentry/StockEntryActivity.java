package ba.cpm.com.lorealba.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gettersetter.FocusProductGetterSetter;
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


   //usk
   ExpandableListView lvExp_audit;
   List<FocusProductGetterSetter> listDataHeader;
    List<FocusProductGetterSetter> questionList;
    HashMap<FocusProductGetterSetter, List<FocusProductGetterSetter>> listDataChild;
    ExpandableListAdapter listAdapter;
    boolean checkflag = true;
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();

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

             //usk
              /*  lvExp_audit.clearFocus();
                lvExp_audit.invalidateViews();
                listAdapter.notifyDataSetChanged();
                if (validateData(listDataChild, listDataHeader)) {
                    db.open();
                  //  db.insertSalesStockData(jcpGetset, listDataChild, listDataHeader);
                    finish();
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                   *//* AlertDialog.Builder builder = new AlertDialog.Builder(FocusProductActivity.this);
                    builder.setTitle("Parinaam").setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertSalesStockData(jcpGetset, listDataChild, listDataHeader);
                            finish();
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();*//*
                } else {

                    Snackbar.make(lvExp_audit, "Please fill Sku Stock", Snackbar.LENGTH_SHORT).show();
                }*/
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

        lvExp_audit=(ExpandableListView)findViewById(R.id.lvExp_audit);


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


      /*  //usk
      //  prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        lvExp_audit.setAdapter(listAdapter);
        for (int i = 0; i < listAdapter.getGroupCount(); i++){
            lvExp_audit.expandGroup(i);
        }

        lvExp_audit.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lvExp_audit.invalidate();

                int lastItem = firstVisibleItem + visibleItemCount;

                if (firstVisibleItem == 0) {
                    storePOSM_fab.show();//.setVisibility(View.VISIBLE);
                } else if (lastItem == totalItemCount) {
                    storePOSM_fab.hide();//setVisibility(View.INVISIBLE);
                } else {
                    storePOSM_fab.show();//setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
                lvExp_audit.invalidateViews();
            }
        });

        // Listview Group click listener
        lvExp_audit.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;
            }
        });

        // Listview Group expanded listener
        lvExp_audit.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getWindow().getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview Group collasped listener
        lvExp_audit.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getWindow().getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview on child click listener
        lvExp_audit.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });
*/

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


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<FocusProductGetterSetter> _listDataHeader;
        private HashMap<FocusProductGetterSetter, List<FocusProductGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<FocusProductGetterSetter> listDataHeader,
                                     HashMap<FocusProductGetterSetter, List<FocusProductGetterSetter>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final FocusProductGetterSetter childText = (FocusProductGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_stock_entory, null);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.card_view);
                holder.ed_Stock = convertView.findViewById(R.id.ed_Stock);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TextView txtListChild = convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(childText.getSku());
            final ViewHolder finalHolder = holder;

/*
            holder.ed_Stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition))
                                .get(childPosition).setStock("");
                    } else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock(value1);
                    }

                }
            });
*/
            holder.stock_img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minteger = minteger + 1;
                    if (minteger ==0){
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock("0");
                    }else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock(minteger+"");
                    }

                }
            });

            holder.stock_img_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minteger = minteger - 1;
                    if (minteger<0){
                        Snackbar.make(lvExp_audit, "Please stock value less than zero.", Snackbar.LENGTH_SHORT).show();

                    }else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock(minteger+"");
                    }
                }
            });

          /*  holder.stock_img_plus.setOnClickListener(new View.OnClickListener() {
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
            });*/

            holder.ed_Stock.setText(childText.getStock());

            if (!checkflag) {
                boolean tempflag = false;
                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getStock().equals("")) {
                    tempflag = true;
                }
                if (tempflag) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
            } else {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            }

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final FocusProductGetterSetter headerTitle = (FocusProductGetterSetter) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_stock_entry, null);
            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setText(headerTitle.getBrand());
            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryLight));
                }
            } else {
                lblListHeader.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryLight));
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class ViewHolder {
      //  EditText ed_Stock;
        TextView ed_Stock;
        Button stock_img_plus,stock_img_minus;
        CardView cardView;
    }


/*
    private void prepareListData() {

        db.open();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        listDataHeader = db.getHeaderSalesData(jcpGetset);
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {
                questionList = db.getSalesStockInsertedData(jcpGetset, listDataHeader.get(i).getBrand_id());
                if (questionList.size() > 0) {
                    storeAudit_fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
                } else {
                    questionList = db.getSalesStockchildData(jcpGetset, listDataHeader.get(i).getBrand_id());
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }
*/


    boolean validateData(HashMap<FocusProductGetterSetter, List<FocusProductGetterSetter>> listDataChild2,
                         List<FocusProductGetterSetter> listDataHeader2) {
        boolean flag = false;
        checkHeaderArray.clear();
        loop1:
        for (int i = 0; i < listDataHeader2.size(); i++) {

            for (int j = 0; j < listDataChild2.get(listDataHeader.get(i)).size(); j++) {
                String stock = listDataChild.get(listDataHeader.get(i)).get(j).getStock();
                if (stock == null || stock.equalsIgnoreCase("")) {
                    if (!checkHeaderArray.contains(i)) {
                        checkHeaderArray.add(i);
                    }
                    flag = false;
                    break;
                }else {
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

    }


}
