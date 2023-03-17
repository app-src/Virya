package com.scammer101.Virya.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Color
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.LensFacing
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.common.MlKit
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.scammer101.Virya.Models.Draw
import com.scammer101.Virya.R
import com.scammer101.Virya.databinding.ActivityPoseDetectorBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PoseDetectorActivity : AppCompatActivity() {
    private lateinit var activityPoseDetectorBinding: ActivityPoseDetectorBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var poseDetector: PoseDetector
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private var cameraFacing: Int = 0

    private lateinit var bitmapBuffer: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPoseDetectorBinding = ActivityPoseDetectorBinding.inflate(layoutInflater)
        setContentView(activityPoseDetectorBinding.root)
        // Request camera permissions
        cameraFacing = CameraSelector.LENS_FACING_FRONT
        val yogaPose :String = intent.getStringExtra("yoga").toString()
        setStatusBarColor(Color.parseColor("#000000"))
        Toast.makeText(applicationContext, yogaPose,Toast.LENGTH_SHORT).show()
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        activityPoseDetectorBinding.switchCamera.setOnClickListener(View.OnClickListener {
            if(cameraFacing == CameraSelector.LENS_FACING_FRONT){
                cameraFacing = CameraSelector.LENS_FACING_BACK
            }else{
                cameraFacing = CameraSelector.LENS_FACING_FRONT
            }
            if(cameraProvider!=null && cameraExecutor!=null) {
                cameraExecutor.shutdown()
                cameraProvider.unbindAll()
            }else(startCamera())

        })
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        setProcessor()

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)

        },ContextCompat.getMainExecutor(this))

    }

    fun setProcessor(){
        val poseDetectorOptions = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
        poseDetector = PoseDetection.getClient(poseDetectorOptions)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraX-MLKit"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).toTypedArray()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    fun Activity.setStatusBarColor(color: Int) {
        var flags = window?.decorView?.systemUiVisibility // get current flag
        if (flags != null) {
            if (isColorDark(color)) {
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                window?.decorView?.systemUiVisibility = flags
            } else {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window?.decorView?.systemUiVisibility = flags
            }
        }
        window?.statusBarColor = color
    }

    fun Activity.isColorDark(color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }
    @SuppressLint("RestrictedApi", "UnsafeExperimentalUsageError", "NewApi",
        "UnsafeOptInUsageError"
    )
    private fun bindPreview(cameraProvider: ProcessCameraProvider){

        Log.d("Check:","inside bind preview")

        val preview = Preview.Builder().build()

        preview.setSurfaceProvider(activityPoseDetectorBinding.previewView.surfaceProvider)

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(cameraFacing)
            .build()


        val point = Point()
        val size = display?.getRealSize(point)



        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), ImageAnalysis.Analyzer { imageProxy ->

            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = imageProxy.image

            if(image!=null){

                val processImage = InputImage.fromMediaImage(image,rotationDegrees)

                poseDetector.process(processImage)
                    .addOnSuccessListener {
                        if(activityPoseDetectorBinding.parentLayout.childCount>3){
                            activityPoseDetectorBinding.parentLayout.removeViewAt(3)
                        }
                        if(it.allPoseLandmarks.isNotEmpty()){

//                            Log.d("this is pose",it.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)!!.position.x .toString())

                            if(activityPoseDetectorBinding.parentLayout.childCount>3){
                                activityPoseDetectorBinding.parentLayout.removeViewAt(3)
                            }

                            val element = Draw(applicationContext,it)
                            activityPoseDetectorBinding.parentLayout.addView(element)
                        }
                        imageProxy.close()
                    }
                    .addOnFailureListener{


                        imageProxy.close()
                    }
            }


        })

        cameraProvider.bindToLifecycle(this,cameraSelector,imageAnalysis,preview)

    }

}