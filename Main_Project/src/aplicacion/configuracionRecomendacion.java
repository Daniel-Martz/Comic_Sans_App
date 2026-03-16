package aplicacion; 
import java.util.*;

public class configuracionRecomendacion {
	
	private int importanciaInteres;
	private int importanciaResena; 
	private int importanciaNovedad;
	private int unidadesRecomendadas;
	
	public configuracionRecomendacion(int importanciaInteres, int importanciaResena, 
									  int importanciaNovedad, int unidadesRecomendadas) {
		this.importanciaInteres = importanciaInteres;
		this.importanciaResena = importanciaResena;
		this.importanciaNovedad = importanciaNovedad;
		this.unidadesRecomendadas = unidadesRecomendadas;
	}
}