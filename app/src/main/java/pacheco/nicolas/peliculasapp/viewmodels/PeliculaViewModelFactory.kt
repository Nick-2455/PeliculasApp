package pacheco.nicolas.peliculasapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pacheco.nicolas.peliculasapp.modelos.PeliculaRepositorio

class PeliculaViewModelFactory(private val repositorio: PeliculaRepositorio) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeliculaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeliculaViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}