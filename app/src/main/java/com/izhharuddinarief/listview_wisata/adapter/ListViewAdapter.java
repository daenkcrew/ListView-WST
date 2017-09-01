package com.izhharuddinarief.listview_wisata.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.izhharuddinarief.listview_wisata.MainActivity;
import com.izhharuddinarief.listview_wisata.R;
import com.izhharuddinarief.listview_wisata.utils.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IzhharuddinArief on 21/08/2017.
 */

public class ListViewAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    public ListViewAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.item_wisata, null);

        TextView idWisata = (TextView) vi.findViewById(R.id.id);
        TextView namaWisata = (TextView) vi.findViewById(R.id.nama);
        TextView alamatWisata = (TextView) vi.findViewById(R.id.alamat);
        TextView hp = (TextView) vi.findViewById(R.id.hp);
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.gambar);

        HashMap<String, String> daftarWisata = new HashMap<String, String>();
        daftarWisata = data.get(position);

        idWisata.setText(daftarWisata.get(MainActivity.TAG_ID));
        namaWisata.setText(daftarWisata.get(MainActivity.TAG_NAMA));
        alamatWisata.setText(daftarWisata.get(MainActivity.TAG_ALAMAT));
       // hp.setText(daftarWisata.get(ObyekWisataActivity.TAG_HP));
/*

        Picasso.with(activity.getApplicationContext()).
        load(daftarWisata.get(ObyekWisataActivity.TAG_GAMBAR)).
        // error(R.drawable.no_image).into(thumb_image);
        return vi;
*/


        imageLoader.DisplayImage(daftarWisata.get(MainActivity.TAG_GAMBAR),thumb_image);

        return vi;

    }
}
