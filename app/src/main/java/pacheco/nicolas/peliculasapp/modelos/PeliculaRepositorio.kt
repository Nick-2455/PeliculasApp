package pacheco.nicolas.peliculasapp.modelos

class PeliculaRepositorio {

    private val peliculas = mutableListOf(
        Pelicula(1, "Inception", "Ciencia Ficción", "2h 28min", "Un ladrón que roba secretos a través de los sueños."),
        Pelicula(2, "The Dark Knight", "Acción", "2h 32min", "Batman se enfrenta al Joker en Gotham."),
        Pelicula(3, "Interstellar", "Drama", "2h 49min", "Un grupo de científicos viaja al espacio para salvar la humanidad."),
        Pelicula(4, "Whiplash", "Música", "1h 46min", "Un joven baterista lucha por el éxito bajo un tutor implacable."),
        Pelicula(5, "Coco", "Animación", "1h 45min", "Un niño viaja a la tierra de los muertos para conocer su legado.")
    )

    fun getPeliculas(): List<Pelicula> {
        return peliculas.toList()
    }

    fun agregarPelicula(pelicula: Pelicula) {
        peliculas.add(pelicula)
    }

    fun editarPelicula(pelicula: Pelicula) {
        val indice = peliculas.indexOfFirst { it.id == pelicula.id }
        if (indice != -1) {
            peliculas[indice] = pelicula
        }
    }

    fun eliminarPelicula(pelicula: Pelicula) {
        peliculas.removeIf { it.id == pelicula.id }
    }
}
