package com.attlds.chinmay.ant;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ScanSearchActivity extends AppCompatActivity {
    private final String TAG = "ScanSearchActivity";
    private BluetoothManager btManager;
    private BluetoothAdapter mAdapter;
    private BluetoothLeScanner bScan;
    private ListView lv;
    private CustomAdapter lAdapter;
    private EditText edit;
    private boolean isScanning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_search);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        getWindow().setLayout((int)(width*0.8),(int)(height*0.5));
        lAdapter = new CustomAdapter(this);
        lv = (ListView) findViewById(R.id.search_list);
        lv.setAdapter(lAdapter);
        edit = (EditText)findViewById(R.id.editText);

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = btManager.getAdapter();
        if (mAdapter == null || !mAdapter.isEnabled()){
            mAdapter = btManager.getAdapter();
            mAdapter.enable();
        }

        bScan = mAdapter.getBluetoothLeScanner();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice dev = (BluetoothDevice)parent.getAdapter().getItem(position);
                String uuid = dev.getAddress();
                String editext = edit.getText().toString();
                String name;
                int user_id = 007;
                if(editext.equals("")||editext.equals("Name it!!!"))
                    name = dev.getName();
                else
                    name = editext;
                addtoDB(uuid,name,user_id);
            }
        });

        startSearch();


    }

    public void addtoDB(String uuid,String name,int user_id){
        DevicesDbHelper devdb = new DevicesDbHelper(this);
        if(devdb.insertDevices(uuid,name,user_id)!=-1)
            Toast.makeText(getApplicationContext(),"Device Added!!",Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Error inserting in DB");
        }
    }

    public void startSearch(){
        if(!isScanning) {
            bScan.startScan(mLeScanCallback);
            isScanning = true;
        }
    }

    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            ScanRecord record = result.getScanRecord();
            int pow = record.getTxPowerLevel();
            final String name = result.getDevice().getName();
            Log.i("Activity", name);
            if(!lAdapter.result.contains(result.getDevice())) {
                lAdapter.add(result.getDevice(),pow);
                lAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), name + " added", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(isScanning) {
            bScan.stopScan(mLeScanCallback);
            isScanning =false;
        }
    }
}
