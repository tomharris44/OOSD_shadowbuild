import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Builder extends Unit {
	
	private static final String BUILDER_PATH = "assets/units/builder.png";
	private static final String BUILDER_ACTIONS = "1- Create Factory";
	private static final int FACTORY_COST = 100;
	private static final double SPEED = 0.1;
	private static final int TOTAL_BUILD_TIME = 10000;
	
	private int currentBuildTime = 0;
	private boolean isBuilding = false;

	public Builder(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setMoveSpeed(SPEED);
		super.setFILE_PATH(BUILDER_PATH);
		super.setImage(new Image(super.getFILE_PATH()));
		super.setSelected(false);
		super.setActions(BUILDER_ACTIONS);
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
				world.generateSprite(new Factory(this.getX(),this.getY(),this.getCamera()));
				isBuilding = false;
				currentBuildTime = 0;
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
			
			if (super.isSelected() && input.isKeyPressed(Input.KEY_1) && world.getMetal() >= FACTORY_COST) {
				isBuilding = true;
				world.setMetal(world.getMetal() - FACTORY_COST);
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

