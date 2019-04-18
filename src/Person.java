public class Person {
	
	private double xpos;
	private double ypos;
	
	private final int MAP_WIDTH = 1920;
	private final int MAP_HEIGHT = 1920;
	
	public Person (double startX, double startY) {
		xpos = startX;
		ypos = startY;
	}
	
	public void setPersonX (double x) {
		if (x >= 0 && x <= MAP_WIDTH) {
			xpos = x;
		}
	}
	
	public void setPersonY (double y) {
		if (y >= 0 && y <= MAP_HEIGHT) {
			ypos = y;
		}	
	}
	
	public double getPersonX () {
		return xpos;
	}
	
	public double getPersonY () {
		return ypos;
	}
	
}
