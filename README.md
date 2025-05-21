  Bloco de Notas Android
Olá! Este é um aplicativo de bloco de notas básico que desenvolvi como parte dos estudos de Computação para Dispositivos Móveis no 4º semestre do curso de Análise e Desenvolvimento de Sistemas (ADS). O objetivo principal foi aplicar conceitos de persistência de dados localmente utilizando SQLite, além de praticar a construção de interfaces de usuário com Android Studio.

Funcionalidades
O aplicativo permite ao usuário:

Visualizar todas as notas: Uma lista de notas é exibida na tela principal, carregada diretamente do banco de dados.
Criar novas notas: Um botão flutuante (FAB) permite adicionar um novo registro no banco de dados.
Editar notas existentes: Clicar em uma nota na lista abre a tela de edição, pré-preenchida com os dados da nota selecionada diretamente do banco.
Excluir notas: Na tela de edição, é possível remover uma nota do banco de dados.
Estrutura do Projeto e Breve Explicação do Código
O projeto foi organizado para seguir boas práticas de desenvolvimento Android, separando as responsabilidades em diferentes pacotes e arquivos:

app/
├── manifests/
│   └── AndroidManifest.xml
├── java/
│   └── com.example.notepad/
│       ├── adapter/
│       │   └── NotasAdapter.java
│       ├── database/
│       │   └── NotasDatabaseHelper.java
│       ├── model/
│       │   └── Nota.java
│       ├── DetalheNotaActivity.java
│       └── MainActivity.java
└── res/
    ├── drawable/
    ├── layout/
    │   ├── activity_detalhe_nota.xml
    │   ├── activity_main.xml
    │   └── item_nota.xml
    ├── mipmap/
    └── values/
Componentes Principais:
MainActivity.java: Esta é a tela principal do aplicativo. Ela é responsável por:

Exibir a lista de notas usando um RecyclerView.
Carregar as notas do banco de dados ao iniciar e retomar o aplicativo (onResume), garantindo que a lista esteja sempre atualizada com os dados persistidos.
Gerenciar o clique no botão de "Adicionar Nota" (Floating Action Button - FAB), que leva à DetalheNotaActivity.
Lidar com o clique em cada item da lista, passando os dados da nota para a DetalheNotaActivity para edição.
DetalheNotaActivity.java: Esta Activity lida com a criação e edição de notas. Ela:

Recebe dados de uma nota existente (se for uma edição) ou inicia vazia (para uma nova nota).
Permite ao usuário inserir/editar o título e o conteúdo da nota.
Salva a nota no banco de dados (inserindo ou atualizando).
Oferece a opção de excluir uma nota existente do banco de dados com uma confirmação.
Nota.java (Pacote model): Esta é uma classe simples que representa o modelo de dados de uma nota. Ela contém atributos como id, titulo e conteudo, além de métodos getter e setter. Implementa Serializable para facilitar a passagem de objetos entre Activities.

NotasAdapter.java (Pacote adapter): Este é o adaptador para o RecyclerView. Ele é essencial para:

Vincular os dados da lista de objetos Nota (obtidos do banco de dados) às visualizações de cada item na lista.
Utiliza o layout item_nota.xml para exibir cada nota individualmente.
Gerencia o clique em cada item da lista.
Persistência de Dados: O Banco de Dados SQLite
Um requisito fundamental para este projeto foi a implementação de um sistema de persistência de dados local, utilizando o SQLite, que é um sistema de gerenciamento de banco de dados relacional leve e integrado ao Android.

NotasDatabaseHelper.java (Pacote database): Esta classe é o coração da interação com o banco de dados. Ela herda de SQLiteOpenHelper, uma classe utilitária que simplifica a criação e o gerenciamento de bancos de dados no Android. Seus principais aspectos são:

Criação do Banco de Dados: No método onCreate(), a tabela notas é criada com as colunas id (chave primária auto-incrementável), titulo e conteudo. Isso garante que o banco de dados e sua estrutura sejam estabelecidos na primeira execução do aplicativo.
Gerenciamento de Versão: O método onUpgrade() permite migrar o esquema do banco de dados se a versão for alterada, garantindo a compatibilidade futura.
Operações CRUD (Create, Read, Update, Delete): A classe expõe métodos essenciais para manipular os dados das notas:
inserirNota(Nota nota): Adiciona uma nova nota à tabela.
buscarNotas(): Recupera todas as notas armazenadas no banco de dados.
atualizarNota(Nota nota): Modifica uma nota existente com base em seu ID.
excluirNota(int id): Remove uma nota específica do banco de dados pelo seu ID.
Funcionamento: Quando o aplicativo é iniciado, a MainActivity se conecta ao NotasDatabaseHelper para buscar todas as notas e exibi-las. Ao adicionar, editar ou excluir uma nota na DetalheNotaActivity, as operações são enviadas ao NotasDatabaseHelper, que interage diretamente com o banco de dados SQLite. As alterações são persistidas e podem ser visualizadas até mesmo através do Database Inspector no Android Studio, mesmo após o aplicativo ser fechado e reaberto.

Layouts XML:
activity_main.xml: Define a interface da tela principal, contendo um TextView para o título, um RecyclerView para a lista de notas e um FloatingActionButton para adicionar novas notas.
item_nota.xml: Define como cada item individual da lista de notas deve ser exibido. Utiliza um CardView para um visual mais moderno, com TextViews para o título e uma prévia do conteúdo da nota.
activity_detalhe_nota.xml: Define a interface para a tela de criação/edição de notas, incluindo EditTexts para o título e conteúdo, e botões para "Salvar" e "Excluir".
Este projeto foi um excelente desafio para consolidar conhecimentos sobre o ciclo de vida de Activities, persistência de dados com SQLite e a arquitetura de UI com RecyclerView no desenvolvimento Android nativo, cumprindo as exigências da disciplina.
