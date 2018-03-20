import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import processing.core.*;

public final class VirtualWorld
        extends PApplet {
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;
    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String LOAD_FILE_NAME = "gaia.sav";

    private static String LAST_DIRECTION = "RIGHT";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long next_time;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
                TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= next_time) {
            scheduler.updateOnTime(time);
            next_time = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    LAST_DIRECTION = "UP";
                    break;
                case DOWN:
                    dy = 1;
                    LAST_DIRECTION = "DOWN";
                    break;
                case LEFT:
                    dx = -1;
                    LAST_DIRECTION = "LEFT";
                    break;
                case RIGHT:
                    dx = 1;
                    LAST_DIRECTION = "RIGHT";
                    break;
                case CONTROL:
                    spawnFire();
            }
            moveStuff(dx, dy);
        }
    }

    private void spawnFire(){
        Character c = world.getCharacter();
        List<Point> firePos = new ArrayList<>();
        Point charPos = c.getPosition();
        String key = "";

        if(c.getInventory().getNumOre() >= 5) {
            if(LAST_DIRECTION.equals("UP")) {
                firePos.add(translatePoint(charPos, 0, -1));
                firePos.add(translatePoint(charPos, 0, -2));
                key = "fireUp";
            } else if(LAST_DIRECTION.equals("RIGHT")) {
                firePos.add(translatePoint(charPos, 1, 0));
                firePos.add(translatePoint(charPos, 2, 0));
                key = "fireRight";
            } else if(LAST_DIRECTION.equals("DOWN")) {
                firePos.add(translatePoint(charPos, 0, 1));
                firePos.add(translatePoint(charPos, 0, 2));
                key = "fireDown";
            } else if(LAST_DIRECTION.equals("LEFT")) {
                firePos.add(translatePoint(charPos, -1, 0));
                firePos.add(translatePoint(charPos, -2, 0));
                key = "fireLeft";
            }
            addFire(key, firePos.get(0), c);
            addFire(key + "2", firePos.get(1), c);
            c.getInventory().decrement5();
        }
    }

    private void addFire(String key, Point p, Character c) {
        if(world.isOccupied(p)) {
            Entity occupant = world.getOccupant(p).get();
            if(occupant.getClass() == MinerNotFull.class || occupant.getClass() == MinerFull.class) {
                ActiveEntity fireMiner = new MinerOnFire("junk", occupant.getPosition(),
                        imageStore.getImageList("minerFire"), 2, 200, 75, 0);
                world.removeEntity(occupant);
                scheduler.unscheduleAllEvents(occupant);
                world.addEntity(fireMiner);
                fireMiner.scheduleActions(scheduler, world, imageStore);
                world.setBackground(p, new Background(imageStore.getImageList("burnt")));
            }
            world.removeEntity(occupant);
            scheduler.unscheduleAllEvents(occupant);
        } else {
            ActiveEntity fire = new Fire(p, imageStore.getImageList(key), 1000, 75, 0);
            world.addEntity(fire);
            fire.scheduleActions(scheduler, world, imageStore);
            world.setBackground(p, new Background(imageStore.getImageList("burnt")));
        }

    }


    private Point translatePoint(Point pos, int dx, int dy) {
        return new Point(pos.getX() + dx, pos.getY() + dy);
    }

    private void moveStuff(int dx, int dy) {
        Character c = world.getCharacter();
        Point newPos = new Point(c.getPosition().getX() + dx, c.getPosition().getY() + dy);

        Optional<Entity> target = world.findNearest(c.getPosition(), Ore.class);
        if(target.isPresent()) {
            if(world.isOccupied(newPos)) {
                if(world.getOccupant(newPos).equals(target)){
                    world.removeEntity(target.get());
                    scheduler.unscheduleAllEvents(target.get());
                    c.getInventory().addOre();
                }
            }
        }

        if(world.withinBounds(newPos) && !world.isOccupied(newPos)) {
            if(world.characterWithinXBounds(c.getPosition(), view)) {
                view.shiftView(dx, 0);
            }
            if(world.characterWithinYBounds(c.getPosition(), view)) {
                view.shiftView(0, dy);
            }
            c.setPosition(new Point(c.getPosition().getX() + dx, c.getPosition().getY() + dy));
        }
    }



    private static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    private static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(String filename, ImageStore imageStore, PApplet screen) {
        try {
            Scanner in = new Scanner(new File(filename));
            ImageCreator.loadImages(in, imageStore, screen);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void loadWorld(WorldModel world, String filename, ImageStore imageStore) {
        try {
            Scanner in = new Scanner(new File(filename));
            Loader.load(in, world, imageStore);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.entities()) {
            if (entity instanceof ActiveEntity) {
                ((ActiveEntity) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    private static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }

}
