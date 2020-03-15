package com.example.park.noteped;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class MyAdapter extends android.widget.BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<SampleData> sample =null;

    public MyAdapter(Context context, ArrayList<SampleData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);


    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public SampleData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_item, null);

        ImageView picture = (ImageView)view.findViewById(R.id.picture);
        TextView tilte = (TextView)view.findViewById(R.id.Notepedtitle);
        TextView context = (TextView)view.findViewById(R.id.Notepedcontext);


       if(sample.get(position).getPicture() == null)  picture.setVisibility(View.GONE);

        else picture.setVisibility(View.VISIBLE);

        picture.setImageBitmap(sample.get(position).getPicture());
        tilte.setText(sample.get(position).getTitle());
        context.setText(sample.get(position).getContext());

        return view;
    }
}