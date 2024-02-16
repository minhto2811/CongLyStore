package com.mgok.conglystore.presentation.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class MapViewModel @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
) : ViewModel() {

    private val _state = MutableStateFlow(MapStateUI())
    val state = _state.asStateFlow()


    @SuppressLint("MissingPermission")
    fun getLocationCurrent(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val sharedPref = context.getSharedPreferences(
                    "location", Context.MODE_PRIVATE
                )
                _state.update {
                    it.copy(
                        location = sharedPref.getString("location", "").toString()
                    )
                }
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await()

                val address = Geocoder(context, Locale.getDefault()).getAddress(location)
                val text = address?.adminArea.toString()
                if (text.isNotEmpty()){
                    _state.update { it.copy(location = text) }
                    with(sharedPref.edit()) {
                        putString("location", text)
                        apply()
                    }
                }else{
                    _state.update { it.copy(location = "Vị trí chưa được cập nhật") }
                }
                Log.e("ghg getLocationCurrent: ", "${location.longitude}  ${location.latitude}")
                Log.e("ghg getLocationCurrent: ", text)
            } catch (e: Exception) {
                Log.e("ghg getLocationCurrent: ", e.toString())
            }
        }
    }

    private suspend fun Geocoder.getAddress(
        location: Location
    ): Address? = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCoroutine { cont ->
                    getFromLocation(location.latitude, location.longitude, 5) {
                        cont.resume(it.firstOrNull())
                    }
                }
            } else {
                suspendCoroutine { cont ->
                    @Suppress("DEPRECATION")
                    val address =
                        getFromLocation(location.latitude, location.longitude, 5)?.firstOrNull()
                    cont.resume(address)
                }
            }
        } catch (e: IllegalArgumentException) {
            Log.e("ghg IllegalArgumentException: ", e.toString())
            null
        }catch (e: IOException) {
            Log.e("ghg getAddress2: ", e.toString())
            null
        }
    }
}