package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.SolicitudValidacion;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.ClienteRegistrado;
import vista.clienteWindows.VentanaPagoValidacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: Auto-generated Javadoc
/**
 * Controlador de la ventana de pago de validación (producto de segunda mano).
 *
 * Lee los datos de la tarjeta de la vista, valida campos básicos y llama al
 * modelo para procesar el pago de la solicitud de validación.
 */
public class ControladorPagoValidacion implements ActionListener {

    /** The vista. */
    private VentanaPagoValidacion vista;
    
    /** The solicitud modelo. */
    private SolicitudValidacion solicitudModelo;

    /**
     * Crea el controlador con la vista y la solicitud a pagar.
     *
     * @param vista ventana de pago
     * @param solicitudModelo solicitud de validación asociada
     */
    public ControladorPagoValidacion(VentanaPagoValidacion vista, SolicitudValidacion solicitudModelo) {
        this.vista = vista;
        this.solicitudModelo = solicitudModelo;
    }

    /**
     * Maneja el evento de confirmación de pago.
     *
     * @param e evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("CONFRIM_PAYMENT")) {
            gestionarConfirmar();
        }
    }

    /**
     * Valida campos, parsea la fecha y llama al modelo para pagar la validación.
     * Muestra ventanas de éxito o error según corresponda.
     */
    private void gestionarConfirmar() {
        String numTarjeta = vista.getNumeroTarjeta();
        String cvv = vista.getCVV();
        String caducidad = vista.getCaducidad();

        if (numTarjeta.isEmpty() || cvv.isEmpty() || caducidad.isEmpty()) {
            vista.mostrarAvisoIncompleto("Please, fill all the fileds.", "Incomplete data");
            return;
        }

        DateTimeSimulado fechaCaducidad = parsearFecha(caducidad);
        if (fechaCaducidad == null) {
            vista.mostrarAvisoIncompleto("Incorrect format date. Use MM/AA (p.ej. 12/26).", "Invalid date");
            return;
        }

        try {
            ClienteRegistrado cliente = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();
            cliente.pagarValidacion(solicitudModelo, numTarjeta, cvv, fechaCaducidad);

            vista.mostrarVentanaExito();
            vista.dispose();

        } catch (Exception ex) {
            vista.mostrarVentanaError(ex.getMessage());
        }
    }

    /**
     * Parsea una fecha en formato MM/AA y devuelve un DateTimeSimulado.
     *
     * @param texto cadena MM/AA
     * @return objeto DateTimeSimulado o null si el formato no es válido
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