import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** World class describes the state of the game and includes the sprites and camera which control
 * various aspects of the game.
 * The code makes use of elements of the Project 1 sample solution - isPositionFree(), distance() 
 * and world-to-tile methods.
 * 
 * @author tomharris44
 * 
 */
public class World {
	private static final String MAP_PATH = "assets/main.tmx";
	private static final String SOLID_PROPERTY = "solid";
	private static final String OCCUPY_PROPERTY = "occupied";
	private static final String INITIAL_OBJECTS_PATH = "assets/objects.csv";
	
	// mapping of csv file sprite strings to static variables
	private static final String COMMAND_CENTRE = "command_centre";
	private static final String METAL_MINE = "metal_mine";
	private static final String UNOBTAINIUM_MINE = "unobtainium_mine";
	private static final String PYLON = "pylon";
	private static final String ENGINEER = "engineer";
	private static final String BUILDER = "builder";
	private static final String SCOUT = "scout";
	private static final String TRUCK = "truck";
	private static final String FACTORY = "factory";
	
	// Sprite ArrayLists for current spritelist and new/dead sprites
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private ArrayList<Sprite> newSprites = new ArrayList<Sprite>();
	private ArrayList<Sprite> deadSprites = new ArrayList<Sprite>();
	
	// map and camera objects
	private TiledMap map;
	private Camera camera = new Camera();
	
	// variables for latest Input object and delta time
	private Input lastInput;
	private int lastDelta;
	
	// sprite object for selected unit/building
	private Sprite selected = null;
	
	// coordinates for latest selection attempt - both stay as -1 if no selection made or selection
	// made at position where no unit/building is
	private double selectX = -1;
	private double selectY = -1;
	
	// metal and unobtainium count for player
	private int metal = 0;
	private int unobtanium = 0;
	
	// engineer carry capacity - increased by activation of pylons
	private int engineerCarryCap = 2;
	
	
	/** Get latest Input object
	 * @return latest Input object for sprites and buildings to interact with
	 */
	public Input getInput() {
		return lastInput;
	}
	
	
	/** Get latest delta time
	 * @return latest delta object for update() and render() methods of game objects to interact with
	 */
	public int getDelta() {
		return lastDelta;
	}
	
	
	/** Checks if the tile for the given coordinates is free 
	 * @param x map x coordinate that position is to be checked for
	 * @param y map y coordinate that position is to be checked for
	 * @return boolean of whether map tile is free
	 */
	public boolean isPositionFree(double x, double y) {
		int tileId = map.getTileId(worldXToTileX(x), worldYToTileY(y), 0);
		return !Boolean.parseBoolean(map.getTileProperty(tileId, SOLID_PROPERTY, "false"));
	}
	
	
	/** Checks if the tile for given coordinates can be built on - checks for occupied property
	 * @param x X-coordinate for point to be checked on map
	 * @param y Y-coordinate for point to be checked on map
	 * @return boolean for whether it is possible to build at this location
	 */
	public boolean canBuild(double x, double y) {
		int tileId = map.getTileId(worldXToTileX(x), worldYToTileY(y), 0);
		return !Boolean.parseBoolean(map.getTileProperty(tileId, OCCUPY_PROPERTY, "false"));
	}
	
	
	/** 
	 * @return map width in pixels
	 */
	public double getMapWidth() {
		return map.getWidth() * map.getTileWidth();
	}

	
	/**
	 * @return map height in pixels
	 */
	public double getMapHeight() {
		return map.getHeight() * map.getTileHeight();
	}
	
	
	/** Reads in list of sprites from a csv stored at a given pathname 
	 * @param objectsPath pathname of csv
	 * @throws IOException thrown when list has been fully read
	 */
	public void readSprites(String objectsPath) throws IOException {
		
		// reader for csv file
		BufferedReader init_objects_reader = new BufferedReader(new FileReader(objectsPath));
		
		// while loop that reads in whole csv file before breaking
		while (true) {
			try {
				
				// read in new line
				String newLine = init_objects_reader.readLine();
				
				// split line on comma to separate type of sprite with coordinates and store in array
				String[] newSpriteInfo = newLine.split(",");
				
				// switch to add appropriate sprite type at specified location,
				// only included sprite types within csv file here, but could add other types 
				switch(newSpriteInfo[0]) {
				
					case(COMMAND_CENTRE): {
						sprites.add(new CommandCentre(Integer.parseInt(newSpriteInfo[1]),
								Integer.parseInt(newSpriteInfo[2]),
								camera));
						break;
					}
					
					case(METAL_MINE): {
						sprites.add(new Metal(Integer.parseInt(newSpriteInfo[1]),
								Integer.parseInt(newSpriteInfo[2]),
								camera));
						break;
					}
					
					case(UNOBTAINIUM_MINE): {
						sprites.add(new Unobtainium(Integer.parseInt(newSpriteInfo[1]),
								Integer.parseInt(newSpriteInfo[2]),
								camera));
						break;
					}
					
					case(PYLON): {
						sprites.add(new Pylon(Integer.parseInt(newSpriteInfo[1]),
								Integer.parseInt(newSpriteInfo[2]),
								camera));
						break;
					}
					
					case(ENGINEER): {
						sprites.add(new Engineer(Integer.parseInt(newSpriteInfo[1]),
								Integer.parseInt(newSpriteInfo[2]),
								camera));
						break;
					}
				}
			} catch (Exception e) {
				// close reader when read in all csv items
				init_objects_reader.close();
				break;
			}
		}
		
	}
	
	/** Appends a newly generated sprite to the queue for creation in the world
	 * @param sprite sprite to be generated
	 */
	public void generateSprite(Sprite sprite) {
		newSprites.add(sprite);
	}
	
	/** Appends sprite to queue for deletion in the world
	 * @param sprite sprite to be deleted
	 */
	public void killSprite(Sprite sprite) {
		deadSprites.add(sprite);
	}
	
	/** Constructor for World class - creates map and intital sprites
	 * @throws SlickException thrown if error related to slick update/render procedure made
	 * @throws IOException thrown if error related to reading in new sprites
	 */
	public World() throws SlickException, IOException {
		map = new TiledMap(MAP_PATH);
		this.readSprites(INITIAL_OBJECTS_PATH);
		Collections.sort(sprites);
	}
	
	/** Updates the world state - runs the various update sequences of world (sprites and map)
	 * @param input input object from Slick App
	 * @param delta time passed since last update sequence in milliseconds
	 * @throws SlickException thrown if error related to slick update/render procedure made
	 */
	public void update(Input input, int delta) throws SlickException {
		lastInput = input;
		lastDelta = delta;
		
		//reverse list for updating - gives preference to units with selection
		Collections.reverse(sprites);
		
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			selectX = camera.screenXToGlobalX(input.getMouseX());
			selectY = camera.screenYToGlobalY(input.getMouseY());
		}
		
		camera.update(this);
		
		
		for (Sprite s: sprites) {
			s.update(this);
		}
		
		if (!deadSprites.isEmpty()) {
			for (Sprite s: deadSprites) {
				sprites.remove(s);
				System.out.println(s.toString());
			}
			deadSprites.clear();
		}
		
		if (!newSprites.isEmpty()) {
			for (Sprite s: newSprites) {
				sprites.add(s);
			}
			newSprites.clear();
		}
		
		//sort list for rendering - renders units over the top of buildings and resources
		Collections.reverse(sprites);
		Collections.sort(sprites);
		
		if (selectX != -1) {
			selectX = -1;
			selectY = -1;
			selected = null;
			camera.setFreeRoam(true);
		}
		
	}
	
	public void render(Graphics g) {
		map.render((int)camera.globalXToScreenX(0),
				   (int)camera.globalYToScreenY(0));
		for (Sprite s: sprites) {
			s.render();
		}
		g.drawString("Metal: " + metal + "\nUnobtainium: " + unobtanium, 32, 32);
		if (selected != null) {
			g.drawString(selected.getActions(), 32, 100);
		}
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return (double)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	private int worldXToTileX(double x) {
		return (int)(x / map.getTileWidth());
	}
	private int worldYToTileY(double y) {
		return (int)(y / map.getTileHeight());
	}
	public Sprite getSelected() {
		return selected;
	}
	public void setSelected(Sprite selected) {
		this.selected = selected;
	}
	public double getSelectX() {
		return selectX;
	}
	public void setSelectX(double selectX) {
		this.selectX = selectX;
	}
	public double getSelectY() {
		return selectY;
	}
	public void setSelectY(double selectY) {
		this.selectY = selectY;
	}
	public ArrayList<Sprite> getSprites() {
		return sprites;
	}

	public int getEngineerCarryCap() {
		return engineerCarryCap;
	}
	public void setEngineerCarryCap(int engineerCarryCap) {
		this.engineerCarryCap = engineerCarryCap;
	}
	public int getMetal() {
		return metal;
	}
	public void setMetal(int metal) {
		this.metal = metal;
	}
	public int getUnobtanium() {
		return unobtanium;
	}
	public void setUnobtanium(int unobtanium) {
		this.unobtanium = unobtanium;
	}
}
