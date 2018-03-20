import java.io.*;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Random;

public class Loader {
    private static final int PROPERTY_KEY = 0;

    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final String MINER_KEY = "miner";
    private static final int MINER_NUM_PROPERTIES = 7;
    private static final int MINER_ID = 1;
    private static final int MINER_COL = 2;
    private static final int MINER_ROW = 3;
    private static final int MINER_LIMIT = 4;
    private static final int MINER_ACTION_PERIOD = 5;
    private static final int MINER_ANIMATION_PERIOD = 6;

    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    private static final String ORE_KEY = "ore";
    private static final int ORE_NUM_PROPERTIES = 5;
    private static final int ORE_COL = 2;
    private static final int ORE_ROW = 3;
    private static final int ORE_ACTION_PERIOD = 4;

    private static final String SMITH_KEY = "blacksmith";
    private static final int SMITH_NUM_PROPERTIES = 4;
    private static final int SMITH_COL = 2;
    private static final int SMITH_ROW = 3;

    private static final String VEIN_KEY = "vein";
    private static final int VEIN_NUM_PROPERTIES = 5;
    private static final int VEIN_COL = 2;
    private static final int VEIN_ROW = 3;
    private static final int VEIN_ACTION_PERIOD = 4;

    private static final String CHARACTER_KEY = "character";
    private static final int CHARACTER_NUM_PROPERTIES = 6;
    private static final int CHARACTER_COL = 2;
    private static final int CHARACTER_ROW = 3;
    private static final int CHARACTER_ACTION_PERIOD = 4;
    private static final int CHARACTER_ANIMATION_PERIOD = 5;

    private static final String WATER_KEY = "water";

    //RGB's
    private static final int GRASS_RGB = -6226052;
    private static final int MINER_RGB = -8454140;
    private static final int ROCK_RGB = -8355712;
    //private static final int ORE_RGB = 0;
    private static final int SMITH_RGB = -16770948;
    private static final int VEIN_RGB = -64837;
    private static final int CHARACTER_RGB = -1;
    private static final int WATER_RGB = -13271809;
    private static final int OBSTACLE_RGB = -11075457;


    public static void load(Scanner in, WorldModel world, ImageStore imageStore) {
        loadWorldImage(world, imageStore);
        /*
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), world, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d", lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(String.format("invalid entry on line %d", lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("issue on line %d: %s", lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
        */
    }

    private static void loadWorldImage(WorldModel world, ImageStore imageStore) {
        BufferedImage img = null;
        File f;

        try {
            f = new File("images/worlds/world1.png");
            img = ImageIO.read(f);
        } catch(IOException e) {
            System.out.print(e.getMessage());
            e.printStackTrace();
        }
        int width = getImageWidth(img);
        int height = getImageHeight(img);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++){
                int rgb = img.getRGB(x, y);
                makeGrass(world, imageStore, x, y);
                switch(rgb){
                    case MINER_RGB:
                        makeMiner(world, imageStore, x, y);
                        break;
                    case ROCK_RGB:
                        makeRock (world, imageStore, x, y);
                        break;
                    case SMITH_RGB:
                        makeSmith(world, imageStore, x, y);
                        break;
                    case VEIN_RGB:
                        makeVein(world, imageStore, x, y);
                        break;
                    case CHARACTER_RGB:
                        makeCharacter(world, imageStore, x, y);
                        break;
                    case WATER_RGB:
                        makeWater(world, imageStore, x, y);
                        break;
                    case OBSTACLE_RGB:
                        makeObstacle(world, imageStore, x, y);
                        break;
                }
            }
        }
    }

    private static void makeGrass(WorldModel world, ImageStore imageStore, int x, int y) {
        Point pt = new Point(x, y);
        world.setBackground(pt, new Background(imageStore.getImageList("grass")));
    }

    private static void makeRock(WorldModel world, ImageStore imageStore, int x, int y) {
        Point pt = new Point(x, y);
        world.setBackground(pt, new Background(imageStore.getImageList("rocks")));
    }

    private static void makeMiner(WorldModel world, ImageStore imageStore, int x, int y) {
        Point pt = new Point(x, y);

        Entity miner = new MinerNotFull("miner", pt,
                imageStore.getImageList("miner"),
                2, 0, 800, 100, 0);

        world.tryAddEntity(miner);
    }

    private static void makeObstacle(WorldModel world, ImageStore imageStore, int x, int y) {
        Point pt = new Point(x, y);

        Entity obstacle = new Obstacle(pt, imageStore.getImageList(OBSTACLE_KEY));
        System.out.print("OBSTACLE ");
        System.out.println(x + " " + y);
        world.tryAddEntity(obstacle);
    }

    private static void makeWater(WorldModel world, ImageStore imageStore, int x, int y) {
        Point pt = new Point(x, y);

        Entity water = new Water(pt, imageStore.getImageList(WATER_KEY));
        world.tryAddEntity(water);
    }

    private static void makeSmith(WorldModel world, ImageStore imageStore, int x, int y) {
        Point pt = new Point(x, y);
        Blacksmith smith = new Blacksmith(pt, imageStore.getImageList(SMITH_KEY));
        world.tryAddEntity(smith);
    }

    private static void makeVein(WorldModel world, ImageStore imageStore, int x, int y) {
        Point pt = new Point(x, y);
        Random r = new Random();
        //4000-10000
        int actionPeriod = r.nextInt(6000) + 4000;
        Entity vein = new Vein(pt, imageStore.getImageList(VEIN_KEY), actionPeriod);
        world.tryAddEntity(vein);
    }

    private static void makeCharacter(WorldModel world, ImageStore imageStore, int x, int y) {
        Point pt = new Point(x, y);
        //851 100
        Entity character = new Character(pt,
                imageStore.getImageList(CHARACTER_KEY),
                851,
                100, 0,
                new Inventory(0));
        world.tryAddEntity(character);
    }

    private static int getImageWidth(BufferedImage image) {
        return image.getWidth();
    }
    private static int getImageHeight(BufferedImage image) {
        return image.getHeight();
    }

    private static boolean processLine(String line, WorldModel world, ImageStore imageStore) {
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

    private static boolean parseCharacter(String[] properties, WorldModel world,
                                          ImageStore imageStore) {
        if(properties.length == CHARACTER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[CHARACTER_COL]),
                    Integer.parseInt(properties[CHARACTER_ROW]));
            Character entity = new Character(pt,
                    imageStore.getImageList(CHARACTER_KEY),
                    Integer.parseInt(properties[CHARACTER_ACTION_PERIOD]),
                    Integer.parseInt(properties[CHARACTER_ANIMATION_PERIOD]), 0,
                    new Inventory(0));
            world.tryAddEntity(entity);
        }

        return properties.length == CHARACTER_NUM_PROPERTIES;
    }

    private static boolean parseBackground(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]), Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground(pt, new Background(imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    private static boolean parseMiner(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == MINER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                    Integer.parseInt(properties[MINER_ROW]));
            Entity entity = new MinerNotFull(properties[MINER_ID], pt, imageStore.getImageList(MINER_KEY),
                    Integer.parseInt(properties[MINER_LIMIT]), 0,
                    Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                    Integer.parseInt(properties[MINER_ANIMATION_PERIOD]), 0);
            world.tryAddEntity(entity);
        }
        return properties.length == MINER_NUM_PROPERTIES;
    }

    private static boolean parseObstacle(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = new Obstacle(pt, imageStore.getImageList(OBSTACLE_KEY));
            world.tryAddEntity(entity);
        }
        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    private static boolean parseOre(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == ORE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                    Integer.parseInt(properties[ORE_ROW]));
            Entity entity = new Ore(pt, imageStore.getImageList(ORE_KEY),
                    Integer.parseInt(properties[ORE_ACTION_PERIOD]));
            world.tryAddEntity(entity);
        }
        return properties.length == ORE_NUM_PROPERTIES;
    }

    private static boolean parseSmith(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == SMITH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                    Integer.parseInt(properties[SMITH_ROW]));
            Entity entity = new Blacksmith(pt, imageStore.getImageList(SMITH_KEY));

            world.tryAddEntity(entity);
        }
        return properties.length == SMITH_NUM_PROPERTIES;
    }

    private static boolean parseVein(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == VEIN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                    Integer.parseInt(properties[VEIN_ROW]));
            Entity entity = new Vein(pt, imageStore.getImageList(VEIN_KEY),
                    Integer.parseInt(properties[VEIN_ACTION_PERIOD]));
            world.tryAddEntity(entity);
        }
        return properties.length == VEIN_NUM_PROPERTIES;
    }

}
