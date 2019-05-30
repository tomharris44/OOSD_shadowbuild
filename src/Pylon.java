import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** Pylon building. 
 * 
 * @author Tom Harris
 *
 */
public class Pylon extends Building {
	
	private static final String PYLON_PATH = "assets/buildings/pylon.png";
	private static final String ACTIVATED_PATH = "assets/buildings/pylon_active.png";
	private static final int CLOSE_THRESHOLD = 32;
	
	// Boolean to track whether Pylon is currently active - true if active
	private boolean isActive;

	
	/** Constructor for Pylon
	 * @param initialX initial X coordinate for Pylon
	 * @param initialY initial Y coordinate for Pylon
	 * @param camera Camera object assigned to Pylon
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Pylon(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setImage(new Image(PYLON_PATH));
		isActive = false;
	}
	
	// Check if unit is close to Pylon
	private boolean unitClose(World world) {
		
		// Check if any close sprites are units
		for (Sprite s: world.getSprites()) {
			if (World.distance(this.getX(), this.getY(), s.getX(), s.getY()) < CLOSE_THRESHOLD && 
					s instanceof Unit) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void update(World world) throws SlickException {
		
		// Set selected property if sprite is currently selected in world
		super.setSelected(this.equals(world.getSelected()));
		
		// Check if building has been selected in latest update cycle
		if (world.getSelectX() != -1 && super.hasBeenSelected(world)) {
			world.setSelected(this);
			super.setSelected(true);
		}
		
		// Check if Pylon has not been activated and if a unit is close; if so, activate Pylon
		if (!isActive && unitClose(world)) {
			super.setImage(new Image(ACTIVATED_PATH));
			isActive = true;
			
			// Update Engineer carry capacity
			world.setEngineerCarryCap(world.getEngineerCarryCap() + 1);
		}
	}

	@Override
	public void render() {
		
		// Check if selected; if so, render highlight image on building
		if (super.isSelected()) {
			super.getHighlightImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
					   (int)super.getCamera().globalYToScreenY(super.getY()));
		}
		
		// Render Pylon image at building location
		super.getImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
				   (int)super.getCamera().globalYToScreenY(super.getY()));

	}

}
