package com.martinryberglaude.solsken;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.martinryberglaude.solsken.data.DayItem;

import java.util.ArrayList;
import java.util.List;

public class GraphsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_graphs);

        toolbar = findViewById(R.id.toolbar_ghs);
        applyToolbarTheme();
        TextView toolbarTitle = findViewById(R.id.title_ghs);
        toolbarTitle.setText(getString(R.string.diagram));
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        List<DayItem> dayList = (List<DayItem>) getIntent().getSerializableExtra("dayList");

        LineChart chartTemperature = (LineChart) findViewById(R.id.chart_temperature);
        chartTemperature.setNoDataText(getString(R.string.general_error_title));
        chartTemperature.setDrawGridBackground(false);
        chartTemperature.setDrawBorders(false);
        chartTemperature.setAutoScaleMinMaxEnabled(true);
        chartTemperature.setPinchZoom(false);
        chartTemperature.setVisibleXRange(1,7);
        chartTemperature.setVisibleYRange(5, 5, YAxis.AxisDependency.LEFT);
        Description description = new Description();
        description.setText("");
        chartTemperature.setDescription(description);

        final ArrayList<String> xLabelNames = new ArrayList<>();
        for (DayItem dayItem : dayList) {
            xLabelNames.add(dayItem.getDayString().substring(0, 3));
        }


        XAxis xAxis = chartTemperature.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelNames.get((int)value);
            }
        });
        xAxis.setDrawGridLines(false);
        YAxis yAxis = chartTemperature.getAxisRight();
        yAxis.setDrawLabels(false);
        yAxis.setDrawGridLines(false);

        List<Entry> entriesTemperatureLow = new ArrayList<>();
        float count = 0;
        for (DayItem dayItem : dayList) {
            String[] temperatures = dayItem.getTemperatureLowString().split("°");
            // turn your data into Entry objects
            entriesTemperatureLow.add(new Entry(count, Integer.parseInt(temperatures[0])));
            count++;
        }

        List<Entry> entriesTemperatureHigh = new ArrayList<>();
        float count2 = 0;
        for (DayItem dayItem : dayList) {
            String[] temperatures = dayItem.getTemperatureHighString().split("°");

            entriesTemperatureHigh.add(new Entry(count2, Integer.parseInt(temperatures[0])));
            count2++;
        }

        LineDataSet dataSetTempLow = new LineDataSet(entriesTemperatureLow, "Low temperatures");
        dataSetTempLow.setColor(getResources().getColor(R.color.dayPrimary));

        LineDataSet dataSetTempHigh = new LineDataSet(entriesTemperatureHigh, "High temperatures");
        dataSetTempHigh.setColor(getResources().getColor(R.color.sunrisePrimary));
        dataSetTempHigh.setDrawValues(true);
        dataSetTempHigh.setDrawCircles(false);
        dataSetTempHigh.setLineWidth(3f);
        dataSetTempHigh.setCircleColor(getResources().getColor(R.color.auroraPrimary));
        dataSetTempHigh.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSetTempLow.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSetTempLow, dataSetTempHigh);
        chartTemperature.setData(lineData);
    }

    private void applyTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String colorTheme = sharedPreferences.getString("theme", "day");
        boolean darkTheme = sharedPreferences.getBoolean("dark_theme", false);
        if (!darkTheme) {
            switch (colorTheme) {
                case "day":
                    setTheme(R.style.AppThemeDay);
                    break;
                case "sunset":
                    setTheme(R.style.AppThemeSunrise);
                    break;
                case "night":
                    setTheme(R.style.AppThemeNight);
                    break;
                case "aurora":
                    setTheme(R.style.AppThemeAurora);
                    break;
                default:
                    setTheme(R.style.AppThemeDay);
                    break;
            }
        } else {
            switch (colorTheme) {
                case "day":
                    setTheme(R.style.DarkThemeDay);
                    break;
                case "sunset":
                    setTheme(R.style.DarkThemeSunrise);
                    break;
                case "night":
                    setTheme(R.style.DarkThemeNight);
                    break;
                case "aurora":
                    setTheme(R.style.DarkThemeAurora);
                    break;
                default:
                    setTheme(R.style.DarkThemeDay);
                    break;
            }
        }
    }
    private void applyToolbarTheme() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.primaryColor, typedValue, true);
        @ColorInt int color = typedValue.data;
        toolbar.setBackgroundColor(color);
    }
}
