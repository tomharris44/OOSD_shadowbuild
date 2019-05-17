import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Unit extends Sprite {
	
	private static final String HIGHLIGHT_PATH = "assets/highlight.png";
	private double moveSpeed;
	private Image highlightImage;
	
	private double targetX;
	private double targetY;
	
	private boolean isSelected;

	public Unit(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		this.targetX = initialX;
		this.targetY = initialY;
		this.highlightImage = new Image(HIGHLIGHT_PATH);
	}

	public double getTargetX() {
		return targetX;
	}

	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}
	
	public void resetTarget() {
		targetX = super.getX();
		targetY = super.getY();		
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Image getHighlightImage() {
		return highlightImage;
	}

	public double getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

}
