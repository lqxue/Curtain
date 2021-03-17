package com.lqx.curtain.sample;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lqx.curtain.sample.fragment.GirdViewFragment;
import com.lqx.curtain.sample.fragment.ListViewFragment;

public class AdapterViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array_guide);
        showList(null);
    }

    public void showList(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, new ListViewFragment())
                .commitAllowingStateLoss();
    }

    public void showGird(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, new GirdViewFragment())
                .commitAllowingStateLoss();
    }

}
