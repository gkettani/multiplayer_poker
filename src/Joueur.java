import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.*;
import java.net.*;

public class Joueur extends JFrame implements ActionListener{
	private ArrayList<Cartes> cartesJoueur = new ArrayList<Cartes>();
	private ArrayList<Cartes> listeCartesCentrales = new ArrayList<Cartes>();
	private Cartes[] cartesCentrales = new Cartes[5];
	private String nomJoueur;
	private int jetons ;
	private int playerID;
	private ArrayList<String> nomAdversaires = new ArrayList<String>();
	private ArrayList<Integer> jetonsAdversaires = new ArrayList<Integer>();

	private int miseActuelle = 0;
	private int blinde;
	private int miseJoueur = 0;
	private int pot = 0;
	private int nbJoueur;

	private int currentPlayer;
	private int numTour;

	private JButton soundOnBtn;
	private JButton soundOffBtn;
	private SoundEffect music;
	private SoundEffect son;

	private ConnexionJoueur cj;
	private Container contentPane;
	private JTextArea filActu;
	private JTextField mise;
	private JScrollPane sp;
	private JTextField txt;
	private JButton b1;  	 //Bouton suivre
	private JButton b2;  	 //Bouton relancer
	private JButton b3;  	 //Bouton faire tapis
	private JButton b4;  	 //Bouton se coucher

	private ArrayList<PlayerPanel> listPanel;
	private JLabel tableFond;
	private JLabel jetonsIcon;
	private JLabel wallpaper;
	private boolean buttonsEnabled;
	private Image imgEnd;
	private boolean finPartie = false;
	private Timer monTimer;

	static Scanner sc = new Scanner(System.in);

	public Joueur(String nomJoueur){
		this.nomJoueur = nomJoueur;
		contentPane = this.getContentPane();
		txt = new JTextField();
		filActu = new JTextArea();
		mise = new JTextField();
		sp = new JScrollPane(filActu);
		tableFond = new JLabel(scaleImage("../images/fond2.png",640,620));
		jetonsIcon = new JLabel(scaleImage("../images/jetons.png",70,70));
		wallpaper = new JLabel(scaleImage("../images/wallpaper.png",1280,850));
		son = new SoundEffect();
		music = new SoundEffect();
		music.setFile("../sons/backgroundMusic.wav");
	}
	
	public void setupIHM(){
		this.setSize(1280, 850);
		this.setTitle("Poker - CAS'INSA");
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tableFond.setBounds(265,75,650,650);
		jetonsIcon.setBounds(500,425,100,100);
		wallpaper.setBounds(0,0,1280,850);

		mise.setBounds(470,500,600,70);
		mise.setBackground(new Color(0,0,0,0));
		mise.setBorder(null);
		mise.setOpaque(false);
		mise.setForeground(Color.white);
		mise.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 20));
		mise.setText("Vous devez miser $ " + blinde +" pour suivre");

		txt.setBounds(590,435,200,100);
		txt.setText("  $ " + pot );
		txt.setOpaque(false);
		txt.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 25) );
		txt.setEditable(false);
		txt.setBackground(new Color(0,0,0,0));
 		txt.setBorder(null);
		txt.setForeground(new Color(255,255,255));

		filActu.setEditable(false);
		filActu.setBackground(new Color(24,30,54));
		filActu.setForeground(Color.white);
		filActu.setBorder(null);
		filActu.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 16));
		sp.setBounds(980,90,270,200);
		sp.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));

		JLabel suivreTxtLabel = new JLabel("Suivre");
		suivreTxtLabel.setBounds(30,70,70,20); 
		suivreTxtLabel.setForeground(new Color(255,255,255));
		suivreTxtLabel.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 17));

		JLabel relancerTxtLabel = new JLabel("Relancer");
		relancerTxtLabel.setBounds(115,70,70,20); 
		relancerTxtLabel.setForeground(new Color(255,255,255));
		relancerTxtLabel.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 17));

		JLabel tapisTxtLabel = new JLabel("Tapis");
		tapisTxtLabel.setBounds(225,70,70,20); 
		tapisTxtLabel.setForeground(new Color(255,255,255));
		tapisTxtLabel.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 17));

		JLabel seCoucherTxtLabel = new JLabel("Se coucher");
		seCoucherTxtLabel.setBounds(320,70,100,20); 
		seCoucherTxtLabel.setForeground(new Color(255,255,255));
		seCoucherTxtLabel.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 17));

		b1 = new JButton(scaleImage("../icons/suivre.png", 50, 50));
		b1.setBounds(30,15,50,50);
		b1.setBorder(null);
		b1.setContentAreaFilled(false);
		b1.setRolloverIcon(scaleImage("../icons/suivre1.png", 50,50 ));
		b1.setPressedIcon(scaleImage("../icons/suivre2.png", 50,50 ));
		b1.addActionListener(this);

		b2 = new JButton(scaleImage("../icons/relancer.png", 50, 50));
		b2.setBounds(120,15,50,50);
		b2.setBorder(null);
		b2.setContentAreaFilled(false);
		b2.setRolloverIcon(scaleImage("../icons/relancer1.png", 50,50 ));
		b2.setPressedIcon(scaleImage("../icons/relancer2.png", 50,50 ));
		b2.addActionListener(this);

		b3 = new JButton(scaleImage("../icons/tapis.png", 50, 50));
		b3.setBounds(220,15,50,50);
		b3.setBorder(null);
		b3.setContentAreaFilled(false);
		b3.setRolloverIcon(scaleImage("../icons/tapis1.png", 50,50 ));
		b3.setPressedIcon(scaleImage("../icons/tapis2.png", 50,50 ));
		b3.addActionListener(this);

		b4 = new JButton(scaleImage("../icons/se_coucher.png", 50, 50));
		b4.setBounds(330,15,50,50);
		b4.setBorder(null);
		b4.setContentAreaFilled(false);
		b4.setRolloverIcon(scaleImage("../icons/se_coucher1.png", 50,50 ));
		b4.setPressedIcon(scaleImage("../icons/se_coucher2.png", 50,50 ));
		b4.addActionListener(this);

		soundOnBtn = new JButton(scaleImage("../icons/sound.png", 50, 50));
		soundOnBtn.setBounds(1183,9,50,50);
		soundOnBtn.setBorder(null);
		soundOnBtn.setContentAreaFilled(false);
		soundOnBtn.setVisible(false);
		soundOnBtn.addActionListener(this);

		soundOffBtn = new JButton(scaleImage("../icons/sound_off.png", 50, 50));
		soundOffBtn.setBounds(1183,9,50,50);
		soundOffBtn.setBorder(null);
		soundOffBtn.setContentAreaFilled(false);
		soundOffBtn.addActionListener(this);

		JPanel actionPanel = new RoundedPanel(null, 100); //Correspond à un panel contenant les boutons ddes differentes action
		actionPanel.setBounds(820,660,430,100);
		actionPanel.setBackground(new Color(45,55,84));
		actionPanel.setForeground (new Color(189,170,70,100));

		JPanel shadowPanel = new RoundedPanel(null, 100);   //Correspond à un panel noir pour avoir un effet  
		shadowPanel.setBounds(823,665,430,100);			    // d'ombre sur le panel des actions		
		shadowPanel.setBackground(new Color(0,0,0,100));
		shadowPanel.setForeground(new Color(189,170,70,70));

		actionPanel.add(b1);
		actionPanel.add(b2);
		actionPanel.add(b3);
		actionPanel.add(b4);
		actionPanel.add(suivreTxtLabel);
		actionPanel.add(relancerTxtLabel);
		actionPanel.add(tapisTxtLabel);
		actionPanel.add(seCoucherTxtLabel);

		contentPane.setLayout(null);
		contentPane.setBounds(0,0,1280,800);
		contentPane.setBackground(new Color(24,30,54));
		listPanel = new ArrayList<PlayerPanel>();

		int id = playerID;

		//On crée ensuite autant de panel que de joueur chaque panel possede un id qui le relie au joueur
		for(int i=0; i<nbJoueur;i++){
			listPanel.add(new PlayerPanel(null, 20, i+1));          
			listPanel.get(i).setBackground(new Color(40,42,64));
			listPanel.get(i).setForeground(new Color(189,170,70,100));
			listPanel.get(i).setNom(nomAdversaires.get(i));
			listPanel.get(i).setJetons(jetonsAdversaires.get(i));

		}

		int comp = 0; 
		for(int i=0; i<nbJoueur; i++){      //On place les panels des adversaires autour de la table 
			if(id > nbJoueur) id = 1;		
			int rayon = 310;         
			double y = 370 + Math.cos(-2*comp*Math.PI/nbJoueur)*rayon;
			double x = 540 + Math.sin(-2*comp*Math.PI/nbJoueur)*rayon;
			listPanel.get(id-1).setBounds((int)x,(int)y,150,70);
			contentPane.add(listPanel.get(id-1));
			if(id != playerID){
				JLabel carteIcon = new JLabel(scaleImage("../images/back.png", 92, 105));
				carteIcon.setBounds((int)x+10,(int)y-55,95,105);
				contentPane.add(carteIcon);
			}
			id++;
			comp++;
		}

		contentPane.add(jetonsIcon);
		contentPane.add(txt);
		contentPane.add(mise);
		contentPane.add(tableFond);
		contentPane.add(actionPanel);
		contentPane.add(shadowPanel);
		contentPane.add(sp);
		contentPane.add(soundOnBtn);
		contentPane.add(soundOffBtn);
		contentPane.add(wallpaper);
		monTimer = new Timer(5500,this);

		this.setVisible(true);	

	}

	public void actionPerformed (ActionEvent e){
		if (e.getSource()== b4){
			txt.setText("  $ " + pot );
			cj.envoyerAction("SE_COUCHER");
			son.setFile("../sons/seCoucher.wav");
      		son.play();  
			buttonsEnabled = false;
			activerBoutons();
		}
        if (e.getSource()== b2){
          	int n = 0;
          	try{
          		n = Integer.parseInt(JOptionPane.showInputDialog(contentPane,"Vous relancez de combien ?", "Relancer", JOptionPane.INFORMATION_MESSAGE));
          	}catch(Exception ex){
          		System.out.println("Error");
          	}	
          	if( (n + miseJoueur > miseActuelle) && n <= jetons ){
          		cj.envoyerAction("RELANCER");
          		cj.envoyerAction(n+"");
          		son.setFile("../sons/coins.wav");
          		son.play();
          		jetons -= n;
          		miseJoueur += n ;
          		miseActuelle = miseJoueur;
				txt.setText("  $ " + pot );
          		buttonsEnabled = false;
				activerBoutons();
          	}else{
          		JOptionPane.showMessageDialog(this,"Veuillez rentrer un nombre valide!");
          	}
          	System.out.println(n);
        }
        if (e.getSource()== b3){
          	cj.envoyerAction("TAPIS");
          	son.setFile("../sons/coins.wav");
      		son.play();
          	cj.envoyerAction(jetons+"");
          	miseJoueur += jetons;
          	jetons = 0;
          	if(jetons + miseJoueur >= miseActuelle) miseActuelle = jetons + miseJoueur;
			txt.setText("  $ " + pot );
          	buttonsEnabled = false;
			activerBoutons();  	
        }
        if (e.getSource()== b1){
          	int n = miseActuelle - miseJoueur;
          	if(jetons >= n){
          		cj.envoyerAction("SUIVRE");
          		son.setFile("../sons/coins.wav");
          		son.play();
	      		if(n>0){
	      			cj.envoyerAction(n +"");
	      			jetons -= n;
	      			miseJoueur += n;
					txt.setText("  $ " + pot );
	      		}else{
	      			cj.envoyerAction("0");
	      		}
          		buttonsEnabled = false;
				activerBoutons();
          	}
        }
        if(e.getSource() == monTimer){
			finPartie = false;
			monTimer.stop();
			repaint();
		}
		if(e.getSource() == soundOnBtn){
			music.stop();
			soundOnBtn.setVisible(false);
			soundOffBtn.setVisible(true);
		}
		if(e.getSource() == soundOffBtn){
			music.play();
			music.loop();
			soundOnBtn.setVisible(true);
			soundOffBtn.setVisible(false);
		}
	}

	public String toString(){
		String res;
		res = "Le joueur "+nomJoueur +" possede "+jetons+"$ de jetons" +" ses cartes sont "+ cartesJoueur;
		return res;
		
	}

	public void connectToServer(String ip){
		cj = new ConnexionJoueur(ip);
	}

	public void activerBoutons(){  
		if(jetons !=0 || buttonsEnabled == false ){
			b4.setEnabled(buttonsEnabled);
			b3.setEnabled(buttonsEnabled);
			if(jetons > miseActuelle - miseJoueur || buttonsEnabled == false){ 
				b2.setEnabled(buttonsEnabled);	
				b1.setEnabled(buttonsEnabled);
			}
		}else if(jetons == 0){
			b4.setEnabled(buttonsEnabled);
		}
	}

	public void play(){
		Thread t = new Thread(new Runnable(){
			public void run(){
				while(true){
					cj.recevoirCartes();
					cj.recevoirCartesCentrales();
					son.setFile("../sons/shuffle.wav");
					son.play();
					miseActuelle = blinde;  //On fixe la grosse blinde en début de partie
					miseJoueur = 0;
					pot  = 0;
 					filActu.setText("                    Nouvelle partie \n" +"\n");
 					mise.setText("Vous devez miser $ " + blinde +" pour suivre");
					if(playerID == 1){
						buttonsEnabled = true;
					} else{
						buttonsEnabled = false;
					}
					txt.setText("  $ " + pot );
					activerBoutons();
					numTour = 1;
					while(true){
						cj.recevoirActionAdversaires();
						if(currentPlayer == playerID) buttonsEnabled = true;
						activerBoutons();
						if(numTour == 2 && listeCartesCentrales.size() != 3) {
							listeCartesCentrales.add(cartesCentrales[0]);
							listeCartesCentrales.add(cartesCentrales[1]);
							listeCartesCentrales.add(cartesCentrales[2]);
							repaint();
						}
						if(numTour == 3 && listeCartesCentrales.size() != 4) {
							listeCartesCentrales.add(cartesCentrales[3]);
							repaint();
						}
						if(numTour == 4 && listeCartesCentrales.size() != 5) {
							listeCartesCentrales.add(cartesCentrales[4]);
							repaint();
						}
						if(numTour == 5) {
							buttonsEnabled = false;
							activerBoutons();
							break;
						}
					}
					cj.recevoirInfoFinPartie();
					filActu.append("Fin de la partie \n");
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		});
		t.start();	
	}

	public void paint(Graphics g){
		super.paint(g);
		if(cartesJoueur.size() == 2){
			cartesJoueur.get(0).dessine(g, 430,690);
			cartesJoueur.get(1).dessine(g, 470,690);
		}
		for(int i=0; i<listeCartesCentrales.size(); i++){
			listeCartesCentrales.get(i).dessine(g,410 + i*80,320);
		}
		if(finPartie){
			BufferedImage imagePreparation = new BufferedImage(1300,900,BufferedImage.TYPE_INT_ARGB);
    		Graphics imagePreparationGraphics = imagePreparation.getGraphics();
    		imagePreparationGraphics.setColor(new Color(0,0,0,120));
    		imagePreparationGraphics.fillRect(0, 0, 1300, 900);
   			imagePreparationGraphics.drawImage(imgEnd,300,200,this);
			g.drawImage(imagePreparation,0,0,this);
		}
	}

	public ImageIcon scaleImage(String iconPath, int w, int h){
 
        ImageIcon imageIcon = new ImageIcon(iconPath); // On charge l'image en ImageIcon
		Image image = imageIcon.getImage();   // On transforme en Image 
		Image newimg = image.getScaledInstance(w,h,  java.awt.Image.SCALE_SMOOTH);  // on recadre l'image 
		imageIcon = new ImageIcon(newimg);  // On retransforme en ImageIcon
		return imageIcon;
    }


	private class ConnexionJoueur   {
		private Socket socket;
		private DataInputStream dataIn;
		private ObjectInputStream ois;
		private PrintWriter pw; 
		private BufferedReader bf;

		public ConnexionJoueur(String ip){
			System.out.println("-------------Joueur--------------");
			try{
			
				socket = new Socket(ip , 31031);
				dataIn = new DataInputStream(socket.getInputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				pw = new PrintWriter(socket.getOutputStream(), true);
				bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				playerID = dataIn.readInt();  //Lorsqu'un joueur se connecte on lui attribut un identifiant
				nbJoueur = dataIn.readInt();  //lecture du nombre de joueur
				jetons = dataIn.readInt();	  //lecture de la valeur des jetons
				blinde = dataIn.readInt(); 	  //lecture de la valeur de la blinde
				pw.println(nomJoueur);        //Envoi du nom du joueur
				System.out.println("Attente de connexion de tous les joueurs et lancement de la partie.");
				for(int i =0; i<nbJoueur; i++){
					nomAdversaires.add(bf.readLine());  //lecture du nom des adversaires
					jetonsAdversaires.add(jetons);		//lecture des jetons des adversaires
				}
				cartesJoueur = new ArrayList<Cartes>();
				filActu.append("Bonjour " + nomJoueur +"\n" );//+ "Connecte au serveur en tant que joueur #" + playerID +"\n");
				filActu.append("Vous avez " + jetons + " jetons \n");
				filActu.append("La valeur de la blinde est : "+blinde+"\n");
				currentPlayer = 1;
			}catch(IOException ex ){
				System.out.println("IOException from CJ constructor");
				JOptionPane.showMessageDialog(contentPane,"L'adresse IP est invalide ou la partie est pleine", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}

		//Methode qui permet d'envoyer au serveur une chaine de caractère
		public void envoyerAction(String str){
			pw.println(str);
		}

		//Methode qui permet de recevoir les cartes du joueur
		public void recevoirCartes(){  
			cartesJoueur.clear();
			try{
				cartesJoueur.add((Cartes) ois.readObject());
				cartesJoueur.add((Cartes) ois.readObject());
				 //On charge les images des cartes du joueurs 
				cartesJoueur.get(0).getImage();         
				cartesJoueur.get(1).getImage();
				repaint();
			}catch(Exception ex){
				System.out.println("IOException from recevoirCartes() in CJ");
				// ex.printStackTrace();
			}
		}

		//En début de partie le joueur recoit toutes les cartes centrales
		//qui seront affichées au fur et à mesure de l'avancement de la partie
		public void recevoirCartesCentrales(){
			listeCartesCentrales.clear();
			try{
				for(int i =0; i<5; i++){
					cartesCentrales[i] = (Cartes) ois.readObject();
					cartesCentrales[i].getImage();      //On charge les images des cartes centrales
				}
			}catch(Exception ex){
				System.out.println("Exception from recevoirCartesCentrales() in CJ");
			}
		}

		//Methode qui permet la reception des informations de fin de
		//la partie et qui met à jour les parametres de la partie
		public void recevoirInfoFinPartie(){
			try{
				String info = bf.readLine();      //Permet d'informer que la partie est finie et qu'on peut en relancer une nouvelle
				switch(info){
					case "GAGNANT":
						filActu.append("Vous avez gagne \n");
						jetons += Integer.parseInt(bf.readLine());
						listPanel.get(playerID-1).setJetons(jetons);
						imgEnd = scaleImage("../images/win.png",650,500).getImage();
						finPartie = true;
						monTimer.start();
						repaint();
						son.setFile("../sons/victoire.wav");
      					son.play();
						break;
					case "PERDANT":
						filActu.append("Vous avez perdu \n");
						int nbGagnant = Integer.parseInt(bf.readLine()); //On recupere le nombre de gagnant
						for(int i=0; i<nbGagnant;i++){
							int id =  Integer.parseInt(bf.readLine());
							filActu.append(nomAdversaires.get(id-1) + " a gagne avec "+ bf.readLine()+"\n");
							listPanel.get(id-1).setJetons(jetonsAdversaires.get(id-1)+pot); //màj des jetons du gagnant
						}
						imgEnd = scaleImage("../images/lose.png",650,500).getImage();
						finPartie = true;
						monTimer.start();
						repaint();
						break;
					default :
						break;
				}	
			} catch(IOException ex){
				System.out.println("IOException from recevoirInfoFinPartie() in CJ");
			}
		}

		public void recevoirActionAdversaires(){
			String tourSuivant = "";
			try{
				currentPlayer = Integer.parseInt(bf.readLine()); //On recupere l'id du joueur qui doit jouer
				int id = Integer.parseInt(bf.readLine()); //On recupere l'id du joueur qui vient de jouer
				jetonsAdversaires.set(id-1, Integer.parseInt(bf.readLine())); //On change les jetons du joueur
				listPanel.get(id-1).setJetons(jetonsAdversaires.get(id-1)); //On met à jour l'affichage des jetons du joueurs
				miseActuelle = Integer.parseInt(bf.readLine()); //on recupere la valeur de la mise actuelle
				pot =Integer.parseInt(bf.readLine());
				String str = bf.readLine();
				filActu.append(str + "\n");
				tourSuivant = bf.readLine(); // en fonction de tourSuivant on passe ou pas au tour suivant
				txt.setText("  $ " + pot );
				mise.setText("Vous devez miser $ " + (int)(miseActuelle-miseJoueur) +" pour suivre");
			} catch(IOException ex){
				System.out.println("IOException from recevoirActionAdversaires() in CJ");
			}

			switch(tourSuivant){
				case "NO":
					break;
				case "YES": //Si on passe au tour suivant
					numTour++;
					break;
				case "END": //Si le dernier tour est fini
					numTour =5;
				default:
					break;
			}
		}

	}
}
