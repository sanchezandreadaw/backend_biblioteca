package libros.backend.controllers.LibroRestController;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import libros.backend.models.EstadoLibro;
import libros.backend.models.Libro;
import libros.backend.services.LibroService;

@RestController
@RequestMapping("/api/libros")
public class LibroRestController {

    @Autowired
    private LibroService libroService;

    @PostMapping("/save_libro")
    public ResponseEntity<Libro> saveLibro(@RequestParam("titulo") String titulo, @RequestParam("autor") String autor,
            @RequestParam("ISBN") String ISBN,
            @RequestParam("fecha_publicacion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_publicacion,
            @RequestParam(value = "fecha_prestamo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_prestamo,
            @RequestParam(value = "fecha_devolucion", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_devolucion,
            @RequestParam("estado_libro") EstadoLibro estadoLibro,
            @RequestParam(value = "usuario", required = false) Long usuario) {

        try {
            libroService.saveLibro(titulo, autor, ISBN, fecha_publicacion, fecha_prestamo, fecha_devolucion,
                    estadoLibro, usuario);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception exception) {
            System.out.println("Se ha producido un error al guardar el libro: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
