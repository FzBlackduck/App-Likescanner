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

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.example.GraphicOverlay
import com.example.workshop1.Showproduct
import com.example.workshop1.VisionProcessorBase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/** Barcode Detector Demo.  */
class BarcodeScannerProcessor(var context: Context) : VisionProcessorBase<List<Barcode>>(context) {

  // Note that if you know which format of barcode your app is dealing with, detection will be
  // faster to specify the supported barcode formats one by one, e.g.
  // BarcodeScannerOptions.Builder()
  //     .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
  //     .build();

  private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()


    val list: ArrayList<String> = ArrayList()
    private lateinit var refUsers: DatabaseReference
    var firebaseUser: FirebaseUser? = null

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
     //val list: ArrayList<String> = ArrayList()
     for (i in barcodes.indices) {
       val barcode = barcodes[i]
       //numbers += barcode.displayValue
       list.add("" + barcode.displayValue)
       //!numbers.contains(barcode.displayValue)
       !list.contains(barcode.displayValue)
       graphicOverlay.add(BarcodeGraphic(graphicOverlay, barcode))
       logExtrasForTesting(barcode)
       /**-----------*/
     }
       alertdialog()

       /**-----------*/
       //startActivity(context, intent, null)
//     intent.putStringArrayListExtra("barcode", ArrayList(list))
//     startActivity(context, intent, null)


   }
    private  fun alertdialog(){
        val dialog: AlertDialog = AlertDialog.Builder(context)

                .setTitle("System message")
                .setMessage("Are you sure?")
                .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                    checkbarcode()
                    val intent = Intent(context, Showproduct::class.java)
                    startActivity(context, intent, null)
                })
                .setNegativeButton(R.string.no, null)
                .create()
        dialog.setOnShowListener(object : OnShowListener {
            private val AUTO_DISMISS_MILLIS = 6000
            override fun onShow(dialog: DialogInterface) {
                val defaultButton: Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)

                val negativeButtonText: CharSequence = defaultButton.text
                object : CountDownTimer(AUTO_DISMISS_MILLIS.toLong(), 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        defaultButton.text = java.lang.String.format(
                                Locale.getDefault(), "%s (%d)",
                                negativeButtonText,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                        )
                    }

                    override fun onFinish() {
                        if ((dialog as AlertDialog).isShowing) {
                            dialog.dismiss()
                        }
                    }
                }.start()
            }
        })
        val window: Window = dialog.window!!
        val wlp: WindowManager.LayoutParams = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        dialog.show()
    }

    private  fun checkbarcode(){
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product").child("barcode")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val num = dataSnapshot.childrenCount
                for (datas in dataSnapshot.children) {

                    val idDB = datas.child("id").value.toString()
                    var filterbarcodeid = list.any { it == idDB }

                    if (filterbarcodeid.equals(true)) {
                        val nameDB = datas.child("name").value.toString()
                        savebarcode(nameDB)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

    }


  private  fun savebarcode(getnameDB: String){
    firebaseUser = FirebaseAuth.getInstance().currentUser
    refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
            .child(firebaseUser!!.uid)
            .child("datalist")
            .child("$getnameDB")
      val userHashMap = HashMap<String, Any>()
        userHashMap["status"] = "Have"
        refUsers!!.updateChildren(userHashMap)


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







