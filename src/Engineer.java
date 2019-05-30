import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** Engineer unit. 
 * The code makes use of elements of the Project 1 sample solution - isPositionFree(), distance(), 
 * logic for moving unit and world-to-tile methods.
 * 
 * @author Tom Harris
 *
 */
public class Engineer extends Unit {
	
	private static final String ENGINEER_PATH = "assets/units/engineer.png";
	private static final double SPEED = 0.1;
	private static final int TOTAL_MINING_TIME = 5000;
	private static final int MAX_CC_DIST_FROM_SPRITE = 3000;
	private static final int CLOSE_THRESHOLD = 32;

	// Boolean to track whether Engineer is currently mining - true if mining
	private boolean isMining = false;

	// Counter for current time spent mining
	private int currentMiningTime = 0;
	
	// Separated resources as these two can sometimes differ
	// Resource object corresponding to one holding
	private Resource holdingResource;
	
	// Resource object corresponding to one to return to
	private Resource returnResource;
	
	// Current amount of resources held
	private int currentAmount;
	

	/** Constructor for Engineer
	 * @param initialX initial X coordinate for Engineer unit
	 * @param initialY initial Y coordinate for Engineer unit
	 * @param camera Camera object assigned to Engineer
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Engineer(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setMoveSpeed(SPEED);
		super.setImage(new Image(ENGINEER_PATH));
		super.setSelected(false);
	}
	
	// Find closets CC and set target to its location
	private void goToClosestCC(World world) {
		
		Sprite targetComCentre = findClosestCC(world);
		super.setTargetX(targetComCentre.getX());
		super.setTargetY(targetComCentre.getY());
	}
	
	// Check if Engineer is close to a mine
	private boolean isCloseToMine(World world) {
		
		// Check each sprite in the world
		for (Sprite s: world.getSprites()) {
			
			// If sprite is close and is a resource that isn't empty, attempt to mine it
			if (World.distance(this.getX(), this.getY(), s.getX(), s.getY()) < CLOSE_THRESHOLD &&
					s instanceof Resource && ((Resource)s).getAmount() > 0) {
				
				// If not holding any resources, set as holdingResource
				if (currentAmount == 0) {
					holdingResource = (Resource)s;
				}
				
				// Set as most recent mine visited
				returnResource = (Resource)s;
				
				return true;
			}
		}
		
		return false;
	}
	
	// Check if Engineer is close to a CC
	private boolean isCloseToCC(World world) {
		
		// Check sprites in world
		for (Sprite s: world.getSprites()) {
			
			// If sprite is nearby and a CC, return true; otherwise, return false
			if (World.distance(this.getX(), this.getY(), s.getX(), s.getY()) < CLOSE_THRESHOLD &&
					s instanceof CommandCentre) {
				return true;
			}
		}
		return false;
	}
	
	// Find the closest CC to the Engineer
	private Sprite findClosestCC(World world) {
		
		// Initialise minimum distance as maximum value 
		double minDist = MAX_CC_DIST_FROM_SPRITE;
		
		// Define sprite object for closest
		Sprite closest = null;
		
		// Check each sprite in world for CC with minimum distance to Engineer
		for (Sprite s: world.getSprites()) {
			double spriteDist = World.distance(this.getX(), this.getY(), s.getX(), s.getY());
			if (s instanceof CommandCentre && spriteDist < minDist) {
				minDist = spriteDist;
				closest = s;
			}
		}
		return closest;
	}
	
	// Resets mining variables to initial state
	private void stopMining() {
		isMining = false;
		currentMiningTime = 0;
	}

	@Override
	public void update(World world) throws SlickException {
		
		// Assign latest Input object to local input variable
		Input input = world.getInput();
		
		// Set selected property if sprite is currently selected in world
		super.setSelected(this.equals(world.getSelected()));
		
		// Check if unit has been selected in latest update cycle
		if (world.getSelectX() != -1 && super.hasBeenSelected(world)) {
			world.setSelected(this);
			super.setSelected(true);
		}
		
		// If the mouse button is being clicked, set the target to the cursor location and stop mining
		// This has priority over mining so is outside mining 'if' loop
		if (this.equals(world.getSelected()) && input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			super.setTargetX(super.getCamera().screenXToGlobalX(input.getMouseX()));
			super.setTargetY(super.getCamera().screenYToGlobalY(input.getMouseY()));
			this.stopMining();
		}
		
		// Check if Engineer is currently mining; if not, check if it is close to mine and stationary
		if (isMining) {
			
			// Add time passed in last update cycle to current build time
			currentMiningTime = currentMiningTime + world.getDelta();
			
			// Check if build time has reached the total build time
			if (currentMiningTime > TOTAL_MINING_TIME) {
				
				// Get maximum amount of resources left or carry capacity
				if (holdingResource.getAmount() < world.getEngineerCarryCap()) {
					currentAmount = holdingResource.getAmount();
				} else {
					currentAmount = world.getEngineerCarryCap();
				}
				
				// If Engineer gathered resources, go to closest CC and update resource state
				if (currentAmount > 0) {
					goToClosestCC(world);
					holdingResource.setAmount(holdingResource.getAmount() - currentAmount);
					holdingResource.setResourcesUndelivered(holdingResource.getResourcesUndelivered() + currentAmount);
				}
				
				// Stop mining
				this.stopMining();
			}
		} else if(super.getTargetX() == super.getX() &&
				super.getTargetY() == super.getY() && 
				isCloseToMine(world)){
			
			// check if Engineer already has resources; if so, send to closest CC
			if (currentAmount == 0) {
				isMining = true;
			} else {
				goToClosestCC(world);
			}
		}
		
		// Check if Engineer is close to CC and has resources to drop off
		if (isCloseToCC(world) && currentAmount > 0) {
			
			// Check type and update world count
			if (holdingResource instanceof Unobtainium) {
				world.setUnobtainium(world.getUnobtainium() + currentAmount);
			} else {
				world.setMetal(world.getMetal() + currentAmount);
			}
			
			// Send Engineer back to most recent mine
			super.setTargetX(returnResource.getX());
			super.setTargetY(returnResource.getY());
			
			// Update resource state
			holdingResource.setResourcesUndelivered(holdingResource.getResourcesUndelivered() - currentAmount);
			
			// Update Engineer resource amount and reset Resource memory
			currentAmount = 0;
			holdingResource = null;
			returnResource = null;
		}
		
		
		// If we're close to our target, reset to our current position
		if (World.distance(getX(), getY(), getTargetX(), getTargetY()) <= (world.getDelta() * super.getMoveSpeed())) {
			super.setX(super.getTargetX());
			super.setY(super.getTargetY());
		} else {
			// Calculate the appropriate x and y distances
			double theta = Math.atan2(getTargetY() - getY(), getTargetX() - getX());
			double dx = (double)Math.cos(theta) * world.getDelta() * super.getMoveSpeed();
			double dy = (double)Math.sin(theta) * world.getDelta() * super.getMoveSpeed();
			// Check the tile is free before moving; otherwise, we stop moving
			if (world.isPositionFree(getX() + dx, getY() + dy)) {
				setX(getX() + dx);
				setY(getY() + dy);
			} else {
				super.resetTarget();
			}
		}
	}

	@Override
	public void render() {

		// Check if selected; if so, render highlight image on unit
		if (super.isSelected()) {
			super.getHighlightImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
					   (int)super.getCamera().globalYToScreenY(super.getY()));
		}

		// Render Engineer image at unit location
		super.getImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
				   (int)super.getCamera().globalYToScreenY(super.getY()));

	}

}
