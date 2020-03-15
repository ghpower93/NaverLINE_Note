package com.example.park.noteped;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_childview, container, false);
        final ImageView imageView = view.findViewById(R.id.WriteImeage);
       // imageView.setImageBitmap(null); //요기당
      if (getArguments() != null) {

            Bundle args = getArguments();
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm=null;
            originalBm = BitmapFactory.decodeFile(args.getString("imgRes"), options);
            imageView.setImageBitmap(originalBm); //요기당
        }
        else{
           imageView.setImageBitmap(null);


         }
        return view;
    }


}