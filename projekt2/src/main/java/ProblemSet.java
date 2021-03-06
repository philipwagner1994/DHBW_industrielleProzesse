import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProblemSet {
	public static final double LOC_X1_LOW = 0;
	public static final double LOC_X1_HIGH = 3;
	public static final double LOC_X2_LOW = 0;
	public static final double LOC_X2_HIGH = 3;
	public static final double LOC_X3_LOW = 0;
	public static final double LOC_X3_HIGH = 3;
	public static final double LOC_X4_LOW = 0;
	public static final double LOC_X4_HIGH = 3;
	public static final double LOC_X5_LOW = 0;
	public static final double LOC_X5_HIGH = 3;
	public static final double LOC_X6_LOW = 0;
	public static final double LOC_X6_HIGH = 3;
	public static final double LOC_X7_LOW = 0;
	public static final double LOC_X7_HIGH = 3;
	public static final double LOC_X8_LOW = 0;
	public static final double LOC_X8_HIGH = 3;
	public static final double LOC_X9_LOW = 0;
	public static final double LOC_X9_HIGH = 3;
	public static final double LOC_X10_LOW = 0;
	public static final double LOC_X10_HIGH = 3;
	public static final double LOC_X11_LOW = 0;
	public static final double LOC_X11_HIGH = 3;
	public static final double LOC_X12_LOW = 0;
	public static final double LOC_X12_HIGH = 3;
	public static final double LOC_X13_LOW = 0;
	public static final double LOC_X13_HIGH = 3;
	public static final double LOC_X14_LOW = 0;
	public static final double LOC_X14_HIGH = 3;
	public static final double LOC_X15_LOW = 0;
	public static final double LOC_X15_HIGH = 3;
	public static final double LOC_X16_LOW = 0;
	public static final double LOC_X16_HIGH = 3;
	public static final double LOC_X17_LOW = 0;
	public static final double LOC_X17_HIGH = 3;
	public static final double VEL_LOW = -0.1;
	public static final double VEL_HIGH = 0.1;
	public static final double ERR_TOLERANCE = 0.000000000000001;// 1E-20; // the smaller the tolerance, the more accurate the result,
	// but the number of iteration is increased
	private final static String QUEUE_NAME = "Inbound";

	public static double evaluate(Location location, String id) throws Exception {

		double result = 0;
		String back;
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

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.99.100");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "{\n" +
				"    \"solutionCandidateId\": \"" + id + "\",\n" +
				"    \"solutionVector\": [\n" +
				x1 + ",\n" +
				x2 + ",\n" +
				x3 + ",\n" +
				x4 + ",\n" +
				x5 + ",\n" +
				x6 + ",\n" +
				x7 + ",\n" +
				x8 + ",\n" +
				x9 + ",\n" +
				x10 + ",\n" +
				x11 + ",\n" +
				x12 + ",\n" +
				x13 + ",\n" +
				x14 + ",\n" +
				x15 + ",\n" +
				x16 + ",\n" +
				x17 + "\n" +
				"    ],\n" +
				"    \"resultValue\": 0.0,\n" +
				"    \"isFeasible\": false,\n" +
				"    \"isEvaluated\": false\n" +
				"}";

		channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
		// System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();

		return 0.0;
	}
}