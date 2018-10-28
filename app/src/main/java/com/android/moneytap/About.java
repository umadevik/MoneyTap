package com.android.moneytap;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class About  extends Activity {

     ImageView imageViewIcon;
     TextView scientistName,descriptionView;

     String name,desc,imgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getData();
        initGui();

    }

    private void getData() {

        name = getIntent().getStringExtra("name");
        desc = getIntent().getStringExtra("desc");
        imgUrl = getIntent().getStringExtra("imgUrl");

    }

    private void initGui() {
        imageViewIcon= (ImageView) findViewById(R.id.scientist_image);
        scientistName= (TextView) findViewById(R.id.scientistName_txt);
        descriptionView= (TextView)findViewById(R.id.description_txt);

        if(name!=null){

            scientistName.setText(name);
        }else{

            scientistName.setText("N/A");

        }
        if(desc!=null)
        {
            descriptionView.setText(desc);

        }else{

            descriptionView.setText("N/A");

        }
        if(imgUrl!=null){

            Picasso.get().load(imgUrl).into(imageViewIcon);

        } else {


            imageViewIcon.setImageResource(R.mipmap.ic_launcher);
        }

    }
}
