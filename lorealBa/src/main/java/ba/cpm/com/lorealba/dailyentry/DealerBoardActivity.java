package ba.cpm.com.lorealba.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.LorealBaLoginActivty;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonFunctions;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.delegates.NavMenuItemGetterSetter;
import ba.cpm.com.lorealba.download.DownloadActivity;
import ba.cpm.com.lorealba.upload.PreviousDataUploadActivity;
import ba.cpm.com.lorealba.retrofit.PostApi;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DealerBoardActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recycle_recce, stock_recycle, recycler_self;
    ValueAdapter adapter, stock_adapter, posm_adapter;
    SharedPreferences preferences;
    ImageView btn_offerat_mystore, take_backup;
    Lorealba_Database db;
    String date, userId;
    Context context;
    String jsonString = "",username="";

    String result= "";
    int status = 0;


    boolean isvalid;
    JSONObject jsonObject = new JSONObject();
    String title="",body="",path="",visited_date="",storeId,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dealer_board);
        context = this;
        db = new Lorealba_Database(context);
        db.open();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        storeId = preferences.getString(CommonString.KEY_STORE_ID, null);
        storeId="1";
        recycle_recce = (RecyclerView) findViewById(R.id.recycle_recce);
        stock_recycle = (RecyclerView) findViewById(R.id.stock_recycle);
        recycler_self = (RecyclerView) findViewById(R.id.recycler_self);

        btn_offerat_mystore = (ImageView) findViewById(R.id.btn_offerat_mystore);
        take_backup = (ImageView) findViewById(R.id.take_backup);
        take_backup.setOnClickListener(this);
        adapter = new ValueAdapter(getApplicationContext(), getdata());
        recycle_recce.setAdapter(adapter);
        recycle_recce.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        //  recycle_recce.getLayoutManager().smoothScrollToPosition(recycle_recce,new RecyclerView.State(), recycle_recce.getAdapter().getItemCount());

        stock_adapter = new ValueAdapter(getApplicationContext(), getdatafor_stock());
        stock_recycle.setAdapter(stock_adapter);
        stock_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        //stock_recycle.getLayoutManager().smoothScrollToPosition(stock_recycle,new RecyclerView.State(), stock_recycle.getAdapter().getItemCount());

        posm_adapter = new ValueAdapter(getApplicationContext(), getdatafor_sale_promotion());
        recycler_self.setAdapter(posm_adapter);
        recycler_self.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        //recycler_self.getLayoutManager().smoothScrollToPosition(recycler_self,new RecyclerView.State(), recycler_self.getAdapter().getItemCount());
        btn_offerat_mystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, OfferActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        });


        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equalsIgnoreCase("title")) {
                    title = getIntent().getExtras().getString(key);
                } else if (key.equalsIgnoreCase("body")) {
                    body = getIntent().getExtras().getString(key);
                } else if (key.equalsIgnoreCase("path")) {
                    path = getIntent().getExtras().getString(key);
                } else if (key.equalsIgnoreCase("Currentdate")) {
                    visited_date = getIntent().getExtras().getString(key);
                }else if (key.equalsIgnoreCase("type")) {
                    type = getIntent().getExtras().getString(key);
                }
            }

            if (!title.equalsIgnoreCase("") && !body.equalsIgnoreCase("")) {
                db.open();
                long value = db.insertNotificationData(title, body, path, visited_date, type);
                if (value > 0) {
                  //  createNotificationView();
                    Toast.makeText(context, "Notification Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Notification Not Inserted", Toast.LENGTH_SHORT).show();
                }
            }

            if(!title.equals("")){
                Intent in = new Intent(getApplicationContext(), NotificationDetailActivity.class);
                in.putExtra("Type", type);
                in.putExtra("Title", title);
                in.putExtra("Body", body);
                in.putExtra("Path", path);
                startActivity(in);
            }
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

//                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        try {
                            jsonObject.put("UserName", username);
                            // jsonObject.put("UserName", "firoz.alam@in.cpm-int.com");
                            jsonObject.put("TokenId",token);
                            jsonString = jsonObject.toString();
                            result =  uploadDeviceTokenDetails(jsonString, CommonString.UPLOAD_DEVICE_TOKEN_DETAILS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_backup:
                take_backup();
                break;
        }
    }


    private String uploadDeviceTokenDetails(String jsonString, Object type) {
        try {

            status = 0;
            isvalid = false;
            final String[] data_global = {""};

            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();

            Retrofit adapter = new Retrofit.Builder()
                    .baseUrl(CommonString.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            PostApi api = adapter.create(PostApi.class);
            Call<ResponseBody> call = null;

            if (type == CommonString.UPLOAD_DEVICE_TOKEN_DETAILS) {
                call = api.uploadTokenDetails(jsonData);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            data = data.substring(1, data.length() - 1).replace("\\", "");
                            if(data.equalsIgnoreCase("")){
                                data_global[0] = "";
                                isvalid = true;
                                status = 1;
                            }else{
                                data = data.substring(1, data.length() - 1).replace("\\", "");
                                data_global[0] = data;
                                isvalid = true;
                                status = 1;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            isvalid = true;
                            status = -2;
                        }
                    } else {
                        isvalid = true;
                        status = -1;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isvalid = true;
                    if (t instanceof SocketTimeoutException) {
                        status = 3;
                    } else if (t instanceof IOException) {
                        status = 3;
                    } else {
                        status = 3;
                    }

                }
            });

            if (status == 1) {
                return data_global[0];
            } else if (status == 2) {
                return CommonString.MESSAGE_NO_RESPONSE_SERVER;
            } else if (status == 3) {
                return CommonString.MESSAGE_SOCKETEXCEPTION;
            } else if (status == -2) {
                return CommonString.MESSAGE_INVALID_JSON;
            } else {
                return CommonString.KEY_FAILURE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonString.KEY_FAILURE;
        }
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<NavMenuItemGetterSetter> data;

        public ValueAdapter(Context context, List<NavMenuItemGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.dealor_icons, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final NavMenuItemGetterSetter current = data.get(position);
            holder.icon.setImageResource(current.getIconImg());

            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current.getIconImg() == R.drawable.data_sink) {
                        if (CommonFunctions.CheckNetAvailability(context)) {
                            if (!db.isCoverageDataFilled(date)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(R.string.parinaam);
                                builder.setMessage(getResources().getString(R.string.want_download_data)).setCancelable(false)
                                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                try {
                                                    db.open();
                                                    db.deletePreviousUploadedData(date);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
                                                startActivity(in);
                                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                                            }
                                        })
                                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle(R.string.parinaam);
                                builder.setMessage(getResources().getString(R.string.previous_data_upload)).setCancelable(false)
                                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent in = new Intent(getApplicationContext(), PreviousDataUploadActivity.class);
                                                startActivity(in);
                                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } else {
                            Snackbar.make(holder.icon, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }

                    } else if (current.getIconImg() == R.drawable.attendance) {
                        if (db.getStoreData(date).size() > 0) {
                            if (preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "") != null && !preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "").equals("")) {
                                Snackbar.make(holder.icon, getString(R.string.present), Snackbar.LENGTH_LONG).show();
                                startActivity(new Intent(context, StoreListActivity.class));
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            } else {
                                startActivity(new Intent(context, AttendanceActivity.class));
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            }
                        } else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }

                    } else if (current.getIconImg() == R.drawable.reports) {
                        if (db.getStoreData(date).size() > 0) {
                            if (preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "") == null) {
                                Snackbar.make(holder.icon, "Mark your attendace first", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            } else if (preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "") != null && !preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "").equals("") && preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "").equalsIgnoreCase("Present")) {
                                startActivity(new Intent(context, ReportsActivity.class));
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            }
                        } else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }

                    } else if (current.getIconImg() == R.drawable.daily_sale) {
                        startActivity(new Intent(context, MyLibraryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.knowledge) {
                        startActivity(new Intent(context, MyLibraryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.daily_stock) {

                        if (db.getStoreData(date).size() > 0) {
                            startActivity(new Intent(context, SignatureActiivty.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }
                    } else if (current.getIconImg() == R.drawable.damaged_stock) {
                        startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    } else if (current.getIconImg() == R.drawable.inward_stock) {

                        if (db.getStoreData(date).size() > 0) {
                            if (db.isInwoardFilledData(storeId)) {
                                Snackbar.make(holder.icon, "Already Data filled.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            }else {
                                startActivity(new Intent(context, InwardStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            }

                        }else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }


                    } else if (current.getIconImg() == R.drawable.customer_wise_sales) {
                        if (db.getStoreData(date).size() > 0) {
                            if (preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "") == null || preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "").equals("")) {
                              //  Snackbar.make(holder.icon, "Mark your attendance first", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            } else if (preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "") != null && !preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "").equals("") && preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "").equalsIgnoreCase("Present")) {
                                startActivity(new Intent(context, SaleTrackingActivity.class));
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            }
                        } else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }

                    } else if (current.getIconImg() == R.drawable.posm_tracking) {
                        if (db.getStoreData(date).size() > 0) {
                            Snackbar.make(holder.icon, "Posm Master Data Not Found.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                            // startActivity(new Intent(context, PromotionActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                            // overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }

                    } else if (current.getIconImg() == R.drawable.promotion_tracking) {
                        if (db.getStoreData(date).size() > 0) {
                            if (preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "") == null) {
                                Snackbar.make(holder.icon, "Mark your attendace first", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            } else if (preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "") != null && !preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "").equals("") && preferences.getString(CommonString.KEY_ATTENDENCE_STATUS, "").equalsIgnoreCase("Present")) {
                                startActivity(new Intent(context, PromotionActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            }
                        } else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }

                    } else if (current.getIconImg() == R.drawable.sample_stock) {

                        if (db.getStoreData(date).size() > 0) {

                            if (db.isPwpGwpFilledData(storeId)) {
                                Snackbar.make(holder.icon, "Already Data filled.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            }else {
                                startActivity(new Intent(context, PwpGwpActivity.class));
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            }

                        }else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }

                    } else if (current.getIconImg() == R.drawable.tester_stock) {

                        if (db.getStoreData(date).size() > 0) {
                            startActivity(new Intent(context, SignatureTesterActiivty.class));
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }

                    } else if (current.getIconImg() == R.drawable.notifications) {
                        if (db.getStoreData(date).size() > 0) {
                            startActivity(new Intent(context, NotificationsActivity.class));
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(holder.icon, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView icon_txtname;

            public MyViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.list_icon);
                icon_txtname = (TextView) itemView.findViewById(R.id.icon_txtname);
            }
        }
    }

    public List<NavMenuItemGetterSetter> getdata() {
        List<NavMenuItemGetterSetter> data = new ArrayList<>();
        int attendance = 0, knowledge, performance, reports, notification, data_sink, store_work;
        attendance = R.drawable.attendance;
        notification = R.drawable.notifications;
        knowledge = R.drawable.knowledge;

        performance = R.drawable.performance;
        reports = R.drawable.reports;
        data_sink = R.drawable.data_sink;
        store_work = R.drawable.store_work;

        int img[] = {data_sink, attendance, notification, reports, knowledge};
        for (int i = 0; i < img.length; i++) {
            NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
            recData.setIconImg(img[i]);
            data.add(recData);
        }

        return data;
    }

    public List<NavMenuItemGetterSetter> getdatafor_stock() {
        List<NavMenuItemGetterSetter> data = new ArrayList<>();
       int daily_stock, damaged_stock, inword_stock, sample_stock, tester_stock;

        daily_stock = R.drawable.daily_stock;
        //damaged_stock = R.drawable.damaged_stock;
        inword_stock = R.drawable.inward_stock;

        sample_stock = R.drawable.sample_stock;
        tester_stock = R.drawable.tester_stock;

       // int img[] = {daily_stock, damaged_stock, inword_stock, sample_stock, tester_stock};
        int img[] = {daily_stock, inword_stock, sample_stock, tester_stock};

        for (int i = 0; i < img.length; i++) {
            NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
            recData.setIconImg(img[i]);
            data.add(recData);
        }

        return data;
    }

    public List<NavMenuItemGetterSetter> getdatafor_sale_promotion() {
        List<NavMenuItemGetterSetter> data = new ArrayList<>();
        int customer_wise_Sale, daily_sales, posm_tracking, promotion_tracking;

        customer_wise_Sale = R.drawable.customer_wise_sales;
        // daily_sales = R.drawable.daily_sale;
        posm_tracking = R.drawable.posm_tracking;

        promotion_tracking = R.drawable.promotion_tracking;

        int img[] = {customer_wise_Sale, /*daily_sales,*/ posm_tracking, promotion_tracking};
        for (int i = 0; i < img.length; i++) {
            NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
            recData.setIconImg(img[i]);
            data.add(recData);
        }

        return data;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Parinaam").setMessage("Do you want to Exit ?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.finishAffinity((Activity) context);
                startActivity(new Intent(context, LorealBaLoginActivty.class));
                overridePendingTransition(ba.cpm.com.lorealba.R.anim.activity_back_in, ba.cpm.com.lorealba.R.anim.activity_back_out);
                DealerBoardActivity.this.finish();
                dialog.dismiss();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void take_backup() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context).setTitle("Parinaam");
        builder1.setMessage("Are you sure you want to take the backup of your data ?").setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @SuppressWarnings("resource")
                    public void onClick(DialogInterface dialog,
                                        int id) {

                        try {
                            File file = new File(Environment.getExternalStorageDirectory(), "LorealBaPitchBackup");
                            if (!file.isDirectory()) {
                                file.mkdir();
                            }
                            File sd = Environment.getExternalStorageDirectory();
                            File data = Environment.getDataDirectory();
                            if (sd.canWrite()) {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yy");
                                String dateString = sdf.format(date);
                                String currentDBPath = "//data//cpm.com.lorealbapitch//databases//" + Lorealba_Database.DATABASE_NAME;
                                String backupDBPath = "LorealBaPitchBackup" + dateString.replace('/', '-');
                                File currentDB = new File(data, currentDBPath);
                                File backupDB = new File("/mnt/sdcard/LorealBaPitchBackup/", backupDBPath);
                                Snackbar.make(btn_offerat_mystore, "Database Exported Successfully.", Snackbar.LENGTH_SHORT).show();
                                if (currentDB.exists()) {
                                    FileChannel src = new FileInputStream(currentDB).getChannel();
                                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert1 = builder1.create();
        alert1.show();

    }
}
