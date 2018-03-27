# Reddit Latest News Browser


#### Desafio

Criar um aplicativo Android nativo que seja capaz de listar e visualizar detalhes de posts mais recentes do serviço Reddit (www.reddit.com). A aplicação deve possuir minimamente duas telas: 
- Uma tela com a lista dos posts mais recentes 
- Uma tela capaz de mostrar mais detalhes sobre cada post.

#### Regras Gerais

- Você deve desenvolver o projeto em Java no Android Studio utilizando a ferramenta de build default, o Gradle. **[Está em Kotlin]**
- É permitido usar quaisquer bibliotecas que desejar para ajudar a construir este aplicativo
- É importante possuir uma build de produção (assinado) no gradle.
- Utilize as praticas mais modernas de material design para construir a interface de sua aplicação.

#### Pontos extras:

- Transições de tela e animações **[Done]**
- Testes unitários **[TODO]**
- Testes instrumentados **[Done]**
- Utilizar o Google Custom Chrome Tabs para abrir URLs externas de um post na tela de detalhes **[??]**
- Criar uma tela nativa onde seja possível visualizar o detalhe de um post e seus comentários **[Done]**
- Suporte a tablets **[TODO]**
- Jacoco Coverage Reports **[Done]**
- Calabash **[??]**

#### Exercício

##### Listagem de posts:

- Listar novos posts Android do Reddit. 
- Você pode utilizar [este endpoint](www.reddit.com/r/Android/new.json) para isso.
- Cada post deve conter minimamente um título e uma imagem (quando houver).
- Você pode adicionar nesta listagem quaisquer outros elementos vindos da API que julgar interessante (score? número de comentários? Você escolhe.).
- A lista deve ser carregada de modo paginado e se possível deve ser atualizável, automaticamente ou manualmente pelo usuário.
- Ao clicar em um post da lista, o aplicativo deve permitir a visualização dos detalhes daquele post.

###### Obs: Está paginado, não está atualizável. O resto está de acordo.

##### Detalhe de post:

- Abrir o endereço do post em um navegador 
- Aqui você pode decidir apenas carregar uma tela com o link externo (quando houver) ou carregar uma tela de detalhe com seus respectivos comentários como sugerido na seção de pontos extras. (Há uma API para recuperar os comentários)
- A API do Reddit disponibiliza vários tamanhos de imagens, tente descobrir qual o melhor tamanho para o dispositivo do usuário, você pode se basear no tamanho da tela e das imagens disponíveis em cada post.

###### Obs: Está abrindo uma tela nativa, mas acabei não utilizando essa imagem para precisar fazer os cálculos de tamanho de tela =( .. Mas sim, eu saberia como fazer xPP

#### Api do Reddit: https://www.reddit.com/dev/api/

###### Obrigado pela oportunidade, sinto muito não ter tido tempo suficiente para entregar tudo e mais um pouco do que vocês me pediram, mas espero que gostem. =D

### Parte técnica

#### Arquitetura
A arquitetura deste aplicativo foi baseada na arquitetura proposta pelo Google no IO de 2017. **[Saiba mais](https://developer.android.com/topic/libraries/architecture/guide.html)**

###### Legal, mas porque?? Porque não usou Rx??
Gostei bastante da abordagem dessa arquitetura, é muito simples.
Não usei Rx porque a Support Library agora já trás a parte de lifecycle do Android implementada. Então não quis usar Rx, que trás reatividade ao código, sendo que já tenho uma plataforma de reatividade, que é o LiveData.

#### Divisão das camadas

 - **View** *Activities, Fragments, Views, etc*
 - **ViewModel** *Trata os dados trazidos pelo Repository e entrega para a **View***
 - **Repository** *Trás dados (**Data**), não importa de onde e nem como, a ideia é ser transparente com as camadas acima*
 - **Data** *Não é bem uma camada, mas seriam os dados crus, que serão entregues ao **ViewModel** pelo **Repository***

###### Dividi as camadas também baseado na arquitetura proposta, inclusive a nomenclatura

#### Módulos

Bom, aqui é bastante discutível. Eu tenho essa mania de dividir os projetos que eu faço em dois módulos, geralmente. Um que cuida dos dados e um que cuida da UI. Mas baseado em algumas tendências e na documentação de **[Instant Apps](https://developer.android.com/topic/instant-apps/index.html)**, acabei optando por uma quebra um pouco maior.

Dito isso, a ideia de Feature Modules veio.
 
###### Feature Modules?
Basicamente, para cada feature, um novo módulo, com as classes pertinentes a *UI* relacionadas a essa feature, deveria ser criado.
 
Claro que essa "quebra" de módulo tem que ser acordada pelo time assim como o conteúdo do módulo.
  
*Ex: Se esse módulo vai conter apenas a tela de listagem, ou se vai conter a listagem e o detalhe.*  
*Ex2: Se vale a pena ou não quebrar em um novo módulo, ou se essa nova demanda se encaixa num módulo já existente.*
 
 - **:module:sdk** *A ideia é ter apenas dados aqui... API, Banco de dados, Modelos... E expor apenas os **Repositories** para os módulos que vão usufruir deste* (Sem nada de View aqui)
 - **:module:base** *Aqui vão implementações Base, Views mais genéricas, Temas, cores, alguns ícones genéricos e utilitários. A ideia é ter um bloco mais genérico para criar módulos relacionados a **UI***
 - **:module:test** *Implementação base para testar um **Feature Module** com mocks e factories de modelos*
 - **:feature:comment** *Feature Module que contém toda a parte de Comentários* 
 - **:app** *Finalmente **O Aplicativo**. Esse cara vai juntar todos os **Feature Modules** para formar o app completo* 

#### Para rodar os testes com Jacoco

 - **:feature:comment** `:feature:comment:jacocoTestReport`
 - **:app** `:app:jacocoTestReport`

#### Pattern de testes instrumentados
O pattern para os testes instrumentados foi baseado na maneira como o **[Robots Pattern](https://academy.realm.io/posts/kau-jake-wharton-testing-robots/)** divide o código de teste e como o **[Cucumber](https://cucumber.io/)** descreve cada cenário.

O grande foco é aumentar a legibilidade e a manutenibilidade dos testes. E por incrível que pareça, a velocidade e facilidade para escrever os testes aumenta consideravelmente... **Vai por mim... acredite!**

#### TODOs
- Testes nos módulos*:
    - **:module:base** - Instrumentados / Unitários
    - **:module:test** - Unitários
    - **:module:sdk** - Unitários
- Listar mais comentários
- Mostrar mais informações de um Post
- Jacoco unificado

###### A importância de ter testes em todos os módulos, validando o código ali presente é muito grande. \o/

## Have fun!