package com.williamkosasih.filemanager;

import java.util.Comparator;


// Don't forget to implement more sorting stuffs!!!
// Plus hide foldersss
// OMG i keep forgetting
// remember scroll location
// fix background color
// focus on textbox when createNewFolderDialog is called
// create settings page
// fix copy mechanism
// check if recursive delete is really needed >:(
// add navigation page on the left side

public class MyFileItemCompare implements Comparator<MyFileItem> {
    private int mode;

    public MyFileItemCompare(int mode) {
        this.mode=mode;
    }

    public int compare(MyFileItem i1, MyFileItem i2) {
        int toReturn;
        switch (mode) {
            case 1:
                toReturn = i1.getThisfile().getName().toLowerCase()
                        .compareTo(i2.getThisfile().getName().toLowerCase());
                break;
            case 2:
                if (i1.getThisfile().lastModified() < i2.getThisfile().lastModified())
                    toReturn = 0;
                else
                    toReturn = -1;

                break;
                default:
                    toReturn = 0;
                    break;
        }
        return toReturn;
    }
}
