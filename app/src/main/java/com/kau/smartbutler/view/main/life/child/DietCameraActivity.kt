package com.kau.smartbutler.view.main.life.child

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.util.MealCameraPreview
import kotlinx.android.synthetic.main.activity_diet.*
import kotlinx.android.synthetic.main.activity_diet_camera.*

class DietCameraActivity (
        override val layoutRes: Int= R.layout.activity_diet,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    private val PERMISSIONS_REQUEST_CODE = 100
    private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private var CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_FRONT
    private var mealCameraPreview: MealCameraPreview? = null
    override fun setupView() {
        super.setupView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // OS가 Marshmallow 이상일 경우 권한체크를 해야 합니다.

            val permissionCheckCamera
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            val permissionCheckStorage
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (permissionCheckCamera ==
                    PackageManager.PERMISSION_GRANTED && permissionCheckStorage == PackageManager.PERMISSION_GRANTED) {

                // 권한 있음
                startCamera()
            } else {

                // 권한 없음
                ActivityCompat.requestPermissions(this,
                        REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE)
            }


        } else {
            // OS가 Marshmallow 이전일 경우 권한체크를 하지 않는다.
            Log.d("MyTag", "마시멜로 버전 이하로 권한 이미 있음")
            startCamera()

        }

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tv_camera_dummy -> {
                mealCameraPreview?.takePicture()
            }
        }
    }

    private fun startCamera() {
        mealCameraPreview = MealCameraPreview(this, CAMERA_FACING)
        cameraPreview.addView(mealCameraPreview)
    }
}
