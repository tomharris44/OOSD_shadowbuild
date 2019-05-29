
public abstract class Resource extends Sprite {
	
	private int amount;
	private int resourcesUndelivered = 0;


	public Resource(double initialX, double initialY, Camera camera) {
		super(initialX, initialY, camera);
		
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		if (amount < 0) {
			this.amount = 0;
		} else {
			this.amount = amount;
		}
	}

	public int getResourcesUndelivered() {
		return resourcesUndelivered;
	}

	public void setResourcesUndelivered(int resourcesUndelivered) {
		this.resourcesUndelivered = resourcesUndelivered;
	}

}
