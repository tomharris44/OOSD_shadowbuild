import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Unobtainium extends Resource {

	private static final String UNOBTAINIUM_FILE_PATH = "assets/resources/unobtainium_mine.png";
	private static final int INITIAL_AMOUNT = 50;

	public Unobtainium(double initialX, double initialY, Camera camera) throws SlickException {
		super(initialX, initialY, camera);
		super.setAmount(INITIAL_AMOUNT);
		super.setImage(new Image(UNOBTAINIUM_FILE_PATH));
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
