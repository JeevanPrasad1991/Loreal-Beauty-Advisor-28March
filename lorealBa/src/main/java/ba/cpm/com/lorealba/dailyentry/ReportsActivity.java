package ba.cpm.com.lorealba.dailyentry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.DashboardDataGetter;
import ba.cpm.com.lorealba.gsonGetterSetter.ReportsGetterSetter;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {
    //Button btn_details_attendance, btn_details_sales, btn_details_incentive;
    Context context;
    ImageView img_home;
    Lorealba_Database db;
    RecyclerView report_recycl;
    ArrayList<DashboardDataGetter> dashboardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        context = this;
        db = new Lorealba_Database(context);
        db.open();
        iduserinterface();
    }

    private void iduserinterface() {
//        btn_details_attendance = (Button) findViewById(R.id.btn_details_attendance);
//        btn_details_sales = (Button) findViewById(R.id.btn_details_sales);
//        btn_details_incentive = (Button) findViewById(R.id.btn_details_incentive);

        img_home = (ImageView) findViewById(R.id.img_home);
        report_recycl = (RecyclerView) findViewById(R.id.report_recycl);
        //  btn_details_attendance.setOnClickListener(this);
        //btn_details_sales.setOnClickListener(this);
        // btn_details_incentive.setOnClickListener(this);
        img_home.setOnClickListener(this);
        dashboardList = db.getdashboard_data();
        if (dashboardList.size() > 0) {
            report_recycl.setAdapter(new ValueAdapter(context, dashboardList));
            report_recycl.setLayoutManager(new GridLayoutManager(context, 2));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_details_sales:
                startActivity(new Intent(context, SalesReportView.class).putExtra(CommonString.KEY_REPORT_TYPE, "2"));
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                break;

            case R.id.btn_details_attendance:
                startActivity(new Intent(context, SalesReportView.class).putExtra(CommonString.KEY_REPORT_TYPE, "1"));
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                break;

            case R.id.btn_details_incentive:
                startActivity(new Intent(context, SalesReportView.class).putExtra(CommonString.KEY_REPORT_TYPE, "3"));
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

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<DashboardDataGetter> data;
        public ValueAdapter(Context context, List<DashboardDataGetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.report_lavel_wise, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final DashboardDataGetter current = data.get(position);

            holder.report_lavelname.setText(current.getLabel());
            holder.report_lavelname.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
            holder.report_lavelname.setId(position);

            holder.report_lavel_value.setText(current.getValue().toString());
            holder.report_lavel_value.setId(position);
        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView report_lavelname, report_lavel_value;

            public MyViewHolder(View itemView) {
                super(itemView);
                report_lavelname = (TextView) itemView.findViewById(R.id.report_lavelname);
                report_lavel_value = (TextView) itemView.findViewById(R.id.report_lavelvalue);
            }
        }
    }

}
