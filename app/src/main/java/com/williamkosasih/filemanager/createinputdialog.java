package com.williamkosasih.filemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

public class createinputdialog extends DialogFragment
{
    private String pretext="";
    private int mode;
    public void setPretext(String pretext)
    {
        this.pretext=pretext;
    }
    public void setMode(int mode)
    {
        this.mode=mode;
    }
    @Override
    public Dialog onCreateDialog(Bundle pass_inf)
    {
        AlertDialog.Builder my_builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inputdialog_inflater = getActivity().getLayoutInflater();
        final View v = inputdialog_inflater.inflate(R.layout.input_dialog,null);
        EditText input_text = (EditText)v.findViewById(R.id.input_et);
        input_text.setText(pretext);

        my_builder.setView(v).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText input_text = (EditText)v.findViewById(R.id.input_et);
                String Sinput_text = input_text.getText().toString();
                String final_text = HomeActivity.curpath.toString()+"/"+Sinput_text;
                switch(mode)
                {
                    case 0:
                        File tocreate = new File(final_text);
                        tocreate.mkdirs();
                        break;
                    case 1:
                        File source = HomeActivity.selected_items.get(0).getThisfile();
                        File target = new File(final_text);
                        source.renameTo(target);
                        break;
                }
                HomeActivity.adapter_update();

            }
        });

        return my_builder.create();
    }
}
