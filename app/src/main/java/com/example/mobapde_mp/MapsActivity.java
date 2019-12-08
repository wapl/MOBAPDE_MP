package com.example.mobapde_mp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.androdocs.httprequest.HttpRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private boolean hasTyped=false;
    private String APIKEY;

    private EditText mSearchText;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "MapsActivity";
    private LatLng refernce;
    ArrayList<Cinema> cinemas=new ArrayList<Cinema>();
    ArrayList<Cinema> inRange=new ArrayList<Cinema>();
    IconGenerator iconFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mSearchText=findViewById(R.id.input_search);
        APIKEY=getString(R.string.APIKEY);
        iconFactory=new IconGenerator(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMarkerClickListener(this);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });
    }
    private void geoLocate(){

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);

        }catch (IOException e){

        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            refernce=new LatLng(address.getLatitude(), address.getLongitude());

            if(hasTyped==false)
            {
                hasTyped=true;
                Log.d(TAG, "HAS TYPED ");
                new cinematask().execute();
            }

            moveCamera(refernce, DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }
    private void moveCamera(LatLng latLng, float zoom, String title){

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


        MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
        mMap.addMarker(options);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        boolean found=false ;
        Cinema cinema = null;
        for(int i=0;i<cinemas.size() & found==false ;i++)
        {
            if( cinemas.get(i).getName().equals(  marker.getTitle() ) )
            {
                found=true;
                cinema=cinemas.get(i);
            }
        }
        if(found==true)
        {
            Intent intent =new Intent(this,movielist.class);
            intent.putExtra("CINEMAID",cinema.getCinemaID());
            intent.putExtra("NAME",cinema.getName());
            startActivity(intent);
        }


        return true;
    }

    public LatLng getRefernce() {
        return refernce;
    }



    class cinematask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

                String response= HttpRequest.excuteGet("https://api.internationalshowtimes.com/v4/cinemas/?location="+refernce.latitude+","+refernce.longitude+"&distance=30&apikey="+APIKEY);
                return response;


        }

        protected void onPostExecute(String result){

                Log.d(TAG, "DATA SCRAPE " );
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    Log.d(TAG, String.valueOf(jsonObj));
                    JSONArray jsonCInemas=jsonObj.getJSONArray("cinemas");
                    inRange.clear();
                    Marker marker;
                    for(int i=0;i<jsonCInemas.length();i++)
                    {
                        JSONObject jsonCinema=jsonCInemas.getJSONObject(i);
                        String  id=jsonCinema.getString("id");
                        String name=jsonCinema.getString("name");
                        JSONObject jsonLocation=jsonCinema.getJSONObject("location");
                        String Lat=jsonLocation.getString("lat");
                        String Lon=jsonLocation.getString("lon");
                        LatLng location=new LatLng(Double.parseDouble(Lat),Double.parseDouble(Lon));
                        Cinema cinema = new Cinema(Integer.parseInt(id),name,location);
                        if(!cinemas.contains(cinema))
                            cinemas.add(cinema);
                        inRange.add(cinema);

                    }
                    for(int i=0;i<inRange.size();i++)
                    {
                        LatLng latLng=inRange.get(i).getLocation();
                        String title=inRange.get(i).getName();
                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title(title);
                        marker=mMap.addMarker(options);
                        marker.hideInfoWindow();
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(title)));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
    }

}
