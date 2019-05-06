package ba.cpm.com.lorealba.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.AlertandMessages;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.delegates.CoverageBean;
import ba.cpm.com.lorealba.gsonGetterSetter.InvoiceGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.ProductMaster;
import ba.cpm.com.lorealba.retrofit.PostApi;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProformaInvoiceActivity extends AppCompatActivity implements View.OnClickListener {
    String subaxename = "", brand_name = "", brand_Id = "0", product_name = "", product_Id = "0", product_mrp = "0", store_cd, visit_date, username, store_name, store_address;
    ArrayAdapter<CharSequence> category_reason_adapter, brand_reason_adapter, sku_reason_adapter;
    ;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;

    ArrayList<ProductMaster> ProductMasterList = new ArrayList<>();
    ArrayList<ProductMaster> BrandMasterList = new ArrayList<>();
    Lorealba_Database db;
    EditText edt_customer, edt_mobile_no;
    Button btn_scan, btn_enter_code, btn_sku_list;
    RecyclerView drawer_layout_recycle_store;
    MultiPurposeDialog multiPurposeDialog;
    public ArrayList<InvoiceGetterSetter> selected_list = new ArrayList<>();
    InvoiceGetterSetter object;
    String product_scan_code = "";
    FloatingActionButton fab;
    TextView qrcode_text, sku_name, stock_text_value, txt_history;
    EditText edt_scan_code;
    CardView card_layout_scan, card_layout_title;
    CheckBox mobile_checkbox, no_name_checkbox;
    boolean no_name_flag = false, no_mobile_no_flag = false;
    ProgressDialog loading;
    Context context;
    int minteger = 0;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performa_invoice);
        context = this;
        iduserinterface();
    }


    private void iduserinterface() {
        db = new Lorealba_Database(context);
        db.open();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_ID, "");
        store_cd = "2";
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        store_name = preferences.getString(CommonString.KEY_STORE_NAME, "");
        store_name = "A One Beauty Store";
        store_address = preferences.getString(CommonString.KEY_STORE_ADDRESS, "");
        store_address = "147,Kalkaji,New Delhi-29";

        ImageView img_home = (ImageView) findViewById(R.id.img_home);
        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);

        textv_sample.setText("Sales Tracking");
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_enter_code = (Button) findViewById(R.id.btn_enter_code);
        btn_sku_list = (Button) findViewById(R.id.btn_sku_list);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        edt_customer = (EditText) findViewById(R.id.edt_customer);
        edt_mobile_no = (EditText) findViewById(R.id.edt_mobile_no);
        txt_history = (TextView) findViewById(R.id.txt_history);
        drawer_layout_recycle_store = (RecyclerView) findViewById(R.id.drawer_layout_recycle_store);

        mobile_checkbox = (CheckBox) findViewById(R.id.mobile_checkbox);
        no_name_checkbox = (CheckBox) findViewById(R.id.no_name_checkbox);

        card_layout_scan = (CardView) findViewById(R.id.card_layout_scan);
        card_layout_title = (CardView) findViewById(R.id.card_layout_title);

        radioSexGroup = (RadioGroup) findViewById(R.id.radioGrp);

        btn_scan.setOnClickListener(this);
        btn_enter_code.setOnClickListener(this);
        btn_sku_list.setOnClickListener(this);
        fab.setOnClickListener(this);
        img_home.setOnClickListener(this);
        mobile_checkbox.setOnClickListener(this);
        no_name_checkbox.setOnClickListener(this);
        txt_history.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_history:
                if (edt_mobile_no.getText().toString().isEmpty()) {
                    Snackbar.make(btn_scan, "Please enter mobile number.", Snackbar.LENGTH_SHORT).show();
                } else if (edt_mobile_no.getText().toString().length() < 10) {
                    Snackbar.make(btn_scan, "Please enter atleast 10 digit contact number", Snackbar.LENGTH_SHORT).show();
                } else if (checkindex()) {
                    if (db.getConsumer_date(edt_mobile_no.getText().toString()).size() > 0) {
                        show_consumer_previous_histry(context, edt_mobile_no.getText().toString());
                    } else {
                        Snackbar.make(btn_scan, "No Previous Consumer History", Snackbar.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.mobile_checkbox:
                if (mobile_checkbox.isChecked()) {
                    no_mobile_no_flag = true;
                    edt_mobile_no.setText("0000000000");
                } else {
                    edt_mobile_no.setText("");
                    no_mobile_no_flag = false;
                }
                break;
            case R.id.no_name_checkbox:
                if (no_name_checkbox.isChecked()) {
                    no_name_flag = true;
                    edt_customer.setText("No Name");
                } else {
                    edt_customer.setText("");
                    no_name_flag = false;
                }
                break;
            case R.id.btn_scan:
                if (!no_name_flag && edt_customer.getText().toString().isEmpty()) {
                    Snackbar.make(btn_scan, "Please enter customer name.", Snackbar.LENGTH_SHORT).show();
                } else if (!no_mobile_no_flag && edt_mobile_no.getText().toString().isEmpty()) {
                    Snackbar.make(btn_scan, "Please enter mobile number.", Snackbar.LENGTH_SHORT).show();
                } else if (!no_mobile_no_flag && edt_mobile_no.getText().toString().length() < 10) {
                    Snackbar.make(btn_scan, "Please enter atleast 10 digit contact number", Snackbar.LENGTH_SHORT).show();
                } else {
                    show_proforma_dialog(context, true);
                }

                break;

            case R.id.btn_enter_code:
                if (!no_name_flag && edt_customer.getText().toString().isEmpty()) {
                    Snackbar.make(btn_scan, "Please enter customer name.", Snackbar.LENGTH_SHORT).show();
                } else if (!no_mobile_no_flag && edt_mobile_no.getText().toString().isEmpty()) {
                    Snackbar.make(btn_scan, "Please enter mobile number.", Snackbar.LENGTH_SHORT).show();
                } else if (!no_mobile_no_flag && edt_mobile_no.getText().toString().length() < 10) {
                    Snackbar.make(btn_scan, "Please enter atleast 10 digit contact number", Snackbar.LENGTH_SHORT).show();
                } else {
                    show_proforma_dialog(context, false);
                }
                break;

            case R.id.fab:
                if (selected_list.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Parinaam").setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                //region Coverage Data
                                if (checkindex()) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("MobileNumber", edt_mobile_no.getText().toString());
                                    getotp_from_mobile_no(jsonObject.toString());
                                } else {
                                    startActivity(new Intent(context, InvoiceReportProformaActivity.class).putExtra(CommonString.TAG_OBJECT, selected_list).putExtra(CommonString.TAG_OTP, "0"));
                                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                    ProformaInvoiceActivity.this.finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Snackbar.make(fab, "Please add first sale_trcking_name", Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_home:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                ProformaInvoiceActivity.this.finish();
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

            case R.id.btn_sku_list:
                if (!no_name_flag && edt_customer.getText().toString().isEmpty()) {
                    Snackbar.make(btn_scan, "Please enter customer name.", Snackbar.LENGTH_SHORT).show();
                } else if (!no_mobile_no_flag && edt_mobile_no.getText().toString().isEmpty()) {
                    Snackbar.make(btn_scan, "Please enter mobile number.", Snackbar.LENGTH_SHORT).show();
                } else if (!no_mobile_no_flag && edt_mobile_no.getText().toString().length() < 10) {
                    Snackbar.make(btn_scan, "Please enter atleast 10 digit contact number", Snackbar.LENGTH_SHORT).show();
                } else {
                    show_dialog_forsku_categorywise(context);
                }
                break;
        }

    }


    private void show_proforma_dialog(final Context context, final boolean flag_for_rl) {
        minteger = 0;
        final MultiPurposeDialog multiPurposeDialog = new MultiPurposeDialog(context);
        multiPurposeDialog.setContentView(R.layout.custom_bar_code_dialog);
        multiPurposeDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(multiPurposeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        multiPurposeDialog.getWindow().setAttributes(lp);
        multiPurposeDialog.setCancelable(false);

        LinearLayout rl_bar_code = (LinearLayout) multiPurposeDialog.findViewById(R.id.rl_bar_code);
        LinearLayout RL_scan_code = (LinearLayout) multiPurposeDialog.findViewById(R.id.RL_scan_code);
        Button check_exist_sku = (Button) multiPurposeDialog.findViewById(R.id.check_exist_sku);
        TextView txt_bar_code = multiPurposeDialog.findViewById(R.id.txt_bar_code);
        TextView txt_enterbar_code = multiPurposeDialog.findViewById(R.id.txt_enterbar_code);
        sku_name = multiPurposeDialog.findViewById(R.id.sku_name);

        if (flag_for_rl) {
            RL_scan_code.setVisibility(View.GONE);
            rl_bar_code.setVisibility(View.VISIBLE);
            txt_bar_code.setVisibility(View.VISIBLE);
            txt_enterbar_code.setVisibility(View.GONE);
        } else {
            rl_bar_code.setVisibility(View.GONE);
            txt_bar_code.setVisibility(View.GONE);
            RL_scan_code.setVisibility(View.VISIBLE);
            txt_enterbar_code.setVisibility(View.VISIBLE);
        }

        qrcode_text = (TextView) multiPurposeDialog.findViewById(R.id.qrcode_text);
        edt_scan_code = (EditText) multiPurposeDialog.findViewById(R.id.edt_scan_code);
        final Button stock_img_minus = (Button) multiPurposeDialog.findViewById(R.id.stock_img_minus);
        Button stock_img_plus = (Button) multiPurposeDialog.findViewById(R.id.stock_img_plus);
        stock_text_value = (TextView) multiPurposeDialog.findViewById(R.id.stock_text_value);

        ImageView img_bar_code = (ImageView) multiPurposeDialog.findViewById(R.id.img_bar_code);
        ImageView cancet_btn = (ImageView) multiPurposeDialog.findViewById(R.id.cancet_btn);
        final ImageView btn_add = (ImageView) multiPurposeDialog.findViewById(R.id.btn_add);

        stock_img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger = minteger + 1;
                stock_text_value.setText("" + minteger);
            }
        });

        stock_img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minteger == 0) {
                    Snackbar.make(stock_img_minus, "Product quantity should not be less than Zero", Snackbar.LENGTH_SHORT).show();
                } else {
                    minteger = minteger - 1;
                    stock_text_value.setText("" + minteger);
                }
            }
        });

        cancet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiPurposeDialog.dismiss();
                sku_name.setText("");
                stock_text_value.setText("");
                qrcode_text.setText("");
                product_scan_code = "";
                product_name = "";
                product_mrp = "";
                product_Id = "0";
                sku_name.setVisibility(View.GONE);
            }
        });

        check_exist_sku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag_for_rl && edt_scan_code.getText().toString().isEmpty()) {
                    Snackbar.make(btn_add, "Please Enter Bar Code.", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (db.getsku_fromproductusing_eancode(product_scan_code).size() == 0) {
                        edt_scan_code.setText("");
                        Snackbar.make(btn_add, "Invalid Bar Code.Please try again.", Snackbar.LENGTH_SHORT).show();
                    } else {
                        show_dialog_forsku(context, db.getsku_fromproductusing_eancode(product_scan_code));
                    }
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);


                if (flag_for_rl && qrcode_text.getText().toString().isEmpty()) {
                    Snackbar.make(btn_add, "Please Scan Bar Code.", Snackbar.LENGTH_SHORT).show();
                } else if (!flag_for_rl && edt_scan_code.getText().toString().isEmpty()) {
                    Snackbar.make(btn_add, "Please Enter Bar Code.", Snackbar.LENGTH_SHORT).show();
                } else if (stock_text_value.getText().toString().equals("0")) {
                    Snackbar.make(btn_add, "Please add atleast One Product Quantity.", Snackbar.LENGTH_SHORT).show();
                } else {
                    object = new InvoiceGetterSetter();
                    if (checkduplicate_entry()) {
                        if (!flag_for_rl) {
                            product_scan_code = edt_scan_code.getText().toString();
                            if (db.getsku_fromproductusing_eancode(product_scan_code).size() == 0) {
                                Snackbar.make(btn_add, "Product not found", Snackbar.LENGTH_SHORT).show();
                            } else {
                                minteger = 0;
                                multiPurposeDialog.cancel();
                                object.setScan_ean_code_or_enterd_ean_code(product_scan_code);
                                object.setCustomer_name(edt_customer.getText().toString());
                                object.setMobile_no(edt_mobile_no.getText().toString());
                                object.setQuantity(stock_text_value.getText().toString());
                                object.setCustomer_gender(radioSexButton.getText().toString());
                                object.setProduct(product_name);
                                object.setProduct_Id(product_Id);
                                object.setProduct_rate(product_mrp);

                                selected_list.add(object);

                                stock_text_value.setText("");
                                qrcode_text.setText("");
                                edt_scan_code.setText("");
                                product_scan_code = "";
                                product_name = "";
                                product_mrp = "";
                                product_Id = "0";
                                sku_name.setText("");
                                sku_name.setVisibility(View.GONE);
                                drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, selected_list));
                                drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
                            }
                        } else {
                            multiPurposeDialog.cancel();
                            minteger = 0;
                            object.setScan_ean_code_or_enterd_ean_code(product_scan_code);
                            object.setCustomer_name(edt_customer.getText().toString());
                            object.setMobile_no(edt_mobile_no.getText().toString());
                            object.setQuantity(stock_text_value.getText().toString());
                            object.setCustomer_gender(radioSexButton.getText().toString());
                            object.setProduct(product_name);
                            object.setProduct_Id(product_Id);
                            object.setProduct_rate(product_mrp);

                            selected_list.add(object);
                            sku_name.setText("");
                            stock_text_value.setText("");
                            qrcode_text.setText("");
                            product_scan_code = "";
                            product_name = "";
                            product_mrp = "";
                            product_Id = "0";
                            sku_name.setVisibility(View.GONE);
                            drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, selected_list));
                            drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));

                        }
                    }
                }
            }
        });

        img_bar_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanner();
            }
        });
        multiPurposeDialog.show();
    }


    private void openScanner() {
        IntentIntegrator intentIntegrator = new IntentIntegrator((Activity) context);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan the barcode or QR code to get the data");
        intentIntegrator.initiateScan();
    }

    public class MultiPurposeDialog extends Dialog {
        public MultiPurposeDialog(Context context) {
            super(context);
            // DIALOG USER_INTERFACE TEMPLATE
            WindowManager.LayoutParams wmLayoutParams = getWindow().getAttributes();
            wmLayoutParams.gravity = Gravity.CENTER;
            getWindow().setAttributes(wmLayoutParams);
            setTitle(null);
            setCancelable(false);
            setOnCancelListener(null);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("ScanActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                if (db.getsku_fromproductusing_eancode(result.getContents()).size() > 0) {
                    show_dialog_forsku(context, db.getsku_fromproductusing_eancode(result.getContents()));
                    sku_name.setText(db.getsku_fromproductusing_eancode(result.getContents()).get(0).getProductName());
                    sku_name.setVisibility(View.VISIBLE);
                    stock_text_value.setText("1");
                    minteger = 1;
                } else {
                    product_name = "";
                    openScanner();
                    product_scan_code = "";
                    qrcode_text.setText("");
                    minteger = 0;
                    Toast.makeText(context, "Unable to scan currect data. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }


    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<InvoiceGetterSetter> data;


        public ValueAdapter(Context context, List<InvoiceGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.row_adapter_proforma, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder holder, final int position) {
            final InvoiceGetterSetter current = data.get(position);

            holder.text_item.setText(current.getProduct());
            holder.text_item.setId(position);

            holder.text_qty.setText(current.getQuantity());
            holder.text_qty.setId(position);

            holder.txt_amount.setText(current.getProduct_rate());
            holder.txt_amount.setId(position);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView text_item, text_qty, txt_amount;

            public MyViewHolder(View itemView) {
                super(itemView);
                text_item = (TextView) itemView.findViewById(R.id.text_item);
                text_qty = (TextView) itemView.findViewById(R.id.text_qty);
                txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);

            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(CommonString.ONBACK_ALERT_MESSAGE).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        ProformaInvoiceActivity.this.finish();
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

    public void getotp_from_mobile_no(String jsondata) {
        try {
            loading = ProgressDialog.show(context, "Processing", "Please wait...", false, false);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).connectTimeout(20, TimeUnit.SECONDS).build();
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsondata.toString());
            Retrofit adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
            PostApi api = adapter.create(PostApi.class);
            retrofit2.Call<ResponseBody> call = api.getOTPMethod(jsonData);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            if (!data.contains("0")) {
                                JSONObject jObject = new JSONObject(data);
                                String otp_code = jObject.getString("SendOTPResult");
                                startActivity(new Intent(context, InvoiceReportProformaActivity.class).putExtra(CommonString.TAG_OBJECT, selected_list).putExtra(CommonString.TAG_OTP, otp_code));
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                ProformaInvoiceActivity.this.finish();
                                loading.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            loading.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    loading.dismiss();
                    AlertandMessages.showAlertlogin((Activity) context, t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loading.dismiss();
        }
    }

    private void show_dialog_forsku_categorywise(final Context context) {
        minteger = 0;
        multiPurposeDialog = new MultiPurposeDialog(context);
        multiPurposeDialog.setContentView(R.layout.custom_sku_list_with_category);
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(multiPurposeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        multiPurposeDialog.getWindow().setAttributes(lp);
        multiPurposeDialog.setCancelable(false);

        final LinearLayout rl_quantity = (LinearLayout) multiPurposeDialog.findViewById(R.id.rl_quantity);
        final LinearLayout rl_brand = (LinearLayout) multiPurposeDialog.findViewById(R.id.rl_brand);
        final LinearLayout rl_sku = (LinearLayout) multiPurposeDialog.findViewById(R.id.rl_sku);

        final Spinner category_spin = (Spinner) multiPurposeDialog.findViewById(R.id.category_spin);
        final Spinner brand_spin = (Spinner) multiPurposeDialog.findViewById(R.id.brand_spin);
        final Spinner sku_spin = (Spinner) multiPurposeDialog.findViewById(R.id.sku_spin);

        final Button stock_img_minus = (Button) multiPurposeDialog.findViewById(R.id.stock_img_minus);
        Button stock_img_plus = (Button) multiPurposeDialog.findViewById(R.id.stock_img_plus);
        final TextView stock_text_value = (TextView) multiPurposeDialog.findViewById(R.id.stock_text_value);

        ImageView cancet_btn = (ImageView) multiPurposeDialog.findViewById(R.id.cancet_btn);
        final ImageView btn_add = (ImageView) multiPurposeDialog.findViewById(R.id.btn_add);

        cancet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiPurposeDialog.dismiss();
            }
        });

        category_reason_adapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item);
        category_reason_adapter.add("-Select Sub Axe Name-");

        brand_reason_adapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item);
        brand_reason_adapter.add("-Select Brand-");

        sku_reason_adapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item);
        sku_reason_adapter.add("-Select Product-");

        for (int i = 0; i < db.getcategory_fromproduct().size(); i++) {
            category_reason_adapter.add(db.getcategory_fromproduct().get(i).getSubAxeName());
        }
        category_spin.setAdapter(category_reason_adapter);
        category_reason_adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        category_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    subaxename = db.getcategory_fromproduct().get(position - 1).getSubAxeName();
                    BrandMasterList = db.getcategory_wise_brand_fromproduct(subaxename);
                    for (int i = 0; i < BrandMasterList.size(); i++) {
                        brand_reason_adapter.add(BrandMasterList.get(i).getBrandName());
                    }
                    rl_brand.setVisibility(View.VISIBLE);
                } else {
                    BrandMasterList.clear();
                    brand_spin.setSelection(0);
                    rl_quantity.setVisibility(View.GONE);
                    rl_brand.setVisibility(View.GONE);
                    rl_sku.setVisibility(View.GONE);
                    stock_text_value.setText("0");
                    BrandMasterList.clear();
                    subaxename = "";
                    minteger = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brand_spin.setAdapter(brand_reason_adapter);
        brand_reason_adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        brand_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    brand_Id = BrandMasterList.get(position - 1).getBrandName();
                    brand_Id = BrandMasterList.get(position - 1).getBrandId().toString();
                    ProductMasterList = db.getbrand_wise_sku_fromproduct(brand_Id);
                    if (ProductMasterList.size() > 0) {
                        for (int i = 0; i < ProductMasterList.size(); i++) {
                            sku_reason_adapter.add(ProductMasterList.get(i).getProductName() + " - (MRP-" + ProductMasterList.get(i).getMrp().toString() + ")");
                        }
                        rl_sku.setVisibility(View.VISIBLE);
                    }
                } else {
                    ProductMasterList.clear();
                    sku_spin.setSelection(0);
                    rl_quantity.setVisibility(View.GONE);
                    rl_sku.setVisibility(View.GONE);
                    stock_text_value.setText("0");
                    brand_name = "";
                    brand_Id = "0";
                    minteger = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sku_spin.setAdapter(sku_reason_adapter);
        sku_reason_adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        sku_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    rl_quantity.setVisibility(View.VISIBLE);
                    product_name = ProductMasterList.get(position - 1).getProductName();
                    product_Id = ProductMasterList.get(position - 1).getProductId().toString();
                    product_mrp = ProductMasterList.get(position - 1).getMrp().toString();
                    product_scan_code = ProductMasterList.get(position - 1).getEanCode();
                    stock_text_value.setText("1");
                    minteger = 1;
                } else {
                    rl_quantity.setVisibility(View.GONE);
                    stock_text_value.setText("0");
                    product_scan_code = "";
                    product_mrp = "0";
                    product_name = "";
                    product_Id = "0";
                    minteger = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        stock_img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger = minteger + 1;
                stock_text_value.setText("" + minteger);
            }
        });

        stock_img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minteger == 0) {
                    Snackbar.make(stock_img_minus, "Product quantity should not be less than Zero", Snackbar.LENGTH_SHORT).show();
                } else {
                    minteger = minteger - 1;
                    stock_text_value.setText("" + minteger);
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);
                if (category_spin.getSelectedItemId() == 0) {
                    Snackbar.make(btn_add, "Please Select Sub Axe Name.", Snackbar.LENGTH_SHORT).show();
                } else if (brand_spin.getSelectedItemId() == 0) {
                    Snackbar.make(btn_add, "Please Select Brand.", Snackbar.LENGTH_SHORT).show();
                } else if (sku_spin.getSelectedItemId() == 0) {
                    Snackbar.make(btn_add, "Please Select Product.", Snackbar.LENGTH_SHORT).show();
                } else if (stock_text_value.getText().toString().equals("0")) {
                    Snackbar.make(btn_add, "Please Enter Product Quantity.", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (checkduplicate_entry()) {
                        object = new InvoiceGetterSetter();
                        object.setProduct_rate(product_mrp);
                        object.setProduct(product_name);
                        object.setProduct_Id(product_Id);
                        object.setScan_ean_code_or_enterd_ean_code(product_scan_code);
                        object.setCustomer_name(edt_customer.getText().toString());
                        object.setMobile_no(edt_mobile_no.getText().toString());
                        object.setQuantity(stock_text_value.getText().toString());
                        object.setCustomer_gender(radioSexButton.getText().toString());
                        selected_list.add(object);

                        minteger = 0;
                        rl_brand.setVisibility(View.GONE);
                        rl_sku.setVisibility(View.GONE);
                        rl_quantity.setVisibility(View.GONE);
                        category_spin.setSelection(0);
                        brand_spin.setSelection(0);
                        sku_spin.setSelection(0);
                        stock_text_value.setText("0");

                        product_scan_code = "";
                        subaxename = "";
                        brand_name = "";
                        brand_Id = "0";
                        product_name = "";
                        product_Id = "0";
                        ProductMasterList.clear();
                        BrandMasterList.clear();
                        drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, selected_list));
                        drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
                        multiPurposeDialog.dismiss();
                    }
                }
            }
        });

        multiPurposeDialog.show();
    }


    private void show_dialog_forsku(final Context context, ArrayList<ProductMaster> skuList_with_eancode) {
        multiPurposeDialog = new MultiPurposeDialog(context);
        multiPurposeDialog.setContentView(R.layout.custom_sku_adapter);
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(multiPurposeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        multiPurposeDialog.getWindow().setAttributes(lp);
        multiPurposeDialog.setCancelable(false);
        TextView sale_trcking_name = (TextView) multiPurposeDialog.findViewById(R.id.sale_trcking_name);
        sale_trcking_name.setText(skuList_with_eancode.get(0).getProductName());
        RecyclerView recycl_sku = (RecyclerView) multiPurposeDialog.findViewById(R.id.recycl_sku);
        recycl_sku.setAdapter(new AdapterforSku(context, skuList_with_eancode, multiPurposeDialog, "0"));
        recycl_sku.setLayoutManager(new LinearLayoutManager(context));
        ImageView dismiss_dialog = (ImageView) multiPurposeDialog.findViewById(R.id.dismiss_dialog);
        dismiss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiPurposeDialog.dismiss();
            }
        });

        multiPurposeDialog.show();
    }

    public class AdapterforSku extends RecyclerView.Adapter<AdapterforSku.MyViewHolder> {
        private LayoutInflater inflator;
        List<ProductMaster> data;
        String show_dialog;
        MultiPurposeDialog multiPurposeDialog;

        public AdapterforSku(Context context, List<ProductMaster> data, MultiPurposeDialog multiPurposeDialog, String show_dialog) {
            inflator = LayoutInflater.from(context);
            this.data = data;
            this.show_dialog = show_dialog;
            this.multiPurposeDialog = multiPurposeDialog;
        }

        @Override
        public AdapterforSku.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.adapter_custom, parent, false);
            return new AdapterforSku.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final ProductMaster current = data.get(position);
            if (show_dialog.equals("1")) {
                holder.sale_card.setVisibility(View.VISIBLE);
                holder.sale_card.setId(position);
                holder.consumerhistory_card.setVisibility(View.VISIBLE);
                holder.consumerhistory_card.setId(position);
                holder.sale_trcking_name.setText("Date : " + current.getVisit_date());
                holder.sale_trcking_name.setId(position);

                ArrayList<ProductMaster> consumerList = db.getConsumerSaleHistry(current.getMobile_no(), current.getVisit_date());
                if (consumerList.size() > 0) {
                    for (int k = 0; k < consumerList.size(); k++) {
                        holder.consume_product.setText(consumerList.get(k).getProductName());
                        holder.consume_product.setId(position);

                        holder.consume_product_qty_with_date.setText("Quantity : " + consumerList.get(k).getConsumer_qty());
                        holder.consume_product_qty_with_date.setId(position);
                    }
                }


            } else {
                holder.sale_card.setVisibility(View.VISIBLE);
                holder.sale_card.setId(position);
                holder.consumerhistory_card.setVisibility(View.GONE);
                holder.consumerhistory_card.setId(position);

                holder.sale_trcking_name.setText("(MRP - " + current.getMrp().toString() + ")");
                holder.sale_trcking_name.setId(position);

                holder.sale_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qrcode_text.setText(current.getEanCode());
                        product_name = current.getProductName();
                        product_Id = current.getProductId().toString();
                        product_scan_code = current.getEanCode();
                        product_mrp = current.getMrp().toString();
                        multiPurposeDialog.dismiss();
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView sale_trcking_name, consume_product, consume_product_qty_with_date;
            CardView sale_card, consumerhistory_card;
            RecyclerView consumer_recycle;
            ImageView btn_ok;

            public MyViewHolder(View itemView) {
                super(itemView);
                sale_card = (CardView) itemView.findViewById(R.id.sale_card);
                sale_trcking_name = (TextView) itemView.findViewById(R.id.sale_trcking_name);
                ////for consumer product
                consumerhistory_card = (CardView) itemView.findViewById(R.id.consumerhistory_card);
                consume_product_qty_with_date = (TextView) itemView.findViewById(R.id.consume_product_qty_with_date);
                consume_product = (TextView) itemView.findViewById(R.id.consume_product);
                btn_ok = (ImageView) itemView.findViewById(R.id.btn_ok);
                consumer_recycle=(RecyclerView)itemView.findViewById(R.id.consumer_recycle);
            }

        }
    }

    private boolean checkduplicate_entry() {
        boolean status = true;
        if (selected_list.size() > 0) {
            for (int k = 0; k < selected_list.size(); k++) {
                if (selected_list.get(k).getProduct_Id().equals(product_Id) && selected_list.get(k).getProduct_rate().equals(product_mrp)) {
                    Toast.makeText(context, "This product already added.", Toast.LENGTH_SHORT).show();
                    status = false;
                    break;
                }
            }
        }
        return status;
    }

    private boolean checkindex() {
        boolean status = false;
        String str = edt_mobile_no.getText().toString();
        String strArray[] = str.split(" ");
        //print elements of String array
        for (int i = 0; i < strArray.length; i++) {
            if (strArray[i].contains("1") || strArray[i].contains("2") || strArray[i].contains("3") || strArray[i].contains("4") || strArray[i].contains("5") || strArray[i].contains("6") || strArray[i].contains("7") || strArray[i].contains("9") || strArray[i].contains("8")) {
                status = true;
                break;
            }
        }
        return status;
    }


    private void show_consumer_previous_histry(final Context context, String consumer_mobile_no) {
        multiPurposeDialog = new MultiPurposeDialog(context);
        multiPurposeDialog.setContentView(R.layout.custom_sku_adapter);
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(multiPurposeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        multiPurposeDialog.getWindow().setAttributes(lp);
        multiPurposeDialog.setCancelable(false);
        TextView txt_bar_code = multiPurposeDialog.findViewById(R.id.txt_bar_code);
        txt_bar_code.setText("Consumer History");
        TextView sale_trcking_name = (TextView) multiPurposeDialog.findViewById(R.id.sale_trcking_name);
        sale_trcking_name.setVisibility(View.GONE);

        RecyclerView recycl_sku = (RecyclerView) multiPurposeDialog.findViewById(R.id.recycl_sku);

        //recycl_sku.setAdapter(new AdapterforSku(context, db.getConsumerSaleHistry(consumer_mobile_no), multiPurposeDialog, "1"));
        recycl_sku.setAdapter(new AdapterforSku(context, db.getConsumer_date(consumer_mobile_no), multiPurposeDialog, "1"));
        recycl_sku.setLayoutManager(new LinearLayoutManager(context));
        ImageView dismiss_dialog = (ImageView) multiPurposeDialog.findViewById(R.id.dismiss_dialog);
        dismiss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiPurposeDialog.dismiss();
            }
        });

        multiPurposeDialog.show();
    }


}
