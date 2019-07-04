package com.williamkosasih.filemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        RadioButton nameRadio = findViewById(R.id.radio_btn_name_sort);
        RadioButton dateRadio = findViewById(R.id.radio_btn_date_sort);

        nameRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.sort_mode = 1;
            }
        });
        dateRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.sort_mode = 2;
            }
        });

    }
}
