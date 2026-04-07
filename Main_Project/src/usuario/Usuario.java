package usuario;
import java.io.*;
/**
 * Implementa la clase Usuario.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public abstract class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** El nombre de usuario. */
	protected String nombreUsuario;
	
	/** El DNI del usuario. */
	private String dni;
	
	/** La contraseña del usuario. */
	private String contraseña;
	
	/**
	 * Instancia un nuevo usuario.
	 *
	 * @param nombreUsuario el nombre de usuario del nuevo usuario
	 * @param dni el DNI
	 * @param contraseña la contraseña
	 */
	public Usuario(String nombreUsuario, String dni, String contraseña) {
		this.nombreUsuario = nombreUsuario;
		this.dni = dni;
		this.contraseña = contraseña;
	}
	
	/**
	 * Obtiene el nombre de usuario.
	 *
	 * @return el nombre de usuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * Obtiene el DNI de un usuario.
	 *
	 * @return el DNI del usuario 
	 */
	public String getDNI() {
		return dni;
	}
	
	/**
	 * Verifica si la contraseña introducida coincide con la del usuario.
	 *
	 * @param contraseñaIntroducida la contraseña introducida
	 * @return true, si la contraseña es correcta; false en caso contrario
	 */
	public boolean verificarContraseña(String contraseñaIntroducida) {
		return this.contraseña.equals(contraseñaIntroducida);
	}

	/**
	 * Establece una nueva contraseña si la antigua es correcta.
	 *
	 * @param oldPassword la contraseña antigua
	 * @param newPassword la nueva contraseña
	 */
	public void setContraseña(String oldPassword, String newPassword) {
		if(verificarContraseña(oldPassword)) {
			this.contraseña = newPassword;
		}
	}

}
