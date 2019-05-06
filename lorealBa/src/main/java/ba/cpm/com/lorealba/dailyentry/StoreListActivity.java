package ba.cpm.com.lorealba.dailyentry;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ba.cpm.com.lorealba.Database.Lorealba_Database;
import ba.cpm.com.lorealba.retrofit.PostApi;
import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.AlertandMessages;
import ba.cpm.com.lorealba.constant.CommonString;
import ba.cpm.com.lorealba.gpsenable.LocationEnableCommon;
import ba.cpm.com.lorealba.gsonGetterSetter.JourneyPlan;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StoreListActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Context context;
    private String userId;
    private ArrayList<JourneyPlan> storelist = new ArrayList<>();
    private String date;
    private Lorealba_Database database;
    private FloatingActionButton fab;
    private double lat = 0.0;
    private double lon = 0.0;
    SharedPreferences preferences;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private SharedPreferences.Editor editor = null;
    private LocationRequest mLocationRequest;
    LocationEnableCommon locationEnableCommon;
    private static final String TAG = StoreimageActivity.class.getSimpleName();
    LinearLayout rl_storelist;
    ImageView img_direction;
    int downloadIndex;
    ImageView img_home, img_sample_stock, img_tester_stock, img_damaged_stock, img_invert_stock, img_daily_stock, img_check_in;
    LinearLayout rl_checkin;
    //for new store list
    TextView store_name, store_address, contact_person, store_contact_no, channel_text;
    Button btn_kyc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelistfablayout);
        context = this;
        declaration();
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }


        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
    }


    protected void onResume() {
        super.onResume();
        database.open();
        storelist = database.getStoreData(date);
        if (storelist.size() > 0) {
            JourneyPlan object = storelist.get(0);
            store_name.setText(object.getStoreName() + " Store Code : " + object.getStoreCode());
            store_address.setText("Address - " + object.getAddress() + "," + object.getCityName() + " " + object.getPincode());
            contact_person.setText("Store Contact Person : " + object.getContactPerson());
            store_contact_no.setText("Store Contact Number : " + object.getContactNo());
            channel_text.setText("Distributor Name : " + object.getDistributorName());
            if (database.getSpecificCoverageData(date, object.getStoreId().toString()).size() > 0) {
                if (validation_for_checkout(object)){
                    img_check_in.setImageResource(R.drawable.check_out);
                }else {
                    img_check_in.setImageResource(R.drawable.checked_in);
                }
            } else {
                img_check_in.setImageResource(R.drawable.check_in_btn);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_check_in:
                JourneyPlan object = storelist.get(0);
                editor = preferences.edit();
                editor.putString(CommonString.KEY_STOREVISITED_STATUS, "Yes");
                editor.putString(CommonString.KEY_STORE_NAME, object.getStoreName());
                editor.putString(CommonString.KEY_STORE_ADDRESS, object.getAddress() + "," + object.getCityName() + " " + object.getPincode());
                editor.putString(CommonString.KEY_STORE_ID, object.getStoreId().toString());
                editor.commit();
                if (database.getSpecificCoverageData(date, object.getStoreId().toString()).size() == 0) {
                    Intent in = new Intent(StoreListActivity.this, StoreimageActivity.class);
                    startActivity(in);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                } else {
                    Intent in = new Intent(StoreListActivity.this, DealerBoardActivity.class);
                    startActivity(in);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    StoreListActivity.this.finish();
                }

                break;

            case R.id.img_home:
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                break;

            case R.id.img_daily_stock:
                startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                StoreListActivity.this.finish();
                break;

            case R.id.img_damaged_stock:
                startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                StoreListActivity.this.finish();
                break;

            case R.id.img_invert_stock:
                startActivity(new Intent(context, StockEntryActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "3"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                StoreListActivity.this.finish();
                break;

            case R.id.img_tester_stock:
                startActivity(new Intent(context, SampleTesterStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "2"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                StoreListActivity.this.finish();
                break;

            case R.id.img_sample_stock:
                startActivity(new Intent(context, SampleTesterStockActivity.class).putExtra(CommonString.KEY_STOCK_TYPE, "1"));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                StoreListActivity.this.finish();
                break;
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    private void declaration() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        rl_storelist = findViewById(R.id.rl_storelist);
        img_direction = findViewById(R.id.img_direction);
        img_sample_stock = (ImageView) findViewById(R.id.img_sample_stock);
        img_tester_stock = (ImageView) findViewById(R.id.img_tester_stock);
        img_daily_stock = (ImageView) findViewById(R.id.img_daily_stock);
        img_damaged_stock = (ImageView) findViewById(R.id.img_damaged_stock);
        img_invert_stock = (ImageView) findViewById(R.id.img_invert_stock);
        img_check_in = (ImageView) findViewById(R.id.img_check_in);
        TextView textv_sample = (TextView) findViewById(R.id.textv_sample);
        rl_checkin = (LinearLayout) findViewById(R.id.rl_checkin);

//////////////////////for new checnges
        store_name = (TextView) findViewById(R.id.store_name);
        store_address = (TextView) findViewById(R.id.store_address);
        contact_person = (TextView) findViewById(R.id.contact_person);
        store_contact_no = (TextView) findViewById(R.id.store_contact_no);
        channel_text = (TextView) findViewById(R.id.channel_text);

        img_sample_stock.setOnClickListener(this);
        img_tester_stock.setOnClickListener(this);
        img_daily_stock.setOnClickListener(this);
        img_damaged_stock.setOnClickListener(this);
        img_invert_stock.setOnClickListener(this);
        img_check_in.setOnClickListener(this);
        fab.setOnClickListener(this);

        locationEnableCommon = new LocationEnableCommon();
        locationEnableCommon.checkgpsEnableDevice(context);
        database = new Lorealba_Database(context);
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }

            return false;
        }
        return true;
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        int UPDATE_INTERVAL = 500;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        int FATEST_INTERVAL = 100;
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int DISPLACEMENT = 5;
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();

            }
        }


        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    private boolean validation_for_checkout(JourneyPlan current) {
        boolean customer_wise_sale, promotion, daily_stock=true, inward_stock=true, tester_stock=true, pwpWith_gwp_stock=true, complete_data_flag;
        if (database.getcategory_fromproduct().size() > 0) {
            if (database.getsalestrackingList(current.getStoreId().toString(), current.getVisitDate()).size() > 0) {
                customer_wise_sale = true;
            } else {
                customer_wise_sale = false;
            }
        } else {
            customer_wise_sale = true;
        }

        if (database.getpromotion_master_data().size() > 0) {
            if (database.getpromotion_inserted_data(current.getStoreId().toString(), current.getVisitDate()).size() > 0) {
                promotion = true;
            } else {
                promotion = false;
            }
        } else {
            promotion = true;
        }

        if (customer_wise_sale && promotion && daily_stock && inward_stock && tester_stock && pwpWith_gwp_stock){
            complete_data_flag=true;
        }else {
            complete_data_flag=false;
        }

        return complete_data_flag;
    }


}

