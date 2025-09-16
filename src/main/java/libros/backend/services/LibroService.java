package libros.backend.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import libros.backend.exception.Excepciones.LibroNoEncontradoException;
import libros.backend.exception.Excepciones.LibroNoEncontradoISBNException;
import libros.backend.exception.Excepciones.TituloNoEncontradoException;
import libros.backend.helpers.LibroHelper;
import libros.backend.models.EstadoLibro;
import libros.backend.models.Libro;
import libros.backend.models.User;
import libros.backend.repositories.LibroRepository;;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public Libro saveLibro(String titulo, String autor, String ISBN, LocalDate fecha_publicacion,
            LocalDate fecha_prestamo, LocalDate fecha_max_devolucion, LocalDate fecha_devolucion,
            EstadoLibro estadoLibro,
            User usuario) throws Exception {

        try {

            LibroHelper.EsUnLibroValido(titulo, autor, fecha_publicacion);
            LibroHelper.verifyISBN(ISBN, libroRepository.findAll(), "no");
            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setISBN(ISBN);
            libro.setFecha_publicacion(fecha_publicacion);
            libro.setFecha_prestamo(fecha_prestamo);
            libro.setFecha_max_devolucion(fecha_max_devolucion);
            libro.setFecha_devolucion(fecha_devolucion);
            libro.setEstado_libro(estadoLibro);
            libro.setUsuario(usuario);
            libroRepository.save(libro);
            return libro;
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }

    }

    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    public List<Libro> findByEstado() {
        List<Libro> libros = libroRepository.findAll().stream()
                .filter((libro) -> libro.getEstado_libro() == EstadoLibro.SIN_PRESTAR).toList();

        return libros;
    }

    public Libro findByISBN(String ISBN) throws LibroNoEncontradoISBNException {

        Libro libro = libroRepository.findByISBN(ISBN.toLowerCase().trim());
        if (libro == null) {
            throw new LibroNoEncontradoISBNException(ISBN);
        }
        return libro;
    }

    public Libro findById(Long id) throws LibroNoEncontradoException {
        return libroRepository.findById(id).orElseThrow(() -> new LibroNoEncontradoException(id));
    }

    public Libro findByTitulo(String titulo) throws Exception, TituloNoEncontradoException {
        Libro libro = libroRepository.findByTitulo(titulo.toLowerCase().trim());
        if (libro == null) {
            throw new TituloNoEncontradoException(titulo);
        }
        return libro;
    }

    public Libro delete(Long id) throws LibroNoEncontradoException {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new LibroNoEncontradoException(id));

        libroRepository.delete(libro);

        return libro;

    }

    public Libro updateLibro(String titulo, String autor, String ISBN, LocalDate fecha_publicacion,
            LocalDate fecha_prestamo, LocalDate fecha_max_devolucion, LocalDate fecha_devolucion,
            EstadoLibro estadoLibro, User usuario, Long id_libro) throws Exception, LibroNoEncontradoException {

        Libro libro = libroRepository.findById(id_libro)
                .orElseThrow(() -> new LibroNoEncontradoException(id_libro));

        try {
            LibroHelper.EsUnLibroValido(titulo, autor, fecha_publicacion);
            LibroHelper.verifyISBN(ISBN, libroRepository.findAll(), "yes");
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setISBN(ISBN);
            libro.setFecha_publicacion(fecha_publicacion);
            libro.setFecha_prestamo(fecha_prestamo);
            libro.setFecha_max_devolucion(fecha_max_devolucion);
            libro.setFecha_devolucion(fecha_devolucion);
            libro.setEstado_libro(estadoLibro);
            libro.setUsuario(usuario);

            return libroRepository.save(libro);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }

    }

}
