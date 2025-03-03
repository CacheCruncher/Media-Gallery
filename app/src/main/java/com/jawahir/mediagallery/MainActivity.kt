package com.jawahir.mediagallery

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.jawahir.mediagallery.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionResultLauncher: ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        permissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            handlePermissionResults(perms)
        }

        binding.permissionReqButton.setOnClickListener {
            openAppSettings()
        }

        permissionRequestLauncher()
    }

    private fun permissionRequestLauncher() {
        val permissionsToRequest = permissions()
        permissionResultLauncher.launch(permissionsToRequest)
    }

    private fun permissions(): Array<String> {
        val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return permissionsToRequest
    }


    private fun handlePermissionResults(perms: Map<String, Boolean>) {
        val allGranted = perms.all { it.value }

        if (allGranted && perms.isNotEmpty()) {
            initView()
        } else {
            val deniedPermissions = perms.filter { !it.value }.keys
            handleDeniedPermissions(deniedPermissions.toList())
        }
    }

    private fun handleDeniedPermissions(deniedPermissions: List<String>) {
        if (deniedPermissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this, it
                )
            }) {
            showPermissionDialog()
        } else {
            showSetting()
        }
    }

    private fun initView() {
        with(binding) {
            navHostFragment.isVisible = true
            permissionReqButton.isVisible = false
        }
    }


    private fun showPermissionDialog() {
        AlertDialog.Builder(this, R.style.AlertDialogTheme_MediaGallery)
            .setTitle(getString(R.string.media_permission_title))
            .setMessage(getString(R.string.media_permission_description))
            .setPositiveButton(getString(R.string.allow)) { _, _ -> permissionRequestLauncher() }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> showSetting() }
            .setCancelable(false).show()

    }

    private fun showSetting() {
        with(binding) {
            permissionReqButton.isVisible = true
            navHostFragment.isVisible = false
        }
    }

    private fun openAppSettings() {
        Intent(
            ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)
        ).also(::startActivity)
    }
}