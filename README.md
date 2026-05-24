# Tutorial 4 -Flows&Firebase

**Curso:** Engenharia Informática e Multimédia

**Estudante:** Rodrigo Silva, A51743

**Data:** 24/05/2026

**Link do Repositório:** https://github.com/Digo34/DAM-TP4

---

## 1. Introdução

Este quarto trabalho prático (TP4) da cadeira de Desenvolvimento de Aplicações Móveis (DAM) foca-se na exploração de fluxos assíncronos reativos, no consumo de APIs de Inteligência Artificial e na integração de serviços Cloud em ecossistemas móveis.

O trabalho aborda três pilares fundamentais do desenvolvimento moderno: a programação reativa baseada em **Kotlin Flows e Coroutines**, a interação polimórfica com Large Language Models (**OpenAI GPT e Google Gemini**) e a implementação de infraestruturas *Backend-as-a-Service* (BaaS) através do **Google Firebase**.

**Objetivos de Aprendizagem:**
* Dominar concorrência, canais de comunicação e fluxos de dados assíncronos (`StateFlow`, `Channels`) para uma gestão de estado robusta.
* Consumir e modular parâmetros de LLMs (como temperatura e tokens), executando análise de sentimento e processamento multimodal (imagem e texto).
* Configurar e integrar múltiplos serviços do ecossistema Firebase (Autenticação, Realtime Database, Cloud Firestore e Cloud Storage).

## 2. Visão Geral do Sistema

O repositório está estruturado em torno das três componentes requisitadas pelo enunciado:

### 2.1 Gestão de Estado Reativa (intro-coroutines V2)
Evolução de uma aplicação baseada em Java Swing que realiza pedidos concorrentes de rede. A infraestrutura foi refatorizada para abolir atualizações manuais, delegando a monitorização de estados a pipelines reativos assíncronos.

### 2.2 Assistente de Inteligência Artificial (AISimpleCall & Gemini Starter)
* **Consola Multi-Provider**: Uma aplicação baseada em comandos estruturada através do padrão *Factory*, permitindo alternar de forma transparente entre chamadas RESTful puras ou serializadas via data classes para a OpenAI e a Gemini.
* **Android Multimodal App**: Projeto Android baseado no modelo *Gemini API Starter* focado na submissão de prompts textuais combinados com análise visual de imagens.

## 3. Arquitetura e Design

### 3.1 Arquitetura do Pipeline de Dados (Flows & Channels)
* **Backing Property Pattern**: Aplicação rigorosa de encapsulamento no módulo de UI. Um `MutableStateFlow` privado (`_loadingState`) manipula internamente as mutações de dados, expondo apenas um `StateFlow` público e imutável para leitura segura por coletores assíncronos.
* **Controlo de Fluxo e Back-pressure**: Introdução de `Channels` intermédios para isolar os produtores de dados (pedidos concorrentes) dos consumidores (atualizações na UI), garantindo uma separação de conceitos limpa e simplificando o cancelamento do ciclo de vida das Coroutines.

### 3.2 Design Polimórfico das APIs de IA
O subprojeto `AISimpleCall` implementa uma arquitetura agnóstica a fornecedores. Uma interface base define o contrato do assistente, sendo herdada por implementações concretas especializadas (`AIAssistantGemini` e `AIAssistantOpenAI`) que gerem os cabeçalhos de autenticação específicos e as suas respeitantes estratégias de serialização Gson/OkHttp.

## 4. Implementação

### 4.1 Processamento de Fluxos e Estados
* **LoadingStateData**: Implementação do modelo de dados que unifica o estado do enum `LoadingStatus` (`INIT`, `IN_PROGRESS`, `COMPLETED`, `CANCELED`) com metadados temporais (`elapsedTime`).
* **Coletores Reactivos**: Implementação do método `observeLoadingStatus()`, que lança uma Coroutine para escutar o `loadingState` e atualizar dinamicamente os componentes gráficos e ícones da UI em função do estado emitido.

### 4.2 Ajustes e Recursos Avançados nas LLMs
* **Configuração Dinâmica**: Adaptação do motor para ler parâmetros de `temperature` e `max_tokens` a partir do ficheiro centralizado `config.properties`, suportando a ausência destes valores.
* **Análise de Sentimento Estrita**: Configuração de um ecrã de processamento que força a resposta da IA a obedecer a uma escala linear de 7 pontos (de *Very Negative* a *Very Positive*), obrigando o output a ser devolvido estritamente estruturado em formato JSON com campos chave de classificação (`rating`) e fundamentação textual (`justification`).
* **Gemini Multimodal**: Extensão da aplicação Android nativa para processar bitmaps de imagens locais (bolos/cookies) em simultâneo com comandos textuais para deduzir receitas.

## 5. Testes e Validação

* **Validação de Elasticidade de IA**: Execução de baterias de testes comparativos variando a temperatura entre `0` (precisão matemática e determinística), `0.5` (equilibrada) e `1` (criatividade máxima), registando as divergências geradas nos outputs para os mesmos prompts.
* **Comportamento sob Carga e Cancelamento**: Testes de interrupção de fluxos assíncronos em background para assegurar que os `Channels` e as Coroutines associadas fecham imediatamente ao cancelar a operação na UI, mitigando fugas de memória.

## 6. Instruções de Utilização

### 6.1 Requisitos
* JDK 17+ e IntelliJ IDEA (para os módulos de consola/Swing).
* Android Studio (Ladybug / Jellyfish ou superior) com Android SDK atualizado.
* Ficheiro de credenciais privado `google-services.json` configurado na consola do Firebase e depositado na diretoria `/app` dos projetos móveis.

### 6.2 Execução
1. **Módulos de Coroutines/IA**: Importar a diretoria correspondente no IntelliJ IDEA, preencher o ficheiro `config.properties` com as chaves das respetivas APIs e executar a classe `Main`.

## 12. Versão e Histórico de Commits
O histórico de Git ilustra a progressão ordenada do projeto: a transição sistemática da gestão manual de ecrãs para a reatividade assíncrona, a parametrização do assistente de IA local e a consolidação dos serviços Cloud do Firebase através do preenchimento e resolução de bugs de interface.

## 13. Dificuldades e Aprendizagem
* **Concorrência Limpa**: A transição de lógicas baseadas em simples callbacks para o ecossistema de fluxos (`StateFlow`) exigiu uma mudança de mentalidade na estruturação do ciclo de vida da UI, mas provou ser altamente eficaz contra acoplamento de código.
* **Problemas Firebase**: Devido a complicações na utilização dos serviços do Firebase, não foi possível realizar as tarefas indicadas para esta parte do tutorial. Os ficheiros do código encontram-se no repositório na mesma, mas não funcionam como pretendido e tem partes incompletas.

## 15. Divulgação de Uso de IA
Este relatório e as fundações estruturais do projeto contaram com o auxílio de ferramentas de IA (Gemini) para a clarificação de dúvidas durante o desenvolvimento. Todo o desenvolvimento lógico e a integração final com os serviços oferecidos pelos agentes foram operados e validados por mim.
