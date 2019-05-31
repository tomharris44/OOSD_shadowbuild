import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** Sprite parent class.
 * 
 * @author Tom Harris
 *
 */
public abstract class Sprite implements Comparable<Sprite> {
	
	private static final int CLOSE_THRESHOLD = 32;
	
	// Current coordinates of sprite
	private double x;
	private double y;
	
	// Sprite image
	private Image image;
	
	// Camera assigned to sprite
	private Camera camera;
	
	// Default value for actions available to sprite - reassigned for sprites with actions
	private String actions = "";
	
	/** Constructor for Sprite
	 * @param initialX initial X coordinate for Sprite
	 * @param initialY initial Y coordinate for Sprite
	 * @param camera Camera object assigned to Sprite
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Sprite (double initialX, double initialY, Camera camera) {
		setX(initialX);
		setY(initialY);
		this.camera = camera;
	}	
	
	/** Update cycle for each sprite.
	 * Monitors and stores state for each sprite and its interactions with the world
	 * @param world World object for sprite to interact with
	 * @throws SlickException thrown if Slick encounters error
	 */
	public abstract void update(World world) throws SlickException;	
	
	/** Render sequence for each sprite to be displayed on the game map.
	 */
	public abstract void render();
	
	/** Get current X coordinate of sprite
	 * @return current X coordinate of sprite
	 */
	public double getX() {
		return x;
	}

	/** Set new X coordinate of sprite
	 * @param x new X coordinate of sprite
	 */
	public void setX(double x) {
		this.x = x;
	}

	/** Get current Y coordinate of sprite
	 * @return current Y coordinate of sprite
	 */
	public double getY() {
		return y;
	}

	/** Set new Y coordinate of sprite
	 * @param y new Y coordinate of sprite
	 */
	public void setY(double y) {
		this.y = y;
	}

	/** Get the Image associated to this sprite
	 * @return Image associated to this sprite
	 */
	public Image getImage() {
		return image;
	}

	/** Set the Image associated to this sprite
	 * @param image Image associated to this sprite
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	
	/** Get the Camera associated to this sprite
	 * @return Camera associated to this sprite
	 */
	public Camera getCamera() {
		return camera;
	}
	
	/** Check if the sprite has been selected in the world
	 * @param world World sprite exists in
	 * @return true if sprite has been selected
	 */
	public boolean hasBeenSelected(World world) {
		
		// Check if selection is nearby
		if (World.distance(x, y, world.getSelectX(), world.getSelectY()) < CLOSE_THRESHOLD) {
			
			// Reset selection checks
			world.setSelectX(-1);
			world.setSelectY(-1);
			
			// Turn off free roam on camera
			camera.setFreeRoam(false);
			
			return true;
		}
		return false;
	}

	/** Get the actions of the sprite to be rendered on screen if it has any
	 * @return actions of sprite
	 */
	public String getActions() {
		return actions;
	}

	/** Set the actions of the sprite to be rendered on screen
	 * @param actions actions of sprite
	 */
	public void setActions(String actions) {
		this.actions = actions;
	}
	
	// Implementation of the CompareTo() for the Comparable interface
	public int compareTo(Sprite s) {
		
		// Compare two sprites with Unit>CommandCentre>Factory>Pylon>Resource hierarchy
		if (this.getClass().equals(s.getClass())) {
			return 0;
		} else if (this instanceof Unit) {
			return 1;
		} else if (this instanceof Building && s instanceof Resource) {
			return 1;
		} else if (this instanceof CommandCentre && (s instanceof Pylon || s instanceof Factory)) {
			return 1;
		} else if (this instanceof Factory && s instanceof Pylon) {
			return 1;
		}else {
			return -1;
		}
	}
}
