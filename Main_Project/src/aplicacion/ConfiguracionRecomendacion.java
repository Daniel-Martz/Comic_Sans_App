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

	public static ConfiguracionRecomendacion getInstancia(int importanciaInteres, int importanciaReseña,
			int importanciaNovedad, int unidadesRecomendadas) {
		if (instancia == null) {
			instancia = new ConfiguracionRecomendacion(importanciaInteres, importanciaReseña, importanciaNovedad,
					unidadesRecomendadas);
		}
		return instancia;
	}
}