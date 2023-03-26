package swing;

import core.Game;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JMenuSizes extends JMenu {

    private static final List<Integer> SUPPORTED_GAME_SIZES = List.of(4, 5, 6);

    private boolean disableDialogConfirmation = false;

    public JMenuSizes(GridLayout gridLayout, Runnable newGameFunction) {
        super("SIZE");
        ButtonGroup sizeItemsGroup = new ButtonGroup();
        Map<Integer, JRadioButtonMenuItem> sizeItems = new HashMap<>();
        JCheckBox disableConfirmation = new JCheckBox("Stop askin'", disableDialogConfirmation);
        disableConfirmation.addActionListener((e) -> disableDialogConfirmation = disableConfirmation.isSelected());
        for (int supportedSize : SUPPORTED_GAME_SIZES) {
            JRadioButtonMenuItem  sizeItem = new JRadioButtonMenuItem (supportedSize + " x " + supportedSize,
                    Game.size == supportedSize);
            sizeItems.put(supportedSize, sizeItem);
            sizeItemsGroup.add(sizeItem);
            sizeItem.addActionListener(e -> {
                if (Game.size == supportedSize) { return; }
                if (!disableDialogConfirmation) {
                    int dialogInput = JOptionPane.showConfirmDialog(null,
                            new Object[]{"Changing the grid size needs to create a new game. Are you sure?", disableConfirmation},
                            "NEW SIZE: " + sizeItem.getText(), JOptionPane.YES_NO_OPTION);
                    if (dialogInput != 0) { sizeItems.get(Game.size).setSelected(true); return; }
                }
                Game.size = supportedSize;
                gridLayout.setRows(Game.size);
                gridLayout.setColumns(Game.size);
                newGameFunction.run();
            });
            add(sizeItem);
        }
    }
}
