package com.example.admin.echomusic.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.example.admin.echomusic.R

class SplashActivity : AppCompatActivity() {

    var permissionString = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.PROCESS_OUTGOING_CALLS,
            android.Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (!hasPermissions(this@SplashActivity, *permissionString)) {    // ask android to grant the permissions

            ActivityCompat.requestPermissions(this, permissionString, 131)
        } else {
            Handler().postDelayed({
                var intent_obj = Intent(this@SplashActivity, MainActivity::class.java)
                this.startActivity(intent_obj)

            }
                    , 1000)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            131 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    Handler().postDelayed({
                        var intent_obj = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent_obj)

                    }
                            , 3000)
                } else {
                    Toast.makeText(this@SplashActivity, "Please grant all permissions to continue.", Toast.LENGTH_SHORT)

                }
                return
            }
            else -> {
                Toast.makeText(this@SplashActivity, "Something went wrong.", Toast.LENGTH_SHORT)

                return
            }

        }

    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        var hasAllPermission = true
        for (p in permissions) {
            var res = context.checkCallingOrSelfPermission(p)
            if (res != PackageManager.PERMISSION_GRANTED) {
                hasAllPermission = false
            }
        }
        return hasAllPermission
    }

}
