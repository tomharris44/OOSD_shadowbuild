import org.newdawn.slick.Input;

/**
 * This class should be used to restrict the game's view to a subset of the entire world.
 * 
 * You are free to make ANY modifications you see fit.
 * These classes are provided simply as a starting point. You are not strictly required to use them.
 */
public class Camera {
	private static final double CAMERA_SPEED = 0.4;
	
	private double x = 300;
	private double y = 300;
	private Sprite target;
	private boolean freeRoam = true;
	
	public void followSprite(Sprite target) {
		this.target = target;
	}
	
	public double globalXToScreenX(double x) {
		return x - this.x;
	}
	public double globalYToScreenY(double y) {
		return y - this.y;
	}

	public double screenXToGlobalX(double x) {
		return x + this.x;
	}
	public double screenYToGlobalY(double y) {
		return y + this.y;
	}
	
	public void update(World world) {
		Input input = world.getInput();
		
		double targetX = x;
		double targetY = y;
		
		
		
		if (input.isKeyDown(Input.KEY_W)) {
			targetY = y - world.getDelta() * CAMERA_SPEED;
			freeRoam = true;
		} else if (input.isKeyDown(Input.KEY_S)) {
			targetY = y + world.getDelta() * CAMERA_SPEED;
			freeRoam = true;
		}
		
		if (input.isKeyDown(Input.KEY_A)) {
			targetX = x - world.getDelta() * CAMERA_SPEED;
			freeRoam = true;
		} else if (input.isKeyDown(Input.KEY_D)) {
			targetX = x + world.getDelta() * CAMERA_SPEED;
			freeRoam = true;
		}
		
		
		if (!freeRoam) {
			this.followSprite(world.getSelected());
			targetX = target.getX() - App.WINDOW_WIDTH / 2;
			targetY = target.getY() - App.WINDOW_HEIGHT / 2;
			
			
		}
		
		x = Math.min(targetX, world.getMapWidth() -	 App.WINDOW_WIDTH);
		x = Math.max(x, 0);
		y = Math.min(targetY, world.getMapHeight() - App.WINDOW_HEIGHT);
		y = Math.max(y, 0);
		
		
	}

	public boolean isFreeRoam() {
		return freeRoam;
	}

	public void setFreeRoam(boolean freeRoam) {
		this.freeRoam = freeRoam;
	}
}
