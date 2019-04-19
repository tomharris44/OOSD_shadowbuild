import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;

// Class responsible for handling the slick game - updating game state and rendering
public class World {
	
	// Objects for game
	private TiledMap map;
	private Person person;
	private Image personImage;
	private Camera camera;
	
	// Variables for tracking game state
	private double xDest;
	private double yDest;
	private int xScreen;
	private int yScreen;
	private int xSoldier;
	private int ySoldier;
	private double direction;
	
	// Constants for game state 
	private static final double START_POS_X = 812;
	private static final double START_POS_Y = 584;
	private static final double SPEED = 0.25;
	private static final double PROXIMITY_THRESHOLD = 0.25;
	private static final int MAP_TILES_LENGTH = 30;
	private static final int SOLDIER_Y_OFFSET = 35;
	private static final int SOLDIER_X_OFFSET = 32;
	private static final int TILE_WIDTH = 64;
	private static final int MAP_LAYER = 0;
	private static final int START_X_RENDER = 0;
	private static final int START_Y_RENDER = 0;
	private static final String TILED_MAP_LOCATION = "assets/main.tmx";
	private static final String TILED_MAP_NAME = "assets";
	private static final String IMAGE_LOCATION = "assets/scout.png";
	
	
	// Constructor for World class - initializes objects and sets the destination to the current x and y position
	public World() throws SlickException {
		
		map = new TiledMap(TILED_MAP_LOCATION,TILED_MAP_NAME);
		person = new Person(START_POS_X,START_POS_Y);
		personImage = new Image(IMAGE_LOCATION);
		camera = new Camera();
		
		xDest = START_POS_X;
		yDest = START_POS_Y;
		
	}
	
	// Method for checking if the player is within the specified 0.25 pixels of destination
	public boolean isClose(double dest, double pos) {
		
		if (Math.abs(dest - pos) < PROXIMITY_THRESHOLD) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	// Method for checking if the upcoming movement enters a solid tile
	public boolean checkMove (double newX, double newY) {
		
		// Get tile x and y coordinates in Tile coordinate system
		int xTile = (int)(person.getPersonX() + newX + SOLDIER_X_OFFSET) / TILE_WIDTH;
		int yTile = (int)(person.getPersonY() + newY + SOLDIER_Y_OFFSET) / TILE_WIDTH;
		
		// Get tile ID
		int tileID = map.getTileId(xTile,yTile,MAP_LAYER);
		
		// Get tile "solid" property
		String tileState = map.getTileProperty(tileID, "solid", "false");
		
		// Check if it is solid or not and return result
		if(tileState.equals("true")) {
			return true;
		}
		
		return false;
		
	}
	
	// Method for updating the game state - x and y position of player and resulting camera state
	public void update(Input input, int delta) {
		
		
		// Check if player needs to move
		if (!isClose(xDest,person.getPersonX()) || !isClose(yDest,person.getPersonY())) {		
			
			double moveDist = SPEED * delta;
			
			double moveX = moveDist * Math.cos(direction);
			double moveY = moveDist * Math.sin(direction);
			
			// Check if solid tile is about to be entered, if so, stop moving
			if (checkMove(moveX,moveY)) {
				xDest = person.getPersonX();
				yDest = person.getPersonY();
			}
			else {
				// Check if destination is about to be reached in next move, if so, set location 
				// to destination to avoid overshoot
				if (Math.abs(xDest - person.getPersonX()) < Math.abs(moveX)) {
					person.setPersonX(xDest);
				}
				else {
					person.setPersonX(person.getPersonX() + moveX);
				}
				
				
				if (Math.abs(yDest - person.getPersonY()) < Math.abs(moveY)) {
					person.setPersonY(yDest);
				}
				else {
					person.setPersonY(person.getPersonY() + moveY);
				}
			}
		}
		
		// Determine player coordinates for where in window to be rendered
		camera.setSoldierLeft(person.getPersonX());
		camera.setSoldierTop(person.getPersonY());

		// Determine screen coordinates for map section rendering
		camera.setScreenLeft(person.getPersonX());
		camera.setScreenTop(person.getPersonY());
		
		// Get player coordinates for where in window to be rendered
		xSoldier = camera.getSoldierLeft();
		ySoldier = camera.getSoldierTop();

		// Get screen coordinates for map section rendering
		xScreen = camera.getScreenLeft();
		yScreen = camera.getScreenTop();
		
		
		
		// Check if user has specified a new destination
		if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON) == true) {
			
			// Assign new destination x and y coordinates
			// Add distance of click from current player position on screen to current x and y coordinates
			// Translates window coordinates to map coordinates for storing of map x and y coordinates in person
			xDest = person.getPersonX() + input.getMouseX() - (xSoldier + SOLDIER_X_OFFSET);
			yDest = person.getPersonY() + input.getMouseY() - (ySoldier + SOLDIER_Y_OFFSET);
			
			// Create difference variables for calculating angle from current player position
			double xDiff = input.getMouseX() - (xSoldier + SOLDIER_X_OFFSET);
			double yDiff = input.getMouseY() - (ySoldier + SOLDIER_Y_OFFSET);
			
			// Set direction of movement using trigonometric function
			direction = Math.atan2(yDiff, xDiff);
		
		}
		
	}
	
	// Method for rendering map and player to screen 
	public void render(Graphics g) {
		// Render section of map determined with variables from update method and constants about map
		map.render(xScreen, yScreen, START_X_RENDER, START_Y_RENDER, MAP_TILES_LENGTH, MAP_TILES_LENGTH);
		// Render player to window x and y coordinates determined in update method
		g.drawImage(personImage,xSoldier,ySoldier);
	}
}
