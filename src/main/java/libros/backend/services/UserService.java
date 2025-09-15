package libros.backend.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User createUser(String nombre, String apellidos, String DNI, String clave, String telefono, String correo,
            EstadoUsuario estado_usuario, TipoUsuario tipoUsuario, LocalDate fecha_fin_penalizacion,
            List<Libro> libros) throws Exception {

        try {

            UserHelper.isValidUser(nombre, apellidos, DNI, telefono, correo);
            UserHelper.verifyDNI(DNI, userRepository.findAll(), "no");
            UserHelper.verifyEmail(correo, userRepository.findAll(), "no");
            UserHelper.verifyPhone(telefono, userRepository.findAll(), "no");
            UserHelper.isValidPassword(clave);

            User user = new User();
            estableceValoresAlUsuario(user, nombre, apellidos, DNI, clave, correo, telefono, estado_usuario,
                    tipoUsuario, fecha_fin_penalizacion, libros);
            return userRepository.save(user);

        } catch (Exception excetpion) {
            throw new Exception(excetpion.getMessage());
        }

    }

    public User altaCliente(String nombre, String apellidos, String DNI, String clave, String telefono, String correo)
            throws Exception {
        try {
            UserHelper.isValidUser(nombre, apellidos, DNI, telefono, correo);
            UserHelper.verifyDNI(DNI, userRepository.findAll(), "no");
            UserHelper.verifyEmail(correo, userRepository.findAll(), "no");
            UserHelper.verifyPhone(telefono, userRepository.findAll(), "no");
            UserHelper.isValidPassword(clave);

            User user = new User();
            establecerValoresCliente(user, nombre, apellidos, DNI, clave, telefono, correo);
            userRepository.save(user);
            return user;

        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public void establecerValoresCliente(User user, String nombre, String apellidos, String DNI, String clave,
            String telefono, String correo) {
        user.setNombre(nombre);
        user.setApellidos(apellidos);
        user.setDNI(DNI);
        user.setClave(passwordEncoder.encode(clave));
        user.setTelefono(telefono);
        user.setCorreo(correo);
        user.setEstado_usuario(EstadoUsuario.SIN_PENALIZAR);
        user.setTipoUsuario(TipoUsuario.CLIENTE);
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

    public User update(String nombre, String apellidos, String DNI, String clave, String correo, String telefono,
            EstadoUsuario estadoUsuario, TipoUsuario tipoUsuario, LocalDate fecha_fin_penalizacion,
            List<Libro> libros, Long id) throws Exception {

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new Exception("El usuario con id: " + id + " no existe."));
            UserHelper.isValidUser(nombre, apellidos, DNI, telefono, correo);
            UserHelper.verifyDNI(DNI, userRepository.findAll(), "yes");
            UserHelper.verifyEmail(correo, userRepository.findAll(), "yes");
            UserHelper.verifyPhone(telefono, userRepository.findAll(), "yes");
            UserHelper.isValidPassword(clave);

            estableceValoresAlUsuario(user, nombre, apellidos, DNI, clave, correo, telefono, estadoUsuario,
                    tipoUsuario, fecha_fin_penalizacion, libros);
            return user;

        } catch (Exception excetpion) {
            throw new Exception(excetpion.getMessage());
        }

    }

    public void cambiarClave(Long id_usuario, String nuevaClave) throws Exception {
        try {
            User usuario = userRepository.findById(id_usuario)
                    .orElseThrow(() -> new Exception("El usuario con id " + id_usuario + " no existe"));
            UserHelper.isValidPassword(nuevaClave);
            usuario.setClave(passwordEncoder.encode(nuevaClave));
            userRepository.save(usuario);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public void estableceValoresAlUsuario(User user, String nombre, String apellidos, String DNI, String clave,
            String correo,
            String telefono, EstadoUsuario estadoUsuario, TipoUsuario tipoUsuario, LocalDate fecha_fin_penalizacion,
            List<Libro> libros) throws Exception {

        user.setNombre(nombre);
        user.setApellidos(apellidos);
        user.setDNI(DNI);
        user.setClave(passwordEncoder.encode(clave));
        user.setCorreo(correo);
        user.setTelefono(telefono);
        user.setEstado_usuario(estadoUsuario);
        user.setTipoUsuario(tipoUsuario);
        user.setFecha_fin_penalizacion(fecha_fin_penalizacion);
        user.setLibros(libros);
    }

    public User deleteUser(Long id) throws Exception {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con ID: " + id + "no existe"));

        userRepository.delete(usuario);
        return usuario;

    }

    public User autenticarUsuario(String DNI, String clave) throws Exception {
        User usuario = userRepository.findByDNI(DNI.toLowerCase().trim());
        if (usuario == null) {
            throw new Exception("El usuario con DNI " + DNI + " no existe");
        }
        if (!passwordEncoder.matches(clave, usuario.getClave())) {
            throw new Exception("Usuario o contraseña incorrectos");
        }
        return usuario;
    }

    public List<Libro> getLibrosUser(Long id) throws Exception {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con ID " + id + " no existe"));

        if (usuario.getLibros() == null) {
            throw new Exception("No existen libros para el usuario con id: " + id);
        }
        return usuario.getLibros();
    }

    public Libro pedirLibro(String titulo, Long id) throws Exception {

        try {
            User usuario = userRepository.findById(id)
                    .orElseThrow(() -> new Exception("El usuario con ID " + id + " no existe"));
            Libro libro = libroRepository.findByTitulo(titulo.toLowerCase().trim());

            verifyIfExistBookAndUser(usuario, libro, id, titulo);
            chequearPrestamoAUsuario(libro, usuario);
            checkNumberOfBooks(usuario);

            if (libro.getFecha_max_devolucion() != null) {
                isFechaMaxDevolucion(libro.getFecha_max_devolucion(), usuario);
            }
            isPenalizedUser(usuario);
            verifyBookStatus(libro);
            actualizarLibroAlPrestar(libro, usuario);

            return libro;

        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public void checkNumberOfBooks(User usuario) throws Exception {
        if (usuario != null) {
            int maxLibros = 5;
            if (usuario.getLibros().size() == maxLibros) {
                throw new Exception("No puedes pedir prestados más de " + maxLibros + " libros");
            }
        }
    }

    public void chequearPrestamoAUsuario(Libro libro, User usuario) throws Exception {
        if (libro.getUsuario() != null) {

            if (libro.getUsuario().equals(usuario)) {
                throw new Exception("Actualmente tienes en préstamo este libro");
            }
        }
    }

    public Libro devolverLibro(String titulo, Long id) throws Exception {

        try {
            User usuario = userRepository.findById(id)
                    .orElseThrow(() -> new Exception("El usuario con " + id + " no existe"));
            Libro libro = libroRepository.findByTitulo(titulo.toLowerCase().trim());

            verifyIfExistBookAndUser(usuario, libro, id, titulo);
            chequearDevolucion(libro, usuario);
            libro.setFecha_devolucion(LocalDate.now());

            if (seraPenalizado(libro, usuario)) {
                usuario.setEstado_usuario(EstadoUsuario.PENALIZADO);
                usuario.setFecha_fin_penalizacion(LocalDate.now().plusDays(20));
                actualizarLibroAlDevolver(libro, usuario);

                String fecha_penalizacion = LocalDate.now().plusDays(20).format(FechaFormat.foramto_fecha);

                throw new Exception("Plazo máximo de devolución superado. Usuario penalizado hasta: "
                        + fecha_penalizacion + "\n" + "Devolución completada.");
            }
            actualizarLibroAlDevolver(libro, usuario);

            return libro;

        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public void chequearDevolucion(Libro libro, User usuario) throws Exception {
        if (libro.getFecha_devolucion() == null && libro.getFecha_max_devolucion() == null
                && libro.getFecha_prestamo() == null) {
            throw new Exception("No se puede devolver un libro que no está prestado");
        }
        if (libro.getUsuario() != null) {
            if (!libro.getUsuario().equals(usuario)) {
                throw new Exception(
                        "No puedes devolver el libro: " + libro.getTitulo() + " porque no lo tienes en préstamo");
            }
        }
    }

    public void verifyIfExistBookAndUser(User usuario, Libro libro, Long id, String titulo) throws Exception {

        if (usuario == null) {
            throw new Exception("El usuario con id " + id + " no existe");
        }
        if (libro == null) {
            throw new Exception("El libro con título: " + titulo + " no existe");
        }
    }

    public User penalizarUsuario(Long id) throws Exception {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con id: " + id + " no existe"));

        usuario.setEstado_usuario(EstadoUsuario.PENALIZADO);
        usuario.setFecha_fin_penalizacion(LocalDate.now().plusDays(20));
        userRepository.save(usuario);
        return usuario;

    }

    public User despenalizarUsuario(Long id) throws Exception {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con id: " + id + " no existe"));

        usuario.setEstado_usuario(EstadoUsuario.SIN_PENALIZAR);
        usuario.setFecha_fin_penalizacion(null);
        userRepository.save(usuario);
        return usuario;
    }

    public void isFechaMaxDevolucion(LocalDate fecha_max_devolucion, User usuario) {
        if (usuario.getFecha_fin_penalizacion() != null) {
            if (usuario.getEstado_usuario().equals(EstadoUsuario.PENALIZADO)
                    && LocalDate.now().isAfter(fecha_max_devolucion)) {
                usuario.setFecha_fin_penalizacion(null);
                userRepository.save(usuario);

            }
        }
    }

    public void isPenalizedUser(User user) throws Exception {

        if (user.getEstado_usuario().equals(EstadoUsuario.PENALIZADO)) {
            StringBuilder sb = new StringBuilder();
            if (user.getFecha_fin_penalizacion() != null) {
                sb.append("No puedes pedir ningún libro porque estás penalizado hasta el día: "
                        + user.getFecha_fin_penalizacion());
                throw new Exception(sb.toString());
            }
        }

    }

    public boolean verifyBookStatus(Libro libro) throws Exception {
        if (libro.getEstado_libro().equals(EstadoLibro.PRESTADO)) {
            throw new Exception("El libro: " + libro.getTitulo() + " ya ha sido prestado");
        }
        return false;
    }

    public Libro actualizarLibroAlPrestar(Libro libro, User usuario) {
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate maxFechaDevolucion = fechaPrestamo.plusDays(15);

        libro.setFecha_prestamo(fechaPrestamo);
        libro.setFecha_max_devolucion(maxFechaDevolucion);
        libro.setEstado_libro(EstadoLibro.PRESTADO);
        libro.setUsuario(usuario);

        List<Libro> libros = new ArrayList<>();
        libros.add(libro);
        usuario.setLibros(libros);

        libroRepository.save(libro);
        userRepository.save(usuario);
        return libro;

    }

    public boolean seraPenalizado(Libro libro, User usuario) {

        if (libro.getFecha_devolucion() != null) {
            if (libro.getFecha_devolucion().isAfter(libro.getFecha_max_devolucion())) {
                return true;
            }
        }
        return false;

    }

    public void actualizarLibroAlDevolver(Libro libro, User usuario) {
        libro.setUsuario(null);
        List<Libro> libros = usuario.getLibros();
        if (libros != null) {
            libros = libros.stream().filter((libro_devolver) -> libro_devolver.getId() != libro.getId()).toList();
            usuario.setLibros(libros);
        }
        libro.setEstado_libro(EstadoLibro.SIN_PRESTAR);
        libro.setFecha_devolucion(null);
        libro.setFecha_max_devolucion(null);
        libro.setFecha_prestamo(null);

        libroRepository.save(libro);
        userRepository.save(usuario);
    }

}
