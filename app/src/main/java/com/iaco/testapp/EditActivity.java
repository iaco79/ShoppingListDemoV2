package com.iaco.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.iaco.testapp.dao.ItemDao;
import com.iaco.testapp.dto.Item;

public class EditActivity extends Activity {


    EditText topText;
    EditText bottomText;
    Button saveButton;
    ItemDao m_dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        m_dao = new ItemDao(this);

        topText = (EditText) this.findViewById(R.id.editTopText);
        bottomText = (EditText)  this.findViewById(R.id.editBottomText);
        saveButton = (Button) this.findViewById(R.id.editButtonSave);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Item item = new Item();
                item.setDescription(bottomText.getText().toString().trim());
                item.setName(topText.getText().toString().trim());
                item.setStatus(1);
                item.setPictureName("fishfillet.jpeg");


                if (m_dao.insert(item)==0)
                {

                    setResult(Activity.RESULT_OK, null);
                    finish();
                }
                else
                {

                    String text = "Save Error";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(v.getContext(), text,duration);
                    toast.show();
                }
            }
        });
    }




}
