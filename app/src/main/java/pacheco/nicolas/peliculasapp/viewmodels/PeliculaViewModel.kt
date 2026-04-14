package pacheco.nicolas.peliculasapp.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import pacheco.nicolas.peliculasapp.modelos.Pelicula
import pacheco.nicolas.peliculasapp.modelos.PeliculaRepositorio

class PeliculaViewModel(private val repositorio: PeliculaRepositorio) : ViewModel() {

    private val _peliculas = mutableStateOf<List<Pelicula>>(emptyList())
    val peliculas: State<List<Pelicula>> = _peliculas

    init {
        cargarPeliculas()
    }

    private fun cargarPeliculas() {
        _peliculas.value = repositorio.getPeliculas()
    }
}