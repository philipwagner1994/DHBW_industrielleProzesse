import com.rabbitmq.client.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReceiveLogs {
	public static PrintWriter pw = null;
    private static final String EXCHANGE_NAME = "results";
    public static double max;
    public static int count=0;
    public static double[] var = new double[17];
    private final static String QUEUE_NAME = "Inbound";
    public static void main(String[] argv) throws Exception {
    	
    	var[0]=2.0;
    	var[1]=2.0;
    	var[2]=0.6290;
    	var[3]=1.0;
    	var[4]=1.2;
    	var[5]=2.0;
    	var[6]=1.89;
    	var[7]=2.0;
    	var[8]=1.0;
    	var[9]=2.32;
    	var[10]=1.8;
    	var[11]=1.01;
    	var[12]=2.0;
    	var[13]=2.0;
    	var[14]=1.54;
    	var[15]=2.0;
    	var[16]=3.8;
    	
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.99.100");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
            	//send
            	

            	FileWriter fw = new FileWriter("C:/Users/D062394/Documents/6.Semester/industrielle Prozesse/test.txt", true);
            	BufferedWriter bw = new BufferedWriter(fw);
            	pw = new PrintWriter(bw);
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                if ( message.contains( "\"isFeasible\":true" ) ){
                	//System.exit(1);
                	max=var[count];
                	var[count]--;
                }
                
                if ( message.contains( "\"isFeasible\":false" ) ){
                	pw.println("var"+count+": " +max);
                	System.out.println("var"+count+": " +max);
                	//pw.println("test");
                	//pw.close();
                	//System.exit(1);
                	var[count]=max;
                	count++;
                }
                pw.close();
                if(count<17){
            		ConnectionFactory factory2 = new ConnectionFactory();
                    factory2.setHost("192.168.99.100");
                    try{
                    	Connection connection2 = factory2.newConnection();
                    	Channel channel2 = connection2.createChannel();
                    	channel2.queueDeclare(QUEUE_NAME, false, false, false, null);
                        
                        String message2 = "{\n" +
                                "    \"solutionCandidateId\": \"c93461cc-797d-48db-b966-57fe1fdb443a\",\n" +
                                "    \"solutionVector\": [\n" +
                                "        "+ var[0] +",\n" +
                                "        "+ var[1] +",\n" +
                                "        "+ var[2] +",\n" +
                                "        "+ var[3] +",\n" +
                                "        "+ var[4] +",\n" +
                                "        "+ var[5] +",\n" +
                                "        "+ var[6] +",\n" +
                                "        "+ var[7] +",\n" +
                                "        "+ var[8] +",\n" +
                                "        "+ var[9] +",\n" +
                                "        "+ var[10] +",\n" +
                                "        "+ var[11] +",\n" +
                                "        "+ var[12] +",\n" +
                                "        "+ var[13] +",\n" +
                                "        "+ var[14] +",\n" +
                                "        "+ var[15] +",\n" +
                                "        "+ var[16] +"\n" +
                                "    ],\n" +
                                "    \"resultValue\": 0.0,\n" +
                                "    \"isFeasible\": false,\n" +
                                "    \"isEvaluated\": false\n" +
                                "}";
                        
                        channel2.basicPublish("", QUEUE_NAME, null, message2.getBytes("UTF-8"));
                        System.out.println(" [x] Sent '" + message2 + "'");

                        channel2.close();
                        connection2.close();
                    }
                    catch ( Exception e ) {
                    	System.out.println("Error");
                    }
            	}else{
            		System.exit(0);
            	}
            }
        };
        channel.basicConsume(queueName, true, consumer);
        //send
        
        ConnectionFactory factory2 = new ConnectionFactory();
        factory2.setHost("192.168.99.100");
        Connection connection2 = factory2.newConnection();
        Channel channel2 = connection2.createChannel();
        
        channel2.queueDeclare(QUEUE_NAME, false, false, false, null);
        
        String message2 = "{\n" +
                "    \"solutionCandidateId\": \"c93461cc-797d-48db-b966-57fe1fdb443a\",\n" +
                "    \"solutionVector\": [\n" +
                "        "+ var[0] +",\n" +
                "        "+ var[1] +",\n" +
                "        "+ var[2] +",\n" +
                "        "+ var[3] +",\n" +
                "        "+ var[4] +",\n" +
                "        "+ var[5] +",\n" +
                "        "+ var[6] +",\n" +
                "        "+ var[7] +",\n" +
                "        "+ var[8] +",\n" +
                "        "+ var[9] +",\n" +
                "        "+ var[10] +",\n" +
                "        "+ var[11] +",\n" +
                "        "+ var[12] +",\n" +
                "        "+ var[13] +",\n" +
                "        "+ var[14] +",\n" +
                "        "+ var[15] +",\n" +
                "        "+ var[16] +"\n" +
                "    ],\n" +
                "    \"resultValue\": 0.0,\n" +
                "    \"isFeasible\": false,\n" +
                "    \"isEvaluated\": false\n" +
                "}";
        
        channel2.basicPublish("", QUEUE_NAME, null, message2.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message2 + "'");

        channel2.close();
        connection2.close();
    }
}
