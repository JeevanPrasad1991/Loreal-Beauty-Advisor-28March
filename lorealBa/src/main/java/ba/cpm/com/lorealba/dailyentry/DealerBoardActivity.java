package ba.cpm.com.lorealba.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.LorealBaLoginActivty;
import ba.cpm.com.lorealba.MainMenuActivity;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.delegates.NavMenuItemGetterSetter;
import ba.cpm.com.lorealba.download.DownloadActivity;

public class DealerBoardActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recycle_recce, stock_recycle, recycler_self;
    ValueAdapter adapter, stock_adapter, posm_adapter;
    ImageView btn_offerat_mystore,take_backup;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dealer_board);
        context = this;
        recycle_recce = (RecyclerView) findViewById(R.id.recycle_recce);
        stock_recycle = (RecyclerView) findViewById(R.id.stock_recycle);
        recycler_self = (RecyclerView) findViewById(R.id.recycler_self);

        btn_offerat_mystore = (ImageView) findViewById(R.id.btn_offerat_mystore);
        take_backup = (ImageView) findViewById(R.id.take_backup);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_backup:
                take_backup();
                break;
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
                        startActivity(new Intent(context, DownloadActivity.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.attendance) {
                        startActivity(new Intent(context, AttendanceActivity.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.store_work) {
                        startActivity(new Intent(context, StoreListActivity.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.reports) {
                        startActivity(new Intent(context, ReportsActivity.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.daily_sale) {
                        startActivity(new Intent(context, MyLibraryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.knowledge) {
                        startActivity(new Intent(context, MyLibraryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.daily_stock) {
                        startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.damaged_stock) {
                        startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.inward_stock) {
                        startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "3"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.customer_wise_sales) {
                        startActivity(new Intent(context, SaleTrackingActivity.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.posm_tracking) {

                        startActivity(new Intent(context, PosmTracking.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.promotion_tracking) {
                        startActivity(new Intent(context, PosmTracking.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.sample_stock) {

                        startActivity(new Intent(context, SampleTesterStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.tester_stock) {
                        startActivity(new Intent(context, SampleTesterStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else if (current.getIconImg() == R.drawable.notifications) {
                        startActivity(new Intent(context, NotificationsActivity.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
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
        int attendance = 0, knowledge, performance, reports, notification, data_sink;
        attendance = R.drawable.attendance;
        notification = R.drawable.notifications;
        knowledge = R.drawable.knowledge;

        performance = R.drawable.performance;
        reports = R.drawable.reports;
        data_sink = R.drawable.data_sink;

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
        damaged_stock = R.drawable.damaged_stock;
        inword_stock = R.drawable.inward_stock;

        sample_stock = R.drawable.sample_stock;
        tester_stock = R.drawable.tester_stock;


        int img[] = {daily_stock, damaged_stock, inword_stock, sample_stock, tester_stock};
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Alert").setMessage("Do you want to Exit ?");
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

    private void take_backup(){
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
