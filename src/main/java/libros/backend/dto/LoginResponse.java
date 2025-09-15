package libros.backend.dto;

public class LoginResponse {
    private Long id;
    private String nombre;
    private String dni;

    public LoginResponse(Long id, String nombre, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
    }

    public String getDNI() {
        return this.dni;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
