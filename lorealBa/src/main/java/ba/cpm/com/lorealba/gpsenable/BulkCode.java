/*
package ba.cpm.com.lorealba.gpsenable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonFunctions;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.dailyentry.PromotionActivity;
import ba.cpm.com.lorealba.dailyentry.SaleTrackingActivity;
import ba.cpm.com.lorealba.gsonGetterSetter.PromotionGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.PromotionMaster;

import static ba.cpm.com.lorealba.constant.CommonFunctions.getCurrentTime;

public class BulkCode {

    package ba.cpm.com.lorealba.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonFunctions;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.PromotionGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.PromotionMaster;

import static ba.cpm.com.lorealba.constant.CommonFunctions.getCurrentTime;

    public class PromotionActivity extends AppCompatActivity implements View.OnClickListener {
        String store_cd, visit_date, username;
        private SharedPreferences preferences;
        private SharedPreferences.Editor editor = null;
        String str_posm_img = "", _pathforcheck, _path, promotion_QR_code_content = "", type_of_promotion = "";
        ArrayList<PromotionMaster> promotionList = new ArrayList<>();
        ImageView img_daily_sales, bottom_customerwise_sale, img_promotion, img_home, header_icon;
        PromotionGetterSetter object = null;
        RecyclerView drawer_layout_recycle_store;
        ba.cpm.com.lorealba.dailyentry.PromotionActivity.ValueAdapter adapter;
        FloatingActionButton fab;
        int _currectposition = -1;
        Lorealba_Database db;
        Context context;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_promotion_tracking);
            context = this;
            db = new Lorealba_Database(context);
            db.open();
            uivalidate();
            validateAdapterData();
        }

        void validateAdapterData() {
            promotionList = db.getpromotion_master_data();
            if (promotionList.size() > 0) {
                adapter = new ba.cpm.com.lorealba.dailyentry.PromotionActivity.ValueAdapter(context, promotionList, type_of_promotion);
                drawer_layout_recycle_store.setAdapter(adapter);
                drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
            }

       */
/* object = new PromotionGetterSetter();
        if (type_of_promotion.equals("1")) {
            object.setPromotion_name("Dangler - Color Rich Matte 20%");
            object.setPosm_currect_ans("");
            object.setPosm_exists_state("");
            object.setPosm_img("");
            object.setPosm_qr_data("");
            promotionList.add(object);

            object = new PromotionGetterSetter();
            object.setPromotion_name("L Card - Color Rich Matte 20%");
            object.setPosm_currect_ans("");
            object.setPosm_exists_state("");
            object.setPosm_img("");
            object.setPosm_qr_data("");
            promotionList.add(object);
        } else {
            object.setPromotion_name("Color Rich Matte Lipsticks ranges 20% Off");
            object.setPosm_currect_ans("");
            object.setPosm_exists_state("");
            object.setPosm_img("");
            object.setPosm_qr_data("");
            promotionList.add(object);

            object = new PromotionGetterSetter();
            object.setPromotion_name("Buy Lip and Eye Gentle Makeup Remover and Super Liner Black Lacquer at only Rs 599");
            object.setPosm_currect_ans("");
            object.setPosm_exists_state("");
            object.setPosm_img("");
            object.setPosm_qr_data("");
            promotionList.add(object);
        }*//*



        }

        private void uivalidate() {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = preferences.edit();
            store_cd = preferences.getString(CommonString.KEY_STORE_ID, "");
            store_cd = "2";
            username = preferences.getString(CommonString.KEY_USERNAME, "");
            visit_date = preferences.getString(CommonString.KEY_DATE, "");

            drawer_layout_recycle_store = (RecyclerView) findViewById(R.id.drawer_layout_recycle_store);
            fab = (FloatingActionButton) findViewById(R.id.fab);

            img_daily_sales = (ImageView) findViewById(R.id.img_daily_sales);
            bottom_customerwise_sale = (ImageView) findViewById(R.id.bottom_customerwise_sale);
            img_promotion = (ImageView) findViewById(R.id.img_promotion);
            img_home = (ImageView) findViewById(R.id.img_home);
            header_icon = (ImageView) findViewById(R.id.header_icon);

            type_of_promotion = getIntent().getStringExtra(CommonString.KEY_STOCK_TYPE);
            TextView textv_sample = findViewById(R.id.textv_sample);

            if (type_of_promotion.equals("1")) {
                header_icon.setImageResource(R.drawable.posm36);
                textv_sample.setText("POSM Tracking");
            } else {
                textv_sample.setText("Promotion Tracking");
                header_icon.setImageResource(R.drawable.promotion36);
            }


            img_daily_sales.setOnClickListener(this);
            bottom_customerwise_sale.setOnClickListener(this);
            img_promotion.setOnClickListener(this);
            img_home.setOnClickListener(this);
            fab.setOnClickListener(this);
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
                                    ba.cpm.com.lorealba.dailyentry.PromotionActivity.this.finish();
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


                case R.id.bottom_customerwise_sale:
                    startActivity(new Intent(context, SaleTrackingActivity.class));
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    ba.cpm.com.lorealba.dailyentry.PromotionActivity.this.finish();
                    break;

                case R.id.img_promotion:
                    startActivity(new Intent(context, ba.cpm.com.lorealba.dailyentry.PromotionActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    ba.cpm.com.lorealba.dailyentry.PromotionActivity.this.finish();
                    break;
                case R.id.fab:
                    builder = new AlertDialog.Builder(context).setTitle(R.string.parinaam).setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            ba.cpm.com.lorealba.dailyentry.PromotionActivity.this.finish();
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

            }

        }

        public class ValueAdapter extends RecyclerView.Adapter<ba.cpm.com.lorealba.dailyentry.PromotionActivity.ValueAdapter.MyViewHolder> {
            private LayoutInflater inflator;
            List<PromotionMaster> data;
            String report_tpe;

            public ValueAdapter(Context context, List<PromotionMaster> data, String report_tpe) {
                inflator = LayoutInflater.from(context);
                this.report_tpe = report_tpe;
                this.data = data;
            }

            @Override
            public ba.cpm.com.lorealba.dailyentry.PromotionActivity.ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
                View view = inflator.inflate(R.layout.report_posm_tracking, parent, false);
                return new ba.cpm.com.lorealba.dailyentry.PromotionActivity.ValueAdapter.MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(final ba.cpm.com.lorealba.dailyentry.PromotionActivity.ValueAdapter.MyViewHolder holder, final int position) {
                final PromotionMaster current = data.get(position);
                holder.promotion_name.setText(current.getPromotionName());
                holder.promotion_name.setId(position);

                if (report_tpe.equals("1")) {
                    holder.posm_btn_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            current.setPosm_exists_state("Yes");
                            holder.posm_rl_child.setVisibility(View.VISIBLE);
                            holder.posm_rl_child.setId(position);
                            holder.posm_rl_non.setVisibility(View.GONE);
                            holder.posm_rl_non.setId(position);
                            holder.posm_btn_first.setBackgroundColor(getResources().getColor(R.color.pinkicolor));
                            holder.posm_btn_first.setTextColor(getResources().getColor(R.color.white));
                            holder.posm_btn_second.setBackgroundDrawable(getResources().getDrawable(R.drawable.rouded_corner_pinki));
                            holder.posm_btn_second.setTextColor(getResources().getColor(R.color.grayfor_login));
                        }
                    });

                    holder.posm_btn_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            current.setPosm_exists_state("No");
                            holder.posm_rl_child.setVisibility(View.GONE);
                            holder.posm_rl_child.setId(position);
                            holder.posm_rl_non.setVisibility(View.VISIBLE);
                            holder.posm_rl_non.setId(position);
                            holder.posm_btn_second.setBackgroundColor(getResources().getColor(R.color.pinkicolor));
                            holder.posm_btn_second.setTextColor(getResources().getColor(R.color.white));
                            holder.posm_btn_first.setBackgroundDrawable(getResources().getDrawable(R.drawable.rouded_corner_pinki));
                            holder.posm_btn_first.setTextColor(getResources().getColor(R.color.grayfor_login));
                        }
                    });

                    holder.scan_qr_code_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openScanner();
                        }
                    });

                    holder.img_posm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _currectposition = position;
                            _pathforcheck = current.getPromotion_name() + "_POSMIMG_" + getCurrentTime().replace(":", "") + ".jpg";
                            _path = CommonString.FILE_PATH + _pathforcheck;
                            CommonFunctions.startAnncaCameraActivity(context, _path);
                        }
                    });


                    if (!str_posm_img.equals("")) {
                        if (_currectposition == position) {
                            current.setPosm_img(str_posm_img);
                            str_posm_img = "";
                        }
                    }

                    if (!current.getPosm_img().equals("")) {
                        holder.img_posm.setImageResource(R.mipmap.camera_green);
                        holder.img_posm.setId(position);
                    } else {
                        holder.img_posm.setImageResource(R.mipmap.camera_orange);
                        holder.img_posm.setId(position);
                    }


                    //for reason spinner
                    final ArrayList<PromotionGetterSetter> reason_list = new ArrayList<>();
                    PromotionGetterSetter non = new PromotionGetterSetter();
                    non.setReason_name("-Select Reason-");
                    non.setReason_Id("0");
                    reason_list.add(0, non);

                    non = new PromotionGetterSetter();
                    non.setReason_name("Posm Not Available");
                    non.setReason_Id("1");
                    reason_list.add(1, non);

                    non = new PromotionGetterSetter();
                    non.setReason_name("Space Not Available");
                    non.setReason_Id("2");
                    reason_list.add(2, non);


                    holder.posm_spin.setAdapter(new ba.cpm.com.lorealba.dailyentry.PromotionActivity.ReasonSpinnerAdapter(context, R.layout.spinner_text_view, reason_list));

                    for (int i = 0; i < reason_list.size(); i++) {
                        if (reason_list.get(i).getReason_name().equals(current.getPosm_currect_ans())) {
                            holder.posm_spin.setSelection(i);
                            break;
                        }
                    }

                    holder.posm_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            if (pos != 0) {
                                PromotionGetterSetter ans = reason_list.get(pos);
                                current.setPosm_currect_ans(ans.getReason_name());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    if (!promotion_QR_code_content.equals("")) {
                        current.setPosm_qr_data(promotion_QR_code_content);
                    } else {
                        current.setPosm_qr_data("");
                    }

                    if (!current.getPosm_qr_data().equals("")) {
                        holder.text_QR_data.setVisibility(View.VISIBLE);
                        holder.text_QR_data.setText(current.getPosm_qr_data());
                        holder.text_QR_data.setId(position);
                    } else {
                        holder.text_QR_data.setVisibility(View.GONE);
                        holder.text_QR_data.setText("");
                        holder.text_QR_data.setId(position);
                    }
                } else {
                    holder.posm_btn_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            current.setPosm_exists_state("Yes");
                            holder.rl_child_promotion.setVisibility(View.VISIBLE);
                            holder.rl_child_promotion.setId(position);
                            holder.posm_rl_non.setVisibility(View.GONE);
                            holder.posm_rl_non.setId(position);
                            holder.posm_btn_first.setBackgroundColor(getResources().getColor(R.color.pinkicolor));
                            holder.posm_btn_first.setTextColor(getResources().getColor(R.color.white));
                            holder.posm_btn_second.setBackgroundDrawable(getResources().getDrawable(R.drawable.rouded_corner_pinki));
                            holder.posm_btn_second.setTextColor(getResources().getColor(R.color.grayfor_login));
                        }
                    });

                    holder.posm_btn_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            current.setPosm_exists_state("No");
                            holder.rl_child_promotion.setVisibility(View.GONE);
                            holder.rl_child_promotion.setId(position);
                            holder.posm_rl_non.setVisibility(View.VISIBLE);
                            holder.posm_rl_non.setId(position);
                            holder.posm_btn_second.setBackgroundColor(getResources().getColor(R.color.pinkicolor));
                            holder.posm_btn_second.setTextColor(getResources().getColor(R.color.white));
                            holder.posm_btn_first.setBackgroundDrawable(getResources().getDrawable(R.drawable.rouded_corner_pinki));
                            holder.posm_btn_first.setTextColor(getResources().getColor(R.color.grayfor_login));
                        }
                    });

                    holder.img_promotion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _currectposition = position;
                            _pathforcheck = current.getPromotion_name() + "_PROMIMG_" + getCurrentTime().replace(":", "") + ".jpg";
                            _path = CommonString.FILE_PATH + _pathforcheck;
                            CommonFunctions.startAnncaCameraActivity(context, _path);
                        }
                    });


                    if (!str_posm_img.equals("")) {
                        if (_currectposition == position) {
                            current.setPosm_img(str_posm_img);
                            str_posm_img = "";
                        }
                    }

                    if (!current.getPosm_img().equals("")) {
                        holder.img_promotion.setImageResource(R.mipmap.camera_green);
                        holder.img_promotion.setId(position);
                    } else {
                        holder.img_promotion.setImageResource(R.mipmap.camera_orange);
                        holder.img_promotion.setId(position);
                    }


                    //for reason spinner
                    final ArrayList<PromotionGetterSetter> reason_list = new ArrayList<>();
                    PromotionGetterSetter non = new PromotionGetterSetter();
                    non.setReason_name("-Select Reason-");
                    non.setReason_Id("0");
                    reason_list.add(0, non);

                    non = new PromotionGetterSetter();
                    non.setReason_name("Promo Stock Not Connected");
                    non.setReason_Id("1");
                    reason_list.add(1, non);

                    non = new PromotionGetterSetter();
                    non.setReason_name("Space Not Available");
                    non.setReason_Id("2");
                    reason_list.add(2, non);


                    holder.posm_spin.setAdapter(new ba.cpm.com.lorealba.dailyentry.PromotionActivity.ReasonSpinnerAdapter(context, R.layout.spinner_text_view, reason_list));

                    for (int i = 0; i < reason_list.size(); i++) {
                        if (reason_list.get(i).getReason_name().equals(current.getPosm_currect_ans())) {
                            holder.posm_spin.setSelection(i);
                            break;
                        }
                    }

                    holder.posm_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            if (pos != 0) {
                                PromotionGetterSetter ans = reason_list.get(pos);
                                current.setPosm_currect_ans(ans.getReason_name());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

            }


            @Override
            public int getItemCount() {
                return data.size();
            }

            class MyViewHolder extends RecyclerView.ViewHolder {
                ImageView img_posm, img_promotion;
                TextView text_QR_data, promotion_name;
                LinearLayout posm_rl_child, posm_rl_non, rl_child_promotion;
                Button posm_btn_first, posm_btn_second, scan_qr_code_btn;
                Spinner posm_spin;

                public MyViewHolder(View itemView) {
                    super(itemView);
                    posm_rl_child = (LinearLayout) itemView.findViewById(R.id.posm_rl_child);
                    posm_rl_non = (LinearLayout) itemView.findViewById(R.id.posm_rl_non);

                    rl_child_promotion = (LinearLayout) itemView.findViewById(R.id.rl_child_promotion);

                    posm_spin = (Spinner) itemView.findViewById(R.id.posm_spin);

                    posm_btn_first = (Button) itemView.findViewById(R.id.posm_btn_first);
                    posm_btn_second = (Button) itemView.findViewById(R.id.posm_btn_second);
                    scan_qr_code_btn = (Button) itemView.findViewById(R.id.scan_qr_code_btn);

                    img_posm = (ImageView) itemView.findViewById(R.id.customerwise_sale);

                    img_promotion = (ImageView) itemView.findViewById(R.id.img_promotion);

                    text_QR_data = (TextView) itemView.findViewById(R.id.text_QR_data);
                    promotion_name = (TextView) itemView.findViewById(R.id.posm_name);

                }
            }
        }

        private void openScanner() {
            IntentIntegrator intentIntegrator = new IntentIntegrator((Activity) context);
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.setPrompt("Scan the QR code to get the data");
            intentIntegrator.initiateScan();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            switch (resultCode) {
                case 0:
                    Log.i("MakeMachine", "User cancelled");
                    break;
                case -1:
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        try {
                            if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                                String metadata = CommonFunctions.setMetadataAtImages("", "0", "Store Image", "test");
                                Bitmap bmp = CommonFunctions.addMetadataAndTimeStampToImage(context, _path, metadata, "04/02/2019");
                                str_posm_img = _pathforcheck;
                                adapter.notifyDataSetChanged();
                                _pathforcheck = "";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

            if (result != null) {
                if (result.getContents() == null) {
                    promotion_QR_code_content = "";
                    Log.d("ScanActivity", "Cancelled scan");
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    promotion_QR_code_content = result.getContents();
                    adapter.notifyDataSetChanged();
                }
            } else
                super.onActivityResult(requestCode, resultCode, data);
        }

        public class ReasonSpinnerAdapter extends ArrayAdapter<PromotionGetterSetter> {
            List<PromotionGetterSetter> list;
            Context context;
            int resourceId;

            public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<PromotionGetterSetter> list) {
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
                PromotionGetterSetter cm = list.get(position);
                TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
                txt_spinner.setText(list.get(position).getReason_name());

                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
                PromotionGetterSetter cm = list.get(position);
                TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
                txt_spinner.setText(cm.getReason_name());

                return view;
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
                            ba.cpm.com.lorealba.dailyentry.PromotionActivity.this.finish();
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

}
*/
