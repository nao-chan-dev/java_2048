package swing;

import core.Tile;
import resource.ImageResources;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;

public class JPanelTile extends JPanel {

    public static boolean animeMode = true;
    public static TileColor selectedColor = TileColor.RED;

    private final Tile tile;
    private final JLabel tileText;

    public JPanelTile(Tile tile) {
        this.tile = tile;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new GridBagLayout());
        tileText = new JLabel(tile.isEmpty() ? "" : String.valueOf(tile.getValue()));
        tileText.setForeground(Color.BLACK);
        add(tileText);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(animeMode) {
            if(tileText.isVisible()) {
                tileText.setVisible(false);
            }
            Image tileImage = ImageResources.TILE_IMAGES.getOrDefault(tile.getValue(), ImageResources.INFINITY_TILE);
            g.drawImage(tileImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            if(!tileText.isVisible()) {
                tileText.setVisible(true);
            }
            tileText.setSize(new Dimension(getWidth(), tileText.getHeight()));
            tileText.setFont(new Font("Comic Sans MS", Font.PLAIN, getCalculatedFontSize(Math.min(getWidth(), getHeight()))));
            tileText.setText(tile.isEmpty() ? "" : String.valueOf(tile.getValue()));
            setBackground(!tile.isEmpty() ? selectedColor.awtColor(tile.getValue()) : Color.white);
        }
    }

    private int getCalculatedFontSize(int minimumWindowSize) {
        int tileValueLength = String.valueOf(tile.getValue()).length();
        double wideTileValueReduction = 1 - 0.05 * tileValueLength;
        return (int) (minimumWindowSize * 0.5 * wideTileValueReduction);
    }
}
