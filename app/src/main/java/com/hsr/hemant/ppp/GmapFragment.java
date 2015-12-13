package com.hsr.hemant.ppp;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GmapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GmapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GmapFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MapView mapView;
    GoogleMap map;
    String urls[];
    String titles[];
    Bitmap bm;
    DisplayMetrics metrics;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private String[] pp;
    private OnFragmentInteractionListener mListener;
    private MarkerOptions options = new MarkerOptions();

    public GmapFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GmapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GmapFragment newInstance(String param1, String param2) {
        GmapFragment fragment = new GmapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Gets the MapView from the XML layout and creates it
        View v = inflater.inflate(R.layout.fragment_gmap, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        metrics = MyApplication.getInstance().getMetrics();
        // Gets to GoogleMap from the MapView and does initialization stuff

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Dialog builder = new Dialog(getActivity());

                // builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.setTitle(titles[Integer.parseInt(marker.getSnippet())]);

                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                String durl = urls[Integer.parseInt(marker.getSnippet())].replace("square", "medium");
                ImageView imageView = new ImageView(getActivity());

                //imageView.setDefaultImageResId(com.google.android.gms.maps.R.drawable.powered_by_google_light);

                //imageView.setImageUrl(durl, VolleySingleton.getInstance().getImageLoader());
                Picasso.with(mContext).load(durl).into(imageView);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        metrics.widthPixels,
                        metrics.widthPixels));
                imageView.bringToFront();
                builder.show();
//                YoYo.with(Techniques.Hinge)
//                        .duration(500)
//                        .playOn(imageView);
                return false;
            }
        });
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        Location l = MyApplication.getInstance().getMyLocation();
        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l.getLatitude(), l.getLongitude()), 9);
        map.animateCamera(cameraUpdate);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        putMarkers();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void putMarkers() {
        urls = MyApplication.getInstance().getUrls();
        double latis[] = MyApplication.getInstance().getLatis();
        Bitmap bitmaps[] = MyApplication.getInstance().getBms();
        double longis[] = MyApplication.getInstance().getLongis();
        titles = MyApplication.getInstance().getTitle();

        for (int i = 0; i < bitmaps.length; i++) {

            //  try {
            //.replace("square","mini_square")
            // URL murl = new URL(urls[i]);

            if (bitmaps[i] != null) {
                map.addMarker(options
                        .title(titles[i])
                        .snippet("" + i)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmaps[i]))
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(new LatLng(latis[i], longis[i])));
            }
            //}
//        catch (MalformedURLException e){
//
//        }


        }
        latis = null;
        longis = null;
        bitmaps = null;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        urls = null;
    }
//    public Bitmap myClickHandler(URL url) throws IOException {
//try {
//    ConnectivityManager connMgr = (ConnectivityManager)
//            MyApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//    if (networkInfo != null && networkInfo.isConnected()) {
//        return new DownloadWebpageTask().execute(url).get();
//
//    } else {
//        // display error
//        Snackbar.make(mapView, "No Internet !!", Snackbar.LENGTH_SHORT)
//                .setAction("Action", null).show();
//
//    }
//} catch (InterruptedException e) {
//    e.printStackTrace();
//} catch (ExecutionException e) {
//    e.printStackTrace();
//}
//        return null;
//
//
//    }
//    private class DownloadWebpageTask extends AsyncTask<URL, Void, Bitmap> {
//        @Override
//        protected Bitmap doInBackground(URL... urls) {
//
//            // params comes from the execute() call: params[0] is the url.
//            try {
//                return BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
//            } catch (IOException e) {
//                return null;
//            }
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(Bitmap result) {
//
//        }
//    }

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
}
