package vista.userPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CrearUsuarioPanel extends JPanel {

    private JLabel titleLabel;
    private JPanel panelInferior;
    private JPanel panelIntermedio;

    public CrearUsuarioPanel() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        titleLabel = new JLabel("Crear usuario");
        panelInferior = new PanelInferior();
        panelIntermedio = new PanelIntermedio();
    }

    private void initLayout() {

        setLayout(new BorderLayout());

        add(titleLabel, BorderLayout.NORTH);
        add(panelIntermedio, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }


//    public String getUsername() {
//        return usernameField.getText().trim();
//    }
//
//	public String getDni() {
//        return dniField.getText().trim();
//    }
//
//    public String getPassword() {
//        return passwordField.getText().trim();
//    }
//
//    public String getConfirmedPassword() {
//        return confirmPasswordField.getText().trim();
//    }
//
//    public void addAddUserListener(ActionListener listener) {
//        createButton.addActionListener(listener);
//    }


    public void reset() {
    }
    
    private class PanelIntermedio extends JPanel{
    	JPanel fieldsPanel;
    	JPanel requirementsPanel;
    	
    	public PanelIntermedio() {
    		this.initComponents();
    		this.initLayout();
    	}
    	
    	void initComponents(){
    		fieldsPanel = new FieldsPanel();
    		requirementsPanel = new RequirementsPanel();
    	}
		void initLayout(){
			this.setLayout(new BorderLayout());
			add(fieldsPanel, BorderLayout.EAST);
			add(requirementsPanel, BorderLayout.WEST);
    	}
    }
    
    private class FieldsPanel extends JPanel{
		private JTextField usernameField;
		private JTextField dniField;
		private JTextField passwordField;
		private JTextField confirmPasswordField;
		
		public FieldsPanel() {
			initComponents();
			initLayout();
		}

		void initComponents() {
			usernameField = new JTextField(25);
			dniField = new JTextField(25);
			passwordField = new JTextField(25);
			confirmPasswordField = new JTextField(25);
		}
		
		void initLayout() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(usernameField);
			add(dniField);
			add(passwordField);
			add(confirmPasswordField);
		}
		void reset() {
			usernameField.setText("");
			dniField.setText("");
			passwordField.setText("");
			confirmPasswordField.setText("");
		}
    	
    }
	private class RequirementsPanel extends JPanel{
    	
    }
	
	private class PanelInferior extends JPanel{
		private JButton botonCrear;
		
		public PanelInferior() {
			initComponents();
			initLayout();
		}
		
		void initComponents() {
			botonCrear = new JButton();
			botonCrear.setText("Crear");
		}
		
		void initLayout() {
			this.add(botonCrear);
		}
	}
}