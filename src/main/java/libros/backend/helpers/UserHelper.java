package libros.backend.helpers;

import java.util.List;
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

	public static boolean verifyDNI(String DNI, List<User> usuarios) {
		boolean existeDNI = false;
		for (User user : usuarios) {
			if (user.getDNI().equalsIgnoreCase(DNI)) {
				System.out.println("El usuario con DNI " + DNI + " ya existe.");
				existeDNI = true;
				break;
			}
		}
		return existeDNI;
	}

	public static boolean verifyEmail(String correo, List<User> usuarios) {
		boolean existeEmail = false;
		for (User user : usuarios) {
			if (user.getCorreo_electronico().equalsIgnoreCase(correo)) {
				System.out.println("El usuario con correo: " + correo + " ya existe.");
				existeEmail = true;
				break;
			}
		}
		return existeEmail;
	}

	public static boolean verifyPhone(String number, List<User> usuarios) {
		boolean existeTelefono = false;
		for (User user : usuarios) {
			if (user.getTelefono().equalsIgnoreCase(number)) {
				System.out.println("El número de teléfono " + number + " ya está registado");
				break;
			}
		}
		return existeTelefono;
	}
}
