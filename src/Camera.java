import org.newdawn.slick.Input;

/** Camera class maintains the state of the game view; keeps track of area of map in view, 
 * whether a target is selected and if it is free roaming.
 * The code makes use of elements of the Project 1 sample solution - screen/global coordinate conversion methods,
 * followSprite() and logic for assignment of x and y boundaries.
 * 
 * @author Tom Harris
 * 
 */
public class Camera {
	
	private static final double CAMERA_SPEED = 0.4;
	
	// Coordinates for boundaries of game view
	private double x = 300;
	private double y = 300;
	
	// Target sprite
	private Sprite target;
	
	// Boolean to indicate if camera is in free roam mode
	private boolean freeRoam = true;
	
	
	/** Set target sprite for when following
	 * @param target Sprite to be followed
	 */
	public void followSprite(Sprite target) {
		this.target = target;
	}
	
	/** Convert between global X coordinate and screen X coordinate
	 * @param x global X coordinate
	 * @return screen X coordinate
	 */
	public double globalXToScreenX(double x) {
		return x - this.x;
	}
	/** Convert between global Y coordinate and screen Y coordinate
	 * @param y global Y coordinate
	 * @return screen Y coordinate
	 */
	public double globalYToScreenY(double y) {
		return y - this.y;
	}

	/** Convert between screen X coordinate and global X coordinate
	 * @param x screen X coordinate
	 * @return global X coordinate
	 */
	public double screenXToGlobalX(double x) {
		return x + this.x;
	}
	/** Convert between screen Y coordinate and global Y coordinate
	 * @param y screen Y coordinate
	 * @return global Y coordinate
	 */
	public double screenYToGlobalY(double y) {
		return y + this.y;
	}
	
	/** Update cycle for camera
	 * @param world game state
	 */
	public void update(World world) {
		
		// Assign variable for latest Input
		Input input = world.getInput();
		
		// set target variables to current boundaries
		double targetX = x;
		double targetY = y;
		
		// Check if vertical camera controls were triggered; if so, update target Y coordinate and start free roam mode
		if (input.isKeyDown(Input.KEY_W)) {
			targetY = y - world.getDelta() * CAMERA_SPEED;
			freeRoam = true;
		} else if (input.isKeyDown(Input.KEY_S)) {
			targetY = y + world.getDelta() * CAMERA_SPEED;
			freeRoam = true;
		}
		
		// Check if horizontal camera controls were triggered; if so, update target X coordinate and start free roam mode
		if (input.isKeyDown(Input.KEY_A)) {
			targetX = x - world.getDelta() * CAMERA_SPEED;
			freeRoam = true;
		} else if (input.isKeyDown(Input.KEY_D)) {
			targetX = x + world.getDelta() * CAMERA_SPEED;
			freeRoam = true;
		}
		
		// Check if in free roam mode; if not, follow selected sprite
		if (!freeRoam) {
			
			this.followSprite(world.getSelected());
			
			// update target boundaries with new target sprite location
			targetX = target.getX() - App.WINDOW_WIDTH / 2;
			targetY = target.getY() - App.WINDOW_HEIGHT / 2;
			
			
		}
		
		// Adjust boundaries for edge of map
		x = Math.min(targetX, world.getMapWidth() -	 App.WINDOW_WIDTH);
		x = Math.max(x, 0);
		y = Math.min(targetY, world.getMapHeight() - App.WINDOW_HEIGHT);
		y = Math.max(y, 0);
	}

	/** Check if currently in free roam mode
	 * @return true if in free roam mode
	 */
	public boolean isFreeRoam() {
		return freeRoam;
	}


	/** Switch camera in/out of free roam mode
	 * @param freeRoam true if set to in free roam mode
	 */
	public void setFreeRoam(boolean freeRoam) {
		this.freeRoam = freeRoam;
	}
}
