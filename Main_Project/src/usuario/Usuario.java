package usuario;

/**
 * Implementa la clase Usuario
 * @author Matteo Artuñedo
 * @version 1.0
 * @date 13-03-2026
 */
public abstract class Usuario {
	
	/** El nombre de usuario. */
	protected String nombreUsuario;
	
	/** El dni del usuario. */
	private String dni;
	
	/** La contraseña del usuario. */
	private String contraseña;
	
	/**
	 * Instancia un nuevo usuario.
	 *
	 * @param username el nombre de usuario del nuevo usuario
	 * @param DNI the dni
	 * @param password the password
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
	 * obtiene el DNI de un usuario
	 *
	 * @return DNI del usuario 
	 */
	public String getDNI() {
		return dni;
	}
	
	public boolean verificarContraseña(String contraseñaIntroducida) {
		return this.contraseña.equals(contraseñaIntroducida);
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setContraseña(String oldPassword, String newPassword) {
		if(verificarContraseña(oldPassword)) {
			this.contraseña = newPassword;
		}
	}

}
