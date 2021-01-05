/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.barcodescanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.GraphicOverlay
import com.example.workshop1.R
import com.example.workshop1.Showproduct
import com.example.workshop1.VisionProcessorBase
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.activity_main.*


/** Barcode Detector Demo.  */
class BarcodeScannerProcessor(var context: Context) : VisionProcessorBase<List<Barcode>>(context) {

  // Note that if you know which format of barcode your app is dealing with, detection will be
  // faster to specify the supported barcode formats one by one, e.g.
  // BarcodeScannerOptions.Builder()
  //     .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
  //     .build();

  private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()

  override fun stop() {
    super.stop()
    barcodeScanner.close()
  }


  override fun detectInImage(image: InputImage): Task<List<Barcode>> {
    return barcodeScanner.process(image)
  }
   /**ff//////////////////////////////////////////////////////////////////// */
  override fun onSuccess(barcodes: List<Barcode>, graphicOverlay: GraphicOverlay) {
    if (barcodes.isEmpty()) {
      Log.v(MANUAL_TESTING_LOG, "No barcode has been detected")
    }
     /**-----------*/
     val intent = Intent(context, Showproduct::class.java)
     //val numbers = mutableListOf("")
       val list: ArrayList<String> = ArrayList()

    for (i in barcodes.indices) {

      val barcode = barcodes[i]
      //numbers += barcode.displayValue
        list.add(""+barcode.displayValue)

      //!numbers.contains(barcode.displayValue)
        !list.contains(barcode.displayValue)


      graphicOverlay.add(BarcodeGraphic(graphicOverlay, barcode))

      logExtrasForTesting(barcode)


      /**-----------*/


    }
     /**-----------*/
     intent.putStringArrayListExtra("barcode", ArrayList(list))
     startActivity(context, intent, null)
     Log.v(
             MANUAL_TESTING_LOG,
             "////////////[[[[]]]]]]////////////// $list"
     )
  }




  override fun onFailure(e: Exception) {
    Log.e(TAG, "Barcode detection failed $e")
  }

  companion object {
    private const val TAG = "BarcodeProcessor"



    /**---- แปลงบาโค้ด ----*/
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

//        val intent = Intent(BarcodeScannerProcessor.context, Showproduct::class.java)
//        intent.putExtra("name", "devahoy")
//        intent.putExtra("isSmart", true)
//        intent.putExtra("star", 5)
//        startActivity(intent)


      }

    }

  }

}





