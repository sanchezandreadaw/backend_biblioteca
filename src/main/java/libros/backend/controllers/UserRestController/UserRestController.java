package libros.backend.controllers.UserRestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import libros.backend.helpers.UserHelper;
import libros.backend.models.EstadoUsuario;
import libros.backend.models.Libro;
import libros.backend.models.User;
import libros.backend.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/save_user")
    public ResponseEntity<String> saveUser(@RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("DNI") String DNI, @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo, @RequestParam("estado_usuario") EstadoUsuario estadoUsuario,
            @RequestParam(value = "libros", required = false) List<Libro> libros) {

        try {
            userService.save(nombre, apellidos, DNI, telefono, correo, estadoUsuario, libros);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Usuario guardado correctamente");
        } catch (Exception exception) {
            System.out.println("Se ha producido un error al guardar el usuario: " + exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Se ha producido un error al guardar el usuario: " + exception.getMessage());
        }

    }

    @PutMapping("/update_user")
    public ResponseEntity<String> updateUser(@RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("DNI") String DNI,
            @RequestParam("correo") String correo,
            @RequestParam("telefono") String telefono,
            @RequestParam("estado_usuario") EstadoUsuario estado_usuario,
            @RequestParam(value = "libros", required = false) List<Libro> libros,
            @RequestParam("id") Long id) {
        try {
            User updated = userService.update(nombre, apellidos, DNI, correo, telefono, estado_usuario, libros, id);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Usuario actualizado correctamente: " + UserHelper.showUser(updated));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/delete_user")
    public ResponseEntity<String> deleteUser(@RequestParam("id") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Usuario con ID : " + id + " eliminado correctamente");
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al eliminar el usuario: " + exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            StringBuilder sb = new StringBuilder();

            sb.append("Usuario encontrado: " + "\n");
            sb.append("===================" + "\n");
            sb.append(UserHelper.showUser(user));

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(sb.toString());
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Se ha producido un error en la búsqueda: " + exception.getMessage());
        }

    }

    @GetMapping("/getByDNI")
    public ResponseEntity<String> getByDNI(@RequestParam("dni") String DNI) {
        try {
            User usuario = userService.findByDNI(DNI);
            StringBuilder sb = new StringBuilder();

            sb.append("Usuario encontrado: " + "\n");
            sb.append("===================" + "\n");
            sb.append(UserHelper.showUser(usuario));

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(sb.toString());

        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener el usuario: " + exception.getMessage());
        }
    }

}
