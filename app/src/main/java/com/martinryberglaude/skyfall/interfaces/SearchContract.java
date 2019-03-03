package com.martinryberglaude.skyfall.interfaces;

import com.martinryberglaude.skyfall.data.LocationItem;

import java.util.List;

public interface SearchContract {
    interface FormatLocationIntractor {
        interface OnFinishedListener {
            void onFinishedFormatLocations(List<LocationItem> locationList);
            void onFailureFormatLocations();
        }
    }
    interface LocationItemClickListener {
        void onItemClick(LocationItem dayItem);
    }
}
