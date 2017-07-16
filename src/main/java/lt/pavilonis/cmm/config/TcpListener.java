package lt.pavilonis.cmm.config;

import lt.pavilonis.cmm.api.tcp.TcpEventStringProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArraySingleTerminatorSerializer;
import org.springframework.messaging.SubscribableChannel;

@EnableIntegration
@IntegrationComponentScan
@Configuration
public class TcpListener {

   @Value("${tcpListener.portNumber}")
   private int port;

   @Autowired
   private TcpEventStringProcessor tcpEventProcessor;

   @Bean
   public AbstractServerConnectionFactory serverCF() {
      TcpNetServerConnectionFactory connectionFactory = new TcpNetServerConnectionFactory(port);
      connectionFactory.setDeserializer(new ByteArraySingleTerminatorSerializer((byte) '\n'));
      return connectionFactory;
   }

   @Bean
   public TcpInboundGateway tcpInGate(AbstractServerConnectionFactory connectionFactory) {
      TcpInboundGateway inGate = new TcpInboundGateway();
      inGate.setConnectionFactory(connectionFactory);

      SubscribableChannel channel = new DirectChannel();
      channel.subscribe(tcpEventProcessor);
      inGate.setRequestChannel(channel);

      return inGate;
   }
}
