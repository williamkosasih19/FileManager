package com.williamkosasih.filemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CopyDialog extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        final Intent intent = getIntent();

        File sourceFile = new File(intent.getStringExtra("src"));
        File destinationFile = new File(intent.getStringExtra("dest"));

        boolean rememberAction = intent.getStringExtra("remember").equals("true");
        String action = intent.getStringExtra("action");

        String copyPath = destinationFile.getPath() + "/" + sourceFile.getName();

        intent.putExtra("newFileName", copyPath);

        if (Files.exists(Paths.get(copyPath))) {
            if (rememberAction) {
                if (action.equals("noSkip")) {
                    intent.putExtra("newFileName",
                            AuxUtils.resolveFileNameConflict(sourceFile, destinationFile));
                }
                setResult(RESULT_FIRST_USER, intent);
                finish();
            }
            setContentView(R.layout.copy_dialog);

            TextView originalFileNameText = findViewById(R.id.copy_dialog_original_name_textview);
            final EditText newFileNameEdit = findViewById(R.id.copy_dialog_new_name_edittext);
            final CheckBox rememberCheckBox = findViewById(R.id.copy_dialog_checkbox);
            Button renameButton = findViewById(R.id.copy_dialog_rename_button);
            Button skipButton = findViewById(R.id.copy_dialog_skip_button);

            originalFileNameText.setText(intent.getStringExtra("originalFileName"));
            newFileNameEdit.
                    setText(AuxUtils.resolveFileNameConflict(sourceFile, destinationFile));


            renameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("newFileName", newFileNameEdit.getText().toString());

                    if (rememberCheckBox.isChecked())
                        intent.putExtra("remember", "true");
                    else
                        intent.putExtra("remember", "false");


                    intent.putExtra("action", "noSkip");

                    setResult(RESULT_FIRST_USER, intent);
                    finish();
                }
            });

            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent.putExtra("action", "skip");

                    if (rememberCheckBox.isChecked())
                        intent.putExtra("remember", "true");
                    else
                        intent.putExtra("remember", "false");

                    setResult(RESULT_FIRST_USER, intent);
                    finish();
                }
            });
        } else {
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
