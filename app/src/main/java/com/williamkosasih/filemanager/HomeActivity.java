package com.williamkosasih.filemanager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private String Scurpath;
    public static File curpath;
    private static GridView foldergv;
    private MenuItem delete_btn;
    private Menu activbar;
    public static List<MyFileItem> selected_items;
    public static List<MyFileItem> copy_items;
    private MenuItem copy_btn;
    private MenuItem paste_btn;
    private MenuItem clipboard_btn;
    private MenuItem newfolder_btn;
    private MenuItem rename_btn;
    private MenuItem about_btn;
    static public String data_dir;
    public static Context appcontext;
    public static ThumbLoader tl=null;



    /*public class ViewStore extends ViewModel
    {
        GridView foldergv;
        public ViewStore()
        {
            super();
            foldergv=(GridView)findViewById(R.id.gridview);
        }

    }*/

    //ViewStore mvmodel;

    public static class inputdialog extends DialogFragment
    {

    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        if(tl!=null)
            tl.cancel(true);
        if(curpath.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath()))
        {
            finish();
        }
        else
        {
            curpath = curpath.getParentFile();
            adapter_update();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        appcontext=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        selected_items = new ArrayList<>();
        copy_items = new ArrayList<>();

        //check if android/data/com.williamksoasih.file_manager is created

        data_dir = Environment.getExternalStorageDirectory()+"/Android/data/com.william_kosasih.file_manager/";

        File check_dir= new File(data_dir);
        if(!check_dir.exists())
            check_dir.mkdir();


        final SwipeRefreshLayout swipe_refresh = findViewById(R.id.main_swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter_update();
                swipe_refresh.setRefreshing(false);
            }
        });

        foldergv = findViewById(R.id.gridview);
        //ViewStore mvmodel = ViewModelProviders.of(this).get(ViewStore.class);
        //foldergv =((ViewStore) mvmodel).foldergv;
        foldergv.setNumColumns(4);

        Scurpath= Environment.getExternalStorageDirectory().getAbsolutePath();
        curpath = new File(Scurpath);
        adapter_update();

        foldergv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!selected_items.isEmpty())
                {
                    select_items(parent,position);
                }
                else

                {
                    MyFileItem temp;
                    temp=(MyFileItem)parent.getItemAtPosition(position);
                    //for(int i=0;i<selected_items.size();i++)
                    //selected_items.get(i).setSelected(false);
                    selected_items.clear();

                    if(temp.getThisfile().isDirectory())            //redundant
                    {
                        // if I press on one of the folder's item, then I go to the next directory
                        // then forget about all of the selected files before, so remove delete button from the menu item
                        delete_btn.setVisible(false);
                        // change current path to the next directory's path
                        curpath=temp.getThisfile();
                        // update adapter
                        adapter_update();
                    }
                    else    //this means i clicked on a file
                    {

                        File current_file = temp.getThisfile();
                        String filename= temp.getThisfile().getName();
                        String ext = filename.substring(filename.lastIndexOf(".")+1);
                        //Toast.makeText(getApplicationContext(),ext,Toast.LENGTH_LONG).show();
                        //if((ext.equals("txt"))) // just for testing...
                        //{

                            Intent me = new Intent(Intent.ACTION_VIEW);
                            me.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt"));
                            Uri uri = FileManagerProvider.getUriForFile(getApplicationContext(),getPackageName()+".provider", current_file);

                            me.setData(uri);
                            me.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(me,"Open File"));
                            //me.setDataAndType(Uri.fromFile(current_file),MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext));
                            //me.putExtra(MediaStore.EXTRA_OUTPUT,Uri.parse(temp.getThisfile().getAbsolutePath()));
                            //me.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            //startActivity(Intent.createChooser(me,"Open File"));
                        //}
                    }
                }


            }
        });


        // this happens when I long clicked on one of the items, this will invoke select_items which will highlight
        // the selection, with that green shade

        foldergv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                select_items(parent,position);




                return true;
            }
        });




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        activbar=menu;
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        delete_btn = menu.findItem(R.id.delete_btn);
        copy_btn = menu.findItem(R.id.copy_btn);
        paste_btn = menu.findItem(R.id.paste_btn);
        clipboard_btn = menu.findItem(R.id.clipboard_btn);
        newfolder_btn = menu.findItem(R.id.newfolder_btn);
        rename_btn = menu.findItem(R.id.rename_btn);
        about_btn = menu.findItem(R.id.about_btn);


        delete_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // toremove is to delete all the files from the selected_items, why not just clear it? I FORGOT
                // YOU NEED TO CHECK ON THIS LATER!!!!!
                //List toremove = new ArrayList();
                for(MyFileItem mf : selected_items )
                {
                    recursive_delete(mf.thisfile);
                }
                //selected_items.removeAll(toremove);
                // ^ I decided to just clear to whole list...
                selected_items.clear();
                adapter_update();
                check_ribbon();
                return true;
            }
        });

        about_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                 @Override
                                                 public boolean onMenuItemClick(MenuItem menuItem) {

                                                     Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                                                     startActivity(intent);
                                                     return true;
                                                 }
                                             }
        );

        copy_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List toremove = new ArrayList();
                for(MyFileItem mf : selected_items)
                {
                    copy_items.add(mf);
                    toremove.add(mf);
                }
                selected_items.removeAll(toremove);
                adapter_update();
                check_ribbon();
                return true;
            }
        });

        paste_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //notifbuilder.setContentTitle("notify_001").setContentText("File Copy Progress").setSmallIcon(R.drawable.ic_launcher_background).setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // this doesn't work as it's supposed to.
                // still don't know how to fix this, putting the loop inside an async function won't work either -> even more broken
                //
                if(!copy_items.isEmpty())
                {
                    final List<MyFileItem> copylist = new ArrayList<>(copy_items);
                    AsyncTask.execute(new Runnable() {

                        @Override
                        public void run() {

                            String CHANNEL_ID="william_channel_001";
                            NotificationManager notifman = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"WilliamFilemanager",NotificationManager.IMPORTANCE_DEFAULT);
                            notifman.createNotificationChannel(channel);
                            Notification.Builder notif_builder = new Notification.Builder(HomeActivity.this,CHANNEL_ID)
                                    .setContentTitle("File Manager")
                                    .setContentText("Copying Files")
                                    .setSmallIcon(R.drawable.ic_launcher_foreground);


                            int size = copy_items.size();
                            int progress=0;

                            Log.d("william","HEREMAN2");
                            Log.d("william","copyitems = "+copy_items.size());

                    for(MyFileItem mf : copylist)
                    {
                        Log.d("william","HEREMAN3");
                        notif_builder.setProgress(size,progress,false);
                        notifman.notify(1,notif_builder.build());
                        File curfile = mf.getThisfile();
                        File Tocurdir = new File(curpath.toString()+"/"+curfile.getName());
                        if(curpath.toString().equals(Tocurdir.toString()))
                            adapter_update();
                        else
                        {
                                    CopyClass.copyRec(curfile,Tocurdir);
                                    Log.d("william","HEREMAN");
                        }


                        progress++;
                    }
                    notif_builder.setContentText("Copy Complete").setProgress(0,0,false);
                    notifman.notify(1,notif_builder.build());
                    }


                    });
                }



                copy_items.clear();
                adapter_update();
                check_ribbon();

                return true;
            }
        });

        clipboard_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(),Clipboard_Activity.class);
                startActivity(intent);
                check_ribbon();
                return true;
            }
        });

        newfolder_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createinputdialog create = new createinputdialog();
                create.setMode(0);
                create.show(getSupportFragmentManager(),"New Folder");
                //adapter_update();
                return true;
            }
        });

        rename_btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createinputdialog create = new createinputdialog();
                create.setMode(1);
                create.setPretext(selected_items.get(0).getThisfile().getName());
                create.show(getSupportFragmentManager(),"Rename File");
                //adapter_update();
                selected_items.clear();
                return true;
            }
        });

        return true;
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        ViewStore mvmodel = ViewModelProviders.of(this).get(ViewStore.class);
        mvmodel.foldergv=foldergv;

    }*/



    private void recursive_delete(File myfile)
    {
        if(myfile.isDirectory())
        {
            File[] file_array = myfile.listFiles();
            for(File each_file : file_array)
                recursive_delete(each_file);
            myfile.delete();
        }
        else
        {
            while(tl!=null);
            myfile.delete();
        }

    }


    public static void adapter_update()
    {
        MyFileItem mfitem = new MyFileItem(curpath);
        foldergv.setAdapter(new WilliamGridView(appcontext,mfitem));

    }

    public void check_ribbon()
    {
        if(copy_items.isEmpty())
        {
            paste_btn.setVisible(false);
            clipboard_btn.setVisible(false);
        }
        else
        {
            paste_btn.setVisible(true);
            clipboard_btn.setVisible(true);
        }
        if(selected_items.isEmpty())
        {
            delete_btn.setVisible(false);
            copy_btn.setVisible(false);
            rename_btn.setVisible(false);
        }
        else
        {
            if(selected_items.size()<=1)
                rename_btn.setVisible(true);
            else
                rename_btn.setVisible(false);
            delete_btn.setVisible(true);
            copy_btn.setVisible(true);

        }

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        check_ribbon();
    }

    private void select_items(AdapterView<?> parent, int position)
    {
        MyFileItem test_file;
        test_file=(MyFileItem) parent.getItemAtPosition(position);
        LinearLayout gv_layout = findViewById(R.id.android_custom_gridview_toad);

        if(test_file.isSelected())
        {
            parent.getChildAt(position-parent.getFirstVisiblePosition()).setBackgroundColor(Color.TRANSPARENT);
            //parent.getSelectedView().setBackgroundColor(Color.WHITE);
            //parent.setBackgroundColor(Color.WHITE);
            //gv_layout.setBackgroundColor(Color.WHITE);
            test_file.setSelected(false);
            selected_items.remove(parent.getItemAtPosition(position));

        }
        else
        {
            parent.getChildAt(position-parent.getFirstVisiblePosition()).setBackgroundColor(Color.parseColor("#4CA99F"));
            //gv_layout.setBackgroundColor(Color.DKGRAY);
            //parent.setBackgroundColor(Color.DKGRAY);
            selected_items.add((MyFileItem) parent.getItemAtPosition(position));
            test_file.setSelected(true);
        }
        check_ribbon();
    }


}
