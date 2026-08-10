package com.twitter.arabic.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ==================== TIMEZONE API ====================

interface TimeZoneApi {
    @GET("api/timezone/{timezone}")
    suspend fun getTimeByZone(@Path("timezone") timezone: String): TimeZoneResponse
}

data class TimeZoneResponse(
    val timezone: String = "",
    val datetime: String = "",
    val utc_offset: String = "",
    val is_dst: Boolean = false
)

object TimeZoneApiClient {
    private const val BASE_URL = "https://worldtimeapi.org/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val instance: TimeZoneApi = retrofit.create(TimeZoneApi::class.java)
}

// ==================== TWITTER API ====================

interface TwitterApi {
    @GET("tweets/{id}")
    suspend fun getTweet(@Path("id") id: String): TweetApiResponse
    
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserApiResponse
}

data class TweetApiResponse(
    val id: String = "",
    val text: String = "",
    val author_id: String = "",
    val created_at: String = "",
    val public_metrics: PublicMetrics = PublicMetrics()
)

data class UserApiResponse(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val description: String = "",
    val followers_count: Int = 0,
    val following_count: Int = 0
)

data class PublicMetrics(
    val retweet_count: Int = 0,
    val reply_count: Int = 0,
    val like_count: Int = 0,
    val quote_count: Int = 0
)

// ==================== WEATHER API ====================

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @retrofit2.http.Query("q") city: String,
        @retrofit2.http.Query("appid") apiKey: String,
        @retrofit2.http.Query("units") units: String = "metric"
    ): WeatherResponse
}

data class WeatherResponse(
    val coord: Coord = Coord(),
    val weather: List<Weather> = emptyList(),
    val main: Main = Main(),
    val wind: Wind = Wind(),
    val clouds: Clouds = Clouds(),
    val sys: Sys = Sys(),
    val name: String = "",
    val cod: Int = 0
)

data class Coord(
    val lon: Double = 0.0,
    val lat: Double = 0.0
)

data class Weather(
    val id: Int = 0,
    val main: String = "",
    val description: String = "",
    val icon: String = ""
)

data class Main(
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val temp_min: Double = 0.0,
    val temp_max: Double = 0.0,
    val pressure: Int = 0,
    val humidity: Int = 0
)

data class Wind(
    val speed: Double = 0.0,
    val deg: Int = 0,
    val gust: Double = 0.0
)

data class Clouds(
    val all: Int = 0
)

data class Sys(
    val type: Int = 0,
    val id: Int = 0,
    val country: String = "",
    val sunrise: Long = 0,
    val sunset: Long = 0
)

object WeatherApiClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val instance: WeatherApi = retrofit.create(WeatherApi::class.java)
}

// ==================== GEOLOCATION API ====================

interface GeolocationApi {
    @GET("json")
    suspend fun getLocation(
        @retrofit2.http.Query("ip") ip: String = ""
    ): GeolocationResponse
}

data class GeolocationResponse(
    val ip: String = "",
    val city: String = "",
    val region: String = "",
    val country: String = "",
    val loc: String = "",
    val timezone: String = "",
    val org: String = ""
)

object GeolocationApiClient {
    private const val BASE_URL = "https://ipinfo.io/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val instance: GeolocationApi = retrofit.create(GeolocationApi::class.java)
}

// ==================== EXCHANGE RATE API ====================

interface ExchangeRateApi {
    @GET("latest")
    suspend fun getExchangeRates(
        @retrofit2.http.Query("base") base: String = "USD"
    ): ExchangeRateResponse
}

data class ExchangeRateResponse(
    val base: String = "",
    val date: String = "",
    val rates: Map<String, Double> = emptyMap()
)

object ExchangeRateApiClient {
    private const val BASE_URL = "https://api.exchangerate-api.com/v4/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val instance: ExchangeRateApi = retrofit.create(ExchangeRateApi::class.java)
}

// ==================== NEWS API ====================

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @retrofit2.http.Query("country") country: String = "us",
        @retrofit2.http.Query("apiKey") apiKey: String
    ): NewsResponse
}

data class NewsResponse(
    val status: String = "",
    val totalResults: Int = 0,
    val articles: List<Article> = emptyList()
)

data class Article(
    val source: Source = Source(),
    val author: String = "",
    val title: String = "",
    val description: String = "",
    val url: String = "",
    val urlToImage: String = "",
    val publishedAt: String = "",
    val content: String = ""
)

data class Source(
    val id: String = "",
    val name: String = ""
)

object NewsApiClient {
    private const val BASE_URL = "https://newsapi.org/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val instance: NewsApi = retrofit.create(NewsApi::class.java)
}

// ==================== REPOSITORY ====================

class ApiRepository {
    
    // TimeZone Operations
    suspend fun getTimeByZone(timezone: String) = withContext(Dispatchers.IO) {
        try {
            TimeZoneApiClient.instance.getTimeByZone(timezone)
        } catch (e: Exception) {
            null
        }
    }
    
    // Weather Operations
    suspend fun getWeather(city: String, apiKey: String) = withContext(Dispatchers.IO) {
        try {
            WeatherApiClient.instance.getWeather(city, apiKey)
        } catch (e: Exception) {
            null
        }
    }
    
    // Geolocation Operations
    suspend fun getLocation(ip: String = "") = withContext(Dispatchers.IO) {
        try {
            GeolocationApiClient.instance.getLocation(ip)
        } catch (e: Exception) {
            null
        }
    }
    
    // Exchange Rate Operations
    suspend fun getExchangeRates(base: String = "USD") = withContext(Dispatchers.IO) {
        try {
            ExchangeRateApiClient.instance.getExchangeRates(base)
        } catch (e: Exception) {
            null
        }
    }
    
    // News Operations
    suspend fun getHeadlines(country: String, apiKey: String) = withContext(Dispatchers.IO) {
        try {
            NewsApiClient.instance.getHeadlines(country, apiKey)
        } catch (e: Exception) {
            null
        }
    }
}