package it.univaq.findervelox.fragments;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import it.univaq.findervelox.R;
import it.univaq.findervelox.database.DB;
import it.univaq.findervelox.model.Autovelox;
import it.univaq.findervelox.utility.LocationHelper;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap map;
    private Marker myMarker;
    private List<Marker> autoveloxMarker = new ArrayList<>();

    private ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result) {
                locationHelper.start(MapsFragment.this);
            } else {
                Toast.makeText(requireContext(),"Enable location to find autovelox close to you", Toast.LENGTH_SHORT).show();
            }
        }
    });
    private LocationHelper locationHelper = new LocationHelper(requireContext(), launcher);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentMap);
        if(fragment != null)
            fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                Autovelox autovelox = (Autovelox) marker.getTag();
                if(autovelox != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("autovelox", autovelox);
                    Navigation.findNavController(requireView()).navigate(R.id.action_navMap_to_detailActivity, bundle);
                    return true;
                }
                return false;
            }
        });
        locationHelper.start(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        load(location);
    }

    private void load(Location location) {
        addMyMarker(location);

        for(Marker m : autoveloxMarker)
            m.remove();

        new Thread(() -> {

            LatLngBounds.Builder bounds = new LatLngBounds.Builder();
            List<Autovelox> autoveloxList = DB.getInstance(requireContext()).autoveloxDao().findAll();
            for(Autovelox autovelox : autoveloxList) {
                Location l = new Location("autovelox");
                l.setLatitude(autovelox.getLatitude());
                l.setLongitude(autovelox.getLongitude());

                if(l.distanceTo(location) > 10000) continue;

                MarkerOptions options = new MarkerOptions();
                options.title(autovelox.getLatitude() + ", " + autovelox.getLongitude());
                options.position(new LatLng(l.getLatitude(), l.getLongitude()));
                bounds.include(new LatLng(l.getLatitude(), l.getLongitude()));
                requireActivity().runOnUiThread(() -> {
                    Marker marker = map.addMarker(options);
                    marker.setTag(autovelox);
                    autoveloxMarker.add(marker);
                });
            }
            try {
                requireActivity().runOnUiThread(() -> {
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 10));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void addMyMarker(Location location) {
        if(myMarker == null) {
            MarkerOptions options = new MarkerOptions();
            options.title("MyLocation");
            options.position(new LatLng(location.getLatitude(), location.getLongitude()));
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            myMarker = map.addMarker(options);
        } else {
            myMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }
}
