package aplicacion;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Clase de utilidad encargada de gestionar la seguridad de las credenciales de
 * usuario. Proporciona métodos para la generación de sales aleatorias y el
 * cifrado de contraseñas mediante el algoritmo PBKDF2. * @author Matteo
 * Artuñedo, Rodrigo Diaz y Daniel Martinez
 * 
 * @version 1.0
 */
public class Seguridad {

	/**
	 * * Número de iteraciones para el algoritmo de hashing. Un valor alto aumenta
	 * el tiempo necesario para ataques de fuerza bruta.
	 */
	private static final int ITERACIONES = 65536;

	/** Longitud de la clave generada en bits. */
	private static final int LONGITUD_CLAVE = 128;

	/**
	 * Algoritmo estándar utilizado para derivar la clave (Password-Based Key
	 * Derivation Function 2).
	 */
	private static final String ALGORITMO = "PBKDF2WithHmacSHA1";

	/**
	 * * Genera una sal (salt) aleatoria de 16 bytes para un usuario. La sal asegura
	 * que usuarios con la misma contraseña tengan hashes finales diferentes.
	 *
	 * @return array de bytes que representa la sal única.
	 */
	public static byte[] generarSal() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	/**
	 * * Crea un hash irreversible de la contraseña utilizando la sal proporcionada.
	 * Emplea PBKDF2 para transformar la contraseña en una cadena de texto
	 * codificada en Base64.
	 *
	 * @param password La contraseña en texto plano introducida por el usuario.
	 * @param salt     El array de bytes (sal) asociado al usuario.
	 * @return Una cadena (String) con el hash resultante listo para ser almacenado.
	 * @throws RuntimeException Si ocurre un error inesperado durante el proceso de
	 *                          cifrado.
	 */
	public static String hashearContraseña(String password, byte[] salt) {
		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERACIONES, LONGITUD_CLAVE);

			SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITMO);

			byte[] hash = factory.generateSecret(spec).getEncoded();

			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			throw new RuntimeException("Error al hashear contraseña", e);
		}
	}
}