package com.example

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.afollestad.materialdialogs.MaterialDialog.Builder
import com.afollestad.materialdialogs.Theme

import android.content.ComponentName
import android.net.Uri
import android.content.pm.PackageManager
import android.support.v4.app.SupportActivity
//import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.app.ActivityCompat
//import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


object Constants {

    val AUTO_START_INTENTS = arrayOf(Intent().setComponent(ComponentName("com.samsung.android.lool",
            "com.samsung.android.sm.ui.battery.BatteryActivity")), Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT), Intent().setComponent(ComponentName("com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity")), Intent().setComponent(
            ComponentName("com.letv.android.letvsafe",
                    "com.letv.android.letvsafe.AutobootManageActivity")), Intent().setComponent(
            ComponentName("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity")), Intent().setComponent(
            ComponentName("com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity")), Intent().setComponent(ComponentName("com.coloros.safecenter",
            "com.coloros.safecenter.startupapp.StartupAppListActivity")), Intent().setComponent(
            ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")), Intent().setComponent(ComponentName("com.iqoo.secure",
            "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")), Intent().setComponent(
            ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")), Intent().setComponent(ComponentName("com.vivo.permissionmanager",
            "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")), Intent().setComponent(
            ComponentName("com.asus.mobilemanager",
                    "com.asus.mobilemanager.entry.FunctionActivity")).setData(
            Uri.parse("mobilemanager://function/entry/AutoStart")))
}

class LoginActivity : AppCompatActivity() {

    private val REQUEST_RECORD_PERMISSION = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this@LoginActivity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_PERMISSION)
        setContentView(R.layout.activity_main)
//        enableAutoStart()
        btnLogin.setOnClickListener {
            val email=edtEmail.text.toString();
            val password=edtPassword.text.toString();
            var intent= Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService(Intent(this@LoginActivity, MyService::class.java))
            } else {
                Toast.makeText(this@LoginActivity, "Permission Denied!", Toast
                        .LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
//        stopService(Intent(this, MyService::class.java))
    }


//    private fun enableAutoStart() {
//        for (intent in Constants.AUTO_START_INTENTS) {
//            if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
//                Builder(this).title("ENABLE")
//                        .content("ARE YOU SURE")
//                        .theme(Theme.LIGHT)
//                        .positiveText("ALLOW")
//                        .onPositive({ dialog, which ->
//                            try {
//                                for (intent1 in Constants.AUTO_START_INTENTS)
//                                    if (packageManager.resolveActivity(intent1, PackageManager.MATCH_DEFAULT_ONLY) != null) {
//                                        startActivity(intent1)
//                                        break
//                                    }
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//                        })
//                        .show()
//                break
//            }
//        }
//    }

}
