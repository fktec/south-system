package br.com.south.system.irs.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.south.system.irs.common.CustomMappingStrategy;
import br.com.south.system.irs.common.helpers.CSVHelper;
import br.com.south.system.irs.domain.Account;


@Component
public class ScheduleIRSAccountService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScheduleIRSAccountService.class);
	
	@Value("${file.csv.separator}")
	private char csvSeparator;
	
	@Value("${file.irs.account.path}")
	private String fileIrsAccountPath;
	
	@Value("${file.irs.account.prefix}")
	private String fileIrsAccountPrefix;
	
	@Value("${file.irs.account.input}")
	private String fileIrsAccountInput;
	
	@Value("${file.irs.account.output}")
	private String fileIrsAccountOutput;
	
	@Value("${file.irs.account.error.output}")
	private String fileIrsAccountErrorOutput;
	
	@Value("${file.irs.input.system}")
	private String fileIrsAccountInputSystem;
	
	@Value("${file.irs.output.system}")
	private String fileIrsAccountOutputSystem;
	
	@Value("${file.irs.error.output.system}")
	private String fileIrsAccountErrorOutputSystem;
	
	@Autowired
	private IRSService irsService;
	
	@Autowired
	private CustomMappingStrategy<Account> customMappingStrategy;
	
	@Scheduled(fixedRateString = "${schedule.irs.account.fixedRate}")
	public void scheduleAccounts() throws Exception 
	{
		List<Account> accountErrors = null;
		
		if (extractListFiles(extractFilesPath(fileIrsAccountInput, fileIrsAccountInputSystem, fileIrsAccountPath)) != null) {
			for (File file : extractListFiles(extractFilesPath(fileIrsAccountInput, fileIrsAccountInputSystem, fileIrsAccountPath))) {
				if (isAccountFile(file)) 
				{
					generateUpdatedAccounts(
							CSVHelper.generateFileId(file),
							file,
							CSVHelper.extractListByCsvFile(Account.class, new FileInputStream(file), csvSeparator, 1),
							accountErrors);
				}
			}
		}
	}

	private void generateUpdatedAccounts(String fileId, File file, List<Account> accounts, List<Account> accountErrors) throws Exception 
	{
		accountErrors = updateAccounts(fileId, accounts, accountErrors);
		
		if (accountErrors == null || (accountErrors != null && accountErrors.size() != accounts.size()))
			generateUpdatedAccountsInDirectory(fileId, accounts);
		
		if (accountErrors != null && !accountErrors.isEmpty())
			generateFileErrors(fileId, accountErrors);
		
		removeOriginalFile(file.getAbsolutePath());
	}

	private List<Account> updateAccounts(String fileId, List<Account> accounts, List<Account> accountErrors) {
		Account account = null;
		LOG.info("{} - Updating Accounts...", fileId);
		for (int i = 0; i < accounts.size(); i++) {
			account = accounts.get(i);
			try {
				irsService.updateAccount(account);
			} catch (Exception ex) { 
				if (accountErrors == null) accountErrors = new ArrayList<>();
				accountErrors.add(account);
				accounts.remove(i);
				i--;
			}
		}
		LOG.info("{} - Updating Complete.", fileId);
		return accountErrors;
	}
	
	private void generateUpdatedAccountsInDirectory(String fileId, List<Account> accounts) throws Exception {
		LOG.info("{} - Generating updated account...", fileId);
		try (Writer writer = new PrintWriter(generateFileName(extractFilesPath(fileIrsAccountOutput, fileIrsAccountOutputSystem, fileIrsAccountPath), fileId))) {
			CSVHelper.writeCsvFileByList(Account.class, writer, accounts, csvSeparator, customMappingStrategy);
		}
		LOG.info("{} - Updated account generation complete.", fileId);
	}

	private void generateFileErrors(String fileId, List<Account> accountErrors) throws Exception {
		LOG.info("{} - Generating errors...", fileId);
		try (Writer writerError = new PrintWriter(generateFileName(extractFilesPath(fileIrsAccountErrorOutput, fileIrsAccountErrorOutputSystem, fileIrsAccountPath), "ERROR-" + fileId))) {
			CSVHelper.writeCsvFileByList(Account.class, writerError, accountErrors, csvSeparator, customMappingStrategy);
		}
		LOG.info("{} - Error generation complete.", fileId);
	}

	private void removeOriginalFile(String absolutePath) throws IOException {
		Files.delete(Paths.get(absolutePath));
	}
	
	private boolean isAccountFile(File file) {
		return !file.isDirectory() && file.getName().startsWith(fileIrsAccountPrefix);
	}
	
	private File[] extractListFiles(String absolutePathName) {
		return new File(absolutePathName).listFiles();
	}
	
	private String extractFilesPath(String path, String defaultPath, String finalPath) {
		return path != null && !path.isEmpty()
				? path
				: MessageFormat.format("{0}/{1}/{2}", FileSystems.getDefault()
				        .getPath("")
				        .toAbsolutePath(),
				        defaultPath,
				        finalPath);
	}
	
	private String generateFileName(String filePath, String fileName) {
		return MessageFormat.format("{0}/{1}", filePath, fileName);
	}

}
