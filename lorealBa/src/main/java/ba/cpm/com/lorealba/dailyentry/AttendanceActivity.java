package ba.cpm.com.lorealba.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.AlertandMessages;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.AttendanceGetterSetter;
import ba.cpm.com.lorealba.retrofit.PostApi;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btn_present, btn_leave, btn_weekoff, btn_meeting, btn_training;
    RecyclerView attendance_histry_recycle;
    TextView selectedtext_attendee, intime_txt;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username;
    Context context;
    SimpleDateFormat simpleDateFormat;
    String time;
    Calendar calander;
    public ArrayList<AttendanceGetterSetter> selected_list = new ArrayList<>();
    FloatingActionButton fab;
    AttendanceGetterSetter object = null;
    Lorealba_Database database;
    ImageView img_home;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        context = this;
        database = new Lorealba_Database(context);
        database.open();
        iduserinterface();
    }

    private void iduserinterface() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_ID, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        img_home = (ImageView) findViewById(R.id.img_home);

        btn_present = (ImageView) findViewById(R.id.btn_present);
        btn_leave = (ImageView) findViewById(R.id.btn_leave);
        btn_weekoff = (ImageView) findViewById(R.id.btn_weekoff);
        btn_meeting = (ImageView) findViewById(R.id.btn_meeting);
        btn_training = (ImageView) findViewById(R.id.btn_training);
        selectedtext_attendee = (TextView) findViewById(R.id.selectedtext_attendee);
        intime_txt = (TextView) findViewById(R.id.intime_txt);
        attendance_histry_recycle = (RecyclerView) findViewById(R.id.attendance_histry_recycle);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btn_present.setOnClickListener(this);
        btn_leave.setOnClickListener(this);
        btn_weekoff.setOnClickListener(this);
        btn_meeting.setOnClickListener(this);
        btn_training.setOnClickListener(this);
        img_home.setOnClickListener(this);
        fab.setOnClickListener(this);

        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(calander.getTime());
        String monthname = (String) android.text.format.DateFormat.format("MMMM", new Date());
        validateAdapterdata();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_present:
                intime_txt.setText("InTime : " + time);
                selectedtext_attendee.setText("Present");
                break;
            case R.id.btn_leave:
                showLeaveBalance();
                selectedtext_attendee.setText("Leave");
                break;

            case R.id.btn_weekoff:
                selectedtext_attendee.setText("Weekoff");

                break;
            case R.id.btn_meeting:
                intime_txt.setText("InTime : " + time);
                selectedtext_attendee.setText("Meeting");
                break;
            case R.id.btn_training:
                intime_txt.setText("InTime : " + time);
                selectedtext_attendee.setText("Training");
                break;

            case R.id.fab:
                if (!selectedtext_attendee.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(R.string.parinaam).setMessage(R.string.alertsaveData).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                JSONObject jsonObject = null;
                                //region Coverage Data
                                jsonObject = new JSONObject();
                                jsonObject.put("UserId", username);
                                jsonObject.put("Reason_Id", selectedtext_attendee.getText().toString());
                                jsonObject.put("Att_Date", visit_date);
                                jsonObject.put("Image_Url", "");
                                uploadattendancedata(jsonObject.toString(), context, username, visit_date, "", selectedtext_attendee.getText().toString());
                                dialogInterface.dismiss();
                            } catch (JSONException e) {
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
                    Snackbar.make(fab, "Please select user attendance.", Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.img_home:
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                this.finish();
                break;
            default:
                break;
        }
    }


    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<AttendanceGetterSetter> data;

        public ValueAdapter(Context context, List<AttendanceGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.custom_attendace_row, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder holder, final int position) {
            final AttendanceGetterSetter current = data.get(position);
            if (current.getSelecetd_value().equalsIgnoreCase("Weekoff")) {
                holder.rl_attend_weekoff.setVisibility(View.VISIBLE);
                holder.rl_attend_weekoff.setId(position);

                holder.rl_attend_all.setVisibility(View.GONE);
                holder.rl_attend_all.setId(position);
            } else {
                holder.rl_attend_weekoff.setVisibility(View.GONE);
                holder.rl_attend_weekoff.setId(position);

                holder.rl_attend_all.setVisibility(View.VISIBLE);
                holder.rl_attend_all.setId(position);
            }

            holder.attend_month.setText(current.getMonthwith_date());
            holder.attend_month.setId(position);

            holder.intime_attend.setText(current.getIntime());
            holder.intime_attend.setId(position);

            holder.outtime_attend.setText(current.getOut_time());
            holder.outtime_attend.setId(position);

        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView attend_month, intime_attend, outtime_attend;
            LinearLayout rl_attend_all, rl_attend_weekoff;

            public MyViewHolder(View itemView) {
                super(itemView);
                attend_month = (TextView) itemView.findViewById(R.id.attend_month);
                intime_attend = (TextView) itemView.findViewById(R.id.intime_attend);
                outtime_attend = (TextView) itemView.findViewById(R.id.outtime_attend);

                rl_attend_all = (LinearLayout) itemView.findViewById(R.id.rl_attend_all);
                rl_attend_weekoff = (LinearLayout) itemView.findViewById(R.id.rl_attend_weekoff);

            }
        }
    }


    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        this.finish();
    }

    void validateAdapterdata() {
        object = new AttendanceGetterSetter();
        object.setIntime("10.02 AM");
        object.setOut_time("07:12 PM");
        object.setSelecetd_value("Present");
        object.setMonthwith_date("March - 04 ");
        selected_list.add(object);

        object = new AttendanceGetterSetter();
        object.setIntime("10.15 AM");
        object.setOut_time("07:20 PM");
        object.setSelecetd_value("Present");
        object.setMonthwith_date("March - 03 ");
        selected_list.add(object);

        object = new AttendanceGetterSetter();
        object.setIntime("");
        object.setOut_time("07:20 PM");
        object.setSelecetd_value("Weekoff");
        object.setMonthwith_date("");
        selected_list.add(object);

        attendance_histry_recycle.setAdapter(new ValueAdapter(context, selected_list));
        attendance_histry_recycle.setLayoutManager(new LinearLayoutManager(context));
        attendance_histry_recycle.setVisibility(View.VISIBLE);

    }

    private void showLeaveBalance() {
        final MultiPurposeDialog multiPurposeDialog = new MultiPurposeDialog(context);
        multiPurposeDialog.setContentView(R.layout.leave_balance);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(multiPurposeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        multiPurposeDialog.getWindow().setAttributes(lp);
        multiPurposeDialog.setCancelable(true);
        Button ob_btn = (Button) multiPurposeDialog.findViewById(R.id.ob_btn);
        ob_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedtext_attendee.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(R.string.parinaam).setMessage(R.string.alertsaveData).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                JSONObject jsonObject = null;
                                //region Coverage Data
                                jsonObject = new JSONObject();
                                jsonObject.put("UserId", username);
                                jsonObject.put("Reason_Id", selectedtext_attendee.getText().toString());
                                jsonObject.put("Att_Date", visit_date);
                                jsonObject.put("Image_Url", "");
                                uploadattendancedata(jsonObject.toString(), context, username, visit_date, "", selectedtext_attendee.getText().toString());
                                dialogInterface.dismiss();
                                multiPurposeDialog.dismiss();
                            } catch (JSONException e) {
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
                    Snackbar.make(fab, "Please select user attendance.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        multiPurposeDialog.show();
    }

    public class MultiPurposeDialog extends Dialog {
        public MultiPurposeDialog(Context context) {
            super(context);
            // DIALOG USER_INTERFACE TEMPLATE
            WindowManager.LayoutParams wmLayoutParams = getWindow().getAttributes();
            wmLayoutParams.gravity = Gravity.CENTER;
            getWindow().setAttributes(wmLayoutParams);
            setTitle(null);
            setCancelable(true);
            setOnCancelListener(null);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    public void uploadattendancedata(String jsondata, final Context context, final String _user_Id, final String visit_date, final String att_image, final String reason) {
        try {
            loading = ProgressDialog.show(context, "Processing", "Please wait...", false, false);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).connectTimeout(20, TimeUnit.SECONDS).build();
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsondata.toString());
            Retrofit adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            PostApi api = adapter.create(PostApi.class);
            retrofit2.Call<ResponseBody> call = api.getAttendanceDetails(jsonData);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            if (data.contains("1")) {
                                if (selectedtext_attendee.getText().toString().equalsIgnoreCase("Present")) {
                                    editor.putString(CommonString.KEY_ATTENDENCE_STATUS, reason);
                                    editor.apply();
                                    database.open();
                                    database.insertAttendenceData(_user_Id, visit_date, att_image, reason);
                                    Intent intent = new Intent(context, StoreListActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    AttendanceActivity.this.finish();
                                    loading.dismiss();
                                } else {
                                    editor.putString(CommonString.KEY_ATTENDENCE_STATUS, reason);
                                    editor.apply();
                                    Intent intent = new Intent(context, DealerBoardActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    AttendanceActivity.this.finish();
                                }
                            } else {
                                if (data.contains("0")) {
                                    editor.putString(CommonString.KEY_ATTENDENCE_STATUS, reason);
                                    editor.apply();
                                    database.open();
                                    database.insertAttendenceData(_user_Id, visit_date, att_image, reason);
                                    Intent intent = new Intent(context, DealerBoardActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    AttendanceActivity.this.finish();
                                    loading.dismiss();
                                } else {
                                    throw new java.lang.Exception();
                                }
                            }
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                            e.printStackTrace();
                            loading.dismiss();
                            editor.putString(CommonString.KEY_ATTENDENCE_STATUS, "0");
                            editor.apply();
                            AlertandMessages.showAlertlogin((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + e.toString() + ")");
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    loading.dismiss();
                    editor.putString(CommonString.KEY_ATTENDENCE_STATUS, "0");
                    editor.apply();
                    if (t != null) {
                        if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                            AlertandMessages.showAlertlogin((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.toString() + ")");
                        }
                    } else {
                        AlertandMessages.showAlertlogin((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loading.dismiss();
            editor.putString(CommonString.KEY_ATTENDENCE_STATUS, "0");
            editor.apply();
            AlertandMessages.showAlertlogin((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + e.toString() + ")");
        }
    }


}
