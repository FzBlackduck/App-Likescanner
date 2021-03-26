package com.example.qrscan

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.GraphicOverlay
import com.example.barcodescanner.BarcodeGraphic
import com.example.workshop1.CameraXLivePreviewActivity
import com.example.workshop1.StillImageActivity
import com.example.workshop1.VisionProcessorBase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

 class QRCamera(var context: Context,var get:String) : VisionProcessorBase<List<Barcode>>(context) {

    private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()
    val list: ArrayList<String> = ArrayList()
     private lateinit var refUsers: DatabaseReference
     var firebaseUser: FirebaseUser? = null

    override fun stop() {
        super.stop()
        barcodeScanner.close()
        list2 = listnull
    }


    override fun detectInImage(image: InputImage): Task<List<Barcode>> {
        return barcodeScanner.process(image)

    }



    override fun onSuccess(barcodes: List<Barcode>, graphicOverlay: GraphicOverlay) {


        if (barcodes.isEmpty()) { }

               for (i in barcodes.indices) {
                   val barcode = barcodes[i]
                   graphicOverlay.add(BarcodeGraphic(graphicOverlay, barcode))
                   check(barcode)
               }

    }


     private fun savebarcode(qr: String?) {
         firebaseUser = FirebaseAuth.getInstance().currentUser
         refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
                 .child(firebaseUser!!.uid)
                 .child("store")
                 .child("$qr")
         val userHashMap = HashMap<String, Any>()
         userHashMap["status"] = "Have"
         refUsers!!.updateChildren(userHashMap)

         if(get == "IMAGE SCANNER") {
             val intent = Intent(context, StillImageActivity::class.java)
             intent.putExtra("qr",qr)
             startActivity(context, intent, null)
         }
         if(get == "REALTIME SCANNER"){
             val intent = Intent(context, CameraXLivePreviewActivity::class.java)
             intent.putExtra("qr",qr)
             startActivity(context, intent, null)
         }
//         val intent = Intent(context, StillImageActivity::class.java)
//         intent.putExtra("qr",qr)
//         startActivity(context, intent, null)

     }

     private fun check(qr: Barcode) {
         var refUsers: DatabaseReference? = null
         refUsers = FirebaseDatabase.getInstance().reference.child("Product")
                 .child("${qr.displayValue}")
                 .child("barcode")
                 .child("0")
         refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {
               val getid = dataSnapshot.child("id").value.toString()
                 Log.i("checkgetid","$getid")

                 if (getid != "null") {
                     savebarcode(qr.displayValue)
                 }else{
                     Toast.makeText(context, "Not found" , Toast.LENGTH_SHORT).show()
                 }

             }

             override fun onCancelled(error: DatabaseError) {
             }
         })
     }


    override fun onFailure(e: Exception) {
        Log.e(TAG, "Barcode detection failed $e")
    }

    companion object {
        private const val TAG = "BarcodeProcessor"
        var list2: ArrayList<String> = ArrayList()
        var listnull: ArrayList<String> = ArrayList()
    }

}