package com.iaco.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.iaco.testapp.dao.ItemDao;
import com.iaco.testapp.dto.Item;
import com.iaco.testapp.util.FileUtil;

public class EditActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1002;

    EditText topText;
    EditText bottomText;
    Button saveButton;
    ItemDao m_dao;
    Button pictureButton;
    ImageView picture;
    boolean hasBitmap = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        m_dao = new ItemDao(this);

        topText = (EditText) this.findViewById(R.id.editTopText);
        bottomText = (EditText)  this.findViewById(R.id.editBottomText);
        saveButton = (Button) this.findViewById(R.id.editButtonSave);
        pictureButton = (Button) this.findViewById(R.id.editButtonSetPicture);
        picture = (ImageView) this.findViewById(R.id.editImage);

        pictureButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                                     Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                     if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                                         startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                                     }
                                                                                             }
                                         }
        );

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(validateSave())
                {
                    int nextId = m_dao.getNextId();
                    String fileName = "EXT_Image" + Integer.toString(nextId).trim();
                    Item item = new Item();
                    item.setDescription(bottomText.getText().toString().trim());
                    item.setName(topText.getText().toString().trim());
                    item.setStatus(1);
                    item.setPictureName(fileName);


                    if (m_dao.insert(item)==0)
                    {


                        setResult(Activity.RESULT_OK, null);
                        BitmapDrawable drawable = (BitmapDrawable) picture.getDrawable();
                        FileUtil.saveBitmap(drawable.getBitmap(),fileName );

                        finish();
                    }
                    else {

                        String text = "Save Error";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(v.getContext(), text, duration);
                        toast.show();
                    }
                }
                else {

                    String text = "Set image and text before saving.";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(v.getContext(), text, duration);
                    toast.show();
                }
            }
        });
    }

    boolean validateSave()
    {
        String text1 = bottomText.getText().toString().trim();
        String text2 = topText.getText().toString().trim();

        if(text1.length()>0 && text2.length()>0 && hasBitmap)
        {
         return true;
        }
        return false;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            picture.setImageBitmap(imageBitmap);
            hasBitmap=true;

        }
    }

}
