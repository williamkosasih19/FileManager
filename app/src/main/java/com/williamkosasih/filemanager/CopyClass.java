package com.williamkosasih.filemanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CopyClass {
    public static Boolean remember = false;
    public static String action = "notskip";
    public static String newName;
    public static Boolean spinLock = false;

    public static void copyRec(List<MyFileItem> src, File dst) {
        for (MyFileItem curItem : src) {
            // check if a file with that path exists in the destination
            // if it does then launch a dialog
            Path destinationPath = Paths.get(dst.getPath() + "/" + curItem.getThisfile().getName());
            if (Files.exists(destinationPath)) {
                if (remember == false) {
                    CopyDialog copydialog = new CopyDialog();
                    CopyDialog.originalFileName = curItem.getThisfile().getName();
                    CopyDialog.src = curItem.getThisfile();
                    CopyDialog.dest = dst;
                    spinLock = true;
                    Intent intent = new Intent(HomeActivity.appcontext, copydialog.getClass());
                    HomeActivity.appcontext.startActivity(intent);


                } else {
                    newName = AuxUtils.resolveFileNameConflict(curItem.getThisfile(), dst);
                }

                if (action.equals("skip")) {
                    continue;
                }

                try {
                    Files.copy(curItem.getThisfile().toPath(), Paths.get(dst.getPath() + "/" + newName));
                } catch (IOException e) {

                }

            } else {
                try {
                    Files.copy(curItem.getThisfile().toPath(), destinationPath);
                } catch (IOException e) {

                }

            }
        }
        return;
    }
}
