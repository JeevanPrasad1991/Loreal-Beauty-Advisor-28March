package ba.cpm.com.lorealba.printer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hoin.btsdk.BluetoothService;


import ba.cpm.com.lorealba.R;
import ba.cpm.com.lorealba.constant.CommonString;

public class PrinterActivity extends AppCompatActivity {
    String store_name, store_address, visit_date, total_ammount;
    Button btnSearch;
    Button btnSend;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    String complet_print_data = "";
    private static final int REQUEST_CONNECT_DEVICE = 1;  //»ñÈ¡Éè±¸ÏûÏ¢
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        context = this;
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new ClickEvent());
        btnSend = (Button) findViewById(R.id.btnSend);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        visit_date = preferences.getString(CommonString.KEY_DATE, "");

        store_name = preferences.getString(CommonString.KEY_STORE_NAME, "");
        store_name = "A One Beauty Store";
        store_address = preferences.getString(CommonString.KEY_STORE_ADDRESS, "");
        store_address = "147,Kalkaji,New Delhi-29";

        btnSend.setOnClickListener(new ClickEvent());
        complet_print_data = getIntent().getStringExtra(CommonString.TAG_OBJECT);
        total_ammount = getIntent().getStringExtra(CommonString.Total_Amount);

        btnSend.setEnabled(false);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }

        mService = new BluetoothService(getApplicationContext(), mHandler);
        //À¶ÑÀ²»¿ÉÓÃÍË³ö³ÌÐò
        if (mService.isAvailable() == false) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            String msg = "";
            switch (v.getId()) {
                case R.id.btnSearch:
                    Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);      //ÔËÐÐÁíÍâÒ»¸öÀàµÄ»î¶¯
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    break;
                case R.id.btnSend:
                    //msg = edtContext.getText().toString();
                    // Bold format center:
                    msg = store_name + "\n"+store_address + "\n"+"Pink Slip\n";
                    writeWithFormat(msg.getBytes(), new Formatter().bold().height().get(), Formatter.centerAlign());
                    // Bold underlined format with right alignment:
                    String for_date="Date: " + visit_date + "\n";
                    writeWithFormat(for_date.getBytes(), new Formatter().small().get(), Formatter.leftAlign());
                    //mService.sendMessage(for_date, "GBK");
                    mService.sendMessage(complet_print_data + "\n", "GBK");
                    mService.sendMessage("Total Amount : " + total_ammount, "GBK");
//                    if (msg.length() > 0) {
//                        mService.sendMessage(msg, "GBK");
//                    }
                    break;
            }
        }
    }

    public void writeWithFormat(byte[] buffer, final byte[] pFormat, final byte[] pAlignment) {

        // Notify printer it should be printed with given alignment:
        mService.write(pAlignment);
        // Notify printer it should be printed in the given format://±¶¿í¡¢±¶¸ßÄ£Ê½
        mService.write(pFormat);
        // Write the actual data://±¶¿í¡¢±¶¸ßÄ£Ê½
        mService.write(buffer);           //±¶¿í¡¢±¶¸ßÄ£Ê½
        //mService.sendMessage("Congratulations!\n", "GBK");
    }

    /**
     * ´´½¨Ò»¸öHandlerÊµÀý£¬ÓÃÓÚ½ÓÊÕBluetoothServiceÀà·µ»Ø»ØÀ´µÄÏûÏ¢
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //ÒÑÁ¬½Ó
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            btnSend.setEnabled(true);

                            break;
                        case BluetoothService.STATE_CONNECTING:  //ÕýÔÚÁ¬½Ó
                            Log.d("À¶ÑÀµ÷ÊÔ", "ÕýÔÚÁ¬½Ó.....");
                            break;
                        case BluetoothService.STATE_LISTEN:     //¼àÌýÁ¬½ÓµÄµ½À´
                        case BluetoothService.STATE_NONE:
                            Log.d("À¶ÑÀµ÷ÊÔ", "µÈ´ýÁ¬½Ó.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //À¶ÑÀÒÑ¶Ï¿ªÁ¬½Ó
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    btnSend.setEnabled(false);

                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:      //ÇëÇó´ò¿ªÀ¶ÑÀ
                if (resultCode == Activity.RESULT_OK) {   //À¶ÑÀÒÑ¾­´ò¿ª
                    Toast.makeText(getApplicationContext(), "Bluetooth open successful", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_CONNECT_DEVICE:     //ÇëÇóÁ¬½ÓÄ³Ò»À¶ÑÀÉè±¸
                if (resultCode == Activity.RESULT_OK) {   //ÒÑµã»÷ËÑË÷ÁÐ±íÖÐµÄÄ³¸öÉè±¸Ïî
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);  //»ñÈ¡ÁÐ±íÏîÖÐÉè±¸µÄmacµØÖ·
                    con_dev = mService.getDevByMac(address);

                    mService.connect(con_dev);
                }
                break;
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
        }
    }

    public static class Formatter {
        private byte[] mFormat;

        public Formatter() {
            // Default:
            mFormat = new byte[]{27, 33, 0};
        }

        /**
         * Method to get the Build result
         *
         * @return the format
         */
        public byte[] get() {
            return mFormat;
        }

        public Formatter bold() {
            // Apply bold:
            mFormat[2] = ((byte) (0x8 | mFormat[2]));
            return this;
        }

        public Formatter small() {
            mFormat[2] = ((byte) (0x1 | mFormat[2]));
            return this;
        }

        public Formatter height() {
            mFormat[2] = ((byte) (0x10 | mFormat[2]));
            return this;
        }

        public Formatter width() {
            mFormat[2] = ((byte) (0x20 | mFormat[2]));
            return this;
        }

        public Formatter underlined() {
            mFormat[2] = ((byte) (0x80 | mFormat[2]));
            return this;
        }

        public static byte[] rightAlign() {
            return new byte[]{0x1B, 'a', 0x02};
        }

        public static byte[] leftAlign() {
            return new byte[]{0x1B, 'a', 0x00};
        }

        public static byte[] centerAlign() {
            return new byte[]{0x1B, 'a', 0x01};
        }
    }
}
