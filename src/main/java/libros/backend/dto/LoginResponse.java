package libros.backend.dto;

public class LoginResponse {
    private Long id;
    private String nombre;

    public LoginResponse(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
