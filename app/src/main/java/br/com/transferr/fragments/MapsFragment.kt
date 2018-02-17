package br.com.transferr.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.util.PermissionUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.CameraPosition
import android.location.Criteria
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import br.com.transferr.extensions.log
import br.com.transferr.helpers.HelperPassengersOnline
import br.com.transferr.model.Quadrant
import br.com.transferr.util.MyLocationLister
import br.com.transferr.webservices.CoordinatePassService
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : SuperClassFragment(), OnMapReadyCallback,com.google.android.gms.location.LocationListener  {


    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 13f))

            val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude))      // Sets the center of the map to location user
                    .zoom(13f)                   // Sets the zoom
                    .bearing(90f)                // Sets the orientation of the camera to east
                    .tilt(40f)                   // Sets the tilt of the camera to 30 degrees
                    .build()                   // Creates a CameraPosition from the builder
            map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private var map : GoogleMap? = null

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        val allowed = PermissionUtil.validate(activity,1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if(allowed) {
            map.isMyLocationEnabled = true
        }
        map.setMaxZoomPreference(16f)
        map.setMinZoomPreference(12f)
        map.setOnCameraMoveListener {
            //Toast.makeText(this.context,"Vc moveu o mapa chapa",Toast.LENGTH_SHORT).show()
            log("Vc moveu o mapa")
            callWebService()
        }
        //moveCamera()
        this.map = map
        //var location = map.myLocation
        //Cria o objeto longitude e latitude
        val locationMarker = LatLng(-3.7318616,-38.5266704)
        //val update = CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude),13f)
        var mylocationListener = MyLocationLister()
        var myLocationLatLng = mylocationListener.getLocation(this.context)
        if(myLocationLatLng != null) {
            val update = CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 16f)
            map.moveCamera(update)
        }else{
            val update = CameraUpdateFactory.newLatLngZoom(locationMarker, 16f)
            map.moveCamera(update)
        }

        //Marcar olocal no mapa
        map.addMarker(MarkerOptions()
                .title("Marcado aqui")
                .snippet("Alguma decricao")
                .position(locationMarker)
        )
        //Tipo do mapa
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.fragment_maps, container, false)
        //Inicia o map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }
/*
    @SuppressLint("MissingPermission")
    private fun moveCamera(){
        val locationManager = this.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 13f))

            val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude))      // Sets the center of the map to location user
                    .zoom(13f)                   // Sets the zoom
                    .bearing(90f)                // Sets the orientation of the camera to east
                    .tilt(40f)                   // Sets the tilt of the camera to 30 degrees
                    .build()                   // Creates a CameraPosition from the builder
            map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
    */

    private fun callWebService(){
        var visibleRegion = map!!.projection.visibleRegion
        var quadrant      = Quadrant()
        if(visibleRegion != null){
            quadrant.farLeftLat   = visibleRegion.farLeft.latitude
            quadrant.farLeftLng   = visibleRegion.farLeft.longitude
            quadrant.farRightLat  = visibleRegion.farRight.latitude
            quadrant.farRightLng  = visibleRegion.farRight.longitude

            quadrant.nearLeftLat  = visibleRegion.nearLeft.latitude
            quadrant.nearLeftLng  = visibleRegion.nearLeft.longitude
            quadrant.nearRightLat = visibleRegion.nearRight.latitude
            quadrant.nearRightLng = visibleRegion.nearRight.longitude

            doAsync {
                var carOnlineList   = CoordinatePassService().getOnlinePassengers(quadrant)
                uiThread {
                    if(carOnlineList != null) {
                        var markers = HelperPassengersOnline.transformInMarkers(carOnlineList)
                        for (mark in markers) {
                            map!!.addMarker(mark)
                        }
                    }
                }
            }


        }


    }


}// Required empty public constructor
