package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.SolicitudPedido;
import modelo.tiempo.DateTimeSimulado;
import vista.clienteWindows.VentanaPagoPedidoWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador de la ventana de pago de pedidos (cliente).
 *
 * Lee los datos de la tarjeta en la vista, los valida mínimamente y delega en
 * el modelo para procesar el pago del pedido asociado a la ventana.
 */
public class ControladorPagoPedido implements ActionListener {

    private VentanaPagoPedidoWindow vista;

    /**
     * Crea el controlador para la ventana de pago.
     *
     * @param vista ventana de pago que maneja este controlador
     */
    public ControladorPagoPedido(VentanaPagoPedidoWindow vista) {
        this.vista = vista;
    }

    /**
     * Escucha el botón de confirmación de pago y lanza la lógica de pago.
     *
     * @param e evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("CONFIRM_ORDER_PAYMENT")) {
            gestionarConfirmar();
        }
    }

    /**
     * Valida los campos de la vista, parsea la fecha de caducidad y llama al
     * modelo para procesar el pago. Muestra ventanas de éxito/error según el caso.
     */
    private void gestionarConfirmar() {
        // 1. Leer datos de la vista mediante getters
        String numTarjeta = vista.getNumeroTarjeta();
        String cvv = vista.getCVV();
        String caducidad = vista.getCaducidad();

        if (numTarjeta.isEmpty() || cvv.isEmpty() || caducidad.isEmpty()) {
            vista.mostrarAvisoIncompleto("Please, fill all the fields", "Incomplete fields");
            return;
        }

        DateTimeSimulado fechaCaducidad = parsearFecha(caducidad);
        if (fechaCaducidad == null) {
            vista.mostrarAvisoIncompleto("Incorrect format date. Use MM/AA (p.ej. 12/26)." ,"Invalid date");
            return;
        }

        // 2. Llamar al modelo para pagar el pedido que se mostró en la ventana
        SolicitudPedido pedidoReal = vista.getPedido();
        try {
            if (pedidoReal == null) {
                throw new IllegalStateException("There is no order associated with the payment window.");
            }

            Aplicacion.getInstancia().gestionarPagoPedido(pedidoReal, numTarjeta, cvv, fechaCaducidad);

            // 3. Mostrar resultado y cerrar ventana
            vista.mostrarVentanaExito();
            vista.dispose();

        } catch (Exception ex) {
            // En caso de error (pedido caducado, datos inválidos, etc.) mostramos la ventana de error
            vista.mostrarVentanaError(ex.getMessage());
        }
    }

    /**
     * Parsea la fecha de caducidad en formato MM/AA y devuelve un objeto
     * DateTimeSimulado con el primer día del mes.
     *
     * @param texto cadena en formato MM/AA
     * @return DateTimeSimulado o null si el formato es inválido
     */
    private DateTimeSimulado parsearFecha(String texto) {
        try {
            String[] partes = texto.split("/");
            if (partes.length != 2) return null;
            int mes = Integer.parseInt(partes[0].trim());
            int año = Integer.parseInt(partes[1].trim());
            if (año < 100) año += 2000;
            return new DateTimeSimulado(año, mes, 1, 0, 0, 0);
        } catch (Exception e) {
            return null;
        }
    }
}
