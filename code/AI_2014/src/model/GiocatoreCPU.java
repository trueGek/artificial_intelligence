package model;

import java.util.ArrayList;
import java.util.List;

public class GiocatoreCPU extends Giocatore {
	
	public GiocatoreCPU(String nomeCorrente, GiocoUno riferimentoGiocoUno) {
		super(nomeCorrente, riferimentoGiocoUno);
		// TODO Auto-generated constructor stub
	}

	//ritorna la carta scelta da giocare utilizzando l'algoritmo minimax
	private Carta MinimaxCartaScelta(){
		//devo valutare il massimo delle prime giocate possibili da me
		//in imput devo avere lo stato del gioco attuale quindi le carte in mano e le carte in tavola
		//quindi devo clonare le due liste per poterle modificare senza effettuare cambiamenti
		List<Carta> carteInManoMax = this.CloneListaCarte(this._carteInMano);
		List<Carta> carteInTavolaMinimax = this.CloneListaCarte(this._riferimentoGiocoUno.getMazzoTotale().getCarteScartate());
		//devo anche ipotizzare le carte che ha in mano il mio avversario. Il numero lo so, ma il valore no, quindi devo
		//generare le carte in mano all'avversario secondo le probabilità di presenza di quelle carte
		List<Carta> carteInManoMin = this.GeneraCarteAvversario(1); //da settare mumero corretto <------------------------------------------------------------------
		//eseguo algoritmo minimax. Mi restituisce il valore del ramo da scegliere
		int risultato = this.TurnoDiMax(carteInManoMax, carteInManoMin, carteInTavolaMinimax);
		//restituisco la carta prescelta o null se passo il turno pescando una carta
		return this.CartaSceltaMinimax(carteInManoMax, risultato, carteInTavolaMinimax);	
	}
	//parte max di minimax
	private int TurnoDiMax(List<Carta> carteInManoMax, List<Carta> carteInManoMin, List<Carta> carteInTavola){
		//se questa è l'ultima azione da fare allora restituisci valore funzione obiettivo.
		//setto valore funzione obiettivo a +1 quando vince
		if(this.TerminatoAlbero(carteInManoMax)){
			return 1;
		}
		//controllo le carte gioabili perche se sono zero devo passare il turno e pescare una carta quindi richiamare il mio avversario sulla sua carta giocata
		//devo controllare anche se l'ultima carta giocata è stata un più due o più quattro o salta turno o inverti giro perchè io salto il turno e tocca di nuovo all'avversario
		Carta ultimaCartaGiocata = carteInTavola.get(carteInTavola.size() -1 );
		//se è già stata usata (cioè l'avversario ha saltato il proprio turno e tocca a me) setto a false il controllo e proseguo tranquillamente nel gioco
		if(ultimaCartaGiocata.getUsata()){
			ultimaCartaGiocata.setUsata(false);
		}else{
			//se non è già stata usata la uso e la setto come usata (questo solo nel caso che la carta sia un +4 o cambia colore )
			ultimaCartaGiocata.setUsata(true);
			if(ultimaCartaGiocata.getColore() == "Jolly"){//se è un +4 o cambia colore cambio il colore e in caso pesco le carte
				if(ultimaCartaGiocata.getTipocarta() == "+4"){ //se è +4 pesco 4 carte
					for(int i = 0 ; i < 4; i++){
						carteInManoMax.add(this.PescaCartaDalMazzo());
					}
				}
				//sia che sia +4 che cambia colore devo cambiare il colore
				ultimaCartaGiocata.setColore(this.ScegliColore(carteInManoMax));
			}
			//ora devo controllare se è un +2 così pesco 2 carte
			if(ultimaCartaGiocata.getTipocarta() == "+2"){ //se è +4 pesco 4 carte
				for(int i = 0 ; i < 2; i++){
					carteInManoMax.add(this.PescaCartaDalMazzo());
				}
			}
			//devo controllare se è una carta che causa il salto di turno. Se lo è io non faccio nulla e salto il turno
			//se è una carta normale esco dall'if e proseguo con il gioco
			if(ultimaCartaGiocata.getCausaSaltoTurno()){
				//devo saltare il turno e quindi richiamare min sempre sull'ultima carta giocata
				int valRestituitoAzioneGiocata = this.TurnoDiMin(carteInManoMin, carteInManoMax, carteInTavola); 
				//dato che in questo giro non ho giocato nessuna carta e richiamato subito min restituisco subito che ha calcolato
				return valRestituitoAzioneGiocata;
			}else{
				//non serve la gestione di usata o meno quindi ritorno a false il controllo
				ultimaCartaGiocata.setUsata(false);
			}
		}
		//setto counter che mi permetterà di verificare quante carte giocabili avevo in mano
		int countNumeroCarteGiocate = 0;
		//setto valori ad un numero sicuramente inferiori a tutti quelli possibili che usciranno per usarlo come minimo
		int valori = -5000;
		for(int i = 0; i < carteInManoMax.size(); i++){
			//prima di giocare la carta devo verificare se questa carta è giocabile. Se la funzione ritorna true allora posso giocare la carta, se la funzione ritorna false non posso
			//giocare quella carta.
			if(this.CheckCartaGiocabile(carteInManoMax.get(i), ultimaCartaGiocata)){
				//devo creare lo stato da passare a min. Quindi una alla volta gioco le carte
				carteInTavola.add(carteInManoMax.get(i));
				//la rimuovo da carte in mano
				Carta rimossa = carteInManoMax.remove(i);
				//mi faccio restituire valore funzione obiettivo da min
				int valRestituitoAzioneGiocata = this.TurnoDiMin(carteInManoMin, carteInManoMax, carteInTavola); 
				//assegno valore restituito a carta così alla fine so quale albero prendere
				rimossa.setValore(valRestituitoAzioneGiocata);
				//reinserisco carta rimossa per far quadrare i conti
				carteInManoMax.add(i, rimossa);
				//e tolgo da carte in tavola questa appena buttata per far quadrare i conti
				carteInTavola.remove(rimossa);
				//ora devo assegnare a valori il malore massimo tra valori e valRestituitoAzioneGiocata
				//in valori è salvato sempre il valore massimio
				if(valRestituitoAzioneGiocata > valori){
					valori = valRestituitoAzioneGiocata;
				}
				//conto carte giocate da Max
				countNumeroCarteGiocate++;
			}
		}
		//se le carte giocate sono pari a 0 vuol dire che non ho giocato nessuna carta perchè non potevo e che quindi mi tocca pescare una carta
		if(countNumeroCarteGiocate == 0){
			//pesco una carta dal mazzo e metto in mano
			carteInManoMax.add(this.PescaCartaDalMazzo());
			//passo il turno senza fare nulla e pasos il gioco a min con la carta già presente in tavola
			int valRestituitoAzioneGiocata = this.TurnoDiMin(carteInManoMin, carteInManoMax, carteInTavola); 
			//dato che in questo giro non ho giocato nessuna carta e richiamato subito min restituisco subito che ha calcolato
			return valRestituitoAzioneGiocata;
		}
		//ritorno valori (valore massimo funzione max)
		return valori;
	}
	//parte min di minimax
	private int TurnoDiMin(List<Carta> carteInManoMin, List<Carta> carteInManoMax, List<Carta> carteInTavola){
		//se questa è l'ultima azione da fare allora restituisci valore funzione obiettivo.
		//setto valore funzione obiettivo a -1 quando vince
		if(this.TerminatoAlbero(carteInManoMin)){
			return -1;
		}
		//controllo le carte gioabili perche se sono zero devo passare il turno e pescare una carta quindi richiamare il mio avversario sulla sua carta giocata
		//devo controllare anche se l'ultima carta giocata è stata un più due o più quattro o salta turno o inverti giro perchè io salto il turno e tocca di nuovo all'avversario
		Carta ultimaCartaGiocata = carteInTavola.get(carteInTavola.size() -1 );
		//se è già stata usata (cioè l'avversario ha saltato il proprio turno e tocca a me) setto a false il controllo e proseguo tranquillamente nel gioco
		if(ultimaCartaGiocata.getUsata()){
			ultimaCartaGiocata.setUsata(false);
		}else{
			//se non è già stata usata la uso e la setto come usata (questo solo nel caso che la carta sia un +4 o cambia colore )
			ultimaCartaGiocata.setUsata(true);
			if(ultimaCartaGiocata.getColore() == "Jolly"){//se è un +4 o cambia colore cambio il colore e in caso pesco le carte
				if(ultimaCartaGiocata.getTipocarta() == "+4"){ //se è +4 pesco 4 carte
					for(int i = 0 ; i < 4; i++){
						carteInManoMin.add(this.PescaCartaDalMazzo());
					}
				}
				//sia che sia +4 che cambia colore devo cambiare il colore
				ultimaCartaGiocata.setColore(this.ScegliColore(carteInManoMin));
			}
			//ora devo controllare se è un +2 così pesco 2 carte
			if(ultimaCartaGiocata.getTipocarta() == "+2"){ //se è +4 pesco 4 carte
				for(int i = 0 ; i < 2; i++){
					carteInManoMin.add(this.PescaCartaDalMazzo());
				}
			}
			//devo controllare se è una carta che causa il salto di turno. Se lo è io non faccio nulla e salto il turno
			//se è una carta normale esco dall'if e proseguo con il gioco
			if(ultimaCartaGiocata.getCausaSaltoTurno()){
				//devo saltare il turno e quindi richiamare max sempre sull'ultima carta giocata
				int valRestituitoAzioneGiocata = this.TurnoDiMax(carteInManoMin, carteInManoMax, carteInTavola); 
				//dato che in questo giro non ho giocato nessuna carta e richiamato subito min restituisco subito che ha calcolato
				return valRestituitoAzioneGiocata;
			}else{
				//non serve la gestione di usata o meno quindi ritorno a false il controllo
				ultimaCartaGiocata.setUsata(false);
			}
		}
		//setto counter che mi permetterà di verificare quante carte giocabili avevo in mano
		int countNumeroCarteGiocate = 0;
		//setto valori ad un valore massimo superiore a tutti quelli possibili di uscita da max
		int valori = 5000;
		//passo tutte le possibili carte che posso giocare 
		for(int i = 0; i < carteInManoMin.size(); i++){
			//prima di giocare la carta devo verificare se questa carta è giocabile. Se la funzione ritorna true allora posso giocare la carta, se la funzione ritorna false non posso
			//giocare quella carta.
			if(this.CheckCartaGiocabile(carteInManoMin.get(i), ultimaCartaGiocata)){
				//devo creare lo stato da passare a min. Quindi una alla volta gioco le carte
				carteInTavola.add(carteInManoMin.get(i));
				//la rimuovo da carte in mano
				Carta rimossa = carteInManoMin.remove(i);
				//mi faccio restituire valore funzione obiettivo da max
				int valRestituitoAzioneGiocata = this.TurnoDiMax(carteInManoMin, carteInManoMax, carteInTavola); 
				//reinserisco carta rimossa per far quadrare i conti
				carteInManoMin.add(i, rimossa);
				//e tolgo da carte in tavola questa appena buttata per far quadrare i conti
				carteInTavola.remove(rimossa);
				//ora devo assegnare a valori il malore minimo tra valori e valRestituitoAzioneGiocata
				//in valori è salvato sempre il valore minimo
				if(valRestituitoAzioneGiocata < valori){
					valori = valRestituitoAzioneGiocata;
				}
				//conto carte giocate da Max
				countNumeroCarteGiocate++;
			}
		}
		//se le carte giocate sono pari a 0 vuol dire che non ho giocato nessuna carta perchè non potevo e che quindi mi tocca pescare una carta
		if(countNumeroCarteGiocate == 0){
			//pesco una carta dal mazzo e metto in mano
			carteInManoMin.add(this.PescaCartaDalMazzo());
			//passo il turno senza fare nulla e passo il gioco a max con la carta già presente in tavola
			int valRestituitoAzioneGiocata = this.TurnoDiMin(carteInManoMin, carteInManoMax, carteInTavola); 
			//dato che in questo giro non ho giocato nessuna carta e richiamato subito min restituisco subito che ha calcolato
			return valRestituitoAzioneGiocata;
		}
		//ritorno valori (valore minimo funzione min)
		return valori;
	}
	
	//check se carta è buttabile secondo le regole scelte
	private Boolean CheckCartaGiocabile(Carta cartaPresaInConsiderazione, Carta cartaSulBanco){
		//se la carta giocata è una carta Jolly
		if(cartaPresaInConsiderazione.getColore() == "Jolly" ){
			Boolean check = false; //var appoggio per controllo colore carte
			//controllo se in mano ho carte dello stesso colore della carta sul banco
			for(int i = 0; i < this._carteInMano.size(); i++){
				if(this._carteInMano.get(i).getColore() == cartaSulBanco.getColore()){
					check = true;
				}
			}
			if(check){//se ho carte in mano con lo stesso colore della carta sul banco non posso buttare il cambia colore
				return false;
			}else{ //non ho carte dello stesso colore quindi sono abilitato all'uso di questa carta e restituisco relativo punteggio
				return true;
			}
		}
		//se il colore tra le due carte è diverso e anche il simbolo è diverso allora ritorno false
		if((cartaPresaInConsiderazione.getColore() != cartaSulBanco.getColore()) & (cartaPresaInConsiderazione.getTipocarta() != cartaSulBanco.getTipocarta())){
			return false;
		}
		//se il colore è uguale tra le carte ritorno true
		if(cartaPresaInConsiderazione.getColore() == cartaSulBanco.getColore()){
			return true;
		}
		//se il simbolo è uguale tra le carte ritorno true
		if(cartaPresaInConsiderazione.getTipocarta() == cartaSulBanco.getTipocarta()){
			return true;
		}
		return false;
	}
	
	//funzione di terminazione. restituisce true se sono alla fine dell'albero
	private Boolean TerminatoAlbero(List<Carta> listaCarteInMano){
		if(listaCarteInMano.isEmpty()){
			return true; // se la lista delle carte in mano è 1 vuol dire che ho vinto e finito il gioco
		}else{
			return false;
		}
	}
	
	//funzione di valutazione carta da giocare
	private int ValutaMossa(Carta cartaGiocata){
		//riferimmento a carta in cima al mazzo sul tavolo
		Carta cartaSulBanco = this._riferimentoGiocoUno.getMazzoTotale().CartaInCima();
		//se il colore tra le due carte è diverso e la carta giocata non è carta azione allora ritorno 0
		if((cartaGiocata.getColore() != cartaSulBanco.getColore()) & cartaGiocata.getColore() != "Jolly"){
			return 0;
		}
		//se la carta giocata è una carta Jolly
		if(cartaGiocata.getColore() == "Jolly" ){
			Boolean check = false; //var appoggio per controllo colore carte
			//controllo se in mano ho carte dello stesso colore della carta sul banco
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
	
	//ritorna la carta scelta da giocare utilizzando l'algoritmo potatura alfa beta  <------------------------------------------------------------------
	private Carta PotaturaAlfaBetaCartaScelta(){
		return new Carta("+2", "rossa", true);
	}

	//sceglie la carta da giocare usando o il minimax o la potatura alfa beta
	public Carta GiocaCarta(){
		//se l'ultima carta giocata in campo era un +2 o +4 non chiedo manco che azione fare, pesco le carte e passo il turno
		//utlima carta giocata
		Carta CimaMazzo = this._riferimentoGiocoUno.getMazzoTotale().getCarteScartate().get(this._riferimentoGiocoUno.getMazzoTotale().getCarteScartate().size() - 1);
		if(CimaMazzo.getUsata()){
			CimaMazzo.setUsata(false);
		}else{
            //se non è già stata usata la uso e la setto come usata (questo solo nel caso che la carta sia un +4 o cambia colore )
            CimaMazzo.setUsata(true);
            if(CimaMazzo.getColore() == "Jolly"){//se è un +4 o cambia colore cambio il colore e in caso pesco le carte
                if(CimaMazzo.getTipocarta() == "+4"){ //se è +4 pesco 4 carte
                        for(int i = 0 ; i < 4; i++){
                                this._carteInMano.add(this.PescaCartaDalMazzo());
                        }
                }
                //sia che sia +4 che cambia colore devo cambiare il colore
                CimaMazzo.setColore(this.ScegliColore(this._carteInMano));
            }
            //ora devo controllare se è un +2 così pesco 2 carte
            if(CimaMazzo.getTipocarta() == "+2"){ //se è +4 pesco 4 carte
                    for(int i = 0 ; i < 2; i++){
                            this._carteInMano.add(this.PescaCartaDalMazzo());
                    }
            }
            //devo controllare se è una carta che causa il salto di turno. Se lo è io non faccio nulla e salto il turno
            //se è una carta normale esco dall'if e proseguo con il gioco
            if(CimaMazzo.getCausaSaltoTurno()){
                    //ritorno null cosi passo il turno
                    return null;
            }else{
                //non serve la gestione di usata o meno quindi ritorno a false il controllo
                CimaMazzo.setUsata(false);
            }
        }
		Carta cartaSelezionata;
		//ottengo al carta da giocare
		if(this._riferimentoGiocoUno.getTipologiaAlgoritmo()){
			cartaSelezionata = this.MinimaxCartaScelta();
		}else{
			cartaSelezionata = this.PotaturaAlfaBetaCartaScelta();
		}
		//se carta selezionata è null vuol dire che salto il turno volontariamente e quindi pesco una carta
		if(cartaSelezionata != null){
			//lo rimuovo dalle carte in mano
			this.getCarteInMano().remove(cartaSelezionata);
			//ritorno questa carta
			return cartaSelezionata;
		}else{
			//pesco una carta
			this._carteInMano.add(this.PescaCartaDalMazzo());
			//ritorno null cosi passo il turno
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
	
	//generare carte in mano all'avversario
	private List<Carta> GeneraCarteAvversario(Integer numeroCarte){
		//da fare  <---------------------------------------------------------------------------------------------------------------------------------------------------------------
		List<Carta> listaCarte = new ArrayList<Carta>();
		for(Integer i = 0; i < numeroCarte ; i++){
			listaCarte.add(new Carta("+2", "rossa", true));
		}
		return listaCarte;
	}
	
	//scegli colore dominante tra le carte in mano per cambiare colore con cambia colore
	private String ScegliColore(List<Carta> carteInMano){
		Integer rosso = 0;
		Integer blu = 0;
		Integer giallo= 0;
		Integer verde= 0;
		//conteggio i vari diversi colori
		for(int i = 0; i < carteInMano.size(); i++){
			String colore = carteInMano.get(i).getColore();
			if(colore == "rosso"){
				rosso++;
			}else{
				if(colore == "blu"){
					blu++;
				}else{
					if(colore == "giallo"){
						giallo++;
					}else{
						if(colore == "verde"){
							verde++;
						}
					}
				}
			}
		}
		//trovo il colore predominante
		if(rosso > blu & rosso > giallo & rosso > verde){
			return "rosso";
		}
		if(blu > rosso & blu > giallo & blu > verde){
			return "blu";
		}
		if(giallo > blu & giallo > rosso & giallo > verde){
			return "giallo";
		}
		if(verde > blu & verde > giallo & verde > rosso){
			return "verde";
		}
		//se nessun colore domina lo scelgo random
		Integer randomNum = 0 + (int)(Math.random()*100); 
		if(randomNum < 25 ) return "rosso";
		if(randomNum < 50 ) return "blu";
		if(randomNum < 75 ) return "giallo";
		return "verde";
	}
	
	//dato risultato minimax sceglie carta da giocare. Se più carte hanno stesso valore scelgo quale usare in base euristica prescelta -> prima a sinistra
	private Carta CartaSceltaMinimax(List<Carta> carteInMano, Integer valore, List<Carta> carteInTavola){
		//conto le carte che possono essere giocate
		int counterCarteGiocabili = 0; //counter
		Carta cartaInTavolo = carteInTavola.get(carteInTavola.size() -1 );//carta sul tavolo
		for(int i = 0; i < carteInMano.size(); i++){
			if(this.CheckCartaGiocabile(carteInMano.get(i), cartaInTavolo)){
				counterCarteGiocabili++;
			}
		}
		//se sono zero devo pescare e saltare il turno
		if(counterCarteGiocabili == 0){
			return null;
		}else{
			//cerco la prima carta con valore ufuale al calore ritornato
			int i = 0;
			while(carteInMano.get(i).getValore() != valore){
				i++;
			}
			//trovato la carta con quel valore la restituisco
			return carteInMano.get(i);
		}
	}
}
