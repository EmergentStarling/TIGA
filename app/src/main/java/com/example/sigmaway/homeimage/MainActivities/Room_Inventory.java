package com.example.sigmaway.homeimage.MainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sigmaway.homeimage.R;

public class Room_Inventory extends AppCompatActivity {

    private static final String IMAGE_DIRECTORY_NAME = "Sigmaway";
    Button Add_Wall;
    Button Add_Objects;
    Button Add_Documents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_inventory);
        Intent mainactivity_receiving_intent = getIntent();
        final String Room_Selected= mainactivity_receiving_intent.getStringExtra("room_name");
        Add_Wall= (Button) findViewById(R.id.add_walls_btn);
        Add_Objects= (Button) findViewById(R.id.add_objects_btn);
        Add_Documents= (Button) findViewById(R.id.add_Document_btn);


        Add_Wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent walls= new Intent(Room_Inventory.this,Walls.class);
                walls.putExtra("room_Selected",Room_Selected);
                startActivity(walls);
            }
        });
        Add_Objects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj =new Intent(Room_Inventory.this,Objects.class);
                obj.putExtra("room_Selected",Room_Selected);
                startActivity(obj);
            }
        });
        Add_Documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Documents = new Intent(Room_Inventory.this,Documents.class);
                Documents.putExtra("room_Selected",Room_Selected);
                startActivity(Documents);
            }
        });
    }
}
