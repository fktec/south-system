package br.com.south.system.irs.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 
 * @author frank.oliveira
 * - Abstraction from using RestTemplate

 */
public class RestUtil {
	
	private TestRestTemplate restTemplate;
	private String uri;
	private HttpMethod httpMethod;
	private Object body;
	private Map<String, String> pathParams;
	private MultiValueMap<String, String> queryParams; 
	private MultiValueMap<String, String> headers;
	
	private RestUtil (TestRestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public static RestUtil of(TestRestTemplate restTemplate) {
		return new RestUtil(restTemplate);
	}
	
	public RestUtil uri(String uri) {
		this.uri = uri;
		return this;
	}
	
	public RestUtil httpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
		return this;
	}

	public RestUtil body(Object body) {
		this.body = body;
		return this;
	}

	public RestUtil pathParam(String key, String value) {
		if (this.pathParams == null) this.pathParams = new HashMap<>();
		this.pathParams.put(key, value);
		return this;
	}
	
	public RestUtil queryParams(String key, String value) {
		if (this.queryParams == null) this.queryParams = new LinkedMultiValueMap<String, String>();
		this.queryParams.add(key, value);
		return this;
	}
	
	public RestUtil addHeader(String key, Object value) {
		if (this.headers == null) this.headers = new LinkedMultiValueMap<String, String>();
		this.headers.add(key, value != null ? value.toString() : null);
		return this;
	}
	
	public ResponseEntity<Object> getReturn() {
		return getReturn(Object.class);
	}
	
	@SuppressWarnings("deprecation")
	public <T> ResponseEntity<T> getReturn(Class<T> returnType) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri);

		HttpHeaders headers = null;
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		HttpEntity<Object> httpEntity = null;
		
		if (!ObjectUtils.isEmpty(body))
			httpEntity = new HttpEntity<>(body, headers);
		
		if (!ObjectUtils.isEmpty(headers))
			httpEntity = new HttpEntity<>(body, this.headers);
		
		if (!ObjectUtils.isEmpty(queryParams))
			uriBuilder.queryParams(queryParams);
		
		if (!ObjectUtils.isEmpty(pathParams))
			return restTemplate.exchange(uriBuilder.buildAndExpand(pathParams).toUri(), httpMethod, httpEntity, returnType);
		
		return restTemplate.exchange(uriBuilder.toUriString(), httpMethod, httpEntity, returnType);
	}
}
