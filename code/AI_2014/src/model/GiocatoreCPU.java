package model;

public class GiocatoreCPU extends Giocatore {
	private Boolean _tipologiaAlgoritmo = false;
	
	//getter e setter
	public Boolean getTipologiaAlgoritmo() {
		return _tipologiaAlgoritmo;
	}

	public void setTipologiaAlgoritmo(Boolean _tipologiaAlgoritmo) {
		this._tipologiaAlgoritmo = _tipologiaAlgoritmo;
	}
	
	public GiocatoreCPU(String nomeCorrente, GiocoUno riferimentoGiocoUno) {
		super(nomeCorrente, riferimentoGiocoUno);
		// TODO Auto-generated constructor stub
	}

	//ritorna la carta scelta da giocare utilizzando l'algoritmo minimax
	private Carta MinimaxCartaScelta(){
		return new Carta("+2", "rossa", true);
	}
	
	//ritorna la carta scelta da giocare utilizzando l'algoritmo potatura alfa beta
	private Carta PotaturaAlfaBetaCartaScelta(){
		return new Carta("+2", "rossa", true);
	}

	public Carta GiocaCarta(){
		Carta cartaSelezionata;
		//ottengo al carta da giocare
		if(this._tipologiaAlgoritmo){
			cartaSelezionata = this.MinimaxCartaScelta();
		}else{
			cartaSelezionata = this.PotaturaAlfaBetaCartaScelta();
		}
		//se carta selezionata è null vuol dire che salto il turno volontariamente
		if(cartaSelezionata != null){
			//lo rimuovo dalle carte in mano
			this.getCarteInMano().remove(cartaSelezionata);
			//ritorno questa carta
			return cartaSelezionata;
		}else{
			return null;
		}
		
	}
	
	
}
