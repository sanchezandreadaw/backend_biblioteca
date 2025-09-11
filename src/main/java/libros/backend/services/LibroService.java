package libros.backend.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import libros.backend.helpers.LibroHelper;
import libros.backend.models.EstadoLibro;
import libros.backend.models.Libro;
import libros.backend.models.User;
import libros.backend.repositories.LibroRepository;
import libros.backend.repositories.UserRepository;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveLibro(String titulo, String autor, String ISBN, LocalDate fecha_publicacion,
            LocalDate fecha_prestamo, LocalDate fecha_max_devolucion, LocalDate fecha_devolucion,
            EstadoLibro estadoLibro,
            Long id_usuario) throws Exception {

        User usuario = userRepository.findById(id_usuario)
                .orElseThrow(() -> new Exception("El usuario con ID: " + id_usuario + " no existe"));

        if (LibroHelper.EsUnLibroValido(titulo, autor, ISBN, fecha_publicacion)
                && !LibroHelper.verifyISBN(ISBN.toLowerCase().trim(), libroRepository.findAll(), "no")) {
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
        }

    }

    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    public Libro findByISBN(String ISBN) throws Exception {

        Libro libro = libroRepository.findByISBN(ISBN.toLowerCase().trim());
        if (libro == null) {
            throw new Exception("El libro con ISBN" + ISBN + " no existe");
        }
        return libro;
    }

    public Libro findById(Long id) throws Exception {
        return libroRepository.findById(id).orElseThrow(() -> new Exception("El libro con ID: " + id + " no existe"));
    }

    public Libro findByTitulo(String titulo) throws Exception {
        Libro libro = libroRepository.findByTitulo(titulo.toLowerCase().trim());
        if (libro == null) {
            throw new Exception("El libro con título" + titulo + " no existe");
        }
        return libro;
    }

    public Libro delete(String ISBN) throws Exception {
        Libro libro = libroRepository.findByISBN(ISBN);
        if (libro == null) {
            throw new Exception("El libro con ISBN" + ISBN + " no existe");
        }
        libroRepository.delete(libro);
        return libro;

    }

    public void updateLibro(String titulo, String autor, String ISBN, LocalDate fecha_publicacion,
            LocalDate fecha_prestamo, LocalDate fecha_max_devolucion, LocalDate fecha_devolucion,
            EstadoLibro estadoLibro, Long id_usuario) throws Exception {

        Libro libro = libroRepository.findByISBN(ISBN);
        User usuario = userRepository.findById(id_usuario)
                .orElseThrow(() -> new Exception("El usuario con id : " + id_usuario + "no existe"));
        if (libro != null) {
            if (LibroHelper.EsUnLibroValido(titulo, autor, ISBN, fecha_publicacion)
                    && !LibroHelper.verifyISBN(ISBN.toLowerCase().trim(), libroRepository.findAll(), "yes")) {
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
            }
        } else {
            System.out.println("El libro que estás intentando actualizar no existe");
        }

    }

}
