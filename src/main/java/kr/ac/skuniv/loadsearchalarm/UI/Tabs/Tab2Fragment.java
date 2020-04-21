package kr.ac.skuniv.loadsearchalarm.UI.Tabs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import kr.ac.skuniv.loadsearchalarm.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by cs618 on 2017-07-17.
 */

public class Tab2Fragment extends Fragment {
    private static String API_KEY = "0afc15ab7890dae74625f682a58abd66";
    private MapView mapView;
    private MapPOIItem customMarker;
    private static final String LOG_TAG = "Tab2Fragment";
    private MapReverseGeoCoder mReverseGeoCoder = null;
    private boolean isUsingCustomLocationMarker = false;
    LocationManager mLM;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLM= (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);
        mapView = new MapView(getActivity());
        ViewGroup mcontainer = (ViewGroup) rootView.findViewById(R.id.map_view);
        mcontainer.addView(mapView);
        mapView.setDaumMapApiKey(API_KEY);
        mapView.setMapType(MapView.MapType.Standard);
        customMarker = new MapPOIItem();
        String name = "Custom Marker";
        customMarker.setItemName(name);



        Button locationbtn=((Button)rootView.findViewById(R.id.locationbtn));
        locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {}

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    @Override
                    public void onProviderEnabled(String provider) {}

                    @Override
                    public void onProviderDisabled(String provider) {}
                });
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location == null) {
                    Toast.makeText(getActivity(), "GPS signal not found", Toast.LENGTH_SHORT).show();
                }
                if (location != null) {
                    Log.e("--------------------",location.getLongitude()+"");
                    Log.e("--------------------" , location.getLatitude()+"");
                }
                mapView.removePOIItem(customMarker);
                MapPoint CUSTOM_MARKER_POINT = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());
                mapView.setMapCenterPoint(CUSTOM_MARKER_POINT, true);
                customMarker.setMapPoint(CUSTOM_MARKER_POINT);
                mapView.addPOIItem(customMarker);
        }});

        return rootView;
    }
}



