package com.williamkosasih.filemanager;

import android.app.Activity;
import android.content.Intent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CopyClass {
    public static Boolean remember = false;
    public static String action = "notskip";
    public static String newName;

    static private Path destinationPath;

    public static void copyRec(List<MyFileItem> src, File dst) {
        for (MyFileItem curItem : src) {
            // check if a file with that path exists in the destination
            // if it does then launch a dialog
            destinationPath =
                    Paths.get(dst.getPath() + "/" + curItem.getThisfile().getName());
            if (Files.exists(destinationPath)) {
                if (remember == false) {
                    CopyDialog copydialog = new CopyDialog();

                    Intent intent = new Intent(HomeActivity.appcontext, copydialog.getClass());

                    Activity homeActivity = (Activity) HomeActivity.appcontext;
                    intent.putExtra("originalFileName", curItem.getThisfile().getName());
                    intent.putExtra("src", curItem.getThisfile().toString());
                    intent.putExtra("dest", dst.toString());

                    homeActivity.startActivityForResult(intent, HomeActivity.resultCopyDialogDone);
                }

                if (action.equals("skip")) {
                    continue;
                } else if (action.equals("rename")) {
                    destinationPath =
                            Paths.get(dst.getPath() + "/" +
                                    AuxUtils.resolveFileNameConflict(curItem.getThisfile(), dst));
                }
            }
        }
        return;
    }

//    public static void ProcessCopy()
//    {
//        try {
//            Files.copy(curItem.getThisfile().toPath(), destinationPath);
//        } catch (IOException e) {
//
//        }
//    }
}
