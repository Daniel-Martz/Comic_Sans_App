package filtro;

import java.io.Serializable;

public abstract class Filtro implements Serializable{
	protected boolean ordenAscendente;

	public Filtro(boolean ordenAscendente) {
		super();
		this.ordenAscendente = ordenAscendente;
	}
	
	public boolean isOrdenAscendente() {
	    return ordenAscendente;
	}
	
	public void limpiarFiltro() {
		ordenAscendente = false;
	}
	
}
