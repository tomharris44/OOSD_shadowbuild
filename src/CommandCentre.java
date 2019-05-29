import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class CommandCentre extends Building {
	
	private static final String COMMAND_CENTRE_PATH = "assets/buildings/command_centre.png";
	private static final int TRAINING_TIME = 5000;
	private static final int SCOUT_COST = 5;
	private static final int BUILDER_COST = 10;
	private static final int ENGINEER_COST = 20;
	private static final String CC_ACTIONS = "1- Create Scout\n2- Create Builder\n3- Create Engineer\n";
	
	private boolean isTraining = false;
	private int currentTrainingTime = 0;
	
	private Unit newUnit;

	public CommandCentre(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setFILE_PATH(COMMAND_CENTRE_PATH);
		super.setImage(new Image(super.getFILE_PATH()));
		super.setSelected(false);
		super.setActions(CC_ACTIONS);
	}
	
	public boolean checkInput(Input input, World world) throws SlickException {
		// check isTraining again to make sure unit creation doesn't queue
		if (input.isKeyPressed(Input.KEY_1) && world.getMetal() >= SCOUT_COST && !isTraining) {
			newUnit = new Scout(this.getX(),this.getY(),this.getCamera());
			world.setMetal(world.getMetal() - SCOUT_COST);
			return true;
		} else if (input.isKeyPressed(Input.KEY_2) && world.getMetal() >= BUILDER_COST && !isTraining) {
			newUnit = new Builder(this.getX(),this.getY(),this.getCamera());
			world.setMetal(world.getMetal() - BUILDER_COST);
			return true;
		} else if (input.isKeyPressed(Input.KEY_3) && world.getMetal() >= ENGINEER_COST && !isTraining) {
			newUnit = new Engineer(this.getX(),this.getY(),this.getCamera());
			world.setMetal(world.getMetal() - ENGINEER_COST);
			return true;
		}
		return false;
		
	}	

	@Override
	public void update(World world) throws SlickException {
		
		Input input = world.getInput();
		
		super.setSelected(this.equals(world.getSelected()));
		
		if (world.getSelectX() != -1 && super.hasBeenSelected(world, input)) {
			world.setSelected(this);
			super.setSelected(true);
		}
		
		if (!isTraining) {
			if (super.isSelected() && checkInput(input,world)) {
				isTraining = true;
			}
		} else {
			currentTrainingTime = currentTrainingTime + world.getDelta();
			if (currentTrainingTime > TRAINING_TIME) {
				world.generateSprite(newUnit);
				isTraining = false;
				currentTrainingTime = 0;
			}
			//  
			checkInput(input,world);
		}
		
		

	}

	@Override
	public void render() {
		if (super.isSelected()) {
			super.getHighlightImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
					   (int)super.getCamera().globalYToScreenY(super.getY()));
		}
		super.getImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
				   (int)super.getCamera().globalYToScreenY(super.getY()));

	}

}
