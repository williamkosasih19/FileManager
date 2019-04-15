package com.williamkosasih.filemanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ThumbLoader extends AsyncTask<File,Void,Bitmap> {

    ImageView imgv;
    int type;
    public ThumbLoader(ImageView imgv, int type)
    {
        super();
        this.type=type;
        this.imgv=imgv;
    }
    @Override
    protected Bitmap doInBackground(File[] myfiles)
    {
        Bitmap thumbimage;
        String md5 = AuxUtils.md5(myfiles[0].getAbsolutePath());
        File check_md5 = new File(HomeActivity.data_dir+md5+".jpg");
        if(check_md5.exists())
        {
            thumbimage = BitmapFactory.decodeFile(check_md5.getAbsolutePath());

        }
        else
        {
            if(type==0)
            {



                Bitmap mbmap = BitmapFactory.decodeFile(myfiles[0].getAbsolutePath());
                //Bitmap thumb = Bitmap.createScaledBitmap(mbmap,64,64,false);
                thumbimage = ThumbnailUtils.extractThumbnail(mbmap, 64, 64);


            }
            else
            {
                thumbimage= ThumbnailUtils.createVideoThumbnail(myfiles[0].getAbsolutePath(),MediaStore.Video.Thumbnails.MICRO_KIND);
            }
            try
            {
                FileOutputStream out = new FileOutputStream(check_md5.getAbsolutePath());
                thumbimage.compress(Bitmap.CompressFormat.JPEG,100,out);
            }
            catch (FileNotFoundException ex) {
            }
        }


            return thumbimage;
    }

    @Override
    protected void onPostExecute(Bitmap toset)
    {
        imgv.setImageBitmap(toset);
    }

}
