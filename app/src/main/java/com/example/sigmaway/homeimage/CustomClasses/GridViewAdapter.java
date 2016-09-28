package com.example.sigmaway.homeimage.CustomClasses;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sigmaway.homeimage.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Family on 11-02-2016.
 */

public class GridViewAdapter extends ArrayAdapter<String> {
    Context context;
    List<Uri> Picture_Uri = new ArrayList<Uri>();
    List<String> Picture_Name = new ArrayList<String>();


    public GridViewAdapter(Context c, List<Uri> Picture_Uri, List<String> Picture_Name) {
        super(c, R.layout.grid_view_singleitem, R.id.wall_name, Picture_Name);
        this.context = c;
        this.Picture_Uri = Picture_Uri;
        this.Picture_Name = Picture_Name;
        Log.wtf("adapter called 1", " Hi");

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Log.wtf("adapter called 2", " Hi");
        GridViewadapter_Myviewhandler holder = null;
        View row = convertView;
        if (row == null) //for recycling already created view
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.grid_view_singleitem, parent, false);
            holder = new GridViewadapter_Myviewhandler(row);
            row.setTag(holder);
        } else {

            holder = (GridViewadapter_Myviewhandler) row.getTag();
        }
        Picasso.with(holder.wall_image.getContext())
                .load(new File(Picture_Uri.get(position).getPath()))
                .resize(200,200)
                .into(holder.wall_image);
     //   Log.wtf("under picasso",Picture_Uri.get(position).getPath() );
        holder.wall_name.setText(Picture_Name.get(position));
       /* AsyncTaskRunner2 runner= new AsyncTaskRunner2();
        runner.execute(holder);*/
        return row;
    }


    class GridViewadapter_Myviewhandler {
        ImageView wall_image;
        TextView wall_name;

        GridViewadapter_Myviewhandler(View row) {
            wall_image = (ImageView) row.findViewById(R.id.wall_image);
            wall_name = (TextView) row.findViewById(R.id.wall_name);
        }
    }

  /*  private class AsyncTaskRunner2 extends AsyncTask< GridViewadapter_Myviewhandler ,Void ,Bitmap>
    {
        GridViewadapter_Myviewhandler holder;



        @Override
        protected Bitmap doInBackground(GridViewadapter_Myviewhandler... params) {
            holder=params[0];
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 20;
            Bitmap bitmap = BitmapFactory.decodeFile(Picture_Uri.get(position).getPath(), options);

            return bitmap;
        }



        @Override
        protected void onPostExecute(Bitmap bitmap) {
            holder.wall_image.setImageBitmap(bitmap);
            holder.wall_name.setText(Picture_Name.get(position));;
        }
    }*/
}
