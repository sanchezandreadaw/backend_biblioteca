package libros.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import libros.backend.helpers.UserHelper;
import libros.backend.models.EstadoUsuario;
import libros.backend.models.Libro;
import libros.backend.models.TipoUsuario;
import libros.backend.models.User;
import libros.backend.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(String nombre, String apellidos, String DNI, String telefono, String correo,
            EstadoUsuario estado_usuario, TipoUsuario tipoUsuario, List<Libro> libros) throws Exception {

        if (UserHelper.isValidUser(nombre, apellidos, DNI, telefono, correo)
                && !UserHelper.verifyDNI(DNI, userRepository.findAll(), "no")
                && !UserHelper.verifyEmail(correo, userRepository.findAll(), "no")
                && !UserHelper.verifyPhone(telefono, userRepository.findAll(), "no")) {

            User user = new User();
            user.setNombre(nombre);
            user.setApellidos(apellidos);
            user.setDNI(DNI);
            user.setCorreo(correo);
            user.setTelefono(telefono);
            user.setEstado_usuario(estado_usuario);
            user.setTipoUsuario(tipoUsuario);
            user.setLibros(libros);
            userRepository.save(user);
            System.out.println("Datos del usuario: ");
            System.out.println(UserHelper.showUser(user));

        } else {
            System.out.println("Error al guardar el usuario.");
            throw new Exception("Error al guardar el usuario. Los datos no son válidos.");
        }

    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByDNI(String DNI) throws Exception {
        User usuario = userRepository.findByDNI(DNI.toLowerCase());
        if (usuario != null) {
            return usuario;
        } else {
            throw new Exception("El usuario con DNI: " + DNI + " no existe");
        }
    }

    public User findById(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con id: " + id + " no existe"));
    }

    public User update(String nombre, String apellidos, String DNI, String correo, String telefono,
            EstadoUsuario estadoUsuario, TipoUsuario tipoUsuario, List<Libro> libros, Long id) throws Exception {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con ID " + id + " no existe"));

        if (!UserHelper.isValidUser(nombre, apellidos, DNI, telefono, correo) ||
                UserHelper.verifyDNI(DNI, userRepository.findAll(), "yes") ||
                UserHelper.verifyPhone(telefono, userRepository.findAll(), "yes") ||
                UserHelper.verifyEmail(correo, userRepository.findAll(), "yes")) {
            throw new Exception("Los valores introducidos no son correctos");
        }

        user.setNombre(nombre);
        user.setApellidos(apellidos);
        user.setDNI(DNI);
        user.setCorreo(correo);
        user.setTelefono(telefono);
        user.setEstado_usuario(estadoUsuario);
        user.setTipoUsuario(tipoUsuario);
        user.setLibros(libros);

        return userRepository.save(user);
    }

    public User deleteUser(Long id) throws Exception {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con ID: " + id + "no existe"));

        userRepository.delete(usuario);
        return usuario;

    }

}
