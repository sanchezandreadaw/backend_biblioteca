package libros.backend.controllers.UserRestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            userService.update(nombre, apellidos, DNI, correo, telefono, estado_usuario, libros, id);
            User user = userService.findById(id);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Usuario actualizado correctamente: " + UserHelper.showUser(user));
        } catch (Exception exception) {
            System.out.println("Se ha producido un error al actualizar el usuario: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
