package libros.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import libros.backend.helpers.UserHelper;
import libros.backend.models.EstadoUsuario;
import libros.backend.models.User;
import libros.backend.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(String nombre, String apellidos, String DNI, String telefono, String correo) {
        List<User> usuarios = userRepository.findAll();
        if(UserHelper.verifyDNI(DNI, usuarios) && UserHelper.verifyPhone(telefono, usuarios)
             && UserHelper.verifyEmail(correo, usuarios)){
                if(UserHelper.isValidUser(nombre, apellidos, DNI, telefono, correo)){
                    User user = new User();
                    user.setNombre(nombre);
                    user.setApellidos(apellidos);
                    user.setDNI(DNI);
                    user.setCorreo_electronico(correo);
                    user.setTelefono(telefono);

                    userRepository.save(user);
                }
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByDNI(String DNI) {
        return userRepository.findByDNI(DNI);
    }

    public void update(String nombre, String apellidos, String DNI, String telefono, String correo,
            EstadoUsuario estadoUsuario) {

        User usuario = userRepository.findByDNI(DNI);

        if (usuario != null) {
            if (UserHelper.isValidUser(nombre, apellidos, DNI, telefono, correo)) {
                usuario.setNombre(nombre);
                usuario.setApellidos(apellidos);
                usuario.setDNI(DNI);
                usuario.setTelefono(telefono);
                usuario.setCorreo_electronico(correo);

                userRepository.save(usuario);
            }
        } else {
            System.out.println("El usuario con DNI: " + DNI + " ya existe.");
        }

    }

}
