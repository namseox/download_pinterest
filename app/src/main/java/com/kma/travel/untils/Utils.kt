package com.kma.travel.utils

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputFilter
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kma.travel.R

import java.io.File

object Utils {
    fun checkPermissions(context: Context, permission: String) {
        if (ActivityCompat.checkSelfPermission(
                context, permission
            ) != PackageManager.PERMISSION_DENIED
        ) {
            SharedPreferenceUtils.getInstance(context).putPermissions(true)
        } else {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(permission),
                123
            )
        }

    }

    private var STORAGE_PERMISSION_UNDER_STORAGE_SCOPE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    private var WRITE_SETTING_PERMISSION = arrayOf(
        Manifest.permission.WRITE_SETTINGS
    )
    private var RECORD_PERMISSION = arrayOf(
        Manifest.permission.RECORD_AUDIO,
    )

    private var STORAGE_PERMISSION_STORAGE_SCOPE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @RequiresApi(33)
    private var STORAGE_PERMISSION_STORAGE_SCOPE_TIRAMISU = arrayOf(
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private const val REQUEST_CODE_PERMISSIONS = 10

    fun isAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun isAndroidTIRAMISU(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun isAndroidP(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    fun isAndroidO(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    fun isAndroidO_MR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1
    }

    fun isAndroidM(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun isAndroidR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    fun isAndroidLOLLIPOP(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    fun getPendingIntentFlags(): Int {
        return if (isAndroidM()) {
            if (isAndroidQ()) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_IMMUTABLE
            }

        } else {
            PendingIntent.FLAG_ONE_SHOT
        }
    }

    fun getAlarmManagerFlags(): Int {
        return if (isAndroidM()) {
            if (isAndroidQ()) {
                AlarmManager.RTC_WAKEUP
            } else {
                AlarmManager.RTC_WAKEUP
            }

        } else {
            AlarmManager.RTC_WAKEUP
        }
    }

    fun writeSettingPermissionGrant(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(context)
        } else {
            allPermissionGrant(context, getWritingPermission())
        }
    }


    fun storagePermissionGrant(context: Context): Boolean {
        return allPermissionGrant(context, getStoragePermissions())
    }

    fun recordPermissionGrant(context: Context): Boolean {
        return allPermissionGrant(context, getRecorderPermissions())
    }

    fun cameraPermissionsGranted(context: Context) = CAMERA_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context, it
        ) == PackageManager.PERMISSION_GRANTED
    }


    //check all permission is granted
    private fun allPermissionGrant(context: Context, intArray: Array<String>): Boolean {
        var isGranted = true
        for (permission in intArray) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isGranted = false
                break
            }
        }
        return isGranted
    }

    //get permission mapping with API Level
    fun getStoragePermissions(): Array<String> {
        return if (isAndroidR()) {
            if (isAndroidTIRAMISU()) {
                //33
                STORAGE_PERMISSION_STORAGE_SCOPE_TIRAMISU
            } else {
                //>=29
                STORAGE_PERMISSION_STORAGE_SCOPE
            }

        } else {
            STORAGE_PERMISSION_UNDER_STORAGE_SCOPE
        }
    }

    fun getRecordPermissions(): Array<String> {
        return RECORD_PERMISSION
    }

    fun getRecorderPermissions(): Array<String> {
        return RECORD_PERMISSION
    }

    fun getWritingPermission(): Array<String> {
        return WRITE_SETTING_PERMISSION
    }

    fun getCameraPermission(): Array<String> {
        return CAMERA_PERMISSIONS
    }

    //show request permission to user
    private fun hasShowRequestPermissionRationale(
        activity: Activity?,
        vararg permissions: String?
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null) {
            for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permission!!
                    )
                ) {
                    return true
                }
            }
        }
        return false
    }


    fun showOverOtherAppPermission(activity: Activity) {
        val intent = Intent()
        intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)
    }


    fun getBasePath(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath
    }

    fun requestWriteSettingPermission(activity: FragmentActivity, context: Context) {
        Toast.makeText(
            context,
            activity.resources?.getString(R.string.goto_settings),
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent()
        intent.action = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.ACTION_MANAGE_WRITE_SETTINGS
        } else {
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        }
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
    }


    private fun getUriForUnderAndroidQ(
        context: Context,
        file: File,
        filePath: String,
        fileName: String
    ): Uri? {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DATA, filePath)
        contentValues.put(MediaStore.MediaColumns.TITLE, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")
        contentValues.put(MediaStore.MediaColumns.SIZE, file.length())
        contentValues.put(MediaStore.Audio.Media.IS_RINGTONE, true)

        val uri = MediaStore.Audio.Media.getContentUriForPath(filePath)
        val cursor: Cursor? = context.contentResolver.query(
            uri!!, null, MediaStore.MediaColumns.DATA + "=?", arrayOf(
                filePath
            ), null
        )
        return if (cursor != null && cursor.moveToFirst() && cursor.count > 0) {
            val id: String = cursor.getString(0)
            context.contentResolver.update(
                uri, contentValues, MediaStore.MediaColumns.DATA + "=?", arrayOf<String>(
                    filePath
                )
            )
            cursor.close()
            ContentUris.withAppendedId(uri, java.lang.Long.valueOf(id))
        } else {
            context.contentResolver.insert(uri, contentValues)
        }
    }

    fun showAlertPermissionNotGrant(view: View, activity: Activity) {
        if (!hasShowRequestPermissionRationale(activity, *getStoragePermissions())) {
            val snackBar = Snackbar.make(
                view,
                activity.resources.getString(R.string.goto_settings),
                Snackbar.LENGTH_LONG
            )
            snackBar.setAction(
                activity.resources.getString(R.string.settings)
            ) { view: View? ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            snackBar.show()
        } else {
            Toast.makeText(
                activity,
                activity.resources.getString(R.string.grant_permission),
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    fun scanMediaFile(context: Context, files: MutableList<File>, callBack: () -> Unit) {
        val mimeType = files.map {
            MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(it.extension)
        }.toTypedArray()
        val paths = files.map { it.absolutePath }.toTypedArray()
        MediaScannerConnection.scanFile(
            context, paths, mimeType
        ) { _, _ ->
            callBack.invoke()
        }
    }


    fun Fragment.setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun View.setMargin(start: Int, top: Int, end: Int, bottom: Int) {
        val layout = layoutParams as ViewGroup.MarginLayoutParams

        layout.setMargins(start, top, end, bottom)
        layoutParams = layout
    }

    val Number.toPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )

    fun getSongCover(path: String?): ByteArray? {
        val mmr = MediaMetadataRetriever()
        var data: ByteArray? = null
        try {
            mmr.setDataSource(path)
        } catch (e: Exception) {
            return null
        }
        data = mmr.embeddedPicture
        mmr.release()
        return data
    }

    fun convertDuration(duration: String): String {
        val result = duration.substring(2, duration.length - 1).toLong()

        val seconds = (result) % 60
        val minutes = (result / 60) % 60

        return "%02d:%02d".format(minutes, seconds)
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private const val URL_WEB1 = "https://id.pinterest.com"
    private const val URL_WEB2 = "https://www.pinterest.com"
    private const val URL_MOBILE = "https://pin.it"
    fun validUrl(url: String): Boolean {
        if (url.contains(URL_WEB1, true) || url.contains(URL_MOBILE, true) || url.contains(
                URL_WEB2,
                true
            )
        ) {
            return true
        }
        return false
    }

    const val FOLDER_PATH = "PinterestDownloader"


    fun View.setOnSingClickListener(onClick: (View) -> Unit) {
        setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(view: View) {
                onClick.invoke(view)
            }
        })
    }

    const val UriMedia = "URI MEDIA"

    fun Fragment.getNavigationResult(key: String = "result") =
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

    fun Fragment.setNavigationResult(result: String, key: String = "result") {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
    }

    fun Activity.shareApp(): String {
        val appPackage = this.packageName
        return try {
            //Uri.parse("market://details?id=$appPackage").toString()
            Uri.parse("https://play.google.com/store/apps/details?id=$appPackage").toString()
        } catch (e: ActivityNotFoundException) {
            Uri.parse("https://play.google.com/store/apps/details?id=$appPackage").toString()

        }
    }




}