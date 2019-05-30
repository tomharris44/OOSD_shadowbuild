import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** Truck unit. 
 * The code makes use of elements of the Project 1 sample solution - isPositionFree(), distance(), 
 * logic for moving unit and world-to-tile methods.
 * 
 * @author Tom Harris
 *
 */
public class Truck extends Unit {
	
	private static final String TRUCK_PATH = "assets/units/truck.png";
	private static final String TRUCK_ACTIONS = "1- Create Command Centre";
	private static final double SPEED = 0.25;
	private static final int TOTAL_BUILD_TIME = 15000;
	
	// Boolean for whether unit is currently building
	private boolean isBuilding = false;
	
	// Building time counter
	private int currentBuildTime = 0;

	
	/** Constructor for Truck
	 * @param initialX initial X coordinate for Truck unit
	 * @param initialY initial Y coordinate for Truck unit
	 * @param camera Camera object assigned to Truck
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Truck(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setMoveSpeed(SPEED);
		super.setImage(new Image(TRUCK_PATH));
		super.setSelected(false);
		super.setActions(TRUCK_ACTIONS);
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
		
		// Check if Truck is currently building
		if (isBuilding) {
			
			// Add time passed in last update cycle to current build time
			currentBuildTime = currentBuildTime + world.getDelta();
			
			// Check if build time has reached the total build time
			if (currentBuildTime > TOTAL_BUILD_TIME) {
				
				// Generate a new factory at current Truck location
				world.generateSprite(new CommandCentre(getX(),getY(),getCamera()));
				
				// Mark sprite for killing in world
				world.killSprite(this);
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
		}
		
		// Check if Truck is selected, '1' key has been pressed and if the space for building is unoccupied
		if (super.isSelected() && input.isKeyPressed(Input.KEY_1) && world.canBuild(getX(), getY())) {
			
			// Start building
			isBuilding = true;
		}

	}
	
	@Override
	public void render() {
		
		// Check if selected; if so, render highlight image on unit
		if (super.isSelected()) {
			super.getHighlightImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
					   (int)super.getCamera().globalYToScreenY(super.getY()));
		}
		
		// Render Truck image at unit location
		super.getImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
				   (int)super.getCamera().globalYToScreenY(super.getY()));

	}

}
