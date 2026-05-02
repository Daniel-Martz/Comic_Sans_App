package vista.empleadoPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana mínima para la funcionalidad "Load From File".
 *
 * Muestra únicamente un botón "Select File" centrado. El controlador se
 * suscribe a ese botón y realiza la lógica de selección y carga del fichero
 * (modelo). De esta forma mantenemos la separación vista-controlador.
 */
public class LoadFromFileWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JButton btnSelectFile;

    public LoadFromFileWindow(Frame owner) {
        super("Load From File");
        btnSelectFile = new JButton("Select File");
        init(owner);
    }

    private void init(Frame owner) {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(new Color(162, 187, 210));

        btnSelectFile.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnSelectFile.setBackground(new Color(74, 144, 226));
        btnSelectFile.setForeground(Color.WHITE);
        btnSelectFile.setFocusPainted(false);
        btnSelectFile.setPreferredSize(new Dimension(220, 48));

        content.add(btnSelectFile, new GridBagConstraints());

        setContentPane(content);
        pack();
        setSize(360, 160);
        setResizable(false);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public JButton getBtnSelectFile() {
        return btnSelectFile;
    }
}
