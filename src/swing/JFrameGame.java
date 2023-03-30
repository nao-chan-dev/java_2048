package swing;

import core.Direction;
import core.Game;
import core.Tile;
import resource.ImageResources;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class JFrameGame extends JFrame implements KeyListener {

    private static final int DEFAULT_WINDOW_SIZE = 600;

    private static final Map<Integer, Direction> inputDirectionsMapping = Map.of(
            KeyEvent.VK_UP, Direction.UP, KeyEvent.VK_KP_UP, Direction.UP,
            KeyEvent.VK_DOWN, Direction.DOWN, KeyEvent.VK_KP_DOWN, Direction.DOWN,
            KeyEvent.VK_LEFT, Direction.LEFT, KeyEvent.VK_KP_LEFT, Direction.LEFT,
            KeyEvent.VK_RIGHT, Direction.RIGHT, KeyEvent.VK_KP_RIGHT, Direction.RIGHT
    );

    private Game game;
    private final JPanel mainPanel;
    private final String frameBaseTitle;
    private long gameStartTime;
    private long gameOverTime;

    public JFrameGame(String title) {
        super(title);
        frameBaseTitle = title;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_WINDOW_SIZE, DEFAULT_WINDOW_SIZE);
        setIconImage(ImageResources.GAME_ICON);
        setLocationRelativeTo(null);
        addKeyListener(this);
        mainPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(Game.size, Game.size);
        mainPanel.setLayout(gridLayout);
        newGame();

        JPopupMenuOptions popupMenu = new JPopupMenuOptions(gridLayout, this::moveGameEvent, this::newGameEvent, () -> System.exit(0), mainPanel::updateUI);
        mainPanel.setComponentPopupMenu(popupMenu);

        add(mainPanel);

        Timer timer = new Timer(1000, e -> {
            if (game.isGameOver() && gameOverTime == 0) {
                gameOverTime = System.currentTimeMillis() / 1000;
            }
            long duration = game.isGameOver() ? gameOverTime - gameStartTime : System.currentTimeMillis() / 1000 - gameStartTime;
            String gameOverString = game.isGameOver() ? " GAME OVER" : "";
            String chronoString = String.format("%d:%02d:%02d", duration / 3600, (duration % 3600) / 60, (duration % 60));
            setTitle(frameBaseTitle + " " + chronoString + " " + Game.size + "x" + Game.size + " - Score: " + game.getScore() + gameOverString);
        });
        timer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(inputDirectionsMapping.containsKey(e.getKeyCode())) {
            moveGameEvent(inputDirectionsMapping.get(e.getKeyCode()));
            return;
        }

        if(e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_R) {
            AutoMoveRandom.toggle(this::randomMoveGameEvent);
            return;
        }

        if(e.getKeyCode() == KeyEvent.VK_R) {
            randomMoveGameEvent();
            return;
        }

        if(e.getKeyCode() == KeyEvent.VK_N) {
            newGameEvent();
            return;
        }

        if(e.getKeyCode() == KeyEvent.VK_Q) {
            System.exit(0);
        }
    }

    private void newGameEvent() {
        mainPanel.removeAll();
        newGame();
        mainPanel.updateUI();
    }

    private void randomMoveGameEvent() {
        Integer randomKeyCode = new ArrayList<>(inputDirectionsMapping.keySet()).get(new Random().nextInt(inputDirectionsMapping.keySet().size()));
        moveGameEvent(inputDirectionsMapping.get(randomKeyCode));
    }

    private void moveGameEvent(Direction direction) {
        game.move(direction);
        mainPanel.updateUI();
    }

    private void newGame() {
        game = new Game();
        for (Tile tile: game.getTiles()) {
            JPanelTile panel = new JPanelTile(tile);
            mainPanel.add(panel);
        }
        gameStartTime = System.currentTimeMillis() / 1000;
        gameOverTime = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
