package ba.cpm.com.lorealba.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.InvoiceGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.ReportsGetterSetter;
import ba.cpm.com.lorealba.scanner.ScanActivity;

public class ProformaInvoiceActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_customer, edt_mobile_no;
    Button btn_scan, btn_enter_code, btn_sku_list;
    RecyclerView drawer_layout_recycle_store;
    MultiPurposeDialog multiPurposeDialog;
    public ArrayList<InvoiceGetterSetter> selected_list = new ArrayList<>();
    InvoiceGetterSetter object;
    String product = "", product_scan_code = "";
    FloatingActionButton fab;
    TextView qrcode_text;
    EditText edt_scan_code, edt_proforma_sku_qty;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performa_invoice);
        context = this;
        iduserinterface();
    }

    private void iduserinterface() {
        ImageView img_home = (ImageView) findViewById(R.id.img_home);
        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);

        textv_sample.setText("Sales Tracking");
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_enter_code = (Button) findViewById(R.id.btn_enter_code);
        btn_sku_list = (Button) findViewById(R.id.btn_sku_list);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        edt_customer = (EditText) findViewById(R.id.edt_customer);
        edt_mobile_no = (EditText) findViewById(R.id.edt_mobile_no);
        drawer_layout_recycle_store = (RecyclerView) findViewById(R.id.drawer_layout_recycle_store);

        btn_scan.setOnClickListener(this);
        btn_enter_code.setOnClickListener(this);
        btn_sku_list.setOnClickListener(this);
        fab.setOnClickListener(this);
        img_home.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (edt_customer.getText().toString().isEmpty()) {
                    Snackbar.make(edt_customer, "Please enter customer name.", Snackbar.LENGTH_LONG).show();
                } else if (edt_mobile_no.getText().toString().isEmpty()) {
                    Snackbar.make(edt_customer, "Please enter mobile number.", Snackbar.LENGTH_LONG).show();
                } else {
                    show_proforma_dialog(context, true);
                }
                break;

            case R.id.btn_enter_code:
                if (edt_customer.getText().toString().isEmpty()) {
                    Snackbar.make(edt_customer, "Please enter customer name.", Snackbar.LENGTH_LONG).show();
                } else if (edt_mobile_no.getText().toString().isEmpty()) {
                    Snackbar.make(edt_customer, "Please enter mobile number.", Snackbar.LENGTH_LONG).show();
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
                            startActivity(new Intent(context, InvoiceReportProformaActivity.class).putExtra(CommonString.TAG_OBJECT, selected_list));
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            ProformaInvoiceActivity.this.finish();
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
                    Snackbar.make(fab, "Please add first product", Snackbar.LENGTH_LONG).show();
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
        }
    }


    private void show_proforma_dialog(final Context context, final boolean flag_for_rl) {
        multiPurposeDialog = new MultiPurposeDialog(context);
        multiPurposeDialog.setContentView(R.layout.custom_bar_code_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(multiPurposeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        multiPurposeDialog.getWindow().setAttributes(lp);
        multiPurposeDialog.setCancelable(true);

        LinearLayout rl_bar_code = (LinearLayout) multiPurposeDialog.findViewById(R.id.rl_bar_code);
        LinearLayout RL_scan_code = (LinearLayout) multiPurposeDialog.findViewById(R.id.RL_scan_code);

        TextView txt_bar_code = multiPurposeDialog.findViewById(R.id.txt_bar_code);
        TextView txt_enterbar_code = multiPurposeDialog.findViewById(R.id.txt_enterbar_code);

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

        edt_proforma_sku_qty = (EditText) multiPurposeDialog.findViewById(R.id.edt_proforma_sku_qty);
        ImageView img_bar_code = (ImageView) multiPurposeDialog.findViewById(R.id.img_bar_code);
        ImageView cancet_btn = (ImageView) multiPurposeDialog.findViewById(R.id.cancet_btn);
        final ImageView btn_add = (ImageView) multiPurposeDialog.findViewById(R.id.btn_add);

        cancet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiPurposeDialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_for_rl && qrcode_text.getText().toString().isEmpty()) {
                    Snackbar.make(btn_add, "Please Scan Bar Code.", Snackbar.LENGTH_LONG).show();
                } else if (!flag_for_rl && edt_scan_code.getText().toString().isEmpty()) {
                    Snackbar.make(btn_add, "Please Enter Bar Code.", Snackbar.LENGTH_LONG).show();
                } else if (edt_proforma_sku_qty.getText().toString().isEmpty()) {
                    Snackbar.make(btn_add, "Please Enter Product Quantity.", Snackbar.LENGTH_LONG).show();
                } else {
                    object = new InvoiceGetterSetter();
                    if (!flag_for_rl) {
                        boolean flag = true;
                        product_scan_code = edt_scan_code.getText().toString();
                        if (product_scan_code.equals("8901526510320")) {
                            product = "White superfresh with pure perlite";
                        } else if (product_scan_code.equals("6946537005757")) {
                            product = "The colo ssal kajal";
                        } else {
                            flag = false;
                            edt_scan_code.setText("");
                            Snackbar.make(btn_add, "This Bar Code is incurrect.Please enter currect code.", Snackbar.LENGTH_LONG).show();
                        }
                        if (flag) {
                            if (product_scan_code.equals("8901526510320")) {
                                object.setAmount("160.00");
                            } else if (product_scan_code.equals("6946537005757")) {
                                object.setAmount("170.00");
                            }
                            object.setProduct(product);
                            object.setScan_code_or_enterd_code(edt_scan_code.getText().toString());

                            object.setCustomer_name(edt_customer.getText().toString());
                            object.setMobile_no(edt_mobile_no.getText().toString());
                            object.setQuantity(edt_proforma_sku_qty.getText().toString());
                            selected_list.add(object);
                            qrcode_text.setText("");
                            edt_proforma_sku_qty.setText("");
                            edt_scan_code.setText("");
                            product_scan_code = "";
                            product = "";
                            drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, selected_list));
                            drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
                            multiPurposeDialog.dismiss();
                        }

                    } else {
                        if (product_scan_code.equals("8901526510320")) {
                            object.setAmount("160.00");
                        } else if (product_scan_code.equals("6946537005757")) {
                            object.setAmount("170.00");
                        }
                        object.setProduct(product);
                        object.setScan_code_or_enterd_code(product_scan_code);
                        object.setCustomer_name(edt_customer.getText().toString());
                        object.setMobile_no(edt_mobile_no.getText().toString());
                        object.setQuantity(edt_proforma_sku_qty.getText().toString());
                        selected_list.add(object);
                        qrcode_text.setText("");
                        edt_proforma_sku_qty.setText("");
                        edt_scan_code.setText("");
                        product_scan_code = "";
                        product = "";
                        drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, selected_list));
                        drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
                        multiPurposeDialog.dismiss();
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
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (result.getContents().equals("8901526510320")) {
                    qrcode_text.setText(result.getContents());
                    product = "White superfresh with pure perlite";
                    product_scan_code = result.getContents();

                } else if (result.getContents().equals("6946537005757")) {
                    qrcode_text.setText(result.getContents());
                    product = "The colo ssal kajal";
                    product_scan_code = result.getContents();
                } else {
                    product = "";
                    openScanner();
                    product_scan_code = "";
                    qrcode_text.setText("");
                    Toast.makeText(context, "Unable to scan currect data. Please try again", Toast.LENGTH_LONG).show();
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

            holder.txt_amount.setText(current.getAmount());
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
}
