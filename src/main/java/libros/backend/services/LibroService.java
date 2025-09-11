package libros.backend.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import libros.backend.helpers.LibroHelper;
import libros.backend.models.EstadoLibro;
import libros.backend.models.Libro;
import libros.backend.repositories.LibroRepository;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public void saveLibro(String titulo, String autor, String ISBN, LocalDate fecha_publicacion,
            LocalDate fecha_prestamo, LocalDate fecha_max_devolucion, LocalDate fecha_devolucion,
            EstadoLibro estadoLibro,
            Long usuario) throws Exception {

        boolean NoExisteISBN = !findByISBN(ISBN).getISBN().equalsIgnoreCase(ISBN);

        if (LibroHelper.EsUnLibroValido(titulo, autor, ISBN, fecha_publicacion) && NoExisteISBN) {
            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setISBN(ISBN);
            libro.setFecha_publicacion(fecha_publicacion);
            libro.setFecha_prestamo(fecha_prestamo);
            libro.setFecha_max_devolucion(fecha_max_devolucion);
            libro.setFecha_devolucion(fecha_devolucion);
            libro.setEstado_libro(estadoLibro);
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
            EstadoLibro estadoLibro) {

        Libro libro = libroRepository.findByISBN(ISBN);
        if (libro != null) {
            if (LibroHelper.EsUnLibroValido(titulo, autor, ISBN, fecha_publicacion)) {
                libro.setTitulo(titulo);
                libro.setAutor(autor);
                libro.setISBN(ISBN);
                libro.setFecha_publicacion(fecha_publicacion);
                libro.setFecha_prestamo(fecha_prestamo);
                libro.setFecha_max_devolucion(fecha_max_devolucion);
                libro.setFecha_devolucion(fecha_devolucion);
                libro.setEstado_libro(estadoLibro);
                libroRepository.save(libro);
            }
        } else {
            System.out.println("El libro que estás intentando actualizar no existe");
        }

    }

}
