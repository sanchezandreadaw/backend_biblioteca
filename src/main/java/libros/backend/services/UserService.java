package libros.backend.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import libros.backend.exception.Excepciones.DNINoEncontradoException;
import libros.backend.exception.Excepciones.FechaSuperadaException;
import libros.backend.exception.Excepciones.IdNoEncontradoException;
import libros.backend.exception.Excepciones.LibroNoEncontradoException;
import libros.backend.exception.Excepciones.MaxLibrosPrestadosException;
import libros.backend.exception.Excepciones.ValoresIncorrectosException;
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
            List<Libro> libros) throws Exception, IdNoEncontradoException {

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

    public User findByDNI(String DNI) throws DNINoEncontradoException {
        User usuario = userRepository.findByDNI(DNI.toLowerCase().trim());
        if (usuario != null) {
            return usuario;
        } else {
            throw new DNINoEncontradoException(DNI);
        }
    }

    public User findById(Long id) throws IdNoEncontradoException {
        return userRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException(id));
    }

    public User update(String nombre, String apellidos, String DNI, String clave, String correo, String telefono,
            EstadoUsuario estadoUsuario, TipoUsuario tipoUsuario, LocalDate fecha_fin_penalizacion,
            List<Libro> libros, Long id) throws IdNoEncontradoException, Exception {

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IdNoEncontradoException(id));
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

    public User cambiarClave(Long id_usuario, String nuevaClave) throws Exception, IdNoEncontradoException {
        try {
            User usuario = userRepository.findById(id_usuario)
                    .orElseThrow(() -> new IdNoEncontradoException(id_usuario));
            UserHelper.isValidPassword(nuevaClave);
            usuario.setClave(passwordEncoder.encode(nuevaClave));
            userRepository.save(usuario);
            return usuario;
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public void estableceValoresAlUsuario(User user, String nombre, String apellidos, String DNI, String clave,
            String correo,
            String telefono, EstadoUsuario estadoUsuario, TipoUsuario tipoUsuario, LocalDate fecha_fin_penalizacion,
            List<Libro> libros) throws Exception, IdNoEncontradoException {

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

    public User deleteUser(Long id) throws Exception, IdNoEncontradoException {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException(id));

        userRepository.delete(usuario);
        return usuario;

    }

    public User autenticarUsuario(String DNI, String clave)
            throws DNINoEncontradoException, ValoresIncorrectosException {
        User usuario = userRepository.findByDNI(DNI.toLowerCase().trim());
        if (usuario == null) {
            throw new DNINoEncontradoException(DNI);
        }
        if (!passwordEncoder.matches(clave, usuario.getClave())) {
            throw new ValoresIncorrectosException();
        }
        return usuario;
    }

    public void actualizarClaveOlvidad(String DNI, String nuevaClave) throws DNINoEncontradoException, Exception {
        User usuario = userRepository.findByDNI(DNI.trim().toLowerCase());
        if (usuario == null) {
            throw new DNINoEncontradoException(DNI);
        }

        UserHelper.isValidPassword(nuevaClave);
        usuario.setClave(passwordEncoder.encode(nuevaClave));
        userRepository.save(usuario);
    }

    public List<Libro> getLibrosUser(Long id) throws IdNoEncontradoException, Exception {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException(id));

        if (usuario.getLibros() == null) {
            throw new Exception("No existen libros para el usuario con id: " + id);
        }
        return usuario.getLibros();
    }

    public Libro pedirLibro(String titulo, Long id)
            throws Exception, IdNoEncontradoException, LibroNoEncontradoException {

        try {
            Libro libro = libroRepository.findByTitulo(titulo.toLowerCase().trim());
            User usuario = userRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException(id));

            verifyIfExistBookAndUser(libro, usuario.getId(), titulo);
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

    public void checkNumberOfBooks(User usuario) throws MaxLibrosPrestadosException {
        if (usuario != null) {
            int maxLibros = 5;
            if (usuario.getLibros().size() == maxLibros) {
                throw new MaxLibrosPrestadosException();
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

    public Libro devolverLibro(String titulo, Long id) throws Exception, FechaSuperadaException {

        try {
            Libro libro = libroRepository.findByTitulo(titulo.toLowerCase().trim());
            User usuario = userRepository.findById(id).get();
            verifyIfExistBookAndUser(libro, usuario.getId(), titulo);

            chequearDevolucion(libro, usuario);

            libro.setFecha_devolucion(LocalDate.now());

            if (seraPenalizado(libro, usuario)) {
                System.out.println("Penalizando usuario");
                usuario.setEstado_usuario(EstadoUsuario.PENALIZADO);
                usuario.setFecha_fin_penalizacion(LocalDate.now().plusDays(20));
                actualizarLibroAlDevolver(libro, usuario);

                throw new FechaSuperadaException(usuario.getFecha_fin_penalizacion());
            }
            System.out.println("Actualizando libro y usuario");
            actualizarLibroAlDevolver(libro, usuario);

            return libro;

        } catch (Exception exception) {
            System.out.println("Mostrando excepción de error " + exception.getMessage());
            exception.printStackTrace();
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

    public void verifyIfExistBookAndUser(Libro libro, Long id, String titulo) throws Exception {

        if (id == null) {
            throw new Exception("El usuario no existe.");
        }
        if (libro == null) {
            throw new Exception("El libro no existe");
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

    public User despenalizarUsuario(Long id) throws IdNoEncontradoException {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException(id));

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
            libro.setEstado_libro(EstadoLibro.SIN_PRESTAR);
            libro.setFecha_devolucion(null);
            libro.setFecha_max_devolucion(null);
            libro.setFecha_prestamo(null);
            libroRepository.save(libro);
        }

        userRepository.save(usuario);
    }

}
