package controladores;

import java.awt.event.ItemEvent;

import vista.clienteWindows.*;

// TODO: Auto-generated Javadoc
/**
 * Controlador de la ventana de filtros.
 *
 * Gestiona la lógica de los checkboxes para seleccionar una categoría y
 * actualizar la parte derecha de la ventana según la categoría elegida.
 */
public class ControladorFiltros {

    /** The vista. */
    private FiltrosWindow vista;

    /**
     * Crea el controlador y enlaza los listeners necesarios.
     *
     * @param vista ventana de filtros que controla
     */
    public ControladorFiltros(FiltrosWindow vista) {
        this.vista = vista;
        inicializarListeners();
    }

    /**
     * Añade los listeners a los checkboxes para mantener comportamiento tipo
     * toggle entre categorías y cambiar la vista derecha.
     */
    private void inicializarListeners() {
        
        // --- LÓGICA PARA CÓMICS ---
        vista.getChkComics().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // Desmarcamos las otras para mantener exclusividad
                vista.getChkFigures().setSelected(false);
                vista.getChkBoardGames().setSelected(false);
                
                vista.cambiarVistaDerecha("COMICS");
                vista.setTitle("Comic Filters");
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                // Si se desmarca, volvemos al estado inicial vacío
                vista.cambiarVistaDerecha("NO_CATEGORY");
                vista.setTitle("No Category Filters");
            }
        });

        // --- LÓGICA PARA FIGURAS ---
        vista.getChkFigures().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // Desmarcamos las otras
                vista.getChkComics().setSelected(false);
                vista.getChkBoardGames().setSelected(false);
                
                vista.cambiarVistaDerecha("FIGURES");
                vista.setTitle("Figures Filters");
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                // Si se desmarca, volvemos al estado inicial vacío
                vista.cambiarVistaDerecha("NO_CATEGORY");
                vista.setTitle("No Category Filters");
            }
        });

        // --- LÓGICA PARA JUEGOS DE MESA ---
        vista.getChkBoardGames().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // Desmarcamos las otras
                vista.getChkComics().setSelected(false);
                vista.getChkFigures().setSelected(false);
                
                vista.cambiarVistaDerecha("BOARD_GAMES");
                vista.setTitle("Board Games Filters");
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                // Si se desmarca, volvemos al estado inicial vacío
                vista.cambiarVistaDerecha("NO_CATEGORY");
                vista.setTitle("No Category Filters");
            }
        });
    }

    /**
     * Hace visible la ventana de filtros.
     */
    public void mostrarVentana() {
        vista.setVisible(true);
    }
}