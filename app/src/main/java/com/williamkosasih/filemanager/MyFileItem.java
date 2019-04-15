package com.williamkosasih.filemanager;

import java.io.File;

public class MyFileItem {
    File thisfile;
    boolean selected;

    public MyFileItem(File thisfile)
    {
        this.thisfile=thisfile;
        selected=false;
    }
    public File getThisfile()
    {
        return thisfile;
    }
    public boolean isSelected()
    {
        return selected;
    }
    public void setSelected(boolean selected)
    {
        this.selected=selected;
    }
}
