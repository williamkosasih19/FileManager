package com.williamkosasih.filemanager;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    private String Scurpath;
    public static File curpath;
    private static GridView foldergv;
    public static List<MyFileItem> selectedItems;
    private Menu activbar;
    public static int sort_mode = 1;
    public static List<MyFileItem> copy_items;
    private MenuItem deleteButton;
    private MenuItem copyButton;
    private MenuItem pasteButton;
    private MenuItem clipboardButton;
    private MenuItem newFolderButton;
    private MenuItem renameButton;
    static public String data_dir;
    public static Context appcontext;
    public static ThumbLoader tl=null;
    private MenuItem aboutButton;
    public static boolean hideHiddenFiles = true;
    NavController navController;
    NavigationView navView;
    private DrawerLayout drawerLayout;
    public static FragmentManager mainFragmentManager;


    @Override
    public void onBackPressed() {
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
        super.onCreate(savedInstanceState);

        mainFragmentManager = getSupportFragmentManager();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        appcontext=this;

        setContentView(R.layout.activity_home);


        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.newfolder_btn:
                        Toast.makeText(getApplicationContext(), "yay I'm clicked", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);

        selectedItems = new ArrayList<>();
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

        foldergv.setNumColumns(4);

        Scurpath= Environment.getExternalStorageDirectory().getAbsolutePath();
        curpath = new File(Scurpath);
        adapter_update();

        foldergv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!selectedItems.isEmpty())
                {
                    select_items(parent,position);
                }
                else

                {
                    MyFileItem temp;
                    temp=(MyFileItem)parent.getItemAtPosition(position);
                    selectedItems.clear();

                    if(temp.getThisfile().isDirectory())            //redundant
                    {
                        // if I press on one of the folder's item, then I go to the next directory
                        // then forget about all of the selected files before, so remove delete button from the menu item
                        deleteButton.setVisible(false);
                        // change current path to the next directory's path
                        curpath=temp.getThisfile();
                        // update adapter
                        adapter_update();
                    }
                    else    //this means i clicked on a file
                    {

                        File current_file = temp.getThisfile();

                            Intent me = new Intent(Intent.ACTION_VIEW);
                            me.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt"));
                            Uri uri = FileManagerProvider.getUriForFile(getApplicationContext(),getPackageName()+".provider", current_file);

                            me.setData(uri);
                            me.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(me,"Open File"));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        activbar=menu;
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        deleteButton = menu.findItem(R.id.delete_btn);
        copyButton = menu.findItem(R.id.copy_btn);
        pasteButton = menu.findItem(R.id.paste_btn);
        clipboardButton = menu.findItem(R.id.clipboard_btn);
        newFolderButton = menu.findItem(R.id.newfolder_btn);
        renameButton = menu.findItem(R.id.rename_btn);
        aboutButton = menu.findItem(R.id.about_btn);
        MenuItem settingsButton = menu.findItem(R.id.settings_btn);

        settingsButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });


        deleteButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                for (MyFileItem mf : selectedItems)
                {
                    recursive_delete(mf.thisfile);
                }
                selectedItems.clear();
                adapter_update();
                check_ribbon();
                return true;
            }
        });

        aboutButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                   @Override
                                                   public boolean onMenuItemClick(MenuItem menuItem) {

                                                       Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                                                       startActivity(intent);
                                                       return true;
                                                   }
                                               }
        );

        copyButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List toRemove = new ArrayList();
                for (MyFileItem mf : selectedItems)
                {
                    copy_items.add(mf);
                    toRemove.add(mf);
                }
                selectedItems.removeAll(toRemove);
                adapter_update();
                check_ribbon();
                return true;
            }
        });

        pasteButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CopyClass.copyRec(copy_items, curpath);
                copy_items.clear();
                adapter_update();
                check_ribbon();

                return true;
            }
        });

        clipboardButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(),Clipboard_Activity.class);
                startActivity(intent);
                check_ribbon();
                return true;
            }
        });

        newFolderButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createinputdialog create = new createinputdialog();
                create.setMode(0);
                create.show(getSupportFragmentManager(),"New Folder");
                adapter_update();
                return true;
            }
        });

        renameButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createinputdialog create = new createinputdialog();
                create.setMode(1);
                create.setPretext(selectedItems.get(0).getThisfile().getName());
                create.show(getSupportFragmentManager(),"Rename File");
                //adapter_update();
                selectedItems.clear();
                return true;
            }
        });

        return true;
    }

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
        foldergv.setAdapter(new WilliamGridView(appcontext,new MyFileItem(curpath)));
    }

    public void check_ribbon()
    {
        if(copy_items.isEmpty())
        {
            pasteButton.setVisible(false);
            clipboardButton.setVisible(false);
        }
        else
        {
            pasteButton.setVisible(true);
            clipboardButton.setVisible(true);
        }
        if (selectedItems.isEmpty())
        {
            deleteButton.setVisible(false);
            copyButton.setVisible(false);
            renameButton.setVisible(false);
        }
        else
        {
            if (selectedItems.size() <= 1)
                renameButton.setVisible(true);
            else
                renameButton.setVisible(false);
            deleteButton.setVisible(true);
            copyButton.setVisible(true);

        }

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        check_ribbon();
    }

    private void select_items(AdapterView<?> parent, int position)
    {
        MyFileItem testFile;
        testFile = (MyFileItem) parent.getItemAtPosition(position);
        LinearLayout gv_layout = findViewById(R.id.android_custom_gridview_toad);

        if (testFile.isSelected())
        {
            parent.getChildAt(position-parent.getFirstVisiblePosition()).setBackgroundColor(Color.TRANSPARENT);
            testFile.setSelected(false);
            selectedItems.remove(parent.getItemAtPosition(position));

        }
        else
        {
            parent.getChildAt(position-parent.getFirstVisiblePosition()).setBackgroundColor(Color.parseColor("#4CA99F"));
            selectedItems.add((MyFileItem) parent.getItemAtPosition(position));
            testFile.setSelected(true);
        }
        check_ribbon();
    }


}
