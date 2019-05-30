import org.newdawn.slick.SlickException;

/** Resource parent class.
 * 
 * @author Tom Harris
 *
 */
public abstract class Resource extends Sprite {
	
	// Amount left in mine
	private int amount;
	
	// Amount of resource currently carried by engineers in world
	private int resourcesUndelivered = 0;

	/** Constructor for Resource
	 * @param initialX initial X coordinate for Resource
	 * @param initialY initial Y coordinate for Resource
	 * @param camera Camera object assigned to Resource
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Resource(double initialX, double initialY, Camera camera) {
		super(initialX, initialY, camera);
	}
	
	
	/** Get the current amount of resource left in mine
	 * @return current amount of resource left in mine
	 */
	public int getAmount() {
		return amount;
	}

	/** Set the new amount of resource left in mine
	 * @param amount new amount of resource left
	 */
	public void setAmount(int amount) {
		
		// If new amount is less than 0, let new amount be 0
		if (amount < 0) {
			this.amount = 0;
		} else {
			this.amount = amount;
		}
	}

	/** Get current amount of resource being carried by engineers in world
	 * @return current amount of resource being carried by engineers in world
	 */
	public int getResourcesUndelivered() {
		return resourcesUndelivered;
	}

	/** Set new amount of resource being carried by engineers in world
	 * @param resourcesUndelivered new amount of resource being carried by engineers in world
	 */
	public void setResourcesUndelivered(int resourcesUndelivered) {
		
		// If new amount is less than 0, let new amount be 0
		if (resourcesUndelivered < 0) {
			this.resourcesUndelivered = 0;
		} else {
			this.resourcesUndelivered = resourcesUndelivered;
		}
	}

}
