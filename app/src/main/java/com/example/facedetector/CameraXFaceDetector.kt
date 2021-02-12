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

package com.example.facedetector

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.CameraXViewModel
import com.example.GraphicOverlay
import com.example.VisionImageProcessor
import com.example.barcodescanner.BarcodeScannerCamera
import com.example.preference.PreferenceUtils
import com.example.workshop1.R
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.face.Face
import com.hsalf.smilerating.BaseRating
import com.hsalf.smilerating.SmileRating

import java.util.ArrayList


/** Live preview demo app for ML Kit APIs using CameraX.  */
@KeepName
@RequiresApi(VERSION_CODES.LOLLIPOP)
class CameraXFaceDetector :
        AppCompatActivity(),
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener,
        Callbackdetect{

  private var previewView: PreviewView? = null
  private var graphicOverlay: GraphicOverlay? = null
  private var cameraProvider: ProcessCameraProvider? = null
  private var previewUseCase: Preview? = null
  private var analysisUseCase: ImageAnalysis? = null
  private var imageProcessor: VisionImageProcessor? = null
  private var needUpdateGraphicOverlayImageSourceInfo = false
  private var selectedModel = FACE_DETECTION
  private var lensFacing = CameraSelector.LENS_FACING_BACK
  private var cameraSelector: CameraSelector? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "onCreate")
    if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
      Toast.makeText(
              applicationContext,
              "CameraX is only supported on SDK version >=21. Current SDK version is " +
                      VERSION.SDK_INT,
              Toast.LENGTH_LONG
      )
              .show()
      return
    }
    if (savedInstanceState != null) {
      selectedModel =
              savedInstanceState.getString(
                      STATE_SELECTED_MODEL,
                  FACE_DETECTION
              )
      lensFacing =
              savedInstanceState.getInt(
                      STATE_LENS_FACING,
                      CameraSelector.LENS_FACING_BACK
              )
    }
    cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    setContentView(R.layout.activity_facedetector)

    previewView = findViewById(R.id.preview_view)
    if (previewView == null) {
      Log.d(TAG, "previewView is null")
    }
    graphicOverlay = findViewById(R.id.graphic_overlay)
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null")
    }
    /**-------------------------------------------------------[select option]----*/
    val spinner = findViewById<Spinner>(R.id.spinner)
    val options: MutableList<String> = ArrayList()
    options.add(FACE_DETECTION)


    // Creating adapter for spinner
    val dataAdapter =
            ArrayAdapter(this, R.layout.spinner_style, options)
    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    // attaching data adapter to spinner
    spinner.adapter = dataAdapter
    spinner.onItemSelectedListener = this
    val facingSwitch =
            findViewById<ToggleButton>(R.id.facing_switch)
    facingSwitch.setOnCheckedChangeListener(this)
    ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )
            .get(CameraXViewModel::class.java)
            .processCameraProvider
            .observe(
                    this,
                    Observer { provider: ProcessCameraProvider? ->
                      cameraProvider = provider
                      if (allPermissionsGranted()) {
                        bindAllCameraUseCases()
                      }
                    }
            )

//    val settingsButton = findViewById<ImageView>(R.id.settings_button)
//    settingsButton.setOnClickListener {
//      val intent =
//              Intent(applicationContext, SettingsActivity::class.java)
//      intent.putExtra(
//              SettingsActivity.EXTRA_LAUNCH_SOURCE,
//              SettingsActivity.LaunchSource.CAMERAX_LIVE_PREVIEW
//      )
//      startActivity(intent)
//    }

    if (!allPermissionsGranted()) {
      runtimePermissions
    }

    /**---------------------------------------*/
    val finnes : Button = findViewById(R.id.finished)

    finnes.setOnClickListener{
          val namecam = BarcodeScannerCamera(this)
           namecam.action()


    }


  }




  override fun onSaveInstanceState(bundle: Bundle) {
    super.onSaveInstanceState(bundle)
    bundle.putString(STATE_SELECTED_MODEL, selectedModel)
    bundle.putInt(STATE_LENS_FACING, lensFacing)
  }

  @Synchronized
  override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent?.getItemAtPosition(pos).toString()
    Log.d(TAG, "Selected model: $selectedModel")
    bindAnalysisUseCase()
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    // Do nothing.
  }

  override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
    Log.d(TAG, "Set facing")
    if (cameraProvider == null) {
      return
    }
    val newLensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
      CameraSelector.LENS_FACING_BACK
    } else {
      CameraSelector.LENS_FACING_FRONT
    }
    val newCameraSelector =
            CameraSelector.Builder().requireLensFacing(newLensFacing).build()
    try {
      if (cameraProvider!!.hasCamera(newCameraSelector)) {
        lensFacing = newLensFacing
        cameraSelector = newCameraSelector
        bindAllCameraUseCases()
        return
      }
    } catch (e: CameraInfoUnavailableException) {
      // Falls through
    }
    Toast.makeText(
            applicationContext, "This device does not have lens with facing: $newLensFacing",
            Toast.LENGTH_SHORT
    )
            .show()
  }

  public override fun onResume() {
    super.onResume()
    bindAllCameraUseCases()
  }

  override fun onPause() {
    super.onPause()

    imageProcessor?.run {
      this.stop()
    }
  }

  public override fun onDestroy() {
    super.onDestroy()
    imageProcessor?.run {
      this.stop()
    }
  }

  private fun bindAllCameraUseCases() {
    if (cameraProvider != null) {
      // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
      cameraProvider!!.unbindAll()
      bindPreviewUseCase()
      bindAnalysisUseCase()
    }
  }

  private fun bindPreviewUseCase() {
    if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) {
      return
    }
    if (cameraProvider == null) {
      return
    }
    if (previewUseCase != null) {
      cameraProvider!!.unbind(previewUseCase)
    }

    val builder = Preview.Builder()
    val targetResolution = PreferenceUtils.getCameraXTargetResolution(this)
    if (targetResolution != null) {
      builder.setTargetResolution(targetResolution)
    }
    previewUseCase = builder.build()
    previewUseCase!!.setSurfaceProvider(previewView!!.getSurfaceProvider())
    cameraProvider!!.bindToLifecycle(/* lifecycleOwner= */this, cameraSelector!!, previewUseCase)
  }

  private fun bindAnalysisUseCase() {
    if (cameraProvider == null) {
      return
    }
    if (analysisUseCase != null) {
      cameraProvider!!.unbind(analysisUseCase)
    }
    if (imageProcessor != null) {
      imageProcessor!!.stop()
    }
    imageProcessor = try {
      /**-------------------------------------------------------[select option]----*/
      when (selectedModel) {


          FACE_DETECTION -> {
              FaceDetectorProcessor(this,this)
          }




        else -> throw IllegalStateException("Invalid model name")
      }
    } catch (e: Exception) {
      Log.e(
              TAG,
              "Can not create image processor: $selectedModel",
              e
      )
      Toast.makeText(
              applicationContext,
              "Can not create image processor: " + e.localizedMessage,
              Toast.LENGTH_LONG
      )
              .show()
      return
    }

    val builder = ImageAnalysis.Builder()
    val targetResolution = PreferenceUtils.getCameraXTargetResolution(this)
    if (targetResolution != null) {
      builder.setTargetResolution(targetResolution)
    }
    analysisUseCase = builder.build()

    needUpdateGraphicOverlayImageSourceInfo = true

    analysisUseCase?.setAnalyzer(
            // imageProcessor.processImageProxy will use another thread to run the detection underneath,
            // thus we can just runs the analyzer itself on main thread.
            ContextCompat.getMainExecutor(this),
            ImageAnalysis.Analyzer { imageProxy: ImageProxy ->
              if (needUpdateGraphicOverlayImageSourceInfo) {
                val isImageFlipped =
                        lensFacing == CameraSelector.LENS_FACING_FRONT
                val rotationDegrees =
                        imageProxy.imageInfo.rotationDegrees
                if (rotationDegrees == 0 || rotationDegrees == 180) {
                  graphicOverlay!!.setImageSourceInfo(
                          imageProxy.width, imageProxy.height, isImageFlipped
                  )
                } else {
                  graphicOverlay!!.setImageSourceInfo(
                          imageProxy.height, imageProxy.width, isImageFlipped
                  )
                }
                needUpdateGraphicOverlayImageSourceInfo = false
              }
              try {
                imageProcessor!!.processImageProxy(imageProxy, graphicOverlay)
              } catch (e: MlKitException) {
                Log.e(
                        TAG,
                        "Failed to process image. Error: " + e.localizedMessage
                )
                Toast.makeText(
                        applicationContext,
                        e.localizedMessage,
                        Toast.LENGTH_SHORT
                )
                        .show()
              }
            }
    )
    cameraProvider!!.bindToLifecycle( /* lifecycleOwner= */this, cameraSelector!!, analysisUseCase)
  }

  private val requiredPermissions: Array<String?>
    get() = try {
      val info = this.packageManager
              .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
      val ps = info.requestedPermissions
      if (ps != null && ps.isNotEmpty()) {
        ps
      } else {
        arrayOfNulls(0)
      }
    } catch (e: Exception) {
      arrayOfNulls(0)
    }

  private fun allPermissionsGranted(): Boolean {
    for (permission in requiredPermissions) {
      if (!isPermissionGranted(this, permission)) {
        return false
      }
    }
    return true
  }

  private val runtimePermissions: Unit
    get() {
      val allNeededPermissions: MutableList<String?> = ArrayList()
      for (permission in requiredPermissions) {
        if (!isPermissionGranted(this, permission)) {
          allNeededPermissions.add(permission)
        }
      }
      if (allNeededPermissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(
                this,
                allNeededPermissions.toTypedArray(),
                PERMISSION_REQUESTS
        )
      }
    }

  override fun onRequestPermissionsResult(
          requestCode: Int,
          permissions: Array<String>,
          grantResults: IntArray
  ) {
    Log.i(TAG, "Permission granted!")
    if (allPermissionsGranted()) {
      bindAllCameraUseCases()
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  companion object {
    private const val TAG = "CameraXLivePreview"
    private const val PERMISSION_REQUESTS = 1
      private const val FACE_DETECTION = "Face Detection"
    private const val STATE_SELECTED_MODEL = "selected_model"
    private const val STATE_LENS_FACING = "lens_facing"

    private fun isPermissionGranted(
            context: Context,
            permission: String?
    ): Boolean {
      if (ContextCompat.checkSelfPermission(context, permission!!)
              == PackageManager.PERMISSION_GRANTED
      ) {
        Log.i(TAG, "Permission granted: $permission")
        return true
      }
      Log.i(TAG, "Permission NOT granted: $permission")
      return false
    }
  }

  override fun callback(face: Face) {
      var smile_rating = findViewById<SmileRating>(R.id.smile_rating)
      var smile = 0
      Log.i(TAG, "0000000000000: ${face.smilingProbability}")
      if (face != null) {
          if (face.smilingProbability > .8) {
              smile = BaseRating.GREAT
          } else if (face.smilingProbability <= .8 && face.smilingProbability > .6) {
              smile = BaseRating.GOOD
          } else if (face.smilingProbability <= .6 && face.smilingProbability > .4) {
              smile = BaseRating.OKAY
          } else if (face.smilingProbability <= .4 && face.smilingProbability > .2) {
              smile = BaseRating.BAD
          }
          smile_rating.setSelectedSmile(smile, true)
      }
  }

  }


  /**------------------------------------------------------------------------*/






