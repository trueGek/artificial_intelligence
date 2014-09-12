package model;

import java.util.ArrayList;
import java.util.List;
import controller.Gioco;

public class GiocoUno {
	private List<Object> _listaGiocatori;
	private Mazzo _mazzoTotale;
	private Boolean _thereIsHumanPlayer;
    private Boolean _tipologiaAlgoritmo = false;
    private String _coloreCartaInGioco = "";
    private Boolean _giocoFinito = false;
    private Gioco _controller;
	
	//getter e setter
	public Boolean getTipologiaAlgoritmo() {
		return _tipologiaAlgoritmo;
	}

	public void setTipologiaAlgoritmo(Boolean _tipologiaAlgoritmo) {
		this._tipologiaAlgoritmo = _tipologiaAlgoritmo;
	}

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
        
    public String getColoreCartaInGioco() {
		return _coloreCartaInGioco;
	}

	public void setThereIsHumanPlayer(Boolean _thereIsHumanPlayer) {
		this._thereIsHumanPlayer = _thereIsHumanPlayer;
	}

    public void setColoreCartaInGioco(String _coloreCartaInGioco) {
		this._coloreCartaInGioco = _coloreCartaInGioco;
	}
        
	//agginge un giocatore alla lista
	public void AddGiocatore(Object giocatoreDaAggiungere){
		this._listaGiocatori.add(giocatoreDaAggiungere);
	}
        
    public Gioco getControllerGioco() {
		return _controller;
	}
        
    //costruttore classe gioco
    public GiocoUno(Gioco controller){
        this._listaGiocatori = new ArrayList<Object>();
        this._mazzoTotale = new Mazzo();
        this._thereIsHumanPlayer = false;
        this._controller = controller;
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
        //devo far pescare ad ogni giocatore una carta
        //la faccio pescare io la carta al giocatore umano senza che lui intervenga
        //faccio pescare ad ogni giocatore una carta
        for(int i = 0; i < this._listaGiocatori.size(); i++){
                ((Giocatore)this._listaGiocatori.get(i)).AggiungeUnaCartaAlMazzo();
        }
        //ora devo decidere quale � la carta pi� alta
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
                //se � carta azione assegno valore zero
                String valoreAttuale = listaValoreCartePescate.get(i);
                if(valoreAttuale == "+4" || valoreAttuale == "+2" || valoreAttuale == "cambia colore" || valoreAttuale == "inverti giro" || valoreAttuale == "stop"){
                        listaPunteggioCartePescate.add(0);
                }else{//altrimenti assegno il valore della carta
                        listaPunteggioCartePescate.add(Integer.parseInt(valoreAttuale));
                }
        }
        //ora devo trovare il valore pi� alto della lista
        int valoreAlto = -1;//assegno valore minimo di cui sicuramente qualcuno superer�
        for(int i = 0; i < listaPunteggioCartePescate.size(); i++){
                if(listaPunteggioCartePescate.get(i) > valoreAlto){
                        valoreAlto = listaPunteggioCartePescate.get(i);
                }
        }
        //trovo indice corrispondente a valore alto e quindi giocatore che inizier� per primo
        int indiceValoreAlto = listaPunteggioCartePescate.indexOf(valoreAlto);
        //riarrangio lista giocatori con nuovo ordine. Si parte da indice pi� alto e tutti gli altri a seguire
        List<Object> listaRiordinata = new ArrayList<Object>();
        for(int i = indiceValoreAlto; i< this._listaGiocatori.size(); i++){
                listaRiordinata.add(this._listaGiocatori.get(i));
        }
        for(int i = 0; i< indiceValoreAlto; i++){
                listaRiordinata.add(this._listaGiocatori.get(i));
        }
        //ora possiedo lista ordinata dell'ordine di gioco dei giocatori CPU
        //devo settare questa lista come lista giocatori
        this._listaGiocatori = listaRiordinata;
        //avviso il controller quale dei due iniza il gioco
        this._controller.avvisoInizioGioco(((Giocatore)this._listaGiocatori.get(0)).getNome());
	}
	
	//metodo utilizzato per iniziare il gioco
	public void IniziaGioco(){
		//chiedo se cpu vs cpu o cpu vs human
		//this._thereIsHumanPlayer = this.StubPresenzaGiocatoreUmano();
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
                //per� prima elimino le carte usate per stabilire l'ordine
		for(int i = 0; i < this._listaGiocatori.size(); i++){
			if(this._listaGiocatori.get(i).getClass() == Giocatore.class){
                                ((Giocatore)this._listaGiocatori.get(i)).EliminaCarteInMano();
				((Giocatore)this._listaGiocatori.get(i)).PescaCarteIniziali();
			}else{
                                ((GiocatoreCPU)this._listaGiocatori.get(i)).EliminaCarteInMano();
				((GiocatoreCPU)this._listaGiocatori.get(i)).PescaCarteIniziali();
			}
		}
        //metto una carta in cima al mazzo carte scoperte
        this._mazzoTotale.ScartaPrimaCartaInizioPartita();
        System.out.println(this._mazzoTotale.CartaInCima().getTipocarta() + " " + this._mazzoTotale.CartaInCima().getColore());
        //la mostro a view
        this._controller.mostraCartaMazzoScoperto(this._mazzoTotale.CartaInCima());
        //setto la variabile con il colore della carta attualmente in gioco
        this._coloreCartaInGioco = this._mazzoTotale.getCarteScartate().get(0).getColore();
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
        Boolean mazzoFinito = false;
		while(!partitaConclusa){
			for(int i = 0; i < this._listaGiocatori.size(); i++){
				if(!partitaConclusa){ //se finisce il giocatore uno non serve che il giocatore due giochi quindi dato che sono su un for non gli faccio fare nulla, cos� esce dal ciclo for e si ferma con il while uscendo.
					GiocatoreCPU giocatoreAttuale = ((GiocatoreCPU)this._listaGiocatori.get(i)); //riferimento giocatore attuale
                    //aggiungo alle carte in tavola la carta giocata dal gioatore cpu
                    Carta cartaDaGiocare = giocatoreAttuale.GiocaCarta(this._coloreCartaInGioco);
                    //se ritorna null vuol dire che devo pescare e passare il turno altrimenti devo giocare
                    if(cartaDaGiocare != null){
                        //devo verificare che il mazzo non sia finito
                        if(cartaDaGiocare.getTipocarta() == "NULL"){
                            //mazzo finito
                            mazzoFinito = true; //� finito il mazzo, finito il gioco
                            partitaConclusa = true; // setto a vero la variabile che ci far� uscire da tutti i cicli
                        }else{
                            //butto la carta
                            this._mazzoTotale.getCarteScartate().add(cartaDaGiocare);
                            //se rimango con una carta sola in mano dopo aver giocato questa carta devo dichiarare UNO
                            if(giocatoreAttuale.getCarteInMano().size() == 1){
                                    giocatoreAttuale.DichiaraUNO();
                                    System.out.println(giocatoreAttuale.getNome() + " UNO!");
                            }
                            //se invece rimango con zero carte ho vinto la partita e devo uscire dal ciclo while e dal ciclo for e dichiarare la vittoria
                            if(giocatoreAttuale.getCarteInMano().isEmpty()){
                                    giocatoreAttuale.DichiaraVittoria();
                                    System.out.println(giocatoreAttuale.getNome() + " Vittoria!");
                                    partitaConclusa = true; // setto a vero la variabile che ci far� uscire da tutti i cicli
                            }
                        }

					}
				}
			}
		}
	    //controllo se sono uscito per aver finito il mazzo o ho effettivamente vinto
	    if(mazzoFinito){//� finito il mazzo
	        this.CheckRisultatoConMazzoFinito();
	    }
		
	}
	
	public void ProseguiGiocoCasoGiocatoriAncheUmani(){
        //ora il giocatore umano si trova con le carte in mano, la carta sul tavolo e sa se tocca a lui o al pc ad iniziare a giocare
        //controllo se tocca ad umano o a pc ad inizare
        if(this._listaGiocatori.get(0).getClass() == GiocatoreCPU.class){
            //se il primo giocatore � il computer lo faccio giocare
            this.AzioneCPU((GiocatoreCPU)this._listaGiocatori.get(0));
        }else{
            //altrimenti segnalo che � il turno dell'umano a giocare
            this._controller.avvisaTurnoUmano();
        }
	}
	
        //azioni svolte dall'intelligenza artificiale durante il gioco
	private void AzioneCPU(GiocatoreCPU giocatoreAttuale){
        //aggiungo alle carte in tavola la carta giocata dal gioatore cpu
        Carta cartaDaGiocare = giocatoreAttuale.GiocaCarta(this._coloreCartaInGioco);
        //se ritorna null vuol dire che devo pescare e passare il turno altrimenti devo giocare
        if(cartaDaGiocare != null){
            //devo verificare che il mazzo non sia finito
            if(cartaDaGiocare.getTipocarta() == "NULL"){
                //mazzo finito
                this.CheckRisultatoConMazzoFinito();
            }else{
                //butto la carta
                this._mazzoTotale.getCarteScartate().add(cartaDaGiocare);
                //se rimango con una carta sola in mano dopo aver giocato questa carta devo dichiarare UNO
                if(giocatoreAttuale.getCarteInMano().size() == 1){
                        giocatoreAttuale.DichiaraUNO();
                        System.out.println(giocatoreAttuale.getNome() + " UNO!");
                }
                //se invece rimango con zero carte ho vinto la partita e devo uscire dal ciclo while e dal ciclo for e dichiarare la vittoria
                if(giocatoreAttuale.getCarteInMano().isEmpty()){
                        giocatoreAttuale.DichiaraVittoria();
                        System.out.println(giocatoreAttuale.getNome() + " Vittoria!");
                        //fermo il gioco
                        this._giocoFinito = true;
                }
            }
        }
        
    }
        
    //devo decidere chi ha vinto. Secondo le regole vince chi ha meno carte
    private void CheckRisultatoConMazzoFinito(){
        //devo decidere chi ha vinto. Secondo le regole vince chi ha meno carte
        int numeroCarteGiocatorePosizioneUno = ((Giocatore)this._listaGiocatori.get(0))._carteInMano.size();
        int numeroCarteGiocatorePosizioneDue = ((Giocatore)this._listaGiocatori.get(1))._carteInMano.size();
        if(numeroCarteGiocatorePosizioneUno < numeroCarteGiocatorePosizioneDue){
            //vince il giocatore in posizione 0
            ((GiocatoreCPU)this._listaGiocatori.get(0)).DichiaraVittoria("Vittoria per minor numero di carte!");
            System.out.println(((GiocatoreCPU)this._listaGiocatori.get(0)).getNome() + " Vittoria per minor numero di carte!");
        }else{
            if(numeroCarteGiocatorePosizioneUno > numeroCarteGiocatorePosizioneDue){
                //vince il giocatore in posizione 1
                ((GiocatoreCPU)this._listaGiocatori.get(1)).DichiaraVittoria("Vittoria per minor numero di carte!");
                System.out.println(((GiocatoreCPU)this._listaGiocatori.get(1)).getNome() + " Vittoria per minor numero di carte!");
            }else{
                //pareggio
                this._controller.dichiaraVittoria("", "Pareggio stesso numero di carte!");
                System.out.println("Pareggio stesso numero di carte!");
            }
        }
    }
	
    //azioni dell'umano. Butta carta
    public void ButtaCartaAzioneUmana(Carta cartaToltaDaLista){
        //ho la carta tolta dalla lista e devo inserirle nelle carte girate.
        //sistemo la carta sul mazzo scoperto
        this._mazzoTotale.getCarteScartate().add(cartaToltaDaLista);
        //aggiunta la carta del mazzo devo passare il turno e tocca al pc ora
        //prima di buttare devo controllare che se ho buttato un jolly devo decidere quale sar� il prossimo colore
        if(cartaToltaDaLista.getColore() == "Jolly"){
            this._coloreCartaInGioco = this._controller.sceltaColore();
        }
        //devo trovar el'altro giocatore
        //so per certo che si con solo 2 giocatori quindi � semplice la storia
        GiocatoreCPU altroGiocatore = null;
        if(this._listaGiocatori.get(0).getClass() == GiocatoreCPU.class){
            altroGiocatore = (GiocatoreCPU) this._listaGiocatori.get(0);
        }else{
            altroGiocatore = (GiocatoreCPU) this._listaGiocatori.get(1);
        }
        //ora che ho l'altro giocatore chiamo la sua mossa
        this.AzioneCPU(altroGiocatore);
        //fatta la sua mossa devo controllare se ha buttato giu carte che mi fanno saltare il turno o meno
        boolean risultato = this.controllaAzioneFattaDaCPUPrimaDiUmano();
        while(!risultato){
            //se � false l'umano passa il turno
            //avviso che salto il turno
            this._controller.avvisoSaltoTurno();
            //tocca di nuovo al pc
            this.AzioneCPU(altroGiocatore);
        }
    }
        
    //azione umano. Pesca carta e passa il turno
    public void PassaIlTurnoAzioneUmana(Carta cartaPescata){
        //se ritorna null vuol dire che il mazzo � finito
        if(cartaPescata != null){
            //aggiongo la carta al giocatore umano
            Giocatore giocatoreUmano = null;
            GiocatoreCPU altroGiocatore = null;
            if(this._listaGiocatori.get(0).getClass() == Giocatore.class){
                giocatoreUmano = (Giocatore) this._listaGiocatori.get(0);
                altroGiocatore = (GiocatoreCPU) this._listaGiocatori.get(1);
            }else{
                giocatoreUmano = (Giocatore) this._listaGiocatori.get(1);
                altroGiocatore = (GiocatoreCPU) this._listaGiocatori.get(0);
            }
            //aggiungo carta
            giocatoreUmano._carteInMano.add(cartaPescata);
            //faccio giocare il pc
            this.AzioneCPU(altroGiocatore);
            //fatta la sua mossa devo controllare se ha buttato giu carte che mi fanno saltare il turno o meno
            boolean risultato = this.controllaAzioneFattaDaCPUPrimaDiUmano();
            while(!risultato){
                //se � false l'umano passa il turno
                //avviso che salto il turno
                this._controller.avvisoSaltoTurno();
                //tocca di nuovo al pc
                this.AzioneCPU(altroGiocatore);
            }
        }else{
            //mazzo finito
            this.CheckRisultatoConMazzoFinito();
        }
    }
        
    public boolean controllaAzioneFattaDaCPUPrimaDiUmano(){
        //giocature umano
        Giocatore giocatoreUmano = null;
        //trovo riferimento giocatore umano
        if(this._listaGiocatori.get(0).getClass() == Giocatore.class){
            giocatoreUmano = (Giocatore) this._listaGiocatori.get(0);
        }else{
            giocatoreUmano = (Giocatore) this._listaGiocatori.get(1);
        }
        Carta CimaMazzo = this._mazzoTotale.CartaInCima();
        //se � gi� stata usata (cio� l'avversario ha saltato il proprio turno e tocca a me) setto a false il controllo e proseguo tranquillamente nel gioco
        if(CimaMazzo.getUsata()){
                CimaMazzo.setUsata(false);
                //posso giocare tranquillamente
                return true;
        }else{
            //se non � gi� stata usata la uso e la setto come usata (questo solo nel caso che la carta sia un +4 o cambia colore )
            CimaMazzo.setUsata(true);
            if(CimaMazzo.getColore() == "Jolly"){//se � un +4 o cambia colore cambio il colore e in caso pesco le carte
                if(CimaMazzo.getTipocarta() == "+4"){ //se � +4 pesco 4 carte
                        //devo avvisare che devo pescare 4 carte
                        this._controller.avvisoPescareNCarte(4);
                        for(int i = 0 ; i < 4; i++){
                            Carta cartaPescata = giocatoreUmano.PescaCartaDalMazzo();
                            if(cartaPescata != null) {
                                giocatoreUmano._carteInMano.add(cartaPescata);
                            }else{
                                //se � null ho finito carte del mazzo
                                this.CheckRisultatoConMazzoFinito();
                            }
                            
                        }
                }
            }
            //ora devo controllare se � un +2 cos� pesco 2 carte
            if(CimaMazzo.getTipocarta() == "+2"){ //se � +4 pesco 4 carte
                    this._controller.avvisoPescareNCarte(2);
                    for(int i = 0 ; i < 2; i++){
                        //devo avvisare che devo pescare due carte
                            Carta cartaPescata = giocatoreUmano.PescaCartaDalMazzo();
                            if(cartaPescata != null) {
                                giocatoreUmano._carteInMano.add(cartaPescata);
                            }else{
                                //se � null ho finito carte del mazzo
                                this.CheckRisultatoConMazzoFinito();
                            }
                    }
            }
            //devo controllare se � una carta che causa il salto di turno. Se lo � io non faccio nulla e salto il turno
            //se � una carta normale esco dall'if e proseguo con il gioco
            if(CimaMazzo.getCausaSaltoTurno()){
                    //ritorno false cosi passo il turno
                    return false;
            }else{
                //non serve la gestione di usata o meno quindi ritorno a false il controllo
                CimaMazzo.setUsata(false);
                return true;
            }
        }
    }
	
	//metodo da implementare come interfaccia grafica
	private String StubNomeGiocatoreUmano(){
		return "Umano";
	}
}