package com.example.park.noteped;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

public class ReadActivity extends AppCompatActivity {

    TextView TextRead;
    String path = (Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_DOWNLOADS+"/LineNote");
    String PathImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "LineNoteImage";

    TextView TitleRead;
    // File saveFile = new File(path);
    String Filen;
    ImageFragment_read imageFragment;
    CustomAdapter_read CustomAdapter;
    File[] listFilesIMG;
    File saveFileimg = new File(PathImg);
    ViewPager pager;
    FrameLayout framekey;
    View LineView;
    ImageView MutiImg;
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
                Intent intent = new Intent(this, WriteActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
                return true;

            case R.id.action_back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        MutiImg=findViewById(R.id. imgMulti);

        TextRead=findViewById(R.id.outputText);
        TitleRead=findViewById(R.id.outputTitle);
    //    imageView = findViewById(R.id.outputImeage);
        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        Filen=intent.getStringExtra("filetitle");
       // MainActivity.tedPermission();
        framekey=findViewById(R.id.FrameKey);
        LineView=findViewById(R.id.outputLine);
        Drawable alpha1 = (findViewById(R.id.outputText)).getBackground();
        alpha1.setAlpha(30);

        pager= (ViewPager)findViewById(R.id.pager_read);
        imageFragment = new ImageFragment_read();
        CustomAdapter = new CustomAdapter_read(getSupportFragmentManager());

        // ViewPager와  FragmentAdapter 연결
        pager.setAdapter(CustomAdapter);




        this.showRead(Filen);
    }

    void showRead(String txtname){
        listFilesIMG = (saveFileimg.listFiles());

        FileInputStream fis = null;
        FileInputStream fisimg = null;

        String txtfinal=txtname+".txt";
        String imgfinal=txtname;
       // int imgcount=0;

        File file = new File(PathImg + "/" + imgfinal+"_1.jpg");
        Bitmap bm=null;



        try{
            fis = new FileInputStream((path + "/" + txtfinal));
            byte[] data = new byte[fis.available()];
            while( fis.read(data) != -1){
            }
            //"Title : "+txtname+"\n\n"+

            TitleRead.setText(txtname);
            TextRead.setText(new String(data));


            if(file.isFile())
            {
                for (File file2 : listFilesIMG)
                {

                }

           //     fisimg = new FileInputStream((PathImg + "/" + imgfinal));

            //bm = BitmapFactory.decodeStream(fisimg) ;

            //    imageView.setImageBitmap(bm) ;
            //    imageView.setVisibility(View.VISIBLE);

                int length = 0;

                for(int i=0;i<listFilesIMG.length;i++)
                {

                    if(listFilesIMG[i].getName().contains(imgfinal))
                    {  length++;

                    }
                }
               // Toast.makeText(getApplicationContext(), String.valueOf(length),Toast.LENGTH_LONG).show();
                CustomAdapter.clear();
                if(length>1)
                {
                    MutiImg.setVisibility(View.VISIBLE);
                }
                else
                {
                    MutiImg.setVisibility(View.GONE);
                }

                for (int i = 0; i < length; i++) {
                   // Toast.makeText(getApplicationContext(), String.valueOf(i),Toast.LENGTH_LONG).show();
                    saveImage(imgfinal,i+1);


                }

            }
            else
            {
                pager.setVisibility(View.GONE);
                framekey.setVisibility(View.GONE);
                LineView.setVisibility(View.GONE);
            }




        }catch(Exception e){

            e.printStackTrace();

        }finally{
            try{ if(fis != null) fis.close(); }catch(Exception e){e.printStackTrace();}
        }

    }

    public void saveImage( String msgImg , int picnum) { //사진 저장
        File file = new File(PathImg + "/" + msgImg + "_" + picnum + ".jpg");

        //Bitmap bm = BitmapFactory.decodeStream(fisimg);
        imageFragment = new ImageFragment_read();
        Bundle bundle = new Bundle();
      //  Toast.makeText(getApplicationContext(), file.getAbsolutePath(),Toast.LENGTH_LONG).show();

        bundle.putString("imgRes_read", file.getAbsolutePath());
        // CustomAdapter.getItemPosition(pager);
        imageFragment.setArguments(bundle);

        CustomAdapter.addItem(imageFragment);
     //   Toast.makeText(getApplicationContext(), "22",Toast.LENGTH_LONG).show();

        //  CustomAdapter.setItem(picnum-1,imageFragment);
        CustomAdapter.notifyDataSetChanged();
      //  Toast.makeText(getApplicationContext(), "3333",Toast.LENGTH_LONG).show();


    }

    }

