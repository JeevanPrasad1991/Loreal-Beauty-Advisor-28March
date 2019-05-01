package ba.cpm.com.lorealba.dailyentry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_details_attendance, btn_details_sales, btn_details_incentive;
    Context context;
    ImageView img_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        context = this;
        iduserinterface();
    }

    private void iduserinterface() {
        btn_details_attendance = (Button) findViewById(R.id.btn_details_attendance);
        btn_details_sales = (Button) findViewById(R.id.btn_details_sales);
        btn_details_incentive = (Button) findViewById(R.id.btn_details_incentive);

        img_home=(ImageView)findViewById(R.id.img_home);

        btn_details_attendance.setOnClickListener(this);
        btn_details_sales.setOnClickListener(this);
        btn_details_incentive.setOnClickListener(this);
        img_home.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_details_sales:
                startActivity(new Intent(context, SalesReportView.class).putExtra(CommonString.KEY_REPORT_TYPE,"2"));
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                break;
            case R.id.btn_details_attendance:
                startActivity(new Intent(context, SalesReportView.class).putExtra(CommonString.KEY_REPORT_TYPE,"1"));
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                break;

            case R.id.btn_details_incentive:
                startActivity(new Intent(context, SalesReportView.class).putExtra(CommonString.KEY_REPORT_TYPE,"3"));
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                break;

            case R.id.img_home:
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                this.finish();
                break;
        }

    }


    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        this.finish();
    }

}
