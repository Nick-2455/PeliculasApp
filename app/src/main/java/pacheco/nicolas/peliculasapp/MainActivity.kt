package pacheco.nicolas.peliculasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import pacheco.nicolas.peliculasapp.modelos.PeliculaRepositorio
import pacheco.nicolas.peliculasapp.viewmodels.PeliculaViewModel
import pacheco.nicolas.peliculasapp.viewmodels.PeliculaViewModelFactory
import pacheco.nicolas.peliculasapp.vistas.PeliculaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Instanciamos el repositorio
            val repositorio = PeliculaRepositorio()

            // 2. Instanciamos el Factory
            val factory = PeliculaViewModelFactory(repositorio)

            // 3. Obtenemos el ViewModel
            val vm: PeliculaViewModel = viewModel(factory = factory)

            // 4. Mostramos la pantalla
            PeliculaScreen(viewModel = vm)
        }
    }
}