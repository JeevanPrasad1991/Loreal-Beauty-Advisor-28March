package ba.cpm.com.lorealba.dailyentry;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gsonGetterSetter.ReportsGetterSetter;

public class SalesReportView extends AppCompatActivity {
    LinearLayout sales_report_rl, incetive_report_rl, attendence_report_rl;
    ArrayList<ReportsGetterSetter> compList = new ArrayList<>();
    RecyclerView drawer_layout_recycle_store;
    ReportsGetterSetter object = null;
    ImageView img_home;
    String report_type;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_view);
        context = this;
        validateui_design();
    }

    void validateui_design() {
        drawer_layout_recycle_store = findViewById(R.id.drawer_layout_recycle_store);
        sales_report_rl = (LinearLayout) findViewById(R.id.sales_report_rl);
        incetive_report_rl = (LinearLayout) findViewById(R.id.incetive_report_rl);
        attendence_report_rl = (LinearLayout) findViewById(R.id.attendence_report_rl);
        img_home = (ImageView) findViewById(R.id.img_home);
        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);

        report_type = getIntent().getStringExtra(CommonString.KEY_REPORT_TYPE);
        if (report_type.equals("1")) {
            textv_sample.setText("Dashboard - Attendance");
            attendence_report_rl.setVisibility(View.VISIBLE);
            sales_report_rl.setVisibility(View.GONE);
            incetive_report_rl.setVisibility(View.GONE);

        } else if (report_type.equals("2")) {
            textv_sample.setText("Dashboard - Sales");
            attendence_report_rl.setVisibility(View.GONE);
            sales_report_rl.setVisibility(View.VISIBLE);
            incetive_report_rl.setVisibility(View.GONE);

        } else if (report_type.equals("3")) {
            textv_sample.setText("Dashboard - Incentive");
            attendence_report_rl.setVisibility(View.GONE);
            sales_report_rl.setVisibility(View.GONE);
            incetive_report_rl.setVisibility(View.VISIBLE);

        }
        validateadapter();
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                SalesReportView.this.finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        this.finish();
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<ReportsGetterSetter> data;
        String report_tpe;

        public ValueAdapter(Context context, List<ReportsGetterSetter> data, String report_tpe) {
            inflator = LayoutInflater.from(context);
            this.report_tpe = report_tpe;
            this.data = data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.report_row_xml, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder holder, final int position) {
            final ReportsGetterSetter current = data.get(position);
            if (report_tpe.equals("1")) {
                holder.rl_child_sales.setVisibility(View.GONE);
                holder.rl_child_sales.setId(position);

                holder.rl_child_incentive.setVisibility(View.GONE);
                holder.rl_child_incentive.setId(position);

                holder.rl_child_attence.setVisibility(View.VISIBLE);
                holder.rl_child_attence.setId(position);
                holder.attend_date.setText(current.getReport_date());
                holder.attend_date.setId(position);
                holder.selected_attend.setText(current.getSelected_attendance());
                holder.selected_attend.setId(position);
                holder.intime_out_time.setText(current.getIntime_outtime());
                holder.intime_out_time.setId(position);

            } else if (report_tpe.equals("2")) {
                holder.rl_child_sales.setVisibility(View.VISIBLE);
                holder.rl_child_sales.setId(position);

                holder.rl_child_incentive.setVisibility(View.GONE);
                holder.rl_child_incentive.setId(position);

                holder.rl_child_attence.setVisibility(View.GONE);
                holder.rl_child_attence.setId(position);

                holder.report_month.setText(current.getReport_date());
                holder.report_month.setId(position);

                holder.sale_value.setText(current.getSale_value());
                holder.sale_value.setId(position);
            } else {
                holder.rl_child_sales.setVisibility(View.GONE);
                holder.rl_child_sales.setId(position);

                holder.rl_child_incentive.setVisibility(View.VISIBLE);
                holder.rl_child_incentive.setId(position);

                holder.rl_child_attence.setVisibility(View.GONE);
                holder.rl_child_attence.setId(position);

                holder.incentive_volume.setText(current.getIncentive_value_name());
                holder.incentive_volume.setId(position);

                holder.text_incentive.setText(current.getIncentive());
                holder.text_incentive.setId(position);
            }

        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout rl_child_incentive, rl_child_sales, rl_child_attence;
            TextView report_month, sale_value, incentive_volume, text_incentive, attend_date, selected_attend, intime_out_time;
            CardView report_cardview;

            public MyViewHolder(View itemView) {
                super(itemView);
                rl_child_incentive = (LinearLayout) itemView.findViewById(R.id.rl_child_incentive);
                rl_child_sales = (LinearLayout) itemView.findViewById(R.id.rl_child_sales);
                rl_child_attence = (LinearLayout) itemView.findViewById(R.id.rl_child_attence);

                report_month = (TextView) itemView.findViewById(R.id.report_month);
                sale_value = (TextView) itemView.findViewById(R.id.sale_value);

                text_incentive = (TextView) itemView.findViewById(R.id.text_incentive);
                incentive_volume = (TextView) itemView.findViewById(R.id.incentive_volume);
                attend_date = (TextView) itemView.findViewById(R.id.attend_date);
                selected_attend = (TextView) itemView.findViewById(R.id.selected_attend);
                intime_out_time = (TextView) itemView.findViewById(R.id.intime_out_time);


                report_cardview = (CardView) itemView.findViewById(R.id.report_cardview);
            }
        }
    }

    private void validateadapter() {
        if (report_type.equals("1")) {
            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setReport_date("01 Feb");
            object.setSelected_attendance("Present");
            object.setIntime_outtime("10:20 19:35");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.storeimage);
            object.setReport_date("02 Feb");
            object.setSelected_attendance("Present");
            object.setIntime_outtime("10:30 19:32");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setReport_date("03 Feb");
            object.setSelected_attendance("Present");
            object.setIntime_outtime("10:10 19:36");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.storeimage);
            object.setReport_date("04 Feb");
            object.setSelected_attendance("Present");
            object.setIntime_outtime("10:20 19:35");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setReport_date("05 Feb");
            object.setSelected_attendance("Present");
            object.setIntime_outtime("10:00 19:35");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.storeimage);
            object.setReport_date("06 Feb");
            object.setSelected_attendance("Present");
            object.setIntime_outtime("10:05 19:35");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setReport_date("07 Feb");
            object.setSelected_attendance("Present");
            object.setIntime_outtime("10:20 19:35");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.storeimage);
            object.setReport_date("08 Feb");
            object.setSelected_attendance("Weekoff");
            object.setIntime_outtime("- -");
            compList.add(object);

        } else if (report_type.equals("2")) {
            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setReport_date("01 Feb");
            object.setSale_value("230.00");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.storeimage);
            object.setReport_date("02 Feb");
            object.setSale_value("1450.00");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setReport_date("03 Feb");
            object.setSale_value("2370.00");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.storeimage);
            object.setReport_date("04 Feb");
            object.setSale_value("630.00");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setReport_date("05 Feb");
            object.setSale_value("2450.00");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.storeimage);
            object.setReport_date("06 Feb");
            object.setSale_value("3330.00");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setReport_date("07 Feb");
            object.setSale_value("0.00");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.storeimage);
            object.setReport_date("08 Feb");
            object.setSale_value("5000.00");
            compList.add(object);

        } else if (report_type.equals("3")) {
            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setIncentive_value_name("Value Target Achv");
            object.setIncentive("1200.00");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.storeimage);
            object.setIncentive_value_name("Volume Target Achv");
            object.setIncentive("500.00");
            compList.add(object);

            object = new ReportsGetterSetter();
            object.setColor_code(R.color.white);
            object.setIncentive_value_name("Focus Product Sales");
            object.setIncentive("700.00");
            compList.add(object);
        }

        drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, compList, report_type));
        drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));

    }

}
