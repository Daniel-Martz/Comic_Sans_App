package controladores;

import vista.userWindows.*;
import java.awt.event.ItemEvent;

/**
 * Controlador específico para la ventana de Filtros.
 * Gestiona la lógica de cambiar los paneles dinámicamente usando comportamiento "Toggle".
 */
public class ControladorFiltros {

    private FiltrosDialog vista;

    public ControladorFiltros(FiltrosDialog vista) {
        this.vista = vista;
        inicializarListeners();
    }

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
     * Muestra la ventana modal.
     */
    public void mostrarVentana() {
        // Por defecto arranca sin categoría seleccionada
        vista.cambiarVistaDerecha("NO_CATEGORY");
        vista.setTitle("No Category Filters");
        vista.setVisible(true);
    }
}