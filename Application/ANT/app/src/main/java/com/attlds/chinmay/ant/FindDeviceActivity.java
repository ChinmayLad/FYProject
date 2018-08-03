package com.attlds.chinmay.ant;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FindDeviceActivity extends AppCompatActivity {
    private static final String TAG = "Activity";
    private final int REQUEST_ENABLE_BT = 1;
    private Button btStart;
    private Button btStop;
    private ListView lt;
    private TextView t;
    private ProgressBar progress;
    private CustomAdapter lAdapter;
    private BluetoothAdapter mAdapter;
    private BluetoothLeScanner bScan;
    private BluetoothManager btManager;
    private BluetoothDevice currDevice;
    private Thread rs;
    private boolean isScanning;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public boolean runThread = true;
    int txPower;

    public boolean isConnected =false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lt = (ListView)findViewById(R.id.rssi_List);
        progress = (ProgressBar)findViewById(R.id.signalLevel);
        progress.setProgress(0);
        t = (TextView)findViewById(R.id.tView);
        t.setText("--- dBm");
        btStart = (Button)findViewById(R.id.start_bt);
        btStop = (Button)findViewById(R.id.btStop);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not supported!!!", Toast.LENGTH_SHORT).show();
            finish();
        }
        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = btManager.getAdapter();
        if (mAdapter == null || !mAdapter.isEnabled()){
            mAdapter = btManager.getAdapter();
            mAdapter.enable();
            recreate();
        }

        bScan = mAdapter.getBluetoothLeScanner();
        lAdapter = new CustomAdapter(this);
        lt.setAdapter(lAdapter);

        lt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isScanning) {
                    bScan.stopScan(mLeScanCallback);
                    isScanning = false;
                }
                currDevice = (BluetoothDevice) parent.getAdapter().getItem(position);
                txPower = lAdapter.getPower(position);
                mBluetoothGatt = currDevice.connectGatt(getApplicationContext(), false, mGattCallback);

            }

        });

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isScanning) {
                    bScan.startScan(mLeScanCallback);
                    isScanning = true;
                    Toast.makeText(getApplicationContext(), "SCAN Started", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScanning) {
                    bScan.stopScan(mLeScanCallback);
                    isScanning = false;
                    Toast.makeText(getApplicationContext(), "SCAN Stopped", Toast.LENGTH_SHORT).show();
                }
                if(isConnected){
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt=null;
                    isConnected = false;
                }
            }
        });
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int rssi= msg.arg1;
            if(txPower ==0)
                txPower = -40;
            double distance = ((double)txPower - rssi)/25;
            distance = Math.pow(10d,distance);
            Log.d(TAG, "txPower" + String.valueOf(txPower));
            if(rssi < -35||rssi > -120) {
                int prog = ((rssi+120) * 100) / 85;
                progress.setProgress(prog);
            }
            else if(rssi>130){
                progress.setProgress(0);
            }
            t.setText(String.format("%.1f m\n %d",distance,rssi));
            return false;
        }
    });

    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            ScanRecord record = result.getScanRecord();
            int pow = record.getTxPowerLevel();
            final String name = result.getDevice().getName();
            Log.i("Activity", name);
            if(!lAdapter.equals(result.getDevice())) {
                lAdapter.add(result.getDevice(),pow);
                lAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), name + " added", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Thread rsi = new Thread(new Runnable() {
        @Override
        public void run() {
            int count = 0;
            boolean rssiStatus = true;
            while (mConnectionState == STATE_CONNECTED && runThread) {
                try {
                    rssiStatus = mBluetoothGatt.readRemoteRssi();
                    Thread.sleep(1000);
                    Log.d("Count", String.format("%d", count));
                } catch (Exception e) {
                    Log.i(TAG, e.toString());
                    runThread = false;
                }
                count++;
            }
        }
    });

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                isConnected = true;
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                rs = new Thread(rsi);
                runThread = true;
                rs.start();
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                isConnected = false;
                Log.i(TAG, "Disconnected from GATT server.");
                runThread = false;
                disconnect();
                broadcastUpdate(intentAction);
            }

        }
        public void onReadRemoteRssi(BluetoothGatt gatt,int rssi,int status){
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Message msg = handler.obtainMessage();
                msg.arg1 = rssi;
                handler.sendMessage(msg);
                Log.d(TAG, "BluetoothGatt ReadRssi:" + rssi + " dBm");
            }
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    public void onPause() {
        super.onPause();
        mConnectionState = STATE_DISCONNECTED;
        this.disconnect();
    }

    public void onDestroy(){
        super.onDestroy();
        this.disconnect();
    }

    public void closeGatt(){
        if(mBluetoothGatt!=null){
            mBluetoothGatt.close();
            mBluetoothGatt =null;
        }
    }
    public void stopThread(){
        if(rs != null){
            rs.interrupt();
            rs=null;
        }
    }
    public void disconnect(){
        mConnectionState = STATE_DISCONNECTED;
        this.closeGatt();
        this.stopThread();
    }


}
