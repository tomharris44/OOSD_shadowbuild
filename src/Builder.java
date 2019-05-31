import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** Builder unit. 
 * The code makes use of elements of the Project 1 sample solution - isPositionFree(), distance(), 
 * logic for moving unit and world-to-tile methods.
 * 
 * @author Tom Harris
 *
 */
public class Builder extends Unit {
	
	private static final String BUILDER_PATH = "assets/units/builder.png";
	private static final String BUILDER_ACTIONS = "1- Create Factory";
	private static final int FACTORY_COST = 100;
	private static final double SPEED = 0.1;
	private static final int TOTAL_BUILD_TIME = 10000;
	
	// Boolean for whether unit is currently building
	private boolean isBuilding = false;
	
	// Building time counter
	private int currentBuildTime = 0;
	
	/** Constructor for Builder
	 * @param initialX initial X coordinate for Builder unit
	 * @param initialY initial Y coordinate for Builder unit
	 * @param camera Camera object assigned to Builder
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Builder(double initialX, double initialY, Camera camera) throws SlickException {
		
		super(initialX, initialY, camera);
		
		// Set constants and initial conditions
		super.setMoveSpeed(SPEED);
		super.setImage(new Image(BUILDER_PATH));
		super.setSelected(false);
		super.setActions(BUILDER_ACTIONS);
	}
	
	// Resets building variables to initial state
	private void stopBuilding() {
		isBuilding = false;
		currentBuildTime = 0;
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
		
		// Check if Builder is currently building
		if (isBuilding) {
			
			// Add time passed in last update cycle to current build time
			currentBuildTime = currentBuildTime + world.getDelta();
			
			// Check if build time has reached the total build time
			if (currentBuildTime > TOTAL_BUILD_TIME) {
				
				// Generate a new factory at current Builder location
				world.generateSprite(new Factory(this.getX(),this.getY(),this.getCamera()));
				
				// Stop building 
				stopBuilding();
			}
		} else {
			
			// If the mouse button is being clicked, set the target to the cursor location
			if (this.equals(world.getSelected()) && input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
				super.setTargetX(super.getCamera().screenXToGlobalX(input.getMouseX()));
				super.setTargetY(super.getCamera().screenYToGlobalY(input.getMouseY()));
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
			
			// Check if Builder is currently selected, if '1' key has been pressed,
			// there is enough metal and it is possible to build at this location
			if (super.isSelected() && input.isKeyPressed(Input.KEY_1) && 
					world.getMetal() >= FACTORY_COST && world.canBuild(getX(),getY())) {
				
				// Start building
				isBuilding = true;
				
				// Subtract metal cost
				world.setMetal(world.getMetal() - FACTORY_COST);
				
				// Reset the target coordinates
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
		
		// Render Builder image at unit location
		super.getImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
				   (int)super.getCamera().globalYToScreenY(super.getY()));
	}
}
