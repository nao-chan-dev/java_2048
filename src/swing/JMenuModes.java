package swing;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import java.util.List;

public class JMenuModes extends JMenu {

    public JMenuModes(JMenuColors colorsMenu, Runnable updateModeFunction) {
        super("MODE");
        ButtonGroup modesItemsGroup = new ButtonGroup();
        JRadioButtonMenuItem  animeModeItem = new JRadioButtonMenuItem("ANIME", JPanelTile.animeMode);
        JRadioButtonMenuItem  basicModeItem = new JRadioButtonMenuItem("BASIC COLORS", !JPanelTile.animeMode);
        for(JRadioButtonMenuItem modeItem: List.of(animeModeItem, basicModeItem)) {
            modeItem.addActionListener(e -> {
                JPanelTile.animeMode = modeItem.getText().equals("ANIME");
                colorsMenu.setEnabled(!JPanelTile.animeMode);
                updateModeFunction.run();
            });
            modesItemsGroup.add(modeItem);
            add(modeItem);
        }
    }
}
