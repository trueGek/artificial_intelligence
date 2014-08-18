package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazzo {
	private List<Carta> _carteDelMazzo;
	private List<Carta> _carteScartate;
	
	//getter e setter
	public List<Carta> getCarteDelMazzo() {
		return _carteDelMazzo;
	}

	public void setCarteDelMazzo(List<Carta> _carteDelMazzo) {
		this._carteDelMazzo = _carteDelMazzo;
	}

	public List<Carta> getCarteScartate() {
		return _carteScartate;
	}

	public void setCarteScartate(List<Carta> _carteScartate) {
		this._carteScartate = _carteScartate;
	}
	
	public Mazzo() {
		this._carteDelMazzo=new ArrayList<Carta>();
		this._carteScartate=new ArrayList<Carta>();
	}
	
	//Metodo che ritorna l'ultima carta inserita nel mazzo carteScartate (sarebbe la carta sul tavolo)
	public Carta CartaInCima() {
		return this._carteScartate.get(this._carteScartate.size() - 1);
	}
	
	//pesca una carta dal mazzo carteDelMazzo. Ritorna la prima carta della lista e la elimiina dalla lista
	public Carta PescaCarta() {
		return this._carteDelMazzo.remove(0);
	}
	
	//butta una carta. Prende una carta passata per parametro e la aggiunge alla lista carteScartate
	public void ButtaCarta(Carta cartaCorrente) {
		this._carteScartate.add(cartaCorrente);
	}
	
	//mescola le carte del mazzo
	public void MescolaCarte() {
		Collections.shuffle(this._carteDelMazzo);
	}
}
