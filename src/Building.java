import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Building extends Sprite {
	
	private static final String HIGHLIGHT_PATH = "assets/highlight_large.png";
	private Image highlightImage;
	
	private boolean isSelected;
	

	public Building(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		this.highlightImage = new Image(HIGHLIGHT_PATH);
	}

	public Image getHighlightImage() {
		return highlightImage;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}


}
