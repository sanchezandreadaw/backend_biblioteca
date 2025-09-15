package libros.backend.dto;

public class PedirDevolverLibro {

    private String titulo;
    private String dni;

    public PedirDevolverLibro(String titulo, String dni) {
        this.titulo = titulo;
        this.dni = dni;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDNI() {
        return this.dni;
    }

    public void setDNI(String dni) {
        this.dni = dni;
    }

}
