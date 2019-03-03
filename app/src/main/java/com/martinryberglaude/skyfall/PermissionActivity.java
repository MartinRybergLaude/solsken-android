package com.martinryberglaude.skyfall;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Button button = findViewById(R.id.button_accept);
        TextView welcomeText = findViewById(R.id.text_welcome);
        TextView permissionText = findViewById(R.id.text_permission);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }
    @TargetApi(23)
    private void requestPermission() {
        // Request location permission
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1 : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, go to main activity
                    Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
                    this.startActivity(intent);
                    finish();
                } else {
                    // Permission is denied
                    Toast.makeText(this, R.string.permission_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
