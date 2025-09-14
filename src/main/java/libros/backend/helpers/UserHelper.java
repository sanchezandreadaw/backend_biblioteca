package libros.backend.helpers;

import java.util.List;
import libros.backend.models.Libro;
import libros.backend.models.User;

public class UserHelper {

	private static char[] alphabetUpper = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};

	private static char[] specialChars = {
			'!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
			':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|',
			'}', '~'
	};

	private static char[] numeros = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	private static char[] alphabetLower = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'ñ', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
	};

	public static boolean isValidUser(String nombre, String apellidos, String DNI, String telefono, String correo)
			throws Exception {
		if (nombre.length() < 3 || nombre.length() > 25) {
			throw new Exception("El nombre debe tener entre 3 y 25 caracteres");
		}
		if (apellidos.length() < 3 || apellidos.length() > 25) {
			throw new Exception("Los apellidos deben tener entre 3 y 25 caracteres");
		}
		if (DNI.length() > 9) {
			throw new Exception("El DNI debe tener como máximo 9 caracteres");
		}
		if (telefono.length() > 9) {
			throw new Exception("El número de teléfono no puede ser superior a 9 dígitos");
		}

		if (correo.length() < 10) {
			throw new Exception("El correo electrónico no puede ser inferior a 10 caracteres");
		}
		if (!correo.contains("@")) {
			throw new Exception("El correo debe contener un dominio. Ejemplo: user@example.com");
		}
		return true;
	}

	public static boolean verifyDNI(String DNI, List<User> usuarios, String updating) throws Exception {
		for (User user : usuarios) {
			if (user.getDNI().equalsIgnoreCase(DNI)) {
				if (updating.equalsIgnoreCase("yes")) {
					continue;
				}
				throw new Exception("El usuario con DNI: " + DNI + " ya existe");
			}
		}
		return true;
	}

	public static boolean verifyEmail(String correo, List<User> usuarios, String updating) throws Exception {

		for (User user : usuarios) {
			if (user.getCorreo().equalsIgnoreCase(correo)) {
				if (updating.equalsIgnoreCase("yes")) {

					continue;
				}

				throw new Exception("El usuario con correo: " + correo + " ya existe");
			}
		}
		return true;
	}

	public static boolean verifyPhone(String number, List<User> usuarios, String updating) throws Exception {

		for (User user : usuarios) {
			if (user.getTelefono().equalsIgnoreCase(number)) {
				if (updating.equalsIgnoreCase("yes")) {
					continue;
				}
				throw new Exception("El número de teléfono: " + number + " ya está registrado");
			}
		}
		return true;
	}

	public static boolean isValidPassword(String password) throws Exception {

		try {
			if (password.length() < 8) {
				throw new Exception("La contraseña debe tener al menos 8 caracteres");
			}

			contieneMayuscula(password);
			contieneMinuscula(password);
			contieneCaracterEspecial(password);
			contieneUnNumero(password);
		} catch (Exception exception) {
			throw new Exception(exception.getMessage());
		}

		return true;
	}

	public static void contieneMayuscula(String password) throws Exception {
		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			for (int j = 0; j < alphabetUpper.length; j++) {
				if (c == alphabetUpper[j]) {
					return;
				}
			}
		}
		throw new Exception("La clave debe contener al menos una mayúscula.");

	}

	public static void contieneMinuscula(String password) throws Exception {
		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			for (int j = 0; j < alphabetLower.length; j++) {
				if (c == alphabetLower[j]) {
					return;
				}
			}
		}
		throw new Exception("La clave debe contener al menos una minúscula.");
	}

	public static void contieneCaracterEspecial(String password) throws Exception {

		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			for (int j = 0; j < specialChars.length; j++) {
				if (c == specialChars[j]) {
					return;
				}

			}
		}
		throw new Exception("La clave debe tener al menos un caracter especial.");

	}

	public static void contieneUnNumero(String password) throws Exception {
		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			for (int j = 0; j < numeros.length; j++) {
				if (c == numeros[j]) {
					return;
				}
			}
		}
		throw new Exception("La clave debe contener al menos un número.");
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

	public static String muestraLibrosUsuario(User usuario) throws Exception {
		if (usuario.getLibros().size() == 0) {
			throw new Exception("El usuario: " + usuario.getNombre() + " " + usuario.getApellidos()
					+ " no ha realizado aún ningún préstamo");
		}
		StringBuilder sb = new StringBuilder();
		for (Libro libro : usuario.getLibros()) {
			sb.append("Libros prestados a " + usuario.getNombre() + " " + usuario.getApellidos() + "\n");
			sb.append("Título: " + libro.getTitulo() + "\n");
			sb.append("Autor: " + libro.getAutor() + "\n");
			sb.append("Fecha de préstamo: " + libro.getFecha_prestamo() + "\n");
			sb.append("Fecha máxima de devolución: " + libro.getFecha_max_devolucion() + "\n");
		}
		return sb.toString();
	}

	public static String muestraMensajeDeBienvenida(User usuario) {
		StringBuilder sb = new StringBuilder();
		sb.append("Te has dado de alta correctamente. ¡Gracias por confiar en nosotros!" + "\n");
		sb.append("\n");
		sb.append("A partir de este momento puedes empezar a pedir libros prestados." + "\n");
		sb.append("\n");
		sb.append("=================================================================" + "\n");
		sb.append("Tus datos: " + "\n");
		sb.append("Nombre: " + usuario.getNombre() + "\n");
		sb.append("Apellidos: " + usuario.getApellidos() + "\n");
		sb.append("DNI: " + usuario.getDNI() + "\n");
		sb.append("Correo: " + usuario.getCorreo() + "\n");
		sb.append("Teléfono: " + usuario.getTelefono() + "\n");
		sb.append("=================================================================" + "\n");
		return sb.toString();
	}

}
