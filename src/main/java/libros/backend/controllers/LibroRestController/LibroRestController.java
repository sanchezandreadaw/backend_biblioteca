package libros.backend.controllers.LibroRestController;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import libros.backend.helpers.LibroHelper;
import libros.backend.models.EstadoLibro;
import libros.backend.models.Libro;
import libros.backend.models.User;
import libros.backend.services.LibroService;
import libros.backend.services.UserService;

@RestController
@RequestMapping("/api/libros")
public class LibroRestController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private UserService userService;

    @PostMapping("/save_libro")
    public ResponseEntity<String> saveLibro(
            @RequestParam("titulo") String titulo,
            @RequestParam("autor") String autor,
            @RequestParam("ISBN") String ISBN,
            @RequestParam("fecha_publicacion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_publicacion,
            @RequestParam(value = "fecha_prestamo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_prestamo,
            @RequestParam(value = "fecha_max_devolucion", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_max_devolucion,
            @RequestParam(value = "fecha_devolucion", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_devolucion,
            @RequestParam("estado_libro") EstadoLibro estadoLibro,
            @RequestParam(value = "id_usuario", required = false) Long id_usuario) {

        try {
            User usuario = null;
            if (id_usuario != null) {
                usuario = userService.findById(id_usuario);
            }

            Libro libro = libroService.saveLibro(
                    titulo,
                    autor,
                    ISBN,
                    fecha_publicacion,
                    fecha_prestamo,
                    fecha_max_devolucion,
                    fecha_devolucion,
                    estadoLibro,
                    usuario);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Libro guardado correctamente: \n" + LibroHelper.muestraDatosLibro(libro));

        } catch (Exception exception) {
            System.out.println("Se ha producido un error al guardar el libro: " + exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al guardar el libro: " + exception.getMessage());
        }
    }

    @PutMapping("/update_libro")
    public ResponseEntity<String> update(@RequestParam("titulo") String titulo, @RequestParam("autor") String autor,
            @RequestParam("ISBN") String ISBN,
            @RequestParam("fecha_publicacion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_publicacion,
            @RequestParam(value = "fecha_prestamo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_prestamo,
            @RequestParam(value = "fecha_max_devolucion", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_max_devolucion,
            @RequestParam(value = "fecha_devolucion", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_devolucion,
            @RequestParam("estado_libro") EstadoLibro estadoLibro,
            @RequestParam("id") Long id_libro,
            @RequestParam(value = "id_usuario", required = false) Long id_usuario) {

        try {
            User usuario = null;
            if (id_usuario != null) {
                usuario = userService.findById(id_usuario);
            }
            Libro libro = libroService.findById(id_libro);

            libroService.updateLibro(
                    titulo, autor, ISBN, fecha_publicacion, fecha_prestamo, fecha_max_devolucion,
                    fecha_devolucion,
                    estadoLibro, usuario, id_libro);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Libro actualizado correctamente. " + "\n" + LibroHelper.muestraDatosLibro(libro));
        } catch (Exception exception) {
            System.out.println("Se ha producido un error al actualizar el libro: " + exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Se ha producido un error al actualizar el libro: " + exception.getMessage());
        }

    }

    @PostMapping("/delete_libro")
    public ResponseEntity<String> deleteLibro(@RequestParam("id_libro") Long id) {
        try {
            Libro libro = libroService.findById(id);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("El libro" + libro.getTitulo() + " se ha eliminado correctamente de la base de datos.");
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Error al eliminar el libro: " + exception.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<String> getAll() {
        List<Libro> libros = libroService.findAll();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(LibroHelper.showLibros(libros));
    }

    @GetMapping("/findById")
    public ResponseEntity<String> getLibroById(@RequestParam("id") Long id) {
        try {
            Libro libro = libroService.findById(id);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(LibroHelper.muestraDatosLibro(libro));
        } catch (Exception excetpion) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Se ha producido un error al obtener el libro: " + excetpion.getMessage());
        }
    }

    @GetMapping("/findByISBN")
    public ResponseEntity<String> getLibroByISBN(@RequestParam("ISBN") String ISBN) {
        try {
            Libro libro = libroService.findByISBN(ISBN);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(LibroHelper.muestraDatosLibro(libro));
        } catch (Exception excetpion) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Se ha producido un error al obtener el libro: " + excetpion.getMessage());
        }

    }

    @GetMapping("/findByTitulo")
    public ResponseEntity<String> getLibroByTitulo(@RequestParam("titulo") String titulo) {
        try {
            Libro libro = libroService.findByTitulo(titulo);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(LibroHelper.muestraDatosLibro(libro));
        } catch (Exception excetpion) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Se ha producido un error al obtener el libro: " + excetpion.getMessage());
        }

    }

}
