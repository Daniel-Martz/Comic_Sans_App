package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import modelo.producto.ProductoSegundaMano;
import modelo.usuario.ClienteRegistrado;
import vista.userPanels.MakeOfferPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ControladorMakeOffer implements ActionListener, ItemListener {

    private final MakeOfferPanel vista;
    private final MainController mainController;
    
    private Set<ProductoSegundaMano> requestedSelected;
    private Set<ProductoSegundaMano> offeredSelected;

    public ControladorMakeOffer(MakeOfferPanel vista, MainController mainController, Set<ProductoSegundaMano> preseleccionados) {
        this.vista = vista;
        this.mainController = mainController;
        this.requestedSelected = new HashSet<>(preseleccionados);
        this.offeredSelected = new HashSet<>();

        this.vista.addSubmitListener(this);
        this.vista.getPanelRequested().addSearchListener(this);
        this.vista.getPanelOffered().addSearchListener(this);
        
        cargarColumnas();
    }

    public void recargar(Set<ProductoSegundaMano> preseleccionados) {
        this.requestedSelected = new HashSet<>(preseleccionados);
        this.offeredSelected.clear();
        cargarColumnas();
    }

    private void cargarColumnas() {
        cargarRequested(vista.getPanelRequested().getSearchText());
        cargarOffered(vista.getPanelOffered().getSearchText());
    }

    private void cargarRequested(String prompt) {
        ClienteRegistrado yo = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();
        // Reutilizamos la lógica del catálogo para buscar productos validados y disponibles
        List<ProductoSegundaMano> todos = Catalogo.getInstancia().obtenerProductosIntercambioFiltrados(prompt);
        
        List<ProductoSegundaMano> ajenos = new ArrayList<>();
        for (ProductoSegundaMano p : todos) {
            if (!p.getClienteProducto().equals(yo)) ajenos.add(p);
        }
        
        // Ordenamos para que los preseleccionados salgan los primeros
        ajenos.sort((a, b) -> {
            boolean selA = requestedSelected.contains(a);
            boolean selB = requestedSelected.contains(b);
            if (selA && !selB) return -1;
            if (!selA && selB) return 1;
            return a.getNombre().compareToIgnoreCase(b.getNombre());
        });

        vista.getPanelRequested().actualizarProductos(ajenos, requestedSelected, this, this);
        actualizarStatusReq();
    }

    private void cargarOffered(String prompt) {
        ClienteRegistrado yo = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();
        List<ProductoSegundaMano> todos = Catalogo.getInstancia().obtenerProductosIntercambioFiltrados(prompt);
        
        List<ProductoSegundaMano> mios = new ArrayList<>();
        for (ProductoSegundaMano p : todos) {
            if (p.getClienteProducto().equals(yo)) mios.add(p);
        }
        
        mios.sort((a, b) -> {
            boolean selA = offeredSelected.contains(a);
            boolean selB = offeredSelected.contains(b);
            if (selA && !selB) return -1;
            if (!selA && selB) return 1;
            return a.getNombre().compareToIgnoreCase(b.getNombre());
        });

        vista.getPanelOffered().actualizarProductos(mios, offeredSelected, this, this);
        actualizarStatusOff();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        if (cmd.equals("SEARCH_REQ")) {
            cargarRequested(vista.getPanelRequested().getSearchText());
        } else if (cmd.equals("SEARCH_OFF")) {
            cargarOffered(vista.getPanelOffered().getSearchText());
        } else if (cmd.equals("SUBMIT_OFFER")) {
            gestionarSubmit();
        } else if (cmd.startsWith("INFO_")) {
            int id = Integer.parseInt(cmd.substring(5));
            ProductoSegundaMano p = Catalogo.getInstancia().buscarProductoIntercambio(id);
            if (p != null) {
                java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(vista);
                vista.userWindows.VentanaDetallesProductoSegundaMano dialog = new vista.userWindows.VentanaDetallesProductoSegundaMano(parentWindow, p);
                dialog.setVisible(true);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JCheckBox chk) {
            String cmd = chk.getActionCommand();
            if (cmd != null && cmd.length() > 4) {
                int id = Integer.parseInt(cmd.substring(4));
                ProductoSegundaMano p = Catalogo.getInstancia().buscarProductoIntercambio(id);
                if (p != null) {
                    if (cmd.startsWith("REQ_")) {
                        if (e.getStateChange() == ItemEvent.SELECTED) requestedSelected.add(p);
                        else requestedSelected.remove(p);
                        actualizarStatusReq();
                    } else if (cmd.startsWith("OFF_")) {
                        if (e.getStateChange() == ItemEvent.SELECTED) offeredSelected.add(p);
                        else offeredSelected.remove(p);
                        actualizarStatusOff();
                    }
                }
            }
        }
    }

    private void gestionarSubmit() {
        if (requestedSelected.isEmpty() || offeredSelected.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "You must select at least one requested product and one offered product.", "Incomplete Offer", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ClienteRegistrado destinatario = null;
        for (ProductoSegundaMano p : requestedSelected) {
            if (destinatario == null) destinatario = (ClienteRegistrado) p.getClienteProducto();
            else if (!destinatario.equals(p.getClienteProducto())) {
                JOptionPane.showMessageDialog(vista, "All requested products must belong to the same user.\nYou cannot mix products from different owners in a single offer.", "Multiple Owners Selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        ClienteRegistrado yo = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();
        try {
            yo.realizarOferta(offeredSelected, requestedSelected, destinatario);
            JOptionPane.showMessageDialog(vista, "Offer successfully sent to " + destinatario.getNombreUsuario() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            mainController.mostrarMisIntercambios(); // Volvemos a las propuestas
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error sending offer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarStatusReq() {
        double total = requestedSelected.stream().mapToDouble(p -> p.getDatosValidacion().getPrecioEstimadoProducto()).sum();
        vista.getPanelRequested().updateStatus(requestedSelected.size(), total);
    }

    private void actualizarStatusOff() {
        double total = offeredSelected.stream().mapToDouble(p -> p.getDatosValidacion().getPrecioEstimadoProducto()).sum();
        vista.getPanelOffered().updateStatus(offeredSelected.size(), total);
    }
}