package com.martinryberglaude.solsken;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String licenseRetrofit = "Copyright 2013 Square, Inc. \n\n Licensed under the Apache License, Version 2.0 (the \"License\"); " +
            "you may not use this file except in compliance with the License. " +
            "You may obtain a copy of the License at\n\n" +
            "http://www.apache.org/licenses/LICENSE-2.0\n\n" +
            "Unless required by applicable law or agreed to in writing, software " +
            "distributed under the License is distributed on an \"AS IS\" BASIS, " +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. " +
            "See the License for the specific language governing permissions and " +
            "limitations under the License.";
    private String licenseGson = "Copyright 2008 Google Inc. \n\n Licensed under the Apache License, Version 2.0 (the \"License\"); " +
            "you may not use this file except in compliance with the License. " +
            "You may obtain a copy of the License at\n\n" +
            "http://www.apache.org/licenses/LICENSE-2.0\n\n" +
            "Unless required by applicable law or agreed to in writing, software " +
            "distributed under the License is distributed on an \"AS IS\" BASIS, " +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. " +
            "See the License for the specific language governing permissions and " +
            "limitations under the License.";
    private String licenseMPAndroidChart = "Copyright 2018 Philipp Jahoda \n\n Licensed under the Apache License, Version 2.0 (the \"License\"); " +
            "you may not use this file except in compliance with the License. " +
            "You may obtain a copy of the License at\n\n" +
            "http://www.apache.org/licenses/LICENSE-2.0\n\n" +
            "Unless required by applicable law or agreed to in writing, software " +
            "distributed under the License is distributed on an \"AS IS\" BASIS, " +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. " +
            "See the License for the specific language governing permissions and " +
            "limitations under the License.";
    private String licenseMaterialDrawer = "Copyright 2018 Mike Penz \n\n Licensed under the Apache License, Version 2.0 (the \"License\"); " +
            "you may not use this file except in compliance with the License. " +
            "You may obtain a copy of the License at\n\n" +
            "http://www.apache.org/licenses/LICENSE-2.0\n\n" +
            "Unless required by applicable law or agreed to in writing, software " +
            "distributed under the License is distributed on an \"AS IS\" BASIS, " +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. " +
            "See the License for the specific language governing permissions and " +
            "limitations under the License.";
    private String licenseSunriseSunset =
            "This software uses the SunriseSunset library.\n\n" +
            "Copyright 2017 caarmen\n\n" +
            "The SunriseSunset library is licensed under the GNU Lesser General Public Library, version 2.1. You can find a copy of this license at https://www.gnu.org/licenses/lgpl-2.1.en.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_about);

        toolbar = findViewById(R.id.toolbar_abt);
        applyToolbarTheme();
        TextView toolbarTitle = findViewById(R.id.title_abt);
        toolbarTitle.setText(getString(R.string.about));
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        ImageView sunImg = findViewById(R.id.img_sun);
        ImageView cloudImg = findViewById(R.id.img_cloud1);
        final ImageView cloud2Img = findViewById(R.id.img_cloud2);

        cloudImg.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_to_side) );
        cloud2Img.setVisibility(View.INVISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cloud2Img.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_to_side) );
                cloud2Img.setVisibility(View.VISIBLE);
            }
        }, 600);

        RelativeLayout nameLayout = findViewById(R.id.select_name);
        RelativeLayout websiteLayout = findViewById(R.id.select_website);
        RelativeLayout twitterLayout = findViewById(R.id.select_twitter);
        RelativeLayout instagramLayout = findViewById(R.id.select_instagram);

        websiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.martinryberglaude.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        twitterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/martini_rl";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        instagramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/martinryberglaude";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        RelativeLayout libRetrofit = findViewById(R.id.lib_retrofit);
        libRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLicenseDialog(licenseRetrofit);
            }
        });
        RelativeLayout libGson = findViewById(R.id.lib_gson);
        libGson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLicenseDialog(licenseGson);
            }
        });
        RelativeLayout libMPAndroidChart = findViewById(R.id.lib_mpandroidchart);
        libMPAndroidChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLicenseDialog(licenseMPAndroidChart);
            }
        });
        RelativeLayout libMaterialDrawer = findViewById(R.id.lib_materialdrawer);
        libMaterialDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLicenseDialog(licenseMaterialDrawer);
            }
        });
        RelativeLayout libSunrisesunset = findViewById(R.id.lib_sunrisesunset);
        libSunrisesunset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLicenseDialog(licenseSunriseSunset);
            }
        });
    }

    private void showLicenseDialog(String licenseText) {
        AlertDialog dialog = new AlertDialog.Builder(AboutActivity.this)
                .setTitle(getString(R.string.license))
                .setMessage(licenseText)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setScroller(new Scroller(AboutActivity.this));
        textView.setVerticalScrollBarEnabled(true);
        textView.setMovementMethod(new ScrollingMovementMethod());
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
