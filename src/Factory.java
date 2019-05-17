import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Factory extends Building {
	
	//TODO: implement metal cost for truck production

	private static final String FACTORY_PATH = "assets/buildings/factory.png";
	private static final String FACTORY_ACTIONS = "1- Create Truck";
	
	private static final int TRAINING_TIME = 5000;
	private boolean isTraining = false;
	private int currentTrainingTime = 0;
	
	public Factory(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setFILE_PATH(FACTORY_PATH);
		super.setImage(new Image(super.getFILE_PATH()));
		super.setSelected(false);
		super.setActions(FACTORY_ACTIONS);
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
			if (super.isSelected() && input.isKeyPressed(Input.KEY_1)) {
				isTraining = true;
			}
		} else {
			currentTrainingTime = currentTrainingTime + world.getDelta();
			if (currentTrainingTime > TRAINING_TIME) {
				world.generateSprite(new Truck(this.getX(),this.getY(),this.getCamera()));
				isTraining = false;
				currentTrainingTime = 0;
			}
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
