package com.example.barcodescanner

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.GraphicOverlay
import com.example.workshop1.Showproduct
import com.example.workshop1.VisionProcessorBase
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

 class BarcodeScannerCamera(var context: Context) : VisionProcessorBase<List<Barcode>>(context) {

    // Note that if you know which format of barcode your app is dealing with, detection will be
    // faster to specify the supported barcode formats one by one, e.g.
    // BarcodeScannerOptions.Builder()
    //     .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
    //     .build();
    private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()
    val list: ArrayList<String> = ArrayList()
    
    override fun stop() {
        super.stop()
        barcodeScanner.close()
        list2 = listnull
        Log.v(
                MANUAL_TESTING_LOG,
                "////////////[[[stop]]]]]]////////////// "
        )
    }




    override fun detectInImage(image: InputImage): Task<List<Barcode>> {
        return barcodeScanner.process(image)

    }



    override fun onSuccess(barcodes: List<Barcode>, graphicOverlay: GraphicOverlay) {
        if (barcodes.isEmpty()) {
            Log.v(MANUAL_TESTING_LOG, "No barcode has been detected")
        }
        /**----*/
           //if(list2.count() >= 3) {

               //stop()
//               val intent = Intent(context, Showproduct::class.java)
//               intent.putStringArrayListExtra("barcode", ArrayList(list))
//               ContextCompat.startActivity(context, intent, null)

               /**----*/


               for (i in barcodes.indices) {
                   val barcode = barcodes[i]
                   graphicOverlay.add(BarcodeGraphic(graphicOverlay, barcode))
                   root(barcode)
                   //logExtrasForTesting(barcode)

//                   val filterbarcode = list.none { it == barcode.displayValue }
//                   if (filterbarcode.equals(true)) {
//                       list.add("" + barcode.displayValue)
//                       Log.v(
//                               MANUAL_TESTING_LOG,
//                               "////////////[[[[+++++++++++++++++++++]]]]]]////////////// $list"+
//                                       "ddd ${list.count()}"
//                       )
//                   }

               }

           }

    fun action() {

        val intent = Intent(context, Showproduct::class.java)
        intent.putStringArrayListExtra("barcode", ArrayList(list2))
        ContextCompat.startActivity(context, intent, null)

    }



    override fun onFailure(e: Exception) {
        Log.e(TAG, "Barcode detection failed $e")
    }

    companion object {
        private const val TAG = "BarcodeProcessor"
        var list2: ArrayList<String> = ArrayList()
        var listnull: ArrayList<String> = ArrayList()
        var ee: ArrayList<String> = ArrayList()

        fun root(barcode: Barcode?){
            if (barcode != null) {
                val filterbarcode = list2.none { it == barcode.displayValue }
                if (filterbarcode.equals(true)) {
                    list2.add("" + barcode.displayValue)
                    Log.v(
                            MANUAL_TESTING_LOG,
                            "////////////[[[[+++++++++++++++++++++]]]]]]////////////// $list2" +
                                    "ddd ${list2.count()}"
                    )
                }else{
                    ee.add(barcode.displayValue)
                }

            }
        }






        private fun logExtrasForTesting(barcode: Barcode?) {
            if (barcode != null) {
                Log.v(
                        MANUAL_TESTING_LOG,
                        String.format(
                                "Detected barcode's bounding box: %s",
                                barcode.boundingBox!!.flattenToString()
                        )
                )
                Log.v(
                        MANUAL_TESTING_LOG,
                        String.format(
                                "Expected corner point size is 4, get %d",
                                barcode.cornerPoints!!.size
                        )
                )
                for (point in barcode.cornerPoints!!) {
                    Log.v(
                            MANUAL_TESTING_LOG,
                            String.format(
                                    "Corner point is located at: x = %d, y = %d",
                                    point.x,
                                    point.y
                            )
                    )
                }
                Log.v(
                        MANUAL_TESTING_LOG,
                        "barcode display value: " + barcode.displayValue
                )
                Log.v(
                        MANUAL_TESTING_LOG,
                        "barcode raw value: " + barcode.rawValue
                )

            }
        }
    }

}