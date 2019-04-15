package com.williamkosasih.filemanager;

import java.util.Comparator;

public class MyFileItemCompare implements Comparator<MyFileItem> {
    private int mode;
    public MyFileItemCompare(int mode)
    {
        this.mode=mode;
    }
    public int compare(MyFileItem i1, MyFileItem i2)
    {
        int to_return;
        switch(mode)
        {
            case 1:
                to_return= i1.getThisfile().getName().toLowerCase().compareTo(i2.getThisfile().getName().toLowerCase());
                break;
                default:
                    to_return=0;
                    break;
        }
        return to_return;
    }
}
