package libros.backend.dto;

public class LoginRequest {
    private String dni;
    private String clave;

    public String getDNI() {
        return this.dni;
    }

    public void setDNI(String dni) {
        this.dni = dni;
    }

    public String getClave() {
        return this.clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
