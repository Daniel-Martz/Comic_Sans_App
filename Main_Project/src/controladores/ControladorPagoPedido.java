package controladores;

import vista.userWindows.VentanaPagoPedido;
import modelo.aplicacion.Aplicacion;
import modelo.solicitud.SolicitudPedido;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.ClienteRegistrado;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador para la VentanaPagoPedido.
 * Implementa ActionListener para capturar los eventos de la vista.
 */
public class ControladorPagoPedido implements ActionListener {

    private VentanaPagoPedido vista;

    public ControladorPagoPedido(VentanaPagoPedido vista) {
        this.vista = vista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("CONFIRMAR_PAGO_PEDIDO")) {
            gestionarConfirmar();
        }
    }

    private void gestionarConfirmar() {
        // 1. Leer datos de la vista mediante getters
        String numTarjeta = vista.getNumeroTarjeta();
        String cvv = vista.getCVV();
        String caducidad = vista.getCaducidad();

        if (numTarjeta.isEmpty() || cvv.isEmpty() || caducidad.isEmpty()) {
            vista.mostrarAvisoIncompleto("Por favor, rellena todos los campos.", "Campos incompletos");
            return;
        }

        DateTimeSimulado fechaCaducidad = parsearFecha(caducidad);
        if (fechaCaducidad == null) {
            vista.mostrarAvisoIncompleto("Formato de fecha incorrecto. Usa MM/AA (p.ej. 12/26).", "Fecha inválida");
            return;
        }

        // 2. Llamar al modelo para pagar el pedido que se mostró en la ventana
        SolicitudPedido pedidoReal = vista.getPedido();
        try {
            if (pedidoReal == null) {
                throw new IllegalStateException("No hay pedido asociado a la ventana de pago.");
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