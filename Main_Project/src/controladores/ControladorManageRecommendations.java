package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.ConfiguracionRecomendacion;
import vista.GestorPanel.ManageRecommendationsPanel;
import vista.main.MainFrame;

public class ControladorManageRecommendations implements ActionListener {

    private ManageRecommendationsPanel vista;
    private MainFrame mainFrame;
    private MainController mainController;
    private ConfiguracionRecomendacion configRec;

    // We maintain the logical order of criteria:
    // "Interests of User", "Puntuations", "New Products"
    private List<String> currentOrder;

    public ControladorManageRecommendations(ManageRecommendationsPanel vista, MainFrame mainFrame, MainController mainController) {
        this.vista = vista;
        this.mainFrame = mainFrame;
        this.mainController = mainController;
        this.configRec = Aplicacion.getInstancia().getConfiguracionRecomendacion();
        
        // Logical default order, could be extracted from configRec if it exposed getters for importance
        this.currentOrder = new ArrayList<>();
        this.currentOrder.add("Interests of User");
        this.currentOrder.add("Puntuations");
        this.currentOrder.add("New Products");

        // Set initial number of recommendations to whatever the model has (not exposed? defaults to 5 in model, 3 in view)
        // We'll trust the view's current selectedNumber or just let it start at 3.
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

        // Add reorder logic for the rows. Each row has a button to swap with the one above it, or cycle.
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
        // Since ManageRecommendationsPanel doesn't expose a method to update texts yet, we might need to update the panel.
        // Wait, the panel has the labels hardcoded in an array in initLayout.
        // Let's modify ManageRecommendationsPanel to allow setting texts.
        vista.updateRowTexts(currentOrder);
        
        // Update Model
        // Highest importance = 3, lowest = 1
        int impInteres = 3 - currentOrder.indexOf("Interests of User");
        int impResena = 3 - currentOrder.indexOf("Puntuations");
        int impNovedad = 3 - currentOrder.indexOf("New Products");
        
        configRec.configurarImportancia(impInteres, impResena, impNovedad);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}