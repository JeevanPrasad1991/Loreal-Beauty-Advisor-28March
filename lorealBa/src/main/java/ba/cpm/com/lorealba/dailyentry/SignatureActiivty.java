package ba.cpm.com.lorealba.dailyentry;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;

public class SignatureActiivty extends AppCompatActivity {
    ImageView loreal_paris,maybelline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_actiivty);
        Toolbar toolbar = findViewById(R.id.toolbar);
        loreal_paris=(ImageView)findViewById(R.id.loreal_paris);
        maybelline=(ImageView)findViewById(R.id.maybelline);




        loreal_paris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(SignatureActiivty.this, StockDailyActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1").putExtra(CommonString.KEY_SIGNETURE_ID,"1"));
                startActivity(new Intent(SignatureActiivty.this, StockDailyActivity.class).putExtra(CommonString.KEY_SIGNETURE_ID,"1"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

            }
        });
        maybelline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignatureActiivty.this, StockDailyActivity.class).putExtra(CommonString.KEY_SIGNETURE_ID,"2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        });
    }

}
