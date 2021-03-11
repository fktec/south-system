package br.com.south.system.irs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.MessageFormat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import br.com.south.system.irs.util.CsvUtil;
import br.com.south.system.irs.util.RestUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SouthSystemIrsApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource({ "classpath:application.properties" })
public class SouthSystemIrsApplicationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Value("${app.protocol}")
	private String appProtocol;
	
	@Value("${app.host}")
	private String appHost;
	
	@Value("${app.port}")
	private String appPort;
	
	@Value("${rest.irs.account.update.path}")
	private String irsAccountRestPath;
	
	@Test
	public void tA1_csvUpdate_Success() {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", CsvUtil.csvRequestSuccess);
		
		ResponseEntity<String> response = RestUtil.of(restTemplate)
				.uri(generateUri(irsAccountRestPath))
				.addHeader("Content-type", MediaType.MULTIPART_FORM_DATA)
				.body(body)
				.httpMethod(HttpMethod.PUT)
				.getReturn(String.class);
		
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void tA2_csvUpdate_Error() {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", CsvUtil.csvRequestError);
		
		ResponseEntity<String> response = RestUtil.of(restTemplate)
				.uri(generateUri(irsAccountRestPath))
				.addHeader("Content-type", MediaType.MULTIPART_FORM_DATA)
				.body(body)
				.httpMethod(HttpMethod.PUT)
				.getReturn(String.class);
		
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	private String generateUri(String path) {
		return MessageFormat.format("{0}://{1}:{2}/{3}", appProtocol, appHost, appPort, path);
	}
}
