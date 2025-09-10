package libros.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import libros.backend.helpers.UserHelper;
import libros.backend.models.EstadoUsuario;
import libros.backend.models.Libro;
import libros.backend.models.User;
import libros.backend.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(String nombre, String apellidos, String DNI, String telefono, String correo,
            EstadoUsuario estado_usuario, List<Libro> libros) {

        if (UserHelper.isValidUser(nombre, apellidos, DNI, telefono, correo)
                && !UserHelper.verifyDNI(DNI, userRepository.findAll())
                && !UserHelper.verifyEmail(correo, userRepository.findAll())
                && !UserHelper.verifyPhone(telefono, userRepository.findAll())) {

            User user = new User();
            user.setNombre(nombre);
            user.setApellidos(apellidos);
            user.setDNI(DNI);
            user.setCorreo(correo);
            user.setTelefono(telefono);
            user.setEstado_usuario(estado_usuario);
            user.setLibros(libros);
            userRepository.save(user);
            System.out.println("Datos del usuario: ");
            System.out.println(UserHelper.showUser(user));

        } else {
            System.out.println("Error al guardar el usuario.");
        }

    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByDNI(String DNI) {
        return userRepository.findByDNI(DNI);
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public void update(String nombre, String apellidos, String DNI, String correo, String telefono,
            EstadoUsuario estadoUsuario, List<Libro> libros, Long id) {

        Optional<User> usuario = userRepository.findById(id);
        if (usuario != null) {
            User user_exist = usuario.get();
            if (UserHelper.isValidUser(nombre, apellidos, DNI, telefono, correo)
                    && !UserHelper.verifyDNI(DNI, userRepository.findAll())
                    && !UserHelper.verifyPhone(telefono, userRepository.findAll())
                    && !UserHelper.verifyEmail(correo, userRepository.findAll())) {
                user_exist.setNombre(nombre);
                user_exist.setApellidos(apellidos);
                user_exist.setDNI(DNI);
                user_exist.setCorreo(correo);
                user_exist.setTelefono(telefono);
                user_exist.setEstado_usuario(estadoUsuario);

                if (libros != null) {
                    user_exist.setLibros(libros);
                }
                userRepository.save(user_exist);
            } else {
                System.out.println("No se ha podido actualizar el usuario.");
            }
        } else {
            System.out.println("El usuario con ID: " + id + " no existe en la BBDD.");
        }

    }

}
