package projeto.br.injecaodependencias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private var locationId: Int? = null

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btLoad.setOnClickListener { load() }
        btDetail.setOnClickListener { detail() }
    }

    private fun load() {
        launch {
            btDetail.isEnabled = false
            val retrofit = createRetrofit()
            val service = retrofit.create(RickAndMortyService::class.java)

            val response = service.characterList(1)
            tvName.text = "Personagem: ${response.results[3].name}"
            locationId = response.results[3].origin.url.substringAfterLast("/").toInt()

            btDetail.isEnabled = true
        }
    }

    private fun detail() {
        val it = Intent(this, DetailActivity::class.java)
        it.putExtra("locationId", locationId)
        startActivity(it)
    }

    private fun createRetrofit(): Retrofit {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()

        val gsonConfig = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()

        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gsonConfig))
            .build()
    }

    data class Character(
        var id: Int,
        var name: String,
        var status: String,
        var species: String,
        var type: String,
        var gender: String,
        var origin: Location,
        var image: String,
        var episode: List<String>,
        var url: String
    )

    /**
     * Mapeamento do JSON da Localização do Personagem na API Rick and Morty
     */
    data class Location(
        var id: Int,
        var name: String,
        var url: String,
        var type: String,
        var dimension: String
    )

    /**
     * Mapeamento das informações de retorno da API Rick and Morty
     */
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String,
        val prev: String
    )

    /**
     * Mapeamento do retorno da API Rick and Morty
     */
    data class RickAndMortyPage(
        val info: Info,
        val results: List<Character>
    )

    interface RickAndMortyService {
        @GET("character")
        @Headers("Content-Type: application/json")
        suspend fun characterList(@Query("page") page: Int): RickAndMortyPage
    }
}