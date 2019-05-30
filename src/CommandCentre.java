import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** CC building.
 * 
 * @author Tom Harris
 *
 */
public class CommandCentre extends Building {
	
	private static final String COMMAND_CENTRE_PATH = "assets/buildings/command_centre.png";
	private static final int TRAINING_TIME = 5000;
	private static final int SCOUT_COST = 5;
	private static final int BUILDER_COST = 10;
	private static final int ENGINEER_COST = 20;
	private static final String CC_ACTIONS = "1- Create Scout\n2- Create Builder\n3- Create Engineer\n";
	
	// Boolean to track whether CC is currently training - true if training
	private boolean isTraining = false;
	
	// Counter for current time spent training 
	private int currentTrainingTime = 0;
	
	// Sprite for new unit creation
	private Unit newUnit;

	/**Constructor for Command Centre
	 * @param initialX initial X coordinate for CC
	 * @param initialY initial Y coordinate for CC
	 * @param camera Camera object assigned to CC
	 * @throws SlickException thrown if Slick encounters error
	 */
	public CommandCentre(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setImage(new Image(COMMAND_CENTRE_PATH));
		super.setSelected(false);
		super.setActions(CC_ACTIONS);
	}
	
	// Check for whether input was activated
	private boolean checkInput(Input input, World world) throws SlickException {
		
		// Check '1' key was pressed and there is enough metal to train unit
		// Check isTraining again to make sure unit creation doesn't queue
		if (input.isKeyPressed(Input.KEY_1) && world.getMetal() >= SCOUT_COST && !isTraining) {
			
			// Generate new unit to be spawned
			newUnit = new Scout(this.getX(),this.getY(),this.getCamera());
			
			// Subtract metal cost from total
			world.setMetal(world.getMetal() - SCOUT_COST);
			
			// Return that input was activated and unit can be trained
			return true;
			
			// Check '2' key was pressed and there is enough metal to train unit
			// Check isTraining again to make sure unit creation doesn't queue
		} else if (input.isKeyPressed(Input.KEY_2) && world.getMetal() >= BUILDER_COST && !isTraining) {
			
			// Generate new unit to be spawned
			newUnit = new Builder(this.getX(),this.getY(),this.getCamera());
			
			// Subtract metal cost from total
			world.setMetal(world.getMetal() - BUILDER_COST);
			
			// Return that input was activated and unit can be trained
			return true;
			
			// Check '3' key was pressed and there is enough metal to train unit
			// Check isTraining again to make sure unit creation doesn't queue
		} else if (input.isKeyPressed(Input.KEY_3) && world.getMetal() >= ENGINEER_COST && !isTraining) {
			
			// Generate new unit to be spawned
			newUnit = new Engineer(this.getX(),this.getY(),this.getCamera());
			
			// Subtract metal cost from total
			world.setMetal(world.getMetal() - ENGINEER_COST);
			
			// Return that input was activated and unit can be trained
			return true;
		}
		
		// Otherwise return false
		return false;
		
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
		
		// Check if CC is currently training a unit 
		if (!isTraining) {
			
			// If not, check if currently selected and input was activated
			if (super.isSelected() && checkInput(input,world)) {
				
				// Start training
				isTraining = true;
			}
		} else {
			
			// Add time passed since last update to counter
			currentTrainingTime = currentTrainingTime + world.getDelta();
			
			// Check if counter has reached required time
			if (currentTrainingTime > TRAINING_TIME) {
				
				// Generate new unit in world
				world.generateSprite(newUnit);
				
				// Reset training variables
				stopTraining();
			}
			
			// Check input to make sure requested unit creations don't queue
			checkInput(input,world);
		}
		
		

	}

	@Override
	public void render() {
		
		// Check if selected; if so, render highlight image on building
		if (super.isSelected()) {
			super.getHighlightImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
					   (int)super.getCamera().globalYToScreenY(super.getY()));
		}
		
		// Render CC image at building location
		super.getImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
				   (int)super.getCamera().globalYToScreenY(super.getY()));
	}

}
