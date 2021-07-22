# south-system
- Java - Spring Boot
- Desafios - South System (Receita)

### Default
- **{PREFIXO}:** Só serão processados arquivos com o PREFIXO = csv-request (default)
- **{EPARATOR}:** ";"
- **{SCHEDULE}:** 5 segundos
- **{INPUT FILE}:** "{jar-path}/IRS/REQUEST/account"
- **{OUTPUT FILE}:** "{jar-path}/IRS/RESPONSE/account"
- **{ERROR FILE}:** "{jar-path}/IRS/ERROR/account"

### Algumas melhorias possíveis
- Envio de e-mail para notificação de arquivos que deram problemas, ou apenas observar a pasta de erros.
- Processo em paralelo para cada registro do arquivos
