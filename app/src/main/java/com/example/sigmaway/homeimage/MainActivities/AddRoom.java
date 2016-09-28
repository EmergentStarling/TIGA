package com.example.sigmaway.homeimage.MainActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sigmaway.homeimage.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddRoom extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    ListView RoomList;
    Button AddRoom;
    Spinner AddRoomSelector;
    ArrayAdapter<String> List_View_Adapter;
    String HomeName;
    ArrayAdapter<String> Spinner_Adapter;
    int SpinnerPosition =0 ;
    String[] Room_Name={"Select Room To Add","living room"," room 1","room 2","room3", "room4","room5" };
    List<String> Selected_Room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Selected room list
        Selected_Room=new ArrayList<String>();
        //room list for spinner
        RoomList= (ListView) findViewById(R.id.roomlist);
        AddRoom= (Button) findViewById(R.id.add_room_btn);
        AddRoomSelector= (Spinner) findViewById(R.id.add_room_selector);
        SharedPreferences sharedPref = getApplication().getSharedPreferences("shrdpref",MODE_PRIVATE);
        HomeName= sharedPref.getString("Homename","no name");
        Log.wtf("addroom",HomeName);
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Sigmaway/"+HomeName);
        File[] fList = mediaStorageDir.listFiles();
        //  Log.wtf("flist", String.valueOf(fList.length));

        if (!(fList==null)){
            for (File file:fList)
            {
                if(file.isDirectory())
                    Selected_Room.add(file.getName());
            }
        }



        //List View adapter
        List_View_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Selected_Room);
        RoomList.setAdapter(List_View_Adapter);
        RoomList.setOnItemClickListener(this);
        //spinner adapter
        Spinner_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,Room_Name);
        AddRoomSelector.setAdapter(Spinner_Adapter);
        AddRoomSelector.setOnItemSelectedListener(this);
        //   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),Room_Name[SpinnerPosition] , Toast.LENGTH_SHORT).show();
                //if and else to avoid addition of "Select room to add" from spinner
                if (SpinnerPosition==0)
                    Toast.makeText(com.example.sigmaway.homeimage.MainActivities.AddRoom.this, "Select The Room First", Toast.LENGTH_LONG).show();
                else
                    // adding selected room to list view and its data set.
                    Selected_Room.add(Room_Name[SpinnerPosition]);
                // To refresh the listview
                RoomList.invalidateViews();
            }
        });

    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //if condition is to rectify which parent view is pressed
        if (parent==AddRoomSelector){
            Log.i("spinner ", String.valueOf(position));
            // to identify the room user selected to add
            SpinnerPosition = position;
           // Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent==RoomList)
        {
            Log.i("roomlist clisked","  hi");
            Intent Room_Inventory= new Intent(this, com.example.sigmaway.homeimage.MainActivities.Room_Inventory.class);
            Room_Inventory.putExtra("room_name",Selected_Room.get(position));
            startActivity(Room_Inventory);
        }
    }
}
