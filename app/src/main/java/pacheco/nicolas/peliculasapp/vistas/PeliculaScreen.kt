package pacheco.nicolas.peliculasapp.vistas

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pacheco.nicolas.peliculasapp.modelos.Pelicula
import pacheco.nicolas.peliculasapp.viewmodels.PeliculaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeliculaScreen(viewModel: PeliculaViewModel) {
    val peliculas = viewModel.peliculas.value
    val context = LocalContext.current
    var mostrarDialogo by remember { mutableStateOf(false) }
    var pelicula_editar by remember { mutableStateOf<Pelicula?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Películas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar película")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(all = 16.dp)
        ) {
            items(peliculas) { pelicula ->
                PeliculaCard(pelicula, onLongClick = {
                    pelicula_editar = pelicula
                }, onEliminar = {
                    viewModel.eliminarPelicula(pelicula)
                })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (mostrarDialogo) {
        AgregarPeliculaDialog(
            onDismiss = { mostrarDialogo = false },
            onConfirm = { titulo, categoria, duracion, sinopsis, posterUri ->
                viewModel.agregarPelicula(titulo, categoria, duracion, sinopsis, posterUri)
                mostrarDialogo = false
            }
        )
    }

    pelicula_editar?.let { pelicula ->
        Toast.makeText(context, "${pelicula_editar!!.titulo}", Toast.LENGTH_SHORT).show()
        EditarPeliculaDialog(
            pelicula = pelicula,
            onDismiss = { pelicula_editar = null },
            onConfirm = { titulo, categoria, duracion, sinopsis, posterUri ->
                viewModel.editarPelicula(pelicula.id, titulo, categoria, duracion, sinopsis, posterUri)
                pelicula_editar = null
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PeliculaCard(pelicula: Pelicula, onLongClick: () -> Unit, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            if (pelicula.posterUri != null) {
                AsyncImage(
                    model = pelicula.posterUri,
                    contentDescription = "Poster de ${pelicula.titulo}",
                    modifier = Modifier
                        .size(80.dp, 110.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier
                        .size(80.dp, 110.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🎬", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = pelicula.titulo, style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar película",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Text(text = "Categoría: ${pelicula.categoria}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Duración: ${pelicula.duracion}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = pelicula.sinopsis, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AgregarPeliculaDialog(
    onDismiss: () -> Unit,
    onConfirm: (titulo: String, categoria: String, duracion: String, sinopsis: String, posterUri: String?) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }
    var posterUri by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { posterUri = it.toString() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Película") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (posterUri.isNotEmpty()) {
                        AsyncImage(
                            model = posterUri,
                            contentDescription = "Poster seleccionado",
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(text = "🎬", style = MaterialTheme.typography.displayMedium)
                    }
                }
                Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (posterUri.isEmpty()) "Seleccionar poster" else "Cambiar poster")
                }
                OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = duracion, onValueChange = { duracion = it }, label = { Text("Duración") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = sinopsis, onValueChange = { sinopsis = it }, label = { Text("Sinopsis") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(titulo, categoria, duracion, sinopsis, posterUri.ifEmpty { null }) },
                enabled = titulo.isNotBlank()
            ) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun EditarPeliculaDialog(
    pelicula: Pelicula,
    onDismiss: () -> Unit,
    onConfirm: (titulo: String, categoria: String, duracion: String, sinopsis: String, posterUri: String?) -> Unit
) {
    var titulo by remember { mutableStateOf(pelicula.titulo) }
    var categoria by remember { mutableStateOf(pelicula.categoria) }
    var duracion by remember { mutableStateOf(pelicula.duracion) }
    var sinopsis by remember { mutableStateOf(pelicula.sinopsis) }
    var posterUri by remember { mutableStateOf(pelicula.posterUri ?: "") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { posterUri = it.toString() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Película") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (posterUri.isNotEmpty()) {
                        AsyncImage(
                            model = posterUri,
                            contentDescription = "Poster seleccionado",
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(text = "🎬", style = MaterialTheme.typography.displayMedium)
                    }
                }
                Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (posterUri.isEmpty()) "Seleccionar poster" else "Cambiar poster")
                }
                OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = duracion, onValueChange = { duracion = it }, label = { Text("Duración") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = sinopsis, onValueChange = { sinopsis = it }, label = { Text("Sinopsis") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(titulo, categoria, duracion, sinopsis, posterUri.ifEmpty { null }) },
                enabled = titulo.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
