package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JOptionPane;

import vista.GestorPanel.ManageTimePanel;
import vista.main.MainFrame;
import modelo.tiempo.TiempoSimulado;

// TODO: Auto-generated Javadoc
/**
 * Controlador para ajustar el tiempo simulado de la aplicación.
 *
 * Permite cambiar días/meses, velocidad del tiempo y avanzar días manualmente.
 */
public class ControladorManageTime implements ActionListener, ChangeListener {
    
    /** The vista. */
    private ManageTimePanel vista;
    
    /** The main frame. */
    private MainFrame mainFrame;
    
    /** The main controller. */
    private MainController mainController;

    /**
     * Crea el controlador y inicializa los componentes con los valores actuales
     * del tiempo simulado.
     *
     * @param vista panel de gestión de tiempo
     * @param mainFrame ventana principal
     * @param mainController controlador principal para refrescar la fecha global
     */
    public ControladorManageTime(ManageTimePanel vista, MainFrame mainFrame, MainController mainController) {
        this.vista = vista;
        this.mainFrame = mainFrame;
        this.mainController = mainController;

        this.vista.setControlador(this, this);
        this.vista.getHeaderPanel().addHomeListener(e -> mainController.mostrarMenuPrincipal());
        
        // Inicializar los componentes con los valores actuales
        TiempoSimulado ts = TiempoSimulado.getInstance();
        vista.getSpinDaysPerMonth().setValue(ts.getDiasPorMes());
        vista.getSpinMonthsPerYear().setValue(ts.getMesesPorAño());
        vista.getSliderSpeed().setValue((int)(ts.getVelocidad() * 10));
        vista.getLblSpeedValue().setText(String.format("%.1fx", ts.getVelocidad()));
    }

    /**
     * Maneja acciones del panel: actualizar calendario, cambiar velocidad o
     * avanzar tiempo.
     *
     * @param e evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        TiempoSimulado ts = TiempoSimulado.getInstance();
        
        try {
            if ("UPDATE_CALENDAR".equals(cmd)) {
                int days = (Integer) vista.getSpinDaysPerMonth().getValue();
                int months = (Integer) vista.getSpinMonthsPerYear().getValue();
                ts.iniciar(ts.getVelocidad(), days, months);
                JOptionPane.showMessageDialog(mainFrame, "Calendar updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                mainController.refreshDateGlobal();
            } else if ("UPDATE_SPEED".equals(cmd)) {
                double speed = vista.getSliderSpeed().getValue() / 10.0;
                ts.setVelocidad(speed);
                JOptionPane.showMessageDialog(mainFrame, "Time speed updated to " + speed + "x.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if ("ADVANCE_TIME".equals(cmd)) {
                int daysToAdvance = (Integer) vista.getSpinAdvanceDays().getValue();
                ts.avanzarDias(daysToAdvance);
                JOptionPane.showMessageDialog(mainFrame, "Time advanced by " + daysToAdvance + " days.", "Success", JOptionPane.INFORMATION_MESSAGE);
                mainController.refreshDateGlobal(); 
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error updating time settings: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * State changed.
     *
     * @param e the e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        double val = vista.getSliderSpeed().getValue() / 10.0;
        vista.getLblSpeedValue().setText(String.format("%.1fx", val));
    }
}