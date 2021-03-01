package com.example.googlemap

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workshop1.DetailProduct
import com.example.workshop1.R
import com.example.workshop1.modurn_main.Main
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maplist.*
import java.text.DecimalFormat
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapAdapter.MapClicklistner {
    /**GoogleMap.OnMarkerClickListener*/
    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1
    //var marker : Marker? = null
    var two = LatLng(13.701587, 100.541169)//blue
    var one = LatLng(13.708665, 100.534849)//red

    var mapList: MutableList<MapList>? = null
    var listcheck : ArrayList<String>  = ArrayList()
    var rv: RecyclerView? = null
    var adapter: MapAdapter? = null
    var kmInDec: Int? = null
    var meterInDec: Int? = null
    var distance: String = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //recyclerView
        rv = findViewById<View>(R.id.recview) as RecyclerView
        rv!!.setHasFixedSize(true)
        rv!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mapList = ArrayList()








        var home = findViewById<View>(R.id.home)
        home.setOnClickListener {
            startActivity(Intent(this, Main::class.java))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationAccess()

        // Add a marker in Sydney and move the camera
//        val two = LatLng(13.701587, 100.541169)
//        mMap.addMarker(MarkerOptions().position(two).title("twoShop"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(two))

        mMap.addMarker(MarkerOptions()
                .position(one)
                .title("7-ELEVEN")
                .snippet("7-ELEVEN-0001")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(one))

        val center = LatLng(13.701587, 100.541169)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15f))

        mMap.addPolyline(
                PolylineOptions().geodesic(true)
                        .add(one)
                        .add(two)
        )

        //mMap.setOnMarkerClickListener(this)

         mMap.addMarker(MarkerOptions()
                 .position(two)
                 .title("Tops")
                 .snippet("Top-0001")
                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
         )

//        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        var location: Location? = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        if (location == null) {
//            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//        }
//        Log.i("location: ", "$location")




        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null) {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        var latitude = location!!.latitude
        var longitude = location!!.longitude

        val precision = DecimalFormat("0.000000")
        var lat = (precision.format(latitude)).toDouble()
        var long = (precision.format(longitude)).toDouble()
        Log.i("location", "$lat,$long")
        val gps = LatLng(lat, long)

        mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow: View = layoutInflater.inflate(R.layout.custom_info_contents, null)
                val title = infoWindow.findViewById<View>(R.id.textViewName) as TextView
                val snippet = infoWindow.findViewById<View>(R.id.textViewSnippet) as TextView
                val imageView: ImageView = infoWindow.findViewById<View>(R.id.imageView) as ImageView

                //val check = mapList!!.none { it == (MapList("${marker.title}","${snippet.text}", drawableId,distance)) }
                val check = listcheck.none { it == marker.title }
                if ("Tops" == marker.title) {
                    // imageView.setImageResource(R.drawable.top)
                    imageView.setImageResource(R.drawable.top)
                    imageView.tag = R.drawable.top
                    val drawableId: Int = imageView.tag as Int
                    title.text = marker.title
                    snippet.text = marker.snippet
                    CalculationByDistance(two, gps)
                    distance = "$kmInDec.$meterInDec Km."
                    if (check == true) {
                        mapList!!.add(MapList("${title.text}", "${snippet.text}", drawableId, distance))
                        listcheck.add("${title.text}")
                        adapter = MapAdapter(mapList!!, applicationContext, this@MapsActivity)
                        rv!!.adapter = adapter
                    }
                }
                if ("7-ELEVEN" == marker.title) {
                    imageView.setImageResource(R.drawable.seven)
                    imageView.tag = R.drawable.seven
                    val drawableId: Int = imageView.tag as Int
                    title.text = marker.title
                    snippet.text = marker.snippet
                    CalculationByDistance(one, gps)
                    distance = "$kmInDec.$meterInDec Km."
                    if(check == true) {
                        mapList!!.add(MapList("${title.text}", "${snippet.text}", drawableId, distance))
                        listcheck.add("${title.text}")
                        adapter = MapAdapter(mapList!!, applicationContext, this@MapsActivity)
                        rv!!.adapter = adapter
                    }
                }



                return infoWindow
            }
        })


    }


    private fun openGoogleMap(src: LatLng, dest: LatLng) {
        val url =
            "http://maps.google.com/maps?saddr=" + src.latitude + "," + src.longitude + "&daddr=" + dest.latitude + "," + dest.longitude + "&mode=driving"
        val gmmIntentUri: Uri = Uri.parse(url)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                        this,
                        "User has not granted location access permission",
                        Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST
            )
    }

//    override fun onMarkerClick(marker: Marker?): Boolean {
//        if (marker == myMarker) {
//           // openGoogleMap(one,two)
//            CalculationByDistance(one, two)
//            val title = marker!!.title
//            Log.i("NAME", "$title")
//        }
//        return true
//    }
    private fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Double {
        val Radius = 6371 // radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (sin(dLat / 2) * sin(dLat / 2)
                + (cos(Math.toRadians(lat1))
                * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                * sin(dLon / 2)))
        val c = 2 * asin(sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        kmInDec = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        meterInDec = Integer.valueOf(newFormat.format(meter))
        Log.i(
                "Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec
        )
        return Radius * c
    }

    override fun onClick(map: MapList, position: Int) {
//        val intent = Intent(this, DetailProduct::class.java)
//        intent.putExtra("name_detail", userList.name)
//        intent.putExtra("price_detail", userList.price)
//        intent.putExtra("quantity_detail", userList.quantity)
//        intent.putExtra("status_detail", userList.status)
//        intent.putExtra("image_detail", userList.image)
//        intent.putExtra("category_detail", userList.category)
//        startActivity(intent)


    }



}




