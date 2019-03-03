package com.martinryberglaude.skyfall.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinryberglaude.skyfall.R;
import com.martinryberglaude.skyfall.data.HourItem;
import com.martinryberglaude.skyfall.data.LocationItem;
import com.martinryberglaude.skyfall.interfaces.SearchContract;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterLocations extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<LocationItem> locationList;
    private SearchContract.LocationItemClickListener itemListener;

    public class LocationViewHolder extends RecyclerView.ViewHolder  {
        TextView cityTextView;
        TextView countryTextView;

        LocationViewHolder(View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.text_city);
            countryTextView = itemView.findViewById(R.id.text_country);
        }
    }
    public RecyclerViewAdapterLocations(Context context, List<LocationItem> locationList, SearchContract.LocationItemClickListener itemListener) {
        this.inflater = LayoutInflater.from(context);
        this.locationList = locationList;
        this.itemListener = itemListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_location_row, parent, false);
        return new RecyclerViewAdapterLocations.LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        LocationItem locationItem =  locationList.get(position);
        RecyclerViewAdapterLocations.LocationViewHolder holder = (RecyclerViewAdapterLocations.LocationViewHolder) viewHolder;

        holder.cityTextView.setText(locationItem.getCityString());
        holder.countryTextView.setText(locationItem.getCountryString());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onItemClick(locationList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}
