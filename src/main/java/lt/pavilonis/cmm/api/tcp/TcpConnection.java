package lt.pavilonis.cmm.api.tcp;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.slf4j.LoggerFactory.getLogger;

final class TcpConnection extends Thread {

   private static final Logger LOG = getLogger(TcpConnection.class.getSimpleName());
   private final Socket clientSocket;
   private final TcpEventStringProcessor processor;

   public TcpConnection(Socket clientSocket, TcpEventStringProcessor processor) throws IOException {
      this.clientSocket = clientSocket;
      this.processor = processor;
   }

   @Override
   public void run() {
      LOG.info("Starting");
      try (InputStreamReader inputStream = new InputStreamReader(clientSocket.getInputStream())) {

         BufferedReader bufferedInput = new BufferedReader(inputStream);

//         boolean read = true;

         while (true) {

            String input = bufferedInput.readLine();

            if (StringUtils.isNotBlank(input)) {
               processor.process(input);
            }
//            else if (input == null) {
//               LOG.warn("Got NULL input. Socket connection will be closed");
//               read = false;
//            }
         }
      } catch (IOException e) {
         LOG.error("TCP Error: " + e);
         e.printStackTrace();
      }
   }
}
