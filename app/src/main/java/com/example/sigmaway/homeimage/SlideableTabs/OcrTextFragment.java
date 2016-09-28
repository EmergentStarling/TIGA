package com.example.sigmaway.homeimage.SlideableTabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sigmaway.homeimage.CustomClasses.Communicator;
import com.example.sigmaway.homeimage.CustomClasses.DataBaseAdapter;
import com.example.sigmaway.homeimage.CustomClasses.ImageInfo;
import com.example.sigmaway.homeimage.R;
import com.example.sigmaway.homeimage.volley.VolleySendData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OcrTextFragment extends Fragment {
    String ImageName;
    Button Analysis;
    TextView OcrText;

    TextView ImageKey;
    List<String> value;
    File file;
    Communicator comm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.wtf("ocr text fragment", "here");
        View v = inflater.inflate(R.layout.ocr_eng_text_frag, container, false);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shrdpref", Context.MODE_PRIVATE);
        Uri ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        file = new File(String.valueOf(ImageUri));
        ImageName = file.getName().substring(0, file.getName().lastIndexOf('.'));
        ImageKey = (TextView) v.findViewById(R.id.Frag_ImgKey_TextView);
        OcrText = (TextView) v.findViewById(R.id.Frag_OcrText_TextView);
        Log.wtf("ocr text fragment", "here 1");
        /*parentfilepath = String.valueOf(file.getParentFile());
        File OCRFile = new File(parentfilepath + File.separator + ImageName + ".txt");
        File Key = new File(parentfilepath + File.separator + ImageName + ".key");*/
       /* String text = null;
        String key = null;*/
       /* try {
            text = read_file(getActivity(), OCRFile.getPath());
            key = read_file(getActivity(), Key.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        DataBaseAdapter dataBaseAdapter =new DataBaseAdapter(getActivity().getApplicationContext());
        value = new ArrayList<String>();
        value= dataBaseAdapter.getdata(file.getName());
        ImageKey.setText("Key Words : " + value.get(1));
        OcrText.setMovementMethod(new ScrollingMovementMethod());
        ImageKey.setMovementMethod(new ScrollingMovementMethod());
        OcrText.setText(value.get(0));
        Log.wtf("key in ocr frag", String.valueOf(value.size()));
//        text = null;

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Analysis= (Button)getActivity().findViewById(R.id.sendforanalysiseng);

        Analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageInfo info=new ImageInfo();
                info.ImageName=file.getName();
                info.EngText=value.get(0);
                info.Keyword=value.get(1);
                info.UserID="8800304343";
                VolleySendData obj=new VolleySendData();
                String result= obj.VolleySend(getActivity(),info);
                Toast.makeText(getActivity(),result,Toast.LENGTH_LONG);
                comm= (Communicator) getActivity();
                comm.update();

            }
        });
    }
    /*    public String read_file(Context context, String filename) {
        StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            } catch (IOException e) {
                //You'll need to add proper error handling here
            }
        return String.valueOf(text);
    }*/

}