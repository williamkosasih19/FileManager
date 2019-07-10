package com.williamkosasih.filemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Clipboard_Activity extends AppCompatActivity {

    private Button clear_btn;
    private List<MyFileItem> copy_items;
    private ListView lv_clipboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard_);
        lv_clipboard = findViewById(R.id.lv_clipboard);
        copy_items = HomeActivity.copy_items;
        clear_btn = findViewById(R.id.clear_btn);
        update_adapter();

    }

    private void update_adapter() {
        String[] sar = new String[copy_items.size()];
        int index=0;
        for(MyFileItem mf : copy_items)
            sar[index++]= mf.getThisfile().getAbsolutePath();
        lv_clipboard.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sar));
        lv_clipboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                copy_items.remove(copy_items.get(position));
                update_adapter();
            }
        });

    }

    public void clear_list(View view) {
        copy_items.clear();
        update_adapter();
    }
}
