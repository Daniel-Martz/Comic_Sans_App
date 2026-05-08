package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.Gestor;
import modelo.usuario.Usuario;
import vista.main.MainFrame;
import vista.empleadoPanel.ManageStatisticsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ControladorManageStatistics implements ActionListener {

    private final ManageStatisticsPanel panel;
    private final MainFrame mainFrame;
    private final MainController mainController;

    public ControladorManageStatistics(ManageStatisticsPanel panel, MainFrame mainFrame, MainController mainController) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.mainController = mainController;

        this.panel.setControlador(this);
        this.panel.getHeaderPanel().addHomeListener(e -> mainController.mostrarMenuPrincipal());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd != null) {
            generarReporte(cmd);
        }
    }

    private void generarReporte(String tipo) {
        // 1. Extraer y validar fechas
        DateTimeSimulado inicio = new DateTimeSimulado(
            (Integer) panel.getCbStartYear().getSelectedItem(), (Integer) panel.getCbStartMonth().getSelectedItem(),
            (Integer) panel.getCbStartDay().getSelectedItem(), 0, 0, 0
        );
        DateTimeSimulado fin = new DateTimeSimulado(
            (Integer) panel.getCbEndYear().getSelectedItem(), (Integer) panel.getCbEndMonth().getSelectedItem(),
            (Integer) panel.getCbEndDay().getSelectedItem(), 23, 59, 59
        );

        if (inicio.dateTimeEnSegundos() > fin.dateTimeEnSegundos()) {
            JOptionPane.showMessageDialog(mainFrame, "Start date cannot be after End date.", "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario u = Aplicacion.getInstancia().getUsuarioActual();
        if (!(u instanceof Gestor)) {
            JOptionPane.showMessageDialog(mainFrame, "Only the Manager can generate statistics.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Gestor gestor = (Gestor) u;

        // 2. Pedir al usuario dónde quiere guardar el archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save " + tipo + " Report As...");
        fileChooser.setSelectedFile(new File("report_" + tipo.toLowerCase() + ".txt"));
        
        if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                if (tipo.equals("MONTHLY")) { gestor.estadisticasRecaudacion(inicio, fin, true, fileToSave); }
                else if (tipo.equals("AMBIT")) { gestor.estadisticasRecaudacion(inicio, fin, false, fileToSave); }
                else if (tipo.equals("PRODUCT")) { gestor.estadisticasVentasProductos(inicio, fin, false, fileToSave); }
                else if (tipo.equals("CLIENT")) { gestor.estadisticasGastoClientes(inicio, fin, fileToSave); }
                JOptionPane.showMessageDialog(mainFrame, "Report successfully generated at:\n" + fileToSave.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) { JOptionPane.showMessageDialog(mainFrame, "Error saving the file: " + ex.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE); }
        }
    }
}