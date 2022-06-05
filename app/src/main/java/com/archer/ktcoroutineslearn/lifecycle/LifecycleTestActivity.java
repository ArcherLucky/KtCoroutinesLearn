package com.archer.ktcoroutineslearn.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.archer.ktcoroutineslearn.R;

public class LifecycleTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle_test);
    }
}