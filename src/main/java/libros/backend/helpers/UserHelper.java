package libros.backend.helpers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import libros.backend.models.EstadoLibro;
import libros.backend.models.EstadoUsuario;
import libros.backend.models.Libro;
import libros.backend.models.User;
import libros.backend.repositories.LibroRepository;
import libros.backend.repositories.UserRepository;

public class UserHelper {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LibroRepository libroRepository;

	public static boolean isValidUser(String nombre, String apellidos, String DNI, String telefono, String correo) {
		if (nombre.length() < 3 || nombre.length() > 25) {
			System.out.println("El nombre debe tener entre 3 y 25 caracteres.");
			return false;
		}
		if (apellidos.length() < 3 || apellidos.length() > 25) {
			System.out.println("Los apellidos deben tener entre 3 y 25 caracteres.");
			return false;
		}
		if (DNI.length() > 9) {
			System.out.println("El DNI debe de tener 9 caracteres.");
			return false;
		}
		if (telefono.length() > 9) {
			System.out.println("El número de teléfono debe tener 9 caracteres.");
			return false;
		}

		if (correo.length() < 10) {
			System.out.println("El correo electrónico no puede tener menos de 10 caracteres.");
			return false;
		}
		return true;
	}

	public static boolean verifyDNI(String DNI, List<User> usuarios, String updating) {
		for (User user : usuarios) {
			if (user.getDNI().equalsIgnoreCase(DNI)) {
				if (updating.equalsIgnoreCase("yes")) {
					continue;
				}
				System.out.println("El usuario con DNI: " + DNI + " ya existe");
				return true;
			}
		}
		return false;
	}

	public static boolean verifyEmail(String correo, List<User> usuarios, String updating) {

		for (User user : usuarios) {
			if (user.getCorreo().equalsIgnoreCase(correo)) {
				if (updating.equalsIgnoreCase("yes")) {

					continue;
				}
				System.out.println("El usuario con correo: " + correo + " ya existe");
				return true;
			}
		}
		return false;
	}

	public static boolean verifyPhone(String number, List<User> usuarios, String updating) {

		for (User user : usuarios) {
			if (user.getTelefono().equalsIgnoreCase(number)) {
				if (updating.equalsIgnoreCase("yes")) {
					continue;
				}
				System.out.println("El número de teléfono " + number + " ya está registado");
				return true;
			}
		}
		return false;
	}

	public static String showUser(User user) {
		StringBuilder sb = new StringBuilder();
		sb.append("Nombre: " + user.getNombre() + "\n");
		sb.append("Apellidos: " + user.getApellidos() + "\n");
		sb.append("DNI: " + user.getDNI() + "\n");
		sb.append("Teléfono: " + user.getTelefono() + "\n");
		sb.append("Correo: " + user.getCorreo() + "\n");
		sb.append("Estado: " + user.getEstado_usuario() + "\n");
		sb.append("Tipo de usuario: " + user.getTipoUsuario() + "\n");
		if (user.getLibros() != null) {
			for (Libro libro : user.getLibros()) {
				sb.append("Título: " + libro.getTitulo() + "\n");
				sb.append("Autor: " + libro.getAutor() + "\n");
				sb.append("ISBN: " + libro.getISBN() + "\n");
				sb.append("Fecha de publicación: " + libro.getFecha_publicacion());
			}
		}
		return sb.toString();
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

	public boolean isNotPenalizedUser(User usuario) throws Exception {

		if (usuario.getEstado_usuario().equals(EstadoUsuario.PENALIZADO)) {
			return true;
		}
		return false;
	}

	public boolean verifyBookStatus(Libro libro) throws Exception {
		if (libro.getEstado_libro().equals(EstadoLibro.PRESTADO)) {
			throw new Exception("El libro: " + libro.getTitulo() + " ya ha sido prestado");
		}
		return false;
	}

	public void actualizarLibroAlPrestar(Libro libro, User usuario) {
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

	}

	public void seraPenalizado(Libro libro, User usuario) throws Exception {
		StringBuilder sb = new StringBuilder();

		if (libro.getFecha_devolucion().isAfter(libro.getFecha_max_devolucion())) {
			usuario.setEstado_usuario(EstadoUsuario.PENALIZADO);
			usuario.setFecha_fin_penalizacion(LocalDate.now().plusDays(20));
			String fecha = usuario.getFecha_fin_penalizacion().format(FechaFormat.foramto_fecha);

			sb.append("Has superado el plazo máximo para devolver el libro y serás penalizado/a" + "\n");
			if (fecha != null) {
				sb.append("La penalización finaliza en la fecha: " + fecha);
			}
			sb.append("El libro se ha devuelto correctamente");
			throw new Exception(sb.toString());

		}

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
