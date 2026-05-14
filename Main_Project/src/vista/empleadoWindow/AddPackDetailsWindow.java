package vista.empleadoWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class AddPackDetailsWindow extends JDialog {
    private static final long serialVersionUID = 1L;

    private JButton btnCancel;
    private JButton btnConfirm;
    private JButton btnChangePhoto;
    private JLabel lblPhoto;
    
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtDescription;
    private JTextField txtStock;
    
    private File selectedPhotoFile = null;

    public AddPackDetailsWindow(JFrame parent) {
        super(parent, "Pack Details", true);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        initLayout();
    }

    private void initLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(54, 119, 189), 3));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(54, 119, 189));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("Enter Pack Details");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Body
        JPanel bodyContent = new JPanel(new BorderLayout(20, 20));
        bodyContent.setOpaque(false);
        bodyContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Area Izquierda: Foto
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(250, 0));
        
        JPanel photoPlaceholder = new JPanel(new BorderLayout());
        photoPlaceholder.setBackground(Color.WHITE);
        photoPlaceholder.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        
        lblPhoto = new JLabel("<html><center>X<br>ADD PHOTO</center></html>", SwingConstants.CENTER);
        lblPhoto.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblPhoto.setForeground(Color.LIGHT_GRAY);
        photoPlaceholder.add(lblPhoto, BorderLayout.CENTER);

        leftPanel.add(photoPlaceholder, BorderLayout.CENTER);
        
        btnChangePhoto = new JButton("ADD / CHANGE PHOTO");
        btnChangePhoto.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnChangePhoto.setBackground(new Color(54, 119, 189));
        btnChangePhoto.setForeground(Color.WHITE);
        btnChangePhoto.setFocusPainted(false);
        btnChangePhoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChangePhoto.addActionListener(e -> seleccionarFoto());
        leftPanel.add(btnChangePhoto, BorderLayout.SOUTH);

        bodyContent.add(leftPanel, BorderLayout.WEST);

        // Area Derecha: Formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        txtName = new JTextField();
        txtPrice = new JTextField();
        txtDescription = new JTextField();
        txtStock = new JTextField();

        formPanel.add(createFormRow("NAME:", txtName));
        formPanel.add(createFormRow("DESCRIPTION:", txtDescription));
        formPanel.add(createFormRow("PRICE (€):", txtPrice));
        formPanel.add(createFormRow("STOCK:", txtStock));

        bodyContent.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(bodyContent, BorderLayout.CENTER);

        // Bottom Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(new Color(230, 235, 240));
        
        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(178, 34, 34));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(e -> dispose());
        
        btnConfirm = new JButton("Confirm");
        btnConfirm.setBackground(new Color(46, 204, 113));
        btnConfirm.setForeground(Color.WHITE);

        bottomPanel.add(btnCancel);
        bottomPanel.add(btnConfirm);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createFormRow(String labelText, JTextField component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setPreferredSize(new Dimension(130, 30));
        row.add(lbl, BorderLayout.WEST);

        component.setFont(new Font("SansSerif", Font.PLAIN, 14));
        component.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 1),
                new EmptyBorder(0, 5, 0, 5)));
        
        row.add(component, BorderLayout.CENTER);
        return row;
    }
    
    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images (jpg, png, gif)", "jpg", "png", "gif", "jpeg"));
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            selectedPhotoFile = fileChooser.getSelectedFile();
            try {
                ImageIcon iconoOriginal = new ImageIcon(selectedPhotoFile.getAbsolutePath()); 
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(250, 300, Image.SCALE_SMOOTH);
                lblPhoto.setIcon(new ImageIcon(imgEscalada));
                lblPhoto.setText("");
            } catch (Exception ex) {
                lblPhoto.setIcon(null);
                lblPhoto.setText("<html><center>X<br>ERROR</center></html>");
            }
        }
    }

    public JButton getBtnConfirm() { return btnConfirm; }
    public String getNewName() { return txtName.getText(); }
    public String getNewPrice() { return txtPrice.getText(); }
    public String getNewDescription() { return txtDescription.getText(); }
    public String getNewStock() { return txtStock.getText(); }
    public File getSelectedPhotoFile() { return selectedPhotoFile; }
}