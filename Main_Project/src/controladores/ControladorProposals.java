package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.Oferta;
import modelo.producto.ProductoSegundaMano;
import modelo.usuario.ClienteRegistrado;
import vista.clienteWindows.ProposalsWindow;
import vista.userPanels.InterchangeCardPanel;
import vista.userPanels.InterchangeCardPanel.Modo;
import vista.userPanels.ProposalsPanel;

import java.util.Set;

/**
 * Controlador para ProposalsWindow + ProposalsPanel.
 *
 * Responsabilidades:
 *  1. Obtener las ofertas recibidas y enviadas del modelo.
 *  2. Convertir cada Oferta en datos String[][] para la vista.
 *  3. Crear una InterchangeCardPanel por cada oferta y añadirla al panel.
 *  4. Registrar listeners en cada card (accept, reject, cancel).
 *  5. Ejecutar la lógica de negocio cuando el usuario pulsa un botón.
 *  6. Recargar la vista cuando algo cambia.
 */
public class ControladorProposals {

    // -------------------------------------------------------
    // Referencias MVC
    // -------------------------------------------------------
    private final ProposalsWindow ventana;
    private final ProposalsPanel  panel;
    private final MainController  mainController;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public ControladorProposals(ProposalsWindow ventana, MainController mainController) {
        this.ventana        = ventana;
        this.panel          = ventana.getProposalsPanel();
        this.mainController = mainController;

        cargarOfertas();
    }

    // -------------------------------------------------------
    // Carga inicial de datos
    // -------------------------------------------------------

    /**
     * Obtiene las ofertas del modelo y crea una card por cada una.
     */
    private void cargarOfertas() {
        panel.limpiar();

        if (!(Aplicacion.getInstancia().getUsuarioActual() instanceof ClienteRegistrado)) {
            return;
        }

        ClienteRegistrado cliente =
                (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();

        // ── Ofertas RECIBIDAS (columna INCOME)
        for (Oferta oferta : cliente.getOfertasRecibidas()) {
            InterchangeCardPanel card = crearCard(oferta, Modo.INCOME);
            registrarListenersIncome(card, oferta, cliente);
            panel.añadirCardIncome(card);
        }

        // ── Ofertas ENVIADAS (columna SENT)
        for (Oferta oferta : cliente.getOfertasRealizadas()) {
            InterchangeCardPanel card = crearCard(oferta, Modo.SENT);
            registrarListenersSent(card, oferta, cliente);
            panel.añadirCardSent(card);
        }
    }

    // -------------------------------------------------------
    // Creación de cards
    // -------------------------------------------------------

    /**
     * Construye una InterchangeCardPanel a partir de una Oferta.
     * Toda la extracción de datos del modelo ocurre aquí,
     * la vista solo recibe String[][].
     */
    private InterchangeCardPanel crearCard(Oferta oferta, Modo modo) {
        String headerLabel;
        ProductoSegundaMano[] givenData;
        ProductoSegundaMano[] receivedData;

        if (modo == Modo.INCOME) {
            // La oferta la recibo yo: el ofertante da, yo doy lo solicitado
            headerLabel   = "FROM: " + oferta.getOfertante().getNombreUsuario();
            givenData     = convertirProductos(oferta.productosSolicitados());  // lo que él pide (yo doy)
            receivedData  = convertirProductos(oferta.productosOfertados());    // lo que él ofrece (yo recibo)
        } else {
            // La oferta la envié yo: yo ofrezco, pido lo del destinatario
            headerLabel   = "TO: " + oferta.getDestinatario().getNombreUsuario();
            givenData     = convertirProductos(oferta.productosOfertados());    // lo que yo ofrezco
            receivedData  = convertirProductos(oferta.productosSolicitados());  // lo que yo pido
        }

        double balance = calcularBalance(oferta.productosOfertados(),
                                          oferta.productosSolicitados(),
                                          modo);

        return new InterchangeCardPanel(headerLabel, balance, givenData, receivedData, modo, null, null);
    }

    /**
     * Convierte un Set de ProductoSegundaMano a array de ProductoSegundaMano.
     */
    private ProductoSegundaMano[] convertirProductos(Set<ProductoSegundaMano> productos) {
        return productos.toArray(new ProductoSegundaMano[0]);
    }

    /**
     * Calcula el balance económico desde el punto de vista del usuario actual.
     * INCOME: lo que recibo - lo que doy.
     * SENT:   lo que recibo - lo que doy (mismo cálculo, distinto signo conceptual).
     */
    private double calcularBalance(Set<ProductoSegundaMano> ofertados,
                                    Set<ProductoSegundaMano> solicitados,
                                    Modo modo) {
        double valorOfertados   = sumarValor(ofertados);
        double valorSolicitados = sumarValor(solicitados);

        if (modo == Modo.INCOME) {
            // Yo recibo lo ofertado, doy lo solicitado
            return valorOfertados - valorSolicitados;
        } else {
            // Yo doy lo ofertado, recibo lo solicitado
            return valorSolicitados - valorOfertados;
        }
    }

    private double sumarValor(Set<ProductoSegundaMano> productos) {
        double total = 0.0;
        for (ProductoSegundaMano p : productos) {
            if (p.getDatosValidacion() != null) {
                total += p.getDatosValidacion().getPrecioEstimadoProducto();
            }
        }
        return total;
    }

    // -------------------------------------------------------
    // Registro de listeners
    // -------------------------------------------------------

    /**
     * Registra ACCEPT y REJECT en una card de tipo INCOME.
     */
    private void registrarListenersIncome(InterchangeCardPanel card,
                                           Oferta oferta,
                                           ClienteRegistrado cliente) {
        card.addAcceptListener(e -> aceptarOferta(oferta, cliente));
        card.addRejectListener(e -> rechazarOferta(oferta, cliente));
        card.setInfoListener(e -> procesarClickInfo(e.getActionCommand(), cliente));
    }

    /**
     * Registra CANCEL en una card de tipo SENT.
     */
    private void registrarListenersSent(InterchangeCardPanel card,
                                         Oferta oferta,
                                         ClienteRegistrado cliente) {
        card.addCancelListener(e -> cancelarOferta(oferta, cliente));
        card.setInfoListener(e -> procesarClickInfo(e.getActionCommand(), cliente));
    }

    // -------------------------------------------------------
    // Lógica de negocio (delegada al modelo)
    // -------------------------------------------------------

    private void aceptarOferta(Oferta oferta, ClienteRegistrado cliente) {
        try {
            cliente.aceptarOferta(oferta);
            ventana.mostrarVentanaExito("¡Oferta aceptada! Se ha creado la solicitud de intercambio.");
            recargar();
        } catch (IllegalStateException ex) {
            ventana.mostrarVentanaError("No se pudo aceptar la oferta: " + ex.getMessage());
        }
    }

    private void rechazarOferta(Oferta oferta, ClienteRegistrado cliente) {
        try {
            cliente.rechazarOferta(oferta);
            ventana.mostrarVentanaExito("Oferta rechazada.");
            recargar();
        } catch (IllegalStateException ex) {
            ventana.mostrarVentanaError("No se pudo rechazar la oferta: " + ex.getMessage());
        }
    }

    private void cancelarOferta(Oferta oferta, ClienteRegistrado cliente) {
        try {
            cliente.cancelarOferta(oferta);
            ventana.mostrarVentanaExito("Oferta cancelada.");
            recargar();
        } catch (IllegalStateException ex) {
            ventana.mostrarVentanaError("No se pudo cancelar la oferta: " + ex.getMessage());
        }
    }

    /**
     * Procesa el evento de la vista para mostrar los detalles de un producto.
     */
    private void procesarClickInfo(String command, ClienteRegistrado cliente) {
        if (command != null && command.startsWith("INFO_")) {
            try {
                int id = Integer.parseInt(command.substring(5));
                mostrarDetallesProducto(id, cliente);
            } catch (NumberFormatException ex) {
                ventana.mostrarVentanaError("ID de producto inválido.");
            }
        }
    }

    /**
     * Busca el producto en las ofertas y delega la apertura de la ventana (MVC correcto).
     */
    private void mostrarDetallesProducto(int idProducto, ClienteRegistrado cliente) {
        ProductoSegundaMano target = null;
        
        // Buscar en ofertas recibidas
        for (Oferta o : cliente.getOfertasRecibidas()) {
            for (ProductoSegundaMano p : o.productosOfertados()) { if (p.getID() == idProducto) { target = p; break; } }
            if (target != null) break;
            for (ProductoSegundaMano p : o.productosSolicitados()) { if (p.getID() == idProducto) { target = p; break; } }
            if (target != null) break;
        }
        
        // Buscar en ofertas enviadas
        if (target == null) {
            for (Oferta o : cliente.getOfertasRealizadas()) {
                for (ProductoSegundaMano p : o.productosOfertados()) { if (p.getID() == idProducto) { target = p; break; } }
                if (target != null) break;
                for (ProductoSegundaMano p : o.productosSolicitados()) { if (p.getID() == idProducto) { target = p; break; } }
                if (target != null) break;
            }
        }

        if (target != null) {
            // Ahora sí, es el CONTROLADOR quien abre la ventana con los datos del Modelo
            vista.clienteWindows.VentanaDetallesProductoSegundaMano dialog = 
                new vista.clienteWindows.VentanaDetallesProductoSegundaMano(ventana, target);
            dialog.setVisible(true);
        } else {
            ventana.mostrarVentanaError("Producto no encontrado.");
        }
    }

    /**
     * Recarga todas las cards desde el modelo.
     * Se llama tras cualquier acción que cambie el estado de las ofertas.
     */
    private void recargar() {
        cargarOfertas();
    }
}