package model;

import java.util.ArrayList;
import java.util.List;

public class GiocoUno {
	private List<Object> _listaGiocatori;
	private Mazzo _mazzoTotale;
	private Boolean _thereIsHumanPlayer;

	//getter and setter
	public List<Object> getListaGiocatori() {
		return _listaGiocatori;
	}

	public void setListaGiocatori(List<Object> _listaGiocatori) {
		this._listaGiocatori = _listaGiocatori;
	}

	public Mazzo getMazzoTotale() {
		return _mazzoTotale;
	}

	public void setMazzoTotale(Mazzo _mazzoTotale) {
		this._mazzoTotale = _mazzoTotale;
	}
	
	public Boolean getThereIsHumanPlayer() {
		return _thereIsHumanPlayer;
	}

	public void setThereIsHumanPlayer(Boolean _thereIsHumanPlayer) {
		this._thereIsHumanPlayer = _thereIsHumanPlayer;
	}

	//agginge un giocatore alla lista
	public void AddGiocatore(Object giocatoreDaAggiungere){
		this._listaGiocatori.add(giocatoreDaAggiungere);
	}
	
	//genera le carte da uno
	public void GeneraCarte(){
		//creo carte rosse 
		this.GeneraMazziColoreCarte("rosso");
		//creo carte blu 
		this.GeneraMazziColoreCarte("blu");
		//creo carte giallo 
		this.GeneraMazziColoreCarte("giallo");
		//creo carte verde 
		this.GeneraMazziColoreCarte("verde");
		//creo carte cambia colore e jolly pesca quattro
		for(int i = 0; i < 4; i++){
			Carta cartaAppenaCreata = new Carta("+4", "Jolly", true);
			this._mazzoTotale.getCarteDelMazzo().add(cartaAppenaCreata);
			cartaAppenaCreata = new Carta("cambia colore", "Jolly", false);
			this._mazzoTotale.getCarteDelMazzo().add(cartaAppenaCreata);
		}
	}
	
	//sottometodo aiuto per generare carte
	private void GeneraMazziColoreCarte(String tipologia){
		//creo carte tipologia dall'1 al 9 per due mazzi
		for(int i = 0; i < 2; i++){
			for(int j = 1; j < 10; j++){
				Carta cartaAppenaCreata = new Carta(Integer.toString(j), tipologia, false);
				this._mazzoTotale.getCarteDelMazzo().add(cartaAppenaCreata);
			}
		}
		//aggiungo carta zero tipologia al mazzo (1 sola copia)
		Carta cartaAppenaCreata = new Carta("0", tipologia, false);
		this._mazzoTotale.getCarteDelMazzo().add(cartaAppenaCreata);
		//due carte +2 del colore tipologia, inverti giro e stop
		for(int i = 0; i < 2; i++){
			cartaAppenaCreata = new Carta("+2", tipologia, true);
			this._mazzoTotale.getCarteDelMazzo().add(cartaAppenaCreata);
			cartaAppenaCreata = new Carta("inverti giro", tipologia, true);
			this._mazzoTotale.getCarteDelMazzo().add(cartaAppenaCreata);
			cartaAppenaCreata = new Carta("stop", tipologia, true);
			this._mazzoTotale.getCarteDelMazzo().add(cartaAppenaCreata);
		}	
	}

	//metodo usato per scegliere ordine partenza giocatori
	public void ScegliOrdinePartenzaGioco(){
		//if(this._thereIsHumanPlayer){//se true sono presenti giocatori umani
			//devo far pescare ad ogni giocatore una carta
			
		//}else{//se false non sono presenti giocatori umani e quindi solo giocatori cpu
			//faccio pescare ad ogni giocatore una carta
			for(int i = 0; i < this._listaGiocatori.size(); i++){
				((Giocatore)this._listaGiocatori.get(i)).PescaCartaDalMazzo();
			}
			//ora devo decidere quale è la carta più alta
			//sapendo che ogni giocatore posiede una sola carta in mano creo una lista dove l'indice corrisponde al giocatore e l'elemento al tipo della carta
			//le carte azione valgono zero. Le carte numerate da 0 a 9 valgono da 0 a 9
			//creo lista appoggio dove salvo il tipo delle carte
			List<String> listaValoreCartePescate = new ArrayList<String>();			
			for(int i = 0; i < this._listaGiocatori.size(); i++){
				listaValoreCartePescate.add(((Giocatore)this._listaGiocatori.get(i)).getCarteInMano().get(0).getTipocarta());
			}
			//creo lista aiuto dove salvo il punteggio delle carte
			List<Integer> listaPunteggioCartePescate = new ArrayList<Integer>();
			for(int i = 0; i < listaValoreCartePescate.size(); i++){
				//se è carta azione assegno valore zero
				String valoreAttuale = listaValoreCartePescate.get(i);
				if(valoreAttuale == "+4" || valoreAttuale == "+2" || valoreAttuale == "cambia colore" || valoreAttuale == "inverti giro" || valoreAttuale == "stop"){
					listaPunteggioCartePescate.add(0);
				}else{//altrimenti assegno il valore della carta
					listaPunteggioCartePescate.add(Integer.parseInt(valoreAttuale));
				}
			}
			//ora devo trovare il valore più alto della lista
			int valoreAlto = -1;//assegno valore minimo di cui sicuramente qualcuno supererà
			for(int i = 0; i < listaPunteggioCartePescate.size(); i++){
				if(listaPunteggioCartePescate.get(i) > valoreAlto){
					valoreAlto = listaPunteggioCartePescate.get(i);
				}
			}
			//trovo indice corrispondente a valore alto e quindi giocatore che inizierà per primo
			int indiceValoreAlto = listaPunteggioCartePescate.indexOf(valoreAlto);
			//riarrangio lista giocatori con nuovo ordine. Si parte da indice più alto e tutti gli altri a seguire
			List<Object> listaRiordinata = new ArrayList<Object>();
			for(int i = indiceValoreAlto; i< this._listaGiocatori.size(); i++){
				listaRiordinata.add(this._listaGiocatori.get(i));
			}
			for(int i = 0; i< indiceValoreAlto; i++){
				listaRiordinata.add(this._listaGiocatori.get(i));
			}
			//ora possiedo lista ordinata dell'ordine di gioco dei giocatori CPU
	//	}
	}
	
	//metodo utilizzato per iniziare il gioco
	public void IniziaGioco(){
		//chiedo se cpu vs cpu o cpu vs human
		this._thereIsHumanPlayer = this.StubPresenzaGiocatoreUmano();
		if(this._thereIsHumanPlayer){
			//chiedere nome al giocatore
			String nomeGiocatoreUmano = this.StubNomeGiocatoreUmano();
			//creo il giocatore umano
			Giocatore giocatoreUmano = new Giocatore(nomeGiocatoreUmano, this);
			//aggiungo il giocatore umano alla lista
			this._listaGiocatori.add(giocatoreUmano);
			//creo il giocatore cpu nemico
			this.CreaGiocatoriCPU(1);
		}else{
			//creo due giocatori CPU per farli scontrare assieme
			this.CreaGiocatoriCPU(2);
		}
		//mescolo tutte le carte in gioco
		this._mazzoTotale.MescolaCarte();
		//ora bisogna scegliere l'ordine di partenza dei giocatori
		this.ScegliOrdinePartenzaGioco();
		//ora ho la lista di giocatori ordinata e posso iniziare effettivamente il gioco. faccio pescare ad ogni giocatore tutte le carte
		for(int i = 0; i < this._listaGiocatori.size(); i++){
			if(this._listaGiocatori.get(i).getClass() == Giocatore.class){
				((Giocatore)this._listaGiocatori.get(i)).PescaCarteIniziali();
			}else{
				((GiocatoreCPU)this._listaGiocatori.get(i)).PescaCarteIniziali();
			}
		}
	}
	
	private void CreaGiocatoriCPU(int numeroDaCreare){
		//creo numeroDaCreare giocatori cpu
		for(int i = 0; i < numeroDaCreare; i++){
			//creo il giocatore CPU
			String nome = "Giocatore-" + Integer.toString(i);
			Giocatore giocatoreCPU = new GiocatoreCPU(nome, this);
			//aggiungo il giocatore cpu alla lista
			this._listaGiocatori.add(giocatoreCPU);
		}
	}
	
	public void ProseguiGiocoCasoGiocatoriSoloCPU(){
		//azioni che tutti i giocatori devono effettuare
		Boolean partitaConclusa = false; // variabile d'appoggio 
		while(!partitaConclusa){
			for(int i = 0; i < this._listaGiocatori.size(); i++){
				if(!partitaConclusa){ //se finisce il giocatore uno non serve che il giocatore due giochi quindi dato che sono su un for non gli faccio fare nulla, così esce dal ciclo for e si ferma con il while uscendo.
					GiocatoreCPU giocatoreAttuale = ((GiocatoreCPU)this._listaGiocatori.get(i)); //riferimento giocatore attuale
					Boolean possoPassareIlTurno = false; // variabile d'appoggio per uscire dal ciclo while. Valida per gioco a 2 giocatori come deciso a priori
					while(!possoPassareIlTurno){
						//aggiungo alle carte in tavola la carta giocata dal gioatore cpu
						Carta cartaDaGiocare = giocatoreAttuale.GiocaCarta();
						//se ritorna null vuol dire che devo pescare e passare il turno altrimenti devo giocare
						if(cartaDaGiocare != null){
							//butto la carta
							this._mazzoTotale.getCarteScartate().add(cartaDaGiocare);
							//se rimango con una carta sola in mano dopo aver giocato questa carta devo dichiarare UNO
							if(giocatoreAttuale.getCarteInMano().size() == 1){
								giocatoreAttuale.DichiaraUNO();
							}
							//se invece rimango con zero carte ho vinto la partita e devo uscire dal ciclo while e dal ciclo for e dichiarare la vittoria
							if(giocatoreAttuale.getCarteInMano().isEmpty()){
								giocatoreAttuale.DichiaraVittoria();
								partitaConclusa = true; // setto a vero la variabile che ci farà uscire da tutti i cicli
							}
							//dato che il programma sviluppa solo due giocatori controllo se la carta giocata fa saltare il turno all'avversario
							//perchè in quel caso tocca ancora a me
							if(!cartaDaGiocare.getCausaSaltoTurno()){
								//non causa salta turno quindi tocca all'avversario e posso uscire dal while
								possoPassareIlTurno = true;
							}
							//fa saltare il turno all'avversaria quindi ritocca a me e quindi non esco dal ciclo
						}else{
							//pesco una carta e passo il turno
							giocatoreAttuale.PescaCartaDalMazzo();
							//passo il turno senza fare nulla e quindi uscire dal while
							possoPassareIlTurno = true;
						}
					}
				}
			}
		}
		
	}
	
	public void ProseguiGiocoCasoGiocatoriAncheUmani(){
		
	}
	
	
	
	
	//metodo da implementare come interfaccia grafica
	private Boolean StubPresenzaGiocatoreUmano(){
		return false;
	}
	//metodo da implementare come interfaccia grafica
	private String StubNomeGiocatoreUmano(){
		return "Alessandro";
	}
}