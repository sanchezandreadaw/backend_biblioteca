package libros.backend.helpers;

import java.time.LocalDate;
import java.util.List;

import libros.backend.models.Libro;

public class LibroHelper {

    public static boolean EsUnLibroValido(String titulo, String autor, String ISBN, LocalDate fecha_publicacion) {

        if (titulo.length() < 3 || titulo.length() > 25) {
            System.out.println("El título del libro debe tener entre 3 y 25 caracteres");
            return false;
        }

        if (autor.length() < 3 || autor.length() > 25) {
            System.out.println("El nombre del autor debe tener entre 3 y 25 caracteres");
            return false;
        }

        if (ISBN.length() != 13) {
            System.out.println("El ISBN debe tener 13 caracteres");
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
}
