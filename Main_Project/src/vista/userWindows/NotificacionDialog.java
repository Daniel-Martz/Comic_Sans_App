package vista.userWindows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import modelo.notificacion.Notificacion;

/**
 * Diálogo modal que muestra el contenido de una notificación.
 */
public class NotificacionDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public NotificacionDialog(Window owner, Notificacion n) {
        super(owner, "Notificación", ModalityType.APPLICATION_MODAL);
        init(n);
    }

    private void init(Notificacion n) {
        setLayout(new BorderLayout(8, 8));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText((n != null) ? n.toString() : "");

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(480, 240));
        add(scroll, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton ok = new JButton("Aceptar");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        botones.add(ok);
        add(botones, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }
}
