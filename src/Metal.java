import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Metal extends Resource {
	
	private static final String METAL_FILE_PATH = "assets/resources/metal_mine.png";
	private static final int INITIAL_AMOUNT = 500;

	public Metal(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setAmount(INITIAL_AMOUNT);
		super.setImage(new Image(METAL_FILE_PATH));
	}

	@Override
	public void update(World world) {
		if (super.getAmount() == 0) {
			world.killSprite(this);
		}
	}

	@Override
	public void render() {
		super.getImage().drawCentered((int)super.getCamera().globalXToScreenX(super.getX()),
				   (int)super.getCamera().globalYToScreenY(super.getY()));
	}

}
