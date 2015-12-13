package com.hsr.hemant.ppp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import io.saeid.fabloading.LoadingView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener {
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected static final String TAG = "FusedLocatioManager";
    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    public String urls[];
    public String titles[];
    public double latis[];
    public double longis[];
    public Bitmap bitmap[];
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;
    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;
    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    String durl, url, url_key;
    Dialog dialog;
    Location mLastLocation;
    GridView gridview;
    int counter;
    int x, y;
    Dialog dlg, builder;
    float x1, x2;
    Double myLati, myLongi, minLati, minLongi, maxLati, maxLongi;
    DisplayMetrics metrics;
    private LoadingView mLoadingView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public String[] getUrls() {
        return urls;
    }

    public double[] getLatis() {
        return latis;
    }

    public double[] getLongis() {
        return longis;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }

        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback((ResultCallback<? super LocationSettingsResult>) this);
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                (LocationListener) this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;

            }
        });

    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                (LocationListener) this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;

            }
        });
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startUpdatesButtonHandler(View view) {
        checkLocationSettings();
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setNumColumns(5);
        counter = 0;
        dialog = new Dialog(getContext(), android.R.style.Animation_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        boolean isLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        int marvel_1 = isLollipop ? R.drawable.marvel_1_lollipop : R.drawable.marvel_1;
        int marvel_2 = isLollipop ? R.drawable.marvel_2_lollipop : R.drawable.marvel_2;
        int marvel_3 = isLollipop ? R.drawable.marvel_3_lollipop : R.drawable.marvel_3;
        int marvel_4 = isLollipop ? R.drawable.marvel_4_lollipop : R.drawable.marvel_4;
        dialog.setContentView(R.layout.loading);
        mLoadingView = (LoadingView) rootView.findViewById(R.id.loading_view);
        mLoadingView.addAnimation(Color.parseColor("#FFD200"), marvel_1,
                LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), marvel_2,
                LoadingView.FROM_TOP);
        mLoadingView.addAnimation(Color.parseColor("#FF4218"), marvel_3,
                LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), marvel_4,
                LoadingView.FROM_BOTTOM);
        dialog.setCancelable(false);
        Window window = getActivity().getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the coandroid.os.Build.ndroid.os.Build.VERandroid.os.Build.ndroid.os.Build.VERSION_CODES.LOLLIPOP) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        }


        gridview.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        metrics = MyApplication.getInstance().getMetrics();

        urls = new String[40];
        latis = new double[40];
        longis = new double[40];
        bitmap = new Bitmap[40];
        titles = new String[40];
        // Inflate the layout for this fragment
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();


        return rootView;
    }

    public void makeUrl() {
        myLongi = mLastLocation.getLongitude();
        myLati = mLastLocation.getLatitude();
        GeoLocation g = new GeoLocation();
        g.degLat = myLati;
        g.degLon = myLongi;
        g.radLat = Math.toRadians(myLati);
        g.radLon = Math.toRadians(myLongi);
        GeoLocation g2[] = g.boundingCoordinates(100, 6371.01);

        minLati = g2[0].getLatitudeInDegrees();
        minLongi = g2[0].getLongitudeInDegrees();
        maxLati = g2[1].getLatitudeInDegrees();
        maxLongi = g2[1].getLongitudeInDegrees();
        x = 0;
        y = 40;
        url = "http://www.panoramio.com/map/get_panoramas.php?set=public&from=" + x + "&to=" + y + "&minx=" + minLongi + "&miny=" + minLati + "&maxx=" + maxLongi + "&maxy=" + maxLati + "&size=square&mapfilter=true";
        Log.d(TAG, "makeUrl() called with: " + url);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
        counter = 0;
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        counter = 0;
        super.onStop();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
        counter = 0;
    }


    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void grid() {

        gridview.setAdapter(new ImageAdapter(getActivity(), urls));
        AdapterView.OnItemClickListener listner = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                YoYo.with(Techniques.Bounce)
                        .duration(700)
                        .playOn(v);
                showImage(urls, position);

            }

        };
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                //AlertDialog  alertDialog = new AlertDialog
                YoYo.with(Techniques.Flash)
                        .duration(700)
                        .playOn(view);
                durl = urls[position].replace("square", "medium");
                final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(durl));
                // appears the same in Notification bar while downloading
                request.setDescription("Description for the DownloadManager Bar");
                request.setTitle(url.subSequence(url.length() - 11, url.length()));

                // save the file in the "Downloads" folder of SDCARD
                Log.d("url_download", durl.replace("medium", "original"));
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.toString());
                // get download service and enqueue file
                final DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                new Thread("Download") {
                    @Override
                    public void run() {
                        manager.enqueue(request);

                    }
                }.start();
                Snackbar.make(view, "Downloaded !!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                return true;
            }
        });
        gridview.setOnItemClickListener(listner);

    }

    public void showImage(String[] iurl, final int pos) {
        builder = new Dialog(getActivity());

        // builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setTitle(titles[pos]);

        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        durl = iurl[pos].replace("square", "medium");
        ImageView imageView = new ImageView(getActivity());

        //imageView.setDefaultImageResId(R.drawable.common_plus_signin_btn_icon_light_normal);

        //imageView.setImageUrl(durl, VolleySingleton.getInstance().getImageLoader());
        Picasso.with(getContext()).load(durl).into(imageView);

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                metrics.widthPixels,
                metrics.widthPixels));
        imageView.bringToFront();
        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Log.d(durl, "imageview touched");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        Log.d(durl, "imageview touched down");
                        builder.dismiss();
                        showImage(urls, pos + 1);
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        Log.d(durl, "imageview touched up");
                        float deltaX = x2 - x1;
                        if (deltaX < -3) {

                            Log.d(durl, "imageview rl");
                            showImage(urls, pos + 1);
                        } else if (deltaX > 3) {
                            builder.dismiss();
                            Log.d(durl, "imageview lr");
                            showImage(urls, pos - 1);
                        }
                        break;
                }
                return false;
            }
        });
        builder.show();
        YoYo.with(Techniques.Swing)
                .duration(900)
                .playOn(imageView);
    }

    public void jsonRequestVolley(String volleyUrl) {
        //dialog.show();
        mLoadingView.show();
        mLoadingView.startAnimation();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, volleyUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("photos");
                            int count = array.length();
                            for (int i = 0; i < count; i++) {
                                JSONObject obj = array.getJSONObject(i);

                  /*  long id = obj.getLong("photo_id");

                    String owner = obj.getString("owner_name");
                    String thumb = obj.getString("photo_file_url");
                    String ownerUrl = obj.getString("owner_url");*/
                                urls[i] = obj.getString("photo_file_url");

                                URL murl = new URL(urls[i]);
//.replace("square","mini_square")
                                titles[i] = obj.getString("photo_title") + " by " + obj.getString("owner_name");
                                new DownloadWebpageTask().execute(murl);

                                latis[i] = obj.getDouble("latitude");
                                longis[i] = obj.getDouble("longitude");

                            }
                            grid();
                            new Thread(new Runnable() {
                                public void run() {
                                    MyApplication.getInstance().setUrls(urls);
                                    MyApplication.getInstance().setLatis(latis);
                                    MyApplication.getInstance().setLongis(longis);
                                    MyApplication.getInstance().setTitle(titles);
                                    MyApplication.getInstance().setBms(bitmap);

                                }
                            }).start();

                            //  dialog.dismiss();
                            // mLoadingView.pauseAnimation();
                        } catch (JSONException e) {
                            Log.e("TAG", e.toString());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance().getRequestQueue().add(jsObjRequest);
    }

    public void hideDialog(Dialog d) {
        d.hide();

    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location

        // is displayed as the activity is re-created.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            myLati = mLastLocation.getLatitude();
            myLongi = mLastLocation.getLongitude();
            MyApplication.getInstance().setMyLocation(mLastLocation);
            makeUrl();
            jsonRequestVolley(url);
        }
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            MyApplication.getInstance().setMyLocation(mCurrentLocation);
        }
    }

    public void nextSet() {
        x = y + 1;
        y = y + 40;
        counter = 0;
        url = "http://www.panoramio.com/map/get_panoramas.php?set=public&from=" + x + "&to=" + y + "&minx=" + minLongi + "&miny=" + minLati + "&maxx=" + maxLongi + "&maxy=" + maxLati + "&size=square&mapfilter=true";
        jsonRequestVolley(url);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class DownloadWebpageTask extends AsyncTask<URL, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(URL... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {

                return BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
            } catch (IOException e) {
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Bitmap result) {
            bitmap[counter] = result;
            counter = counter + 1;
            if (counter == 39) {

                hideDialog(dialog);
                mLoadingView.hide();
                Snackbar.make(getView(), "Map Ready !!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        }
    }
}
