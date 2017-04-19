
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * Created by Lars on 22.02.2017.
 * Send for rabbitMQ
 */
public class Send {

    private final static String QUEUE_NAME = "Inbound";

    public static void main(String[] argv) throws Exception {
    	double x1=2.0;
		double x2=2.0;
		double x3=0.6290;
		double x4=1.0;
		double x5=1.2;
		double x6=2.0;
		double x7=1.89;
		double x8=2.0;
		double x9=1.0;
		double x10=2.32;
		double x11=2.8;
		double x12=1.01;
		double x13=2.0;
		double x14=2.0;
		double x15=1.54;
		double x16=2.0;
		double x17=3.8;
		
    	for(int i=0; i<201; i++){
	        ConnectionFactory factory = new ConnectionFactory();
	        factory.setHost("192.168.99.100");
	        Connection connection = factory.newConnection();
	        Channel channel = connection.createChannel();
	        
	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	        
	        x1++;
	        x2++;
	        x3++;
	        x4++;
	        x5++;
	        x6++;
	        x7++;
	        x8++;
	        x9++;
	        x10++;
	        x11++;
	        x12++;
	        x13++;
	        x14++;
	        x15++;
	        x16++;
	        x17++;
	        
	        String message = "{\n" +
	                "    \"solutionCandidateId\": \"c93461cc-797d-48db-b966-57fe1fdb443a\",\n" +
	                "    \"solutionVector\": [\n" +
	                "        "+ x1 +",\n" +
	                "        "+ x2 +",\n" +
	                "        "+ x3 +",\n" +
	                "        "+ x4 +",\n" +
	                "        "+ x5 +",\n" +
	                "        "+ x6 +",\n" +
	                "        "+ x7 +",\n" +
	                "        "+ x8 +",\n" +
	                "        "+ x9 +",\n" +
	                "        "+ x10 +",\n" +
	                "        "+ x11 +",\n" +
	                "        "+ x12 +",\n" +
	                "        "+ x13 +",\n" +
	                "        "+ x14 +",\n" +
	                "        "+ x15 +",\n" +
	                "        "+ x16 +",\n" +
	                "        "+ x17 +"\n" +
	                "    ],\n" +
	                "    \"resultValue\": 0.0,\n" +
	                "    \"isFeasible\": false,\n" +
	                "    \"isEvaluated\": false\n" +
	                "}";
	        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
	        System.out.println(" [x] Sent '" + message + "'");
	
	        channel.close();
	        connection.close();
    	}
    }
}

/*
* Geeigneten Zeitpunkt zum auswerten der Ergebnisse zu wählen.
* Speedup --> 1 Prozessor/Thread = 1, 2 Prozessoren/Threads = etwas mehr, aber nicht zwei. Starke abhängigkeit von Anzahl der CPUs(Hier kein Problem)
* Bei Multithreading besteht Ressourcenbeschränkung
 * &
 * Efficiency --> Mehrere Threads führen nicht zu einer gleichmäßigen Verbesserung der Effizienz
*
*
* */