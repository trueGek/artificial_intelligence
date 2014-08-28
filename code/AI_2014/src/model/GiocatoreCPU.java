package model;

import java.util.ArrayList;
import java.util.List;

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
		//devo valutare il massimo delle prime giocate possibili da me
		//in imput devo avere lo stato del gioco attuale quindi le carte in mano e le carte in tavola
		//quindi devo clonare le due liste per poterle modificare senza effettuare cambiamenti
		List<Carta> carteInManoMiniMax = this.CloneListaCarte(this._carteInMano);
		List<Carta> carteInTavolaMinimax = this.CloneListaCarte(this._riferimentoGiocoUno.getMazzoTotale().getCarteScartate());
		int risultato = this.TurnoDiMax(carteInManoMiniMax, carteInTavolaMinimax);
		
		return new Carta("+2", "rossa", true); 	
	}
	//parte max di minimax
	private int TurnoDiMax(List<Carta> carteInMano, List<Carta> carteInTavola){
		//se questa è l'ultima azione da fare allora restituisci valore funzione obiettivo
		if(this.TerminatoAlbero(carteInMano)){
			int risultato = this.ValutaMossa(cartaGiocata);
		}
		int valori = -1;
		//passo tutte le possibili carte che posso giocare 
		for(int i = 0; i < this._carteInMano.size(); i++){
			int valRestituitoAzioneGiocata = this.TurnoDiMin(); 
		}
	}
	//parte min di minimax
	private int TurnoDiMin(){
		return 1;
	}
	
	
	//funzione di terminazione. restituisce true se sono alla fine dell'albero
	private Boolean TerminatoAlbero(List<Carta> listaCarteInMano){
		if(listaCarteInMano.isEmpty()){
			return true; // se la lista delle carte in mano è 1 vuol dire che ho vinto e finito il gioco
		}else{
			return false;
		}
	}
	
	//funzione di valutazione
	private int ValutaMossa(Carta cartaGiocata){
		//riferimmento a carta in cima al mazzo sul tavolo
		Carta cartaSulBanco = this._riferimentoGiocoUno.getMazzoTotale().CartaInCima();
		//se il colore tra le due carte è dicerso e la carta giocata non è carta azione allora ritorno 0
		if((cartaGiocata.getColore() != cartaSulBanco.getColore()) & cartaGiocata.getColore() != "Jolly"){
			return 0;
		}
		//se la carta giocata è una carta Jolly
		if(cartaGiocata.getColore() == "Jolly" ){
			Boolean check = false; //var appoggio per controllo colore carte
			//controllo e in mano ho carte dello stesso colore della carta sul banco
			for(int i = 0; i < this._carteInMano.size(); i++){
				if(this._carteInMano.get(i).getColore() == cartaSulBanco.getColore()){
					check = true;
				}
			}
			if(check){//se ho carte in mano con lo stesso colore della carta sul banco non posso buttare il cambia colore
				return 0;
			}else{ //non ho carte dello stesso colore quindi sono abilitato all'uso di questa carta e restituisco relativo punteggio
				//favorisco l'uso del cambia colore rispetto del +4 conferendo due punteggi diversi
				if(cartaGiocata.getTipocarta() == "+4"){
					return 1;
				}else{
					return 2;
				}
			}
		}
		//se la carta giocata è una carta con stesso simbolo ma colore diverso
		if(cartaGiocata.getTipocarta() == cartaSulBanco.getTipocarta() & cartaGiocata.getColore() != cartaSulBanco.getColore()){
			return 1 + this.SubFunzioneCasiParticolari(cartaGiocata, cartaSulBanco);
		}
		//se la carta ha lo stesso colore
		if(cartaGiocata.getColore() == cartaSulBanco.getColore()){
			return 2 + this.SubFunzioneCasiParticolari(cartaGiocata, cartaSulBanco);
		}
		return 0;
	}	
	private int SubFunzioneCasiParticolari(Carta cartaGiocata, Carta cartaSulBanco){
		//se la carta giocata è una carta azione cambia giro
		if(cartaGiocata.getTipocarta() == "inverti giro"){
			return 1;
		}
		//se la carta giocata è una carta azione stop
		if(cartaGiocata.getTipocarta() == "stop"){
			return 1;
		}
		//se la carta giocata è una carta azione +2
		if(cartaGiocata.getTipocarta() == "+2"){
			return 2;
		}
		//se la carta è una carta normale
		return 3;
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
	
	
	//clona mazzo di carte
	private List<Carta> CloneListaCarte(List<Carta> DaClonare){
		List<Carta> nuovaLista = new ArrayList<Carta>();
		//copio tutti gli elementi della lista
		for(int i = 0; i < DaClonare.size(); i++){
			Carta nuovaCarta = new Carta(DaClonare.get(i).getTipocarta(),DaClonare.get(i).getColore(),DaClonare.get(i).getCausaSaltoTurno());
			nuovaLista.add(nuovaCarta);
		}
		return nuovaLista;
	}
	
	
}
