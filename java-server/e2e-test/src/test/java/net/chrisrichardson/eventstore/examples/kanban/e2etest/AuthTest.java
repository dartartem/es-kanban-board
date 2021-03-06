package net.chrisrichardson.eventstore.examples.kanban.e2etest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.model.AuthResponse;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.webapi.AuthRequest;
import net.chrisrichardson.eventstore.examples.kanban.testutil.BaseTest;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {E2ETestConfiguration.class})
public class AuthTest extends BaseTest {

  @Autowired
  RestTemplate restTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  @Test
  public void shouldFailWithoutToken() throws IOException {
    try {
      Request.Get(baseUrl("/")).execute();
    } catch (HttpClientErrorException e) {
      Assert.assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
    }
  }

  @Test
  public void shouldSuccessWithToken() throws IOException {
    AuthRequest authRequest = new AuthRequest();
    authRequest.setEmail("test@test.com");
    HttpResponse authResp = Request.Post(baseUrl("api/authenticate"))
            .bodyString(mapper.writeValueAsString(authRequest), ContentType.APPLICATION_JSON)
            .execute().returnResponse();

    Assert.assertEquals(HttpStatus.OK.value(), authResp.getStatusLine().getStatusCode());
    String content = EntityUtils.toString(authResp.getEntity());
    Assert.assertNotNull(content);
    AuthResponse authResponse = mapper.readValue(content, AuthResponse.class);
    Assert.assertFalse(authResponse.getToken().isEmpty());
  }
}
