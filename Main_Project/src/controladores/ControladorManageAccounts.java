package controladores;

import modelo.aplicacion.Aplicacion;

import modelo.usuario.Empleado;
import modelo.usuario.Gestor;
import modelo.usuario.Permiso;
import modelo.usuario.Usuario;
import vista.GestorPanel.*;
import vista.GestorWindow.ManageEmployeeWindow;
import vista.GestorWindow.ModifyPermissionsWindow;
import vista.GestorWindow.NewEmployeeWindow;
import vista.main.MainFrame;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador para la gestión de cuentas de empleados.
 *
 * Permite buscar, crear, modificar y eliminar empleados desde la interfaz de
 * gestión. Abre ventanas auxiliares (crear, modificar permisos) según convenga.
 */
public class ControladorManageAccounts {

    private ManageAccountsPanel manageAccountsPanel;
    private MainFrame mainFrame;

    /**
     * Inicializa el controlador y configura listeners sobre el panel.
     *
     * @param manageAccountsPanel panel de gestión de cuentas
     * @param mainFrame ventana principal
     */
    public ControladorManageAccounts(ManageAccountsPanel manageAccountsPanel, MainFrame mainFrame) {
        this.manageAccountsPanel = manageAccountsPanel;
        this.mainFrame = mainFrame;

        initListeners();
        // Ajustar visibilidad inicial del botón '+' siguiendo MVC (solo visible para Gestor)
        updateCreateButtonVisibility();
    }

    /**
     * Ajusta la visibilidad del botón de crear empleado según el usuario
     * actualmente logueado (solo Gestor puede crear empleados).
     */
    private void updateCreateButtonVisibility() {
        Usuario current = Aplicacion.getInstancia().getUsuarioActual();
        boolean isGestor = current instanceof Gestor;
        manageAccountsPanel.setCreateButtonVisible(isGestor);
    }

    /**
     * Inicializa los listeners que responden a acciones del panel (volver al
     * menú, búsqueda, gestionar usuario, crear usuario).
     */
    private void initListeners() {
        manageAccountsPanel.addHomeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volver al menú principal del gestor
                mainFrame.mostrarPanel(MainFrame.PANEL_MENU_GESTOR);
            }
        });

        manageAccountsPanel.addSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = manageAccountsPanel.getSearchText();
                cargarCuentas(query);
            }
        });

        manageAccountsPanel.addManageUserListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dni = e.getActionCommand();
                abrirManageUserWindow(dni);
            }
        });

        manageAccountsPanel.addCreateUserListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Usuario current = Aplicacion.getInstancia().getUsuarioActual();
                if (!(current instanceof Gestor)) {
                    JOptionPane.showMessageDialog(mainFrame, "Only the Gestor can create employees.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Gestor gestor = (Gestor) current;
                // ventana para crear nuevo empleado (abierta desde el Controlador siguiendo MVC)
                NewEmployeeWindow newEmpWindow = new NewEmployeeWindow(mainFrame);
                newEmpWindow.addAcceptListener(ev -> {
                    String name = newEmpWindow.getEmployeeName();
                    String dni = newEmpWindow.getEmployeeDni();
                    if (name.isEmpty() || dni.isEmpty()) {
                        JOptionPane.showMessageDialog(newEmpWindow, "Name and DNI cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        Empleado newEmp = gestor.crearEmpleado(name, dni);
                        if (newEmpWindow.hasValidaciones()) gestor.añadirPermiso(newEmp, Permiso.VALIDACIONES);
                        if (newEmpWindow.hasIntercambios()) gestor.añadirPermiso(newEmp, Permiso.INTERCAMBIOS);
                        if (newEmpWindow.hasPedidos()) gestor.añadirPermiso(newEmp, Permiso.PEDIDOS);
                        
                        JOptionPane.showMessageDialog(newEmpWindow, "Employee created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        newEmpWindow.dispose();
                        cargarCuentas(""); // recargar lista
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(newEmpWindow, "Error creating employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                newEmpWindow.setVisible(true);
            }
        });
    }

    /**
     * Abre la ventana de gestión de un empleado concreto (borrar, modificar
     * permisos). Comprueba permisos y evita gestionar la propia cuenta.
     *
     * @param dni DNI del empleado a gestionar
     */
    private void abrirManageUserWindow(String dni) {
        // Buscamos explícitamente entre los empleados registrados para evitar
        // conflictos si hay usuarios de otros tipos con el mismo DNI.
        Empleado userToManage = null;
        for (Empleado e : Aplicacion.getInstancia().getEmpleados()) {
            if (e.getDNI().equals(dni)) {
                userToManage = e;
                break;
            }
        }
        
        if (userToManage == null) {
            JOptionPane.showMessageDialog(mainFrame, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Sólo los empleados están aquí por diseño
        Usuario current = Aplicacion.getInstancia().getUsuarioActual();
        if (current != null && current.getDNI().equals(userToManage.getDNI())) {
            JOptionPane.showMessageDialog(mainFrame, "You cannot manage your own account.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ManageEmployeeWindow manageWindow = new ManageEmployeeWindow(mainFrame, dni);
        final Empleado finalUser = userToManage;

        manageWindow.addDeleteUserListener(e -> {
            // Protección adicional: sólo empleados (no el propio gestor) pueden eliminarse desde aquí
            // finalUser es un Empleado por construcción
            if (current != null && current.getDNI().equals(finalUser.getDNI())) {
                JOptionPane.showMessageDialog(mainFrame, "You cannot delete your own account.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            try {
                Aplicacion.getInstancia().eliminarUsuario(finalUser);
                JOptionPane.showMessageDialog(mainFrame, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                manageWindow.dispose();
                cargarCuentas(""); // recargar lista
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        manageWindow.addModifyPermissionsListener(e -> {
            // finalUser ya es Empleado
            Empleado emp = finalUser;
            boolean hasVal = emp.tienePermiso(Permiso.VALIDACIONES);
            boolean hasInt = emp.tienePermiso(Permiso.INTERCAMBIOS);
            boolean hasPed = emp.tienePermiso(Permiso.PEDIDOS);
            
            ModifyPermissionsWindow permWindow = new ModifyPermissionsWindow(mainFrame, dni, hasVal, hasInt, hasPed);
            
            permWindow.addAcceptListener(ev -> {
                if (permWindow.hasValidaciones()) emp.añadirPermiso(Permiso.VALIDACIONES);
                else emp.eliminarPermiso(Permiso.VALIDACIONES);
                
                if (permWindow.hasIntercambios()) emp.añadirPermiso(Permiso.INTERCAMBIOS);
                else emp.eliminarPermiso(Permiso.INTERCAMBIOS);
                
                if (permWindow.hasPedidos()) emp.añadirPermiso(Permiso.PEDIDOS);
                else emp.eliminarPermiso(Permiso.PEDIDOS);
                                
                JOptionPane.showMessageDialog(mainFrame, "Permissions updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                permWindow.dispose();
                manageWindow.dispose();
            });
            
            permWindow.setVisible(true);
        });

        manageWindow.setVisible(true);
    }

    /**
     * Recarga la lista de cuentas sin filtro.
     */
    public void cargarCuentas() {
        cargarCuentas("");
    }

    /**
     * Carga y filtra la lista de empleados mostrada en el panel.
     *
     * @param query texto de búsqueda (nombre o DNI)
     */
    public void cargarCuentas(String query) {
        manageAccountsPanel.clearAccounts();
        Usuario current = Aplicacion.getInstancia().getUsuarioActual();
        // Aseguramos visibilidad del botón según el usuario actual
        manageAccountsPanel.setCreateButtonVisible(current instanceof Gestor);
        List<Empleado> usuarios = Aplicacion.getInstancia().getEmpleados();
        String lowerQuery = query == null ? "" : query.toLowerCase();
        
        for (Usuario u : usuarios) {
            // Sólo mostramos empleados
            if (!(u instanceof Empleado)) continue;
            // No mostramos al propio usuario actual (gestor no puede gestionarse a si mismo)
            if (current != null && current.getDNI().equals(u.getDNI())) continue;

            String name = u.getNombreUsuario();
            String dni = u.getDNI();

            if (lowerQuery.isEmpty() || 
                (name != null && name.toLowerCase().contains(lowerQuery)) || 
                (dni != null && dni.toLowerCase().contains(lowerQuery))) {
                manageAccountsPanel.addAccountRow(name, dni);
            }
        }
    }
}
