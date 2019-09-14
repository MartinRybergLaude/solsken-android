package com.martinryberglaude.solsken;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.martinryberglaude.solsken.data.DayItem;
import com.martinryberglaude.solsken.data.HourItem;
import com.martinryberglaude.solsken.data.WindDirection;
import com.martinryberglaude.solsken.utils.CustomChartGestureListener;

import java.util.ArrayList;
import java.util.List;

public class GraphsActivity extends AppCompatActivity {

    private static final String TAG = GraphsActivity.class.getSimpleName();
    private boolean isDarkMode = false;

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("dark_theme", false)) {
            isDarkMode = true;
        }

        DayItem dayItem = (DayItem) getIntent().getSerializableExtra("dayItem");
        List<HourItem> hourList = new ArrayList<>(dayItem.getHourList());

        LineChart tempChart = findViewById(R.id.chart_temperature);
        LineChart windChart = findViewById(R.id.chart_wind);
        LineChart pressureChart = findViewById(R.id.chart_pressure);
        LineChart humidityChart = findViewById(R.id.chart_humidity);
        CombinedChart precChart = findViewById(R.id.chart_precipitation);

        initChartTemperature(hourList, tempChart);
        initChartWind(hourList, windChart);
        initChartPressure(hourList, pressureChart);
        initChartPrecipitation(hourList, precChart);
        initChartHumidity(hourList, humidityChart);

        /*tempChart.setOnChartGestureListener(new CustomChartGestureListener(
                tempChart, new Chart[] { windChart, pressureChart, precChart, humidityChart}));
        windChart.setOnChartGestureListener(new CustomChartGestureListener(
                windChart, new Chart[] { tempChart, pressureChart, precChart, humidityChart}));
        pressureChart.setOnChartGestureListener(new CustomChartGestureListener(
                pressureChart, new Chart[] { tempChart, windChart, precChart, humidityChart}));
        precChart.setOnChartGestureListener(new CustomChartGestureListener(
                precChart, new Chart[] { tempChart, windChart, pressureChart, humidityChart}));
        humidityChart.setOnChartGestureListener(new CustomChartGestureListener(
                humidityChart, new Chart[] { tempChart, windChart, pressureChart, precChart})); */
    }

    private void initChartTemperature(List<HourItem> hourList, LineChart chart) {

        if (isDarkMode) {
            chart.setBackgroundColor(Color.BLACK);
            chart.getAxisLeft().setTextColor(Color.WHITE);
            chart.getXAxis().setTextColor(Color.WHITE);
            chart.getLegend().setTextColor(Color.WHITE);
        }

        chart.setNoDataText(getString(R.string.general_error_title));
        chart.setDrawBorders(false);
        chart.setAutoScaleMinMaxEnabled(false);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        final ArrayList<String> xLabelNames = new ArrayList<>();
        for (HourItem hourItem : hourList) {
            xLabelNames.add(hourItem.getHourString());
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelNames.get((int)value);
            }
        });
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        if (isDarkMode) {
            xAxis.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            xAxis.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }


        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisRight.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisRight.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisLeft.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisLeft.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(Math.round(value)) + "°";
            }
        });

        List<Entry> entries = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        float count = 0;
        for (HourItem hourItem : hourList) {
            if (hourItem.getTemperatureString() != null) {
                String[] temperatures = hourItem.getTemperatureString().split("°");
                Drawable icon = getResources().getDrawable(hourItem.getWsymb2Drawable());
                if (isDarkMode) { icon.setTint(Color.WHITE); }
                entries.add(new Entry(count, Integer.parseInt(temperatures[0]), icon));
            }
            if (hourItem.getFeelsLikeString() != null) {
                String[] temperatures = hourItem.getFeelsLikeString().split("°");
                entries2.add(new Entry(count, Integer.parseInt(temperatures[0])));
            }
            count++;
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.temperature));
        dataSet.setColor(getResources().getColor(R.color.sunrisePrimary));
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawIcons(true);
        dataSet.setLineWidth(3f);
        dataSet.setDrawFilled(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet dataSet2 = new LineDataSet(entries2, getString(R.string.feels_like));
        dataSet2.setColor(getResources().getColor(R.color.sunriseColor1));
        dataSet2.setDrawValues(false);
        dataSet2.setDrawCircles(false);
        dataSet2.setDrawIcons(true);
        dataSet2.setLineWidth(3f);
        dataSet2.setDrawFilled(false);
        dataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet2, dataSet);
        chart.setData(lineData);
        chart.setVisibleXRangeMaximum(8);
        chart.getAxisLeft().setAxisMinimum(yAxisLeft.getAxisMinimum() - 1);
        chart.getAxisLeft().setAxisMaximum(yAxisLeft.getAxisMaximum() + 1);
        chart.getData().setHighlightEnabled(false);
    }
    private void initChartWind(List<HourItem> hourList, LineChart chart) {
        if (isDarkMode) {
            chart.setBackgroundColor(Color.BLACK);
            chart.getAxisLeft().setTextColor(Color.WHITE);
            chart.getXAxis().setTextColor(Color.WHITE);
            chart.getLegend().setTextColor(Color.WHITE);
        }
        chart.setNoDataText(getString(R.string.general_error_title));
        chart.setDrawBorders(false);
        chart.setAutoScaleMinMaxEnabled(false);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        final ArrayList<String> xLabelNames = new ArrayList<>();
        for (HourItem hourItem : hourList) {
            xLabelNames.add(hourItem.getHourString());
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelNames.get((int)value);
            }
        });
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        if (isDarkMode) {
            xAxis.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            xAxis.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }


        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisRight.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisRight.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisLeft.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisLeft.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(Math.round(value));
            }
        });

        List<Entry> entries = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        float count = 0;
        for (HourItem hourItem : hourList) {
            if (hourItem.getWindSpeedString() != null) {
                String[] winds = hourItem.getWindSpeedString().split(" ");
                Drawable icon = getResources().getDrawable(hourItem.getWindDrawable());
                if (isDarkMode) {icon.setTint(Color.WHITE);}
                entries.add(new Entry(count, Integer.parseInt(winds[0]), icon));
            }
            if (hourItem.getGustSpeedString() != null) {
                String[] gusts = hourItem.getGustSpeedString().split(" ");
                entries2.add(new Entry(count, Integer.parseInt(gusts[0])));
            }
            count++;
        }
        LineDataSet dataSet = new LineDataSet(entries, getResources().getString(R.string.wind));
        dataSet.setColor(getResources().getColor(R.color.auroraPrimary));
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawIcons(true);
        dataSet.setLineWidth(3f);
        dataSet.setDrawFilled(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet dataSet2 = new LineDataSet(entries2, getResources().getString(R.string.gusts));
        dataSet2.setColor(getResources().getColor(R.color.darkerLightGray));
        dataSet2.setDrawValues(false);
        dataSet2.setDrawCircles(false);
        dataSet2.setDrawIcons(true);
        dataSet2.setLineWidth(3f);
        dataSet2.setFillColor(getResources().getColor(R.color.darkerLightGray));
        dataSet2.setDrawFilled(true);
        dataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet, dataSet2);
        chart.setData(lineData);
        chart.setVisibleXRangeMaximum(8);
        chart.getAxisLeft().setAxisMinimum(yAxisLeft.getAxisMinimum() - 1);
        chart.getAxisLeft().setAxisMaximum(yAxisLeft.getAxisMaximum() + 1);
        chart.getData().setHighlightEnabled(false);
    }
    private void initChartPressure(List<HourItem> hourList, LineChart chart) {
        if (isDarkMode) {
            chart.setBackgroundColor(Color.TRANSPARENT);
            chart.getAxisLeft().setTextColor(Color.WHITE);
            chart.getXAxis().setTextColor(Color.WHITE);
            chart.getLegend().setTextColor(Color.WHITE);
        }
        chart.setNoDataText(getString(R.string.general_error_title));
        chart.setDrawBorders(false);
        chart.setAutoScaleMinMaxEnabled(false);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        final ArrayList<String> xLabelNames = new ArrayList<>();
        for (HourItem hourItem : hourList) {
            xLabelNames.add(hourItem.getHourString());
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelNames.get((int)value);
            }
        });
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        if (isDarkMode) {
            xAxis.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            xAxis.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }


        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisRight.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisRight.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisLeft.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisLeft.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(Math.round(value));
            }
        });

        List<Entry> entries = new ArrayList<>();
        float count = 0;
        for (HourItem hourItem : hourList) {
            if (hourItem.getPressureString() != null) {
                String[] pressures = hourItem.getPressureString().split(" ");
                entries.add(new Entry(count, Integer.parseInt(pressures[0])));
            }
            count++;
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.pressure));
        dataSet.setColor(getResources().getColor(R.color.nightPrimary));
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawIcons(true);
        dataSet.setLineWidth(3f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(getResources().getColor(R.color.nightPrimary));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setVisibleXRangeMaximum(8);
        chart.getAxisLeft().setAxisMinimum(yAxisLeft.getAxisMinimum() - 1);
        chart.getAxisLeft().setAxisMaximum(yAxisLeft.getAxisMaximum() + 1);
        chart.getData().setHighlightEnabled(false);
    }
    private void initChartPrecipitation(List<HourItem> hourList, CombinedChart chart) {
        if (isDarkMode) {
            chart.setBackgroundColor(Color.TRANSPARENT);
            chart.getAxisLeft().setTextColor(Color.WHITE);
            chart.getXAxis().setTextColor(Color.WHITE);
            chart.getLegend().setTextColor(Color.WHITE);
        }
        chart.setNoDataText(getString(R.string.general_error_title));
        chart.setDrawBorders(false);
        chart.setAutoScaleMinMaxEnabled(false);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        final ArrayList<String> xLabelNames = new ArrayList<>();
        for (HourItem hourItem : hourList) {
            xLabelNames.add(hourItem.getHourString());
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelNames.get((int)value);
            }
        });
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        if (isDarkMode) {
            xAxis.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            xAxis.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }


        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisRight.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisRight.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisLeft.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisLeft.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(value);
            }
        });

        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();
        float count = 0;
        for (HourItem hourItem : hourList) {
            if (hourItem.getRainAmountStringLow() != null) {
                String[] rains = hourItem.getRainAmountStringLow().split(" ");
                entries.add(new BarEntry(count, Float.parseFloat(rains[0])));
            }
            if (hourItem.getRainAmountStringHigh() != null) {
                String[] rains2 = hourItem.getRainAmountStringHigh().split(" ");
                entries2.add(new BarEntry(count, Float.parseFloat(rains2[0])));
            }
            if (hourItem.getRainAmountString() != null) {
                String[] rains3 = hourItem.getRainAmountString().split(" ");
                if (Float.parseFloat(rains3[0]) == 0) {
                    entries3.add(new Entry(count, -0.5f));
                } else {
                    entries3.add(new Entry(count, Float.parseFloat(rains3[0])));
                }
            }
            count++;
        }

        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.precipitation_low));
        dataSet.setColor(getResources().getColor(R.color.dayColor2));
        dataSet.setDrawValues(false);
        dataSet.setDrawIcons(true);

        BarDataSet dataSet2 = new BarDataSet(entries2, getString(R.string.precipitation_high));
        dataSet2.setColor(getResources().getColor(R.color.rainHigh));
        dataSet2.setDrawValues(false);
        dataSet2.setDrawIcons(true);

        LineDataSet dataSet3 = new LineDataSet(entries3, getString(R.string.precipitation_median));
        dataSet3.setColor(getResources().getColor(R.color.dayPrimary));
        dataSet3.setDrawValues(false);
        dataSet3.setDrawCircles(false);
        dataSet3.setDrawIcons(true);
        dataSet3.setLineWidth(3f);
        dataSet3.setDrawFilled(false);
        dataSet3.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        BarData barData = new BarData(dataSet2, dataSet);
        LineData lineData = new LineData(dataSet3);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        combinedData.setData(lineData);
        chart.setData(combinedData);
        chart.setVisibleXRangeMaximum(8);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setAxisMaximum(yAxisLeft.getAxisMaximum() + 0.5f);
        chart.getData().setHighlightEnabled(false);
    }
    private void initChartHumidity(List<HourItem> hourList, LineChart chart) {
        if (isDarkMode) {
            chart.setBackgroundColor(Color.TRANSPARENT);
            chart.getAxisLeft().setTextColor(Color.WHITE);
            chart.getXAxis().setTextColor(Color.WHITE);
            chart.getLegend().setTextColor(Color.WHITE);
        }
        chart.setNoDataText(getString(R.string.general_error_title));
        chart.setDrawBorders(false);
        chart.setAutoScaleMinMaxEnabled(false);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        final ArrayList<String> xLabelNames = new ArrayList<>();
        for (HourItem hourItem : hourList) {
            xLabelNames.add(hourItem.getHourString());
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelNames.get((int)value);
            }
        });
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        if (isDarkMode) {
            xAxis.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            xAxis.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }


        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisRight.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisRight.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setDrawAxisLine(false);
        if (isDarkMode) {
            yAxisLeft.setGridColor(getResources().getColor(R.color.lighterDarkGray));
        } else {
            yAxisLeft.setGridColor(getResources().getColor(R.color.darkerLightGray));
        }
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(Math.round(value));
            }
        });

        List<Entry> entries = new ArrayList<>();
        float count = 0;
        for (HourItem hourItem : hourList) {
            if (hourItem.getHumidityString() != null) {
                String[] humidity = hourItem.getHumidityString().split("%");
                entries.add(new Entry(count, Integer.parseInt(humidity[0])));
            }
            count++;
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.humidity));
        dataSet.setColor(getResources().getColor(R.color.dayPrimary));
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawIcons(true);
        dataSet.setLineWidth(3f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(getResources().getColor(R.color.dayPrimary));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setVisibleXRangeMaximum(8);
        chart.getAxisLeft().setAxisMinimum(yAxisLeft.getAxisMinimum() - 1);
        chart.getAxisLeft().setAxisMaximum(yAxisLeft.getAxisMaximum() + 1);
        chart.getData().setHighlightEnabled(false);
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
