package com.example.weatherapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.db.fb.FBCity
import com.example.weatherapp.db.fb.FBDatabase
import com.example.weatherapp.db.fb.FBUser
import com.example.weatherapp.db.fb.toFBCity
import com.example.weatherapp.model.City
import com.example.weatherapp.model.User
import com.google.android.gms.maps.model.LatLng

class MainViewModel (private val db: FBDatabase) : ViewModel(), FBDatabase.Listener {
    private val _cities = mutableStateListOf<City>()
    private val _user = mutableStateOf<User?> (null)

    init{
        db.setListener(this)
    }

    val user : User?
        get() = _user.value

    val cities get() = _cities.toList()

    fun remove(city: City) {
        db.remove(city.toFBCity())
    }

    fun add(name: String, location: LatLng? = null) {
        db.add(City(name = name, location = location).toFBCity())
    }

    override fun onUserLoaded(user: FBUser) { _user.value = user.toUser()}
    override fun onUserSignOut() { }
    override fun onCityAdded(city: FBCity) { _cities.add(city.toCity())}
    override fun onCityUpdated(city: FBCity) { }
    override fun onCityRemoved(city: FBCity) { _cities.remove(city.toCity())}

}

// FUNÇÃO DEPRECIADA NA ATIVIDADE DA LISTA 6 - PARTE 2 - LISTA DE CIDADES CARREGA DIRETO DO FIREBASE
private fun getCities() = List(20){ i ->
    City(name = "Cidade $i", weather = "Carregando Clima", location = null)
}

class MainViewModelFactory(private val db: FBDatabase) :
    ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel (db) as T
        }
        throw IllegalArgumentException(
            "Unknown ViewModel class."
        )
    }
}