package com.attlds.chinmay.ant;

/**
 * Created by chinmay on 1/3/16.
 */
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {
    ArrayList<BluetoothDevice> result = new ArrayList<>();
    ArrayList<Integer> power = new ArrayList<>();
    Context context;
    //    ArrayList<BluetoothDevice> AddrId = new ArrayList<>();
    private static LayoutInflater inflater=null;
    public CustomAdapter(Activity mainActivity) {
        // TODO Auto-generated constructor stub
        //result=prgmNameList;
        context=mainActivity;
        //AddrId=prgmAddrList;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    public void add(BluetoothDevice deviceName,int pow){
        if(!result.contains(deviceName)) {
            result.add(deviceName);
            power.add(pow);
        }

    }

    public void clear(){
        result.clear();
        power.clear();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public Integer getPower(int position){
        return power.get(position);
    }
    public class Holder
    {
        TextView tv;
        TextView tv2;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.device_list, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.tv2=(TextView) rowView.findViewById(R.id.textView2);
        holder.tv.setText(result.get(position).getName());
        holder.tv2.setText(result.get(position).toString());

        return rowView;
    }

}