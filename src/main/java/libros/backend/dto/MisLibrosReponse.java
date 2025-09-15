package libros.backend.dto;

import java.time.LocalDate;

public class MisLibrosReponse {

    private String titulo;
    private String autor;
    private LocalDate fecha_publicacion;
    private LocalDate fecha_prestamo;
    private LocalDate fecha_max_devolucion;

    public MisLibrosReponse(String titulo, String autor, LocalDate fecha_publicacion, LocalDate fecha_prestamo,
            LocalDate fecha_max_devolucion) {
        this.titulo = titulo;
        this.autor = autor;
        this.fecha_publicacion = fecha_publicacion;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_max_devolucion = fecha_max_devolucion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }

    public LocalDate getFecha_prestamo() {
        return fecha_prestamo;
    }

    public LocalDate getFecha_max_devolucion() {
        return fecha_max_devolucion;
    }

}
