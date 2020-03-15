package com.example.park.noteped;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;


public class MainActivity extends AppCompatActivity {

        ArrayList<SampleData>  DataList = new ArrayList<SampleData>();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +File.separator+"LineNote";
        String PathImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "LineNoteImage";

        File saveFile = new File(path);
        File saveFile2 = new File(PathImg);
    ImageView listPicture;


        Intent intent;



    //MyAdapter myAdapter=null;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tedPermission();
        this.InitializeNotelist();
    ListView listView = (ListView)findViewById(R.id.listView);
    final MyAdapter myAdapter = new MyAdapter(this,DataList);
    listPicture = findViewById(R.id.picture);
    Drawable alpha = (findViewById(R.id.listView)).getBackground();
    alpha.setAlpha(30);
    listView.setAdapter(myAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
@Override
public void onItemClick(AdapterView parent, View v, int position, long id){



    Intent intent1 = new Intent(
            getApplicationContext(), // 현재화면의 제어권자
            ReadActivity.class); // 다음넘어갈 화면

    // intent 객체에 데이터를 실어서 보내기
    // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
    intent1.putExtra("filetitle",  myAdapter.getItem(position).getTitle());

    startActivity(intent1);
    finish();
}
        });


        }




public void InitializeNotelist()
        {
            DataList = new ArrayList<SampleData>();
          String readcontext=null;




            if(!saveFile.exists()){ // 폴더 없을 경우
                saveFile.mkdir();
            }
            if(!saveFile2.exists()){ // 폴더 없을 경우

                saveFile2.mkdir();
            }


                FileInputStream fis = null;
                FileInputStream fisImg = null;
            Bitmap bm=null;
            Bitmap bm2=null;

            BufferedInputStream buf=null;
            BufferedReader reader= null;
            try {

                  //  saveFile.length()


                  /*  for( File file : listFiles )//리스트에
                    {

                        ListItems.add((file.getName()));//추가 해 줍니다.ㅑ

                    }*/

                    if (!saveFile.exists())
                    {
                        Toast.makeText(getApplicationContext(),
                                "메모를 추가해주세요",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    File[] listFiles =(saveFile.listFiles());

                    listFiles =(saveFile.listFiles());

                    for( File file : listFiles )//리스트에
                    {


                        bm=null;
                        bm2=null;
                        String msgImg=null;

                        msgImg=file.getName().replace(".txt","_1.jpg");
                        fis = new FileInputStream(path + File.separator + file.getName());
                        reader = new BufferedReader(new InputStreamReader(fis));

                       readcontext = reader.readLine();
                        if (readcontext.length()>20) {
                            readcontext = readcontext.substring(0,15);
                        }
                        File booleanFiles2 =new File(PathImg+ File.separator + msgImg);

                       if(booleanFiles2.isFile()) {

                           if (bm2 != null) {
                               bm2.recycle();
                               bm2 = null;
                           }
                            fisImg = new FileInputStream(booleanFiles2.getAbsolutePath());
                            buf=new BufferedInputStream(fisImg);
                        bm = BitmapFactory.decodeStream(buf);
                        if (bm==null)
                        {
                            return;
                        }
                        else {
                            bm2 = resizeBitmapImage(bm, 70);
                        }
                         //  DataList.add(new SampleData(bm2, file.getName().substring(0,file.getName().length() - 4), readcontext));

                           if (bm != bm2) {
                               bm.recycle();
                               bm = null;

                           }

                           DataList.add(new SampleData(bm2, file.getName().substring(0,file.getName().length() - 4), readcontext));



                        }
                        else{
//                           bm.recycle();

                       //   bm=BitmapFactory.decodeResource(getResources(), R.drawable.picture);
                          DataList.add(new SampleData(null, file.getName().substring(0,file.getName().length() - 4), readcontext));

                         // bm=null;

                       }

                        //imageView.setImageBitmap(bm) ;
                       /* fis = new FileInputStream(PathImg+ File.separator+"corona.jpg");
                        buf=new BufferedInputStream(fis);
                        bm = BitmapFactory.decodeStream(buf);
                        bm = resizeBitmapImage(bm,60);
                        */
                   //     DataList.add(new SampleData(bm, file.getName().substring(0,file.getName().length() - 4), readcontext));
                         msgImg=null;
                       /* if (!bm.isRecycled()) {
                            bm.recycle();
                        }*/
                       // DataList.add(new SampleData(bm, file.getName().substring(0,file.getName().length() - 4), readcontext));


                    }

                    fis.close();
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }





        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu) ;

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                 intent = new Intent(this, WriteActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
                finish();
                return true;

            case R.id.action_back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

     void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();



    }



    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution)
    {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height)
        {
            if(maxResolution < width)
            {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        }
        else
        {
            if(maxResolution < height)
            {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }
}
