package lt.pavilonis.cmm.api.tcp;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.ServerSocket;
import java.net.Socket;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TcpListener extends Thread {

   private static final Logger LOG = getLogger(TcpConnection.class.getSimpleName());

   @Value("${tcpListener.portNumber}")
   private int portNumber;

   @Autowired
   private TcpEventStringProcessor processor;

   @PostConstruct
   public void listen() {
      start();
   }

   @Override
   public void run() {

      try (ServerSocket listenSocket = new ServerSocket(portNumber)) {

         while (true) {
            try (Socket clientSocket = listenSocket.accept()) {

               new TcpConnection(clientSocket, processor)
                     .run();

            } catch (Exception e) {
               LOG.error("Error listening to socket port " + portNumber);
               e.printStackTrace();
            }
         }

      } catch (Exception e) {
         LOG.error("Error listening to socket port " + portNumber);
         e.printStackTrace();
      }
   }

}

