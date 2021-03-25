package com.dathdq.androidopencvcamera

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.CameraGLSurfaceView
import org.opencv.android.CameraGLSurfaceView.CameraTextureListener

class MyGLSurfaceView(context: Context?, attrs: AttributeSet?) : CameraGLSurfaceView(context, attrs), CameraTextureListener {
    private var frameCounter = 0
    private var lastNanoTime: Long = 0
    private var frontFacing = false
    private var mFpsText: TextView? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (e.action == MotionEvent.ACTION_DOWN) (context as AppCompatActivity).openOptionsMenu()
        return true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        super.surfaceCreated(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        super.surfaceDestroyed(holder)
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        (context as AppCompatActivity).runOnUiThread { Toast.makeText(context, "onCameraViewStarted", Toast.LENGTH_SHORT).show() }
        frameCounter = 0
        lastNanoTime = System.nanoTime()
    }

    override fun onCameraViewStopped() {
        (context as AppCompatActivity).runOnUiThread { Toast.makeText(context, "onCameraViewStopped", Toast.LENGTH_SHORT).show() }
    }

    fun setFrontFacing(frontFacing: Boolean) {
        this.frontFacing = frontFacing
    }

    @SuppressLint("SetTextI18n")
    override fun onCameraTexture(texIn: Int, texOut: Int, width: Int, height: Int): Boolean {
        // FPS
        frameCounter++
        if (frameCounter >= 30) {
            val fps = (frameCounter * 1e9 / (System.nanoTime() - lastNanoTime)).toInt()
            Log.i(LOGTAG, "drawFrame() FPS: $fps")
            if (mFpsText != null) {
                val fpsUpdater = Runnable { mFpsText!!.text = "FPS: $fps" }
                Handler(Looper.getMainLooper()).post(fpsUpdater)
            } else {
                Log.d(LOGTAG, "mFpsText == null")
                mFpsText = (context as AppCompatActivity).findViewById<View>(R.id.fps_text_view) as TextView
            }
            frameCounter = 0
            lastNanoTime = System.nanoTime()
        }
        processFrame(texIn, texOut, width, height, frontFacing)
        return true
    }

    companion object {
        const val LOGTAG = "MyGLSurfaceView"

    }
    external fun processFrame(tex1: Int, tex2: Int, w: Int, h: Int, frontFacing: Boolean)
}