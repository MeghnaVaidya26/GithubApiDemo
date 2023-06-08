package com.example.githubapidemo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.WindowCompat
import com.example.githubapidemo.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class Utils {
    companion object {
        @SuppressLint("RestrictedApi")
        fun createFullScreen(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                activity.window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            } else {
                @Suppress("DEPRECATION") activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }

        fun pasteTextFromClipboard(context: Context): String {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            return clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
        }

        fun copyTextToClipboard(text: String, context: Context) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", text)
            clipboardManager.setPrimaryClip(clipData)

        }

        private fun getBottomSheetDialogDefaultHeight(context: Context, percetage: Int): Int {
            return getWindowHeight(context) * percetage / 100
        }

        private fun getWindowHeight(context: Context): Int {
            // Calculate window height for fullscreen use
            val displayMetrics = DisplayMetrics()
            (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }



        enum class Device {
            DEVICE_TYPE
        }

        fun getDeviceInfo(context: Context, device: Device): String? {
            try {
                if (device == Device.DEVICE_TYPE) {
                    return if (isTablet(context)) {
                        if (getDevice5Inch(context)) {
                            "This is Tablet"
                        } else {
                            "This is Mobile"
                        }
                    } else {
                        "This is Mobile"
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return ""
        }

        private fun getDevice5Inch(context: Context): Boolean {
            return try {
                val displayMetrics = context.resources.displayMetrics
                val yinch = displayMetrics.heightPixels / displayMetrics.ydpi
                val xinch = displayMetrics.widthPixels / displayMetrics.xdpi
                val diagonalinch = Math.sqrt((xinch * xinch + yinch * yinch).toDouble())
                diagonalinch >= 7
            } catch (e: java.lang.Exception) {
                false
            }
        }

        private fun isTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        }





    }


}
