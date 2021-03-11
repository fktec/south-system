package br.com.south.system.irs.resources;

import java.io.Writer;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.south.system.irs.common.helpers.CSVHelper;
import br.com.south.system.irs.domain.Account;
import br.com.south.system.irs.services.IRSService;

@RestController
@RequestMapping("${rest.irs.account.update.path}")
public class IRSAccountResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(IRSAccountResource.class);
	
	@Autowired
	private IRSService irsService;
	
	@Value("${file.csv.separator}")
	private char csvSeparator;

	@PutMapping
	public void updateAccount(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception  {
		List<Account> accounts = CSVHelper.extractListByCsvFile(Account.class, file, csvSeparator, 1);
		String requestId = CSVHelper.generateFileId(file);
		
		// TODO: MELHORIA > PERFORMANCE: Multithread
		LOG.info("{} - Updating Accounts...", requestId);
		for (Account account : accounts)
			irsService.updateAccount(account);			
		LOG.info("{} - Updating Complete.", requestId);
		
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, MessageFormat.format("attachment; filename=\"{0}\"", requestId));

        LOG.info("{} - Downloading updated account...", requestId);
        downloadFile(response.getWriter(), accounts);
        LOG.info("{} - Download Complete.", requestId);
	}

	public void downloadFile(Writer writer, List<Account> accounts) throws Exception {
		CSVHelper.writeCsvFileByList(Account.class, writer, accounts, csvSeparator);
	}
}
