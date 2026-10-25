package com.example.weatherapp.ui

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.weatherapp.MainViewModel
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
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
    val context = LocalContext.current
    val hasLocationPermission by remember {
        mutableStateOf (
            ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        onMapClick = {
            val lat = "%.2f".format(it.latitude)
            val lng = "%.2f".format(it.longitude)
            viewModel.add("cidade@$lat:$lng", location = it)
        },
        cameraPositionState = camPosState,
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true)
    ){
        // VARIÁVEIS DE MOCKUP DESATIVADAS - FUNÇÃO DEPRECIADA NA ATIVIDADE DA LISTA 6 - PARTE 2 - LISTA DE CIDADES CARREGA DIRETO DO FIREBASE
//        val recife = remember { MarkerState(LatLng(-8.05, -34.9)) }
//        val caruaru = remember { MarkerState(LatLng(-8.27, -35.98)) }
//        val joaoPessoa = remember { MarkerState(LatLng(-7.12, -34.84))}

        viewModel.cities.forEach {
            if (it.location != null){
                val markerState = remember { MarkerState(it.location) }
                Marker(
                    state = markerState,
                    title = it.name,
                    snippet = "${it.location}")
            }
        }
//
        // VARIÁVEIS DE MOCKUP DESATIVADAS - FUNÇÃO DEPRECIADA NA ATIVIDADE DA LISTA 6 - PARTE 2 - LISTA DE CIDADES CARREGA DIRETO DO FIREBASE
//        Marker (
//            state = recife,
//            title = "recife",
//            snippet = "Marcador em recife",
//            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
//        )
//        Marker (
//            state = caruaru,
//            title = "caruaru",
//            snippet = "Marcador em caruaru",
//            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
//        )
//        Marker (
//            state = joaoPessoa,
//            title = "João Pessoa",
//            snippet = "Marcador em João Pessoa",
//            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
//        )
    }
}

////    Column(
////        modifier = modifier.fillMaxSize()
////            .background(Color.Green)
////            .wrapContentSize(Alignment.Center),
////    ) {
////        Text(
////            text = "Mapas",
////            fontWeight = FontWeight.Bold,
////            color = Color.White,
////            modifier = modifier.align(CenterHorizontally),
////            textAlign = TextAlign.Center,
////            fontSize = 20.sp
////        )
////    }
//}