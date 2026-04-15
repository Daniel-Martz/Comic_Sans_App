package modelo.producto;

/**
 * Define los posibles estados de conservación de un producto de segunda mano.
 * Estos estados son asignados por un empleado durante el proceso de validación
 * y determinan, en gran medida, la valoración del producto.
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public enum EstadoConservacion {
	/** El producto está como nuevo, sin marcas de uso. */
	PERFECTO,
	
	/** El producto tiene marcas casi imperceptibles. */
	MUY_BUENO,
	
	/** Marcas de uso normales que no afectan a la estética general. */
	USO_LIGERO,
	
	/** Desgaste visible por el uso continuado. */
	USO_EVIDENTE,
	
	/** Desgaste severo, pero el producto sigue siendo funcional. */
	MUY_USADO,
	
	/** El producto tiene daños estructurales o le faltan componentes. */
	DANADO
}