package com.example.weatherapp

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.api.toForecast
import com.example.weatherapp.api.toWeather
import com.example.weatherapp.db.fb.FBCity
import com.example.weatherapp.db.fb.FBDatabase
import com.example.weatherapp.db.fb.FBUser
import com.example.weatherapp.db.fb.toFBCity
import com.example.weatherapp.model.City
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.User
import com.example.weatherapp.model.Weather
import com.example.weatherapp.ui.nav.Route
import com.google.android.gms.maps.model.LatLng

class MainViewModel (private val db: FBDatabase, private val service: WeatherService) : ViewModel(), FBDatabase.Listener {
    private val _cities = mutableStateMapOf<String, City>()

    private val _user = mutableStateOf<User?> (null)

    private val _weather = mutableStateMapOf<String, Weather>()

    private val _forecast = mutableStateMapOf<String, List<Forecast>?>()

    private var _city = mutableStateOf<String?>(null)

    private var _page = mutableStateOf<Route>(Route.Home)


    init{
        db.setListener(this)
    }

    var city: String?
        get() = _city.value
        set(tmp) { _city.value = tmp }

    val user : User?
        get() = _user.value

    //depreciado na prática 8
    //val cities get() = _cities.toList()

    val cities: List<City> by derivedStateOf {
        _cities.values.toList().sortedBy { it.name }
    }

    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp}

    fun remove(city: City) {
        db.remove(city.toFBCity())
    }

// FUNÇÃO DEPRECIADA PELA INTRODUÇÃO DA CAMADA DE REQUISIÇÃO DA API DE CLIMA NA PRÁTICA 7
//    fun add(name: String, location: LatLng? = null) {
//        db.add(City(name = name, location = location).toFBCity())
//    }

    fun addCity(name: String){
        service.getLocation(name){ lat, lng ->
            if(lat != null && lng != null){
                db.add(City(name = name , location = LatLng(lat, lng)).toFBCity())
            }
        }
    }

    fun addCity(location: LatLng){
        service.getName(location.latitude, location.longitude){ name ->
            if(name != null){
                db.add(City(name = name , location = location).toFBCity())
            }
        }
    }

    override fun onUserLoaded(user: FBUser) { _user.value = user.toUser()}
    override fun onUserSignOut() { }
    override fun onCityAdded(city: FBCity) { _cities[city.name!!] = city.toCity()}
    override fun onCityUpdated(city: FBCity) {
        _cities.remove(city.name)
        _cities[city.name!!] = city.toCity() }
    override fun onCityRemoved(city: FBCity) { _cities.remove(city.name)}

    fun weather(name: String) = _weather.getOrPut(name){
        loadWeather(name)
        Weather.LOADING
    }

    private fun loadWeather(name: String){
        service.getWeather(name){
            apiWeather -> apiWeather?.let {
                _weather[name] = apiWeather.toWeather()
                loadBitmap(name)
            }
        }
    }

    fun forecast (name: String) = _forecast.getOrPut(name) {
        loadForecast(name)
        emptyList()
    }

    private fun loadForecast(name: String){
        service.getForecast(name){
            aPIWeatherForecast ->  aPIWeatherForecast?.let {
                _forecast[name] = aPIWeatherForecast.toForecast()
            }
        }
    }
    
    private fun loadBitmap(name: String){
        _weather[name]?.let{ weather ->
            service.getBitmap(weather.imgUrl){ bitmap ->
                _weather[name] = weather.copy(bitmap = bitmap)
            }
        }
    }
}

// FUNÇÃO DEPRECIADA NA ATIVIDADE DA LISTA 6 - PARTE 2 - LISTA DE CIDADES CARREGA DIRETO DO FIREBASE
//private fun getCities() = List(20){ i ->
//    City(name = "Cidade $i", weather = "Carregando Clima", location = null)
//}

class MainViewModelFactory(private val db: FBDatabase, private val service: WeatherService) :
    ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel (db,service) as T
        }
        throw IllegalArgumentException(
            "Unknown ViewModel class."
        )
    }
}