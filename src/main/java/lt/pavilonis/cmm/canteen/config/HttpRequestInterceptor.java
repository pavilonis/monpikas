package lt.pavilonis.monpikas.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Base64;

import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class HttpRequestInterceptor implements ClientHttpRequestInterceptor {

   @Value(("${api.auth.username}"))
   private String apiUsername;

   @Value(("${api.auth.password}"))
   private String apiPassword;

   @Override
   public ClientHttpResponse intercept(HttpRequest request,
                                       byte[] body,
                                       ClientHttpRequestExecution execution) throws IOException {

      String creds = apiUsername + ":" + apiPassword;
      byte[] base64credsBytes = Base64.getEncoder().encode(creds.getBytes());

      HttpHeaders headers = request.getHeaders();
      headers.add("Authorization", "Basic " + new String(base64credsBytes));
      headers.setAccept(singletonList(APPLICATION_JSON));
      return execution.execute(request, body);
   }
}
