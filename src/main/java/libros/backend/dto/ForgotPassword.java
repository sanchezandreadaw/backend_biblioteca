package libros.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForgotPassword {

    @JsonProperty("DNI")
    private String DNI;

    @JsonProperty("nuevaClave")
    private String nuevaClave;

    public ForgotPassword() {
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNuevaClave() {
        return nuevaClave;
    }

    public void setNuevaClave(String nuevaClave) {
        this.nuevaClave = nuevaClave;
    }
}