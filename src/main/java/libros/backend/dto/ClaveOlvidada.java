package libros.backend.dto;

public class ClaveOlvidada {
    private String DNI;
    private String nuevaClave;

    public ClaveOlvidada(String DNI, String nuevaClave) {
        this.DNI = DNI;
        this.nuevaClave = nuevaClave;
    }

    public String getDNI() {
        return this.DNI;
    }

    public String getNuevaClave() {
        return this.nuevaClave;
    }

}
