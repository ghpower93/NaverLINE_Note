package com.example.park.noteped;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class SampleData {
    private Bitmap picture;
    private String title;
    private String context;

    public SampleData(Bitmap picture, String title, String context){
        this.picture = picture;
        this.title = title;
        this.context = context;
    }

    public Bitmap getPicture()
    {

        return this.picture;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getContext()
    {
        return this.context;
    }
}
