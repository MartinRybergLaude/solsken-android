package com.martinryberglaude.skyfall.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martinryberglaude.skyfall.R;
import com.martinryberglaude.skyfall.data.DayItem;
import com.martinryberglaude.skyfall.interfaces.RecyclerItemClickListener;

import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DayItem> dayList;
    private LayoutInflater inflater;
    private RecyclerItemClickListener recyclerItemClickListener;

    // stores and recycles views as they are scrolled off screen
    public class DayViewHolder extends RecyclerView.ViewHolder  {
        TextView tTextView;
        TextView wsymb2TextView;
        TextView dayTextView;
        ImageView wsymb2ImageView;

        DayViewHolder(View itemView) {
            super(itemView);
            tTextView = itemView.findViewById(R.id.text_temperature);
            wsymb2TextView = itemView.findViewById(R.id.text_wsymb2);
            dayTextView = itemView.findViewById(R.id.text_day);
            wsymb2ImageView = itemView.findViewById(R.id.wsymb2_img);
        }
    }

    public RecyclerViewAdapter(Context context, List<DayItem> dayList) {
        this.inflater = LayoutInflater.from(context);
        this.dayList = dayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.recyclerview_day_row, parent, false);
        return new DayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

            DayItem dayItem =  dayList.get(position);
            DayViewHolder holder = (DayViewHolder) viewHolder;

            holder.tTextView.setText(dayItem.getTemperatureString());

            Calendar currentCal = Calendar.getInstance();
            Calendar tomorrowCal = Calendar.getInstance();
            tomorrowCal.add(Calendar.DAY_OF_MONTH, 1);
            currentCal.setTime(dayItem.getDate());
            boolean sameDay = currentCal.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                currentCal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);

            boolean nextDay = currentCal.get(Calendar.DAY_OF_YEAR) == tomorrowCal.get(Calendar.DAY_OF_YEAR) &&
                currentCal.get(Calendar.YEAR) == tomorrowCal.get(Calendar.YEAR);

            if (sameDay) {
                holder.dayTextView.setText(R.string.today);
            } else if (nextDay) {
                holder.dayTextView.setText(R.string.tomorrow);
            } else {
                holder.dayTextView.setText(dayItem.getDayString());
            }
            holder.wsymb2TextView.setText(dayItem.getWsymb2String());
            holder.wsymb2ImageView.setImageResource(dayItem.getWsymb2Drawable());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerItemClickListener.onItemClick(dayList.get(position));
                }
            });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return dayList.size();
    }
}
