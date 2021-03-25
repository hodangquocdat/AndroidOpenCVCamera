package com.dathdq.androidopencvcamera

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.dathdq.androidopencvcamera.MyGLSurfaceView
import android.os.Bundle
import android.content.pm.PackageManager
import android.widget.Toast
import android.content.pm.ActivityInfo
import com.dathdq.androidopencvcamera.R
import android.widget.CompoundButton
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.CameraGLSurfaceView
import org.opencv.android.CameraGLSurfaceView.CameraTextureListener
import android.widget.TextView
import android.os.Looper
import android.os.Process
import android.view.*
import android.widget.Switch
import com.dathdq.androidopencvcamera.databinding.ActivityBinding

class CameraActivity : AppCompatActivity() {
    companion object {
        private const val MY_PERMISSIONS_REQUEST_CAMERA = 1337
        private fun checkSelfPermission(context: Context, permission: String): Int {
            return context.checkPermission(permission, Process.myPid(), Process.myUid())
        }

        // Used to load the 'native-lib' and 'opencv' libraries on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
    private lateinit var binding: ActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check for camera permissions
        val context = applicationContext
        if (checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // Show some text
                Toast.makeText(context, "Need access to your camera to proceed", Toast.LENGTH_LONG).show()
                finish()
            } else {
                // No explanation needed; request the permission
                requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
            }
        } else {
            setupApplication()
        }
    }

    private fun setupApplication() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        theme.applyStyle(android.R.style.Theme_Light_NoTitleBar_Fullscreen, true)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        binding = ActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup switch to swap between back and front camera
        binding.cameraSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.myGlSurfaceView.setFrontFacing(true)
                binding.myGlSurfaceView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT)
            } else {
                binding.myGlSurfaceView.setFrontFacing(false)
                binding.myGlSurfaceView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK)
            }
        }
        binding.myGlSurfaceView.setMaxCameraPreviewSize(1080, 1920)
        binding.myGlSurfaceView.holder.setFixedSize(1920, 1080)
        binding.myGlSurfaceView.cameraTextureListener = binding.myGlSurfaceView
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
                }
            }
        }
    }

    override fun onResume() {
        binding.myGlSurfaceView.onResume()
        super.onResume()
    }

    public override fun onPause() {
        binding.myGlSurfaceView.onPause()
        super.onPause()
    }
}