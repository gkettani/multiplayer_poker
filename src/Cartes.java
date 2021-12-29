import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;


public class Cartes implements Comparable<Cartes>, Serializable  {
    
    public int valeur;
    public String signe;
    private Image image;

    
    public Cartes(int valeur, String signe){
        this.valeur = valeur;
        this.signe = signe;
       
        
    }

    public boolean equals(Cartes carte){
        if (this.valeur == carte.valeur && this.signe.equals(carte.signe)){
            return true;
        }
        else{
            return false;
        }
    }

    public void getImage(){                                    //Permet de charger les images associées aux cartes 
        String fileName = valeur +"_"+ signe;
        try{
            BufferedImage img = ImageIO.read(new File("../images/" + fileName +".png"));
            image = img.getScaledInstance(75,116,java.awt.Image.SCALE_SMOOTH);

        } catch(IOException ex){
            System.out.println("IOException from cartes constructor");
        }
    }

    public void dessine(Graphics g, int x,int y){
        g.drawImage(image, x,y,null);
    }

    public int compareTo(Cartes carte){
    	if (this.valeur > carte.valeur)
    		return 1;
    	else if (this.valeur == carte.valeur)
    		return 0;
    	else 
    		return -1;
    }
    
    /**
     * Pour afficher les attributs de la carte
     * @return les attributs de la carte sous la forme (valeur=int ,signe=string)
     */
    public String toString() {
		String res ;
        res=" (" + valeur + "," + signe + ") ";
        return res;  
    }    
}

