package ba.cpm.com.lorealba.dailyentry;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonFunctions;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.delegates.NavMenuItemGetterSetter;

public class MyLibraryActivity extends AppCompatActivity {
    RecyclerView recycl_knowledge;
    private WebView webView;
    String type, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library);
        ImageView img_home = findViewById(R.id.img_home);
        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);
        ImageView img_knwledge = (ImageView) findViewById(R.id.img_knwledge);
       // recycl_knowledge=(RecyclerView)findViewById(R.id.recycl_knowledge);

        type = getIntent().getStringExtra(CommonString.KEY_STOCK_TYPE);

        if (type.equals("1")) {
            textv_sample.setText("Daily Sales");
            url = CommonString.KEY_MY_LIBRARY_URL;
            img_knwledge.setImageResource(R.drawable.daily_sale36);
        } else {
            textv_sample.setText("Knowledge");
            url = CommonString.KEY_MY_KNOWLEDGE_URL;
            img_knwledge.setImageResource(R.drawable.knowledge36);
        }

        webView = (WebView) findViewById(R.id.webview);
        if (CommonFunctions.CheckNetAvailability(this)) {
            //load notice board url
            webView.setWebViewClient(new MyWebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            if (url != null && !url.equals("")) {
                webView.loadUrl(url);
            }
        }

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                MyLibraryActivity.this.finish();
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.clearCache(true);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        this.finish();
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<NavMenuItemGetterSetter> data = Collections.emptyList();
        String report_tpe;

        public ValueAdapter(Context context, List<NavMenuItemGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.report_tpe = report_tpe;
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.custom_knowledge, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder holder, final int position) {
            final NavMenuItemGetterSetter current = data.get(position);

        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView offer_sku, offer_validity, view_above;

            public MyViewHolder(View itemView) {
                super(itemView);

                view_above = (TextView) itemView.findViewById(R.id.view_above);
                offer_sku = (TextView) itemView.findViewById(R.id.offer_sku);
                offer_validity = (TextView) itemView.findViewById(R.id.offer_validity);
            }
        }
    }

}
