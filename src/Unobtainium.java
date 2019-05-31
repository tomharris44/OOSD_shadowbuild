import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** Unobtainium resource. 
 * 
 * @author Tom Harris
 *
 */
public class Unobtainium extends Resource {

	private static final String UNOBTAINIUM_PATH = "assets/resources/unobtainium_mine.png";
	private static final int INITIAL_AMOUNT = 50;
	
	
	/** Constructor for Unobtainium
	 * @param initialX initial X coordinate for Unobtainium
	 * @param initialY initial Y coordinate for Unobtainium
	 * @param camera Camera object assigned to Unobtainium
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Unobtainium(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setAmount(INITIAL_AMOUNT);
		super.setImage(new Image(UNOBTAINIUM_PATH));
	}

	@Override
	public void update(World world) {
		
		// Check if mine is empty and all its resource have been delivered to a CC
		if (super.getAmount() == 0 && super.getResourcesUndelivered() == 0) {
			
			// If empty, mark this mine for deletion in world
			world.killSprite(this);
		}
	}

	@Override
	public void render() {
		
		// If mine has resources left, render it
		// 'if' loop implemented to handle case where sprite still alive while unit returns resources,
		// but should not be visible as it has been depleted
		if (super.getAmount() > 0) {
			
			// Render mine image resource location
			super.getImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
					(int)super.getCamera().globalYToScreenY(super.getY()));
		}
	}


}
