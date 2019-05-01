package ba.cpm.com.lorealba.oneQAD;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ba.cpm.com.lorealba.LorealBaLoginActivty;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.AlertandMessages;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.dailyentry.DealerBoardActivity;
import ba.cpm.com.lorealba.gettersetter.LoginGsonGetterSetter;
import ba.cpm.com.lorealba.gettersetter.TodayQuestion;
import ba.cpm.com.lorealba.progressbar.CircularProgressBar;
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


public class OneQADActivity extends AppCompatActivity {

    int durationMillis = 2000;
    CircularProgressBar c2;
    CountDownTimer countDownTimer;
    private Dialog dialog;
    Button btn1, btn2, btn3, btn4;
    RelativeLayout rel_text, rel_progress;
    TextView tv_welldone, tv_question, tv_time_Up;
    FloatingActionButton fab;

    Animation animBounce, animBounceZoom;
    RecyclerView rec_quiz;
    boolean checkFlag = false;
    boolean timeElapseFlag = false;
    ValueAdapter valueAdapter;
    private Retrofit adapter;
    RelativeLayout rel_qad;
    Context context;
    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor = null;
    String userId, visit_date;
    TodayQuestion selectedObj = new TodayQuestion();
    final AnswerData answerData = new AnswerData();
    List<TodayQuestion> todayQuestion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_one_qad);
        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        userId = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
        animBounceZoom = AnimationUtils.loadAnimation(context, R.anim.bounce_with_zoom);
        rec_quiz = (RecyclerView) findViewById(R.id.rec_quiz);
        rel_text = (RelativeLayout) findViewById(R.id.rel_layout_text);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_layout_progress);
        tv_welldone = (TextView) findViewById(R.id.tv_welldone);
        tv_question = (TextView) findViewById(R.id.tv_question);
        tv_time_Up = (TextView) findViewById(R.id.tv_time_Up);
        rel_qad = (RelativeLayout) findViewById(R.id.rel_qad);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedObj.getQuestion() == null) {
                    answerData.setAnswer_id("0");
                    answerData.setQuestion_id(todayQuestion.get(0).getQuestionId().toString());
                } else {
                    answerData.setAnswer_id(selectedObj.getAnswerId().toString());
                    answerData.setQuestion_id(selectedObj.getQuestionId().toString());
                }
                answerData.setUsername(userId);
                answerData.setVisit_date(visit_date);
                if ((checkNetIsAvailable())) {
                    try {
                        final String[] data_global = {""};
                        final OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).connectTimeout(20, TimeUnit.SECONDS).build();
                        JSONArray answerDetaills = new JSONArray();
                        JSONObject object = new JSONObject();

                        //upload answer OneQad
                        object.put("ANSWER_ID", "0");
                        object.put("QUESTION_ID", answerData.getQuestion_id());
                        object.put("VISIT_DATE", answerData.getVisit_date());
                        object.put("USER_NAME", answerData.getUsername());
                        answerDetaills.put(object);

                        object = new JSONObject();
                        object.put("MID", "0");
                        object.put("Keys", "TODAY_ANSWER");
                        object.put("JsonData", answerDetaills.toString());
                        object.put("UserId", userId);

                        String jsonString = object.toString();
                        if (jsonString != null && !jsonString.equals("")) {
                            showProgress();
                            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                            adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
                            PostApi api = adapter.create(PostApi.class);
                            Call<ResponseBody> call = api.getUploadJsonDetail(jsonData);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    ResponseBody responseBody = response.body();
                                    String data = null;
                                    if (responseBody != null && response.isSuccessful()) {
                                        try {
                                            data = response.body().string();
                                            if (data.equalsIgnoreCase("")) {
                                            } else {
                                                data = data.substring(1, data.length() - 1).replace("\\", "");
                                                data_global[0] = data;
                                                if (data.contains("Success")) {
                                                    String visit_date = preferences.getString(CommonString.KEY_DATE, null);
                                                    editor = preferences.edit();
                                                    editor.putBoolean(CommonString.KEY_IS_QUIZ_DONE + visit_date, true);
                                                    editor.commit();
                                                    Intent intent = new Intent(context, DealerBoardActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    editor = preferences.edit();
                                                    editor.putString(CommonString.KEY_QUESTION_CD + visit_date, answerData.getQuestion_id());
                                                    editor.putString(CommonString.KEY_ANSWER_CD + visit_date, answerData.getAnswer_id());
                                                    editor.commit();
                                                    Intent intent = new Intent(context, DealerBoardActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        } catch (Exception e) {
                                            dialog.dismiss();
                                            AlertandMessages.showAlertlogin((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION);
                                        }
                                    } else {
                                        dialog.dismiss();
                                        AlertandMessages.showAlertlogin((Activity) context, "Check Your Internet Connection");

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    if (t instanceof SocketTimeoutException) {
                                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                    } else {
                                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                    }
                                }
                            });

                        }
                        dialog.cancel();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("No internet connection");
                }
            }
        });

        c2 = (CircularProgressBar) findViewById(R.id.circularprogressbar1);
        c2.setTitle("June");

        getOneqad();
    }

    private boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void showToast(String message) {
        Snackbar.make(rec_quiz, message, Snackbar.LENGTH_LONG).show();
    }

    public void showComment() {
        Dialog dialog = new Dialog(OneQADActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.comment_layout);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        dialog.show();

    }

    void showProgress() {
        dialog = new Dialog(OneQADActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        dialog.setCancelable(false);
        window.setAttributes(wlp);

        dialog.show();
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;

        List<TodayQuestion> data;

        public ValueAdapter(Context context, List<TodayQuestion> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.oneqad_answer_layout, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
            final TodayQuestion current = data.get(position);
            viewHolder.btn_answer.setText(current.getAnswer());
            viewHolder.btn_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!timeElapseFlag && !checkFlag) {
                        checkFlag = true;
                        selectedObj = current;
                        //disable timer
                        countDownTimer.cancel();
                        fab.setVisibility(View.VISIBLE);
                        valueAdapter.notifyDataSetChanged();
                    }
                }
            });

            if (checkFlag) {
                if (selectedObj == current) {
                    if (current.getRightAnswer()) {
                        viewHolder.btn_answer.setBackgroundDrawable(getResources().getDrawable(R.drawable.transition_1));
                        TransitionDrawable transition6 = (TransitionDrawable) viewHolder.btn_answer.getBackground();
                        transition6.startTransition(durationMillis);
                        showCorrect();
                    } else {
                        viewHolder.btn_answer.setBackgroundDrawable(getResources().getDrawable(R.drawable.transition_red));
                        TransitionDrawable transition6 = (TransitionDrawable) viewHolder.btn_answer.getBackground();
                        transition6.startTransition(durationMillis);
                        showComment();
                    }

                } else if (current.getRightAnswer()) {
                    viewHolder.btn_answer.setBackgroundDrawable(getResources().getDrawable(R.drawable.transition_1));
                    TransitionDrawable transition6 = (TransitionDrawable) viewHolder.btn_answer.getBackground();
                    transition6.startTransition(durationMillis);
                }
            } else if (timeElapseFlag && current.getRightAnswer()) {
                viewHolder.btn_answer.setBackgroundDrawable(getResources().getDrawable(R.drawable.transition_1));
                TransitionDrawable transition6 = (TransitionDrawable) viewHolder.btn_answer.getBackground();
                transition6.startTransition(durationMillis);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            Button btn_answer;

            public MyViewHolder(View itemView) {
                super(itemView);
                btn_answer = (Button) itemView.findViewById(R.id.btn_answer);

            }
        }
    }

    void showCorrect() {
        c2.setTitle("");
        rel_progress.setVisibility(View.GONE);
        rel_text.setVisibility(View.VISIBLE);
        tv_welldone.startAnimation(animBounceZoom);
    }

    private void getOneqad() {
        try {
            dialog = new Dialog(OneQADActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.progress_layout);

            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            dialog.show();
            final OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).connectTimeout(20, TimeUnit.SECONDS).build();

            //Download Todays Questions
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Username", userId);
            jsonObject.put("Downloadtype", "Today_Question_Pitch");
            String jsonString = jsonObject.toString();
            RequestBody questionjsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
            adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
            PostApi api1 = adapter.create(PostApi.class);
            Call<ResponseBody> callquest = api1.getDownloadAllUSINGLOGIN(questionjsonData);
            callquest.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            data = data.substring(1, data.length() - 1).replace("\\", "");
                            if (data.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                dialog.dismiss();
                                AlertandMessages.showAlertlogin((Activity) context, "Check Your Internet Connection");
                            } else if (data.contains("No Data")) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, DealerBoardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Gson gs = new Gson();
                                final LoginGsonGetterSetter userques = gs.fromJson(data.toString().trim(), LoginGsonGetterSetter.class);
                                dialog.dismiss();

                                if (userques.getTodayQuestion().size() > 0 && userques.getTodayQuestion().get(0).getStatus().equals("N")) {
                                    for (int i = 0; i < userques.getTodayQuestion().size(); i++) {
                                        if (userques.getTodayQuestion().get(i).getRightAnswer().toString().equalsIgnoreCase("true")) {
                                            break;
                                        }
                                    }
                                    todayQuestion = userques.getTodayQuestion();
                                    rel_qad.setVisibility(View.VISIBLE);
                                    c2.setSubTitle("");
                                    countDownTimer = new CountDownTimer(60000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                                            c2.setTitle(millisUntilFinished / 1000 + "");
                                            c2.setProgress(((int) millisUntilFinished / 1000) * 5);
                                            //here you can have your logic to set text to edittext
                                        }

                                        public void onFinish() {
                                            c2.setTitle("Time Up");
                                            timeElapseFlag = true;
                                            c2.setVisibility(View.INVISIBLE);
                                            tv_time_Up.setVisibility(View.VISIBLE);
                                            fab.setVisibility(View.VISIBLE);
                                            valueAdapter.notifyDataSetChanged();
                                            //disableButton();
                                            //mTextField.setText("done!");
                                        }

                                    }.start();

                                    tv_question.setText(todayQuestion.get(0).getQuestion());
                                    valueAdapter = new ValueAdapter(context, todayQuestion);

                                    rec_quiz.setAdapter(valueAdapter);
                                    rec_quiz.setLayoutManager(new LinearLayoutManager(context));

                                } else {
                                    Intent intent = new Intent(context, DealerBoardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }


                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            AlertandMessages.showAlertlogin((Activity) context, CommonString.MESSAGE_NO_RESPONSE_SERVER + "(" + e.toString() + ")");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                        AlertandMessages.showAlertlogin(OneQADActivity.this, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")");
                    } else {
                        AlertandMessages.showAlertlogin(OneQADActivity.this, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")");
                    }
                }
            });

        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            AlertandMessages.showAlertlogin(OneQADActivity.this, CommonString.MESSAGE_SOCKETEXCEPTION + "(" + e.toString() + ")");
        }
    }

    class AnswerData {
        public String question_id, answer_id, username, visit_date, right_answer;

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getAnswer_id() {
            return answer_id;
        }

        public void setAnswer_id(String answer_id) {
            this.answer_id = answer_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getVisit_date() {
            return visit_date;
        }

        public void setVisit_date(String visit_date) {
            this.visit_date = visit_date;
        }

        public String getRight_answer() {
            return right_answer;
        }

        public void setRight_answer(String right_answer) {
            this.right_answer = right_answer;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Alert").setMessage("Do you want to Exit ?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.finishAffinity((Activity) context);
                startActivity(new Intent(context, LorealBaLoginActivty.class));
                overridePendingTransition(ba.cpm.com.lorealba.R.anim.activity_back_in, ba.cpm.com.lorealba.R.anim.activity_back_out);
                OneQADActivity.this.finish();
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
}
