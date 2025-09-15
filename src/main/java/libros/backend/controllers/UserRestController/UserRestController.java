package libros.backend.controllers.UserRestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import libros.backend.dto.CambiarClave;
import libros.backend.dto.LoginRequest;
import libros.backend.dto.LoginResponse;
import libros.backend.dto.MisLibrosReponse;
import libros.backend.dto.PedirDevolverLibro;
import libros.backend.helpers.UserHelper;
import libros.backend.models.EstadoUsuario;
import libros.backend.models.Libro;
import libros.backend.models.TipoUsuario;
import libros.backend.models.User;
import libros.backend.services.LibroService;
import libros.backend.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private LibroService libroService;

    @PostMapping("/save_user")
    public ResponseEntity<String> saveUser(@RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("DNI") String DNI,
            @RequestParam("clave") String clave,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("estado_usuario") EstadoUsuario estadoUsuario,
            @RequestParam("tipo_usuario") TipoUsuario tipoUsuario,
            @RequestParam(value = "fecha_fin_penalizacion", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_fin_penalizacion,
            @RequestParam(value = "libros", required = false) List<Libro> libros) {

        try {
            userService.createUser(nombre, apellidos, DNI, clave, telefono, correo, estadoUsuario, tipoUsuario,
                    fecha_fin_penalizacion, libros);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Usuario guardado correctamente");
        } catch (Exception exception) {
            System.out.println("Se ha producido un error al guardar el usuario: " + exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Se ha producido un error al guardar el usuario: " + exception.getMessage());
        }

    }

    @PostMapping("/login_user")
    public ResponseEntity<?> login_user(@RequestBody LoginRequest loginRequest) {
        try {
            User usuario = userService.findByDNI(loginRequest.getDNI());
            userService.autenticarUsuario(loginRequest.getDNI(), loginRequest.getClave());

            LoginResponse response = new LoginResponse(usuario.getId(), usuario.getNombre(), usuario.getDNI());

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(response);

        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }

    @PostMapping("/alta_cliente")
    public ResponseEntity<String> alta_cliente(@RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("DNI") String DNI,
            @RequestParam("clave") String clave,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo) {

        try {
            userService.altaCliente(nombre, apellidos, DNI, clave, telefono, correo);
            User user = userService.findByDNI(DNI);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(UserHelper.muestraMensajeDeBienvenida(user));
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }

    @PutMapping("/update_user")
    public ResponseEntity<String> updateUser(@RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("DNI") String DNI,
            @RequestParam("clave") String clave,
            @RequestParam("correo") String correo,
            @RequestParam("telefono") String telefono,
            @RequestParam("estado_usuario") EstadoUsuario estado_usuario,
            @RequestParam("tipo_usuario") TipoUsuario tipo_usuario,
            @RequestParam(value = "fecha_fin_penalizacion", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_fin_penalizacion,
            @RequestParam(value = "libros", required = false) List<Libro> libros,
            @RequestParam("id") Long id) {
        try {
            User updated = userService.update(nombre, apellidos, DNI, clave, correo, telefono, estado_usuario,
                    tipo_usuario, fecha_fin_penalizacion, libros, id);
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

    @GetMapping("/getLibrosUser")
    public ResponseEntity<?> getLibrosUser(@RequestParam("id") Long id) {
        try {
            User usuario = userService.findById(id);
            List<Libro> libros_usuario = usuario.getLibros();

            List<MisLibrosReponse> responseList = new ArrayList<>();

            for (Libro libro : libros_usuario) {
                responseList.add(new MisLibrosReponse(
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getFecha_publicacion(),
                        libro.getFecha_prestamo(),
                        libro.getFecha_max_devolucion()));
            }
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(responseList);

        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener los libros del usuario: " + exception.getMessage());
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

    @GetMapping("/getAll")
    public ResponseEntity<String> getAllUsers() {
        List<User> usuarios = userService.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("Listado de usuarios: " + "\n");
        sb.append("====================" + "\n");
        for (User usuario : usuarios) {
            sb.append(UserHelper.showUser(usuario));
            sb.append("====================" + "\n");
        }
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(sb.toString());
    }

    @PutMapping("/penalizar_usuario")
    public ResponseEntity<String> penalizarUsuario(@RequestParam("id") Long id) {
        try {
            User usuario = userService.findById(id);
            userService.penalizarUsuario(id);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Usuario: " + usuario.getNombre() + " " + usuario.getApellidos() + " " + " DNI: "
                            + usuario.getDNI()
                            + " penalizado.");
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Se ha producido un error al penalizar el usuario: " + exception.getMessage());
        }
    }

    @PutMapping("/despenalizar_usuario")
    public ResponseEntity<String> despenalizarUsuario(@RequestParam("id") Long id) {
        try {
            User usuario = userService.findById(id);
            userService.despenalizarUsuario(id);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Usuario: " + usuario.getNombre() + " " + usuario.getApellidos() + " " + usuario.getDNI()
                            + " despenalizado.");
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Se ha producido un error al despenalizar al usuario: " + exception.getMessage());
        }
    }

    @PutMapping("/cambiar_clave")
    public ResponseEntity<String> cambiarClave(@RequestBody CambiarClave cambiarClave) {
        try {
            userService.cambiarClave(cambiarClave.getIdUsuario(), cambiarClave.getNuevaClave());
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("La clave se ha cambiado correctamente");
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + exception.getMessage());
        }
    }

    @PutMapping("/pedir_libro")
    public ResponseEntity<?> pedirLibro(@RequestBody PedirDevolverLibro pedirDevolverLibro) {
        try {
            libroService.findByTitulo(pedirDevolverLibro.getTitulo());
            userService.pedirLibro(pedirDevolverLibro.getTitulo(), pedirDevolverLibro.getId());

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Libro adquirido correctamente");
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }

    @PutMapping("/devolver_libro")
    public ResponseEntity<String> devolverLibro(@RequestBody PedirDevolverLibro pedirDevolverLibro) {
        try {
            Libro libro = libroService.findByTitulo(pedirDevolverLibro.getTitulo());
            userService.devolverLibro(pedirDevolverLibro.getTitulo(), pedirDevolverLibro.getId());
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("El libro: " + libro.getTitulo() + " se ha devuelto correctamente");
        } catch (Exception exception) {
            if (exception.getMessage().startsWith("Plazo")) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(exception.getMessage());
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }

}
