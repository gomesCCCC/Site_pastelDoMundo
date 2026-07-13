# Site_pastelDoMundo

Esse site está sendo desenvolvido para a função de delivery com a proposta der um site fléxivel, com uma interface mais agrádavel tanto para o cliente para com o dono do qual será o controlador, o sistema do site foi montado ao todo para seguir um fluxo intuitivo, além da função de cardápio o site já conta com uma single-page administrativa com pedido,estoque e financeiro sendo o acesso dessa página protegido por spring security.

*-Backend-*

A parte responsável pelo funcionamento do site,controllers,services e o código ao todo são feito através do java Spring-Boot (DevTools e Lombok) utilizando dessa ferramenta para a criação de API REST que foram testados no postman para antes serem aplicados para interação do site, o banco de dados utilizado foi MySQL via JPA e H2 com 7 tabelas cadastrados.

*-Frontend-*

A interface foi criado em cima de CSS,HTML e Thymeleaf por melhor compatibilidade com um design simples porém agrádavel e prático de usar sem poluição e complexidade desnecessária, o JavaScript está sendo aplicado aos pouco para melhor interatividade e navegação melhor via DOM e fetch.

*-FLUXO DE FUNCIONAMENTO-*

<img width="907" height="451" alt="cardapio" src="https://github.com/user-attachments/assets/47464656-d9ec-4b3f-a321-3b0588a4a364" />

Ao acessar o site você já se depara com a lista dos produtos do quais o delivery vende divididos por cada categoria:Pastel,Bedidas e doces e cada um deles pode ser selecionado e adicionado ao carrinho(lembre-se que algumas features apenas funcionam quando o cliente está logado e cadastrado no sistema) 

<img width="436" height="437" alt="carrinho" src="https://github.com/user-attachments/assets/96c052f5-23eb-4e0f-8dbb-29400a1d60da" />

Os produtos de interesse são adicionados no carrinho já somando o valor total, além da opção que torna possível retirar um dos produtos se necessário

<img width="887" height="419" alt="pagamento" src="https://github.com/user-attachments/assets/4d509b93-7520-4214-972b-6bb1e91d59d6" />

tela de pagamento onde mostra os produtos que foram colocado, a quantia e os dados para conferir, após Confirmar pedido o usuário vai ser redirecionado para o checkout

<img width="876" height="432" alt="checkout" src="https://github.com/user-attachments/assets/e20ddd42-395f-436b-92fd-add7c43c024a" />

aqui escolhe qual o método mais adequado para pagamento, a razão de ser pela integração do mercado pago é por que simplifica a seleção de pagamento invés da necessidade de configurar no próprio código.

<img width="891" height="289" alt="pedido" src="https://github.com/user-attachments/assets/b4e10b91-539c-4f5f-aeab-a900eccc1874" />

o pedido vai para a page de administração do qual vai avaliar o pedido podendo cancelar ou aceitar o pedido alterando entre "Aberto" para "processando", não está devidamente configuração ainda já que a intenção seria que a transição ocorresse automaticamente e o administrador tivesse o poder de cancelar ou aceitar no status "processando":

<img width="714" height="207" alt="status processando" src="https://github.com/user-attachments/assets/2476f20d-1a63-4e8b-9d1f-3143afe6b875" />
<img width="706" height="200" alt="status finalizado" src="https://github.com/user-attachments/assets/83994df9-7ac1-451d-91ec-6a67b79534f9" />

***OBS: após a mudança de status o sistemma já desconta os produtos do pedido no estoque como deve funcionar em um sistema desse tipo***

<img width="214" height="374" alt="icon meus pedidos" src="https://github.com/user-attachments/assets/6e7ad2e0-31a2-4e16-85b3-a45f408da687" />
<img width="741" height="192" alt="meus pedidos" src="https://github.com/user-attachments/assets/61729386-26ef-42d5-87ef-fabb25c1df4a" />

no final do fluxo, o cliente pode visualizar no icon pedido ou atráves do perfil como está o andamento do pedido


já essas aqui são parte do sistema participando do fluxo de forma indireta ou diretamente:

<img width="897" height="434" alt="estoque" src="https://github.com/user-attachments/assets/b25ee965-60ed-4a70-b6c0-b1d8df572db1" />

é a área do ERP responsável por controlar o estoque vendo de forma rapída cada produto e conseguindo fazer cada opção como + estoque,desativar/reativar e remover
e tem um fácil sistema de cadastro para caso precisar adicionar mais um produto na lista do cardápio

<img width="905" height="293" alt="financeiro" src="https://github.com/user-attachments/assets/63ab1f35-84b5-4013-98d7-6254a8bc1673" />

contabiliza todos os ganhos dividios em mês,semana e dia, está em um estado bem prematuro então por enquanto não uma funcionalidade real


## Tecnologias

### Backend
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- H2 Database
- Maven

### Frontend
- HTML5
- CSS3
- Thymeleaf
- JavaScript




