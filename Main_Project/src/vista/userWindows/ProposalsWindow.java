package vista.userWindows;

import javax.swing.*;

import controladores.ControladorProposals;
import modelo.aplicacion.Aplicacion;
import modelo.producto.EstadoConservacion;
import modelo.producto.ProductoSegundaMano;
import modelo.solicitud.Oferta;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.ClienteRegistrado;
import vista.userPanels.ProposalsPanel;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Ventana que muestra el panel de propuestas de intercambio.
 * Es un JDialog para que esté a parte respecto al MainFrame.
 * Se abre desde el menú principal al pulsar el botón de intercambios y seleccionar el botón proposals.
 */
public class ProposalsWindow extends JDialog {

    private static final long serialVersionUID = 1L;

    private ProposalsPanel proposalsPanel;

    /**
     * @param parent el JFrame padre (MainFrame)
     */
    public ProposalsWindow(JFrame parent) {
        super(parent, "Interchange Proposals", true);
        initComponents();
        initLayout();
        pack();
        setSize(1100, 750);
        setLocationRelativeTo(parent);
        setResizable(true);
    }

    private void initComponents() {
        proposalsPanel = new ProposalsPanel();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        add(proposalsPanel, BorderLayout.CENTER);
    }

    /**
     * Devuelve el panel interno para que el controlador pueda
     * registrar listeners y cargar datos.
     */
    public ProposalsPanel getProposalsPanel() {
        return proposalsPanel;
    }

    /**
     * Abre la ventana. Llamado por el MainController.
     */
    public void mostrar() {
        setVisible(true);
    }

    /**
     * Cierra la ventana.
     */
    public void cerrar() {
        dispose();
    }
    
    /**
     * Muestra la ventana de éxito personalizada.
     * @param mensaje Descripción del éxito.
     */
    public void mostrarVentanaExito(String mensaje) {
        VentanaExito exito = new VentanaExito(
            this, 
            "Operación Exitosa", 
            "¡ACCIÓN COMPLETADA!", 
            mensaje
        );
        exito.mostrar();
    }

    /**
     * Muestra la ventana de error personalizada.
     * @param motivo Descripción del error.
     */
    public void mostrarVentanaError(String motivo) {
        VentanaError error = new VentanaError(
            this, 
            "Error en la operación", 
            "HA OCURRIDO UN PROBLEMA", 
            motivo
        );
        error.mostrar();
    }

 // ── Main para testear con el MODELO REAL
    public static void main(String[] args) {
        // 1. Inicializar la aplicación
        Aplicacion app = Aplicacion.getInstancia();

        // 2. Crear usuarios de prueba
        ClienteRegistrado yo = new ClienteRegistrado("demoUser", "123456789A", "Passw0rd!");
        ClienteRegistrado bob = new ClienteRegistrado("BobElIntercambiador", "987654321B", "Passw0rd!");
        ClienteRegistrado alice = new ClienteRegistrado("Alice_Geek", "111222333C", "Passw0rd!");
        ClienteRegistrado charlie = new ClienteRegistrado("Charlie_Coleccionista", "444555666D", "Passw0rd!");
        
        // Simular que estamos logueados como "demoUser"
        app.setUsuarioActual(yo);

        // -------------------------------------------------------------------------
        // 3. OFERTAS RECIBIDAS (INCOME): Otros me ofrecen cosas a mí
        // -------------------------------------------------------------------------
        
        // Oferta 1: Bob me ofrece su Comic a cambio de mi Juego (Tu oferta original)
        ProductoSegundaMano comicBob = new ProductoSegundaMano("Spider-Man Vol.1", "Primera edición",  null, bob);
        comicBob.getSolicitudValidacion().validarProducto(5.0, 150.0, EstadoConservacion.USO_LIGERO);
        
        ProductoSegundaMano juegoMio = new ProductoSegundaMano("Catan", "Casi nuevo", null, yo);
        juegoMio.getSolicitudValidacion().validarProducto(5.0, 45.0, EstadoConservacion.USO_LIGERO);

        Set<ProductoSegundaMano> of1_daBob = new HashSet<>(Arrays.asList(comicBob));
        Set<ProductoSegundaMano> of1_pideBob = new HashSet<>(Arrays.asList(juegoMio));
        // Ojo al orden: (fecha, ofertante, destinatario, ofrecedos, solicitados)
        Oferta ofertaBob = new Oferta(new DateTimeSimulado(), bob, yo, of1_daBob, of1_pideBob);
        yo.getOfertasRecibidas().add(ofertaBob); 

        // Generar 6 ofertas más de Alice hacia mí usando un bucle
        for (int i = 1; i <= 6; i++) {
            ProductoSegundaMano pDaAlice = new ProductoSegundaMano("Figura de Anime #" + i, "Buen estado", null, alice);
            pDaAlice.getSolicitudValidacion().validarProducto(2.0, 15.0 + (i * 2), EstadoConservacion.USO_LIGERO);

            ProductoSegundaMano pPideAlice = new ProductoSegundaMano("Mi carta Magic #" + i, "Con funda", null, yo);
            pPideAlice.getSolicitudValidacion().validarProducto(1.0, 12.0 + (i * 2.5), EstadoConservacion.PERFECTO);

            Oferta off = new Oferta(new DateTimeSimulado(), alice, yo, 
                                    new HashSet<>(Arrays.asList(pDaAlice)), 
                                    new HashSet<>(Arrays.asList(pPideAlice)));
            yo.getOfertasRecibidas().add(off);
        }

        // -------------------------------------------------------------------------
        // 4. OFERTAS ENVIADAS (SENT): Yo ofrezco cosas a los demás
        // -------------------------------------------------------------------------
        
        // Oferta enviada 1: Yo ofrezco mi Cómic a Bob a cambio de su Figura (Tu oferta original)
        ProductoSegundaMano comicMio = new ProductoSegundaMano("Batman: Año Uno", "Tapa dura",  null, yo);
        comicMio.getSolicitudValidacion().validarProducto(5.0, 20.0, EstadoConservacion.USO_LIGERO);
        
        ProductoSegundaMano figuraBob = new ProductoSegundaMano("Figura Iron Man", "Sin caja", null, bob);
        figuraBob.getSolicitudValidacion().validarProducto(5.0, 30.0, EstadoConservacion.USO_LIGERO);

        Set<ProductoSegundaMano> of2_doyYo = new HashSet<>(Arrays.asList(comicMio));
        Set<ProductoSegundaMano> of2_pidoYo = new HashSet<>(Arrays.asList(figuraBob));
        Oferta ofertaMia = new Oferta(new DateTimeSimulado(), yo, bob, of2_doyYo, of2_pidoYo);
        yo.getOfertasRealizadas().add(ofertaMia);

        // Generar 4 ofertas más enviadas a Charlie usando un bucle
        for (int i = 1; i <= 4; i++) {
            ProductoSegundaMano pDoyYo = new ProductoSegundaMano("Mi videojuego #" + i, "Completo", null, yo);
            pDoyYo.getSolicitudValidacion().validarProducto(5.0, 40.0, EstadoConservacion.PERFECTO);

            ProductoSegundaMano pPidoCharlie = new ProductoSegundaMano("Juego de Rol de Charlie #" + i, "Nuevo", null, charlie);
            pPidoCharlie.getSolicitudValidacion().validarProducto(5.0, 35.0 + i, EstadoConservacion.PERFECTO);

            Oferta off = new Oferta(new DateTimeSimulado(), yo, charlie, 
                                    new HashSet<>(Arrays.asList(pDoyYo)), 
                                    new HashSet<>(Arrays.asList(pPidoCharlie)));
            yo.getOfertasRealizadas().add(off);
        }

        // -------------------------------------------------------------------------
        // 5. Lanzar la Interfaz
        // -------------------------------------------------------------------------
        SwingUtilities.invokeLater(() -> {
            // Un frame padre invisible solo para que el JDialog modal tenga de donde anclarse en el test
            JFrame dummyParent = new JFrame();
            dummyParent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Le damos un tamaño nulo para que no se vea una ventanita fea de fondo
            dummyParent.setSize(0, 0); 
            dummyParent.setVisible(true); // Oculto visualmente pero activo
            
            // Instanciar la vista
            ProposalsWindow window = new ProposalsWindow(dummyParent);
            
            // Instanciar el controlador (pasamos null al MainController ya que es un test aislado)
            new ControladorProposals(window, null);
            
            // Mostrar la ventana
            window.mostrar();
            
            // Cuando cerremos la ventana de propuestas, matamos el programa
            System.exit(0); 
        });
    }
    
}