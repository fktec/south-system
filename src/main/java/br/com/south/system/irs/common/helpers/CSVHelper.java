package br.com.south.system.irs.common.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CSVHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(CSVHelper.class);
	
	private CSVHelper() {}

	public static <T> List<T> extractListByCsvFile(Class<T> clazz, InputStream file, char separator) throws Exception {
		return extractListByCsvFile(clazz, file, separator, 0);
	}
	
	public static <T> List<T> extractListByCsvFile(Class<T> clazz, InputStream file, char separator, int skipLines) throws Exception {
		try (Reader reader = new BufferedReader(new InputStreamReader(file))) {
			CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
				.withType(clazz)
				.withSeparator(separator)
				.withIgnoreLeadingWhiteSpace(true)
				.withSkipLines(skipLines)
				.build();

			return csvToBean.parse();

		} catch (Exception e) {
			String errorMessage = "Não foi possível ler o arquivo CSV.";
			LOG.error(errorMessage, e);
			throw new Exception(errorMessage);
		}		
	}
	
	public static <T> void writeCsvFileByList(Class<T> clazz, Writer inputWriter, List<T> items, char separator) throws Exception {
		writeCsvFileByList(clazz, inputWriter, items, separator, null);
	}
	
	public static <T> void writeCsvFileByList(Class<T> clazz, Writer inputWriter, List<T> items, char separator, MappingStrategy<T> mappingStrategy) throws Exception {
		try { 
			StatefulBeanToCsv<T> writer = new StatefulBeanToCsvBuilder<T>(inputWriter)
					.withMappingStrategy(mappingStrategy != null ? mappingStrategy : null)
	                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	                .withSeparator(separator)
	                .withOrderedResults(true)
	                .build();
	        
			writer.write(items);
			
		} catch (Exception e) {
			String errorMessage = "Não foi possível realizar o download do arquivo CSV atualizado.";
			LOG.error(errorMessage, e);
			throw new Exception(errorMessage);
		}	
	}
	
	public static String generateFileId(File file) {
		//return MessageFormat.format("{0}-{1}",String.valueOf(new Date().getTime()),  file.getName());
		return MessageFormat.format("{0}", file.getName());
	}
	
	public static String csvValue(Object value) {
		return value != null ? value.toString() : null;
	}
}
