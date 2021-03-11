package br.com.south.system.irs.services;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.south.system.irs.domain.Account;

@Service
public class IRSService {
	
	private static final Logger LOG = LoggerFactory.getLogger(IRSService.class);
	
	private ReceitaService receitaService = new ReceitaService();

	public void updateAccount(Account account) {
		try {
			account.setApproved(receitaService.atualizarConta(account.getBranch(), account.getOnlyNumber(), account.getBalance(), account.getStatus()));
		} catch (Exception e) {
			String errorMessage = MessageFormat.format("# Não foi possível atualizar a conta >> {0}", account.getNumber());
			LOG.error(errorMessage, e);
			throw new RuntimeException(errorMessage);
		}
	}
	
}
