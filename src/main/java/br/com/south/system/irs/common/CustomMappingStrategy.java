package br.com.south.system.irs.common;

import org.springframework.stereotype.Component;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

@Component
public class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
	
	@Override
	public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
		return ((CsvModel) bean).generateHeaders();
	}
	
	@Override
	public String[] transmuteBean(T bean) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		return ((CsvModel) bean).generateValues();
	}
}
