import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

final class WorldView {
    private PApplet screen;
    private WorldModel world;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world, int tileWidth, int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }

    public int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = clamp(viewport.col() + colDelta, 0,
                world.numCols() - viewport.numCols());
        int newRow = clamp(viewport.row() + rowDelta, 0,
                world.numRows() - viewport.numRows());
        viewport.shift(newCol, newRow);
    }

    public void drawViewport() {
        drawBackground();
        drawEntities();
        drawInventory();
    }

    public void drawInventory() {
        Character c = world.getCharacter();
        Inventory i = c.getInventory();
        int num = i.getNumOre();
        screen.textSize(32);
        screen.text("Ore Count: " + num, 25, 25);
    }

    private void drawEntities() {
        for (Entity entity : world.entities()) {
            Point pos = entity.getPosition();

            if (viewport.contains(pos)) {
                Point viewPoint = viewport.worldToViewport(pos.x, pos.y);
                screen.image(entity.getCurrentImage(),
                        viewPoint.x * tileWidth, viewPoint.y * tileHeight);
            }
        }
    }

    private void drawBackground() {
        for (int row = 0; row < viewport.numRows(); row++) {
            for (int col = 0; col < viewport.numCols(); col++) {
                Point worldPoint = viewport.viewportToWorld(col, row);
                Optional<PImage> image = world.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    screen.image(image.get(), col * tileWidth, row * tileHeight);
                }
            }
        }
    }

    public int screenHeight() {
        return screen.height;
    }

    public int screenWidth() {
        return screen.width;
    }
}
