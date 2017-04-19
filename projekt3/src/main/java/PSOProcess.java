import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class PSOProcess implements PSOConstants {
	private static final String EXCHANGE_NAME = "results";
	private Vector<Particle> swarm = new Vector<Particle>();
	private double[] pBest = new double[SWARM_SIZE];
	private Vector<Location> pBestLocation = new Vector<Location>();
	private double gBest;
	private Location gBestLocation;
	private double[] fitnessValueList = new double[SWARM_SIZE];
	private String message;
	private int particleUpdated = 0;
	private double err = 9999;
	private double lastval = 999999;
	private int t;
	private double w;
	
	Random generator = new Random();
	
	public void execute() throws Exception {
		

		/*ADDED PART*/
		//int t = 0;
		//double w;
		//double err = 9999;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.99.100");//192.168.99.100
		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		initializeSwarm();
		updateFitnessList();
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
									   AMQP.BasicProperties properties, byte[] body) throws IOException {
				message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				String messageID = message.split(":")[1].split(",")[0].split("\"")[1];
				double messageValue =Double.parseDouble(message.split(":")[3].split(",")[0]);
				String isFeasible = message.split(":")[4].split(",")[0];
				if(isFeasible.equals("false")){ 
					messageValue=1000000;}
				if(messageID.equals("global")){
					lastval = err;
                    err = messageValue - 0; // minimizing the functions means it's getting closer to 0
                    t++;
                    try {
                        updateFitnessList();
                    }catch(Exception e){

                    }
                }else{
				    int id = Integer.parseInt(messageID );
                    for(int i=0; i<SWARM_SIZE; i++) {
                       int particleId = swarm.get(i).getId();
                       if(id == particleId){
                           swarm.get(i).setFitnessValue(messageValue);
                           fitnessValueList[i] = messageValue;
                           particleUpdated++;
                       }
                    }

                }
				//System.out.println(err);
				//System.out.println(t);
				//System.out.println(ProblemSet.ERR_TOLERANCE);
                if(t >= MAX_ITERATION || Math.abs(err-lastval) <= ProblemSet.ERR_TOLERANCE && err<1000000){ //|| Math.abs(err-lastval) <= ProblemSet.ERR_TOLERANCE
				    try {
                        System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
                        for (int j = 0; j < PROBLEM_DIMENSION; j++) {
                            System.out.println("     Best X" + j + ": " + gBestLocation.getLoc()[j]);
                        }
                        System.out.println("     Value: " + err);//ProblemSet.evaluate(gBestLocation));
                        //System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
                        channel.close();
                        connection.close();
                    }catch(Exception e){
				        System.out.println(e);
                    }

                }
                else if(particleUpdated==swarm.size()){
                    particleUpdated = 0;
                    // step 1 - update pBest
                    for(int i=0; i<SWARM_SIZE; i++) {
                        if(fitnessValueList[i] < pBest[i]) {
                            pBest[i] = fitnessValueList[i];
                            pBestLocation.set(i, swarm.get(i).getLocation());
                        }
                    }

                    // step 2 - update gBest
                    int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
                    if(t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
                        gBest = fitnessValueList[bestParticleIndex];
                        gBestLocation = swarm.get(bestParticleIndex).getLocation();
                    }

                    w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);

                    for(int i=0; i<SWARM_SIZE; i++) {
                        double r1 = generator.nextDouble();
                        double r2 = generator.nextDouble();

                        Particle p = swarm.get(i);

                        // step 3 - update velocity
                        double[] newVel = new double[PROBLEM_DIMENSION];
                        for(int j=0;j<PROBLEM_DIMENSION;j++){
                            newVel[j] = (w * p.getVelocity().getPos()[j]) +
                                    (r1 * C1) * (pBestLocation.get(i).getLoc()[j] - p.getLocation().getLoc()[j]) +
                                    (r2 * C2) * (gBestLocation.getLoc()[j] - p.getLocation().getLoc()[j]);
                        }
				/*newVel[1] = (w * p.getVelocity().getPos()[1]) +
							(r1 * C1) * (pBestLocation.get(i).getLoc()[1] - p.getLocation().getLoc()[1]) +
							(r2 * C2) * (gBestLocation.getLoc()[1] - p.getLocation().getLoc()[1]);*/
                        Velocity vel = new Velocity(newVel);
                        p.setVelocity(vel);

                        // step 4 - update location
                        double[] newLoc = new double[PROBLEM_DIMENSION];
                        for(int j=0;j<PROBLEM_DIMENSION;j++){
                            newLoc[j] = p.getLocation().getLoc()[j] + newVel[j];
                        }
                        //newLoc[1] = p.getLocation().getLoc()[1] + newVel[1];
                        Location loc = new Location(newLoc);
                        p.setLocation(loc);
                    }
                    try {
                        ProblemSet.evaluate(gBestLocation,"global"); // minimizing the functions means it's getting closer to 0
                    }catch(Exception e){
                        System.out.println(e);
                    }

			/*System.out.println("ITERATION " + t + ": ");
			for(int j=0;j<PROBLEM_DIMENSION;j++){
			System.out.println("     Best X"+j+": " + gBestLocation.getLoc()[j]);
			}
			//System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
			System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));*/

                //    t++;
                 //   updateFitnessList();
                }
			}
			
		};
		channel.basicConsume(queueName, true, consumer);
		/*ADDED PART END*/


		for(int i=0; i<SWARM_SIZE; i++) {
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}
		
		//int t = 0;
		//double w;
		//double err = 9999;
		
		/*while(t < MAX_ITERATION && err > ProblemSet.ERR_TOLERANCE) {
			// step 1 - update pBest
			for(int i=0; i<SWARM_SIZE; i++) {
				if(fitnessValueList[i] < pBest[i]) {
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());
				}
			}
				
			// step 2 - update gBest
			int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
			if(t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
				gBest = fitnessValueList[bestParticleIndex];
				gBestLocation = swarm.get(bestParticleIndex).getLocation();
			}
			
			w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);
			
			for(int i=0; i<SWARM_SIZE; i++) {
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();
				
				Particle p = swarm.get(i);
				
				// step 3 - update velocity
				double[] newVel = new double[PROBLEM_DIMENSION];
				for(int j=0;j<PROBLEM_DIMENSION;j++){
				newVel[j] = (w * p.getVelocity().getPos()[j]) + 
							(r1 * C1) * (pBestLocation.get(i).getLoc()[j] - p.getLocation().getLoc()[j]) +
							(r2 * C2) * (gBestLocation.getLoc()[j] - p.getLocation().getLoc()[j]);
				}
				/*newVel[1] = (w * p.getVelocity().getPos()[1]) + 
							(r1 * C1) * (pBestLocation.get(i).getLoc()[1] - p.getLocation().getLoc()[1]) +
							(r2 * C2) * (gBestLocation.getLoc()[1] - p.getLocation().getLoc()[1]);*/
				/*Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);
				
				// step 4 - update location
				double[] newLoc = new double[PROBLEM_DIMENSION];
				for(int j=0;j<PROBLEM_DIMENSION;j++){
				newLoc[j] = p.getLocation().getLoc()[j] + newVel[j];
				}
				//newLoc[1] = p.getLocation().getLoc()[1] + newVel[1];
				Location loc = new Location(newLoc);
				p.setLocation(loc);
			}
			
			err = ProblemSet.evaluate(gBestLocation) - 0; // minimizing the functions means it's getting closer to 0
			
			
			/*System.out.println("ITERATION " + t + ": ");
			for(int j=0;j<PROBLEM_DIMENSION;j++){
			System.out.println("     Best X"+j+": " + gBestLocation.getLoc()[j]);
			}
			//System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
			System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));*/
			
			/*t++;
			updateFitnessList();
		}
		
		System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
		for(int j=0;j<PROBLEM_DIMENSION;j++){
		System.out.println("     Best X"+j+": " + gBestLocation.getLoc()[j]);
		}
		System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));
		//System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);*/
	}
	
	public void initializeSwarm() {
		Particle p;
		for(int i=0; i<SWARM_SIZE; i++) {
			p = new Particle();
			
			// randomize location inside a space defined in Problem Set
			double[] loc = new double[PROBLEM_DIMENSION];
			loc[0] = ProblemSet.LOC_X1_LOW + generator.nextDouble() * (ProblemSet.LOC_X1_HIGH - ProblemSet.LOC_X1_LOW);
			loc[1] = ProblemSet.LOC_X2_LOW + generator.nextDouble() * (ProblemSet.LOC_X2_HIGH - ProblemSet.LOC_X2_LOW);
			loc[2] = ProblemSet.LOC_X3_LOW + generator.nextDouble() * (ProblemSet.LOC_X3_HIGH - ProblemSet.LOC_X3_LOW);
			loc[3] = ProblemSet.LOC_X4_LOW + generator.nextDouble() * (ProblemSet.LOC_X4_HIGH - ProblemSet.LOC_X4_LOW);
			loc[4] = ProblemSet.LOC_X5_LOW + generator.nextDouble() * (ProblemSet.LOC_X5_HIGH - ProblemSet.LOC_X5_LOW);
			loc[5] = ProblemSet.LOC_X6_LOW + generator.nextDouble() * (ProblemSet.LOC_X6_HIGH - ProblemSet.LOC_X6_LOW);
			loc[6] = ProblemSet.LOC_X7_LOW + generator.nextDouble() * (ProblemSet.LOC_X7_HIGH - ProblemSet.LOC_X7_LOW);
			loc[7] = ProblemSet.LOC_X8_LOW + generator.nextDouble() * (ProblemSet.LOC_X8_HIGH - ProblemSet.LOC_X8_LOW);
			loc[8] = ProblemSet.LOC_X9_LOW + generator.nextDouble() * (ProblemSet.LOC_X9_HIGH - ProblemSet.LOC_X9_LOW);
			loc[9] = ProblemSet.LOC_X10_LOW + generator.nextDouble() * (ProblemSet.LOC_X10_HIGH - ProblemSet.LOC_X10_LOW);
			loc[10] = ProblemSet.LOC_X11_LOW + generator.nextDouble() * (ProblemSet.LOC_X11_HIGH - ProblemSet.LOC_X11_LOW);
			loc[11] = ProblemSet.LOC_X12_LOW + generator.nextDouble() * (ProblemSet.LOC_X12_HIGH - ProblemSet.LOC_X12_LOW);
			loc[12] = ProblemSet.LOC_X13_LOW + generator.nextDouble() * (ProblemSet.LOC_X13_HIGH - ProblemSet.LOC_X13_LOW);
			loc[13] = ProblemSet.LOC_X14_LOW + generator.nextDouble() * (ProblemSet.LOC_X14_HIGH - ProblemSet.LOC_X14_LOW);
			loc[14] = ProblemSet.LOC_X15_LOW + generator.nextDouble() * (ProblemSet.LOC_X15_HIGH - ProblemSet.LOC_X15_LOW);
			loc[15] = ProblemSet.LOC_X16_LOW + generator.nextDouble() * (ProblemSet.LOC_X16_HIGH - ProblemSet.LOC_X16_LOW);
			loc[16] = ProblemSet.LOC_X17_LOW + generator.nextDouble() * (ProblemSet.LOC_X17_HIGH - ProblemSet.LOC_X17_LOW);
			Location location = new Location(loc);
			
			// randomize velocity in the range defined in Problem Set
			double[] vel = new double[PROBLEM_DIMENSION];
			vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[2] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[3] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[4] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[5] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[6] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[7] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[8] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[9] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[10] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[11] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[12] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[13] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[14] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[15] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[16] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			
			
			Velocity velocity = new Velocity(vel);
			
			p.setLocation(location);
			p.setVelocity(velocity);
			p.setId(i);
			swarm.add(p);
		}
	}
	
	public void updateFitnessList() throws Exception {
		for(int i=0; i<SWARM_SIZE; i++) {
			//fitnessValueList[i] =
            swarm.get(i).getFitnessValue();
		}
	}
}