package ba.cpm.com.lorealba.dailyentry;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.InvoiceGetterSetter;
import ba.cpm.com.lorealba.printer.PrinterActivity;

public class InvoiceReportProformaActivity extends AppCompatActivity implements View.OnClickListener {
    String otp_response = "", complete_string_datafor_print = "";
    ArrayList<InvoiceGetterSetter> list = new ArrayList<>();
    TextView text_cus_name, text_mobile_number, text_inr_value;
    RecyclerView drawer_layout_recycle_store;
    FloatingActionButton fab;
    Double total_INR_value = 0.0, total = 0.0;
    boolean mobileno_flag = false;
    ImageView img_printer;
    Context context;
    EditText edt_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_report_proforma);
        context = this;
        validateui_design();
    }

    void validateui_design() {

        drawer_layout_recycle_store = findViewById(R.id.drawer_layout_recycle_store);
        text_cus_name = (TextView) findViewById(R.id.text_cus_name);
        text_mobile_number = (TextView) findViewById(R.id.text_mobile_number);
        text_inr_value = (TextView) findViewById(R.id.text_inr_value);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        img_printer = (ImageView) findViewById(R.id.img_printer);

        ImageView img_home = (ImageView) findViewById(R.id.img_home);
        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);
        edt_otp = (EditText) findViewById(R.id.edt_otp);

        list = (ArrayList<InvoiceGetterSetter>) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
        if (list.size() > 0) {
            text_cus_name.setText("Customer Name : " + list.get(0).getCustomer_name());
            text_mobile_number.setText("Mobile Number : " + list.get(0).getMobile_no());

            if (list.get(0).getMobile_no().contains("1")) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else if (list.get(0).getMobile_no().contains("2")) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else if (list.get(0).getMobile_no().contains("3")) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else if (list.get(0).getMobile_no().contains("4")) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else if (list.get(0).getMobile_no().contains("5")) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else if (list.get(0).getMobile_no().contains("6")) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else if (list.get(0).getMobile_no().contains("7")) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else if (list.get(0).getMobile_no().contains("8")) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else if (list.get(0).getMobile_no().contains("9")) {
                edt_otp.setVisibility(View.VISIBLE);
                mobileno_flag = true;
            } else {
                edt_otp.setVisibility(View.GONE);
                mobileno_flag = false;
            }

            Double cCom = 0.0, totalam = 0.0;
            for (int k = 0; k < list.size(); k++) {
                if (list.get(k).getScan_code_or_enterd_code().equals("6946537005757")) {
                    int quantity = Integer.parseInt(list.get(k).getQuantity());
                    double amount = Double.parseDouble(list.get(k).getAmount());
                    amount = amount * quantity;

                    list.get(k).setTotal_amount("" + amount);

                    cCom = ((Double.parseDouble(list.get(k).getTotal_amount())) * .2);

                } else {
                    int quantity = Integer.parseInt(list.get(k).getQuantity());
                    double amount = Double.parseDouble(list.get(k).getAmount());
                    amount = amount * quantity;
                    list.get(k).setTotal_amount("" + amount);
                }

                String product = list.get(k).getProduct() + "\nQuantity : " + list.get(k).getQuantity() + " Amount : " + list.get(k).getTotal_amount()+"\n\n";
                complete_string_datafor_print = complete_string_datafor_print + product;

                total_INR_value = total_INR_value + Double.valueOf(list.get(k).getTotal_amount());

            }


            total = total_INR_value - cCom;
            drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, list));
            drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
            text_inr_value.setText("" + total);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkotp_validation()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(R.string.parinaam).setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
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
            }
        });

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        img_printer.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_printer:
                String senddata_for_print = "A One Beauty Store\n147,Kalkaji,new Delhi -26\n\nDate :12/10/2019\n\n" +
                        "Customer Name : Amit\nMobile Number :123322444\n\n" + complete_string_datafor_print + "\nTotal Amount :" + total + "";
                startActivity(new Intent(context, PrinterActivity.class).putExtra(CommonString.TAG_OBJECT, senddata_for_print));
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

            if (current.getScan_code_or_enterd_code().equals("6946537005757")) {

                Double cCom = ((Double.parseDouble(current.getTotal_amount())) * .2);

                holder.product.setText(current.getProduct() + "\n" + " 20% Discount : (" + "" + cCom + ")");
                holder.product.setId(position);

                holder.rate.setText(current.getAmount());
                holder.rate.setId(position);

                Double totalam = (Double.parseDouble(current.getTotal_amount()) - cCom);

                holder.amount.setText("" + totalam);
                holder.amount.setId(position);


                holder.qty.setText(current.getQuantity());
                holder.qty.setId(position);


            } else {
                holder.rate.setText(current.getAmount());
                holder.rate.setId(position);

                holder.amount.setText(current.getTotal_amount());
                holder.amount.setId(position);


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
            TextView product, qty, rate, amount;

            public MyViewHolder(View itemView) {
                super(itemView);

                product = (TextView) itemView.findViewById(R.id.product);
                qty = (TextView) itemView.findViewById(R.id.qty);
                rate = (TextView) itemView.findViewById(R.id.rate);
                amount = (TextView) itemView.findViewById(R.id.amount);

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
                if (length == 4) {
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

        }
        return status;
    }
}
