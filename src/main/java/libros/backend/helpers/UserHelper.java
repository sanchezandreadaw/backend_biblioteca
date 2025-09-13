package libros.backend.helpers;

import java.util.Arrays;
import java.util.List;
import libros.backend.models.Libro;
import libros.backend.models.User;

public class UserHelper {

	private static String[] alphabetUpper = {
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
			"N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};

	private static String[] specialChars = {
			"!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/",
			":", ";", "<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "|",
			"}", "~"
	};

	private static String[] alphabetLower = (String[]) Arrays.asList(alphabetUpper).stream()
			.map((caracter) -> caracter.toLowerCase()).toArray();

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
			if (password.length() >= 8) {
				throw new Exception("La contraseña debe tener al menos 8 caracteres");
			}

			int currentIndex = 0;
			int lastIndex = 0;

			contieneMayuscula(currentIndex, lastIndex, password);
			contieneMinuscula(currentIndex, lastIndex, password);
			contieneCaracterEspecial(currentIndex, lastIndex, password);
		} catch (Exception exception) {
			throw new Exception(exception.getMessage());
		}

		return true;
	}

	public static boolean contieneMayuscula(int currentIndex, int lastIndex, String password) throws Exception {

		for (int i = 0; i < alphabetUpper.length; i++) {
			currentIndex = i;
			lastIndex = (alphabetUpper.length - 1);
			if (!password.contains(alphabetUpper[i]) && currentIndex < lastIndex) {
				continue;
			} else {
				throw new Exception("La clave debe tener al menos una letra mayúscula");
			}
		}
		return true;
	}

	public static boolean contieneMinuscula(int currentIndex, int lastIndex, String password) throws Exception {
		for (int i = 0; i < alphabetLower.length; i++) {
			currentIndex = i;
			lastIndex = (alphabetLower.length - 1);

			if (!password.contains(alphabetLower[i]) && currentIndex < lastIndex) {
				continue;
			} else {
				throw new Exception("La clave debe tener al menos una letra minúscula");
			}
		}

		return true;
	}

	public static boolean contieneCaracterEspecial(int currentIndex, int lastIndex, String password) throws Exception {
		for (int i = 0; i < specialChars.length; i++) {
			currentIndex = i;
			lastIndex = (specialChars.length - 1);

			if (!password.contains(specialChars[i]) && currentIndex < lastIndex) {
				continue;
			} else {
				throw new Exception("La clave debe de tener al menos un caracter especial");
			}
		}

		return true;
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

}
