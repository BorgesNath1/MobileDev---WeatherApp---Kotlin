package com.example.weatherapp.ui

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.weatherapp.MainViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPage(modifier: Modifier = Modifier.Companion,
            viewModel: MainViewModel
){
    val cityList = viewModel.cities
    val activity = LocalActivity.current as Activity
    val camPosState = rememberCameraPositionState()

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        onMapClick = {
            val lat = "%.2f".format(it.latitude)
            val lng = "%.2f".format(it.longitude)
            viewModel.add("cidade@$lat:$lng", location = it)
        },
        cameraPositionState = camPosState
    ){
        val recife = remember { MarkerState(LatLng(-8.05, -34.9)) }
        val caruaru = remember { MarkerState(LatLng(-8.27, -35.98)) }
        val joaoPessoa = remember { MarkerState(LatLng(-7.12, -34.84))}

        viewModel.cities.forEach {
            if (it.location != null){
                val markerState = remember { MarkerState(it.location) }
                Marker(
                    state = markerState,
                    title = it.name,
                    snippet = "${it.location}")
            }
        }

        Marker (
            state = recife,
            title = "recife",
            snippet = "Marcador em recife",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        )
        Marker (
            state = caruaru,
            title = "caruaru",
            snippet = "Marcador em caruaru",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        )
        Marker (
            state = joaoPessoa,
            title = "João Pessoa",
            snippet = "Marcador em João Pessoa",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        )
    }

//    Column(
//        modifier = modifier.fillMaxSize()
//            .background(Color.Green)
//            .wrapContentSize(Alignment.Center),
//    ) {
//        Text(
//            text = "Mapas",
//            fontWeight = FontWeight.Bold,
//            color = Color.White,
//            modifier = modifier.align(CenterHorizontally),
//            textAlign = TextAlign.Center,
//            fontSize = 20.sp
//        )
//    }
}