
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
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.99.100");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "{\n" +
                "    \"solutionCandidateId\": \"c93461cc-797d-48db-b966-57fe1fdb443a\",\n" +
                "    \"solutionVector\": [\n" +
                "        42.0,\n" +
                "        2.0,\n" +
                "        8.0,\n" +
                "        1.335,\n" +
                "        345.0,\n" +
                "        3.5,\n" +
                "        3.80,\n" +
                "        34.5,\n" +
                "        5.0,\n" +
                "        561.0,\n" +
                "        4.0,\n" +
                "        3.0,\n" +
                "        7.0,\n" +
                "        2.0,\n" +
                "        7.0,\n" +
                "        4.0,\n" +
                "        2.0\n" +
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

/*
* Geeigneten Zeitpunkt zum auswerten der Ergebnisse zu wählen.
* Speedup --> 1 Prozessor/Thread = 1, 2 Prozessoren/Threads = etwas mehr, aber nicht zwei. Starke abhängigkeit von Anzahl der CPUs(Hier kein Problem)
* Bei Multithreading besteht Ressourcenbeschränkung
 * &
 * Efficiency --> Mehrere Threads führen nicht zu einer gleichmäßigen Verbesserung der Effizienz
*
*
* */