package com.attlds.chinmay.ant;

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

/**
 * Created by chinmay on 18/3/16.
 */
public class DbAdapter extends BaseAdapter {

    LayoutInflater inflater;
    public ArrayList<String> result = new ArrayList<>();
    public ArrayList<String> UUID = new ArrayList<>();

    private Context mContext;
    public DbAdapter(Activity MainActivity){
        mContext = MainActivity;
        inflater = ( LayoutInflater )MainActivity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return result.size();
    }

    public void add(String name, String uuid){
        result.add(name);
        UUID.add(uuid);
    }
    @Override
    public Object getItem(int position) {
        return UUID.get(position);
    }

    public void setList(ArrayList<ArrayList<String>> res){
        result.clear();
        UUID.clear();
        result.addAll(res.get(0));
        UUID.addAll(res.get(1));
    }
    @Override
    public long getItemId(int position) {
        return position;
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
        holder.tv.setText(result.get(position));
        holder.tv2.setText(UUID.get(position));
        return rowView;
    }
}
