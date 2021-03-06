package model;

import java.util.ArrayList;
import java.util.List;

public class GiocatoreCPU extends Giocatore {
    
	public GiocatoreCPU(String nomeCorrente, GiocoUno riferimentoGiocoUno) {
		super(nomeCorrente, riferimentoGiocoUno);
		// TODO Auto-generated constructor stub
	}

	//ritorna la carta scelta da giocare utilizzando l'algoritmo minimax
	private Carta AlgoritmoCartaScelta(Boolean isMinimax, String ultimoColoreValido){
		//devo valutare il massimo delle prime giocate possibili da me
		//in imput devo avere lo stato del gioco attuale quindi le carte in mano e le carte in tavola
		//quindi devo clonare le due liste per poterle modificare senza effettuare cambiamenti
		List<Carta> carteInManoMax = this.CloneListaCarte(this._carteInMano);
		List<Carta> carteInTavolaMinimax = this.CloneListaCarte(this._riferimentoGiocoUno.getMazzoTotale().getCarteScartate());
        //devo clonare anche il mazzo perch� non posso modificarlo.
        Mazzo mazzoDaUsare = this._riferimentoGiocoUno.getMazzoTotale().ClonaMazzo();
        //mazzo reset. ogni volta che eseguo il mimimax o la potatura devo resettare a questa condizione iniziale il mazzo
        Mazzo mazzoRESET = this._riferimentoGiocoUno.getMazzoTotale().ClonaMazzo();
        //dato che non conosco le carte dell'avversario devo calcolare una probabilit� per ogni ordine di carte
        //calcolo il valor eminimax di ogni azione in ogni ordine delle carte e quindi scelgo l'azione con il valore aspettato pi� alto
        //quindi ciclo n volte e vedo quale risultato viene riestituito pi� volte
        //creo lista appoggio integer che conta quante volte � stata usata quella carta della lista delle mie carte in mano
        Integer[] vettoreConteggioUsoCarta = new Integer[carteInManoMax.size() + 1];
        //azzero tutte le posizioni
        for(int i = 0 ; i < carteInManoMax.size() + 1; i++){
            vettoreConteggioUsoCarta[i] = 0;
        }
        //devo controllare se la carta in cambo � segnata usata o meno. perch� devo ricordarmerlo altrimenti il minimax impazzisce perch� la cambia
        //un ciclo si e uno no
        Boolean ricordoUsata = false;
        if(carteInTavolaMinimax.get(carteInTavolaMinimax.size() -1 ).getUsata()){
            ricordoUsata = true;
        }
        for(int i = 0 ; i < 50; i++){
            //devo anche ipotizzare le carte che ha in mano il mio avversario. Non conosco ne quante carte ha in mano l'avversario ne quali sono quindi devo
            //generare le carte in mano all'avversario secondo le probabilit� di presenza di quelle carte
            //ipotizzo che in mano abbia esattamente le mie stesse carte
            List<Carta> carteInManoMin = this.GeneraCarteAvversario(carteInManoMax.size(), mazzoDaUsare); 
            //se ricordo usata � tru devo controllare che sia sempre true ad ogni passo
            if(ricordoUsata) carteInTavolaMinimax.get(carteInTavolaMinimax.size() -1 ).setUsata(true);
            //eseguo algoritmo minimax. Mi restituisce il valore del ramo da scegliere
            int risultato = this.TurnoDiMax(carteInManoMax, carteInManoMin, carteInTavolaMinimax, mazzoDaUsare, -10000, 10000, isMinimax, -1, ultimoColoreValido);
            //ottengo index della carta scelta da giocare
            Integer ris = this.CartaSceltaMinimax(carteInManoMax, risultato, carteInTavolaMinimax, ultimoColoreValido);
            //se ritorna null devo pescare una carta quindi uso size+1 per tenerlo conto
            if(ris == null){
                vettoreConteggioUsoCarta[carteInManoMax.size()]++;
            }else{
                vettoreConteggioUsoCarta[ris]++;
            }
            //ogni volta devo effettuare il minimax o potatura con il mazzo resettato alla condizione iniziale
            mazzoDaUsare.ResetMazzo(mazzoRESET);
        }
        //devo trovare quale � la scelta con maggior peso. se ce ne sono di uguali prendo la prima
        int valoreMassimo = -2000;
        for(int i = 0 ; i < carteInManoMax.size() + 1; i++){
            if(vettoreConteggioUsoCarta[i] > valoreMassimo) valoreMassimo = vettoreConteggioUsoCarta[i];
        }
        int i = 0;
        while(vettoreConteggioUsoCarta[i] < carteInManoMax.size() + 1 & valoreMassimo != vettoreConteggioUsoCarta[i]){
            i++;
        }
        //controllo se � il null e se lo � lo restituisco
        if(i == this._carteInMano.size()){
            return null;
        }
        //sistemo mazzo totale carte
        this._riferimentoGiocoUno.getMazzoTotale().ResetMazzo(mazzoRESET);
		//restituisco la carta prescelta 
		return 	this._carteInMano.get(i);

	}
	//parte max di minimax
	private int TurnoDiMax(List<Carta> carteInManoMax, List<Carta> carteInManoMin, List<Carta> carteInTavola, Mazzo mazzoCarteTotali, int alfa, int beta, Boolean isMinimax, int livello, String ultimoColoreValido){
        //aumento di 1 il livello dell'albero
        livello++;
		//se questa � l'ultima azione da fare allora restituisci valore funzione obiettivo.
		//setto valore funzione obiettivo a +10 quando vince
		if(this.TerminatoAlbero(carteInManoMax)){
			return 100;
		}
		//controllo le carte gioabili perche se sono zero devo passare il turno e pescare una carta quindi richiamare il mio avversario sulla sua carta giocata
		//devo controllare anche se l'ultima carta giocata � stata un pi� due o pi� quattro o salta turno o inverti giro perch� io salto il turno e tocca di nuovo all'avversario
		Carta ultimaCartaGiocata = carteInTavola.get(carteInTavola.size() -1 );
                //se voglio limitare la profondit� ad un livello prefissato invece che esplorare il tutto utilizzo questo codice
                if(this.TestTaglio(livello)){
			return this.ValutaMossa(ultimaCartaGiocata, carteInManoMax, carteInManoMin, true);
		}
		//se � gi� stata usata (cio� l'avversario ha saltato il proprio turno e tocca a me) setto a false il controllo e proseguo tranquillamente nel gioco
		if(ultimaCartaGiocata.getUsata()){
			ultimaCartaGiocata.setUsata(false);
		}else{
			//se non � gi� stata usata la uso e la setto come usata (questo solo nel caso che la carta sia un +4 o cambia colore )
			ultimaCartaGiocata.setUsata(true);
			if(ultimaCartaGiocata.getColore() == "Jolly"){//se � un +4 o cambia colore cambio il colore e in caso pesco le carte
				if(ultimaCartaGiocata.getTipocarta() == "+4"){ //se � +4 pesco 4 carte
					for(int i = 0 ; i < 4; i++){
                        //potrei ritornare null perch� il mazzo � finito
                        Carta cartaPescata = this.PescaCartaDalMazzo();
                        if(cartaPescata != null){
                            carteInManoMax.add(cartaPescata);
                        }else{
                            //ritorna null. vuol dire che il mazzo � finito. devo vedere chi ha vinto. 
                            //vince chi ha meno carte in mano
                            return this.CheckNumeroCarteInManoAllaFineDelMazzo(true);
                        }
					}
				}
			}
			//ora devo controllare se � un +2 cos� pesco 2 carte
			if(ultimaCartaGiocata.getTipocarta() == "+2"){ //se � +4 pesco 4 carte
				for(int i = 0 ; i < 2; i++){
					//potrei ritornare null perch� il mazzo � finito
                    Carta cartaPescata = this.PescaCartaDalMazzo();
                    if(cartaPescata != null){
                        carteInManoMax.add(cartaPescata);
                    }else{
                        //ritorna null. vuol dire che il mazzo � finito. devo vedere chi ha vinto. 
                        //vince chi ha meno carte in mano
                        return this.CheckNumeroCarteInManoAllaFineDelMazzo(true);
                    }
				}
			}
			//devo controllare se � una carta che causa il salto di turno. Se lo � io non faccio nulla e salto il turno
			//se � una carta normale esco dall'if e proseguo con il gioco
			if(ultimaCartaGiocata.getCausaSaltoTurno()){
                //mi salvo tutte le liste per risistemarle al ritorno dalla ricorsione
                List<Carta> carteInManoMinCopia = this.CloneListaCarte(carteInManoMin);
                List<Carta> carteInManoMaxCopia = this.CloneListaCarte(carteInManoMax);
                List<Carta> carteInTavolaCopia = this.CloneListaCarte(carteInTavola);
                //String coloreAttuale = this._coloreDiGioco;
                Mazzo mazzoCarteCopia = mazzoCarteTotali.ClonaMazzo();
				//devo saltare il turno e quindi richiamare min sempre sull'ultima carta giocata
				int valRestituitoAzioneGiocata = this.TurnoDiMin(carteInManoMin, carteInManoMax, carteInTavola, mazzoCarteTotali, alfa, beta, isMinimax, livello, ultimoColoreValido); 
                //restituisco alle liste le loro posizioni originali
                this.CopiaListaCarte(carteInTavola, carteInTavolaCopia);
                this.CopiaListaCarte(carteInManoMin, carteInManoMinCopia);
                this.CopiaListaCarte(carteInManoMax, carteInManoMaxCopia);
                //this._coloreDiGioco = coloreAttuale;
                mazzoCarteTotali.ResetMazzo(mazzoCarteCopia);
                //controllo se sto facendo potatura alfa beta o no
                if(!isMinimax){//se � false sto facendo potatura quindi inserisco codice per potatura
                    if(valRestituitoAzioneGiocata >= beta){
                        return valRestituitoAzioneGiocata;
                    }
                    //alfa <- max(alfa,v)
                    if(valRestituitoAzioneGiocata >= alfa){
                        alfa = valRestituitoAzioneGiocata;
                    }
                }
				//dato che in questo giro non ho giocato nessuna carta e richiamato subito min restituisco subito che ha calcolato
				return valRestituitoAzioneGiocata;
			}else{
				//non serve la gestione di usata o meno quindi ritorno a false il controllo
				ultimaCartaGiocata.setUsata(false);
			}
		}
		//setto counter che mi permetter� di verificare quante carte giocabili avevo in mano
		int countNumeroCarteGiocate = 0;
		//setto valori ad un numero sicuramente inferiori a tutti quelli possibili che usciranno per usarlo come minimo
		int valori = -5000;
		for(int i = 0; i < carteInManoMax.size(); i++){
			//prima di giocare la carta devo verificare se questa carta � giocabile. Se la funzione ritorna true allora posso giocare la carta, se la funzione ritorna false non posso
			//giocare quella carta.
			if(this.CheckCartaGiocabile(carteInManoMax.get(i), ultimaCartaGiocata, carteInManoMax, ultimoColoreValido)){
	            //mi salvo carteInMAnoMin e carte in tavola per risistemarli al ritorno dalla ricorsione
	            List<Carta> carteInManoMinCopia = this.CloneListaCarte(carteInManoMin);
	            List<Carta> carteInTavolaCopia = this.CloneListaCarte(carteInTavola);
	            String coloreAttuale = ultimoColoreValido;
	            Mazzo mazzoCarteCopia = mazzoCarteTotali.ClonaMazzo();
	            //List<Carta> carteInManoMaxCopia = this.CloneListaCarte(carteInManoMax);
				//devo creare lo stato da passare a min. Quindi una alla volta gioco le carte
				carteInTavola.add(carteInManoMax.get(i));
				//la rimuovo da carte in mano
				Carta rimossa = carteInManoMax.remove(i);
                //se � un jolly devo cambiare colore
                if(rimossa.getColore()== "Jolly") ultimoColoreValido = this.ScegliColore(this._carteInMano, ultimoColoreValido);
                //se la carta giocata ha lo stesso simbolo ma � di colore diverso devo cambiare il colore di gioco
                if(rimossa.getColore() != ultimoColoreValido & rimossa.getTipocarta() == ultimaCartaGiocata.getTipocarta()) ultimoColoreValido = rimossa.getColore();
				//mi faccio restituire valore funzione obiettivo da min
				int valRestituitoAzioneGiocata = this.TurnoDiMin(carteInManoMin, carteInManoMax, carteInTavola, mazzoCarteTotali, alfa, beta, isMinimax, livello, ultimoColoreValido); 
				//assegno valore restituito a carta cos� alla fine so quale albero prendere
				rimossa.setValore(valRestituitoAzioneGiocata);
				//reinserisco carta rimossa per far quadrare i conti
				carteInManoMax.add(i, rimossa);
                //risistemo le liste salvate prima
                this.CopiaListaCarte(carteInTavola, carteInTavolaCopia);
                this.CopiaListaCarte(carteInManoMin, carteInManoMinCopia);
                //this.CopiaListaCarte(carteInManoMax, carteInManoMaxCopia);
                ultimoColoreValido = coloreAttuale;
                mazzoCarteTotali.ResetMazzo(mazzoCarteCopia);
				//ora devo assegnare a valori il malore massimo tra valori e valRestituitoAzioneGiocata
				//in valori � salvato sempre il valore massimio
				if(valRestituitoAzioneGiocata > valori){
					valori = valRestituitoAzioneGiocata;
				}
                //controllo se sto facendo potatura alfa beta o no
                if(!isMinimax){//se � false sto facendo potatura quindi inserisco codice per potatura
                    if(valRestituitoAzioneGiocata >= beta){
                        return valRestituitoAzioneGiocata;
                    }
                    //alfa <- max(alfa,v)
                    if(valRestituitoAzioneGiocata >= alfa){
                        alfa = valRestituitoAzioneGiocata;
                    }
                }
				//conto carte giocate da Max
				countNumeroCarteGiocate++;
			}
		}
		//se le carte giocate sono pari a 0 vuol dire che non ho giocato nessuna carta perch� non potevo e che quindi mi tocca pescare una carta
		if(countNumeroCarteGiocate == 0){
            return 0; //non ha senso cercare perch� tanto questa � l'unica azione disponibile
		}
		//ritorno valori (valore massimo funzione max)
		return valori;
	}
	//parte min di minimax
	private int TurnoDiMin(List<Carta> carteInManoMin, List<Carta> carteInManoMax, List<Carta> carteInTavola, Mazzo mazzoCarteTotali, int alfa, int beta, Boolean isMinimax, int livello, String ultimoColoreValido){
        //aumento di 1 il livello dell'albero
        livello++;
		//se questa � l'ultima azione da fare allora restituisci valore funzione obiettivo.
		//setto valore funzione obiettivo a -10 quando vince
		if(this.TerminatoAlbero(carteInManoMin)){
			return -100;
		}
		//controllo le carte gioabili perche se sono zero devo passare il turno e pescare una carta quindi richiamare il mio avversario sulla sua carta giocata
		//devo controllare anche se l'ultima carta giocata � stata un pi� due o pi� quattro o salta turno o inverti giro perch� io salto il turno e tocca di nuovo all'avversario
		Carta ultimaCartaGiocata = carteInTavola.get(carteInTavola.size() -1 );
        //se voglio limitare la profondit� ad un livello prefissato invece che esplorare il tutto utilizzo questo codice
        if(this.TestTaglio(livello)){
			return this.ValutaMossa(ultimaCartaGiocata, carteInManoMin, carteInManoMax, false);
		}
		//se � gi� stata usata (cio� l'avversario ha saltato il proprio turno e tocca a me) setto a false il controllo e proseguo tranquillamente nel gioco
		if(ultimaCartaGiocata.getUsata()){
			ultimaCartaGiocata.setUsata(false);
		}else{
			//se non � gi� stata usata la uso e la setto come usata (questo solo nel caso che la carta sia un +4 o cambia colore )
			ultimaCartaGiocata.setUsata(true);
			if(ultimaCartaGiocata.getColore() == "Jolly"){//se � un +4 o cambia colore cambio il colore e in caso pesco le carte
				if(ultimaCartaGiocata.getTipocarta() == "+4"){ //se � +4 pesco 4 carte
					for(int i = 0 ; i < 4; i++){
                        //potrei ritornare null perch� il mazzo � finito
                        Carta cartaPescata = this.PescaCartaDalMazzo();
                        if(cartaPescata != null){
                            carteInManoMin.add(cartaPescata);
                        }else{
                            //ritorna null. vuol dire che il mazzo � finito. devo vedere chi ha vinto. 
                            //vince chi ha meno carte in mano
                            return this.CheckNumeroCarteInManoAllaFineDelMazzo(false);
                        }
					}
				}
			}
			//ora devo controllare se � un +2 cos� pesco 2 carte
			if(ultimaCartaGiocata.getTipocarta() == "+2"){ //se � +4 pesco 4 carte
				for(int i = 0 ; i < 2; i++){
                    //potrei ritornare null perch� il mazzo � finito
                    Carta cartaPescata = this.PescaCartaDalMazzo();
                    if(cartaPescata != null){
                        carteInManoMin.add(cartaPescata);
                    }else{
                        //ritorna null. vuol dire che il mazzo � finito. devo vedere chi ha vinto. 
                        //vince chi ha meno carte in mano
                        return this.CheckNumeroCarteInManoAllaFineDelMazzo(false);
                    }
				}
			}
			//devo controllare se � una carta che causa il salto di turno. Se lo � io non faccio nulla e salto il turno
			//se � una carta normale esco dall'if e proseguo con il gioco
			if(ultimaCartaGiocata.getCausaSaltoTurno()){
	            //mi salvo tutte le liste per risistemarle al ritorno dalla ricorsione
	            List<Carta> carteInManoMinCopia = this.CloneListaCarte(carteInManoMin);
	            List<Carta> carteInManoMaxCopia = this.CloneListaCarte(carteInManoMax);
	            List<Carta> carteInTavolaCopia = this.CloneListaCarte(carteInTavola);
	            //String coloreAttuale = this._coloreDiGioco;
	            Mazzo mazzoCarteCopia = mazzoCarteTotali.ClonaMazzo();
				//devo saltare il turno e quindi richiamare max sempre sull'ultima carta giocata
				int valRestituitoAzioneGiocata = this.TurnoDiMax(carteInManoMax, carteInManoMin, carteInTavola, mazzoCarteTotali, alfa, beta, isMinimax, livello, ultimoColoreValido); 
                //restituisco alle liste le loro posizioni originali
                this.CopiaListaCarte(carteInTavola, carteInTavolaCopia);
                this.CopiaListaCarte(carteInManoMin, carteInManoMinCopia);
                this.CopiaListaCarte(carteInManoMax, carteInManoMaxCopia);
                //this._coloreDiGioco = coloreAttuale;
                mazzoCarteTotali.ResetMazzo(mazzoCarteCopia);
				//dato che in questo giro non ho giocato nessuna carta e richiamato subito min restituisco subito che ha calcolato
				return valRestituitoAzioneGiocata;
			}else{
				//non serve la gestione di usata o meno quindi ritorno a false il controllo
				ultimaCartaGiocata.setUsata(false);
			}
		}
		//setto counter che mi permetter� di verificare quante carte giocabili avevo in mano
		int countNumeroCarteGiocate = 0;
		//setto valori ad un valore massimo superiore a tutti quelli possibili di uscita da max
		int valori = 5000;
		//passo tutte le possibili carte che posso giocare 
		for(int i = 0; i < carteInManoMin.size(); i++){
			//prima di giocare la carta devo verificare se questa carta � giocabile. Se la funzione ritorna true allora posso giocare la carta, se la funzione ritorna false non posso
			//giocare quella carta.
			if(this.CheckCartaGiocabile(carteInManoMin.get(i), ultimaCartaGiocata, carteInManoMin, ultimoColoreValido)){
                //mi salvo carteInMAnoMin e carte in tavola per risistemarli al ritorno dalla ricorsione
                List<Carta> carteInManoMaxCopia = this.CloneListaCarte(carteInManoMax);
                List<Carta> carteInTavolaCopia = this.CloneListaCarte(carteInTavola);
                String coloreAttuale = ultimoColoreValido;
                Mazzo mazzoCarteCopia = mazzoCarteTotali.ClonaMazzo();
                //List<Carta> carteInManoMinCopia = this.CloneListaCarte(carteInManoMin);
				//devo creare lo stato da passare a min. Quindi una alla volta gioco le carte
				carteInTavola.add(carteInManoMin.get(i));
				//la rimuovo da carte in mano
				Carta rimossa = carteInManoMin.remove(i);
                //se � un jolly devo cambiare colore
                if(rimossa.getColore() == "Jolly") ultimoColoreValido = this.ScegliColore(this._carteInMano, ultimoColoreValido);
                //se la carta giocata ha lo stesso simbolo ma � di colore diverso devo cambiare il colore di gioco
                if(rimossa.getColore() != ultimoColoreValido) ultimoColoreValido = rimossa.getColore();
				//mi faccio restituire valore funzione obiettivo da max
				int valRestituitoAzioneGiocata = this.TurnoDiMax(carteInManoMax, carteInManoMin, carteInTavola, mazzoCarteTotali, alfa, beta, isMinimax, livello, ultimoColoreValido); 
                                //assegno valore restituito a carta cos� alla fine so quale albero prendere
				rimossa.setValore(valRestituitoAzioneGiocata);
				//reinserisco carta rimossa per far quadrare i conti
				carteInManoMin.add(i, rimossa);
				//risistemo le liste salvate prima
                this.CopiaListaCarte(carteInTavola, carteInTavolaCopia);
                //this.CopiaListaCarte(carteInManoMin, carteInManoMinCopia);
                this.CopiaListaCarte(carteInManoMax, carteInManoMaxCopia);
                ultimoColoreValido = coloreAttuale;
                mazzoCarteTotali.ResetMazzo(mazzoCarteCopia);
				//ora devo assegnare a valori il malore minimo tra valori e valRestituitoAzioneGiocata
				//in valori � salvato sempre il valore minimo
				if(valRestituitoAzioneGiocata < valori){
					valori = valRestituitoAzioneGiocata;
				}
	            //controllo se sto facendo potatura alfa beta o no
	            if(!isMinimax){//se � false sto facendo potatura quindi inserisco codice per potatura
	                if(valRestituitoAzioneGiocata <= alfa){
	                    return valRestituitoAzioneGiocata;
	                }
	                //beta <- min(beta,v)
	                if(valRestituitoAzioneGiocata <= beta){
	                    beta = valRestituitoAzioneGiocata;
	                }
	            }
				//conto carte giocate da Max
				countNumeroCarteGiocate++;
			}
		}
		//se le carte giocate sono pari a 0 vuol dire che non ho giocato nessuna carta perch� non potevo e che quindi mi tocca pescare una carta
		if(countNumeroCarteGiocate == 0){
        	return 0;
		}
		//ritorno valori (valore minimo funzione min)
		return valori;
	}
	
	
	
	//funzione di terminazione. restituisce true se sono alla fine dell'albero
	private Boolean TerminatoAlbero(List<Carta> listaCarteInMano){
		if(listaCarteInMano.isEmpty()){
			return true; // se la lista delle carte in mano � 1 vuol dire che ho vinto e finito il gioco
		}else{
			return false;
		}
	}
        //test di taglio per non esplorare intero albero
        private Boolean TestTaglio(Integer livello){
		if(livello >= 10){
			return true; // se la lista delle carte in mano � 1 vuol dire che ho vinto e finito il gioco
		}else{
			return false;
		}
	}
        
	//funzione di valutazione carta da giocare
	private int ValutaMossa(Carta cartaGiocata, List<Carta> carteInManoIo, List<Carta> carteInManoAvversario, Boolean turnoDiMax){
        //proviamo funzione di valutazione effettuata contando carte giocabili da me e carte giocabili da avversario e sottraendo questi due valori. Il risultato sar� il valore restituito dalla funzione
        //calcolo per me
        int carteGiocabiliIo = 0;
        for(int i = 0; i < carteInManoIo.size(); i++){
            if(carteInManoIo.get(i).getColore() == "Jolly" ) carteGiocabiliIo++; //se sono +4 o cambia colore posso giocarli sempre senza problemi
            if(carteInManoIo.get(i).getColore() == cartaGiocata.getColore() ) carteGiocabiliIo++; //se le carte sono dello stesso colore della carta in campo posso giocarle
            if(carteInManoIo.get(i).getTipocarta() == cartaGiocata.getTipocarta() & carteInManoIo.get(i).getColore() != cartaGiocata.getColore()) carteGiocabiliIo++; //se le carte sono dello stesso simbolo ma colore diverso della carta in campo posso giocarle
        }
        //calcolo per avversario
        int carteGiocabiliAvversario = 0;
        for(int i = 0; i < carteInManoAvversario.size(); i++){
            if(carteInManoAvversario.get(i).getColore() == "Jolly" ) carteGiocabiliAvversario++; //se sono +4 o cambia colore posso giocarli sempre senza problemi
            if(carteInManoAvversario.get(i).getColore() == cartaGiocata.getColore() ) carteGiocabiliAvversario++; //se le carte sono dello stesso colore della carta in campo posso giocarle
            if(carteInManoAvversario.get(i).getTipocarta() == cartaGiocata.getTipocarta() & carteInManoAvversario.get(i).getColore() != cartaGiocata.getColore()) carteGiocabiliAvversario++; //se le carte sono dello stesso simbolo ma colore diverso della carta in campo posso giocarle
        }
        int sottrazioneCarteGiocabili = carteGiocabiliIo - carteGiocabiliAvversario;
        int sottrazioneNumeroCarte = - (carteInManoIo.size() - carteInManoAvversario.size());
        //ritorno la differenza tra i due valori
        int risultatofinale = sottrazioneNumeroCarte + sottrazioneCarteGiocabili;
        if(!turnoDiMax){
            risultatofinale = -risultatofinale;
        }
        //l'intuizione � che chi ha pi� carte giocabili compatibili con la carta in campo ha piu chance di vincere. ma dobbiamo anche contare quante carte in mano hanno le persone. chi ha
        //meno carte ha pi� possibilit� di vincere
        return risultatofinale;
	}	

	//sceglie la carta da giocare usando o il minimax o la potatura alfa beta
	public Carta GiocaCarta(String ColoreOriginario){
		//se l'ultima carta giocata in campo era un +2 o +4 non chiedo manco che azione fare, pesco le carte e passo il turno
		//utlima carta giocata
		Carta CimaMazzo = this._riferimentoGiocoUno.getMazzoTotale().CartaInCima();
                //se � gi� stata usata (cio� l'avversario ha saltato il proprio turno e tocca a me) setto a false il controllo e proseguo tranquillamente nel gioco
		if(CimaMazzo.getUsata()){
			//CimaMazzo.setUsata(false);
		}else{
            //se non � gi� stata usata la uso e la setto come usata (questo solo nel caso che la carta sia un +4 o cambia colore )
            CimaMazzo.setUsata(true);
            if(CimaMazzo.getColore() == "Jolly"){//se � un +4 o cambia colore cambio il colore e in caso pesco le carte
                if(CimaMazzo.getTipocarta() == "+4"){ //se � +4 pesco 4 carte
                        for(int i = 0 ; i < 4; i++){
                                this._carteInMano.add(this.PescaCartaDalMazzo());
                        }
                }
            }
            //ora devo controllare se � un +2 cos� pesco 2 carte
            if(CimaMazzo.getTipocarta() == "+2"){ //se � +4 pesco 4 carte
                    for(int i = 0 ; i < 2; i++){
                            this._carteInMano.add(this.PescaCartaDalMazzo());
                    }
            }
            //devo controllare se � una carta che causa il salto di turno. Se lo � io non faccio nulla e salto il turno
            //se � una carta normale esco dall'if e proseguo con il gioco
            if(CimaMazzo.getCausaSaltoTurno()){
                    //ritorno null cosi passo il turno
                    return null;
            }else{
                //non serve la gestione di usata o meno quindi ritorno a false il controllo
                CimaMazzo.setUsata(false);
            }
        }
                
		Carta cartaSelezionata = null;
                
        try{
            //ottengo al carta da giocare true=minimax, false=potatura alfabeta
            cartaSelezionata = this.AlgoritmoCartaScelta(this._riferimentoGiocoUno.getTipologiaAlgoritmo(), ColoreOriginario);
        }catch(Exception ex){ //lascio leccezione perch� se finisco il mazzo potrei lanciare un eccezione che non ho ancora trovato da dove viene lanciata, ma
            //il programma funziona correttamente perch� termina senza problemi
            System.out.println("errore nel generare carte dall'algoritmo");
        }
		//se carta selezionata � null vuol dire che salto il turno volontariamente e quindi pesco una carta
		if(cartaSelezionata != null){
            //stampo valore carta in debug
            System.out.println(this.getNome() + " " + cartaSelezionata.getTipocarta() + " " + cartaSelezionata.getColore());
			//lo rimuovo dalle carte in mano
			this.getCarteInMano().remove(cartaSelezionata);
            //se � un jolly devo cambiare colore
            if(cartaSelezionata.getColore()== "Jolly"){ 
                String coloreScelto = this.ScegliColore(this._carteInMano, ColoreOriginario);
                this._riferimentoGiocoUno.setColoreCartaInGioco(coloreScelto);
                //mostro colore scelto
                this._riferimentoGiocoUno.getControllerGioco().avvisoCambioColore(coloreScelto, false);
            }
            //se la carta giocata ha lo stesso simbolo ma � di colore diverso devo cambiare il colore di gioco
            if(cartaSelezionata.getColore() != ColoreOriginario & cartaSelezionata.getTipocarta().equals(CimaMazzo.getTipocarta())) this._riferimentoGiocoUno.setColoreCartaInGioco(cartaSelezionata.getColore());
			//ritorno questa carta
			return cartaSelezionata;
		}else{
	        //stampo valore carta in debug
	        System.out.println(this.getNome() + " pesca una carta");
			//pesco una carta
            //controllo che se ritorna null vuol dire che il mazzo � finito
            Carta cartaPescata = this.PescaCartaDalMazzo();
            if(cartaPescata != null){
                this._carteInMano.add(cartaPescata);
            }else{
                //mazzo finito, bisogna stabilire vincitore e bisogna far capire che il mazzo � finito
                Carta cartaSignificatoMazzoFinito = new Carta("NULL","NULL",false);
                return cartaSignificatoMazzoFinito;
            }
			
			//ritorno null cosi passo il turno
			return null;
		}
		
	}
	
	//clona mazzo di carte
	private List<Carta> CloneListaCarte(List<Carta> DaClonare){
		List<Carta> nuovaLista = new ArrayList<Carta>();
		//copio tutti gli elementi della lista
		for(int i = 0; i < DaClonare.size(); i++){
			Carta nuovaCarta = new Carta(DaClonare.get(i).getTipocarta(),DaClonare.get(i).getColore(),DaClonare.get(i).getCausaSaltoTurno(),DaClonare.get(i).getUsata(), DaClonare.get(i).getValore());
			nuovaLista.add(nuovaCarta);
		}
		return nuovaLista;
	}
	//copia mazzo clonato in quello originale
    private void CopiaListaCarte(List<Carta> Destinazione, List<Carta> Origine){
        Destinazione.clear();
        for(int i = 0; i < Origine.size(); i++){
                Destinazione.add(Origine.get(i));
        }
    }
        
	//generare carte in mano all'avversario
	private List<Carta> GeneraCarteAvversario(Integer numeroCarte, Mazzo mazzoCarte){
		List<Carta> listaCarte = new ArrayList<Carta>();
		for(Integer i = 0; i < numeroCarte ; i++){
            //genero randomicamente una carta da quelle presenti nel mazzo
            if(mazzoCarte.getCarteDelMazzo().size() > 0){
                int randomNum = 0 + (int)(Math.random() * mazzoCarte.getCarteDelMazzo().size() -1 ); 
                listaCarte.add(mazzoCarte.getCarteDelMazzo().remove(randomNum));
            }
                        
		}
		return listaCarte;
	}
	
	//scegli colore dominante tra le carte in mano per cambiare colore con cambia colore
	public String ScegliColore(List<Carta> carteInMano, String vecchioColore){
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
        //se non ce ne � uno predominante trovo i due predominanti e ne ritorno uno a caso
		if(rosso == blu & rosso > giallo & rosso > verde) return "rosso";
        if(rosso == giallo & rosso > blu & rosso > verde) return "giallo";
        if(rosso == verde & rosso > blu & rosso > giallo) return "rosso";
        if(blu == giallo & blu > rosso & blu > verde) return "blu";
        if(blu == verde & blu > rosso & blu > giallo) return "blu";
        if(giallo == verde & giallo > blu & giallo > rosso) return "verde";
		//se nessun colore domina lo scelgo random ma diverso da quello precedente
		Integer randomNum = 0 + (int)(Math.random()*100); 
		if(randomNum < 25 ) return "rosso" != vecchioColore ? "rosso" : "blu";
		if(randomNum < 50 ) return "blu" != vecchioColore ? "blu" : "rosso";
		if(randomNum < 75 ) return "giallo" != vecchioColore ? "giallo" : "verde";
		return "verde" != vecchioColore ? "verde" : "giallo";
	}
	
	//dato risultato minimax sceglie carta da giocare. Se pi� carte hanno stesso valore scelgo quale usare in base euristica prescelta -> prima a sinistra
	private Integer CartaSceltaMinimax(List<Carta> carteInMano, Integer valore, List<Carta> carteInTavola, String ultimoColoreValido){
		//conto le carte che possono essere giocate
		int counterCarteGiocabili = 0; //counter
		Carta cartaInTavolo = carteInTavola.get(carteInTavola.size() -1 );//carta sul tavolo
		for(int i = 0; i < carteInMano.size(); i++){
			if(this.CheckCartaGiocabile(carteInMano.get(i), cartaInTavolo, carteInMano, ultimoColoreValido)){
				counterCarteGiocabili++;
			}
		}
		//se sono zero devo pescare e saltare il turno
		if(counterCarteGiocabili == 0){
			return null;
		}else{
			//cerco la prima carta con valore uguale al valore ritornato
            Boolean exit = false;
            int i = -1;
            while(i < carteInMano.size() & !exit){
                i++;
                if(this.CheckCartaGiocabile(carteInMano.get(i), cartaInTavolo, carteInMano, ultimoColoreValido)){
                    if(carteInMano.get(i).getValore() == valore) exit = true;
                }
            }
            //trovato la carta con quel valore la restituisco
			return i;
		}
	}
        
        //controlla chi ha meno carte per stabilire che ha vinto nel caso che il mazzo abbia finito le carte
        private Integer CheckNumeroCarteInManoAllaFineDelMazzo(Boolean maxOrMinEqualMax){
            //carte in mano al giocatore attuale
            int numeroCarteGiocatoreAttuale = this._carteInMano.size();
            //carte in mano al giocatore avversario
            int numeroCarteGiocatoreAvversario = 0;
            List<Object> listaGiocatori = this._riferimentoGiocoUno.getListaGiocatori();
            //so che ci son due giocatori quindi cerco semplicemente l'altro
            String mioNome = this.getNome();
            if(listaGiocatori.get(0).getClass() == Giocatore.class){
                numeroCarteGiocatoreAvversario = ((Giocatore)listaGiocatori.get(0)).getCarteInMano().size();
            }else{
                if(listaGiocatori.get(1).getClass() == Giocatore.class){
                    numeroCarteGiocatoreAvversario = ((Giocatore)listaGiocatori.get(0)).getCarteInMano().size();
                }else{
                    String nome = ((GiocatoreCPU)listaGiocatori.get(0)).getNome();
                    if(nome != mioNome){
                        numeroCarteGiocatoreAvversario = ((GiocatoreCPU)listaGiocatori.get(0)).getCarteInMano().size();
                    }else{
                        numeroCarteGiocatoreAvversario = ((GiocatoreCPU)listaGiocatori.get(1)).getCarteInMano().size();
                    }
                }
            }
            //controllo chi ha meno carte
            if(maxOrMinEqualMax){
                if(numeroCarteGiocatoreAttuale < numeroCarteGiocatoreAvversario){
                    return 100;
                }
                return -100;
            }else{
                if(numeroCarteGiocatoreAttuale < numeroCarteGiocatoreAvversario){
                    return -100;
                }
                return 100;
            }
            
        }
        
}