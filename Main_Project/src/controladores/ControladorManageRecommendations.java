package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.ConfiguracionRecomendacion;
import vista.GestorPanel.ManageRecommendationsPanel;
import vista.main.MainFrame;

/**
 * Controlador para ajustar la configuración de recomendaciones.
 *
 * Permite cambiar el número de recomendaciones y reordenar los criterios
 * (intereses, puntuaciones, novedades). Actualiza tanto la vista como el
 * modelo de configuración.
 */
public class ControladorManageRecommendations implements ActionListener {

    private ManageRecommendationsPanel vista;
    private ConfiguracionRecomendacion configRec;

    // Mantiene el orden lógico de criterios:
    // "Interests of User", "Puntuations", "New Products"
    private List<String> currentOrder;

    /**
     * Controlador para la gestión de recomendaciones. Inicializa el orden de criterios
     * @param vista
     * @param mainFrame
     * @param mainController
     */
    public ControladorManageRecommendations(ManageRecommendationsPanel vista, MainFrame mainFrame, MainController mainController) {
        this.vista = vista;
        this.configRec = Aplicacion.getInstancia().getConfiguracionRecomendacion();
        
        // Orden por defecto de criterios
        this.currentOrder = new ArrayList<>();
        this.currentOrder.add("Interests of User");
        this.currentOrder.add("Puntuations");
        this.currentOrder.add("New Products");

        // Establece el número inicial de recomendaciones usando lo seleccionado en la vista
        int initialNum = vista.getSelectedNumber();
        configRec.configurarUnidades(initialNum);

        registrarListeners();
    }

    private void registrarListeners() {
        vista.addLeftArrowListener(e -> {
            int num = vista.getSelectedNumber();
            if (num > 1) {
                vista.setSelectedNumber(num - 1);
                configRec.configurarUnidades(num - 1);
            }
        });

        vista.addRightArrowListener(e -> {
            int num = vista.getSelectedNumber();
            if (num < 7) {
                vista.setSelectedNumber(num + 1);
                configRec.configurarUnidades(num + 1);
            }
        });

        // Añade la lógica para reordenar las filas. Cada botón intercambia la fila
        // con la anterior (si es la primera, rota con la última).
        List<JButton> reorderBtns = vista.getReorderButtons();
        for (int i = 0; i < reorderBtns.size(); i++) {
            final int index = i;
            reorderBtns.get(i).addActionListener(e -> {
                // Swap with the previous element (if it's the first, swap with the last)
                if (index > 0) {
                    Collections.swap(currentOrder, index, index - 1);
                } else {
                    Collections.swap(currentOrder, 0, currentOrder.size() - 1);
                }
                actualizarVistaYModelo();
            });
        }
    }
    
    private void actualizarVistaYModelo() {
        // Actualiza la vista con los textos en el nuevo orden
        vista.updateRowTexts(currentOrder);

        // Actualiza el modelo con la importancia relativa (3 = más importante)
        int impInteres = 3 - currentOrder.indexOf("Interests of User");
        int impResena = 3 - currentOrder.indexOf("Puntuations");
        int impNovedad = 3 - currentOrder.indexOf("New Products");

        configRec.configurarImportancia(impInteres, impResena, impNovedad);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}