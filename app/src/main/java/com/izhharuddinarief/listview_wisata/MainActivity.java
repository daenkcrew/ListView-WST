package com.izhharuddinarief.listview_wisata;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.izhharuddinarief.listview_wisata.adapter.ListViewAdapter;
import com.izhharuddinarief.listview_wisata.server.URLConfig;
import com.izhharuddinarief.listview_wisata.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    // Progress Dialog
    private ProgressDialog progressDialog;

    ArrayList<HashMap<String, String>> ListWisata = new ArrayList<HashMap<String, String>>();

    // JSON Node
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_GAMBAR = "gambar";

    static final int tampil_error = 1;

    // wisata JSONArray
    JSONArray jsonArray = null;

    ListView ls;
    ListViewAdapter adapter;

    // URL WISATA
    URLConfig config = new URLConfig();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Obyek Wisata");

        ListWisata = new ArrayList<HashMap<String, String>>();

        if (cek_status(this))

            new TampilWisata().execute();
        else {
            showDialog(tampil_error);
        }

        ls = (ListView) findViewById(R.id.list);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = ListWisata.get(position);
                Intent a = new Intent(getApplicationContext(), DetailWisata.class);
                a.putExtra(TAG_ID, map.get(TAG_ID));
                a.putExtra(TAG_NAMA, map.get(TAG_NAMA));
                a.putExtra(TAG_GAMBAR, map.get(TAG_GAMBAR));
                startActivity(a);
            }
        });

    }

    public boolean cek_status(Context cek) {
        ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case tampil_error:
                // Create an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Set the Alert Dialog Message
                builder.setMessage("Cek Koneksi Internet Anda!")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                /*    // Restart the Activity
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);*/
                                    }
                                });
                AlertDialog errorAlert = builder.create();
                return errorAlert;

            default:
                break;
        }
        return dialog;
    }


    class TampilWisata extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading! Data Wisata..");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            // Buat Parameter
            List<NameValuePair> parampa = new ArrayList<NameValuePair>();

            // Ambil json dari url
            JSONObject jsonObject = jsonParser.makeHttpRequest(config.url_listwisata,"GET", parampa);
            Log.i("JSON WISATA : ", ""+jsonObject);
            try{
                jsonArray = jsonObject.getJSONArray("wisata");
                for(int a = 0; a <jsonArray.length(); a++){
                    JSONObject js = jsonArray.getJSONObject(a);
                    String idWisata = js.getString(TAG_ID);
                    String namaWisata = js.getString(TAG_NAMA);
                    String alamatWisata = js.getString(TAG_ALAMAT);
                    // String gbrWisata = config.gambar_wisata+ js.getString(TAG_GAMBAR);
                    String gbrWisata = js.getString(TAG_GAMBAR);

                    HashMap<String,String> map = new HashMap<String ,String>();
                    map.put(TAG_ID, idWisata);
                    map.put(TAG_NAMA, namaWisata);
                    map.put(TAG_ALAMAT, alamatWisata);
                    map.put(TAG_GAMBAR, gbrWisata);

                    ListWisata.add(map);
                }
            }catch (JSONException e){
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SetListWisata(ListWisata);
                }
            });
        }
    }
    private void SetListWisata(ArrayList<HashMap<String, String>> listWisata) {
        adapter = new ListViewAdapter(this,listWisata );
        ls.setAdapter(adapter);
    }

}