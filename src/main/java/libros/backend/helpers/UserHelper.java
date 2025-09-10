package libros.backend.helpers;

import java.util.List;

import libros.backend.models.Libro;
import libros.backend.models.User;

public class UserHelper {

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
}
