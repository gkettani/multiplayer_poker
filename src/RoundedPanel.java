import java.awt.*;
import javax.swing.*;

class RoundedPanel extends JPanel{

    //Classe pour créer des JPanel avec des bords arrondis

    private Color backgroundColor;
    private int cornerRadius;


    public RoundedPanel(LayoutManager layout, int radius) {
        super(layout);
        cornerRadius = radius;
        this.setOpaque(false);    
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
        graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //paint background
        graphics.setColor(getForeground());
        graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //paint border
    }
}
