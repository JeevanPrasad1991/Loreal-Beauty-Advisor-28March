package intelre.cpm.com.intelre.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.InfoTypeMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;

public class MarketInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String brand = "", brand_cd = "", category = "", category_cd = "", infotype_cd = "", infotype = "";
    String store_cd, visit_date, user_type, username, Error_Message;
    INTEL_RE_DB db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    EditText remark_edtM;
    TextView market_typeTXT;
    ImageView marketi_img;
    RecyclerView recycl_marketinfo;
    FloatingActionButton fab, btn_add_marketi;
    Spinner market_spin_infotype, spinM_brand;
    String _pathforcheck, _path, img1 = "";
    boolean checkflag = true;
    ArrayAdapter<CharSequence> adapter;
    MyAdapter myAdapter;
    ArrayAdapter<CharSequence> adapterforinfotype;
    ArrayList<BrandMaster> brandmasterList = new ArrayList<>();
    ArrayList<InfoTypeMaster> infotypeList = new ArrayList<>();
    ArrayList<InfoTypeMaster> secCompleteMarketDATA = new ArrayList<>();
    boolean addflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_info);
        marketinfoUI();
        fab = findViewById(R.id.fab);
        btn_add_marketi = findViewById(R.id.btn_add_marketi);
        validateDATA();
        setDataToListView();
        btn_add_marketi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (duplicateValue(brand_cd, infotype_cd)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MarketInfoActivity.this);
                        builder.setTitle("Parinaam").setMessage("Do you want to add data");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addflag = true;
                                InfoTypeMaster info = new InfoTypeMaster();
                                info.setBrand(brand);
                                info.setBrand_cd(brand_cd);
                                info.setType(category);
                                info.setType_cd(category_cd);
                                info.setInfoTypeId(Integer.valueOf(infotype_cd));
                                info.setInfoType(infotype);
                                info.setRemark(remark_edtM.getText().toString().replaceAll("[-@.?/|=+_#%:;^*()!&^<>{},'$0]", ""));
                                info.setMarketinfo_img(img1);
                                secCompleteMarketDATA.add(info);
                                myAdapter = new MyAdapter(MarketInfoActivity.this, secCompleteMarketDATA);
                                recycl_marketinfo.setAdapter(myAdapter);
                                recycl_marketinfo.setLayoutManager(new LinearLayoutManager(MarketInfoActivity.this));
                                myAdapter.notifyDataSetChanged();
                                Snackbar.make(btn_add_marketi, "Data has been added", Snackbar.LENGTH_SHORT).show();
                                remark_edtM.setText("");
                                remark_edtM.setHint("");
                                img1 = "";
                                marketi_img.setImageResource(R.mipmap.camera_orange);
                                market_spin_infotype.setSelection(0);
                                spinM_brand.setSelection(0);
                                market_typeTXT.setText("");
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        Snackbar.make(btn_add_marketi, "This " + brand + " and " + infotype + " alredy exist. Please select another ", Snackbar.LENGTH_LONG).show();

                    }

                } else {
                    Snackbar.make(btn_add_marketi, Error_Message, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secCompleteMarketDATA.size() > 0 && addflag) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MarketInfoActivity.this);
                    builder.setTitle("Parinaam").setMessage("Do you want to save data");
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertMarketinfoData(store_cd, visit_date, secCompleteMarketDATA);
                            Snackbar.make(btn_add_marketi, "Data has been saved", Snackbar.LENGTH_SHORT).show();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            MarketInfoActivity.this.finish();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                    builder.show();
                } else {
                    Snackbar.make(fab, "Please add first", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void marketinfoUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        remark_edtM = findViewById(R.id.remark_edtM);
        spinM_brand = findViewById(R.id.spinM_brand);
        marketi_img = findViewById(R.id.marketi_img);
        market_typeTXT = findViewById(R.id.market_typeTXT);
        recycl_marketinfo = findViewById(R.id.recycl_marketinfo);
        market_spin_infotype = findViewById(R.id.market_spin_infotype);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        getSupportActionBar().setTitle("Market info - " + visit_date);
        db = new INTEL_RE_DB(this);
        db.open();
    }

    private void validateDATA() {
        db.open();
        brandmasterList = db.getbranddataformarketinfo();
        if (brandmasterList.size() > 0) {
            adapter = new ArrayAdapter<>(MarketInfoActivity.this, R.layout.spinner_custom_item);
            adapter.add("Select  Brand");
            for (int i = 0; i < brandmasterList.size(); i++) {
                adapter.add(brandmasterList.get(i).getBrand());
            }
            adapter.setDropDownViewResource(R.layout.spinner_custom_item);
            spinM_brand.setAdapter(adapter);
            spinM_brand.setOnItemSelectedListener(MarketInfoActivity.this);

            infotypeList = db.getInfoTypeData();
            if (infotypeList.size() > 0) {
                adapterforinfotype = new ArrayAdapter<>(MarketInfoActivity.this, R.layout.spinner_custom_item);
                adapterforinfotype.add("Select Info Type");
                for (int j = 0; j < infotypeList.size(); j++) {
                    adapterforinfotype.add(infotypeList.get(j).getInfoType());
                }
            }
            adapterforinfotype.setDropDownViewResource(R.layout.spinner_custom_item);
            market_spin_infotype.setAdapter(adapterforinfotype);
            market_spin_infotype.setOnItemSelectedListener(MarketInfoActivity.this);
        }
        marketi_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _pathforcheck = store_cd + "_" + brand_cd + "_MARKETINFOING_" + visit_date.replace("/", "") +
                        "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                startCameraActivity();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spinM_brand:
                if (position != 0) {
                    brand = brandmasterList.get(position - 1).getBrand();
                    brand_cd = brandmasterList.get(position - 1).getBrandId().toString();
                    category = brandmasterList.get(position - 1).getCategory();
                    category_cd = brandmasterList.get(position - 1).getCategoryId().toString();
                    market_typeTXT.setTypeface(null, Typeface.BOLD);
                    market_typeTXT.setText(category);
                } else {
                    brand = "";
                    brand_cd = "";
                    category = "";
                    category_cd = "";
                }
            case R.id.market_spin_infotype:
                if (position != 0) {
                    infotype = infotypeList.get(position - 1).getInfoType();
                    infotype_cd = infotypeList.get(position - 1).getInfoTypeId().toString();
                } else {
                    infotype_cd = "";
                    infotype = "";
                }
                break;
        }

    }

    private boolean validate() {
        boolean status = true;
        if (spinM_brand.getSelectedItem().toString().equalsIgnoreCase("Select Brand")) {
            Error_Message = "Please Select Brand";
            status = false;
        } else if (market_spin_infotype.getSelectedItem().toString().equalsIgnoreCase("Select Info Type")) {
            Error_Message = "Please select Info Type";
            status = false;
        } else if (img1.equals("")) {
            Error_Message = "Please click Market info image";
            status = false;
        }
        return status;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MarketInfoActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MarketInfoActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    finish();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        }

        return super.onOptionsItemSelected(item);
    }

    protected void startCameraActivity() {
        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:


                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                        marketi_img.setImageResource(R.mipmap.camera_green);
                        img1 = _pathforcheck;
                        _pathforcheck = "";
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void setDataToListView() {
        try {
            secCompleteMarketDATA = db.getinfotypeinsetedDATA(store_cd);
            if (secCompleteMarketDATA.size() > 0) {
                Collections.reverse(secCompleteMarketDATA);
                myAdapter = new MyAdapter(this, secCompleteMarketDATA);
                recycl_marketinfo.setAdapter(myAdapter);
                recycl_marketinfo.setLayoutManager(new LinearLayoutManager(MarketInfoActivity.this));
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching", e.toString());
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        Context context;
        ArrayList<InfoTypeMaster> insertedlist_Data;

        MyAdapter(Context context, ArrayList<InfoTypeMaster> insertedlist_Data) {
            inflator = LayoutInflater.from(context);
            this.context = context;
            this.insertedlist_Data = insertedlist_Data;

        }

        @Override
        public int getItemCount() {
            return insertedlist_Data.size();

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.secondary_adapter, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.status.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (insertedlist_Data.get(position).getKey_id() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MarketInfoActivity.this);
                        builder.setMessage("Are you sure you want to Delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                insertedlist_Data.remove(position);
                                                notifyDataSetChanged();
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(MarketInfoActivity.this, insertedlist_Data);
                                                    recycl_marketinfo.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MarketInfoActivity.this);
                        builder.setMessage("Are you sure you want to Delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                String listid = insertedlist_Data.get(position).getKey_id();
                                                db.remove(listid);
                                                insertedlist_Data.remove(position);
                                                notifyDataSetChanged();
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(MarketInfoActivity.this,
                                                            insertedlist_Data);
                                                    recycl_marketinfo.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }
            });

            holder.brand_name.setText(insertedlist_Data.get(position).getBrand());
            holder.type.setText(insertedlist_Data.get(position).getType());
            holder.info_type.setText(insertedlist_Data.get(position).getInfoType());
            holder.brand_name.setId(position);
            holder.type.setId(position);
            holder.info_type.setId(position);
            holder.status.setId(position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView brand_name, type, info_type;
            ImageView status;

            public MyViewHolder(View convertView) {
                super(convertView);
                brand_name = convertView.findViewById(R.id.brand_name);
                type = convertView.findViewById(R.id.type);
                info_type = convertView.findViewById(R.id.info_type);
                status = convertView.findViewById(R.id.imgDelRow);
            }
        }
    }

    private boolean duplicateValue(String brand_cd, String infotype_cd) {
        boolean status = true;
        if (secCompleteMarketDATA.size() > 0) {
            for (int i = 0; i < secCompleteMarketDATA.size(); i++) {
                if (secCompleteMarketDATA.get(i).getBrand_cd().toString().equals(brand_cd)
                        && secCompleteMarketDATA.get(i).getInfoTypeId().toString().equals(infotype_cd)) {
                    status = false;
                    break;

                }
            }
        }
        return status;
    }

}
