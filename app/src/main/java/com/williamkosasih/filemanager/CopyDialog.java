package com.williamkosasih.filemanager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class CopyDialog extends AppCompatActivity {
    public static String originalFileName;
    public static File src;
    public static File dest;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.copy_dialog);

        TextView originalFileNameText = findViewById(R.id.copy_dialog_original_name_textview);
        final EditText newFileNameEdit = findViewById(R.id.copy_dialog_new_name_edittext);
        final CheckBox rememberCheckBox = findViewById(R.id.copy_dialog_checkbox);
        Button renameButton = findViewById(R.id.copy_dialog_rename_button);
        Button skipButton = findViewById(R.id.copy_dialog_skip_button);

        originalFileNameText.setText(originalFileName);
        newFileNameEdit.setText(AuxUtils.resolveFileNameConflict(src, dest));

        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyClass.newName = newFileNameEdit.getText().toString();
                CopyClass.remember = rememberCheckBox.isChecked();
                CopyClass.action = "rename";

                finish();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyClass.remember = rememberCheckBox.isChecked();
                CopyClass.action = "skip";

                finish();
            }
        });
        return;
    }
}
