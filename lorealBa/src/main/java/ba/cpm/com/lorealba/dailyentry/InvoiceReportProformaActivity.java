package ba.cpm.com.lorealba.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.InvoiceGetterSetter;
import ba.cpm.com.lorealba.printer.PrinterActivity;

public class InvoiceReportProformaActivity extends AppCompatActivity implements View.OnClickListener {
    String otp_response = "", complete_string_datafor_print = "", store_cd, visit_date, username, store_name, store_address;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;

    ArrayList<InvoiceGetterSetter> invoice_List = new ArrayList<>();
    TextView text_cus_name, text_mobile_number, text_inr_value, invoice_storename, invoice_gst_no;
    RecyclerView drawer_layout_recycle_store;
    Double total_INR_value = 0.0, total = 0.0;
    boolean mobileno_flag = false, pos_sale_flag = false;
    Switch switch_pos_sale;
    FloatingActionButton fab;
    ImageView img_printer;
    Lorealba_Database db;
    Context context;
    EditText edt_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_report_proforma);
        context = this;
        db = new Lorealba_Database(context);
        db.open();
        validateui_design();
    }

    void validateui_design() {
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
        otp_response = getIntent().getStringExtra(CommonString.TAG_OTP);

        drawer_layout_recycle_store = findViewById(R.id.drawer_layout_recycle_store);
        text_cus_name = (TextView) findViewById(R.id.text_cus_name);
        text_mobile_number = (TextView) findViewById(R.id.text_mobile_number);
        text_inr_value = (TextView) findViewById(R.id.text_inr_value);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        img_printer = (ImageView) findViewById(R.id.img_printer);

        ImageView img_home = (ImageView) findViewById(R.id.img_home);
        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);
        edt_otp = (EditText) findViewById(R.id.edt_otp);

        invoice_storename = (TextView) findViewById(R.id.invoice_storename);
        invoice_gst_no = (TextView) findViewById(R.id.invoice_gst_no);

        switch_pos_sale = (Switch) findViewById(R.id.switch_pos_sale);
        switch_pos_sale.setOnClickListener(this);
        invoice_storename.setText(store_name + "\n" + store_address);

        invoice_List = (ArrayList<InvoiceGetterSetter>) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
        if (invoice_List.size() > 0) {
            text_cus_name.setText("Customer Name : " + invoice_List.get(0).getCustomer_name());
            text_mobile_number.setText("Mobile Number : " + invoice_List.get(0).getMobile_no());

            if (checkindex()) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else {
                edt_otp.setVisibility(View.GONE);
                mobileno_flag = false;
            }

            Double cCom = 0.0, totalam = 0.0;
            for (int k = 0; k < invoice_List.size(); k++) {
                if (invoice_List.get(k).getScan_ean_code_or_enterd_ean_code().equals("6946537005757")) {
                    int quantity = Integer.parseInt(invoice_List.get(k).getQuantity());
                    double amount = Double.parseDouble(invoice_List.get(k).getProduct_rate());
                    amount = amount * quantity;

                    invoice_List.get(k).setTotal_amount("" + amount);

                    cCom = ((Double.parseDouble(invoice_List.get(k).getTotal_amount())) * .2);

                } else {
                    int quantity = Integer.parseInt(invoice_List.get(k).getQuantity());
                    double amount = Double.parseDouble(invoice_List.get(k).getProduct_rate());
                    amount = amount * quantity;
                    invoice_List.get(k).setTotal_amount("" + amount);
                }

                invoice_List.get(k).setStore_name(store_name);
                invoice_List.get(k).setStore_address(store_address);

                String product = invoice_List.get(k).getProduct() + "\n" + invoice_List.get(k).getScan_ean_code_or_enterd_ean_code() + "\nQuantity : " + invoice_List.get(k).getQuantity() + " Amount : " + invoice_List.get(k).getTotal_amount() + "\n\n";
                complete_string_datafor_print = complete_string_datafor_print + product;

                total_INR_value = total_INR_value + Double.valueOf(invoice_List.get(k).getTotal_amount());

            }


            total = total_INR_value - cCom;
            drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, invoice_List));
            drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
            text_inr_value.setText("" + total);
        }


        img_printer.setOnClickListener(this);
        img_home.setOnClickListener(this);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (checkotp_validation()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(R.string.parinaam).setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            db.open();
                            db.insertsaleTrackingdata(username, visit_date, store_cd, invoice_List, total.toString(), pos_sale_flag);
                            InvoiceReportProformaActivity.this.finish();
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
                break;
            case R.id.img_home:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                InvoiceReportProformaActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            case R.id.switch_pos_sale:
                if (switch_pos_sale.isChecked()) {
                    pos_sale_flag = true;
                } else {
                    pos_sale_flag = false;
                }
                break;

            case R.id.img_printer:

// Bold format center:
//                String senddata_for_print = "A One Beauty Store\n147,Kalkaji,new Delhi -26\n\nDate :12/10/2019\n\n" +
//                        "Customer Name : Amit\nMobile Number :123322444\n\n" + complete_string_datafor_print + "\nTotal Amount :" + total + "";

                String senddata_for_print = complete_string_datafor_print;
                startActivity(new Intent(context, PrinterActivity.class).putExtra(CommonString.TAG_OBJECT, senddata_for_print).putExtra(CommonString.Total_Amount, total.toString()));
                try {
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.row_adapter_proforma_invoice, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder holder, final int position) {
            final InvoiceGetterSetter current = data.get(position);

            if (current.getScan_ean_code_or_enterd_ean_code().equals("6946537005757")) {

                Double cCom = ((Double.parseDouble(current.getTotal_amount())) * .2);

                holder.product.setText(current.getProduct() + "\n" + " 20% Discount : (" + "" + cCom + ")");
                holder.product.setId(position);

                holder.rate.setText(current.getProduct_rate());
                holder.rate.setId(position);

                Double totalam = (Double.parseDouble(current.getTotal_amount()) - cCom);

                holder.total_amount.setText("" + totalam);
                holder.total_amount.setId(position);

                holder.qty.setText(current.getQuantity());
                holder.qty.setId(position);


            } else {
                holder.rate.setText(current.getProduct_rate());
                holder.rate.setId(position);

                holder.total_amount.setText(current.getTotal_amount());
                holder.total_amount.setId(position);

                holder.qty.setText(current.getQuantity());
                holder.qty.setId(position);

                holder.product.setText(current.getProduct());
                holder.product.setId(position);
            }


        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView product, qty, rate, total_amount;

            public MyViewHolder(View itemView) {
                super(itemView);

                product = (TextView) itemView.findViewById(R.id.product);
                qty = (TextView) itemView.findViewById(R.id.qty);
                rate = (TextView) itemView.findViewById(R.id.rate);
                total_amount = (TextView) itemView.findViewById(R.id.total_amount);

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
                        InvoiceReportProformaActivity.this.finish();
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

    private boolean checkotp_validation() {
        boolean status = true;
        if (mobileno_flag && edt_otp.getText().toString().isEmpty()) {
            Snackbar.make(fab, "Please enter OTP", Snackbar.LENGTH_SHORT).show();
            status = false;
        } else {
            if (!edt_otp.getText().toString().equals("")) {
                int otp_count = Integer.parseInt(edt_otp.getText().toString());
                int length = (int) (Math.log10(otp_count) + 1);
                if (edt_otp.getText().toString().equals(otp_response)) {
                    status = true;
                } else if (length == 1 && edt_otp.getText().toString().equals("0")) {
                    status = true;
                } else {
                    status = false;
                    Snackbar.make(fab, "Incurrect OTP", Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        return status;
    }

    private boolean checkindex() {
        boolean status = false;
        String str = invoice_List.get(0).getMobile_no();
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

}
