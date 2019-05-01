package ba.cpm.com.lorealba.dailyentry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.gsonGetterSetter.InvoiceGetterSetter;
import ba.cpm.com.lorealba.gsonGetterSetter.StockGetterSetter;

public class OfferActivity extends AppCompatActivity {
    StockGetterSetter object = null;
    RecyclerView drawer_layout_recycle_store;
    ArrayList<StockGetterSetter> offerlist = new ArrayList<>();
    ImageView img_home;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        context = this;
        img_home = (ImageView) findViewById(R.id.img_home);
        drawer_layout_recycle_store = (RecyclerView) findViewById(R.id.drawer_layout_recycle_store);

        validateAdapterData();
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                OfferActivity.this.finish();
            }
        });
    }

    void validateAdapterData() {
        object = new StockGetterSetter();
        object.setSku("20% Off On The Maybelline COLO SSAL Kajal.");
        object.setValidity("05 April to 31St April 2019");
        offerlist.add(object);

        object = new StockGetterSetter();
        object.setSku("Buy One Maybelline White Superfresh and get Second At 50% Off.");
        object.setValidity("06 April to 31St April 2019");
        offerlist.add(object);

        drawer_layout_recycle_store.setAdapter(new ValueAdapter(context, offerlist));
        drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));

    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<StockGetterSetter> data;
        String report_tpe;

        public ValueAdapter(Context context, List<StockGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.report_tpe = report_tpe;
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.custom_offer, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder holder, final int position) {
            final StockGetterSetter current = data.get(position);
            holder.offer_sku.setText(current.getSku());
            holder.offer_sku.setId(position);

            holder.offer_validity.setText(current.getValidity());
            holder.offer_validity.setId(position);

            holder.view_above.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show_offer_image_dialog(context, position);
                }
            });


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

    private void show_offer_image_dialog(final Context context, int position) {
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
        if (position == 0) {
            img_offer_page.setImageDrawable(getResources().getDrawable(R.drawable.product_kajal));
        } else {
            img_offer_page.setImageDrawable(getResources().getDrawable(R.drawable.product_compact));
        }
        ImageView btn_ok = (ImageView) multiPurposeDialog.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiPurposeDialog.dismiss();
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
            setCancelable(false);
            setOnCancelListener(null);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
        }
    }


}
