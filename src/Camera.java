// Class for managing the camera coordinates
public class Camera {
	
	// Constants for the x and y coordinate thresholds for when the camera needs to render an edge of the map
	private static final int SCREEN_LEFT_THRESHOLD = 482;
	private static final int SCREEN_RIGHT_THRESHOLD = 1378;
	private static final int SCREEN_TOP_THRESHOLD = 354;
	private static final int SCREEN_BOTTOM_THRESHOLD = 1506;
	
	// Constants for x and y coordinates for rendering image in centre of window 
	private static final int SCREEN_CENTRE_X = 482;
	private static final int SCREEN_CENTRE_Y = 354;
	
	// Constants for where to render the left and top of screen when rendering edge of map
	private static final int EDGE_OF_SCREEN_MIN = 0;
	private static final int EDGE_OF_SCREEN_MAX_X = -896;
	private static final int EDGE_OF_SCREEN_MAX_Y = -1152;
	
	private int xScreen;
	private int yScreen;
	private int xSoldier;
	private int ySoldier;

	// Method for getting the x map coordinate for the left side of the screen based off the current position of the soldier
	public void setScreenLeft(double xpos) {
		if (xpos < SCREEN_LEFT_THRESHOLD) {
			xScreen = EDGE_OF_SCREEN_MIN;
		}
		else if (xpos > SCREEN_RIGHT_THRESHOLD) {
			xScreen = EDGE_OF_SCREEN_MAX_X;
		}
		else {
			xScreen = - (int)(xpos - SCREEN_CENTRE_X);
		}
	}
	
	// Method for getting the x window coordinate for the soldier based off the current position of the soldier in the map
	public void setSoldierLeft(double xpos) {
		if (xpos < SCREEN_LEFT_THRESHOLD) {
			xSoldier = (int)xpos;
		}
		else if (xpos > SCREEN_RIGHT_THRESHOLD) {
			xSoldier = (int)xpos + EDGE_OF_SCREEN_MAX_X;
		}
		else {
			xSoldier = SCREEN_CENTRE_X;
		}
	}
	
	// Method for getting the y map coordinate for the left side of the screen based off the current position of the soldier
	public void setScreenTop (double ypos) {
		if (ypos < SCREEN_TOP_THRESHOLD) {
			yScreen = EDGE_OF_SCREEN_MIN;
			
		}
		else if (ypos > SCREEN_BOTTOM_THRESHOLD) {
			yScreen = EDGE_OF_SCREEN_MAX_Y;
			
		}
		else {
			yScreen = - (int)(ypos - SCREEN_CENTRE_Y);	
		}
	}
	
	// Method for getting the y window coordinate for the soldier based off the current position of the soldier in the map
	public void setSoldierTop (double ypos) {
		if (ypos < SCREEN_TOP_THRESHOLD) {
			ySoldier = (int)ypos;
			
		}
		else if (ypos > SCREEN_BOTTOM_THRESHOLD) {
			ySoldier = (int)ypos + EDGE_OF_SCREEN_MAX_Y;
			
		}
		else {
			ySoldier = SCREEN_CENTRE_Y;
		}
	}
	
	// Getter method for map left of screen map x coordinate
	public int getScreenLeft() {
		return xScreen;
	}
	
	// Getter method for soldier window x coordinate
	public int getSoldierLeft() {
		return xSoldier;
	}
	
	// Getter method for map top of screen map y coordinate
	public int getScreenTop() {
		return yScreen;
	}
	
	// Getter method for soldier window y coordinate
	public int getSoldierTop() {
		return ySoldier;
	}
}
