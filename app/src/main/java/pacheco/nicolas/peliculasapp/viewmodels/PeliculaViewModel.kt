package pacheco.nicolas.peliculasapp.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import pacheco.nicolas.peliculasapp.modelos.Pelicula
import pacheco.nicolas.peliculasapp.modelos.PeliculaRepositorio

class PeliculaViewModel(val repo: PeliculaRepositorio) : ViewModel() {

    private val _peliculas = mutableStateOf<List<Pelicula>>(emptyList())
    val peliculas: State<List<Pelicula>> = _peliculas

    init {
        _peliculas.value = repo.getPeliculas()
    }

    fun agregarPelicula(titulo: String, categoria: String, duracion: String, sinopsis: String, posterUri: String?) {
        val nuevoId = _peliculas.value.size + 1
        val pel = Pelicula(nuevoId, titulo, categoria, duracion, sinopsis, posterUri)
        repo.agregarPelicula(pel)
        _peliculas.value = repo.getPeliculas()
    }

    fun editarPelicula(id: Int, titulo: String, categoria: String, duracion: String, sinopsis: String, posterUri: String?) {
        val pel = Pelicula(id, titulo, categoria, duracion, sinopsis, posterUri)
        repo.editarPelicula(pel)
        _peliculas.value = repo.getPeliculas()
    }

    fun eliminarPelicula(pelicula: Pelicula) {
        repo.eliminarPelicula(pelicula)
        _peliculas.value = repo.getPeliculas()
    }
}
