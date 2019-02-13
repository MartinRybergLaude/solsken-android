package com.martinryberglaude.skyfall.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.martinryberglaude.skyfall.R;
import com.martinryberglaude.skyfall.interfaces.RecyclerItemClickListener;
import com.martinryberglaude.skyfall.data.EventItem;
import com.martinryberglaude.skyfall.data.HeaderItem;
import com.martinryberglaude.skyfall.data.ListItem;
import com.martinryberglaude.skyfall.data.SMHIWeatherSymbol;
import com.martinryberglaude.skyfall.network.RetroParameter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ListItem> itemList;
    private LayoutInflater inflater;
    private RecyclerItemClickListener recyclerItemClickListener;

    // stores and recycles views as they are scrolled off screen
    public class HeaderViewHolder extends RecyclerView.ViewHolder  {
        TextView dayTextView;
        TextView dateTextView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.text_day);
            dateTextView = itemView.findViewById(R.id.text_date);
        }

    }
    // stores and recycles views as they are scrolled off screen
    public class EventViewHolder extends RecyclerView.ViewHolder  {
        TextView tTextView;
        TextView wsymb2TextView;
        TextView hourTextView;
        TextView windTextView;

        EventViewHolder(View itemView) {
            super(itemView);
            tTextView = itemView.findViewById(R.id.text_temperature);
            wsymb2TextView = itemView.findViewById(R.id.text_wsymb2);
            hourTextView = itemView.findViewById(R.id.text_time);
            windTextView = itemView.findViewById(R.id.text_wind_speed);
        }
    }

    public RecyclerViewAdapter(Context context, List<ListItem> itemList) {
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                View itemView = inflater.inflate(R.layout.recyclerview_header_row, parent, false);
                return new HeaderViewHolder(itemView);
            }
            case ListItem.TYPE_EVENT: {
                View itemView = inflater.inflate(R.layout.recyclerview_event_row, parent, false);
                return new EventViewHolder(itemView);
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                HeaderItem header = (HeaderItem) itemList.get(position);
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;

                Calendar cal = Calendar.getInstance();
                cal.setTime(header.getDate());
                boolean sameDay = cal.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                        cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
                if (sameDay) {
                    holder.dayTextView.setText(R.string.today);
                } else {
                    holder.dayTextView.setText(header.getDayString());
                }
                holder.dateTextView.setText(header.getDateString());
                break;
            }
            case ListItem.TYPE_EVENT: {
                EventItem event = (EventItem) itemList.get(position);
                EventViewHolder holder = (EventViewHolder) viewHolder;

                holder.tTextView.setText(event.getTemperatureString());
                holder.hourTextView.setText(event.getHourString());
                holder.windTextView.setText(event.getWindSpeedString());
                holder.wsymb2TextView.setText(event.getWsymb2String());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerItemClickListener.onItemClick(itemList.get(position));
                    }
                });
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // convenience method for getting data at click position
    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }
}
