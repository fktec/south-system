# south-system
Desafios - South System (Receita)

### Default
- [INPUT FILE]: "{jar-path}/IRS/REQUEST/account"
- [OUTPUT FILE]: "{jar-path}/IRS/RESPONSE/account"
- [ERROR FILE]: "{jar-path}/IRS/ERROR/account"
- [PREFIXO]: Só serão processados arquivos com o PREFIXO = csv-request (default)
- [SEPARATOR]: ";"
- [SCHEDULE]: 5 segundos

### Algumas melhorias possíveii
- Envio de e-mail para notificação de arquivos que deram problemas, ou apenas observar a pasta de erros.
- Processo em paralelo para cada registro do arquivos
