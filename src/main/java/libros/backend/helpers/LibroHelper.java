package libros.backend.helpers;

import java.time.LocalDate;
import java.util.List;

import libros.backend.models.Libro;

public class LibroHelper {

    public static boolean EsUnLibroValido(String titulo, String autor, LocalDate fecha_publicacion) throws Exception {

        if (titulo.length() < 3) {
            throw new Exception("El título del libro debe de tener al menos 3 caracteres");
        }

        if (autor.length() < 3) {
            throw new Exception("El nombre del autor debe de tener al menos 3 caracteres");
        }

        if (fecha_publicacion.getYear() < 1950) {
            throw new Exception("La fecha de publicación NO puede ser inferior a 1950");
        }

        return true;
    }

    public static boolean verifyISBN(String ISBN, List<Libro> libros, String updating) throws Exception {
        for (Libro libro : libros) {
            if (libro.getISBN().equalsIgnoreCase(ISBN)) {
                if (updating.equalsIgnoreCase("yes")) {
                    continue;
                }
                System.out.println("El libro con ISBN " + ISBN + " ya está registado");
                throw new Exception("El libro con ISBN: " + ISBN + " ya está registrado.");
            }
        }
        return false;
    }

    public static String muestraDatosLibro(Libro libro) {
        StringBuilder sb = new StringBuilder();
        sb.append("Datos del libro: " + "\n");
        sb.append("Título: " + libro.getTitulo() + "\n");
        sb.append("Autor: " + libro.getAutor() + "\n");
        sb.append("Fecha de publicación: " + libro.getFecha_publicacion() + "\n");
        sb.append("Estado del libro: " + libro.getEstado_libro() + "\n");
        sb.append("=============================" + "\n");

        return sb.toString();
    }

    public static String showLibros(List<Libro> libros) throws Exception {

        StringBuilder sb = new StringBuilder();
        if (libros.size() == 0) {
            throw new Exception("No existen libros en la base de datos");
        } else {
            for (Libro libro : libros) {
                sb.append(muestraDatosLibro(libro));
            }
            return sb.toString();
        }
    }

}
