package br.com.south.system.irs.util;

import org.springframework.core.io.ClassPathResource;

public class CsvUtil {

	public static ClassPathResource csvRequestSuccess = new ClassPathResource("samples/csv/csv-request-success.csv");
	public static ClassPathResource csvRequestError = new ClassPathResource("samples/csv/csv-request-error.csv");
	public static ClassPathResource csvExpected = new ClassPathResource("samples/csv/csv-expected.csv");
}
