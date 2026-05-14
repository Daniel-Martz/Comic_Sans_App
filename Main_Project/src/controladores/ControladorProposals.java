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

// TODO: Auto-generated Javadoc
/**
 * Controlador para la ventana y panel de propuestas (intercambios).
 *
 * Hace de puente entre el modelo (ofertas) y la vista: crea las cards para
 * cada oferta, registra listeners y delega las acciones en el modelo.
 */
public class ControladorProposals {
	
    /** The ventana. */
    private final ProposalsWindow ventana;
    
    /** The panel. */
    private final ProposalsPanel  panel;
    
    /**
     * Inicializa el controlador, carga las ofertas del cliente actual y crea las
     * cards correspondientes en la vista.
     *
     * @param ventana the ventana
     * @param mainController the main controller
     */
    public ControladorProposals(ProposalsWindow ventana, MainController mainController) {
        this.ventana        = ventana;
        this.panel          = ventana.getProposalsPanel();
        cargarOfertas();
    }

    /**
     * Obtiene las ofertas del modelo y crea una card por cada una.
     */
    private void cargarOfertas() {
        panel.clear();

        if (!(Aplicacion.getInstancia().getUsuarioActual() instanceof ClienteRegistrado)) {
            return;
        }

        ClienteRegistrado cliente =
                (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();

        // ── Ofertas RECIBIDAS (columna INCOME)
        for (Oferta oferta : cliente.getOfertasRecibidas()) {
            InterchangeCardPanel card = crearCard(oferta, Modo.INCOME);
            registrarListenersIncome(card, oferta, cliente);
            panel.addIncomeCard(card);
        }

        // ── Ofertas ENVIADAS (columna SENT)
        for (Oferta oferta : cliente.getOfertasRealizadas()) {
            InterchangeCardPanel card = crearCard(oferta, Modo.SENT);
            registrarListenersSent(card, oferta, cliente);
            panel.addSentCard(card);
        }
    }



    /**
     * Construye una InterchangeCardPanel a partir de una Oferta.
     * La extracción de datos del modelo se realiza aquí; la vista recibe
     * únicamente arrays de String con la información ya formateada.
     *
     * @param oferta the oferta
     * @param modo the modo
     * @return the interchange card panel
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
     * Convierte un Set de ProductoSegundaMano a array.
     *
     * @param productos the productos
     * @return the producto segunda mano[]
     */
    private ProductoSegundaMano[] convertirProductos(Set<ProductoSegundaMano> productos) {
        return productos.toArray(new ProductoSegundaMano[0]);
    }

    /**
     * Calcula el balance económico desde el punto de vista del usuario.
     * Para INCOME se calcula (recibo - doy); para SENT se invierte la lógica.
     *
     * @param ofertados the ofertados
     * @param solicitados the solicitados
     * @param modo the modo
     * @return the double
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

    /**
     * Sumar valor.
     *
     * @param productos the productos
     * @return the double
     */
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
     * Registra ACCEPT y REJECT en una card de tipo INCOME (ofertas recibidas).
     *
     * @param card the card
     * @param oferta the oferta
     * @param cliente the cliente
     */
    private void registrarListenersIncome(InterchangeCardPanel card,
                                           Oferta oferta,
                                           ClienteRegistrado cliente) {
        card.addAcceptListener(e -> aceptarOferta(oferta, cliente));
        card.addRejectListener(e -> rechazarOferta(oferta, cliente));
        card.setInfoListener(e -> procesarClickInfo(e.getActionCommand(), cliente));
    }

    /**
     * Registra CANCEL en una card de tipo SENT (ofertas enviadas).
     *
     * @param card the card
     * @param oferta the oferta
     * @param cliente the cliente
     */
    private void registrarListenersSent(InterchangeCardPanel card,
                                         Oferta oferta,
                                         ClienteRegistrado cliente) {
        card.addCancelListener(e -> cancelarOferta(oferta, cliente));
        card.setInfoListener(e -> procesarClickInfo(e.getActionCommand(), cliente));
    }


    /**
     * Aceptar oferta.
     *
     * @param oferta the oferta
     * @param cliente the cliente
     */
    private void aceptarOferta(Oferta oferta, ClienteRegistrado cliente) {
        try {
            cliente.aceptarOferta(oferta);
            ventana.mostrarVentanaExito("Offer accepted! An exchange request has been created.");
            recargar();
        } catch (IllegalStateException ex) {
            ventana.mostrarVentanaError("The offer could not be accepted: " + ex.getMessage());
        }
    }

    /**
     * Reject offer.
     *
     * @param oferta the offer
     * @param cliente the customer
     */
    private void rechazarOferta(Oferta oferta, ClienteRegistrado cliente) {
        try {
            cliente.rechazarOferta(oferta);
            ventana.mostrarVentanaExito("Offer rejected.");
            recargar();
        } catch (IllegalStateException ex) {
            ventana.mostrarVentanaError("The offer could not be rejected: " + ex.getMessage());
        }
    }

    /**
     * Cancelar oferta.
     *
     * @param oferta the oferta
     * @param cliente the cliente
     */
    private void cancelarOferta(Oferta oferta, ClienteRegistrado cliente) {
        try {
            cliente.cancelarOferta(oferta);
            ventana.mostrarVentanaExito("Canceled offer.");
            recargar();
        } catch (IllegalStateException ex) {
            ventana.mostrarVentanaError("The offer could not be canceled: " + ex.getMessage());
        }
    }

    /**
     * Procesa el evento de la vista para mostrar los detalles de un producto
     * contenido en una oferta.
     *
     * @param command the command
     * @param cliente the cliente
     */
    private void procesarClickInfo(String command, ClienteRegistrado cliente) {
        if (command != null && command.startsWith("INFO_")) {
            try {
                int id = Integer.parseInt(command.substring(5));
                mostrarDetallesProducto(id, cliente);
            } catch (NumberFormatException ex) {
                ventana.mostrarVentanaError("Invalid product ID.");
            }
        }
    }

    /**
     * Busca el producto en las ofertas (recibidas y enviadas) y abre la ventana
     * de detalles si se encuentra.
     *
     * @param idProducto the id producto
     * @param cliente the cliente
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
            vista.clienteWindows.VentanaDetallesProductoSegundaManoWindow dialog = 
                new vista.clienteWindows.VentanaDetallesProductoSegundaManoWindow(ventana, target);
            dialog.setVisible(true);
        } else {
            ventana.mostrarVentanaError("Product not found.");
        }
    }

    /**
     * Recarga todas las cards desde el modelo. Se llama después de acciones
     * que cambien el estado de las ofertas.
     */
    private void recargar() {
        cargarOfertas();
    }
}