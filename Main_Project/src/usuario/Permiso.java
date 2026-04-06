package usuario;

/**
 * Implementa la enumeración Permiso. Define los diferentes niveles de acceso
 * que puede tener un empleado en el sistema.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public enum Permiso {

	/** Permiso para gestionar y aprobar las validaciones de productos. */
	VALIDACIONES,

	/** Permiso para gestionar y actualizar los estados de los pedidos. */
	PEDIDOS,

	/** Permiso para gestionar y aprobar las solicitudes de intercambios. */
	INTERCAMBIOS;
}