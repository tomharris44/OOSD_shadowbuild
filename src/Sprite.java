import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public abstract class Sprite {
	
	private String FILE_PATH;
	
	private double x;
	private double y;
	
	private Image image;
	
	private Camera camera;
	
	private String actions = "";
	
	public Sprite (double initialX, double initialY, Camera camera) {
		setX(initialX);
		setY(initialY);
		this.camera = camera;
	}
	
	public abstract void update(World world) throws SlickException;
	public abstract void render();

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getFILE_PATH() {
		return FILE_PATH;
	}

	public void setFILE_PATH(String FILE_PATH) {
		this.FILE_PATH = FILE_PATH;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public boolean hasBeenSelected(World world, Input input) {
		if (World.distance(x, y, world.getSelectX(), world.getSelectY()) < 32) {
			world.setSelectX(-1);
			world.setSelectY(-1);
			camera.setFreeRoam(false);
			return true;
		}
		return false;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}
	
}
