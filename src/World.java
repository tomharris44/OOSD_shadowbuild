import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class World {
	private static final String MAP_PATH = "assets/main.tmx";
	private static final String SOLID_PROPERTY = "solid";
	private static final String INITIAL_OBJECTS_PATH = "assets/objects.csv";
	
	// mapping of csv file sprite strings to static variables
	private static final String COMMAND_CENTRE = "command_centre";
	private static final String METAL_MINE = "metal_mine";
	private static final String UNOBTAINIUM_MINE = "unobtainium_mine";
	private static final String PYLON = "pylon";
	private static final String ENGINEER = "engineer";
	private static final String BUILDER = "builder";
	private static final String SCOUT = "scout";
	private static final String TRUCK = "truck";
	private static final String FACTORY = "factory";
	
	// Sprite ArrayLists for current spritelist and new/dead sprites
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private ArrayList<Sprite> newSprites = new ArrayList<Sprite>();
	private ArrayList<Sprite> deadSprites = new ArrayList<Sprite>();
	
	// 
	private TiledMap map;
	private Camera camera = new Camera();
	
	private Input lastInput;
	private int lastDelta;
	
	private Sprite selected = null;
	private double selectX = -1;
	private double selectY = -1;
	
	private int metal = 1000;
	private int unobtanium = 0;
	
	private int engineerCarryCap = 2;

	public Input getInput() {
		return lastInput;
	}
	public int getDelta() {
		return lastDelta;
	}
	
	public boolean isPositionFree(double x, double y) {
		int tileId = map.getTileId(worldXToTileX(x), worldYToTileY(y), 0);
		return !Boolean.parseBoolean(map.getTileProperty(tileId, SOLID_PROPERTY, "false"));
	}
	
	public double getMapWidth() {
		return map.getWidth() * map.getTileWidth();
	}
	
	public double getMapHeight() {
		return map.getHeight() * map.getTileHeight();
	}
	
	public void readSprites() throws IOException {
		BufferedReader init_objects_reader = new BufferedReader(new FileReader(INITIAL_OBJECTS_PATH));
		while (true) {
			try {
				String newLine = init_objects_reader.readLine();
				String[] newSpriteInfo = newLine.split(",");
				if(newSpriteInfo[0].equals(COMMAND_CENTRE)) {
					sprites.add(new CommandCentre(Integer.parseInt(newSpriteInfo[1]),
							Integer.parseInt(newSpriteInfo[2]),
							camera));
				}
				if(newSpriteInfo[0].equals(METAL_MINE)) {
					sprites.add(new Metal(Integer.parseInt(newSpriteInfo[1]),
							Integer.parseInt(newSpriteInfo[2]),
							camera));
				}
				if(newSpriteInfo[0].equals(UNOBTAINIUM_MINE)) {
					// change once other mine implemented
					sprites.add(new Unobtainium(Integer.parseInt(newSpriteInfo[1]),
							Integer.parseInt(newSpriteInfo[2]),
							camera));
				}
				if(newSpriteInfo[0].equals(PYLON)) {
					// change once other pylon implemented
					sprites.add(new Pylon(Integer.parseInt(newSpriteInfo[1]),
							Integer.parseInt(newSpriteInfo[2]),
							camera));
				}
				if(newSpriteInfo[0].equals(ENGINEER)) {
					// change once other engineer implemented
					sprites.add(new Engineer(Integer.parseInt(newSpriteInfo[1]),
							Integer.parseInt(newSpriteInfo[2]),
							camera));
				}
			} catch (Exception e) {
				init_objects_reader.close();
				break;
			}
		}
		
	}
	
	public void generateSprite(Sprite sprite) {
		newSprites.add(sprite);
	}
	
	public void killSprite(Sprite sprite) {
		deadSprites.add(sprite);
	}
	
	public World() throws SlickException, IOException {
		map = new TiledMap(MAP_PATH);
		this.readSprites();
		Collections.sort(sprites);
	}
	
	public void update(Input input, int delta) throws SlickException {
		lastInput = input;
		lastDelta = delta;
		
		//reverse list for updating - gives preference to units with selection
		Collections.reverse(sprites);
		
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			selectX = camera.screenXToGlobalX(input.getMouseX());
			selectY = camera.screenYToGlobalY(input.getMouseY());
		}
		
		camera.update(this);
		for (Sprite s: sprites) {
			s.update(this);
		}
		
		if (!deadSprites.isEmpty()) {
			for (Sprite s: deadSprites) {
				sprites.remove(s);
			}
			deadSprites.clear();
		}
		
		if (!newSprites.isEmpty()) {
			for (Sprite s: newSprites) {
				sprites.add(s);
			}
			newSprites.clear();
		}
		
		//sort list for rendering - renders units over the top of buildings and resources
		Collections.reverse(sprites);
		Collections.sort(sprites);
		
		if (selectX != -1) {
			selectX = -1;
			selectY = -1;
			selected = null;
			camera.setFreeRoam(true);
		}
	}
	
	public void render(Graphics g) {
		map.render((int)camera.globalXToScreenX(0),
				   (int)camera.globalYToScreenY(0));
		for (Sprite s: sprites) {
			s.render();
		}
		g.drawString("Metal: " + metal + "\nUnobtainium: " + unobtanium, 32, 32);
		if (selected != null) {
			g.drawString(selected.getActions(), 32, 100);
		}
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return (double)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	private int worldXToTileX(double x) {
		return (int)(x / map.getTileWidth());
	}
	private int worldYToTileY(double y) {
		return (int)(y / map.getTileHeight());
	}
	public Sprite getSelected() {
		return selected;
	}
	public void setSelected(Sprite selected) {
		this.selected = selected;
	}
	public double getSelectX() {
		return selectX;
	}
	public void setSelectX(double selectX) {
		this.selectX = selectX;
	}
	public double getSelectY() {
		return selectY;
	}
	public void setSelectY(double selectY) {
		this.selectY = selectY;
	}
	public ArrayList<Sprite> getSprites() {
		return sprites;
	}

	public int getEngineerCarryCap() {
		return engineerCarryCap;
	}
	public void setEngineerCarryCap(int engineerCarryCap) {
		this.engineerCarryCap = engineerCarryCap;
	}
	public int getMetal() {
		return metal;
	}
	public void setMetal(int metal) {
		this.metal = metal;
	}
	public int getUnobtanium() {
		return unobtanium;
	}
	public void setUnobtanium(int unobtanium) {
		this.unobtanium = unobtanium;
	}
}
