package libros.backend.dto;

import java.time.LocalDate;

public class LibrosDisponiblesReponse {
    private String titulo;
    private String autor;
    private LocalDate fecha_publicacion;

    public LibrosDisponiblesReponse(String titulo, String autor, LocalDate fecha_publicacion) {
        this.titulo = titulo;
        this.autor = autor;
        this.fecha_publicacion = fecha_publicacion;
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

}
