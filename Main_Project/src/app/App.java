package app;
import vista.main.MainFrame;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import modelo.categoria.Categoria;
import modelo.descuento.Precio;
import modelo.producto.Comic;
import modelo.producto.EstadoConservacion;
import modelo.producto.Figura;
import modelo.producto.JuegoDeMesa;
import modelo.producto.LineaProductoVenta;
import modelo.producto.ProductoSegundaMano;
import modelo.producto.TipoJuegoMesa;
import modelo.solicitud.Oferta;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Gestor;

public class App {
    public static void main(String[] args) {

        // ─────────────────────────────────────────────────────────────────
        // 1. INICIALIZACIÓN
        // ─────────────────────────────────────────────────────────────────
        Aplicacion app = Aplicacion.getInstancia();
        Catalogo cat = app.getCatalogo();

        // ─────────────────────────────────────────────────────────────────
        // 2. CATEGORÍAS
        // ─────────────────────────────────────────────────────────────────
        Categoria catComics      = new Categoria("Comics");
        Categoria catFiguras     = new Categoria("Figuras");
        Categoria catJuegos      = new Categoria("Juegos de Mesa");
        Categoria catManga       = new Categoria("Manga");
        Categoria catSuperheroes = new Categoria("Superhéroes");
        Categoria catEstrategia  = new Categoria("Estrategia");
        Categoria catAnime       = new Categoria("Anime");

        cat.añadirCategoria(catComics);
        cat.añadirCategoria(catFiguras);
        cat.añadirCategoria(catJuegos);
        cat.añadirCategoria(catManga);
        cat.añadirCategoria(catSuperheroes);
        cat.añadirCategoria(catEstrategia);
        cat.añadirCategoria(catAnime);

        // ─────────────────────────────────────────────────────────────────
        // 3. PRODUCTOS ─ mínimo 20
        // ─────────────────────────────────────────────────────────────────

        // ── Comics (6) ──────────────────────────────────────────────────
        Comic spidermanAdv = new Comic(
                "Spiderman Adventures Vol.3",
                "Un cómic de auténtico coleccionista preservado en su envoltorio original.",
                new File("images/SpidermanAdventures3.jpg"),
                20, 19.99, 0,
                120, "Stan Lee", "Marvel", 1995);
        spidermanAdv.añadirCategoria(catComics);
        spidermanAdv.añadirCategoria(catSuperheroes);
        cat.añadirProducto(spidermanAdv);

        Comic spidermanChron = new Comic(
                "Spiderman Chronicles Vol.5",
                "Edición limitada de la saga Chronicles con portada alternativa.",
                new File("images/SpidermanChronicles5.jpg"),
                20, 19.99, 0,
                98, "Dan Slott", "Marvel", 2010);
        spidermanChron.añadirCategoria(catComics);
        spidermanChron.añadirCategoria(catSuperheroes);
        cat.añadirProducto(spidermanChron);

        Comic batmanYearOne = new Comic(
                "Batman: Year One",
                "La historia de origen definitiva de Batman narrada por Frank Miller.",
                new File("images/BatmanYearOne.jpg"),
                15, 24.99, 0,
                144, "Frank Miller", "DC Comics", 1987);
        batmanYearOne.añadirCategoria(catComics);
        batmanYearOne.añadirCategoria(catSuperheroes);
        cat.añadirProducto(batmanYearOne);

        Comic watchmen = new Comic(
                "Watchmen",
                "La obra maestra de Alan Moore que cambió el cómic para siempre.",
                new File("images/Watchmen.jpg"),
                10, 29.99, 0,
                416, "Alan Moore", "DC Comics", 1986);
        watchmen.añadirCategoria(catComics);
        cat.añadirProducto(watchmen);

        Comic xmenGold = new Comic(
                "X-Men: Gold Vol.1",
                "La resurrección de los X-Men más clásicos en una aventura épica.",
                new File("images/XmenGold.jpg"),
                12, 17.99, 0,
                136, "Marc Guggenheim", "Marvel", 2017);
        xmenGold.añadirCategoria(catComics);
        xmenGold.añadirCategoria(catSuperheroes);
        cat.añadirProducto(xmenGold);

        Comic dragonBallZ = new Comic(
                "Dragon Ball Z Vol.1",
                "El inicio épico de la saga Z con Raditz llegando a la Tierra.",
                new File("images/DragonBallZ1.jpg"),
                25, 8.99, 0,
                200, "Akira Toriyama", "Shueisha", 1989);
        dragonBallZ.añadirCategoria(catManga);
        dragonBallZ.añadirCategoria(catAnime);
        cat.añadirProducto(dragonBallZ);

        // ── Figuras (7) ──────────────────────────────────────────────────
        Figura ironManFigure = new Figura(
                "Figura Iron Man Mark 85 (30cm)",
                "Figura de Iron Man con armadura Mark 85 de Endgame, escala 1/6 con LEDs.",
                new File("images/IronManMark85.jpg"),
                8, 89.99, 0,
                "Hot Toys", "PVC+ABS", 30.0, 22.0, 18.0);
        ironManFigure.añadirCategoria(catFiguras);
        ironManFigure.añadirCategoria(catSuperheroes);
        cat.añadirProducto(ironManFigure);

        Figura gokuFigure = new Figura(
                "Figura Son Goku Super Saiyan (25cm)",
                "Figura de alta calidad de Goku en estado Super Saiyan con base de energía.",
                new File("images/GokuSSJ.jpg"),
                15, 49.99, 0,
                "Bandai", "PVC", 25.0, 15.0, 12.0);
        gokuFigure.añadirCategoria(catFiguras);
        gokuFigure.añadirCategoria(catAnime);
        cat.añadirProducto(gokuFigure);

        Figura narutoFigure = new Figura(
                "Figura Naruto Uzumaki (20cm)",
                "Naruto en pose de ataque con Rasengan, edición especial con efecto de viento.",
                new File("images/NarutoFigure.jpg"),
                20, 39.99, 0,
                "Megahouse", "PVC", 20.0, 12.0, 10.0);
        narutoFigure.añadirCategoria(catFiguras);
        narutoFigure.añadirCategoria(catAnime);
        cat.añadirProducto(narutoFigure);

        Figura batmanFigure = new Figura(
                "Figura Batman Arkham Knight (35cm)",
                "Figura premium de Batman con la armadura de Arkham Knight, articulada.",
                new File("images/BatmanArkham.jpg"),
                6, 119.99, 0,
                "DC Collectibles", "Resina", 35.0, 18.0, 15.0);
        batmanFigure.añadirCategoria(catFiguras);
        batmanFigure.añadirCategoria(catSuperheroes);
        cat.añadirProducto(batmanFigure);

        Figura lucinaFigure = new Figura(
                "Figura Lucina Fire Emblem (22cm)",
                "Figura oficial de Lucina con su espada Falchion y capa azul.",
                new File("images/LucinaFigure.jpg"),
                10, 64.99, 0,
                "Good Smile", "PVC", 22.0, 10.0, 8.0);
        lucinaFigure.añadirCategoria(catFiguras);
        cat.añadirProducto(lucinaFigure);

        Figura spidermanFigure = new Figura(
                "Figura Spiderman Miles Morales (28cm)",
                "Miles Morales en pleno salto, con telarañas y base diorama de ciudad.",
                new File("images/MilesMorales.jpg"),
                12, 74.99, 0,
                "Sideshow", "PVC+ABS", 28.0, 20.0, 20.0);
        spidermanFigure.añadirCategoria(catFiguras);
        spidermanFigure.añadirCategoria(catSuperheroes);
        cat.añadirProducto(spidermanFigure);

        Figura zeldaFigure = new Figura(
                "Figura Link (The Legend of Zelda, 26cm)",
                "Link con el Maestro Espada y el Escudo Hyliano, edición Breath of the Wild.",
                new File("images/LinkFigure.jpg"),
                9, 84.99, 0,
                "First 4 Figures", "Resina", 26.0, 14.0, 14.0);
        zeldaFigure.añadirCategoria(catFiguras);
        cat.añadirProducto(zeldaFigure);

        // ── Juegos de Mesa (5) ───────────────────────────────────────────
        JuegoDeMesa catan = new JuegoDeMesa(
                "Catan",
                "El clásico juego de estrategia y negociación en una isla de recursos.",
                new File("images/Catan.jpg"),
                18, 39.99,
                4, 10, 99, TipoJuegoMesa.DADOS);
        catan.añadirCategoria(catJuegos);
        catan.añadirCategoria(catEstrategia);
        cat.añadirProducto(catan);

        JuegoDeMesa pandemic = new JuegoDeMesa(
                "Pandemic",
                "Juego cooperativo: salva al mundo de cuatro enfermedades mortales.",
                new File("images/Pandemic.jpg"),
                14, 44.99,
                4, 8, 99, TipoJuegoMesa.CARTAS);
        pandemic.añadirCategoria(catJuegos);
        pandemic.añadirCategoria(catEstrategia);
        cat.añadirProducto(pandemic);

        JuegoDeMesa ticket = new JuegoDeMesa(
                "Ticket to Ride Europa",
                "Conecta ciudades europeas con rutas de tren en este clásico familiar.",
                new File("images/TicketToRide.jpg"),
                16, 49.99,
                5, 8, 99, TipoJuegoMesa.CARTAS);
        ticket.añadirCategoria(catJuegos);
        ticket.añadirCategoria(catEstrategia);
        cat.añadirProducto(ticket);

        JuegoDeMesa codenames = new JuegoDeMesa(
                "Codenames",
                "El juego de palabras y espías más famoso del mundo. Adivina las contraseñas.",
                new File("images/Codenames.jpg"),
                22, 22.99,
                8, 10, 99, TipoJuegoMesa.CARTAS);
        codenames.añadirCategoria(catJuegos);
        cat.añadirProducto(codenames);

        JuegoDeMesa dominion = new JuegoDeMesa(
                "Dominion",
                "El juego de construcción de mazo que definió un género entero.",
                new File("images/Dominion.jpg"),
                10, 34.99,
                4, 13, 99, TipoJuegoMesa.CARTAS);
        dominion.añadirCategoria(catJuegos);
        dominion.añadirCategoria(catEstrategia);
        cat.añadirProducto(dominion);

        // ── Productos genéricos adicionales (5) ─────────────────────────
        LineaProductoVenta artbook = new LineaProductoVenta(
                "Artbook Marvel: 80 Años de Maravillas",
                "Recopilación visual de los mejores artes conceptuales de Marvel a lo largo de 80 años.",
                new File("images/ArtbookMarvel.jpg"),
                7, 59.99);
        artbook.añadirCategoria(catComics);
        artbook.añadirCategoria(catSuperheroes);
        cat.añadirProducto(artbook);

        LineaProductoVenta funkoPop = new LineaProductoVenta(
                "Funko Pop! Goku Black (Exclusive)",
                "Funko Pop exclusivo de Goku Black SSJ Rose, edición especial de coleccionista.",
                new File("images/FunkGokuBlack.jpg"),
                30, 14.99);
        funkoPop.añadirCategoria(catFiguras);
        funkoPop.añadirCategoria(catAnime);
        cat.añadirProducto(funkoPop);

        LineaProductoVenta sleeves = new LineaProductoVenta(
                "Fundas Premium para Cartas (200 uds)",
                "Fundas de PVC transparente de alta calidad, compatibles con Magic, Pokémon y más.",
                new File("images/CardSleeves.jpg"),
                50, 9.99);
        sleeves.añadirCategoria(catJuegos);
        cat.añadirProducto(sleeves);

        LineaProductoVenta dado20 = new LineaProductoVenta(
                "Dado de 20 Caras Metálico (Dragones y Mazmorras)",
                "Dado D20 de zinc chapado en oro con grabados rúnicos, edición de coleccionista.",
                new File("images/D20Metal.jpg"),
                40, 12.99);
        dado20.añadirCategoria(catJuegos);
        dado20.añadirCategoria(catEstrategia);
        cat.añadirProducto(dado20);

        LineaProductoVenta manga1 = new LineaProductoVenta(
                "One Piece Vol.1 - La Leyenda de Luffy",
                "El comienzo de la aventura más larga del manga: Luffy y su búsqueda del tesoro.",
                new File("images/OnePiece1.jpg"),
                35, 7.99);
        manga1.añadirCategoria(catManga);
        manga1.añadirCategoria(catAnime);
        cat.añadirProducto(manga1);

        // ── Descuento de ejemplo (10 % en Watchmen) ─────────────────────
        Precio descWatchmen = new Precio(
                new DateTimeSimulado(),
                DateTimeSimulado.DateTimeDiasDespues(30),
                10);
        cat.añadirDescuento(descWatchmen);
        cat.aplicarDescuento(watchmen, descWatchmen);

        // ─────────────────────────────────────────────────────────────────
        // 4. USUARIOS DE PRUEBA
        // ─────────────────────────────────────────────────────────────────
        ClienteRegistrado yo      = new ClienteRegistrado("demoUser",              "123456789A", "Passw0rd!");
        ClienteRegistrado bob     = new ClienteRegistrado("BobElIntercambiador",   "987654321B", "Passw0rd!");
        ClienteRegistrado alice   = new ClienteRegistrado("Alice_Geek",            "111222333C", "Passw0rd!");
        ClienteRegistrado charlie = new ClienteRegistrado("Charlie_Coleccionista", "444555666D", "Passw0rd!");

        app.setUsuarioActual(yo);

        // ─────────────────────────────────────────────────────────────────
        // 5. OFERTAS RECIBIDAS (INCOME)
        // ─────────────────────────────────────────────────────────────────
        ProductoSegundaMano comicBob = new ProductoSegundaMano(
                "Spider-Man Vol.1", "Primera edición", null, bob);
        comicBob.getSolicitudValidacion().validarProducto(5.0, 150.0, EstadoConservacion.USO_LIGERO);

        ProductoSegundaMano juegoMio = new ProductoSegundaMano(
                "Catan", "Casi nuevo", null, yo);
        juegoMio.getSolicitudValidacion().validarProducto(5.0, 45.0, EstadoConservacion.USO_LIGERO);

        Oferta ofertaBob = new Oferta(
                new DateTimeSimulado(), bob, yo,
                new HashSet<>(Arrays.asList(comicBob)),
                new HashSet<>(Arrays.asList(juegoMio)));
        yo.getOfertasRecibidas().add(ofertaBob);

        for (int i = 1; i <= 6; i++) {
            ProductoSegundaMano pDaAlice = new ProductoSegundaMano(
                    "Figura de Anime #" + i, "Buen estado", null, alice);
            pDaAlice.getSolicitudValidacion().validarProducto(2.0, 15.0 + i * 2, EstadoConservacion.USO_LIGERO);

            ProductoSegundaMano pPideAlice = new ProductoSegundaMano(
                    "Mi carta Magic #" + i, "Con funda", null, yo);
            pPideAlice.getSolicitudValidacion().validarProducto(1.0, 12.0 + i * 2.5, EstadoConservacion.PERFECTO);

            yo.getOfertasRecibidas().add(new Oferta(
                    new DateTimeSimulado(), alice, yo,
                    new HashSet<>(Arrays.asList(pDaAlice)),
                    new HashSet<>(Arrays.asList(pPideAlice))));
        }

        // ─────────────────────────────────────────────────────────────────
        // 6. OFERTAS ENVIADAS (SENT)
        // ─────────────────────────────────────────────────────────────────
        ProductoSegundaMano comicMio = new ProductoSegundaMano(
                "Batman: Año Uno", "Tapa dura", null, yo);
        comicMio.getSolicitudValidacion().validarProducto(5.0, 20.0, EstadoConservacion.USO_LIGERO);

        ProductoSegundaMano figuraBob = new ProductoSegundaMano(
                "Figura Iron Man", "Sin caja", null, bob);
        figuraBob.getSolicitudValidacion().validarProducto(5.0, 30.0, EstadoConservacion.USO_LIGERO);

        yo.getOfertasRealizadas().add(new Oferta(
                new DateTimeSimulado(), yo, bob,
                new HashSet<>(Arrays.asList(comicMio)),
                new HashSet<>(Arrays.asList(figuraBob))));

        for (int i = 1; i <= 4; i++) {
            ProductoSegundaMano pDoyYo = new ProductoSegundaMano(
                    "Mi videojuego #" + i, "Completo", null, yo);
            pDoyYo.getSolicitudValidacion().validarProducto(5.0, 40.0, EstadoConservacion.PERFECTO);

            ProductoSegundaMano pPidoCharlie = new ProductoSegundaMano(
                    "Juego de Rol de Charlie #" + i, "Nuevo", null, charlie);
            pPidoCharlie.getSolicitudValidacion().validarProducto(5.0, 35.0 + i, EstadoConservacion.PERFECTO);

            yo.getOfertasRealizadas().add(new Oferta(
                    new DateTimeSimulado(), yo, charlie,
                    new HashSet<>(Arrays.asList(pDoyYo)),
                    new HashSet<>(Arrays.asList(pPidoCharlie))));
        }

        // ─────────────────────────────────────────────────────────────────
        // 7. ARRANCAR LA UI
        // ─────────────────────────────────────────────────────────────────
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
 