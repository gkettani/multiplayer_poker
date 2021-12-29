import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import java.awt.event.*;

public class GameServer implements ActionListener{

	private ServerSocket ss;
	private int playerID; //identifiant du joueur 
	private int nbJoueur; //nombre de joueur
	private ArrayList<Jeu> listJoueur;
	private int numTour;  // Numéro du tour (de 1 à 5)
	private String[] nomJoueurs ;
	private int[] jetonsJoueurs ;
	private int jetonsInitials;    // Jetons avec lesquels tous les joueurs commencent
	private int blinde = 0;  //Valeur de la blinde
	private Timer monTimer;

	private int compteur;    //A chaque fois qu'un joueur joue on incremente de 1 (Afin de s'assurer que chaque joueur a joue)
	private int nbJoueurTapis;
	private Paquet paquetInitial;			//Paquet classique de 52 cartes
	private int[] pointsJoueurs;     		 // contient les points des joueurs liés à la combinaison gagnante
	private int potTotal;					//Représente la somme des mises de tous les joueurs
	private int[] miseJoueur;				//Tableau qui contient la mise de chaque joueur
	private int miseActuelle ;			//Correspond à la somme que tous les joueurs doivent miser pour passer au tour suivant
	private ArrayList<Integer> idJoueurs;  	//Contient les identifiants des joueurs encore présents dans la partie (qui ne se sont pas couchés)
	private ArrayList<Integer> idGagnant;		//Contient les identifiants des gagnants en cas d'égalité parfaite
	private Cartes[][] tableauCartesJoueurs;	//Matrice de taille 2xnbJoueur contient les cartes de tous les joueurs (chaque joueur a 2 cartes)
	private Cartes[] cartesCentrales = new Cartes[5]; //Tableau des cartes centrales
	private boolean finPartie;                 	//Boolean qui vaut true si la partie est finie

	
	public GameServer(int nbJoueur, int blinde, int jetonsInitials){
		this.nbJoueur = nbJoueur;
		this.jetonsInitials = jetonsInitials;
		this.blinde = blinde;
		nomJoueurs = new String[nbJoueur];
		jetonsJoueurs = new int[nbJoueur];
		playerID = 0;
		paquetInitial = new Paquet("signe");
		paquetInitial.melanger();
		listJoueur = new ArrayList<Jeu>(nbJoueur);
		for (int i=0; i<nbJoueur; i++) {
			jetonsJoueurs[i] = jetonsInitials;         //On fixe une valeur de jetons par default pour tous les joueurs
		}
		try{
			ss = new ServerSocket(31031);         //On lance le serveur
		}catch(IOException ex){
			System.out.println("IOException from GameServer constructor");
		}

		monTimer = new Timer(5500,this);
	}

	//Methode qui permet d'accepter les joueurs dans la partie
	public void acceptConnections(){
		try{
			System.out.println("Attente de connection");
			while(playerID < nbJoueur){
				Socket s = ss.accept();
				playerID ++;
				System.out.println("Joueur #"+ playerID +" est connecte.");
				listJoueur.add(new Jeu(s, playerID));
				Thread t = new Thread(listJoueur.get(playerID-1));
				t.start();
			}
			System.out.println(nbJoueur + " joueurs sont connectes. Plus aucun joueur ne peut rejoindre la partie.");
		}catch(IOException ex){
			System.out.println("IOEXception from acceptConnections()");
		}
	}

	//methode qui permet de lancer la partie une fois tous les joueurs connectés
	public void startGame(){
		if(listJoueur.size() == nbJoueur){  //On s'assure que tous les joueurs ont rejoint la partie
			for(int i=0; i<nbJoueur; i++){
				for(int j=0; j<nbJoueur; j++){
					listJoueur.get(i).envoyerAction(nomJoueurs[j]);  //On envoie aux joueurs le nom de leurs adversaires
				}
			}
			//On attent un peu avant d'envoyer aux joueurs leurs cartes et de commencer la partie
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}

			for(int i=0; i<nbJoueur; i++){
				listJoueur.get(i).envoyerCartes();  //On envoie à chaque joueur ses cartes
			}
			finPartie = false;

		}
	}

	//Un timer est lancé une fois la partie finie pour pouvoir relancer 
	//directement une nouvelle partie
	public void actionPerformed (ActionEvent e){	
		if(e.getSource() == monTimer){  
			monTimer.stop();
			paquetInitial.melanger();
			for(int i=0; i<nbJoueur;i++){
				System.out.println("Envoyer au joueur #"+(int)(i+1));
				//Remise en place et initialisation de la partie et 
				// reenvoie à chaque joueur ses cartes pour lancer une nouvelle partie
				listJoueur.get(i).setup();  
				listJoueur.get(i).envoyerCartes();  
			}
		}
	}

	private class Jeu implements Runnable {

		private ArrayList<Cartes> mainJoueur;
		private int playerID;
		private Combinaison c;

		private int nextPlayer;
		private int previousPlayer;

		private Socket socket;
		private DataOutputStream dataOut;
		private ObjectOutputStream oos;
		private BufferedReader bf;
		private PrintWriter pw;


		public Jeu(Socket socket, int playerID){
			this.socket = socket;
			this.playerID = playerID;

			//On instancie les objets permettants d'envoyer et de recevoir des informations 
			try{
				dataOut = new DataOutputStream(socket.getOutputStream()); //permet d'envoyer un entier
				oos = new ObjectOutputStream(socket.getOutputStream());		//permet d'envoyer un objet quelconque
				pw = new PrintWriter(socket.getOutputStream(), true);   // permet d'envoyer une chaine de caractère
				bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // permet de lire une chaine de caractère
			}catch(IOException ex){
				System.out.println("IOException dans le constructeur de jeu ");
			}
			
		}

		@Override
		public void run(){
			try{
				dataOut.writeInt(playerID);          //Envoyer a chaque joueur son identifiant
				dataOut.writeInt(nbJoueur);		// Envoyer aux joueurs le nombre total de joueur
				dataOut.writeInt(jetonsInitials); // On envoie à tous les joueurs la valeur initiale des jetons
				dataOut.writeInt(blinde);   // Envoyer aux joueurs la valeur de la blindeoueur
				dataOut.flush();

				//A chaque fois qu'un joueur se connecte, on range son nom dans un tableau
				nomJoueurs[playerID-1] = bf.readLine();     
				System.out.println(jetonsJoueurs[playerID-1]);
			} catch(IOException ex){
				System.out.println("IOException dans run() de jeu ");
			}
			setup();
			while(true){
				finPartie = false;
				while(!finPartie){
					recevoirActionJoueur();
					updateTurn();
					if (numTour == 5) finPartie = true;
				}
				for(Integer i : idJoueurs){
					listJoueur.get(i-1).verifierCombinaison();
				}
				verifierGagnant();										//On compare les points des joueurs
				if(idGagnant.size()>1){										//S'il y a plusieur joueur avec la meme combinaison gagnante
					for(Integer i : idGagnant){
						//En cas d'égalité parfaite entre deux ou plusieurs joueurs on compare leurs mains
						//Et on additionne à leur points la valeur de leur carte haute
						listJoueur.get(i-1).comparerMainJoueurs(1);    
					}	
					verifierGagnant();	                             //On recompare les points des joueurs
				}													
				if(idGagnant.size()>1){                           //Si les joueurs on la meme carte haute on regarde leur seconde carte 
					for(Integer i : idGagnant){
						listJoueur.get(i-1).comparerMainJoueurs(0);       
					}	
					verifierGagnant();
				}
				String str = "";
				for(int i : idGagnant){
					str += nomJoueurs[i-1] + " gagne avec "+listJoueur.get(i-1).c.nom + "\n";
				}
				System.out.println(str);
				//Une fois la partie finie, on envoie à chaque joueur le détaille
				// de la fin de partie puis on met à jour la valeur des jetons du gagnant
				for(int i=1; i<=nbJoueur;i++){
					if(idGagnant.contains(i))  {
						listJoueur.get(i-1).envoyerAction("GAGNANT");
						listJoueur.get(i-1).envoyerAction(potTotal/idGagnant.size()+"");
						jetonsJoueurs[i-1] += potTotal/idGagnant.size();
					}
					else{
						listJoueur.get(i-1).envoyerAction("PERDANT");
						listJoueur.get(i-1).envoyerAction(idGagnant.size()+"");
						for(int j=0; j<idGagnant.size(); j++){
							listJoueur.get(i-1).envoyerAction(idGagnant.get(j)+"");
							listJoueur.get(i-1).envoyerAction(listJoueur.get(idGagnant.get(j)-1).c.nom);
						}	
					}
				}
				//Une fois la partie finie on lance un timer de 5 secondes avant de relancer
				monTimer.start();  
			}
		}

		public void setup(){     //Mise en place et initialisation des paramètres de la partie                             
			tableauCartesJoueurs = new Cartes[2][nbJoueur];
			for(int i=0; i < 2; i++){
				for(int j=0; j<nbJoueur; j++){
					//Distribution des cartes aux joueurs
					tableauCartesJoueurs[i][j] = paquetInitial.tableauDeCartes[j + i*nbJoueur];
				}
			}
			for(int i=0; i<5; i++){
				cartesCentrales[i] = paquetInitial.tableauDeCartes[2*nbJoueur + i];
			}
			//--------Initialisation de tous les paramètres de la partie----------
			miseJoueur = new int[nbJoueur];                   
			miseActuelle = blinde;
			potTotal = 0;
			compteur = 0;
			nbJoueurTapis = 0;
			numTour = 1;
			pointsJoueurs = new int[nbJoueur];
			idJoueurs = new ArrayList<Integer>();
			idGagnant = new ArrayList<Integer>();
			for(int i=1; i <= nbJoueur; i++){
				idJoueurs.add(i);
			}

			//Mise en place d'une liste doublement chainée des identifiants des joueurs,
			//chaque joueur connait l'identifiant du joueur précédent et suivant 
			if(playerID != nbJoueur){
				nextPlayer = playerID +1;
			}else{
				nextPlayer = 1;  //Le joueur qui suit le dernier joueur est le joueur 1
			}
			if(playerID != 1){
				previousPlayer = playerID -1;
			}else{
				previousPlayer = nbJoueur;//Le joueur qui précéde le premier joueur est le dernier joueur
			}
		}

		public void envoyerAction(String str){  //Methode pour envoyer une chaine de caracteres
			pw.println(str);
		}

		public void envoyerCartes(){   //Permet d'envoyer à chaque joueur ses cartes et les cartes centrales
			try{
				oos.writeObject(tableauCartesJoueurs[0][playerID-1]);
				oos.writeObject(tableauCartesJoueurs[1][playerID-1]);
				oos.writeObject(cartesCentrales[0]);
				oos.writeObject(cartesCentrales[1]);
				oos.writeObject(cartesCentrales[2]);
				oos.writeObject(cartesCentrales[3]);
				oos.writeObject(cartesCentrales[4]);
				oos.flush();
			}catch(IOException ex){
				System.out.println("IOException from envoyerCarte() in Jeu");
			}
		}

		public void recevoirActionJoueur(){
			try{
				String action = bf.readLine();  //retourne l'action du joueur (si il suit, se couche, relance ou fait tapis...)    
				int n = 0;
				compteur++; 
				String str = "";
				switch(action){
					case "RELANCER":
						n = Integer.parseInt(bf.readLine());   //On lit la somme que le joueur a misé pour relancer
						//on met à jour ensuite le pot, la mise actuelle et la mise du joueur
						miseJoueur[playerID-1] += n;
						jetonsJoueurs[playerID-1] -= n;
						miseActuelle = miseJoueur[playerID -1];
						potTotal += n;
						str = nomJoueurs[playerID-1] + " a relance de " + n;
						System.out.println(str);
						for(int i=0; i<nbJoueur; i++){
							//On envoie ensuite à chaque joueur :
							listJoueur.get(i).envoyerAction(nextPlayer+"");  //Identifiant du prochain joueur à jouer
							listJoueur.get(i).envoyerAction(playerID+"");    //Identifiant du joueur qui vient de jouer
							listJoueur.get(i).envoyerAction(jetonsJoueurs[playerID-1]+""); //Mise à jour des jetons du joueur
							listJoueur.get(i).envoyerAction(miseActuelle+"");  
							listJoueur.get(i).envoyerAction(potTotal+"");
							listJoueur.get(i).envoyerAction(str);
						}
						break;
					case "SUIVRE" :
						n += Integer.parseInt(bf.readLine());      //On lit la somme que le joueur a misé pour suivre
						//on met à jour ensuite le pot, la mise actuelle et la mise du joueur
						miseJoueur[playerID-1] += n;
						miseActuelle = miseJoueur[playerID -1];
						potTotal += n;
						jetonsJoueurs[playerID-1] -= n; 
						str = nomJoueurs[playerID-1] + " a suivi";
						System.out.println(str);
						for(int i=0; i<nbJoueur; i++){
							//On envoie ensuite à chaque joueur :
							listJoueur.get(i).envoyerAction(nextPlayer+"");  //Identifiant du prochain joueur à jouer
							listJoueur.get(i).envoyerAction(playerID+"");    //Identifiant du joueur qui vient de jouer
							listJoueur.get(i).envoyerAction(jetonsJoueurs[playerID-1]+""); //Mise à jour des jetons du joueur
							listJoueur.get(i).envoyerAction(miseActuelle+"");  
							listJoueur.get(i).envoyerAction(potTotal+"");
							listJoueur.get(i).envoyerAction(str);
						}
						break;
					case "SE_COUCHER":
						idJoueurs.remove((Integer) playerID);
						str = nomJoueurs[playerID-1] + " s'est couche" ;
						//On redefinit la liste doublement chainée des identifiants
						//des joueurs (lorsqu'un joueur se couche il ne peut plus jouer)
						listJoueur.get(previousPlayer-1).nextPlayer = nextPlayer;
						listJoueur.get(nextPlayer-1).previousPlayer = previousPlayer;
						System.out.println(str);
						for(int i=0; i<nbJoueur; i++){
							//On envoie ensuite à chaque joueur :
							listJoueur.get(i).envoyerAction(nextPlayer+"");  //Identifiant du prochain joueur à jouer
							listJoueur.get(i).envoyerAction(playerID+"");    //Identifiant du joueur qui vient de jouer
							listJoueur.get(i).envoyerAction(jetonsJoueurs[playerID-1]+""); //Mise à jour des jetons du joueur
							listJoueur.get(i).envoyerAction(miseActuelle+"");  
							listJoueur.get(i).envoyerAction(potTotal+"");
							listJoueur.get(i).envoyerAction(str);
						}
						compteur--;
						break;
					case "TAPIS":
						n = Integer.parseInt(bf.readLine());
						miseJoueur[playerID-1] += n; 
						jetonsJoueurs[playerID-1] = 0;
						potTotal += n;
						//On redefinit la liste doublement chainée des identifiants
						//des joueurs (lorsqu'un joueur fait tapis il ne joue plus )
						listJoueur.get(previousPlayer-1).nextPlayer = nextPlayer;
						listJoueur.get(nextPlayer-1).previousPlayer = previousPlayer;
						if(miseJoueur[playerID-1] >= miseActuelle) miseActuelle = miseJoueur[playerID-1];
						str = nomJoueurs[playerID-1] + " a fait tapis";
						System.out.println(str);
						for(int i=0; i<nbJoueur; i++){
							//On envoie ensuite à chaque joueur :
							listJoueur.get(i).envoyerAction(nextPlayer+"");  //Identifiant du prochain joueur à jouer
							listJoueur.get(i).envoyerAction(playerID+"");    //Identifiant du joueur qui vient de jouer
							listJoueur.get(i).envoyerAction(jetonsJoueurs[playerID-1]+""); //Mise à jour des jetons du joueur
							listJoueur.get(i).envoyerAction(miseActuelle+"");  
							listJoueur.get(i).envoyerAction(potTotal+"");
							listJoueur.get(i).envoyerAction(str);
						}
						compteur = 0;
						nbJoueurTapis++;
						break;
					default:
						break;
				}   

			}catch(IOException ex){
				System.out.println("IOException from recevoirActionJoueur() in Jeu");
			}
		}

		public void updateTurn(){  //Methode qui permet de vérifier si on peut passer au tour suivant
			int c =0;
			if(compteur >= idJoueurs.size()){  //On vérifie si tous les joueurs ont joué au moins une fois
				for(int i=0; i<miseJoueur.length; i++){
					if(miseJoueur[i] == miseActuelle){ //On vérifie si tous les joueurs ont misé la meme somme
						c++;
					}
				}
			}
			if(nbJoueurTapis == idJoueurs.size( ) || idJoueurs.size() <= 1 ){   //S'il ne reste aucun joueur (tous tapis ou couché)
				for(int  i=0; i<nbJoueur ;i++){
					listJoueur.get(i).envoyerAction("END");    //On informe de la fin de la partie 
					numTour = 5;
				}
			}

			else if(c >= idJoueurs.size() - nbJoueurTapis){ 

				for(int  i=0; i<nbJoueur ;i++){
					listJoueur.get(i).envoyerAction("YES");    //On informe les joueur qu'on passe au tour suivant 
				}
				numTour ++;
				compteur = 0;
			}else{
				for(int  i=0; i<nbJoueur ;i++){
					listJoueur.get(i).envoyerAction("NO");    //On informe les joueur qu'on ne passe pas au tour suivant 
				}
			} 

		}

		public void verifierCombinaison(){      //Methode qui verifie la combinaison de chaque joueur 
			c = new Combinaison("");
			mainJoueur = new ArrayList<Cartes>();
			mainJoueur.add(tableauCartesJoueurs[0][playerID-1]);
			mainJoueur.add(tableauCartesJoueurs[1][playerID-1]);
			ArrayList<Cartes> listeCombinaison = new ArrayList<Cartes>(); //liste qui contient les cartes du joueur + les cartes centrales
			listeCombinaison.add(tableauCartesJoueurs[0][playerID-1]);
			listeCombinaison.add(tableauCartesJoueurs[1][playerID-1]);
			for(int i=0; i<5; i++){
				listeCombinaison.add(cartesCentrales[i]);
			}
			while(true){ 
				//On vérifie les combinaisons par ordre de supériorité
				//Une fois une combinaison vérifiée on sort de la boucle
				if(c.quinteFlushRoyal(listeCombinaison)){
					c.nom = "QUINTE FLUSH ROYAL";
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
				c.points = 0;
				c.addAs(listeCombinaison);
				if(c.quinteFlush(listeCombinaison)){
					c.nom = "QUINTE FLUSH";
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
				c.points = 0;
				c.removeAs(listeCombinaison);
				if(c.carre(listeCombinaison)){
					c.nom = "CARRE";
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
				c.points = 0;
				if(c.full(listeCombinaison)){         
					c.nom = "FULL";
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
				c.points = 0;
				if(c.couleur(listeCombinaison)){
					c.nom = "COULEUR";
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
				c.points = 0;
				c.addAs(listeCombinaison);
				if(c.suite(listeCombinaison)){
					c.nom = "SUITE";
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
				c.points = 0;
				c.removeAs(listeCombinaison);
				if(c.brelan(listeCombinaison)){
					c.nom = "BRELAN";
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
				c.points = 0;
				if(c.doublePaires(listeCombinaison)){  
					c.nom = "DOUBLE PAIRES";
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
				c.points = 0;
				if(c.paire(listeCombinaison)){          
					c.nom = "PAIRE";
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
				c.points = 0;
				if(c.nom.equals("")){ 
					c.nom = "CARTE HAUTE";
					c.valeurCarteHaute(mainJoueur);
					pointsJoueurs[playerID-1] = c.points;
					break;
				}
			}
		}

		public void comparerMainJoueurs(int n){     //En cas d'egalite parfaite on compare la main des joueurs
			Collections.sort(mainJoueur);
			//on ajoute aux points du joueur la valeur de sa carte haute
			pointsJoueurs[playerID-1] += mainJoueur.get(n).valeur;
		}

		//Methode qui range dans la liste idGagnant les identifiants 
		//des joueurs possèdant le plus grand nombre de points 
		public void verifierGagnant(){  
			int pointMaxi = 0;
			for(Integer i : idJoueurs){
				if(pointsJoueurs[i-1] > pointMaxi){
					pointMaxi = pointsJoueurs[i-1];
					idGagnant.clear();                     
					idGagnant.add(i);
				} else if(pointsJoueurs[i-1] == pointMaxi){
					idGagnant.add(i);
				}
			}
		}
	}
}