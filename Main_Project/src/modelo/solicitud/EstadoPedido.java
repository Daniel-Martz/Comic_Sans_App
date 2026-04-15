package modelo.solicitud;

/**
 * Representa los diferentes estados por los que puede pasar un pedido.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public enum EstadoPedido {
	
	/** * El pedido ha sido creado pero el pago aún no se ha completado. 
	 */
	PENDIENTE_DE_PAGO,
	
	/** * El pago se ha realizado correctamente y el pedido está a la espera de ser preparado. 
	 */
	PAGADO,
	
	/** * El pedido ya está preparado y esperando a que el cliente vaya a retirarlo. 
	 */
	LISTO_PARA_RECOGER,
	
	/** * El cliente ya ha recogido el pedido y el proceso ha finalizado. 
	 */
	RECOGIDO;
}