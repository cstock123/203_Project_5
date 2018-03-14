import processing.core.PImage;

import java.util.List;
import java.util.Scanner;

public class Loader {

    //private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    private static final int PROPERTY_KEY = 0;

    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final String MINER_KEY = "miner";
    private static final int MINER_NUM_PROPERTIES = 7;
    //private static final int MINER_ID = 1;
    private static final int MINER_COL = 2;
    private static final int MINER_ROW = 3;
    private static final int MINER_LIMIT = 4;
    private static final int MINER_ACTION_PERIOD = 5;
    private static final int MINER_ANIMATION_PERIOD = 6;

    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    //private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    private static final String ORE_KEY = "ore";
    private static final int ORE_NUM_PROPERTIES = 5;
    private static final int ORE_ID = 1;
    private static final int ORE_COL = 2;
    private static final int ORE_ROW = 3;
    private static final int ORE_ACTION_PERIOD = 4;

    private static final String VEIN_KEY = "vein";
    private static final int VEIN_NUM_PROPERTIES = 5;
    //private static final int VEIN_ID = 1;
    private static final int VEIN_COL = 2;
    private static final int VEIN_ROW = 3;
    private static final int VEIN_ACTION_PERIOD = 4;

    private static final String SMITH_KEY = "blacksmith";
    private static final int SMITH_NUM_PROPERTIES = 4;
    //private static final int SMITH_ID = 1;
    private static final int SMITH_COL = 2;
    private static final int SMITH_ROW = 3;

    private static final String CHARACTER_KEY = "character";
    private static final int CHARACTER_NUM_PROPERTIES = 6;
    private static final int CHARACTER_COL = 2;
    private static final int CHARACTER_ROW = 3;
    private static final int CHARACTER_ACTION_PERIOD = 4;
    private static final int CHARACTER_ANIMATION_PERIOD = 5;

    public static String ORE_KEY() { return ORE_KEY; }

    public static void load(Scanner in, WorldModel world, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), world, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(String.format("invalid entry on line %d",
                        lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("issue on line %d: %s",
                        lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
    }

    private static boolean processLine(String line, WorldModel world,
                                       ImageStore imageStore) {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return parseBackground(properties, world, imageStore);
                case MINER_KEY:
                    return parseMiner(properties, world, imageStore);
                case OBSTACLE_KEY:
                    return parseObstacle(properties, world, imageStore);
                case ORE_KEY:
                    return parseOre(properties, world, imageStore);
                case SMITH_KEY:
                    return parseSmith(properties, world, imageStore);
                case VEIN_KEY:
                    return parseVein(properties, world, imageStore);
                case CHARACTER_KEY:
                    return parseCharacter(properties, world, imageStore);
            }
        }
        return false;
    }


    private static boolean parseBackground(String[] properties,
                                           WorldModel world, ImageStore imageStore) {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground(pt, new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    private static boolean parseMiner(String[] properties, WorldModel world,
                                      ImageStore imageStore) {
        if (properties.length == MINER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                    Integer.parseInt(properties[MINER_ROW]));
            MinerNotFull entity = createMinerNotFull(Integer.parseInt(properties[MINER_LIMIT]),
                    pt,
                    Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                    Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
                    imageStore.getImageList(MINER_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == MINER_NUM_PROPERTIES;
    }

    private static boolean parseCharacter(String[] properties, WorldModel world,
                                          ImageStore imageStore) {
        if(properties.length == CHARACTER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[CHARACTER_COL]),
                    Integer.parseInt(properties[CHARACTER_ROW]));
            Character entity = createCharacter(pt,
                    imageStore.getImageList(CHARACTER_KEY),
                    Integer.parseInt(properties[CHARACTER_ACTION_PERIOD]),
                    Integer.parseInt(properties[CHARACTER_ANIMATION_PERIOD]));
            world.tryAddEntity(entity);
        }

        return properties.length == CHARACTER_NUM_PROPERTIES;
    }

    private static boolean parseObstacle(String[] properties, WorldModel world,
                                         ImageStore imageStore) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Obstacle entity = createObstacle(pt, imageStore.getImageList(OBSTACLE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    private static boolean parseOre(String[] properties, WorldModel world,
                                    ImageStore imageStore) {
        if (properties.length == ORE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                    Integer.parseInt(properties[ORE_ROW]));
            Entity entity = createOre(properties[ORE_ID],
                    pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
                    imageStore.getImageList(ORE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == ORE_NUM_PROPERTIES;
    }

    private static boolean parseSmith(String[] properties, WorldModel world,
                                      ImageStore imageStore) {
        if (properties.length == SMITH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                    Integer.parseInt(properties[SMITH_ROW]));
            BlackSmith entity = createBlacksmith(pt, imageStore.getImageList(SMITH_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == SMITH_NUM_PROPERTIES;
    }

    private static boolean parseVein(String[] properties, WorldModel world,
                                     ImageStore imageStore) {
        if (properties.length == VEIN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                    Integer.parseInt(properties[VEIN_ROW]));
            Vein entity = createVein(pt,
                    Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
                    imageStore.getImageList(VEIN_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == VEIN_NUM_PROPERTIES;
    }


    private static BlackSmith createBlacksmith(Point position, List<PImage> images) {
        return new BlackSmith(position, images);
    }

    public static MinerFull createMinerFull(Point position, List<PImage> images, int resourceLimit,
                                            int actionPeriod, int animationPeriod) {
        return new MinerFull(position, images, resourceLimit, actionPeriod, animationPeriod);
    }

    public static MinerNotFull createMinerNotFull(int resourceLimit, Point position, int actionPeriod,
                                                  int animationPeriod, List<PImage> images) {
        return new MinerNotFull(position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }

    private static Obstacle createObstacle(Point position, List<PImage> images) {
        return new Obstacle(position, images);
    }

    public static Ore createOre(String id, Point position, int actionPeriod, List<PImage> images) {
        return new Ore(id, position, images, actionPeriod);
    }

    public static OreBlob createOreBlob(Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        return new OreBlob(position, images, actionPeriod, animationPeriod);
    }

    public static Quake createQuake(Point position, List<PImage> images) {
        return new Quake(position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    private static Vein createVein(Point position, int actionPeriod, List<PImage> images) {
        return new Vein(position, images, actionPeriod);
    }

    private static Character createCharacter(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        return new Character(position, images, actionPeriod, animationPeriod);
    }
}
