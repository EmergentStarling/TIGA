package com.example.sigmaway.homeimage.SlideableTabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sigmaway.homeimage.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * A placeholder fragment containing a simple view.
 */
public class ImageDisplayFragment extends Fragment {
    ImageView ImgView;
    TextView ImgName;
    Uri ImageUri;
    SharedPreferences sharedPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_image_display,container, false);
        ImgView= (ImageView) v.findViewById(R.id.FragmentImageView);
        ImgName= (TextView) v.findViewById(R.id.FragImageName);

        sharedPref = getActivity().getSharedPreferences("shrdpref",Context.MODE_PRIVATE);
        ImageUri = Uri.parse(sharedPref.getString("ImgUri", "no name"));
        try
        {
            Picasso.with(getActivity())
                    .load(new File(ImageUri.toString()))
                    .resize(1500,1500)
                    .into(ImgView);
//            ImgView.setImageURI(ImageUri);
            File file=new File(String.valueOf(ImageUri));
            ImgName.setHorizontallyScrolling(true);
            ImgName.setText(file.getName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return v;
    }


}
