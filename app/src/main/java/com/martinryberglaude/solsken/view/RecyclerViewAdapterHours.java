package com.martinryberglaude.solsken.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.martinryberglaude.solsken.R;
import com.martinryberglaude.solsken.data.HourItem;
import com.martinryberglaude.solsken.data.WindDirection;
import com.martinryberglaude.solsken.interfaces.MainContract;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterHours extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HourItem> hourList;
    private LayoutInflater inflater;
    private Context context;

    // stores and recycles views as they are scrolled off screen
    public class HourViewHolder extends RecyclerView.ViewHolder  {
        TextView tTextView;
        TextView wsymb2TextView;
        TextView hourTextView;
        ImageView wsymb2ImageView;

        TableLayout detailsView;
        TextView rainAmountText;
        TextView windSpeedText;
        TextView windDirectionText;
        ImageView windDirectionImg;
        TextView pressureText;
        TextView visibilityText;
        TextView humidityText;
        TextView gustSpeedText;
        TextView cloudText;
        TextView feelsLikeText;

        HourViewHolder(View itemView) {
            super(itemView);
            tTextView = itemView.findViewById(R.id.text_temperature);
            wsymb2TextView = itemView.findViewById(R.id.text_wsymb2);
            hourTextView = itemView.findViewById(R.id.text_day);
            wsymb2ImageView = itemView.findViewById(R.id.wsymb2_img);

            detailsView = itemView.findViewById(R.id.details);
            rainAmountText = itemView.findViewById(R.id.ic_rain_amount);
            windSpeedText = itemView.findViewById(R.id.ic_wind_speed);
            windDirectionText = itemView.findViewById(R.id.ic_wind_direction);
            windDirectionImg = itemView.findViewById(R.id.ic_wind_icon);
            pressureText = itemView.findViewById(R.id.ic_air_pressure);
            visibilityText = itemView.findViewById(R.id.ic_visibility);
            humidityText = itemView.findViewById(R.id.ic_humidity);
            gustSpeedText = itemView.findViewById(R.id.ic_gust_speed);
            cloudText = itemView.findViewById(R.id.ic_cloud_cover);
            feelsLikeText = itemView.findViewById(R.id.ic_feels_like);
        }
    }

    public RecyclerViewAdapterHours(Context context, List<HourItem> hourList) {
        this.inflater = LayoutInflater.from(context);
        this.hourList = hourList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.recyclerview_hour_row, parent, false);
        return new RecyclerViewAdapterHours.HourViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final HourItem hourItem =  hourList.get(position);
        final RecyclerViewAdapterHours.HourViewHolder holder = (RecyclerViewAdapterHours.HourViewHolder) viewHolder;

        boolean expanded = hourItem.isExpanded();

        holder.tTextView.setText(hourItem.getTemperatureString());
        holder.hourTextView.setText(hourItem.getHourString());
        holder.wsymb2TextView.setText(hourItem.getWsymb2String());
        holder.wsymb2ImageView.setImageResource(hourItem.getWsymb2Drawable());

        if (!expanded) {
            holder.detailsView.setVisibility(View.GONE);
        } else {
            holder.detailsView.setVisibility(View.VISIBLE);
            holder.rainAmountText.setText(hourItem.getRainAmountString());
            holder.windSpeedText.setText(hourItem.getWindSpeedString());
            setWindDirectionUI(hourItem.getWindDirection(), holder);
            holder.pressureText.setText(hourItem.getPressureString());
            holder.visibilityText.setText(hourItem.getVisibilityString());
            holder.humidityText.setText(hourItem.getHumidityString());
            holder.gustSpeedText.setText(hourItem.getGustSpeedString());
            holder.cloudText.setText(hourItem.getCloudCoverString());
            holder.feelsLikeText.setText(hourItem.getFeelsLikeString());
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = hourItem.isExpanded();
                hourItem.setExpanded(!expanded);
                notifyItemChanged(position);
                //itemListener.onItemClick(hourList.get(position));
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return hourList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setWindDirectionUI(@WindDirection.Direction int windDirection, RecyclerViewAdapterHours.HourViewHolder holder) {
        String windDirectionString;

        switch (windDirection) {
            case WindDirection.N:
                windDirectionString = context.getString(R.string.north);
                holder.windDirectionText.setText(windDirectionString);
                holder.windDirectionImg.setRotation(180);
                break;
            case WindDirection.NE:
                windDirectionString = context.getString(R.string.northeast);
                holder.windDirectionText.setText(windDirectionString);
                holder.windDirectionImg.setRotation(225);
                break;
            case WindDirection.E:
                windDirectionString = context.getString(R.string.east);
                holder.windDirectionText.setText(windDirectionString);
                holder.windDirectionImg.setRotation(270);
                break;
            case WindDirection.SE:
                windDirectionString = context.getString(R.string.southeast);
                holder.windDirectionText.setText(windDirectionString);
                holder.windDirectionImg.setRotation(315);
                break;
            case WindDirection.S:
                windDirectionString = context.getString(R.string.south);
                holder.windDirectionText.setText(windDirectionString);
                holder.windDirectionImg.setRotation(0);
                break;
            case WindDirection.SW:
                windDirectionString = context.getString(R.string.southwest);
                holder.windDirectionText.setText(windDirectionString);
                holder.windDirectionImg.setRotation(45);
                break;
            case WindDirection.W:
                windDirectionString = context.getString(R.string.west);
                holder.windDirectionText.setText(windDirectionString);
                holder.windDirectionImg.setRotation(90);
                break;
            case WindDirection.NW:
                windDirectionString = context.getString(R.string.northwest);
                holder.windDirectionText.setText(windDirectionString);
                holder.windDirectionImg.setRotation(135);
                break;
        }
    }
}
