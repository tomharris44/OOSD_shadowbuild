import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** Factory building. 
 * 
 * @author Tom Harris
 *
 */
public class Factory extends Building {

	private static final String FACTORY_PATH = "assets/buildings/factory.png";
	private static final String FACTORY_ACTIONS = "1- Create Truck";
	private static final int TRUCK_COST = 100;
	private static final int TRAINING_TIME = 5000;

	// Boolean to track whether Factory is currently training - true if training
	private boolean isTraining = false;

	// Counter for current time spent training
	private int currentTrainingTime = 0;
	
	/** Constructor for Factory
	 * @param initialX initial X coordinate for Factory
	 * @param initialY initial Y coordinate for Factory
	 * @param camera Camera object assigned to Factory
	 * @throws SlickException thrown if Slick encounters error
	 */
	public Factory(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setImage(new Image(FACTORY_PATH));
		super.setSelected(false);
		super.setActions(FACTORY_ACTIONS);
	}
	
	// Resets training variables to initial state
	private void stopTraining() {
		isTraining = false;
		currentTrainingTime = 0;
	}
	
	@Override
	public void update(World world) throws SlickException {
		
		// Assign latest Input object to local input variable
		Input input = world.getInput();
		
		// Set selected property if sprite is currently selected in world
		super.setSelected(this.equals(world.getSelected()));
		
		// Check if building has been selected in latest update cycle
		if (world.getSelectX() != -1 && super.hasBeenSelected(world)) {
			world.setSelected(this);
			super.setSelected(true);
		}
		
		// Check if Factory is currently training a unit 
		if (isTraining) {
			
			// Add time passed since last update to counter
			currentTrainingTime = currentTrainingTime + world.getDelta();
			
			// Check if counter has reached required time
			if (currentTrainingTime > TRAINING_TIME) {
				
				// Generate new unit in world
				world.generateSprite(new Truck(this.getX(),this.getY(),this.getCamera()));
				
				// Reset training variables
				stopTraining();
			}
		}
		
		// Check if '1' key was pressed in latest update cycle, Factory is selected and enough metal
		// If so, start training Truck and subtract metal from world total
		if (super.isSelected() && input.isKeyPressed(Input.KEY_1) && world.getMetal() >= TRUCK_COST) {
			isTraining = true;
			world.setMetal(world.getMetal() - TRUCK_COST);
		}
		
		

	}

	@Override
	public void render() {

		// Check if selected; if so, render highlight image on building
		if (super.isSelected()) {
			super.getHighlightImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
					   (int)super.getCamera().globalYToScreenY(super.getY()));
		}

		// Render Factory image at building location
		super.getImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
				   (int)super.getCamera().globalYToScreenY(super.getY()));
	}

}
