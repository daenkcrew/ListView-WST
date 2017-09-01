package com.izhharuddinarief.listview_wisata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.izhharuddinarief.listview_wisata.server.URLConfig;
import com.izhharuddinarief.listview_wisata.utils.ImageLoader;
import com.izhharuddinarief.listview_wisata.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailWisata extends AppCompatActivity {



    JSONParser jsonParser = new JSONParser();
    ProgressDialog progressDialog;
    //  ListViewAdapter adapter;
    JSONArray jsonArray = null;
    public ImageLoader imageLoader;{
        imageLoader = new ImageLoader(null);
    }

    String id;
    private static final String TAG_ID ="id";
    private static final String TAG_NAMA ="nama";
    private static final String TAG_GAMBAR="gambar";
    private static final String TAG_ISI="deskripsi";
    private static final String TAG_HP="hp";
    private static final String TAG_ALAMAT="alamat";
    private static final String TAG_EMAIL="email";
    private static final String TAG_WEBSITE="website";
    private static final String TAG_TIKET="tiket";
    private static final String TAG_FASILITAS="fasilitas";

    private static final String TAG_LAT="latitude";
    private static final String TAG_LNG="longitude";


    //private FloatingActionButton fab;

    URLConfig config = new URLConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);


      /*  fab = (FloatingActionButton) findViewById(R.id.fabRute);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(DetailWisata.this, AddRouteActivity.class);
                 startActivity(intent);
            }
        });*/


        Intent a = getIntent();
        id = a.getStringExtra(TAG_ID);

        new TampilDetailWisata().execute();

    }


    class TampilDetailWisata extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetailWisata.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Mengambil Data..");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> parampa = new ArrayList<NameValuePair>();
                parampa.add(new BasicNameValuePair("id_wisata", id));
                JSONObject jsonObject = jsonParser.makeHttpRequest(config.url_detail_wisata,"GET",parampa);
                jsonArray = jsonObject.getJSONArray("wisata");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        ImageView img = (ImageView)findViewById(R.id.gambar);
                        TextView nama = (TextView)findViewById(R.id.nama);
                        TextView desc = (TextView) findViewById(R.id.deskripsi);
                        TextView alamat = (TextView) findViewById(R.id.alamat);
                        TextView hp = (TextView)findViewById(R.id.hp);
                        TextView email = (TextView)findViewById(R.id.email);
                        TextView website = (TextView)findViewById(R.id.website);
                        TextView tiket = (TextView)findViewById(R.id.tiket);
                        TextView fasilitas = (TextView)findViewById(R.id.fasilitas);

                        TextView latitude = (TextView)findViewById(R.id.latitude);
                        TextView longitude = (TextView)findViewById(R.id.longitude);



                        try {

                            JSONObject jo = jsonArray.getJSONObject(0);
                            String namaWisata = jo.getString(TAG_NAMA);
                            String descWisata = jo.getString(TAG_ISI);
                            String alamatWisata = jo.getString(TAG_ALAMAT);
                            String hpWisata = jo.getString(TAG_HP);
                            String emailWisata = jo.getString(TAG_EMAIL);
                            String websiteWisata = jo.getString(TAG_WEBSITE);
                            String tiketWisata = jo.getString(TAG_TIKET);
                            String fasilitasWisata = jo.getString(TAG_FASILITAS);
                            String url_gambar_detail = jo.getString(TAG_GAMBAR);

                            String latitudeWisata = jo.getString(TAG_LAT);
                            String longitudeWisata = jo.getString(TAG_LNG);

                            nama.setText(namaWisata);
                            desc.setText(descWisata);
                            //desc.loadData("<p style=\"text-align: justify\">"+ descWisata +"</p>", "text/html", "UTF-8");

                            alamat.setText(alamatWisata);
                            hp.setText(hpWisata);
                            email.setText(emailWisata);
                            website.setText(websiteWisata);
                            tiket.setText(tiketWisata);
                            fasilitas.setText(fasilitasWisata);

                            latitude.setText(latitudeWisata);
                            longitude.setText(longitudeWisata);

                            imageLoader.DisplayImage(url_gambar_detail, img);

                        }catch (JSONException e)
                        {
                        }
                    }
                });

            }catch (JSONException e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }

}
