package com.example.park.noteped;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment_read extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_childview_read, container, false);
        final ImageView imageView = view.findViewById(R.id.outputImeage);
       // imageView.setImageBitmap(null); //요기당
      if (getArguments() != null) {

            Bundle args = getArguments();
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm=null;
            originalBm = BitmapFactory.decodeFile(args.getString("imgRes_read"), options);
            imageView.setImageBitmap(originalBm); //요기당
        }
        else{
          //  imageView.setImageBitmap(null); //요기당
        }
        return view;
    }


}