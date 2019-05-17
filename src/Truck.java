import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Truck extends Unit {
	
	private static final String TRUCK_PATH = "assets/units/truck.png";
	private static final String TRUCK_ACTIONS = "1- Create Command Centre";
	private static final double SPEED = 0.25;
	private static final int TOTAL_BUILD_TIME = 15000;
	
	private boolean isBuilding = false;
	private int currentBuildTime = 0;

	public Truck(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setMoveSpeed(SPEED);
		super.setFILE_PATH(TRUCK_PATH);
		super.setImage(new Image(super.getFILE_PATH()));
		super.setSelected(false);
		super.setActions(TRUCK_ACTIONS);
	}

	@Override
	public void update(World world) throws SlickException {
		Input input = world.getInput();
		
		super.setSelected(this.equals(world.getSelected()));
		
		if (world.getSelectX() != -1 && super.hasBeenSelected(world, input)) {
			world.setSelected(this);
			super.setSelected(true);
		}
		
		if (isBuilding) {
			currentBuildTime = currentBuildTime + world.getDelta();
			if (currentBuildTime > TOTAL_BUILD_TIME) {
				world.generateSprite(new CommandCentre(this.getX(),this.getY(),this.getCamera()));
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
				
				//super.resetTarget();
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
		
		if (super.isSelected() && input.isKeyPressed(Input.KEY_1)) {
			isBuilding = true;
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
