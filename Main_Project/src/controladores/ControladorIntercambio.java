package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.Oferta;
import modelo.producto.ProductoSegundaMano;
import modelo.usuario.ClienteRegistrado;
import vista.userPanels.InterchangeCardPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

/**
 * Controlador para InterchangeCardPanel.
 *
 * Responsabilidades:
 *  1. Extraer datos del modelo (Oferta) y pasarlos a la vista en formato String[][].
 *  2. Registrar los listeners en la vista.
 *  3. Ejecutar la lógica de negocio cuando el usuario pulsa Accept o Reject.
 *  4. Notificar al controlador principal (MainController) para navegar a otra vista.
 *
 * Sigue MVC:
 *  - La vista NO conoce el modelo.
 *  - El modelo NO conoce la vista.
 *  - Este controlador es el único punto de contacto entre ambos.
 */
public class ControladorIntercambio implements ActionListener {

    // -------------------------------------------------------
    // Referencias a los tres elementos del MVC
    // -------------------------------------------------------
    private final InterchangeCardPanel vista;
    private final Oferta oferta;                  // modelo
    private final MainController mainController;  // para navegar entre vistas

    // -------------------------------------------------------
    // Comandos de acción (identifican qué botón se pulsó)
    // -------------------------------------------------------
    public static final String ACCION_ACEPTAR  = "ACEPTAR";
    public static final String ACCION_RECHAZAR = "RECHAZAR";

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * @param vista          la vista que este controlador gestiona
     * @param oferta         la oferta del modelo que se está mostrando
     * @param mainController el controlador principal, para navegación
     */
    public ControladorIntercambio(InterchangeCardPanel vista,
                                   Oferta oferta,
                                   MainController mainController) {
        this.vista          = vista;
        this.oferta         = oferta;
        this.mainController = mainController;

        // 1. Calcular datos y actualizar la vista
        cargarDatosEnVista();

        // 2. Registrar listeners en la vista
        vista.addAcceptListener(this);   // botón Accept → this.actionPerformed
        vista.addRejectListener(this);   // botón Reject → this.actionPerformed
    }

    // -------------------------------------------------------
    // Carga de datos del modelo a la vista
    // -------------------------------------------------------

    /**
     * Extrae los datos de la Oferta (modelo) y los convierte a
     * String[][] para pasárselos a la vista.
     * La vista NO necesita saber qué es un ProductoSegundaMano.
     */
    private void cargarDatosEnVista() {
        String usernameFrom = oferta.getOfertante().getNombreUsuario();

        // Productos que el ofertante DA (los que nosotros recibiríamos)
        String[][] receivedData = convertirProductos(oferta.productosOfertados());

        // Productos que el ofertante PIDE (los que nosotros daríamos)
        String[][] givenData = convertirProductos(oferta.productosSolicitados());

        // El balance lo calcula el controlador, NO la vista
        double balance = calcularBalance(oferta.productosOfertados(),
                                         oferta.productosSolicitados());

        // ¿El usuario actual es el destinatario? → puede aceptar
        boolean esDestinatario = esUsuarioActualElDestinatario();

        vista.update(usernameFrom, receivedData, givenData, balance, esDestinatario);
    }

    /**
     * Convierte un Set de ProductoSegundaMano a String[][] para la tabla.
     * Columnas: [nombre, estadoConservacion, precio]
     */
    private String[][] convertirProductos(Set<ProductoSegundaMano> productos) {
        String[][] data = new String[productos.size()][3];
        int i = 0;
        for (ProductoSegundaMano p : productos) {
            data[i][0] = p.getNombre();
            data[i][1] = p.getDatosValidacion() != null
                    ? p.getDatosValidacion().getEstadoConservacion().toString()
                    : "Pendiente";
            data[i][2] = p.getDatosValidacion() != null
                    ? String.format("%.2f €", p.getDatosValidacion().getPrecioEstimadoProducto())
                    : "N/A";
            i++;
        }
        return data;
    }

    /**
     * Calcula el balance económico del intercambio.
     * Lo que recibo - Lo que doy.
     * Esta lógica estaba en la vista antes — aquí es donde debe estar.
     */
    private double calcularBalance(Set<ProductoSegundaMano> recibo,
                                    Set<ProductoSegundaMano> doy) {
        double totalRecibo = 0.0;
        double totalDoy    = 0.0;

        for (ProductoSegundaMano p : recibo) {
            if (p.getDatosValidacion() != null) {
                totalRecibo += p.getDatosValidacion().getPrecioEstimadoProducto();
            }
        }
        for (ProductoSegundaMano p : doy) {
            if (p.getDatosValidacion() != null) {
                totalDoy += p.getDatosValidacion().getPrecioEstimadoProducto();
            }
        }

        return totalRecibo - totalDoy;
    }

    /**
     * Comprueba si el usuario logueado es el destinatario de esta oferta.
     * Si lo es, puede aceptar. Si es el ofertante, solo puede rechazar.
     */
    private boolean esUsuarioActualElDestinatario() {
        if (Aplicacion.getInstancia().getUsuarioActual() == null) return false;
        String usuarioActual = Aplicacion.getInstancia().getUsuarioActual().getNombreUsuario();
        return usuarioActual.equals(oferta.getDestinatario().getNombreUsuario());
    }

    // -------------------------------------------------------
    // Gestión de eventos (ActionListener)
    // -------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("ACCEPT".equals(comando)) {
            aceptarOferta();
        } else if ("REJECT".equals(comando)) {
            rechazarOferta();
        }
    }

    // -------------------------------------------------------
    // Lógica de negocio delegada al modelo
    // -------------------------------------------------------

    private void aceptarOferta() {
        try {
            ClienteRegistrado destinatario = oferta.getDestinatario();
            destinatario.aceptarOferta(oferta);
            vista.mostrarMensaje("¡Oferta aceptada con éxito! Se ha creado la solicitud de intercambio.");
            // Avisamos al controlador principal para navegar a otra vista
            mainController.navegarA(MainController.PANEL_MIS_INTERCAMBIOS);
        } catch (IllegalStateException ex) {
            vista.mostrarError("No se pudo aceptar la oferta: " + ex.getMessage());
        }
    }

    private void rechazarOferta() {
        try {
            ClienteRegistrado destinatario = oferta.getDestinatario();
            destinatario.rechazarOferta(oferta);
            vista.mostrarMensaje("Oferta rechazada.");
            mainController.navegarA(MainController.PANEL_MIS_INTERCAMBIOS);
        } catch (IllegalStateException ex) {
            vista.mostrarError("No se pudo rechazar la oferta: " + ex.getMessage());
        }
    }
}

