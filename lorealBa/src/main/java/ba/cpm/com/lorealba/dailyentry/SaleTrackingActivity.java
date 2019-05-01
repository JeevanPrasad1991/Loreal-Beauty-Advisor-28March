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
import android.widget.TextView;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;

public class SaleTrackingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btn_saletra_add_performa;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_tracking);
        context = this;
        iduserinterface();
    }

    private void iduserinterface() {
        ImageView img_home = (ImageView) findViewById(R.id.img_home);
        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);

        btn_saletra_add_performa = (ImageView) findViewById(R.id.btn_saletra_add_performa);

        ImageView img_daily_sales = (ImageView) findViewById(R.id.img_daily_sales);
        ImageView bottom_posm = (ImageView) findViewById(R.id.bottom_posm);
        ImageView img_promotion = (ImageView) findViewById(R.id.img_promotion);

        btn_saletra_add_performa.setOnClickListener(this);

        img_daily_sales.setOnClickListener(this);
        bottom_posm.setOnClickListener(this);
        img_promotion.setOnClickListener(this);
        img_home.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_saletra_add_performa:
                startActivity(new Intent(context, ProformaInvoiceActivity.class));
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                break;
            case R.id.img_home:
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                this.finish();
                break;


            case R.id.img_daily_sales:
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                this.finish();
                break;

            case R.id.bottom_posm:
                startActivity(new Intent(context, PosmTracking.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                this.finish();
                break;

            case R.id.img_promotion:
                startActivity(new Intent(context, PosmTracking.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
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
