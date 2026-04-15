package modelo.usuario;

import java.io.*;
import modelo.aplicacion.Seguridad;

/**
 * Clase abstracta que define la estructura base de un Usuario en el sistema.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public abstract class Usuario implements Serializable{
	
	/** Identificador único para la serialización. */
    private static final long serialVersionUID = 1L;

    /** El nombre de usuario único en el sistema. */
    protected String nombreUsuario;

    /** El DNI del usuario utilizado para validaciones legales. */
    private String dni;

    /** * Representación irreversible (Hash) de la contraseña del usuario. 
     * Nunca se almacena la contraseña en texto plano.
     */
    private String hashContraseña;

    /** * Valor aleatorio (Salt) único por cada usuario para mitigar ataques 
     * de diccionario y tablas arcoíris. 
     */
    private byte[] sal;

    /**
     * Instancia un nuevo usuario y protege su contraseña de forma inmediata
     * generando una sal única y su correspondiente hash.
     *
     * @param nombreUsuario el nombre de usuario único
     * @param dni el DNI del usuario
     * @param contraseña la contraseña en texto plano (procesada y descartada)
     */
    public Usuario(String nombreUsuario, String dni, String contraseña) {
        this.nombreUsuario = nombreUsuario;
        this.dni = dni;
        // Generamos seguridad inicial
        this.sal = Seguridad.generarSal();
        this.hashContraseña = Seguridad.hashearContraseña(contraseña, this.sal);
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
     * Obtiene el DNI del usuario.
     *
     * @return el DNI del usuario
     */
    public String getDNI() {
        return dni;
    }

    /**
     * Verifica si la contraseña introducida coincide con la almacenada.
     * El proceso recrea el hash a partir de la entrada y la sal del usuario.
     *
     * @param contraseñaIntroducida la contraseña a verificar
     * @return {@code true} si la credencial es válida; {@code false} en caso contrario
     */
    public boolean verificarContraseña(String contraseñaIntroducida) {
        // Hasheamos la entrada con la sal guardada y comparamos los hashes
        String hashIntento = Seguridad.hashearContraseña(contraseñaIntroducida, this.sal);
        return this.hashContraseña.equals(hashIntento);
    }

    /**
     * Actualiza la contraseña del usuario. Requiere la verificación de la
     * contraseña antigua por seguridad. Se regenera una nueva sal para 
     * maximizar la entropía del nuevo hash.
     *
     * @param oldPassword la contraseña actual del usuario
     * @param newPassword la nueva contraseña que se desea establecer
     */
    public void setContraseña(String oldPassword, String newPassword) {
        if (verificarContraseña(oldPassword)) {
            // Al cambiar contraseña, es buena práctica generar una sal nueva
            this.sal = Seguridad.generarSal();
            this.hashContraseña = Seguridad.hashearContraseña(newPassword, this.sal);
        }
    }

    /**
     * Devuelve una representación en cadena del usuario. 
     * Por seguridad, no se incluye ni el hash ni la sal en la salida.
     * * @return cadena con el nombre de usuario y DNI
     */
    @Override
    public String toString() {
        return "Usuario: " + nombreUsuario + " (DNI: " + dni + ")";
    }
}