package com.williamkosasih.filemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        RadioButton nameRadio = findViewById(R.id.radio_btn_name_sort);
        RadioButton dateRadio = findViewById(R.id.radio_btn_date_sort);
        CheckBox hideFilesCheckBox = findViewById(R.id.hide_files_checkbox);

        switch (HomeActivity.sort_mode) {
            case 1:
                nameRadio.setChecked(true);
                break;
            case 2:
                dateRadio.setChecked(true);
                break;
        }
        hideFilesCheckBox.setChecked(HomeActivity.hideHiddenFiles);

        nameRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.sort_mode = 1;
                HomeActivity.adapter_update();
            }
        });
        dateRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.sort_mode = 2;
                HomeActivity.adapter_update();
            }
        });
        hideFilesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HomeActivity.hideHiddenFiles = isChecked;
                HomeActivity.adapter_update();
            }
        });


    }
}
