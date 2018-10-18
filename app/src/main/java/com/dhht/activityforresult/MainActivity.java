package com.dhht.activityforresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected OnResultManager onResultManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onResultManager = new OnResultManager(this);
        Intent intent = new Intent(this, Main2Activity.class);
        onResultManager.startForResult(intent, 1111, new OnResultManager.Callback() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "" + requestCode, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "canceled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onResultManager.trigger(requestCode, resultCode, data);
    }
}
