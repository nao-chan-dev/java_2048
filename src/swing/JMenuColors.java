package swing;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class JMenuColors extends JMenu {

    public JMenuColors() {
        super("COLORS");
        setEnabled(!JPanelTile.animeMode);
        ButtonGroup colorsItemsGroup = new ButtonGroup();
        for(TileColor tileColor: TileColor.values()) {
            JRadioButtonMenuItem  colorItem = new JRadioButtonMenuItem(tileColor.name(), JPanelTile.selectedColor == tileColor);
            colorItem.addActionListener(e -> JPanelTile.selectedColor = tileColor);
            colorsItemsGroup.add(colorItem);
            add(colorItem);
        }
    }
}
