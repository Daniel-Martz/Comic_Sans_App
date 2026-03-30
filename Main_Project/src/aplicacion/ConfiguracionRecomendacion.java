package aplicacion;

import java.util.*;

public class ConfiguracionRecomendacion {

	private static ConfiguracionRecomendacion instancia;
	private int importanciaInteres;
	private int importanciaResena;
	private int importanciaNovedad;
	private int unidadesRecomendadas;

	private ConfiguracionRecomendacion(int importanciaInteres, int importanciaResena, int importanciaNovedad,
			int unidadesRecomendadas) {
		this.importanciaInteres = importanciaInteres;
		this.importanciaResena = importanciaResena;
		this.importanciaNovedad = importanciaNovedad;
		this.unidadesRecomendadas = unidadesRecomendadas;
	}

	public static ConfiguracionRecomendacion getInstancia() {
		if (instancia == null) {
			instancia = new ConfiguracionRecomendacion(1, 2, 3,
					5);
		}
		return instancia;
	}
	
	public void configurarImportancia(int importanciaInteres, int importanciaResena, int importanciaNovedad) {
	    if (importanciaInteres < 0 || importanciaResena < 0 || importanciaNovedad < 0) {
	        throw new IllegalArgumentException("Las importancias no pueden ser negativas");
	    }
		this.importanciaInteres = importanciaInteres;				
		this.importanciaResena = importanciaResena;		
		this.importanciaNovedad = importanciaNovedad;
	}
	
	public void configurarUnidades(int unidades) {
		if(unidades <= 0) {
			throw new IllegalArgumentException("El número de unidades recomendadas no pueden ser menores o iguales");
		}
		unidadesRecomendadas = unidades;
	}
}