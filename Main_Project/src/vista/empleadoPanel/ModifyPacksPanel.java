package vista.empleadoPanel;

import modelo.producto.LineaProductoVenta;
import vista.userPanels.HeaderPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class ModifyPacksPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color BG_COLOR = new Color(162, 187, 210);      
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 

    private HeaderPanel headerPanel;
    private JPanel gridPacks;
    private JButton btnBack;
    private JTextField txtSearch;
    private List<JButton> modifyButtons = new ArrayList<>();

    public ModifyPacksPanel() {
        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuEmpleado();
        btnBack = headerPanel.addSecondaryTopButton("BACK");
        add(headerPanel, BorderLayout.NORTH);

        JPanel bodyContent = new JPanel(new BorderLayout(0, 20));
        bodyContent.setBackground(BG_COLOR);
        bodyContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Banner and Search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("MODIFY PACKS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(BANNER_MAIN_COLOR);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBox.setOpaque(false);
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton btnSearchIcon = new JButton("Search");
        btnSearchIcon.setActionCommand("SEARCH");
        searchBox.add(txtSearch);
        searchBox.add(btnSearchIcon);
        topPanel.add(searchBox, BorderLayout.SOUTH);

        bodyContent.add(topPanel, BorderLayout.NORTH);

        // Grid
        gridPacks = new JPanel(new GridLayout(0, 4, 15, 15));
        gridPacks.setBackground(BG_COLOR);
        gridPacks.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(BG_COLOR);
        gridWrapper.add(gridPacks, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(gridWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        bodyContent.add(scroll, BorderLayout.CENTER);

        add(bodyContent, BorderLayout.CENTER);
    }

    public void actualizarPacks(List<LineaProductoVenta> packs, ActionListener modifyListener) {
        gridPacks.removeAll();
        modifyButtons.clear();

        if (packs == null || packs.isEmpty()) {
            JLabel vacio = new JLabel("No packs found.", SwingConstants.CENTER);
            vacio.setFont(new Font("SansSerif", Font.BOLD, 16));
            gridPacks.add(vacio);
        } else {
            for (LineaProductoVenta p : packs) {
                gridPacks.add(createCard(p, modifyListener));
            }
        }
        gridPacks.revalidate();
        gridPacks.repaint();
    }

    private JPanel createCard(LineaProductoVenta p, ActionListener modifyListener) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        card.setPreferredSize(new Dimension(200, 250));
        
        JLabel lblName = new JLabel("<html><center>" + p.getNombre() + "</center></html>", SwingConstants.CENTER);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblName.setBorder(new EmptyBorder(5, 5, 5, 5));
        card.add(lblName, BorderLayout.NORTH);

        JLabel imgPlaceholder = new JLabel();
        imgPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        imgPlaceholder.setBackground(Color.WHITE);
        imgPlaceholder.setOpaque(true);
        try {
            if (p.getFoto() != null && p.getFoto().exists()) {
                ImageIcon iconoOriginal = new ImageIcon(p.getFoto().getAbsolutePath()); 
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imgPlaceholder.setIcon(new ImageIcon(imgEscalada));
            } else {
                imgPlaceholder.setText("NO IMAGE");
            }
        } catch (Exception e) {
            imgPlaceholder.setText("NO IMAGE");
        }
        card.add(imgPlaceholder, BorderLayout.CENTER);

        JButton btnModify = new JButton("Modify");
        btnModify.setBackground(new Color(74, 144, 226));
        btnModify.setForeground(Color.WHITE);
        btnModify.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnModify.setFocusPainted(false);
        btnModify.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnModify.setActionCommand("MODIFY_" + p.getID());
        if (modifyListener != null) {
            btnModify.addActionListener(modifyListener);
            modifyButtons.add(btnModify);
        }
        
        card.add(btnModify, BorderLayout.SOUTH);

        return card;
    }

    public HeaderPanel getHeaderPanel() { return headerPanel; }
    public JButton getBtnBack() { return btnBack; }
    public String getSearchText() { return txtSearch.getText().trim(); }
    public void addSearchListener(ActionListener l) {
        for (Component c : ((JPanel)((JPanel)getComponent(1)).getComponent(0)).getComponents()) {
            if (c instanceof JPanel) {
                for (Component inner : ((JPanel)c).getComponents()) {
                    if (inner instanceof JButton) {
                        ((JButton)inner).addActionListener(l);
                    }
                }
            }
        }
        txtSearch.addActionListener(e -> l.actionPerformed(new java.awt.event.ActionEvent(this, 0, "SEARCH")));
    }
}