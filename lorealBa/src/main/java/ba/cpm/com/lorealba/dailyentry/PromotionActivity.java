package ba.cpm.com.lorealba.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import ba.cpm.com.lorealba.gsonGetterSetter.NonPromotionReason;
import ba.cpm.com.lorealba.gsonGetterSetter.PromotionGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.PromotionMaster;

import static ba.cpm.com.lorealba.constant.CommonFunctions.getCurrentTime;

public class PromotionActivity extends AppCompatActivity implements View.OnClickListener {
    String store_cd, visit_date, username;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String str_promotion_img = "", _pathforcheck, _path, promotion_QR_code_content = "", type_of_promotion = "";
    ArrayList<PromotionMaster> promotionList = new ArrayList<>();
    ImageView img_daily_sales, bottom_customerwise_sale, img_promotion, img_home, header_icon;
    RecyclerView drawer_layout_recycle_store;
    ValueAdapter adapter;
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
        promotionList = db.getpromotion_inserted_data(store_cd, visit_date);
        if (promotionList.size() == 0) {
            promotionList = db.getpromotion_master_data();
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.edit_icon_white));
        }
        if (promotionList.size() > 0) {
            adapter = new ValueAdapter(context, promotionList, type_of_promotion);
            drawer_layout_recycle_store.setAdapter(adapter);
            drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
        }
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

        textv_sample.setText("Promotion Tracking");
        header_icon.setImageResource(R.drawable.promotion36);

        img_daily_sales.setOnClickListener(this);
        bottom_customerwise_sale.setOnClickListener(this);
        img_promotion.setOnClickListener(this);
        img_home.setOnClickListener(this);
        fab.setOnClickListener(this);

        drawer_layout_recycle_store.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.img_home:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                PromotionActivity.this.finish();
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
                PromotionActivity.this.finish();
                break;

            case R.id.img_promotion:
                startActivity(new Intent(context, PromotionActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                PromotionActivity.this.finish();
                break;
            case R.id.fab:
                if (validate_condition(promotionList)) {
                    builder = new AlertDialog.Builder(context).setTitle(R.string.parinaam).setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertpromotion_captured_data(store_cd, visit_date, promotionList);
                            dialogInterface.dismiss();
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            PromotionActivity.this.finish();
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
        }

    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<PromotionMaster> data;
        String report_tpe;

        public ValueAdapter(Context context, List<PromotionMaster> data, String report_tpe) {
            inflator = LayoutInflater.from(context);
            this.report_tpe = report_tpe;
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.report_posm_tracking, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final PromotionMaster current = data.get(position);
            holder.promotion_name.setText(current.getPromotionName());
            holder.promotion_name.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
            holder.promotion_name.setId(position);

            holder.start_date.setText("Start Date : " + current.getStartDate());
            holder.start_date.setId(position);

            holder.end_date.setText("End Date : " + current.getEndDate());
            holder.end_date.setId(position);

            if (!current.getPromotion_exists_state().equals("") && current.getPromotion_exists_state().equalsIgnoreCase("Yes")) {
                current.setPromotion_exists_state("Yes");
                holder.rl_child_promotion.setVisibility(View.VISIBLE);
                holder.rl_child_promotion.setId(position);
                holder.promotion_rl_non.setVisibility(View.GONE);
                holder.promotion_rl_non.setId(position);
                holder.promotion_btn_first.setBackgroundColor(getResources().getColor(R.color.pinkicolor));
                holder.promotion_btn_first.setTextColor(getResources().getColor(R.color.white));
                holder.promotion_btn_second.setBackgroundDrawable(getResources().getDrawable(R.drawable.rouded_corner_pinki));
                holder.promotion_btn_second.setTextColor(getResources().getColor(R.color.grayfor_login));
                current.setPromotion_currect_ans_Id("0");
                current.setPromotion_currect_ans("");
                holder.promotion_spin.setSelection(0);
            } else if (!current.getPromotion_exists_state().equals("") && current.getPromotion_exists_state().equalsIgnoreCase("Yes")) {
                current.setPromotion_exists_state("No");
                holder.rl_child_promotion.setVisibility(View.GONE);
                holder.rl_child_promotion.setId(position);
                holder.promotion_rl_non.setVisibility(View.VISIBLE);
                holder.promotion_rl_non.setId(position);
                holder.promotion_btn_second.setBackgroundColor(getResources().getColor(R.color.pinkicolor));
                holder.promotion_btn_second.setTextColor(getResources().getColor(R.color.white));
                holder.promotion_btn_first.setBackgroundDrawable(getResources().getDrawable(R.drawable.rouded_corner_pinki));
                holder.promotion_btn_first.setTextColor(getResources().getColor(R.color.grayfor_login));

                holder.img_promotion.setImageResource(R.mipmap.camera_orange);
                holder.img_promotion.setId(position);
                current.setPromotion_img("");
            }

            holder.promotion_btn_first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current.setPromotion_exists_state("Yes");
                    holder.rl_child_promotion.setVisibility(View.VISIBLE);
                    holder.rl_child_promotion.setId(position);
                    holder.promotion_rl_non.setVisibility(View.GONE);
                    holder.promotion_rl_non.setId(position);
                    holder.promotion_btn_first.setBackgroundColor(getResources().getColor(R.color.pinkicolor));
                    holder.promotion_btn_first.setTextColor(getResources().getColor(R.color.white));
                    holder.promotion_btn_second.setBackgroundDrawable(getResources().getDrawable(R.drawable.rouded_corner_pinki));
                    holder.promotion_btn_second.setTextColor(getResources().getColor(R.color.grayfor_login));
                    current.setPromotion_currect_ans_Id("0");
                    current.setPromotion_currect_ans("");
                    holder.promotion_spin.setSelection(0);
                }
            });

            holder.promotion_btn_second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current.setPromotion_exists_state("No");
                    holder.rl_child_promotion.setVisibility(View.GONE);
                    holder.rl_child_promotion.setId(position);
                    holder.promotion_rl_non.setVisibility(View.VISIBLE);
                    holder.promotion_rl_non.setId(position);
                    holder.promotion_btn_second.setBackgroundColor(getResources().getColor(R.color.pinkicolor));
                    holder.promotion_btn_second.setTextColor(getResources().getColor(R.color.white));
                    holder.promotion_btn_first.setBackgroundDrawable(getResources().getDrawable(R.drawable.rouded_corner_pinki));
                    holder.promotion_btn_first.setTextColor(getResources().getColor(R.color.grayfor_login));

                    holder.img_promotion.setImageResource(R.mipmap.camera_orange);
                    holder.img_promotion.setId(position);
                    current.setPromotion_img("");

                }
            });

            holder.img_promotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _currectposition = position;
                    _pathforcheck = current.getPromotionId().toString() + "_PROMIMG_" + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    CommonFunctions.startAnncaCameraActivity(context, _path);
                }
            });

            if (!str_promotion_img.equals("")) {
                if (_currectposition == position) {
                    current.setPromotion_img(str_promotion_img);
                    str_promotion_img = "";
                }
            }

            if (!current.getPromotion_img().equals("")) {
                holder.img_promotion.setImageResource(R.mipmap.camera_green);
                holder.img_promotion.setId(position);
            } else {
                holder.img_promotion.setImageResource(R.mipmap.camera_orange);
                holder.img_promotion.setId(position);
            }


            //for reason spinner
            final ArrayList<NonPromotionReason> reason_list = db.getnon_promotion_reasonList();
            NonPromotionReason non = new NonPromotionReason();
            non.setPReason("- Select -");
            non.setPReasonId(0);
            reason_list.add(0, non);

            holder.promotion_spin.setAdapter(new ReasonSpinnerAdapter(context, R.layout.spinner_text_view, reason_list));

            for (int i = 0; i < reason_list.size(); i++) {
                if (reason_list.get(i).getPReasonId().toString().equals(current.getPromotion_currect_ans_Id())) {
                    holder.promotion_spin.setSelection(i);
                    break;
                }
            }

            holder.promotion_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (pos != 0) {
                        NonPromotionReason ans = reason_list.get(pos);
                        current.setPromotion_currect_ans(ans.getPReason());
                        current.setPromotion_currect_ans_Id(ans.getPReasonId().toString());
                    } else {
                        current.setPromotion_currect_ans("");
                        current.setPromotion_currect_ans_Id("0");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            holder.reference_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show_offer_image_dialog(context, position, current.getImageName());
                }
            });
        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView img_promotion;
            TextView promotion_name, start_date, end_date, reference_img;
            LinearLayout promotion_rl_non, rl_child_promotion;
            Button promotion_btn_first, promotion_btn_second;
            Spinner promotion_spin;

            public MyViewHolder(View itemView) {
                super(itemView);
                promotion_rl_non = (LinearLayout) itemView.findViewById(R.id.promotion_rl_non);
                rl_child_promotion = (LinearLayout) itemView.findViewById(R.id.rl_child_promotion);
                promotion_spin = (Spinner) itemView.findViewById(R.id.promotion_spin);
                promotion_btn_first = (Button) itemView.findViewById(R.id.posm_btn_first);
                promotion_btn_second = (Button) itemView.findViewById(R.id.posm_btn_second);
                img_promotion = (ImageView) itemView.findViewById(R.id.img_promotion);
                promotion_name = (TextView) itemView.findViewById(R.id.posm_name);
                start_date = (TextView) itemView.findViewById(R.id.start_date);
                end_date = (TextView) itemView.findViewById(R.id.end_date);
                reference_img = (TextView) itemView.findViewById(R.id.reference_img);

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
                            String metadata = CommonFunctions.setMetadataAtImages(preferences.getString(CommonString.KEY_STORE_NAME, ""), store_cd, "Promotion Image", username);
                            CommonFunctions.addMetadataAndTimeStampToImage(context, _path, metadata, visit_date);
                            str_promotion_img = _pathforcheck;
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

    public class ReasonSpinnerAdapter extends ArrayAdapter<NonPromotionReason> {
        List<NonPromotionReason> list;
        Context context;
        int resourceId;

        public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<NonPromotionReason> list) {
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
            NonPromotionReason cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getPReason());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            NonPromotionReason cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getPReason());

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
                        PromotionActivity.this.finish();
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


    private boolean validate_condition(ArrayList<PromotionMaster> promotionList) {
        boolean status = true;
        for (int k = 0; k < promotionList.size(); k++) {
            PromotionMaster object = promotionList.get(k);
            if (!object.getPromotion_exists_state().equals("")) {
                if (object.getPromotion_exists_state().equalsIgnoreCase("Yes")) {
                    if (object.getPromotion_img().equals("")) {
                        showTost("Please capture " + object.getPromotionName() + " image.");
                        status = false;
                        break;
                    }
                } else {
                    if (object.getPromotion_currect_ans_Id().equals("0")) {
                        showTost("Please Select reason of " + object.getPromotionName() + ".");
                        status = false;
                        break;
                    }
                }
            } else {
                showTost("Please Click Exists of " + object.getPromotionName() + ".");
                status = false;
            }
        }
        return status;
    }

    void showTost(String msg) {
        Snackbar.make(fab, msg, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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


    private void show_offer_image_dialog(final Context context, int position, String reference_img) {
        final MultiPurposeDialog multiPurposeDialog = new MultiPurposeDialog(context);
        multiPurposeDialog.setContentView(R.layout.dialog_offer);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(multiPurposeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        multiPurposeDialog.getWindow().setAttributes(lp);
        multiPurposeDialog.setCancelable(true);
        ImageView img_offer_page = (ImageView) multiPurposeDialog.findViewById(R.id.img_offer_page);
        ImageView btn_ok = (ImageView) multiPurposeDialog.findViewById(R.id.btn_ok);

        File dir = new File(CommonString.Promotion_ReF_File_Path + reference_img);
        if (dir.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(dir.getAbsolutePath());
            img_offer_page.setImageBitmap(myBitmap);
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiPurposeDialog.dismiss();
            }
        });

        multiPurposeDialog.show();
    }


}
