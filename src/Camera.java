
/**
 * This class should be used to restrict the game's view to a subset of the entire world.
 * 
 * You are free to make ANY modifications you see fit.
 * These classes are provided simply as a starting point. You are not strictly required to use them.
 */
public class Camera {
	
	private final double HALF_HORIZONTAL = 512;
	private final double HALF_VERTICAL = 512;


	public float getLeft(double xpos) {
		float left = (float)(xpos - HALF_HORIZONTAL);
		if (left < 0) {
			left = 0;
		}
		if (left > 896) {
			left=896;
		}
		return left;
	}
	public float getTop(double ypos) {
		float top = (float)(ypos - HALF_VERTICAL);
		if (top < 0) {
			top = 0;
		}
		if (top > 1152) {
			top=1152;
		}
		return top;
	}
	public float getRight(float xpos) {
		float right = (float)(xpos + HALF_HORIZONTAL);
		return right;
	}
	public float getBottom(float ypos) {
		float bottom = (float)(ypos + HALF_VERTICAL);
		return bottom;
	}
}
