package libros.backend.dto;

public class CambiarClave {
    private Long id_usuario;
    private String nuevaClave;

    public Long getIdUsuario() {
        return this.id_usuario;
    }

    public String getNuevaClave() {
        return this.nuevaClave;
    }

    public void setNuevaClave(String nuevaClave) {
        this.nuevaClave = nuevaClave;
    }
}
