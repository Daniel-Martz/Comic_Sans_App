package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.SolicitudValidacion;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.ClienteRegistrado;
import vista.clienteWindows.VentanaPagoValidacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador para la VentanaPago.
 * Implementa ActionListener para capturar los eventos de la vista. [cite: 8, 80]
 */
public class ControladorPagoValidacion implements ActionListener {

    private VentanaPagoValidacion vista;
    private SolicitudValidacion solicitudModelo;

    public ControladorPagoValidacion(VentanaPagoValidacion vista, SolicitudValidacion solicitudModelo) {
        this.vista = vista;
        this.solicitudModelo = solicitudModelo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Si se ha pulsado el botón Confirmar [cite: 95]
        if (e.getActionCommand().equals("CONFIRMAR_PAGO")) {
            gestionarConfirmar();
        }
    }

    private void gestionarConfirmar() {
        // 1. Validar valores en la vista leyendo a través de sus getters [cite: 101, 102]
        String numTarjeta = vista.getNumeroTarjeta();
        String cvv = vista.getCVV();
        String caducidad = vista.getCaducidad();

        if (numTarjeta.isEmpty() || cvv.isEmpty() || caducidad.isEmpty()) {
            vista.mostrarAvisoIncompleto("Por favor, rellena todos los campos.", "Campos incompletos");
            return; // [cite: 105]
        }

        DateTimeSimulado fechaCaducidad = parsearFecha(caducidad);
        if (fechaCaducidad == null) {
            vista.mostrarAvisoIncompleto("Formato de fecha incorrecto. Usa MM/AA (p.ej. 12/26).", "Fecha inválida");
            return;
        }

        // 2. Modificar el modelo (Procesar el pago) [cite: 106]
        try {
            ClienteRegistrado cliente = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();
            cliente.pagarValidacion(solicitudModelo, numTarjeta, cvv, fechaCaducidad);

            // 3. Generar / mostrar la nueva vista (Éxito) [cite: 109]
            vista.mostrarVentanaExito();
            vista.dispose(); // Cierra la ventana de pago

        } catch (Exception ex) {
            // Mostrar vista de error
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