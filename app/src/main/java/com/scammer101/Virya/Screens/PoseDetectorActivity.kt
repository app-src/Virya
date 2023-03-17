package com.scammer101.Virya.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.scammer101.Virya.Models.Draw
import com.scammer101.Virya.Models.PoseDetectionUtils
import com.scammer101.Virya.Utilities.ConstantsValues
import com.scammer101.Virya.Utilities.PreferenceManager
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
    private lateinit var yogaPose : String
    private var start : Long = 0
    private var end : Long = 0
    private var poseCount : Int = 0
    private var preferenceManager: PreferenceManager? = null
    private lateinit var firestore : FirebaseFirestore

    private lateinit var bitmapBuffer: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPoseDetectorBinding = ActivityPoseDetectorBinding.inflate(layoutInflater)
        setContentView(activityPoseDetectorBinding.root)
        // Request camera permissions
        cameraFacing = CameraSelector.LENS_FACING_FRONT
        firestore = FirebaseFirestore.getInstance()
        yogaPose = intent.getStringExtra("yoga").toString()
        setStatusBarColor(Color.parseColor("#000000"))
        preferenceManager = PreferenceManager(this)
        if (allPermissionsGranted()) {
            start = System.currentTimeMillis()
            //start camera
            cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener(Runnable {
                cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)

            },ContextCompat.getMainExecutor(this))
            cameraExecutor = Executors.newSingleThreadExecutor()

            //start pose detector
            val poseDetectorOptions = AccuratePoseDetectorOptions.Builder()
                .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                .build()
            poseDetector = PoseDetection.getClient(poseDetectorOptions)


            activityPoseDetectorBinding.switchCamera.setOnClickListener(View.OnClickListener {
                if(cameraFacing == CameraSelector.LENS_FACING_FRONT){
                    cameraFacing = CameraSelector.LENS_FACING_BACK
                }else{
                    cameraFacing = CameraSelector.LENS_FACING_FRONT
                }
                cameraProvider.unbindAll()
                cameraExecutor.shutdown()

                //start camera
                cameraProviderFuture = ProcessCameraProvider.getInstance(this)
                cameraProviderFuture.addListener(Runnable {
                    cameraProvider = cameraProviderFuture.get()
                    bindPreview(cameraProvider)

                },ContextCompat.getMainExecutor(this))
                cameraExecutor = Executors.newSingleThreadExecutor()

            })
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun startCamera() {
        setProcessor()



    }

    fun setProcessor(){

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        end = System.currentTimeMillis()
        val timeSpent : Long = end-start
        val seconds: Long = timeSpent / 1000
        Log.d("timeSpent", seconds.toString())
        var isFinish : Int = 0
        if(poseCount>0)
        {
            isFinish = 1
        }

        if(yogaPose == "treepose")
        {
            val todayUserDateAndId = preferenceManager!!.getString(ConstantsValues.KEY_DATE)
            if(isFinish==1)
            {
                val userDocRef = firestore.collection("DailyYoga").document(todayUserDateAndId)
                userDocRef.update("finished", FieldValue.increment(1))
                userDocRef.update("treePoseCountPose", FieldValue.increment(1))
                userDocRef.update("treePoseCountTimer", FieldValue.increment(seconds))
                userDocRef.update("timeSpent", FieldValue.increment(seconds))
            }
            else {
                val userDocRef = firestore.collection("DailyYoga").document(todayUserDateAndId)
                userDocRef.update("inProgress", FieldValue.increment(1))
                userDocRef.update("treePoseCountTimer", FieldValue.increment(seconds))
                userDocRef.update("timeSpent", FieldValue.increment(seconds))
            }


        } else if(yogaPose == "warrior2pose")
            {
                val todayUserDateAndId = preferenceManager!!.getString(ConstantsValues.KEY_DATE)
                if(isFinish==1)
                {
                    val userDocRef = firestore.collection("DailyYoga").document(todayUserDateAndId)
                    userDocRef.update("finished", FieldValue.increment(1))
                    userDocRef.update("warriorPoseCountPose", FieldValue.increment(1))
                    userDocRef.update("warriorPoseCountTimer", FieldValue.increment(seconds))
                    userDocRef.update("timeSpent", FieldValue.increment(seconds))
                }
                else {
                    val userDocRef = firestore.collection("DailyYoga").document(todayUserDateAndId)
                    userDocRef.update("inProgress", FieldValue.increment(1))
                    userDocRef.update("warriorPoseCountTimer", FieldValue.increment(seconds))
                    userDocRef.update("timeSpent", FieldValue.increment(seconds))
                }


            }
        else
        {
            val todayUserDateAndId = preferenceManager!!.getString(ConstantsValues.KEY_DATE)
            if(isFinish==1)
            {
                val userDocRef = firestore.collection("DailyYoga").document(todayUserDateAndId)
                userDocRef.update("finished", FieldValue.increment(1))
                userDocRef.update("tPoseCountPose", FieldValue.increment(1))
                userDocRef.update("tPoseCountTimer", FieldValue.increment(seconds))
                userDocRef.update("timeSpent", FieldValue.increment(seconds))
            }
            else {
                val userDocRef = firestore.collection("DailyYoga").document(todayUserDateAndId)
                userDocRef.update("inProgress", FieldValue.increment(1))
                userDocRef.update("tPoseCountTimer", FieldValue.increment(seconds))
                userDocRef.update("timeSpent", FieldValue.increment(seconds))
            }


        }

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
        "UnsafeOptInUsageError", "SetTextI18n"
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
                            if(activityPoseDetectorBinding.parentLayout.childCount>3){
                                activityPoseDetectorBinding.parentLayout.removeViewAt(3)
                            }
                            var i = 0
                            var overallLikelyHood = 0f
                            for (pose in it.allPoseLandmarks){
                                overallLikelyHood+=pose.inFrameLikelihood
                                i+=1
                            }
                            overallLikelyHood/=i
                            if(overallLikelyHood>0.5){
                                val element = Draw(applicationContext,it)
                                var poseDetectionUtils = PoseDetectionUtils()
                                var angleList = poseDetectionUtils.pose_angles(it)
                                Log.v("angleCheck", angleList.toString())
                                var accuracy = 00.00
                                if(yogaPose == "treepose")
                                {
                                    accuracy = poseDetectionUtils.accuracy_Treepose(angleList)
                                    Log.d("Posename", yogaPose.toString())
                                    if(accuracy>=70)
                                    {
                                        poseCount++
                                    }
                                }
                                else if(yogaPose == "warrior2pose")
                                {
                                    accuracy = poseDetectionUtils.accuracy_Warrior2pose(angleList)
                                    Log.d("Posename", yogaPose.toString())
                                    if(accuracy>=70)
                                    {
                                        poseCount++
                                    }
                                }
                                else
                                {
                                    accuracy = poseDetectionUtils.accuracy_Tpose(angleList)
                                    Log.d("Posename", yogaPose.toString())
                                    if(accuracy>=70)
                                    {
                                        poseCount++
                                    }
                                }

                                Log.d("Tree Pose Accuracy:","$accuracy")
                                runOnUiThread(Runnable {
                                    activityPoseDetectorBinding.accuracy.visibility = View.VISIBLE
                                    activityPoseDetectorBinding.accuracy.text = "${accuracy.toInt()}%"
                                })
                                activityPoseDetectorBinding.parentLayout.addView(element)
                            }else{activityPoseDetectorBinding.accuracy.visibility = View.GONE}
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
