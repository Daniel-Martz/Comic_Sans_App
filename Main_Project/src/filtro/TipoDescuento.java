package filtro;

/**
 * Define los tipos de descuentos disponibles para el filtrado.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public enum TipoDescuento {
	/** Descuento por volumen de compra (ej. 3x2). */
	CANTIDAD, 
	
	/** Descuento porcentual o directo sobre el precio. */
	PRECIO, 
	
	/** Rebaja aplicada al superar un umbral de gasto. */
	REBAJA, 
	
	/** Inclusión de un producto gratuito. */
	REGALO;
}