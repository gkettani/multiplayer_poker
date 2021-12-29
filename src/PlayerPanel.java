import java.awt.*;
import javax.swing.*;

public class PlayerPanel extends JPanel{

    private Color backgroundColor;
    private int cornerRadius = 15;
    private int idPanel;          //Chaque panel aura un identifiant qui permettra de le relier au joueur
    public JTextField nom;
    private JTextField jetons;

    public PlayerPanel(LayoutManager layout, int radius, int id) {
        super(layout);
        cornerRadius = radius;
        idPanel = id;

        JLabel coinsIcon = new JLabel(scaleImage("../icons/coins.png", 30, 30));
		coinsIcon.setBounds(10,35,30,30);
		JLabel idIcon = new JLabel(scaleImage("../icons/"+ idPanel+".png", 25, 25));
		idIcon.setBounds(12,7,25,25);

        nom = new JTextField();
        nom.setOpaque(false);
        nom.setBackground(new Color(0,0,0,0));
        nom.setBorder(null);
        nom.setEditable(false);
        nom.setForeground(new Color(255,255,255));
        nom.setBounds(50,7,120,30);
        nom.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 18) );

        jetons = new JTextField();
        jetons.setOpaque(false);
        jetons.setBackground(new Color(0,0,0,0));
        jetons.setBorder(null);
        jetons.setEditable(false);
        jetons.setForeground(new Color(220,195,30));
        jetons.setBounds(25,37,120,30);
        jetons.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 23) );


        this.setOpaque(false);
        this.add(nom);
        this.add(coinsIcon);
        this.add(idIcon);
        this.add(jetons);
    }

    public void setNom(String str){
        nom.setText(str);
    }

    public void setJetons(int coins){
    	jetons.setText(" $ " +  coins );
    }

    public ImageIcon scaleImage(String iconPath, int w, int h){    
 
        ImageIcon imageIcon = new ImageIcon(iconPath);   // On charge l'image en ImageIcon
		Image image = imageIcon.getImage();    // On transforme en Image 
		Image newimg = image.getScaledInstance(w,h,  java.awt.Image.SCALE_SMOOTH);    // on recadre l'image 
		imageIcon = new ImageIcon(newimg);     // On retransforme en ImageIcon
		return imageIcon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessine un JPanel avec des bords arrondis
        if (backgroundColor != null) {
            graphics.setColor(backgroundColor);
        } else {
            graphics.setColor(getBackground());
        }
        graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //on paint le fond
        graphics.setColor(getForeground());
        graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //on paint les bords
    }

}
