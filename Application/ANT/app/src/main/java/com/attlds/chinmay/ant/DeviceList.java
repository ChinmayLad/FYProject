package com.attlds.chinmay.ant;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeviceList extends AppCompatActivity {

    DevicesDbHelper dbhelper;
    private int flag = 0;
    private ListView lv;
    private DbAdapter lAdapter;
    private Activity activity;
    private final int START_ADD_DEVICE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbhelper = new DevicesDbHelper(this);
        lAdapter = new DbAdapter(this);
        getDevices();
        lv = (ListView)findViewById(R.id.dev_list);
        registerForContextMenu(lv);
        lv.setAdapter(lAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchDevice((String) parent.getAdapter().getItem(position));
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.dev_list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_list, menu);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();
        switch (id){
            case R.id.context_edit:{
                Toast.makeText(getApplicationContext(),"Edit Selected!!",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.context_delete:{
                deleteDevice(info.position);
                break;
            }
            case R.id.context_tag:{
                // temporary solution
                //if(item.getTitle().equals(getResources().getString(R.string.tag_string))) {
                    tagDevice(info.position);
                /*    flag = 1;
                }
                else if(item.getTitle().equals(getResources().getString(R.string.untag_string))) {
                    unTagDevice(info.position);
                    flag = 0;
                }*/
                break;
            }
            case R.id.context_untag:{
                unTagDevice(info.position);
                break;
            }
        }

        return super.onContextItemSelected(item);
    }

    public void getDevices(){
        ArrayList<ArrayList<String>> a = dbhelper.getAllDevices();
        if(a.size()!=0) {
            lAdapter.setList(dbhelper.getAllDevices());
            lAdapter.notifyDataSetChanged();
        }
    }

    private void tagDevice(int position){
        dbhelper.insertTagDevices((String)lAdapter.getItem(position));
    }

    private void unTagDevice(int position){
        dbhelper.removeTagDevices((String) lAdapter.getItem(position));
    }
    private void deleteDevice(int position){
        dbhelper.deleteDevices((String)lAdapter.getItem(position));
        updateUI();
    }
    private void searchDevice(String uuid){

        Intent search = new Intent(this,ScanDevice.class);
        search.putExtra("UUID", uuid);
        startActivity(search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.devicelist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id== R.id.action_add){
            Intent addDevice = new Intent(this,ScanSearchActivity.class);
            startActivity(addDevice);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    public void updateUI(){
        getDevices();
    }
}
