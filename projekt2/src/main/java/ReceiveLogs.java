import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs {
    private static final String EXCHANGE_NAME = "results";
    private static String message;

    public static void main(String[] argv) throws Exception {
    	ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.99.100");//192.168.99.100
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
                message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                
            }
        };
        channel.basicConsume(queueName, true, consumer);
        System.out.println("Test");
    }
    
    public static String Receive() throws IOException, TimeoutException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.99.100");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                message = new String(body, "UTF-8");
                //System.out.println(" [x] Received '" + message + "'");
                  }
        };
        channel.basicConsume(queueName, true, consumer);
        return message;
    }
}
