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
 * @author Tom Harris
 * 
 */
public class World {
	
	private static final String MAP_PATH = "assets/main.tmx";
	private static final String SOLID_PROPERTY = "solid";
	private static final String OCCUPY_PROPERTY = "occupied";
	private static final String INITIAL_OBJECTS_PATH = "assets/objects.csv";
	private static final int RESOURCE_COUNT_X_POS = 32;
	private static final int RESOURCE_COUNT_Y_POS = 32;
	private static final int ACTIONS_X_POS = 32;
	private static final int ACTIONS_Y_POS = 100;
	
	// Mapping of csv file sprite strings to static variables
	private static final String COMMAND_CENTRE = "command_centre";
	private static final String METAL_MINE = "metal_mine";
	private static final String UNOBTAINIUM_MINE = "unobtainium_mine";
	private static final String PYLON = "pylon";
	private static final String ENGINEER = "engineer";
	
	// Not included other constants for reading in other sprite types into csv
//	private static final String BUILDER = "builder";
//	private static final String SCOUT = "scout";
//	private static final String TRUCK = "truck";
//	private static final String FACTORY = "factory";
	
	// Sprite ArrayLists for current spritelist and new/dead sprites
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private ArrayList<Sprite> newSprites = new ArrayList<Sprite>();
	private ArrayList<Sprite> deadSprites = new ArrayList<Sprite>();
	
	// Map and camera objects
	private TiledMap map;
	private Camera camera = new Camera();
	
	// Variables for latest Input object and delta time
	private Input lastInput;
	private int lastDelta;
	
	// Sprite object for selected unit/building
	private Sprite selected = null;
	
	// Coordinates for latest selection attempt - both stay as -1 if no selection made or selection
	// made at position where no unit/building is
	private double selectX = -1;
	private double selectY = -1;
	
	// Metal and Unobtainium count for player
	private int metal = 0;
	private int unobtainium = 0;
	
	// Engineer carry capacity - increased by activation of pylons
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
		
		// Reader for csv file
		BufferedReader init_objects_reader = new BufferedReader(new FileReader(objectsPath));
		
		// While loop that reads in whole csv file before breaking
		while (true) {
			try {
				
				// Read in new line
				String newLine = init_objects_reader.readLine();
				
				// Split line on comma to separate type of sprite with coordinates and store in array
				String[] newSpriteInfo = newLine.split(",");
				
				// Switch to add appropriate sprite type at specified location,
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
				
				// Close reader when read in all csv items
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
		// Update input and delta with latest values
		lastInput = input;
		lastDelta = delta;
		
		// Reverse list for updating - gives preference to units for selection
		Collections.reverse(sprites);
		
		// Check for selection and store selection coordinates
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			selectX = camera.screenXToGlobalX(input.getMouseX());
			selectY = camera.screenYToGlobalY(input.getMouseY());
		}
		
		// Update camera state 
		camera.update(this);
		
		// Update each Sprite's state
		for (Sprite s: sprites) {
			s.update(this);
		}
		
		// Check if there are Sprites to delete
		if (!deadSprites.isEmpty()) {
			
			// Delete each sprite designated from current sprite list
			for (Sprite s: deadSprites) {
				sprites.remove(s);
			}
			
			// Clear the dead processed sprites
			deadSprites.clear();
		}
		
		// Check if there are Sprites to create
		if (!newSprites.isEmpty()) {
			
			// Add new sprites to current sprite list
			for (Sprite s: newSprites) {
				sprites.add(s);
			}
			
			// Clear the new processed sprites
			newSprites.clear();
		}
		
		//Sort list for rendering - renders units over the top of buildings and resources
		Collections.reverse(sprites);
		Collections.sort(sprites);
		
		// Check if selection was made  
		// -1 indicates either no selection was made or a selection has been processed
		// ! -1 indicates selection was made at a place where no unit exists and selection 
		// coordinates need to be reset
		if (selectX != -1) {
			selectX = -1;
			selectY = -1;
			selected = null;
			
			// Set camera to free roam mode when left click not on unit made
			camera.setFreeRoam(true);
		}
		
	}
	
	/** Render all of game state on screen - current sprites, resource count and actions
	 * @param g graphics object for Slick2D
	 */
	public void render(Graphics g) {
		
		// Render map at camera position
		map.render((int)camera.globalXToScreenX(0),
				   (int)camera.globalYToScreenY(0));
		
		// Render each Sprite's state
		for (Sprite s: sprites) {
			s.render();
		}
		
		// Render resource count
		g.drawString("Metal: " + metal + "\nUnobtainium: " + unobtainium, RESOURCE_COUNT_X_POS, RESOURCE_COUNT_Y_POS);
		
		// Render actions of selected unit/building if it exists
		if (selected != null) {
			g.drawString(selected.getActions(), ACTIONS_X_POS, ACTIONS_Y_POS);
		}
	}
	
	
	/** Determine distance between two coordinates in the map
	 * @param x1 X coordinate of first position
	 * @param y1 Y coordinate of first position
	 * @param x2 X coordinate of second position
	 * @param y2 Y coordinate of second position
	 * @return distance between two coordinates
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return (double)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	// Converts world X coordinate to tile X coordinate
	private int worldXToTileX(double x) {
		return (int)(x / map.getTileWidth());
	}
	
	// Converts world Y coordinate to tile Y coordinate
	private int worldYToTileY(double y) {
		return (int)(y / map.getTileHeight());
	}
	
	/** Get currently selected sprite
	 * @return current selected sprite
	 */
	public Sprite getSelected() {
		return selected;
	}
	
	/** Set currently selected sprite
	 * @param selected newly selected sprite
	 */
	public void setSelected(Sprite selected) {
		this.selected = selected;
	}
	
	/** Get X coordinate of latest selection
	 * @return X coordinate of latest selection
	 */
	public double getSelectX() {
		return selectX;
	}
	
	/** Set X coordinate of latest selection
	 * @param selectX new X coordinate of selection
	 */
	public void setSelectX(double selectX) {
		this.selectX = selectX;
	}
	
	/** Get Y coordinate of latest selection
	 * @return Y coordinate of latest selection
	 */
	public double getSelectY() {
		return selectY;
	}
	
	/** Set Y coordinate of latest selection
	 * @param selectY new Y coordinate of selection
	 */
	public void setSelectY(double selectY) {
		this.selectY = selectY;
	}
	
	/** Get current ArrayList of sprites
	 * @return current ArrayList of sprites
	 */
	public ArrayList<Sprite> getSprites() {
		return sprites;
	}

	/** Get current Engineer carry capacity
	 * @return current Engineer carry capacity
	 */
	public int getEngineerCarryCap() {
		return engineerCarryCap;
	}
	
	/** Set new Engineer carry capacity
	 * @param engineerCarryCap new Engineer carry capacity
	 */
	public void setEngineerCarryCap(int engineerCarryCap) {
		this.engineerCarryCap = engineerCarryCap;
	}
	
	/** Get latest metal count 
	 * @return latest metal count 
	 */
	public int getMetal() {
		return metal;
	}
	
	/** Set new metal count 
	 * @param metal new metal count
	 */
	public void setMetal(int metal) {
		this.metal = metal;
	}
	
	/** Get latest unobtainium count 
	 * @return latest unobtainium count 
	 */
	public int getUnobtainium() {
		return unobtainium;
	}
	
	/** Set new unobtainium count
	 * @param unobtainium new unobtainium count
	 */
	public void setUnobtainium(int unobtainium) {
		this.unobtainium = unobtainium;
	}
}
