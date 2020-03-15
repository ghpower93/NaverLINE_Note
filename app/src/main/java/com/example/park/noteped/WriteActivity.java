package com.example.park.noteped;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WriteActivity extends AppCompatActivity {

    private static String TAG = "WriteActivity";
    private static final int PICK_IMAGE_MULTIPLE = 1;
    private static final int PICK_FROM_CAMERA = 3;

    private File tempFile;
    Button load, save, delete;
   // ImageButton imgButton;
   @SuppressLint("StaticFieldLeak")
   static EditText inputText;
    EditText TitleText;
     CustomAdapter CustomAdapter ;

    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "LineNote";
    String PathImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "LineNoteImage";
    String PathTmpImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "LineTemp_img";

    File saveFile = new File(path);
    File saveFileimg = new File(PathImg);

    File[] listFiles;
    File[] listFilesIMG;

    List<String> ListItems = new ArrayList<>();
    List<String> ListItemsIMG = new ArrayList<>();

    CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
    List SelectedItems = new ArrayList();
    ImageView imageView;
     String imagePath;
     List<String> imagePathList;
    ViewPager pager;
    ImageFragment imageFragment;
    ImageButton imgButton;

    EditText urlText;
    ImageButton urlButton;
  //  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    View.OnClickListener listener2
             =new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            removeimg();

        }

    };

    View.OnClickListener urllistener
            =new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            urlImage(urlText.getText().toString().trim());

        }

    };


    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {


            switch (view.getId()) {
                case R.id.load:
                    Log.i("TAG", "load 진행");
                    showSetup();
                    break;
                case R.id.save:
                    Log.i("TAG", "Save 진행");
                    showPopup();
                    break;

                case R.id.delete:
                    Log.i("TAG", "delete 진행");

                    showDelete();
                    break;

            }
        }
    };


    void showPopup() {

        ListItems = new ArrayList<>();
        items = ListItems.toArray(new String[ListItems.size()]);

        final EditText editname = new EditText(this);
        editname.setText(TitleText.getText());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메모 저장");
        builder.setMessage("메모이름을 입력하시오.");
        builder.setView(editname);

        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Log.i("TAG", "save 진행");
                FileOutputStream fos = null;

                try {
                    if (!(editname.getText().toString().contains(".txt"))) {

                        File saveFile2 = new File(path, editname.getText() + ".txt");


                        FileWriter wr = new FileWriter(saveFile2); //두번째 파라미터 true: 기존파일에 추가할지 여부를 나타냅니다.
                        PrintWriter writer = new PrintWriter(wr);
                        writer.println(inputText.getText().toString());

                        writer.close();

                        boolean CP=false;
                       for(int i=0; i<CustomAdapter.getCount();i++) {
                          CP = MakeCache( CustomAdapter.getItem(i), editname.getText().toString(), i + 1);
                       }
                        Toast.makeText(getApplicationContext(), editname.getText() + ".txt 저장 완료", Toast.LENGTH_LONG).show();

                       if(CP==true)
                       {
                           Toast.makeText(getApplicationContext(), editname.getText() + " 사진이 저장되었습니다", Toast.LENGTH_SHORT).show();

                       }
                    } else {
                        Toast.makeText(getApplicationContext(), editname.getText() + " 불가능한 파일 이름입니다.", Toast.LENGTH_LONG).show();

                    }
                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), e.toString() + " error", Toast.LENGTH_LONG).show();

                    Log.i("TAG", e.toString());
                    inputText.setText(e.toString());

                    e.printStackTrace();
                } finally {
                   try {
                        if (fos != null) fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //    dialog.dismiss();     //닫기
                }

            }
        });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();

    }

    void showSetup() {
        //       final List<String> ListItems = new ArrayList<>();
        //      Log.v(TAG, saveFile.toString());

        ListItems = new ArrayList<>();

        listFiles = (saveFile.listFiles());
        listFilesIMG = (saveFileimg.listFiles());

        SelectedItems = new ArrayList();
        ListItemsIMG = new ArrayList<>();


        for (File file : listFiles)//리스트에
        {

            ListItems.add((file.getName()));//추가 해 줍니다.
            ListItemsIMG.add((file.getName().replace(".txt", "")));//추가 해 줍니다.

        }
        items = ListItems.toArray(new String[ListItems.size()]);
        if (!saveFile.exists()) { // 폴더 없을 경우
            saveFile.mkdirs();
            Toast.makeText(getApplicationContext(), saveFile.toString(), Toast.LENGTH_LONG).show();
            //  폴더 생성
        }


        if (items.length == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("알림").setMessage("파일이 존재하지 않음. \n 파일을 저장해 주세요.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Log.i("msg", "alert 종료");

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {


            int defaultItem = 0;
            SelectedItems.add(defaultItem);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("파일 목록");
            builder.setSingleChoiceItems(items, defaultItem,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SelectedItems.clear();
                            SelectedItems.add(which);
                        }
                    });
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String msg = null;
                            String msgImg = null;
                           // imageView = (ImageView) findViewById(R.id.WriteImeage);
                            //    imageView.setImageBitmap(findViewById(R.drawable.pictu
                            //    re3)) ;
                           // imageView.setImageBitmap(null);
                           // imageView.destroyDrawingCache();

                            for (File file : listFilesIMG)
                            {

                            }//리스트에


                            if (!SelectedItems.isEmpty()) {
                                int index = (int) SelectedItems.get(0);
                                if (ListItems.get(index).substring(ListItems.get(index).length() - 4, ListItems.get(index).length()).contains(".txt")) {
                                    msg = ListItems.get(index);
                                    msgImg = ListItemsIMG.get(index);
                                }
                            }


                            FileInputStream fis = null;
                            FileInputStream fisimg = null;

                            try {
                                fis = new FileInputStream((path + "/" + msg));
                                byte[] data = new byte[fis.available()];
                                while (fis.read(data) != -1) {
                                }

                                inputText.setText(new String(data));
                                TitleText.setText(msg.replace(".txt", ""));

                                // File image = File.createTempFile(msgImg, ".jpg", storageDir);

                                Toast.makeText(getApplicationContext(),
                                        "File Load\n" + msg, Toast.LENGTH_SHORT)
                                        .show();


                                try {
                                    int length = 0;
                                    for(int i=0;i<listFilesIMG.length;i++)
                                    {
                                        if(listFilesIMG[i].getName().contains(msgImg))
                                            length++;
                                    }


                                   CustomAdapter.clear();
                                    for (int i = 0; i < length; i++) {
                                        saveImage(msgImg,i+1);

                                    }
                                }
                                catch (Exception e) {

                                    e.printStackTrace();
                                  /*  StringWriter errors =new StringWriter();
                                    e.printStackTrace(new PrintWriter(errors));
                                    Log.i("TAG", errors.toString());
                                    inputText.setText(errors.toString());*/

                                }
                            }
                            catch (Exception e) {

                                e.printStackTrace();

                            } finally {
                                try {
                                    if (fis != null) fis.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }

    }

    void showDelete() {
        ListItems = new ArrayList<>();
        ListItemsIMG = new ArrayList<>();

        SelectedItems = new ArrayList();
        listFiles = (saveFile.listFiles());
        listFilesIMG = (saveFileimg.listFiles());

        Log.v(TAG, saveFile.toString());

        for (File file : listFiles)//리스트에
        {
            ListItems.add((file.getName()));//추가 해 줍니다.
            ListItemsIMG.add((file.getName().replace(".txt", "")));//추가 해 줍니다.
        }
        items = ListItems.toArray(new String[ListItems.size()]);

        if (items.length == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("알림").setMessage("파일이 존재하지 않음.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Log.i("msg", "alert 종료");
                }
            });

            builder.show();

        } else {


            //    final List SelectedItems  = new ArrayList();
            int defaultItem = 0;
            SelectedItems.add(defaultItem);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("파일 목록");
            builder.setSingleChoiceItems(items, defaultItem,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SelectedItems.clear();
                            SelectedItems.add(which);
                        }
                    });
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String msg = null;
                            String msgImg = null;

                            for (File file : listFilesIMG)
                            {

                            }


                            if (!SelectedItems.isEmpty()) {
                                int index = (int) SelectedItems.get(0);
                                msg = ListItems.get(index);
                                msgImg = ListItemsIMG.get(index);
                            }



                            FileInputStream fis = null;


                            try {
                                File file = new File(path + "/" + msg);
                               // File fileimg = new File(PathImg + "/" + msgImg);

                                int length=0;
                                for(int i=0;i<listFilesIMG.length;i++)
                                {
                                    if(listFilesIMG[i].getName().contains(msgImg))
                                        length++;
                                }


                                boolean b = file.delete();


                                if (b) {
                                    Toast.makeText(WriteActivity.this, "File Delete\n" + msg, Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(WriteActivity.this, "Fail Delete" + msg + "\n" + "Please check file", Toast.LENGTH_SHORT).show();
                                }

                                //  CustomAdapter.clear();
                                boolean C =false;
                                for (int i = 0; i < length; i++) {
                                  C= savedelete(msgImg,i+1);

                                }

                                if(C==true) {
                                    Toast.makeText(WriteActivity.this, "IMG Delete\n" + msgImg + ".jpg", Toast.LENGTH_SHORT).show();
                                }


                            } catch (Exception e) {

                                e.printStackTrace();

                            } finally {
                                try {
                                    if (fis != null) fis.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        // MainActivity.tedPermission();
        imageView = findViewById(R.id.WriteImeage);
        imgButton= (ImageButton) findViewById(R.id.imgBtn);
        Drawable alpha = (findViewById(R.id.pager)).getBackground();
        alpha.setAlpha(80);
        tempFile=null;


        pager= (ViewPager)findViewById(R.id.pager);
        urlButton=findViewById(R.id.urlBtn);
        urlText=findViewById(R.id.URLText);

        if (!saveFile.exists()) { // 폴더 없을 경우
            saveFile.mkdirs();
        }

       // imgButton = (ImageButton)findViewById(R.id.imgBtn);

        load = (Button) findViewById(R.id.load);
        save = (Button) findViewById(R.id.save);
        delete = (Button) findViewById(R.id.delete);
        inputText = (EditText) findViewById(R.id.inputText);
        TitleText = (EditText) findViewById(R.id.TitleText);

        load.setOnClickListener(listener);
        save.setOnClickListener(listener);
        delete.setOnClickListener(listener);
        imgButton.setOnClickListener(listener2);

        urlButton.setOnClickListener(urllistener);







        ///https://dev-imaec.tistory.com/13 다시보기.
       imageFragment = new ImageFragment();
       CustomAdapter = new CustomAdapter(getSupportFragmentManager());

        // ViewPager와  FragmentAdapter 연결
        pager.setAdapter(CustomAdapter);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, WriteActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
                finish();
                return true;

            case R.id.action_back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
                finish();
                return true;

            case R.id.CameraCreate:
                takePhoto();
                // finish();
                return true;

            case R.id.ImageCreate:

                goToAlbum();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void goToAlbum() {


        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");


        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("data",false);
        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }
        try {
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null) {

                int count=1;


                imagePathList = new ArrayList<>();
                imagePathList.clear();
                if(data.getClipData() != null){

                     count = data.getClipData().getItemCount();

                    tempFile=null;
                    for (int i=0; i<count; i++){

                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        getImageFilePath(imageUri);

                        tempFile = new File(imagePathList.get(i));
                       setImage(tempFile);



                    }
                    Toast.makeText(this,"이미지 "+count+"장 불러오기 완료" , Toast.LENGTH_LONG).show();
       }
                else if(data.getData() != null){
                    tempFile=null;;
                    Uri imgUri = data.getData();

                   getImageFilePath(imgUri);
                    tempFile = new File(imagePathList.get(0));

                    setImage(tempFile);

                }



            }

        else if(requestCode ==PICK_FROM_CAMERA)
    {

        setImage(tempFile);
    }


        else
    {
        Toast.makeText(this, "Error",
                Toast.LENGTH_LONG).show();
    }
}  catch (Exception e) {
            e.printStackTrace();

    }
        super.onActivityResult(requestCode, resultCode, data);
}
    public void getImageFilePath(Uri uri) {


        File file = new File(uri.getPath());
        String filePath = file.getPath().split(":")[1];

        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        String selection =  MediaStore.Images.Media._ID+"="+filePath;

        imagePath=null;
        //imagePathList.clear();

        Log.i("TAG", filePath);

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.getContentUri("external"),filePathColumn, selection, null, null);
        if (cursor == null) {
            imagePath = uri.getPath();
            imagePathList.add(imagePath);
            cursor.close();
        }

            else {
            if(cursor.moveToFirst()) {

                int idx = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(idx);



            }
            else{
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                imagePath = cursor.getString(idx);

            }

            Log.i("TAG", imagePath);
            cursor.close();
            imagePathList.add(imagePath);



        }
    }

    private void setImage(File tmpf) { //갤러리에서 받아온 이미지 넣기.

Toast.makeText(this,tmpf.getAbsolutePath(),Toast.LENGTH_SHORT).show();
       imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imgRes", tmpf.getAbsolutePath());
        imageFragment.setArguments(bundle);
        CustomAdapter.addItem(imageFragment);
        CustomAdapter.notifyDataSetChanged();


    }



    private void removeimg() { //갤러리에서 받아온 이미지 넣기.
    try {
        imageFragment = new ImageFragment();
        CustomAdapter.notifyDataSetChanged();
        pager= (ViewPager)findViewById(R.id.pager);


        int length=CustomAdapter.getCount();
        if (pager.getCurrentItem()>-1 && pager.getCurrentItem() <length) {
            int number =pager.getCurrentItem();
            imageFragment = (ImageFragment) CustomAdapter.getItem(number);
          //
            CustomAdapter.remove(number);
            CustomAdapter.notifyDataSetChanged();
            number =pager.getCurrentItem();
            pager.setCurrentItem(number+ 1);

            Toast.makeText(this, number+1 +"번째 이미지 삭제 완료", Toast.LENGTH_SHORT).show();


        }
    }
    catch (Exception e)
    {
        e.printStackTrace();

    }
    }



    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            Uri photoUri = Uri.fromFile(tempFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);


            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(photoUri);
            sendBroadcast(mediaScanIntent);


        }
    }

    private File createImageFile() throws IOException {

        // 이미지 파일 이름

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "CameraImageFile_" + timeStamp;



       File storageDir = new File(PathTmpImg);

        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }
    public boolean MakeCache(Fragment fr, String filename , int picnum) { //사진 저장
    boolean CP=false;
    try{

            File f = new File(PathImg);
            if (!f.isDirectory()) f.mkdirs();

        Bundle args = fr.getArguments();
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(args.getString("imgRes"), options);
          FileOutputStream fos;
            if (bitmap != null) {
                try {
                    fos = new FileOutputStream(PathImg + "/" + filename +"_"+picnum+ ".jpg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    CP=true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    catch (Exception e){
        e.printStackTrace();

    }

    return CP;
}

    public void saveImage( String msgImg , int picnum) { //사진 저장
        File file = new File(PathImg + "/" + msgImg + "_" + picnum + ".jpg");

        imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imgRes", file.getAbsolutePath());
        imageFragment.setArguments(bundle);
        CustomAdapter.addItem(imageFragment);
        CustomAdapter.notifyDataSetChanged();


    }

    public boolean savedelete( String msgImg , int picnum) { //사진 저장
        File fileimg = new File(PathImg + "/" + msgImg + "_" + picnum + ".jpg");

        boolean C = fileimg.delete();

        if (C) {
        Log.i("delete","삭제완료");
        }

            return C;
    }

    public  void urlImage(String urladdr) { //사진 저장


        if(urladdr.contains("http") ) {

            new ImageDownload().execute(urladdr);
        }
        else{

            Toast.makeText(getApplicationContext(),"올바른 주소를 입력하세요",Toast.LENGTH_SHORT).show();
            urlText.setText("");


            return;
        }



    }
    private class ImageDownload extends AsyncTask<String, Void, Void> {


        private String fileName;



        @Override
        protected Void doInBackground(String... params) {

            String savePath = PathTmpImg;

            File dir = new File(savePath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            //파일 이름 :날짜_시간PathImg
            int i=1;
            Date day = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String date =String.valueOf(sdf.format(day));
            fileName ="URLImageFile_"+date +"_"+i;


            String fileUrl = params[0];



            while (new File(savePath + "/" + fileName).exists() == false) {
            if(new File(savePath + "/" + fileName).exists() == false)
            {
                break;
            }
                    ++i;
                fileName ="URLImageFile_"+date +"_"+i;
            }

            String localPath = savePath + "/" + fileName + ".jpg";
            URL imgUrl;
            HttpURLConnection conn;


            try {

                try {
                     imgUrl = new URL(fileUrl);
                    conn = (HttpURLConnection)imgUrl.openConnection();

                }
                catch (Exception e)
                {
                    fileName=null;
                    return null;
                }

                //서버와 접속하는 클라이언트 객체 생성
                if(conn.getUseCaches()==false)
                {
                    fileName=null;
                    return null;
                }
                int len = conn.getContentLength();

                    if(len<=0)
                        {
                            fileName=null;
                            return null;
                        }
                    byte[] tmpByte = new byte[len];

                InputStream is=null;
                //입력 스트림을 구한다
             try{
                is = conn.getInputStream();

             } catch (IOException exception) {


                fileName=null;
                 return null;

             }
                File file = new File(localPath);

                FileOutputStream fos = new FileOutputStream(file);
                int read;


                    for (; ; ) {
                        read = is.read(tmpByte);
                        if (read <= 0) {
                            break;
                        }
                        fos.write(tmpByte, 0, read); //file 생성
                    }



                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if(bitmap==null)
                {
                    file.delete();
                    fileName=null;
                    return null;
                }


                is.close();
                fos.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"이미지 임시 저장 실패",Toast.LENGTH_SHORT).show();
                fileName=null;
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try {
                String targetDir = PathTmpImg;

                if (fileName==null)
                {
                    urlText.setText("");
                    Toast.makeText(getApplicationContext(),"이미지 불러오기 실패",Toast.LENGTH_SHORT).show();

                    return;
                }

                File file = new File(targetDir + "/" + fileName + ".jpg");

                if(file.canRead()==false)
                {
                    file.delete();
                    urlText.setText("");
                    Toast.makeText(getApplicationContext(),"이미지 불러오기 실패",Toast.LENGTH_SHORT).show();

                    return;
                }

                if (file.isFile() == true) {
                    imageFragment = new ImageFragment();
                    Bundle bundle = new Bundle();
                    Toast.makeText(getApplicationContext(), urlText.getText().toString().trim()+"\n URL 이미지 추가",Toast.LENGTH_SHORT).show();

                    bundle.putString("imgRes", file.getAbsolutePath());

                    imageFragment.setArguments(bundle);
                    CustomAdapter.addItem(imageFragment);
                    CustomAdapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), urlText.getText().toString().trim()+"\n URL 이미지 추가",Toast.LENGTH_SHORT).show();
                    urlText.setText("");

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"이미지 불러오기 실패",Toast.LENGTH_SHORT).show();
                    urlText.setText("");
                    return;
                }

            }
            catch (Exception e)
            {

                Toast.makeText(getApplicationContext(),"이미지 불러오기 실패",Toast.LENGTH_SHORT).show();
                urlText.setText("");
                return;
            }
        }

    }

}



