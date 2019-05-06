package ba.cpm.com.lorealba.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonFunctions;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.SampleGetterSetter;

import static ba.cpm.com.lorealba.constant.CommonFunctions.getCurrentTime;

public class SampleTesterStockActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    ValueAdapter adapter;
    FloatingActionButton fab;
    TextView sample_photos_text;
    RecyclerView drawer_layout_recycle_store;
    ImageView sample_img_one, sample_img_two, sample_img_three,
            sample_img_four, img_daily_stock, img_tester_stock, img_damaged_stock, img_invert_stock, btn_add_stock;
    int minteger = 0;
    String type_of, _pathforcheck, _path, str_img_one = "", str_img_two = "", str_img_three = "", str_img_four = "";

    ArrayList<SampleGetterSetter> sampleList = new ArrayList<>();
    SampleGetterSetter object = null;
    ImageView img_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_tester_stock);
        context = this;
        uivalidate();
    }

    private void uivalidate() {
        drawer_layout_recycle_store = (RecyclerView) findViewById(R.id.drawer_layout_recycle_store);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btn_add_stock = (ImageView) findViewById(R.id.btn_add_stock);
        sample_photos_text = (TextView) findViewById(R.id.sample_photos_text);

        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);
        img_home = (ImageView) findViewById(R.id.img_home);

        sample_img_one = (ImageView) findViewById(R.id.sample_img_one);
        sample_img_two = (ImageView) findViewById(R.id.sample_img_two);
        sample_img_three = (ImageView) findViewById(R.id.sample_img_three);


        sample_img_four = (ImageView) findViewById(R.id.sample_img_four);

        img_daily_stock = (ImageView) findViewById(R.id.img_daily_stock);
        img_tester_stock = (ImageView) findViewById(R.id.img_tester_stock);
        img_damaged_stock = (ImageView) findViewById(R.id.img_damaged_stock);
        img_invert_stock = (ImageView) findViewById(R.id.img_invert_stock);
        ImageView header_icon = (ImageView) findViewById(R.id.header_icon);

        TextView title = (TextView) findViewById(R.id.textv_sample);

        type_of = getIntent().getStringExtra(CommonString.KEY_STOCK_TYPE);

        if (type_of.equals("1")) {
            title.setText("Sample Stock");
            sample_photos_text.setText("Take Photos Of Sample Stock");
            header_icon.setImageDrawable(getResources().getDrawable(R.drawable.sample36));
        } else {
            title.setText("Tester Stock");
            sample_photos_text.setText("Take Photos Of Tester Stock");
            header_icon.setImageDrawable(getResources().getDrawable(R.drawable.tester36));
        }

        fab.setOnClickListener(this);
        btn_add_stock.setOnClickListener(this);
        sample_img_one.setOnClickListener(this);
        sample_img_two.setOnClickListener(this);
        sample_img_three.setOnClickListener(this);
        sample_img_four.setOnClickListener(this);

        img_home.setOnClickListener(this);


        img_daily_stock.setOnClickListener(this);

        img_tester_stock.setOnClickListener(this);
        img_damaged_stock.setOnClickListener(this);
        img_invert_stock.setOnClickListener(this);

        validate_adapter();
    }

    void validate_adapter() {
        object = new SampleGetterSetter();
        if (type_of.equals("1")) {
            object.setSample_name("Maybeline Color Sensational Creamy Matte,643 Cosmopolitan Red");
            object.setLast_entered_value("2");
            object.setCurrent_stock("0");
            sampleList.add(object);

            object.setSample_name("Maybeline Color Sensational Creamy Matte, Mesmerising Magenta");
            object.setLast_entered_value("1");
            object.setCurrent_stock("0");
            sampleList.add(object);

            object.setSample_name("Maybeline Color Sensational Creamy Matte, Vibrant Violet");
            object.setLast_entered_value("3");
            object.setCurrent_stock("0");
            sampleList.add(object);
        } else {
            object.setSample_name("Maybeline Color Sensational Creamy Matte,643 Cosmopolitan Red");
            object.setLast_entered_value("5");
            object.setCurrent_stock("0");
            sampleList.add(object);

            object.setSample_name("Maybeline Color Sensational Creamy Matte, Mesmerising Magenta");
            object.setLast_entered_value("4");
            object.setCurrent_stock("0");
            sampleList.add(object);

            object.setSample_name("Maybeline Color Sensational Creamy Matte, Vibrant Violet");
            object.setLast_entered_value("2");
            object.setCurrent_stock("0");
            sampleList.add(object);
        }

        adapter = new ValueAdapter(context, sampleList, type_of);
        drawer_layout_recycle_store.setAdapter(adapter);
        drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                        SampleTesterStockActivity.this.finish();
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
                break;
            case R.id.fab:
                builder = new AlertDialog.Builder(context).setTitle(R.string.parinaam).setMessage(R.string.alertsaveData);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        SampleTesterStockActivity.this.finish();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();

                break;

            case R.id.btn_add_stock:
                if (!str_img_one.equals("") || !str_img_two.equals("") || !str_img_three.equals("") || !str_img_four.equals("")) {
                    drawer_layout_recycle_store.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(btn_add_stock, "Please add atleast one image.", Snackbar.LENGTH_LONG).show();
                }

                break;

            case R.id.sample_img_one:
                if (type_of.equals("1")) {
                    _pathforcheck = "0_SMP_ONE_IMG_" + getCurrentTime().replace(":", "") + ".jpg";
                } else {
                    _pathforcheck = "0_TSTR_ONE_IMG_" + getCurrentTime().replace(":", "") + ".jpg";
                }
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(context, _path);

                break;

            case R.id.sample_img_two:
                if (type_of.equals("1")) {
                    _pathforcheck = "0_SMP_TWO_IMG_" + getCurrentTime().replace(":", "") + ".jpg";
                } else {
                    _pathforcheck = "0_TSTR_TWO_IMG_" + getCurrentTime().replace(":", "") + ".jpg";
                }
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(context, _path);
                break;

            case R.id.sample_img_three:
                if (type_of.equals("1")) {
                    _pathforcheck = "0_SMP_THREE_IMG_" + getCurrentTime().replace(":", "") + ".jpg";
                } else {
                    _pathforcheck = "0_TSTR_THREE_IMG_" + getCurrentTime().replace(":", "") + ".jpg";
                }
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(context, _path);
                break;

            case R.id.sample_img_four:
                if (type_of.equals("1")) {
                    _pathforcheck = "0_SMP_FOUR_IMG_" + getCurrentTime().replace(":", "") + ".jpg";
                } else {
                    _pathforcheck = "0_TSTR_FOUR_IMG_" + getCurrentTime().replace(":", "") + ".jpg";
                }
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(context, _path);

                break;


            case R.id.img_daily_stock:
                startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                SampleTesterStockActivity.this.finish();
                break;

            case R.id.img_damaged_stock:
                startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                SampleTesterStockActivity.this.finish();
                break;

            case R.id.img_invert_stock:
                startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "3"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                SampleTesterStockActivity.this.finish();
                break;

            case R.id.img_tester_stock:
                startActivity(new Intent(context, SampleTesterStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                SampleTesterStockActivity.this.finish();
                break;


        }
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<SampleGetterSetter> data;
        String report_tpe;

        public ValueAdapter(Context context, List<SampleGetterSetter> data, String report_tpe) {
            inflator = LayoutInflater.from(context);
            this.report_tpe = report_tpe;
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.report_sample, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder holder, final int position) {
            final SampleGetterSetter current = data.get(position);

            holder.sample_name.setText(current.getSample_name());
            holder.sample_name.setId(position);

            holder.stock_img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minteger = minteger + 1;
                    holder.stock_text_value.setText("" + minteger);
                }
            });

            holder.stock_img_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minteger = minteger - 1;
                    holder.stock_text_value.setText("" + minteger);

                }
            });
        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            Button stock_img_plus, stock_img_minus;
            TextView sample_name, last_entered_value, stock_text_value;

            public MyViewHolder(View itemView) {
                super(itemView);
                stock_img_plus = (Button) itemView.findViewById(R.id.stock_img_plus);
                stock_img_minus = (Button) itemView.findViewById(R.id.stock_img_minus);

                sample_name = (TextView) itemView.findViewById(R.id.sample_name);
                last_entered_value = (TextView) itemView.findViewById(R.id.last_entered_value);
                stock_text_value = (TextView) itemView.findViewById(R.id.stock_text_value);

            }
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
                    try {
                        if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                            if (_pathforcheck.contains("0_SMP_ONE_IMG_") || _pathforcheck.contains("0_TSTR_ONE_IMG_")) {
                                sample_img_one.setImageResource(R.mipmap.camera_green);
                                str_img_one = _pathforcheck;

                            } else if (_pathforcheck.contains("0_SMP_TWO_IMG_") || _pathforcheck.contains("0_TSTR_TWO_IMG_")) {
                                sample_img_two.setImageResource(R.mipmap.camera_green);
                                str_img_two = _pathforcheck;

                            } else if (_pathforcheck.contains("0_SMP_THREE_IMG_") || _pathforcheck.contains("0_TSTR_THREE_IMG_")) {
                                sample_img_three.setImageResource(R.mipmap.camera_green);
                                str_img_three = _pathforcheck;

                            } else if (_pathforcheck.contains("0_SMP_FOUR_IMG_") || _pathforcheck.contains("0_TSTR_FOUR_IMG_")) {
                                sample_img_four.setImageResource(R.mipmap.camera_green);
                                str_img_four = _pathforcheck;
                            }
                            String metadata = CommonFunctions.setMetadataAtImages("", "", "", "test");
                            CommonFunctions.addMetadataAndTimeStampToImage(context, _path, metadata, "04/02/2019");
                            _pathforcheck = "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                SampleTesterStockActivity.this.finish();
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

}
