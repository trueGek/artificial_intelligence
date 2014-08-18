package model;

public class Carta {
	private String _tipocarta;
	private String _colore;
	private Integer _valore;
	private Boolean _causaSaltoTurno;

	//getter and setter
	public String getTipocarta() {
		return _tipocarta;
	}

	public void setTipocarta(String _tipocarta) {
		this._tipocarta = _tipocarta;
	}

	public String getColore() {
		return _colore;
	}

	public void setColore(String _colore) {
		this._colore = _colore;
	}

	public Integer getValore() {
		return _valore;
	}

	public void setValore(Integer _valore) {
		this._valore = _valore;
	}
	
	public Carta(String tipoCarta, String colore, Boolean saltaTurno){
		this._colore = colore;
		this._tipocarta = tipoCarta;
		this._causaSaltoTurno = saltaTurno;
	}

	public Boolean getCausaSaltoTurno() {
		return _causaSaltoTurno;
	}

	public void setCausaSaltoTurno(Boolean _causaSaltoTurno) {
		this._causaSaltoTurno = _causaSaltoTurno;
	}

}
