//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;

public class ProblemSet {
	public static final double LOC_X1_LOW = 0;
	public static final double LOC_X1_HIGH = 1;
	public static final double LOC_X2_LOW = 0;
	public static final double LOC_X2_HIGH = 1;
	public static final double LOC_X3_LOW = 0;
	public static final double LOC_X3_HIGH = 1;
	public static final double LOC_X4_LOW = 0;
	public static final double LOC_X4_HIGH = 1;
	public static final double LOC_X5_LOW = 0;
	public static final double LOC_X5_HIGH = 1;
	public static final double LOC_X6_LOW = 0;
	public static final double LOC_X6_HIGH = 1;
	public static final double LOC_X7_LOW = 0;
	public static final double LOC_X7_HIGH = 1;
	public static final double LOC_X8_LOW = 0;
	public static final double LOC_X8_HIGH = 1;
	public static final double LOC_X9_LOW = 0;
	public static final double LOC_X9_HIGH = 1;
	public static final double LOC_X10_LOW = 0;
	public static final double LOC_X10_HIGH = 1;
	public static final double LOC_X11_LOW = 0;
	public static final double LOC_X11_HIGH = 1;
	public static final double LOC_X12_LOW = 0;
	public static final double LOC_X12_HIGH = 1;
	public static final double LOC_X13_LOW = 0;
	public static final double LOC_X13_HIGH = 1;
	public static final double LOC_X14_LOW = 0;
	public static final double LOC_X14_HIGH = 1;
	public static final double LOC_X15_LOW = 0;
	public static final double LOC_X15_HIGH = 1;
	public static final double LOC_X16_LOW = 0;
	public static final double LOC_X16_HIGH = 1;
	public static final double LOC_X17_LOW = 0;
	public static final double LOC_X17_HIGH = 1;
	public static final double VEL_LOW = -1;
	public static final double VEL_HIGH = 1;
	
	public static final double ERR_TOLERANCE = 1E-20; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	
	public static double evaluate(Location location) {
		/*double result = 0;
		double x = location.getLoc()[0]; // the "x" part of the location
		double y = location.getLoc()[1]; // the "y" part of the location
		
		result = Math.pow(2.8125 - x + x * Math.pow(y, 4), 2) + 
				Math.pow(2.25 - x + x * Math.pow(y, 2), 2) + 
				Math.pow(1.5 - x + x * y, 2);
		*/
		double result = 0;
		double x1 = location.getLoc()[0]; 
		double x2 = location.getLoc()[1];
		double x3 = location.getLoc()[2]; 
		double x4 = location.getLoc()[3];
		double x5 = location.getLoc()[4];
		double x6 = location.getLoc()[5];
		double x7 = location.getLoc()[6];
		double x8 = location.getLoc()[7];
		double x9 = location.getLoc()[8];
		double x10 = location.getLoc()[9];
		double x11 = location.getLoc()[10];
		double x12 = location.getLoc()[11];
		double x13 = location.getLoc()[12];
		double x14 = location.getLoc()[13];
		double x15 = location.getLoc()[14];
		double x16 = location.getLoc()[15];
		double x17 = location.getLoc()[16];
		
		/*result = Math.pow(x1,2)
				+ Math.pow(x2,2)
				+ Math.pow(x3,2)
				+ Math.pow(x4,2)
				+ Math.pow(x5,2)
				+ Math.pow(x6,2)
				+ Math.pow(x7,2)
				+ Math.pow(x8,2)
				+ Math.pow(x9,2)
				+ Math.pow(x10,2)
				+ Math.pow(x11,2)
				+ Math.pow(x12,2)
				+ Math.pow(x13,2)
				+ Math.pow(x14,2)
				+ Math.pow(x15,2)
				+ Math.pow(x16,2)
				+ Math.pow(x17,2);
		return result;*/
		
		/*ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.99.100");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "{\n" +
                "    \"solutionCandidateId\": \"c93461cc-797d-48db-b966-57fe1fdb443a\",\n" +
                "    \"solutionVector\": [\n" +
                x1 +
                x2 +
                x3 +
                x4 +
                x5 +
                x6 +
                x7 +
                x8 +
                x9 +
                x10 +
                x11 +
                x12 +
                x13 +
                x14 +
                x15 +
                x16 +
                x17 +
                "    ],\n" +
                "    \"resultValue\": 0.0,\n" +
                "    \"isFeasible\": false,\n" +
                "    \"isEvaluated\": false\n" +
                "}";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();*/
        
        //result = ReceiveLogs.Receive();
		return 0.0;
	}
}