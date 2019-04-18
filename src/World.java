import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;

public class World {
	
	private TiledMap map;
	private double xDest;
	private double yDest;
	private int xPerson;
	private int yPerson;
	
	private int xImage;
	private int yImage;

	private Person person;
	private Image personImage;
	private Camera camera;
	
	private final double START_POS_X = 812;
	private final double START_POS_Y = 584;
	private final double speed = 0.25;
	
	private double direction;
	
	
	
	
	public World() throws SlickException {
		
		map = new TiledMap("assets/main.tmx","assets");
		person = new Person(START_POS_X,START_POS_Y);
		personImage = new Image("assets/scout.png");
		camera = new Camera();
		
		xDest = START_POS_X;
		yDest = START_POS_Y;
		
	}
	
	public boolean isClose(double dest, double pos) {
		
		if (dest - pos < 0.25 && dest - pos > -0.25) {
			
			return true;
			
		}
		else {
			return false;
		}
		
	}
	
	public boolean checkMove (double newX, double newY) {
		
		int xTile = (int)(person.getPersonX() + newX + 30) / 64;
		int yTile = (int)(person.getPersonY() + newY + 30) / 64;
		
		int tileID = map.getTileId(xTile,yTile,0);
		
		String tileState = map.getTileProperty(tileID, "solid", "false");
		
		if(tileState.equals("true")) {
			return true;
		}
		
		return false;
		
	}
	
	public void update(Input input, int delta) {
		
		
		if (isClose(xDest,person.getPersonX()) && isClose(yDest,person.getPersonY())) {
			
		}
		else {
			
			
			double moveDist = speed * delta;
			
			double moveX = moveDist * Math.cos(direction);
			double moveY = moveDist * Math.sin(direction);
			
			if (checkMove(moveX,moveY)) {
				xDest = person.getPersonX();
				yDest = person.getPersonY();
			}
			else {
			
				if (Math.abs(xDest - person.getPersonX()) < Math.abs(moveX)) {
					person.setPersonX(xDest);
				}
				else {
					person.setPersonX(person.getPersonX() + moveX);
				}
				
				
				if (Math.abs(yDest - person.getPersonY()) < Math.abs(moveY)) {
					person.setPersonY(yDest);
				}
				else {
					person.setPersonY(person.getPersonY() + moveY);
				}
			}
					
			
		}
		
		if (person.getPersonX() < 482) {
			
			xImage = (int)person.getPersonX();
			xPerson = 0;
		}
		else if (person.getPersonX() > 1378) {
			
			xImage = (int)person.getPersonX() - 896;
			xPerson = -896;
		}
		else {
			xImage = 482;
			xPerson = (int)- ((person.getPersonX() - 482) / 1);
		}
		
		if (person.getPersonY() < 354) {
			yImage = (int)person.getPersonY();
			yPerson = 0;
			
		}
		else if (person.getPersonY() > 1506) {
			yImage = (int)person.getPersonY() - 1152;
			yPerson = -1152;
			
		}
		else {
			yImage = 354;
			yPerson = (int)- ((person.getPersonY() - 354) / 1);	
		}
		
		if (input.isMousePressed(input.MOUSE_RIGHT_BUTTON) == true) {
			
			xDest = person.getPersonX() + input.getMouseX() - (xImage + 30);
			yDest = person.getPersonY() + input.getMouseY() - (yImage + 30);
			
			double xDiff = input.getMouseX() - (xImage + 30);
			double yDiff = input.getMouseY() - (yImage + 30);
			
			
			direction = Math.atan2(yDiff, xDiff);
		
		}
		
	}
	
	public void render(Graphics g) {
		map.render(xPerson, yPerson, 0, 0, 30, 30);
		g.drawImage(personImage,xImage,yImage);
	}
}
