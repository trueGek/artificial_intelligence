package model;

import java.util.ArrayList;
import java.util.List;

public class Giocatore {
	private String _nome;
	protected List<Carta> _carteInMano;
	protected GiocoUno _riferimentoGiocoUno;

	//getter e setter
	public String getNome() {
		return _nome;
	}

	public void setNome(String _nome) {
		this._nome = _nome;
	}

	public List<Carta> getCarteInMano() {
		return _carteInMano;
	}

	public void setCarteInMano(List<Carta> _carteInMano) {
		this._carteInMano = _carteInMano;
	}
	
	public Giocatore(String nomeCorrente, GiocoUno riferimentoGiocoUno){
		this._nome = nomeCorrente;
		this._carteInMano = new ArrayList<Carta>();
		this._riferimentoGiocoUno = riferimentoGiocoUno;
	}
	
	//Pesca una carta dal mazzo e la aggiunge alla mano
	public Carta PescaCartaDalMazzo(){
		return this._riferimentoGiocoUno.getMazzoTotale().PescaCarta();
	}	
	
	//Pesca le carte iniziali per iniziare il gioco
	public void PescaCarteIniziali(){
		for(int i = 0; i < 6 ; i++){
			this._carteInMano.add(this.PescaCartaDalMazzo());
		}
	}

	//data la carta cliccata sull ui gioco questa carta. versione se da click ricevo la carta
	public Carta GiocaCarta(Carta selezionata){
		//tolgo la carta selezionata da quelle che possiedo in mano
		this._carteInMano.remove(selezionata);
		return selezionata;
	}
	
	//data la carta cliccata sull ui gioco questa carta. versione se da click ricevo index
	public Carta GiocaCarta(Integer i){
		//tolgo la carta selezionata da quelle che possiedo in mano
		Carta cartaSelezionata = this._carteInMano.get(i);
		this._carteInMano.remove(i);
		return cartaSelezionata;
	}
	
	//metodo usato per passare il turno. non si fa nulla
	public void PassaIlTurno(){
		 
	}
	
	//metodo dichiara UNO
	public void DichiaraUNO(){
		//visualizza segnale di uno
	}
	
	//metodo usato per dichiarare la vittoria
	public void DichiaraVittoria(){
		//visualizza segnale di vittoria
	}
}
