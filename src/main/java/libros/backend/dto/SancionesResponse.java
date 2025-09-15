package libros.backend.dto;

import java.time.LocalDate;

public class SancionesResponse {
    LocalDate fecha_max_sancion;

    public SancionesResponse(LocalDate fecha_max_sancion) {
        this.fecha_max_sancion = fecha_max_sancion;
    }

    public LocalDate getFecha_max_sancion() {
        return fecha_max_sancion;
    }

}
