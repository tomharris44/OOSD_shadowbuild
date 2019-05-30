import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** Unit parent class.
 * 
 * @author Tom Harris
 *
 */
public abstract class Unit extends Sprite {
	
	private static final String HIGHLIGHT_PATH = "assets/highlight.png";
	
	// Unit move speed in pixels/millisecond and the small highlight Image
	private double moveSpeed;
	private Image highlightImage;
	
	// Unit target X and Y coordinates in world map when moving
	private double targetX;
	private double targetY;
	
	// Boolean for if unit is selected
	private boolean isSelected;

	
	/** Constructor for Unit
	 * @param initialX initial X coordinate for Unit
	 * @param initialY initial Y coordinate for Unit
	 * @param camera Camera object assigned to Unit
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Unit(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		
		// Initialise target coordinates to starting coordinates
		this.targetX = initialX;
		this.targetY = initialY;
		
		this.highlightImage = new Image(HIGHLIGHT_PATH);
	}

	/** Get the current target X coordinate for this unit
	 * @return current target X coordinate for this unit
	 */
	public double getTargetX() {
		return targetX;
	}

	/** Set new target X coordinate for this unit
	 * @param targetX new target X coordinate for this unit
	 */
	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}

	/** Get the current target Y coordinate for this unit
	 * @return current target Y coordinate for this unit
	 */
	public double getTargetY() {
		return targetY;
	}

	/** Set new target Y coordinate for this unit
	 * @param targetY new target Y coordinate for this unit
	 */
	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}
	
	/** Resets the target coordinates of the unit to its current location.
	 * 
	 */
	public void resetTarget() {
		targetX = super.getX();
		targetY = super.getY();		
	}

	/** Check if unit is currently selected 
	 * @return true if unit is selected in world
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/** Set new selection state of the unit
	 * @param isSelected true if has been selected
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/** Get the small highlight Image associated with unit
	 * @return small highlight Image
	 */
	public Image getHighlightImage() {
		return highlightImage;
	}

	/** Get the move speed of the unit in pixels/millisecond
	 * @return move speed of the unit in pixels/millisecond
	 */
	public double getMoveSpeed() {
		return moveSpeed;
	}

	/** Set the move speed of the unit in pixels/millisecond
	 * @param moveSpeed move speed of the unit in pixels/millisecond
	 */
	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

}
