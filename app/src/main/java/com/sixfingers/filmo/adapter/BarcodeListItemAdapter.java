package com.sixfingers.filmo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sixfingers.filmo.R;
import com.sixfingers.filmo.model.Movie;

import java.util.HashMap;

public class BarcodeListItemAdapter extends BaseAdapter {
    private HashMap<String, Movie> map;
    private String[] keys;

    public static class BarcodeSearchViewHolder extends RecyclerView.ViewHolder {
        public TextView barcodeText;
        public ProgressBar progress;

        public BarcodeSearchViewHolder(View itemView) {
            super(itemView);

            barcodeText = (TextView) itemView.findViewById(R.id.barcode_text);
            progress = (ProgressBar) itemView.findViewById(R.id.action_progress);
        }
    }

    public BarcodeListItemAdapter(HashMap<String, Movie> barcodes) {
        map = barcodes;
        keys = barcodes.keySet().toArray(new String[barcodes.size()]);
    }

    public boolean add(String text) {
        if (!map.containsKey(text)) {
            map.put(text, null);
            keys = map.keySet().toArray(new String[map.size()]);

            notifyDataSetChanged();

            return true;
        }

        return false;
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Movie getItem(int i) {
        return map.get(keys[i]);
    }

    @Override
    public long getItemId(int i) {
        return keys[i].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String barcode = keys[position];
        Movie movie = map.get(barcode);

        BarcodeSearchViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.barcode_searching,
                    parent,
                    false
            );

            viewHolder = new BarcodeSearchViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BarcodeSearchViewHolder) convertView.getTag();
        }

        viewHolder.barcodeText.setText(barcode);

        return convertView;
    }
}
