import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Engineer extends Unit {
	
	private static final String ENGINEER_PATH = "assets/units/engineer.png";
	private static final double SPEED = 0.1;
	private static final int TOTAL_MINING_TIME = 5000;
	
	private boolean isMining = false;
	private int currentMiningTime = 0;
	
	private Resource resource;
	private int currentAmount;

	public Engineer(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setMoveSpeed(SPEED);
		super.setFILE_PATH(ENGINEER_PATH);
		super.setImage(new Image(super.getFILE_PATH()));
		super.setSelected(false);
	}
	
	public boolean isCloseToMine(World world) {
		for (Sprite s: world.getSprites()) {
			if (s instanceof Resource && 
					World.distance(this.getX(), this.getY(), s.getX(), s.getY()) < 32) {
				resource = (Resource)s;
				return true;
			}
		}
		return false;
	}
	
	public boolean isCloseToCC(World world) {
		for (Sprite s: world.getSprites()) {
			if (s instanceof CommandCentre && 
					World.distance(this.getX(), this.getY(), s.getX(), s.getY()) < 32) {
				return true;
			}
		}
		return false;
	}
	
	public Sprite findClosestCC(World world) {
		double minDist = 3000;
		Sprite closest = null;
		for (Sprite s: world.getSprites()) {
			if (s instanceof CommandCentre && 
					World.distance(this.getX(), this.getY(), s.getX(), s.getY()) < minDist) {
				// could compute once and assign to local variable
				minDist = World.distance(this.getX(), this.getY(), s.getX(), s.getY());
				closest = s;
			}
		}
		return closest;
	}

	@Override
	public void update(World world) throws SlickException {
		Input input = world.getInput();
		
		super.setSelected(this.equals(world.getSelected()));
		
		if (world.getSelectX() != -1 && super.hasBeenSelected(world, input)) {
			world.setSelected(this);
			super.setSelected(true);
		}
		
		// If the mouse button is being clicked, set the target to the cursor location
		if (this.equals(world.getSelected()) && input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			super.setTargetX(super.getCamera().screenXToGlobalX(input.getMouseX()));
			super.setTargetY(super.getCamera().screenYToGlobalY(input.getMouseY()));
			// turn into method for reset mining?
			isMining = false;
			currentMiningTime = 0;
		}
		
		if (isMining) {
			currentMiningTime = currentMiningTime + world.getDelta();
			if (currentMiningTime > TOTAL_MINING_TIME) {
				
				if (resource.getAmount() < world.getEngineerCarryCap()) {
					currentAmount = resource.getAmount();
				} else {
					currentAmount = world.getEngineerCarryCap();
				}
				
				if (currentAmount > 0) {
					Sprite targetComCentre = findClosestCC(world);
					super.setTargetX(targetComCentre.getX());
					super.setTargetY(targetComCentre.getY());
					resource.setAmount(resource.getAmount() - currentAmount);
				}
				isMining = false;
				currentMiningTime = 0;
			}
		} else if(super.getTargetX() == super.getX() &&
				super.getTargetY() == super.getY() && 
				isCloseToMine(world)){
			isMining = true;
		}
		
		if (isCloseToCC(world) && currentAmount > 0) {
			if (resource instanceof Unobtainium) {
				world.setUnobtanium(world.getUnobtanium() + currentAmount);
			} else {
				world.setMetal(world.getMetal() + currentAmount);
			}
			
			super.setTargetX(resource.getX());
			super.setTargetY(resource.getY());
			currentAmount = 0;
			resource = null;
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
