package libros.backend.helpers;

import java.time.LocalDate;
import java.util.List;

import libros.backend.models.Libro;

public class LibroHelper {

    public static boolean EsUnLibroValido(String titulo, String autor, LocalDate fecha_publicacion) {

        if (titulo.length() < 3) {
            System.out.println("El título del libro debe tener entre 3 y 25 caracteres");
            return false;
        }

        if (autor.length() < 3) {
            System.out.println("El nombre del autor debe tener entre 3 y 25 caracteres");
            return false;
        }

        if (fecha_publicacion.getYear() < 1950) {
            System.out.println("La fecha de publicación debe ser mayor o igual al año 1950");
            return false;
        }

        return true;
    }

    public static boolean verifyISBN(String ISBN, List<Libro> libros, String updating) {
        for (Libro libro : libros) {
            if (libro.getISBN().equalsIgnoreCase(ISBN)) {
                if (updating.equalsIgnoreCase("yes")) {
                    continue;
                }
                System.out.println("El libro con ISBN " + ISBN + " ya está registado");
                return true;
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

        return sb.toString();
    }

    public static String showLibros(List<Libro> libros) {

        StringBuilder sb = new StringBuilder();
        if (libros.size() == 0) {
            sb.append("No existen libros en la base de datos.");
            return sb.toString();
        } else {
            for (Libro libro : libros) {
                sb.append(muestraDatosLibro(libro));
            }
            return sb.toString();
        }
    }
}
