package libros.backend.exception;

import java.time.LocalDate;

public class Excepciones {
    public static class DNINoEncontradoException extends Exception {
        public DNINoEncontradoException(String dni) {
            super("El usuario con DNI " + dni + " no existe");
        }
    }

    public static class IdNoEncontradoException extends Exception {
        public IdNoEncontradoException(long id) {
            super("El usuario con ID " + id + " no existe");
        }
    }

    public static class ValoresIncorrectosException extends Exception {
        public ValoresIncorrectosException() {
            super("Usuario o contraseña incorrectos");
        }
    }

    public static class FechaSuperadaException extends Exception {
        public FechaSuperadaException(LocalDate fechaMaxPenalizacion) {
            super("Has superado la fecha máxima de devolución y serás penalizado hasta el día " + fechaMaxPenalizacion);
        }
    }

    public static class TituloNoEncontradoException extends Exception {
        public TituloNoEncontradoException(String titulo) {
            super("El libro '" + titulo + "' no existe");
        }
    }

    public static class LibroNoEncontradoException extends Exception {
        public LibroNoEncontradoException(long id) {
            super("El libro con ID " + id + " no existe");
        }
    }

    public static class MaxLibrosPrestadosException extends Exception {
        public MaxLibrosPrestadosException() {
            super("No puedes pedir prestados más de 5 libros");
        }
    }

    public static class LibroNoEncontradoISBNException extends RuntimeException {
        public LibroNoEncontradoISBNException(String isbn) {
            super("El libro con ISBN " + isbn + " no existe");
        }
    }

}
