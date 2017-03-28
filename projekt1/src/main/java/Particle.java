public class Particle {
	private double fitnessValue;
	private Velocity velocity;
	private Location location;
	private int id;
	
	public Particle() {
		super();
	}

	public Particle(double fitnessValue, Velocity velocity, Location location) {
		super();
		this.fitnessValue = fitnessValue;
		this.velocity = velocity;
		this.location = location;
	}

	public Velocity getVelocity() {
		return velocity;
	}

	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getFitnessValue() throws Exception {
	    String id2 = Integer.toString(id);
		fitnessValue = ProblemSet.evaluate(location, id2);
		return fitnessValue;
	}

	public void setFitnessValue(double fitnessValue){
	    this.fitnessValue = fitnessValue;
	}

	public void setId(int id){
	    this.id = id;
    }

    public int getId(){
	    return id;
    }
}