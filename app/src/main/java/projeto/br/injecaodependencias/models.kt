package projeto.br.injecaodependencias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class models : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_models)
    }
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
