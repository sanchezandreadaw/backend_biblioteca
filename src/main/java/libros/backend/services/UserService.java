package libros.backend.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import libros.backend.helpers.FechaFormat;
import libros.backend.helpers.UserHelper;
import libros.backend.models.EstadoLibro;
import libros.backend.models.EstadoUsuario;
import libros.backend.models.Libro;
import libros.backend.models.TipoUsuario;
import libros.backend.models.User;
import libros.backend.repositories.LibroRepository;
import libros.backend.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibroRepository libroRepository;

    public User createUser(String nombre, String apellidos, String DNI, String telefono, String correo,
            EstadoUsuario estado_usuario, TipoUsuario tipoUsuario, LocalDate fecha_fin_penalizacion,
            List<Libro> libros) throws Exception {

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
            user.setFecha_fin_penalizacion(fecha_fin_penalizacion);
            user.setLibros(libros);
            userRepository.save(user);
            return user;

        } else {
            System.out.println("Error al guardar el usuario.");
            throw new Exception("Error al guardar el usuario. Los datos no son válidos.");
        }

    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByDNI(String DNI) throws Exception {
        User usuario = userRepository.findByDNI(DNI.toLowerCase().trim());
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
            EstadoUsuario estadoUsuario, TipoUsuario tipoUsuario, LocalDate fecha_fin_penalizacion,
            List<Libro> libros, Long id) throws Exception {

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
        user.setFecha_fin_penalizacion(fecha_fin_penalizacion);
        user.setLibros(libros);

        return userRepository.save(user);
    }

    public User deleteUser(Long id) throws Exception {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con ID: " + id + "no existe"));

        userRepository.delete(usuario);
        return usuario;

    }

    public Libro pedirLibro(String titulo, String DNI) throws Exception {
        User usuario = userRepository.findByDNI(DNI.toLowerCase().trim());
        Libro libro = libroRepository.findByTitulo(titulo.toLowerCase().trim());

        if (usuario.getEstado_usuario().equals(EstadoUsuario.PENALIZADO)) {
            StringBuilder sb = new StringBuilder();
            String fecha = usuario.getFecha_fin_penalizacion().format(FechaFormat.foramto_fecha);

            sb.append("No puedes pedir ningún libro prestado debido a que has sido penalizado.");
            sb.append("La penalización finalizará el: " + fecha);

            throw new Exception(sb.toString());
        }

        if (libro.getEstado_libro().equals(EstadoLibro.PRESTADO)) {
            throw new Exception("El libro: " + libro.getTitulo() + "ya ha sido prestado.");
        }
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate maxFechaDevolucion = fechaPrestamo.plusDays(15);

        libro.setFecha_prestamo(fechaPrestamo);
        libro.setFecha_devolucion(maxFechaDevolucion);
        libro.setEstado_libro(EstadoLibro.PRESTADO);
        return libro;
    }

    public Libro devolverLibro(String titulo, String DNI) throws Exception {
        User usuario = userRepository.findByDNI(DNI.toLowerCase().trim());
        Libro libro = libroRepository.findByTitulo(titulo.toLowerCase().trim());
        libro.setFecha_devolucion(LocalDate.now());

        if (libro.getFecha_devolucion().isAfter(libro.getFecha_max_devolucion())) {
            usuario.setEstado_usuario(EstadoUsuario.PENALIZADO);
            usuario.setFecha_fin_penalizacion(LocalDate.now().plusDays(20));
            String fecha = usuario.getFecha_fin_penalizacion().format(FechaFormat.foramto_fecha);

            StringBuilder sb = new StringBuilder();
            sb.append("Has superado el plazo máximo de devolución y serás penalizado/a" + "\n");
            sb.append("La penalización finaliza en la fecha: " + fecha);

            throw new Exception(sb.toString());

        }
        return libro;
    }

    public User penalizarUsuario(String DNI) throws Exception {
        User usuario = userRepository.findByDNI(DNI.toLowerCase().trim());
        if (usuario == null) {
            throw new Exception("El usuario con DNI: " + DNI + " no existe.");
        }
        usuario.setEstado_usuario(EstadoUsuario.PENALIZADO);
        return usuario;

    }

    public User despenalizarUsuario(String DNI) throws Exception {
        User usuario = userRepository.findByDNI(DNI.toLowerCase().trim());
        if (usuario == null) {
            throw new Exception("El usuario con DNI: " + DNI + "no existe.");
        }
        usuario.setEstado_usuario(EstadoUsuario.SIN_PENALIZAR);
        return usuario;
    }

}
