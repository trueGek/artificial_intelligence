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
		this._carteDelMazzo = new ArrayList<Carta>();
		this._carteScartate = new ArrayList<Carta>();
	}
	
	//Metodo che ritorna l'ultima carta inserita nel mazzo carteScartate (sarebbe la carta sul tavolo)
	public Carta CartaInCima() {
		return this._carteScartate.get(this._carteScartate.size() - 1);
	}
	
	//pesca una carta dal mazzo carteDelMazzo. Ritorna la prima carta della lista e la elimiina dalla lista
	public Carta PescaCarta() {
        //faccio check di quante carte ci sono nel mazzo. Se le carte sono finite devo ritornare null
        if(this._carteDelMazzo.isEmpty()){
            return null;
        }
        return this._carteDelMazzo.remove(0);
	}
        
	//inizializza carte scartate
	public void ScartaPrimaCartaInizioPartita(){
	    //la prima carta non può essere jolli o +2, salta giro o cambia giro
	    Carta cartaPescata = this.PescaCarta();
	    while(cartaPescata.getColore() == "Jolly" || cartaPescata.getTipocarta() == "+2" || cartaPescata.getTipocarta() == "inverti giro" || cartaPescata.getTipocarta() == "stop"){
	        this._carteDelMazzo.add(cartaPescata);
	        cartaPescata = this.PescaCarta();
	    }
	    this._carteScartate.add(cartaPescata);
	}
	
	//butta una carta. Prende una carta passata per parametro e la aggiunge alla lista carteScartate
	public void ButtaCarta(Carta cartaCorrente) {
		this._carteScartate.add(cartaCorrente);
	}
	
	//mescola le carte del mazzo
	public void MescolaCarte() {
		Collections.shuffle(this._carteDelMazzo);
	}
        
    //clona mazzo per poterlo modificare senza intoppi durante la partita
    public Mazzo ClonaMazzo(){
        Mazzo nuovoMazzo = new Mazzo();
        //copio tutti gli elementi delle liste
        for(int i = 0; i < this._carteDelMazzo.size(); i++){
                Carta nuovaCarta = new Carta(this._carteDelMazzo.get(i).getTipocarta(),this._carteDelMazzo.get(i).getColore(),this._carteDelMazzo.get(i).getCausaSaltoTurno());
                nuovoMazzo._carteDelMazzo.add(nuovaCarta);
        }
        for(int i = 0; i < this._carteScartate.size(); i++){
                Carta nuovaCarta = new Carta(this._carteScartate.get(i).getTipocarta(),this._carteScartate.get(i).getColore(),this._carteScartate.get(i).getCausaSaltoTurno());
                nuovoMazzo._carteScartate.add(nuovaCarta);
        }
        return nuovoMazzo;
    }
    
    public void ResetMazzo(Mazzo Resettore){
        //resetto il mazzo con le informazioni presenti il mazzoReste
        this._carteDelMazzo.clear();
        this._carteScartate.clear();
        for(int i = 0; i < Resettore._carteDelMazzo.size(); i++){
                Carta nuovaCarta = new Carta(Resettore._carteDelMazzo.get(i).getTipocarta(),Resettore._carteDelMazzo.get(i).getColore(),Resettore._carteDelMazzo.get(i).getCausaSaltoTurno());
                this._carteDelMazzo.add(nuovaCarta);
        }
        for(int i = 0; i < Resettore._carteScartate.size(); i++){
                Carta nuovaCarta = new Carta(Resettore._carteScartate.get(i).getTipocarta(),Resettore._carteScartate.get(i).getColore(),Resettore._carteScartate.get(i).getCausaSaltoTurno());
                this._carteScartate.add(nuovaCarta);
        }
    }
}