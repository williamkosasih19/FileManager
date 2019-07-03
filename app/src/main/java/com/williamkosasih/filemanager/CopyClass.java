package com.williamkosasih.filemanager;

import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CopyClass {
        public static void copyRec(File src, File dst)
        {
            if(src.isDirectory())
            {
                try
                {
                    FileUtils.copyDirectory(src,dst);
                }
                catch(IOException e)
                {
                    //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                try
                {
                    FileUtils.copyFile(src,dst);
                }
                catch(IOException e)
                {
                    Log.d("same file here","same file here");
                    Toast.makeText(HomeActivity.appcontext,"Same file!",Toast.LENGTH_LONG);
                }
            }
        }
    }

