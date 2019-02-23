package com.martinryberglaude.skyfall.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martinryberglaude.skyfall.R;
import com.martinryberglaude.skyfall.data.HourItem;
import com.martinryberglaude.skyfall.interfaces.MainContract;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterHours extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HourItem> hourList;
    private LayoutInflater inflater;
    private MainContract.HourItemOnClickListener itemListener;

    // stores and recycles views as they are scrolled off screen
    public class HourViewHolder extends RecyclerView.ViewHolder  {
        TextView tTextView;
        TextView wsymb2TextView;
        TextView hourTextView;
        ImageView wsymb2ImageView;

        HourViewHolder(View itemView) {
            super(itemView);
            tTextView = itemView.findViewById(R.id.text_temperature);
            wsymb2TextView = itemView.findViewById(R.id.text_wsymb2);
            hourTextView = itemView.findViewById(R.id.text_day);
            wsymb2ImageView = itemView.findViewById(R.id.wsymb2_img);
        }
    }

    public RecyclerViewAdapterHours(Context context, List<HourItem> hourList, MainContract.HourItemOnClickListener itemListener) {
        this.inflater = LayoutInflater.from(context);
        this.hourList = hourList;
        this.itemListener = itemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new RecyclerViewAdapterHours.HourViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        HourItem hourItem =  hourList.get(position);
        RecyclerViewAdapterHours.HourViewHolder holder = (RecyclerViewAdapterHours.HourViewHolder) viewHolder;

        holder.tTextView.setText(hourItem.getTemperatureString());
        holder.hourTextView.setText(hourItem.getHourString());
        holder.wsymb2TextView.setText(hourItem.getWsymb2String());
        holder.wsymb2ImageView.setImageResource(hourItem.getWsymb2Drawable());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onItemClick(hourList.get(position));
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return hourList.size();
    }
}
