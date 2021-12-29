import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.io.IOException;

public class Launcher extends JFrame implements ActionListener{

	private JButton lancerServeur;
	private JButton serverInfoBtn;
	private JButton partieInfoBtn;
	private JButton returnBtn;
	private JButton rejoindrePartie;
	private Container contentPane;
	private JPanel panelChoix;

	private JPanel serverInfoPanel;
	private JPanel partieInfoPanel;

	private JPanel playerPanel;
	private JTextField adresseIP;
	private JTextField nomJoueur;
	private JButton rejoindreBtn;

	private GameServer gs;

	private JPanel serverPanel;
	private JButton startBtn;
	private JButton launchBtn;
	private JComboBox<Integer> choixJetons;  
	private JTextField choixNbreJoueurs;
	private JTextField choixBlinde;
	private JTextArea infoTxt;

	public Launcher(){
		this.setSize(1280,780);
		this.setTitle("CASINSA - Poker");
		this.setLocation(350,150);
		this.setResizable(false);
		this.setVisible(true);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = this.getContentPane();

		JLabel logo = new JLabel(scaleImage("../images/logo.png",200,140));
		logo.setBounds(55,50,200,140);

		serverInfoBtn = new JButton("En savoir plus");
		serverInfoBtn.setBounds(0,349,250,55);
		serverInfoBtn.setBackground(new Color(31,27,31));
		serverInfoBtn.setForeground(new Color(189,170,70));
		serverInfoBtn.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		serverInfoBtn.addActionListener(this);

		partieInfoBtn = new JButton("En savoir plus");
		partieInfoBtn.setBounds(265,349,250,55);
		partieInfoBtn.setBackground(new Color(31,27,31));
		partieInfoBtn.setForeground(new Color(189,170,70));
		partieInfoBtn.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		partieInfoBtn.addActionListener(this);

		lancerServeur = new JButton(scaleImage("../images/server.png", 250, 350));
		lancerServeur.setRolloverIcon(scaleImage("../images/server1.png", 250,350 ));
		lancerServeur.setPressedIcon(scaleImage("../images/server2.png", 250,350 ));
		lancerServeur.setBounds(0,0,250,350);
		lancerServeur.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		lancerServeur.addActionListener(this);
		
		rejoindrePartie = new JButton(scaleImage("../images/player.png", 250, 350));
		rejoindrePartie.setRolloverIcon(scaleImage("../images/player1.png", 250,350 ));
		rejoindrePartie.setPressedIcon(scaleImage("../images/player2.png", 250,350 ));
		rejoindrePartie.setBounds(265,0,250,350);
		rejoindrePartie.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		rejoindrePartie.addActionListener(this);
		
		JLabel wallpaper = new JLabel(scaleImage("../images/wall.png",1240,715));
		wallpaper.setBounds(10,10,1240,715);

		returnBtn = new JButton(scaleImage("../icons/return.png", 50, 50));
		returnBtn.setBounds(1140,60,50,50);
		returnBtn.setBorder(null);
		returnBtn.setContentAreaFilled(false);
		returnBtn.setPressedIcon(scaleImage("../icons/return1.png",50,50 ));
		returnBtn.addActionListener(this);
		returnBtn.setVisible(false);

		panelChoix = new JPanel();
		panelChoix.setLocation(410,135);
		panelChoix.setSize(515,404);
		panelChoix.setBackground(new Color(31,27,31));
		panelChoix.setLayout(null);
		panelChoix.add(lancerServeur);
		panelChoix.add(rejoindrePartie);
		panelChoix.add(partieInfoBtn);
		panelChoix.add(serverInfoBtn);

		//-------------------------Création du panel pour le lancement du serveur------------------------------

		serverPanel = new JPanel();
		serverPanel.setLocation(410,135);
		serverPanel.setSize(515,404);
		serverPanel.setBackground(new Color(31,27,31));
		serverPanel.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		serverPanel.setLayout(null);
		serverPanel.setVisible(false);

		JTextArea titleServerPanel = new JTextArea();
		titleServerPanel.setText("Creer une partie");
		titleServerPanel.setBackground(new Color(31,27,31));
		titleServerPanel.setBorder(null);
		titleServerPanel.setEditable(false);
		titleServerPanel.setForeground(new Color(189,170,70));
		titleServerPanel.setBounds(160,8,200,50);
		titleServerPanel.setFont(new Font("Book Antiqua", Font.HANGING_BASELINE, 30));
		serverPanel.add(titleServerPanel);

		infoTxt = new JTextArea();
		infoTxt.setText(getAdresseIP());
		infoTxt.setBackground(new Color(31,27,31));
		infoTxt.setBorder(null);
		infoTxt.setEditable(false);
		infoTxt.setForeground(Color.white);
		infoTxt.setBounds(45,235,400,50);
		infoTxt.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		serverPanel.add(infoTxt);

		JLabel titleChoixJetons = new JLabel("Jetons de depart");
		titleChoixJetons.setBounds(50,71,150,30);
		titleChoixJetons.setForeground(new Color(189,170,70));
		titleChoixJetons.setFont(new Font("Book Antiqua", Font.BOLD, 16));
		serverPanel.add(titleChoixJetons);

		JLabel titleChoixNbreJoueurs = new JLabel("Nombre de joueurs");
		titleChoixNbreJoueurs.setBounds(50,121,150,30);
		titleChoixNbreJoueurs.setForeground(new Color(189,170,70));
		titleChoixNbreJoueurs.setFont(new Font("Book Antiqua", Font.BOLD, 16));
		serverPanel.add(titleChoixNbreJoueurs);

		JLabel titleChoixBlinde = new JLabel("Valeur de la blinde");
		titleChoixBlinde.setBounds(50,171,150,30);
		titleChoixBlinde.setForeground(new Color(189,170,70));
		titleChoixBlinde.setFont(new Font("Book Antiqua", Font.BOLD, 16));
		serverPanel.add(titleChoixBlinde);

		Integer[] values = new Integer[] {150, 500, 1000, 1500, 2000, 2500, 5000, 10000};
		choixJetons = new JComboBox<>(values);
		choixJetons.setBounds(220,70,200,35);
		choixJetons.setBackground(new Color(70,71,71));
		choixJetons.setForeground(Color.white);
		choixJetons.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		choixJetons.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		serverPanel.add(choixJetons);

		choixNbreJoueurs = new JTextField();
		choixNbreJoueurs.setBounds(220,120,200,35);
		choixNbreJoueurs.setBackground(new Color(70,71,71));
		choixNbreJoueurs.setForeground(Color.white);
		choixNbreJoueurs.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		choixNbreJoueurs.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		serverPanel.add(choixNbreJoueurs);

		choixBlinde = new JTextField();
		choixBlinde.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		choixBlinde.setBounds(220,170,200,35);
		choixBlinde.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		choixBlinde.setBackground(new Color(70,71,71));
		choixBlinde.setForeground(Color.white);
		serverPanel.add(choixBlinde);

		startBtn = new JButton("Creer une partie");
		startBtn.setBounds(71,300,150,50);
		startBtn.setBackground(new Color(50,60,70));
		startBtn.setForeground(new Color(189,170,70));
		startBtn.setRolloverEnabled(true);
		startBtn.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		startBtn.addActionListener(this);
		serverPanel.add(startBtn);

		launchBtn = new JButton("Lancer");
		launchBtn.setBounds(292,300,150,50);
		launchBtn.setBackground(new Color(50,60,70));
		launchBtn.setForeground(new Color(189,170,70));
		launchBtn.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		launchBtn.addActionListener(this);
		launchBtn.setEnabled(false);
		serverPanel.add(launchBtn);

		//-----------------------Création du panel pour rejoindre une partie -----------------------------------

		playerPanel = new JPanel();
		playerPanel.setLocation(410,135);
		playerPanel.setSize(515,404);
		playerPanel.setBackground(new Color(31,27,31));
		playerPanel.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		playerPanel.setLayout(null);
		playerPanel.setVisible(false);

		JLabel titleNomJoueur = new JLabel("Nom joueur");
		titleNomJoueur.setBounds(70,101,150,30);
		titleNomJoueur.setForeground(new Color(189,170,70));
		titleNomJoueur.setFont(new Font("Book Antiqua", Font.BOLD, 16));
		playerPanel.add(titleNomJoueur);

		JLabel titleAdresseIP = new JLabel("Adresse IP");
		titleAdresseIP.setBounds(70,161,150,30);
		titleAdresseIP.setForeground(new Color(189,170,70));
		titleAdresseIP.setFont(new Font("Book Antiqua", Font.BOLD, 16));
		playerPanel.add(titleAdresseIP);

		nomJoueur = new JTextField();
		nomJoueur.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		nomJoueur.setBounds(220,101,200,35);
		nomJoueur.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		nomJoueur.setBackground(new Color(70,71,71));
		nomJoueur.setForeground(Color.white);
		playerPanel.add(nomJoueur);

		adresseIP= new JTextField();
		adresseIP.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		adresseIP.setBounds(220,161,200,35);
		adresseIP.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		adresseIP.setBackground(new Color(70,71,71));
		adresseIP.setForeground(Color.white);
		playerPanel.add(adresseIP);

		JTextArea titlePlayerPanel = new JTextArea();
		titlePlayerPanel.setText("Rejoindre une partie ");
		titlePlayerPanel.setBackground(new Color(31,27,31));
		titlePlayerPanel.setBorder(null);
		titlePlayerPanel.setEditable(false);
		titlePlayerPanel.setForeground(new Color(189,170,70));
		titlePlayerPanel.setBounds(150,12,250,50);
		titlePlayerPanel.setFont(new Font("Book Antiqua", Font.HANGING_BASELINE, 30));
		playerPanel.add(titlePlayerPanel);

		rejoindreBtn = new JButton("Rejoindre");
		rejoindreBtn.setBounds(180,250,150,50);
		rejoindreBtn.setBackground(new Color(50,60,70));
		rejoindreBtn.setForeground(new Color(189,170,70));
		rejoindreBtn.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		rejoindreBtn.addActionListener(this);
		playerPanel.add(rejoindreBtn);

		//----------------------------------------------------------------------------------

		JTextArea infoServer = new JTextArea(); //Informations pour lancer une nouvelle partie
		infoServer.setText(getAdresseIP());
		infoServer.setBackground(new Color(31,27,31));
		infoServer.setBorder(null);
		infoServer.setEditable(false);
		infoServer.setForeground(Color.white);
		infoServer.setBounds(30,75,450,200);
		infoServer.setText( " Afin de creer une nouvelle partie, vous devez renseigner \n"+
		                    " la valeur des jetons de depart des joueurs, le nombre de \n"+
		                    " joueurs et la valeur de la blinde, et ensuite cliquer sur \n"+
		                    " creer une partie. Les joueurs connectes au meme reseaux \n"+
		                    " pourront  alors rejoindre la partie en renseignant l'adresse\n"+
		                    " IP de votre machine.\n"+" Une fois tous les joueurs connectes, vous pourrez lancer la\n"+
		                    " partie en cliquant sur lancer.");
		infoServer.setFont(new Font("Book Antiqua", Font.BOLD, 15));

		JTextArea infoPartie = new JTextArea();  //Informations pour rejoindre une nouvelle partie
		infoPartie.setText(getAdresseIP());
		infoPartie.setBackground(new Color(31,27,31));
		infoPartie.setBorder(null);
		infoPartie.setEditable(false);
		infoPartie.setForeground(Color.white);
		infoPartie.setBounds(30,75,450,200);
		infoPartie.setText( " Afin de rejoindre une partie, vous devez renseigner votre \n"+
		                    " nom ainsi que l'adresse IP du serveur que vous voulez rejoindre\n"+
		                    " et ensuite cliquer sur rejoindre. \n"+
		                    " Vous devez ensuite attendre le lancement de la partie. La \n"+
		                    " fenetre du jeu se lancera automatiquement");
		infoPartie.setFont(new Font("Book Antiqua", Font.BOLD, 15));
		
		//Panel qui contient des informations supplémentaires pour rejoindre une partie
		partieInfoPanel = new JPanel();
		partieInfoPanel.setLocation(410,135);
		partieInfoPanel.setSize(515,404);
		partieInfoPanel.setBackground(new Color(31,27,31));
		partieInfoPanel.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		partieInfoPanel.setLayout(null);
		partieInfoPanel.setVisible(false);
		partieInfoPanel.add(infoPartie);

       //Panel qui contient des informations supplémentaires pour lancer un serveur
		serverInfoPanel = new JPanel();
		serverInfoPanel.setLocation(410,135);
		serverInfoPanel.setSize(515,404);
		serverInfoPanel.setBackground(new Color(31,27,31));
		serverInfoPanel.setBorder(BorderFactory.createLineBorder(new Color(189,170,70,150)));
		serverInfoPanel.setLayout(null);
		serverInfoPanel.setVisible(false);
		serverInfoPanel.add(infoServer);

		contentPane.setBackground(new Color(31,27,31));
		contentPane.setBounds(0,0,1280,780);
		contentPane.setLayout(null);
		contentPane.add(returnBtn);
		contentPane.add(serverPanel);
		contentPane.add(playerPanel);
		contentPane.add(panelChoix);
		contentPane.add(serverInfoPanel);
		contentPane.add(partieInfoPanel);
		contentPane.add(logo);
		contentPane.add(wallpaper);
		repaint();

	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == lancerServeur){
			panelChoix.setVisible(false);
			serverPanel.setVisible(true);
			returnBtn.setVisible(true);
		}
		if(e.getSource() == rejoindrePartie){
			panelChoix.setVisible(false);
			playerPanel.setVisible(true);
			returnBtn.setVisible(true);
		}
		if(e.getSource() == returnBtn){
			if(serverPanel.isVisible()){
				serverPanel.setVisible(false);
				panelChoix.setVisible(true);
				returnBtn.setVisible(false);
			}else if(playerPanel.isVisible()){
				playerPanel.setVisible(false);
				panelChoix.setVisible(true);
				returnBtn.setVisible(false);
			}else if(serverInfoPanel.isVisible()){
				serverInfoPanel.setVisible(false);
				panelChoix.setVisible(true);
				returnBtn.setVisible(false);

			}else if(partieInfoPanel.isVisible()){
				partieInfoPanel.setVisible(false);
				panelChoix.setVisible(true);
				returnBtn.setVisible(false);
			}
		}
		if(e.getSource() == rejoindreBtn){
			if(nomJoueur.getText().equals("") || adresseIP.getText().equals("")){
				JOptionPane.showMessageDialog(contentPane,"Veuillez remplire tous les champs!", "Erreur", JOptionPane.ERROR_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(contentPane,"Attente de lancement de la partie.", "Lancement de la partie", JOptionPane.INFORMATION_MESSAGE);
				String nom  = nomJoueur.getText();
				String ip = adresseIP.getText();
				Joueur j = new Joueur(nom);
				j.connectToServer(ip);
				j.setupIHM();
				j.play();
				this.dispose();
			}
		}
		if(e.getSource() == startBtn){
			if(choixNbreJoueurs.getText().equals("") || choixBlinde.getText().equals("")){
				JOptionPane.showMessageDialog(contentPane,"Veuillez remplire tous les champs!", "Erreur", JOptionPane.ERROR_MESSAGE);
			}else{
				int nbJoueur = Integer.parseInt(choixNbreJoueurs.getText());
				int jetonsInitials = (int)choixJetons.getSelectedItem();
				int blinde = Integer.parseInt(choixBlinde.getText());
				if(blinde*10 > jetonsInitials || blinde < 15){  
					blinde = 15;
				}
				if(nbJoueur > 1 && nbJoueur <= 8){
					JOptionPane.showMessageDialog(contentPane,"Attente de connexion des joueurs...", "Lancement de la partie", JOptionPane.INFORMATION_MESSAGE);
					gs = new GameServer(nbJoueur, blinde,jetonsInitials);
					choixNbreJoueurs.setEditable(false);
					choixBlinde.setEditable(false);
					launchBtn.setEnabled(true);
					startBtn.setEnabled(false);
					gs.acceptConnections();
					infoTxt.setText("Tous les joueurs sont connectes.");
				}else{
					JOptionPane.showMessageDialog(contentPane,"Nombre de joueurs invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			
		}
		if(e.getSource() == launchBtn){
			gs.startGame();
			launchBtn.setEnabled(false);
			infoTxt.setText("Partie lancee. Veuillez ne pas fermer la fenetre!");
		}
		if(e.getSource() == serverInfoBtn){
			serverInfoPanel.setVisible(true);
			panelChoix.setVisible(false);
			returnBtn.setVisible(true);

		}
		if(e.getSource() == partieInfoBtn){
			partieInfoPanel.setVisible(true);
			panelChoix.setVisible(false);
			returnBtn.setVisible(true);
		}

	}

	//Methode qui retourne l'adresse IP de la machine qui lance le serveur
	public String getAdresseIP(){
		String localIp = "";
		try{
			localIp = ""+InetAddress.getLocalHost();
		}catch(IOException ex){
			System.out.println("IOException from getAdresseIP()");
		}

		//On recupere uniquement les chiffres correspondant à 
		//l'adresse IP exemple: 145.32.98.41 car localIP est de la forme: LAPTOP-908 /145.32.98.41
		int indice=0;
		while(true){                                   
			if(localIp.charAt(indice) == '/'){
				localIp = localIp.substring(indice+1,localIp.length());
				break;
			}else{
				indice++;
			}

		}
		return "Adresse IP : "+localIp; 
	}

	public ImageIcon scaleImage(String iconPath, int w, int h){
 
        ImageIcon imageIcon = new ImageIcon(iconPath); // On charge l'image en ImageIcon
		Image image = imageIcon.getImage();   // On transforme en Image 
		Image newimg = image.getScaledInstance(w,h,  java.awt.Image.SCALE_SMOOTH);  // on recadre l'image 
		imageIcon = new ImageIcon(newimg);  // On retransforme en ImageIcon
		return imageIcon;
    }

	public static void main(String[] args) {
		 new Launcher();
	}
}