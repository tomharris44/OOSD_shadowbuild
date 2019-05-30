import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** Building parent class.
 * 
 * @author Tom Harris
 *
 */
public abstract class Building extends Sprite {
	
	private static final String HIGHLIGHT_PATH = "assets/highlight_large.png";
	
	// Image object for large highlight image
	private Image highlightImage;
	
	// Boolean for if building is selected
	private boolean isSelected;
	
	
	/** Constructor for Building
	 * @param initialX initial X coordinate for Building
	 * @param initialY initial Y coordinate for Building
	 * @param camera Camera object assigned to Building
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Building(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		this.highlightImage = new Image(HIGHLIGHT_PATH);
	}

	/** Get the large highlight Image object
	 * @return large highlight Image object
	 */
	public Image getHighlightImage() {
		return highlightImage;
	}
	
	/** Check if Building is selected
	 * @return Boolean indicating if it is selected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/** Set the building selection state
	 * @param isSelected new building selection state
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}


}
