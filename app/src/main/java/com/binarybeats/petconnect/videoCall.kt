package com.binarybeats.petconnect

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas

class videoCall : AppCompatActivity() {
    private val PERMISSION_ID = 12
    private val app_id = "36466575ce8a45c8b66c3efe957d9afd"
    private val channelName = "voicecallnew"
    private val token = "007eJxTYDhx41+D+KZb3w6sUwqwaP36fC+/dqBuxK/ppf+U0y6axskrMBibmZiZmZqbJqdaJJqYJlskmZklG6empVqamqdYJqalbPnvlNYQyMhg8aOTlZEBAkF8Hoay/Mzk1OTEnJy81HIGBgAbXiUn"
    private var uid = 0
    private lateinit var name: String
    private var isJoined = false
    private var agoraEngine: RtcEngine? = null
    private var localSurfaceView: SurfaceView? = null
    private var remoteSurfaceView: SurfaceView? = null
    private val REQUESTED_PERMISSION = arrayOf(
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.CAMERA
    )
    private var isFrontCamera = true // Track which camera is currently active

    private fun checkSelfPermission(): Boolean {
        return !(ContextCompat.checkSelfPermission(
            this, REQUESTED_PERMISSION[0]
        ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this, REQUESTED_PERMISSION[1]
                ) != PackageManager.PERMISSION_GRANTED)
    }

    private fun setupVideoSdkEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = app_id
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            agoraEngine!!.enableVideo()
        } catch (e: Exception) {
            // Handle exception
        }
    }

    private var startTime: Long = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)

        if (!checkSelfPermission()) {
            ActivityCompat
                .requestPermissions(
                    this, REQUESTED_PERMISSION, PERMISSION_ID
                )
        }
        name = intent.getStringExtra("username") ?: ""


        setupVideoSdkEngine()
        joinCall()

        // Start timer
        startTime = SystemClock.elapsedRealtime()
        startTimer()


    }

    private fun leaveCall() {
        if (!isJoined) {
            // Handle not joined scenario
        } else {
            agoraEngine?.leaveChannel()
            remoteSurfaceView?.visibility = View.GONE
            localSurfaceView?.visibility = View.GONE

            isJoined = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        agoraEngine?.stopPreview()
        agoraEngine?.leaveChannel()

        Thread {
            RtcEngine.destroy()
            agoraEngine = null
        }.start()
    }

    private fun joinCall() {
        if (checkSelfPermission()) {
            val option = ChannelMediaOptions()
            option.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            option.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            setupLocalVideo()
            localSurfaceView?.visibility = View.VISIBLE
            agoraEngine?.startPreview()
            agoraEngine?.joinChannel(token, channelName, uid, option)
        }
    }

    private val mRtcEventHandler: IRtcEngineEventHandler =
        object : IRtcEngineEventHandler() {
            override fun onUserJoined(uid: Int, elapsed: Int) {
                runOnUiThread { setupRemoteVideo(uid) }
            }

            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                isJoined = true
            }

            override fun onUserOffline(uid: Int, reason: Int) {
                runOnUiThread {
                    remoteSurfaceView?.visibility = View.GONE
                }
            }
        }

    private fun setupRemoteVideo(uid: Int) {
        remoteSurfaceView = SurfaceView(baseContext)
        remoteSurfaceView?.setZOrderMediaOverlay(false)
        val remoteView = findViewById<FrameLayout>(R.id.remote_user)
        remoteView.addView(remoteSurfaceView)

        agoraEngine?.setupRemoteVideo(
            VideoCanvas(
                remoteSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                uid
            )
        )
    }

    private fun setupLocalVideo() {
        localSurfaceView = SurfaceView(baseContext)
        localSurfaceView?.setZOrderMediaOverlay(true)
        val localView = findViewById<FrameLayout>(R.id.local_user)
        localView.addView(localSurfaceView)

        agoraEngine?.setupLocalVideo(
            VideoCanvas(
                localSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                0
            )
        )
    }

    private fun startTimer() {
        handler.postDelayed(timerRunnable, 1000)
    }


    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            val elapsedTime = SystemClock.elapsedRealtime() - startTime
            val seconds = (elapsedTime / 1000) % 60
            val minutes = (elapsedTime / 1000) / 60
            val timeString = String.format("%02d:%02d", minutes, seconds)
            findViewById<TextView>(R.id.textView3).text = timeString
            handler.postDelayed(this, 1000)
        }

    }

    fun onCameraHideClick(view: View) {
        localSurfaceView?.visibility = if (localSurfaceView?.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
    }

    fun onNoVoiceClick(view: View) {
        if (agoraEngine != null) {
            agoraEngine!!.muteLocalAudioStream(true) // Mute the local audio stream
        }
    }





    fun onChangeCameraClick(view: View) {
        isFrontCamera = !isFrontCamera
        agoraEngine?.switchCamera()

        localSurfaceView?.visibility = View.INVISIBLE

        Handler().postDelayed({
            localSurfaceView?.visibility = View.VISIBLE
        }, 500)
    }

    fun backbookappointment(view: View) {
        leaveCall()

        val intent = Intent(this, Chat::class.java)
        // Pass the species name as an argument to the next activity
        intent.putExtra("username", name)
        startActivity(intent)
        finish()
    }
}