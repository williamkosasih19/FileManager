package com.williamkosasih.filemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.williamkosasih.filemanager.AuxUtils;

import com.williamkosasih.filemanager.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WilliamGridView extends BaseAdapter{

    private Context mContext;
    private MyFileItem curdir;
    private List<MyFileItem> myitemlist;
    private File[] dirlist;

    public WilliamGridView(Context mContext,MyFileItem curdir)
    {
        this.mContext=mContext;
        this.curdir=curdir;
        dirlist=curdir.getThisfile().listFiles();
        int size = dirlist.length;
        myitemlist = new ArrayList<>();
        for(int i=0;i<size;i++)
            myitemlist.add(new MyFileItem(dirlist[i]));
        MyFileItemCompare mfc = new MyFileItemCompare(1);
        Collections.sort(myitemlist,mfc);

    }

    @Override
    public int getCount()
    {
        return dirlist.length;
    }

    @Override
    public Object getItem(int index)
    {
        return myitemlist.get(index);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(final int index, View convertView, ViewGroup parent)
    {
        View mygridview;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
        {

            mygridview = new View(mContext);
            mygridview = inflater.inflate(R.layout.gridview_layout_custom_toad,null);

        }
        else
        {
            mygridview= (View) convertView;
        }

        TextView mytextview = (TextView) mygridview.findViewById(R.id.android_custom_gridview_toad_text);
        final ImageView myimageview = (ImageView) mygridview.findViewById(R.id.android_custom_gridview_toad_image);

        final int setsize = 10;

        String textname = myitemlist.get(index).getThisfile().getName();
        String toprint = textname.substring(0,Math.min(textname.length(),10));
        if(textname.length()>=setsize)
            toprint+="...";
        mytextview.setText(toprint);
        if(myitemlist.get(index).getThisfile().isDirectory())
        {
            myimageview.setImageResource(R.drawable.folder);
        }
        else
        {
            String filename=textname;
            String extname = filename.substring(filename.lastIndexOf(".")+1);
            extname=extname.toLowerCase();
            //mytextview.setText(mytextview.getText()+"|"+extname);


            int resid = mContext.getResources().getIdentifier((extname),"drawable",mContext.getPackageName());
            if(!(resid==0))
            {
                myimageview.setImageResource(resid);
            }
            else
                myimageview.setImageResource(R.drawable.file);
            if((extname.equals("jpeg"))||(extname.equals("png"))||(extname.equals("jpg")))
            {
                    while(HomeActivity.tl!=null);
                        HomeActivity.tl = new ThumbLoader(myimageview, 0);
                        HomeActivity.tl.execute(myitemlist.get(index).getThisfile());
                        HomeActivity.tl = null;


            }
            else if(extname.equals("mov")||extname.equals("mp4")||extname.equals("mkv")||extname.equals("avi"))
            {
                while(HomeActivity.tl!=null);
                    HomeActivity.tl = new ThumbLoader(myimageview, 1);
                    HomeActivity.tl.execute(myitemlist.get(index).getThisfile());
                    HomeActivity.tl = null;

            }
            else
            {

            }


        }

        return mygridview;

    }
}
