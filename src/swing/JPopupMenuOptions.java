package swing;

import core.Direction;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.Consumer;

public class JPopupMenuOptions extends JPopupMenu {

    private static final boolean SONGS_ENABLED = true;

    public JPopupMenuOptions(GridLayout gridLayout, Consumer<Direction> moveGameFunction, Runnable newGameFunction, Runnable quitGameFunction, Runnable updateModeFunction) {
        for (JMenuItem contextItem: List.of(new JMenuItem("UP"), new JMenuItem("DOWN"),
                new JMenuItem("LEFT"), new JMenuItem("RIGHT"))) {
            add(contextItem);
            contextItem.addActionListener(e -> moveGameFunction.accept(Direction.valueOf(contextItem.getText())));
        }
        addSeparator();

        JMenuSizes sizes = new JMenuSizes(gridLayout, newGameFunction);
        add(sizes);
        JMenuColors colors = new JMenuColors();
        add(colors);
        JMenuModes modes = new JMenuModes(colors, updateModeFunction);
        add(modes);
        if(SONGS_ENABLED) {
            JMenuSongs songs = new JMenuSongs();
            add(songs);
        }
        addSeparator();

        JMenuItem newGame = new JMenuItem("NEW GAME");
        newGame.addActionListener(e -> newGameFunction.run());
        JMenuItem quitGame = new JMenuItem("QUIT GAME");
        quitGame.addActionListener(e -> quitGameFunction.run());
        add(newGame);
        add(quitGame);
    }
}
