package pacheco.nicolas.peliculasapp.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pacheco.nicolas.peliculasapp.modelos.Pelicula
import pacheco.nicolas.peliculasapp.viewmodels.PeliculaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeliculaScreen(viewModel: PeliculaViewModel) {
    val lista = viewModel.peliculas.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Películas") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            items(lista) { pelicula ->
                PeliculaCard(pelicula)
            }
        }
    }
}

@Composable
fun PeliculaCard(pelicula: Pelicula) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = pelicula.titulo, style = MaterialTheme.typography.titleLarge)
            Text(text = "Categoría: ${pelicula.categoria}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Duración: ${pelicula.duracion}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = pelicula.sinopsis, style = MaterialTheme.typography.bodyLarge)
        }
    }
}