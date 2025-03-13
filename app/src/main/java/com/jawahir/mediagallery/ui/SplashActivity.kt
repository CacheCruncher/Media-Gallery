package com.jawahir.mediagallery.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.databinding.ActivitySplashScreenBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private val permissionResultLauncher =
        registerForActivityResult(RequestMultiplePermissions()) {
            handlePermissionResults(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        if (hasAllPermissions()) navigateToMain()
        else permissionResultLauncher.launch(getPermissionsToRequest())
    }

    private fun hasAllPermissions() = getPermissionsToRequest().all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getPermissionsToRequest(): Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    private fun handlePermissionResults(permissions: Map<String, Boolean>) {
        when {
            permissions.all { it.value } -> navigateToMain()
            permissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    it.key
                )
            } -> showPermissionDialog()

            else -> showSettingsOption()
        }
    }

    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.media_permission_title))
            .setMessage(getString(R.string.media_permission_description))
            .setPositiveButton(getString(R.string.allow)) { _, _ -> checkAndRequestPermissions() }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> showRetryButton() }
            .setIcon(R.drawable.ic_permission_icon)
            .setCancelable(false)
            .show()
    }

    private fun showSettingsOption() {
        binding.apply {
            permissionInfo.text = getString(R.string.permission_settings_info)
            permissionInfo.isVisible = true
            retryButton.text = getString(R.string.go_to_settings)
            retryButton.setOnClickListener { openAppSettings() }
            retryButton.isVisible = true
        }
    }

    private fun showRetryButton() {
        binding.apply {
            permissionInfo.isVisible = true
            retryButton.apply {
                text = getString(R.string.retry)
                isVisible = true
                setOnClickListener { checkAndRequestPermissions() }
            }
        }
    }

    private fun openAppSettings() {
        startActivity(
            Intent(
                ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
        )
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
