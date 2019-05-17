import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Pylon extends Building {
	
	private static final String PYLON_PATH = "assets/buildings/pylon.png";
	private static final String ACTIVATED_PATH = "assets/buildings/pylon_active.png";

	private boolean isActive;

	public Pylon(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setFILE_PATH(PYLON_PATH);
		super.setImage(new Image(super.getFILE_PATH()));
		isActive = false;
	}
	
	public boolean unitClose(World world) {
		for (Sprite s: world.getSprites()) {
			if (s instanceof Unit && 
					World.distance(this.getX(), this.getY(), s.getX(), s.getY()) < 32) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void update(World world) throws SlickException {
		
		Input input = world.getInput();
		
		if (!isActive && unitClose(world)) {
			super.setFILE_PATH(ACTIVATED_PATH);
			// might not need to redefine
			super.setImage(new Image(super.getFILE_PATH()));
			isActive = true;
			world.setEngineerCarryCap(world.getEngineerCarryCap() + 1);
		}
		
		super.setSelected(this.equals(world.getSelected()));
		
		if (world.getSelectX() != -1 && super.hasBeenSelected(world, input)) {
			world.setSelected(this);
			super.setSelected(true);
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
