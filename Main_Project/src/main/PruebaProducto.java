package main;

import java.io.File;
import java.util.*;

import aplicacion.*;
import categoria.Categoria;
import notificacion.NotificacionCliente;
import producto.*;
import solicitud.Oferta;
import solicitud.SolicitudIntercambio;
import solicitud.SolicitudValidacion;
import tiempo.DateTimeSimulado;
import usuario.*;

public class PruebaProducto {
  public static void main(String args[]) {
    Catalogo cat = Catalogo.getInstancia();
    Categoria animeCategoria = new Categoria("Anime");
    Categoria estrategiaCategoria = new Categoria("Estrategia");
    cat.añadirCategoria(animeCategoria);
    cat.añadirCategoria(estrategiaCategoria);
    File f = new File("txt/cargarDesdeFichero1.txt");
    try {
      cat.añadirProductosDesdeFichero(f);
    } catch (Exception e) {
      System.out.println("No ha sido posible cargar el producto");
    }
    System.out.print(cat.getNombresCategorias());
  }
}
