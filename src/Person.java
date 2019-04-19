// Class describing soldier position on map
public class Person {
	
	// X and Y position in entire map, double to allow for interaction with movement method in world
	private double xpos;
	private double ypos;
	
	
	// Map width and height of total map (1920) minus 40-45px to ensure soldier can't render half off the map at edge of screen
	private static final int MAP_WIDTH = 1880;
	private static final int MAP_HEIGHT = 1875;
	
	// Soldier offset for setter methods to ensure soldier doesn't render partly off page at edge of screen
	private static final int SOLDIER_OFFSET = 20;
	
	// Constructor for person setting the initial X and Y coordinates
	public Person (double startX, double startY) {
		xpos = startX;
		ypos = startY;
	}
	
	// Setter method for X position, checks coordinate is valid i.e. within map
	public void setPersonX (double x) {
		if (x >= - SOLDIER_OFFSET && x <= MAP_WIDTH) {
			xpos = x;
		}
	}
	
	// Setter method for Y position, checks coordinate is valid i.e. within map
	public void setPersonY (double y) {
		if (y >= - SOLDIER_OFFSET && y <= MAP_HEIGHT) {
			ypos = y;
		}	
	}
	
	// Getter method for X position
	public double getPersonX () {
		return xpos;
	}
	
	// Getter method for Y position
	public double getPersonY () {
		return ypos;
	}
	
}
