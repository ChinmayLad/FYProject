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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.List;
import java.util.TimerTask;

public class ScanDevice extends AppCompatActivity {
    private final String TAG = "ScanDeviceActivity";
    private BluetoothManager btManager;
    private BluetoothAdapter mAdapter;
    private Context mContext;
    private Thread rs;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothLeScanner bScan;
    private String mUuid;
    private BluetoothDevice mDevice;
    private ArcProgress arcProgress;
    private Handler timeHandler;
    private boolean isScanning = false;
    private boolean isConnected = false;
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
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device);
        tv = (TextView) findViewById(R.id.dev_name);
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        arcProgress.setBottomText("00 dBm");
        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = btManager.getAdapter();
        if (mAdapter == null || !mAdapter.isEnabled()){
            mAdapter = btManager.getAdapter();
            mAdapter.enable();
        }
        timeHandler = new Handler();
        mUuid = getIntent().getStringExtra("UUID");
        bScan = mAdapter.getBluetoothLeScanner();
        if(!isScanning) {
            bScan.startScan(mLeScanCallback);
            isScanning = true;
        }
        timeHandler.postDelayed(task, 300000);
    }

    Thread task = new Thread(new Runnable() {
        @Override
        public void run() {
            bScan.stopScan(mLeScanCallback);
            Toast.makeText(getApplicationContext(),"No Device Found!!",Toast.LENGTH_SHORT).show();
        }
    });




    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if(result.getDevice().getAddress().equals(mUuid)){
                mDevice = result.getDevice();
                tv.setText(mDevice.getName());
                connectToDevice();
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            if (results.contains(mUuid)){
                mDevice = results.get(results.indexOf(mUuid)).getDevice();
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    public void connectToDevice(){
        if(isScanning) {
            bScan.stopScan(mLeScanCallback);
            isScanning = false;
        }

        mBluetoothGatt = mDevice.connectGatt(getApplicationContext(), false, mGattCallback);
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int rssi= msg.arg1;

            double distance = (-40 - rssi)/25;
            distance = Math.pow(10d,distance);
            if(rssi < -35||rssi > -120) {
                int prog = ((rssi+120) * 100) / 85;
                arcProgress.setProgress(prog);
            }
            else if(rssi>130){
                arcProgress.setProgress(0);
            }
            arcProgress.setBottomText(String.format("%d",rssi)+" dBm.");
            return false;
        }
    });


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

    @Override
    protected void onStop() {
        super.onStop();
        this.disconnect();
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
