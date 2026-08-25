import { useState, useEffect, useRef, useCallback } from "react";
import fotoPerfil from "./assets/IMG_1538.jpeg";

/* ------------------------------------------------------------------
   👉 TROQUE AQUI PELA SUA FOTO
   Cole a URL da sua foto (ou um import). Deixando vazio, aparece um
   retrato-marcador com suas iniciais no lugar.
------------------------------------------------------------------- */
const FOTO = "";

const CONTATO = {
  email: "camilasoares1507@gmail.com",
  github: "github.com/camila-soares",
  linkedin: "linkedin.com/in/camila-soares-3475027a",
  cidade: "Recife, PE",
};

const NAV = [
  { id: "inicio", rotulo: "Início" },
  { id: "sobre", rotulo: "Sobre mim" },
  { id: "habilidades", rotulo: "Habilidades" },
  { id: "projetos", rotulo: "Projetos" },
  { id: "trajetoria", rotulo: "Trajetória" },
  { id: "contato", rotulo: "Contato" },
];

/* Habilidades vindas do currículo, agrupadas por como eu realmente uso cada uma */
const HABILIDADES = [
  {
    grupo: "Backend",
    nota: "É onde eu moro. Java desde 2017, do Forms ao microsserviço.",
    itens: [
      { nome: "Java", destaque: true },
      { nome: "Spring Boot", destaque: true },
      { nome: "Micronaut" },
      { nome: "JSF / Oracle Forms" },
      { nome: "Kotlin", nota: "em formação" },
      { nome: "Go" },
      { nome: "C / C++ / C#" },
    ],
  },
  {
    grupo: "Frontend",
    nota: "Entrego a tela junto com a API quando o time precisa das duas pontas.",
    itens: [
      { nome: "Angular", destaque: true },
      { nome: "React", destaque: true },
      { nome: "TypeScript" },
      { nome: "JavaScript" },
      { nome: "HTML5 / CSS3" },
      { nome: "Dart / Flutter" },
    ],
  },
  {
    grupo: "Dados",
    nota: "Query lenta é problema de produto. Gosto de abrir o plano de execução.",
    itens: [
      { nome: "Oracle", destaque: true },
      { nome: "SQL Server" },
      { nome: "MySQL" },
      { nome: "Otimização de queries" },
      { nome: "Rotinas batch" },
    ],
  },
  {
    grupo: "Automação com Python",
    nota: "Uso Python pra tirar da minha frente tudo que eu faria duas vezes.",
    itens: [
      { nome: "Python", destaque: true },
      { nome: "Scripts de apoio" },
      { nome: "Integrações e cargas" },
    ],
  },
  {
    grupo: "Infra e entrega",
    nota: "Sei o caminho do commit até o ar — e o caminho de volta.",
    itens: [
      { nome: "Docker", destaque: true },
      { nome: "AWS" },
      { nome: "Azure" },
      { nome: "Tomcat / GlassFish" },
      { nome: "Maven / Gradle / Ant" },
      { nome: "Git / GitHub / GitLab" },
    ],
  },
  {
    grupo: "Qualidade e processo",
    nota: "Teste é o que me deixa mexer em sistema crítico sem prender a respiração.",
    itens: [
      { nome: "TDD", destaque: true },
      { nome: "JUnit / Mockito" },
      { nome: "REST / SOAP" },
      { nome: "BPM / Camunda" },
      { nome: "Scrum / Kanban" },
      { nome: "Legado: Struts, JasperReports" },
    ],
  },
];

/* Soft skills do currículo, escritas do jeito que elas aparecem no dia a dia */
const JEITO = [
  {
    titulo: "Resolução de problemas",
    texto: "Gosto do momento em que o bug para de ser misterioso e vira uma linha.",
  },
  {
    titulo: "Pensamento crítico",
    texto: "Antes de construir, pergunto se o problema é mesmo esse.",
  },
  {
    titulo: "Trabalho em equipe",
    texto: "Reviso PR com calma e explico o porquê, não só o que mudar.",
  },
  {
    titulo: "Comunicação assertiva",
    texto: "Consigo contar pro time de negócio o que o sistema faz sem usar jargão.",
  },
  {
    titulo: "Organização",
    texto: "Deixo rastro: commit, README, decisão registrada onde alguém acha.",
  },
  {
    titulo: "Adaptabilidade",
    texto: "Já entrei em sete stacks diferentes. Nenhuma delas era a que eu esperava.",
  },
];

/* Ícones das tecnologias (Devicon, via CDN) */
const ICONE = "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons";
const TEC = {
  java: { nome: "Java", src: `${ICONE}/java/java-original.svg` },
  spring: { nome: "Spring", src: `${ICONE}/spring/spring-original.svg` },
  quarkus: { nome: "Quarkus", src: `${ICONE}/quarkus/quarkus-original.svg` },
  kafka: { nome: "Apache Kafka", src: `${ICONE}/apachekafka/apachekafka-original.svg` },
  rabbitmq: { nome: "RabbitMQ", src: `${ICONE}/rabbitmq/rabbitmq-original.svg` },
  mongodb: { nome: "MongoDB", src: `${ICONE}/mongodb/mongodb-original.svg` },
  postgres: { nome: "PostgreSQL", src: `${ICONE}/postgresql/postgresql-original.svg` },
  docker: { nome: "Docker", src: `${ICONE}/docker/docker-original.svg` },
  kubernetes: { nome: "Kubernetes", src: `${ICONE}/kubernetes/kubernetes-original.svg` },
  aws: { nome: "AWS", src: `${ICONE}/amazonwebservices/amazonwebservices-plain-wordmark.svg`, largo: true },
  jenkins: { nome: "Jenkins", src: `${ICONE}/jenkins/jenkins-original.svg` },
  sonar: { nome: "SonarQube", src: `${ICONE}/sonarqube/sonarqube-original.svg` },
  react: { nome: "React", src: `${ICONE}/react/react-original.svg` },
};

/* Projetos do meu GitHub — github.com/camila-soares */
const PROJETOS = [
  {
    nome: "microservice",
    titulo: "E-commerce orientado a eventos",
    stack: "11 microsserviços · Spring Cloud · RabbitMQ · Kubernetes",
    tecs: ["java", "spring", "rabbitmq", "postgres", "mongodb", "docker", "kubernetes"],
    texto:
      "Plataforma de e-commerce em arquitetura orientada a eventos: pedido, pagamento, envio, fulfillment, qualificação e notificação coreografados por RabbitMQ, com Eureka para descoberta e gateway na entrada. Tem OpenAPI, AsyncAPI, máquina de estado do pedido e manifestos Kubernetes por serviço.",
    repo: "https://github.com/camila-soares/microservice",
  },
  {
    nome: "api-hexagonal",
    titulo: "API em arquitetura hexagonal",
    stack: "Java 17 · Spring Boot · Kafka · MongoDB · ArchUnit",
    tecs: ["java", "spring", "kafka", "mongodb", "docker"],
    texto:
      "Portas e adaptadores separados de verdade: caso de uso no centro, Kafka e MongoDB nas bordas, busca de endereço por CEP via OpenFeign. O ArchUnit testa se as regras de arquitetura continuam valendo a cada build.",
    repo: "https://github.com/camila-soares/api-hexagonal",
  },
  {
    nome: "plataforma-empregos-ia",
    titulo: "Plataforma de empregos com IA",
    stack: "Spring Boot · Spring Security · JWT · PostgreSQL · Swagger",
    tecs: ["java", "spring", "postgres"],
    texto:
      "SaaS que cruza candidatos e vagas e devolve um score de compatibilidade de 0 a 100, com planos de assinatura e processamento assíncrono. É o repositório mais documentado que tenho: arquitetura, exemplos de API e fluxo assíncrono escritos.",
    repo: "https://github.com/camila-soares/alelofrota-back-end",
  },
  {
    nome: "api-calculo-rescisao",
    titulo: "Cálculo de rescisão trabalhista",
    stack: "Java 17 · Spring Boot · JPA · iText · OpenAPI · Docker",
    tecs: ["java", "spring", "docker"],
    texto:
      "Regra trabalhista brasileira do começo ao fim: tipos de rescisão, cálculo, persistência e o termo saindo em PDF no final. Documentada em OpenAPI e empacotada em Docker.",
    repo: "https://github.com/camila-soares/api-calculo-rescisao",
  },
  {
    nome: "minerao-bff-quarkus",
    titulo: "Microsserviços com BFF em Quarkus",
    stack: "Quarkus · Kafka · REST · 4 módulos",
    tecs: ["java", "quarkus", "kafka"],
    texto:
      "Gateway BFF na frente de três serviços — cotação, proposta e relatório — conversando por eventos Kafka. A cotação USD/BRL atualiza sozinha por scheduler.",
    repo: "https://github.com/camila-soares/minerao-bff-quarkus",
  },
  {
    nome: "leitor-ftp",
    titulo: "Migração de arquivos de FTP para a nuvem",
    stack: "Spring Boot · AWS S3 · Kafka · PostgreSQL · Jenkins · SonarQube",
    tecs: ["java", "spring", "aws", "kafka", "postgres", "jenkins", "sonar"],
    texto:
      "Lê o arquivo no FTP, sobe para o S3, publica o evento no Kafka e guarda o status no banco. Nasceu de um caso real de tirar rotina legada do servidor e colocar em microsserviço.",
    repo: "https://github.com/camila-soares/leitor-ftp",
  },
  {
    nome: "app-financas + minhas-financas-app",
    titulo: "Controle financeiro, das duas pontas",
    stack: "Spring Boot · JWT · PostgreSQL · React · PrimeReact",
    tecs: ["java", "spring", "postgres", "react"],
    texto:
      "Backend com autenticação por token e lançamentos por tipo e status; frontend em React consumindo a API. O par que mostra o caminho inteiro, do endpoint até a tela.",
    repo: "https://github.com/camila-soares/app-financas",
  },
];

const TRAJETORIA = [
  {
    empresa: "MV Informática",
    cargo: "Desenvolvedora Java Sênior",
    periodo: "Jan 2024 — atual",
    local: "Recife, PE",
    texto:
      "Sistemas corporativos em Java com Spring Boot e Oracle Forms, integrações com Oracle DB e módulos legados, rotinas batch e suporte a aplicações críticas.",
  },
  {
    empresa: "act digital",
    cargo: "Desenvolvedora Backend Java",
    periodo: "Ago 2023 — Mai 2025",
    local: "Recife, PE",
    texto: "Serviços e soluções para o banco Sicoob.",
  },
  {
    empresa: "Banco Inter",
    cargo: "Desenvolvedora Java",
    periodo: "Jul 2022 — Dez 2023",
    local: "Remoto",
    texto:
      "Backend em Micronaut, evolução de sistemas críticos, testes unitários e integrações AWS.",
  },
  {
    empresa: "Blue Technology",
    cargo: "Engenheira Full Stack",
    periodo: "Jul 2022 — Jul 2023",
    local: "Recife, PE",
    texto: "Desenvolvimento da aplicação web Comunidade Sebrae PR.",
  },
  {
    empresa: "Capgemini",
    cargo: "Engenheira de Software Java Sênior",
    periodo: "Fev 2021 — Jan 2023",
    local: "—",
    texto: "Tech Lead na construção de microsserviços para o Bradesco.",
  },
  {
    empresa: "Alelo Brasil",
    cargo: "Desenvolvedora Full Stack",
    periodo: "Jul 2020 — Fev 2021",
    local: "Barueri, SP",
    texto: "",
  },
  {
    empresa: "Cast Group",
    cargo: "Desenvolvedora Java",
    periodo: "Jan 2019 — Jul 2020",
    local: "Recife, PE",
    texto: "",
  },
  {
    empresa: "MuchMore Digital",
    cargo: "Engenheira de Software Júnior",
    periodo: "Mai 2018 — Jan 2019",
    local: "Recife, PE",
    texto: "",
  },
  {
    empresa: "Accenture",
    cargo: "Analista de Desenvolvimento Java",
    periodo: "Fev 2017 — Mai 2018",
    local: "—",
    texto: "Onde tudo começou.",
  },
];

const CSS = `
@import url('https://fonts.googleapis.com/css2?family=Bricolage+Grotesque:opsz,wght@12..96,500;12..96,700;12..96,800&family=Newsreader:ital,opsz,wght@0,6..72,300;0,6..72,400;1,6..72,300;1,6..72,400&family=JetBrains+Mono:wght@400;500&display=swap');

.cs-root{
  --papel:#F1EDE4;
  --papel-claro:#FAF7F1;
  --tinta:#10324B;
  --tinta-2:#1B4C6B;
  --texto:#23221C;
  --musgo:#6E6A5F;
  --manga:#E39A2E;
  --sage:#3E7F72;
  --linha:rgba(16,50,75,0.16);
  --display:'Bricolage Grotesque',sans-serif;
  --corpo:'Newsreader',Georgia,serif;
  --mono:'JetBrains Mono',ui-monospace,monospace;
  --pad-lado:clamp(20px,5vw,72px);
  --maxw:1120px;

  background:var(--papel);
  color:var(--texto);
  font-family:var(--corpo);
  font-size:17px;
  line-height:1.65;
  -webkit-font-smoothing:antialiased;
  overflow-x:hidden;
}
.cs-root *{box-sizing:border-box;}
.cs-root h1,.cs-root h2,.cs-root h3,.cs-root p,.cs-root ul,.cs-root li,.cs-root figure{margin:0;padding:0;list-style:none;}
.cs-root a{color:inherit;text-decoration:none;}
.cs-root button{font:inherit;color:inherit;background:none;border:none;cursor:pointer;}
.cs-root :focus-visible{outline:2px solid var(--manga);outline-offset:3px;border-radius:2px;}

.cs-faixa{width:100%;}
.cs-limite{max-width:var(--maxw);margin:0 auto;padding-left:var(--pad-lado);padding-right:var(--pad-lado);}
.cs-secao{padding-top:clamp(72px,11vw,132px);padding-bottom:clamp(72px,11vw,132px);scroll-margin-top:76px;}

/* ---- tipografia ---- */
.cs-eyebrow{
  font-family:var(--mono);font-size:11.5px;letter-spacing:0.16em;text-transform:uppercase;
  color:var(--musgo);
}
.cs-h2{
  font-family:var(--display);font-weight:800;letter-spacing:-0.03em;line-height:1;
  font-size:clamp(34px,5.4vw,58px);color:var(--tinta);
}
.cs-lead{font-size:clamp(18px,2vw,21px);color:var(--musgo);max-width:52ch;}

/* ---- azulejo ---- */
.cs-azulejo{
  background-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='44' height='44' viewBox='0 0 44 44'%3E%3Cg fill='none' stroke='%23BFD8E6' stroke-width='1.1'%3E%3Cpath d='M22 7C28 13 31 16 37 22C31 28 28 31 22 37C16 31 13 28 7 22C13 16 16 13 22 7Z'/%3E%3Ccircle cx='22' cy='22' r='3.2'/%3E%3Cpath d='M0 0L6 6M44 0L38 6M0 44L6 38M44 44L38 38'/%3E%3C/g%3E%3C/svg%3E");
  background-size:44px 44px;
}

/* ---- cabeçalho ---- */
.cs-topo{
  position:fixed;inset:0 0 auto 0;z-index:50;
  transition:background 300ms ease,box-shadow 300ms ease,backdrop-filter 300ms ease;
}
.cs-topo-interno{
  display:flex;align-items:center;justify-content:space-between;gap:24px;
  height:76px;transition:height 300ms ease;
}
.cs-topo.cs-preso .cs-topo-interno{height:62px;}
.cs-topo.cs-preso{
  background:rgba(241,237,228,0.88);backdrop-filter:blur(10px);
  box-shadow:0 1px 0 var(--linha);
}
.cs-marca{font-family:var(--display);font-weight:800;font-size:17px;letter-spacing:-0.02em;color:#F6EFE2;transition:color 300ms ease;display:flex;align-items:center;gap:9px;}
.cs-topo.cs-preso .cs-marca{color:var(--tinta);}
.cs-marca-ponto{width:9px;height:9px;border-radius:50%;background:var(--manga);flex:none;}
.cs-menu{display:flex;align-items:center;gap:4px;}
.cs-link{
  position:relative;font-family:var(--mono);font-size:12.5px;letter-spacing:0.04em;
  padding:9px 13px;color:rgba(246,239,226,0.72);transition:color 220ms ease;
}
.cs-topo.cs-preso .cs-link{color:var(--musgo);}
.cs-link::after{
  content:"";position:absolute;left:13px;right:13px;bottom:4px;height:1.5px;
  background:var(--manga);transform:scaleX(0);transform-origin:left;transition:transform 260ms ease;
}
.cs-link:hover{color:#F6EFE2;}
.cs-topo.cs-preso .cs-link:hover{color:var(--tinta);}
.cs-link.cs-ativo{color:#F6EFE2;}
.cs-topo.cs-preso .cs-link.cs-ativo{color:var(--tinta);}
.cs-link.cs-ativo::after{transform:scaleX(1);}
.cs-hamburguer{display:none;padding:10px;color:#F6EFE2;}
.cs-topo.cs-preso .cs-hamburguer{color:var(--tinta);}
.cs-hamburguer span{display:block;width:22px;height:1.6px;background:currentColor;margin:5px 0;transition:transform 260ms ease,opacity 200ms ease;}
.cs-hamburguer.cs-x span:nth-child(1){transform:translateY(6.6px) rotate(45deg);}
.cs-hamburguer.cs-x span:nth-child(2){opacity:0;}
.cs-hamburguer.cs-x span:nth-child(3){transform:translateY(-6.6px) rotate(-45deg);}
.cs-gaveta{display:none;background:var(--papel-claro);border-bottom:1px solid var(--linha);}
.cs-gaveta a{display:block;padding:15px 0;font-family:var(--mono);font-size:14px;color:var(--tinta);border-bottom:1px solid var(--linha);}
.cs-gaveta a:last-child{border-bottom:none;}

/* ---- hero ---- */
.cs-hero{background:var(--tinta);color:#F6EFE2;position:relative;overflow:hidden;padding-top:clamp(112px,15vw,148px);padding-bottom:clamp(64px,9vw,104px);}
.cs-hero-textura{position:absolute;inset:0;opacity:0.09;pointer-events:none;}
.cs-hero-grade{
  position:relative;display:grid;grid-template-columns:1.15fr 0.85fr;
  gap:clamp(32px,6vw,72px);align-items:center;
}
.cs-hero h1{
  font-family:var(--display);font-weight:800;letter-spacing:-0.035em;line-height:0.98;
  font-size:clamp(40px,6.6vw,74px);color:#FBF6EC;margin:22px 0 0;text-wrap:balance;
}
.cs-hero h1 em{font-family:var(--corpo);font-style:italic;font-weight:300;color:var(--manga);letter-spacing:-0.01em;}
.cs-hero-sub{margin-top:24px;font-size:clamp(17px,1.8vw,20px);line-height:1.6;color:rgba(246,239,226,0.78);max-width:46ch;}
.cs-hero .cs-eyebrow{color:rgba(227,154,46,0.95);}
.cs-acoes{display:flex;flex-wrap:wrap;gap:12px;margin-top:36px;}
.cs-btn{
  font-family:var(--mono);font-size:13px;letter-spacing:0.03em;padding:14px 22px;border-radius:2px;
  transition:transform 200ms ease,background 200ms ease,color 200ms ease;display:inline-block;
}
.cs-btn-cheio{background:var(--manga);color:#17293A;font-weight:500;}
.cs-btn-cheio:hover{transform:translateY(-2px);background:#F0AB43;}
.cs-btn-vazio{border:1px solid rgba(246,239,226,0.34);color:#F6EFE2;}
.cs-btn-vazio:hover{transform:translateY(-2px);background:rgba(246,239,226,0.09);}

/* retrato em arco — o elemento de assinatura */
.cs-retrato-caixa{position:relative;width:100%;max-width:340px;margin-left:auto;}
.cs-retrato-caixa::before{
  content:"";position:absolute;inset:0;transform:translate(16px,16px);
  border:1.5px solid var(--manga);border-radius:170px 170px 10px 10px;pointer-events:none;
}
.cs-retrato{
  position:relative;aspect-ratio:4/5;border-radius:170px 170px 10px 10px;overflow:hidden;
  background:var(--tinta-2);display:flex;align-items:center;justify-content:center;
}
.cs-retrato img{width:100%;height:100%;object-fit:cover;display:block;}
.cs-retrato-vazio{position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center;gap:10px;}
.cs-retrato-vazio .cs-iniciais{font-family:var(--display);font-weight:800;font-size:64px;color:rgba(246,239,226,0.28);letter-spacing:-0.04em;}
.cs-retrato-vazio .cs-dica{font-family:var(--mono);font-size:10.5px;letter-spacing:0.14em;text-transform:uppercase;color:rgba(246,239,226,0.5);}
.cs-legenda{margin-top:18px;font-family:var(--mono);font-size:11px;letter-spacing:0.1em;text-transform:uppercase;color:rgba(246,239,226,0.5);text-align:right;}

.cs-tira{
  margin-top:clamp(48px,7vw,84px);padding-top:26px;border-top:1px solid rgba(246,239,226,0.18);
  display:flex;flex-wrap:wrap;gap:10px 34px;font-family:var(--mono);font-size:12px;
  letter-spacing:0.05em;color:rgba(246,239,226,0.62);
}
.cs-tira span{display:inline-flex;align-items:center;gap:9px;}
.cs-tira span::before{content:"";width:5px;height:5px;background:var(--manga);border-radius:50%;}

/* ---- sobre ---- */
.cs-sobre-grade{display:grid;grid-template-columns:0.42fr 0.58fr;gap:clamp(28px,5vw,64px);align-items:start;}
.cs-sobre-texto p+p{margin-top:20px;}
.cs-sobre-texto p{font-size:18.5px;line-height:1.72;}
.cs-citacao{
  margin-top:40px;padding:30px 32px 30px;background:var(--papel-claro);
  border-radius:120px 120px 6px 6px;border:1px solid var(--linha);text-align:center;
}
.cs-citacao p{font-family:var(--corpo);font-style:italic;font-size:clamp(20px,2.6vw,26px);line-height:1.4;color:var(--tinta);text-wrap:balance;}
.cs-citacao .cs-eyebrow{margin-top:14px;display:block;}

/* ---- habilidades ---- */
.cs-cards{display:grid;grid-template-columns:repeat(auto-fit,minmax(288px,1fr));gap:18px;margin-top:52px;}
.cs-card{
  background:var(--papel-claro);border:1px solid var(--linha);border-radius:4px;
  padding:28px 26px 30px;transition:transform 260ms ease,box-shadow 260ms ease;
}
.cs-card:hover{transform:translateY(-3px);box-shadow:0 12px 28px rgba(16,50,75,0.09);}
.cs-card h3{font-family:var(--display);font-weight:700;font-size:21px;letter-spacing:-0.02em;color:var(--tinta);}
.cs-card-nota{font-style:italic;font-size:16px;color:var(--musgo);margin-top:8px;line-height:1.55;min-height:50px;}
.cs-chips{display:flex;flex-wrap:wrap;gap:7px;margin-top:20px;}
.cs-chip{
  font-family:var(--mono);font-size:11.5px;letter-spacing:0.02em;padding:6px 11px;
  border:1px solid var(--linha);border-radius:2px;color:var(--musgo);white-space:nowrap;
}
.cs-chip.cs-destaque{border-color:var(--manga);color:var(--tinta);background:rgba(227,154,46,0.11);}
.cs-chip small{color:var(--sage);font-size:10px;margin-left:5px;}

.cs-jeito{margin-top:clamp(56px,8vw,88px);}
.cs-jeito-lista{display:grid;grid-template-columns:repeat(auto-fit,minmax(268px,1fr));gap:2px;margin-top:34px;background:var(--linha);border:1px solid var(--linha);border-radius:4px;overflow:hidden;}
.cs-jeito-item{background:var(--papel);padding:26px 24px;}
.cs-jeito-item h4{font-family:var(--mono);font-size:11.5px;letter-spacing:0.14em;text-transform:uppercase;color:var(--sage);font-weight:500;}
.cs-jeito-item p{margin-top:12px;font-size:17px;line-height:1.6;color:var(--texto);}

/* ---- projetos: um índice, não um mural de cards ---- */
.cs-indice{margin-top:52px;border-top:1px solid var(--linha);}
.cs-obra{
  display:grid;grid-template-columns:minmax(220px,0.9fr) 1.6fr auto;gap:8px 34px;align-items:start;
  padding:30px 18px 30px 18px;border-bottom:1px solid var(--linha);
  transition:background 260ms ease,padding-left 260ms ease;
}
.cs-obra:hover{background:var(--papel-claro);padding-left:26px;}
.cs-obra-nome{font-family:var(--display);font-weight:700;font-size:21px;letter-spacing:-0.02em;color:var(--tinta);}
.cs-obra-repo{font-family:var(--mono);font-size:11px;letter-spacing:0.06em;color:var(--sage);margin-top:7px;display:block;}
.cs-obra-texto{font-size:17px;line-height:1.62;color:var(--texto);}
.cs-obra-stack{
  display:block;margin-top:12px;font-family:var(--mono);font-size:11px;letter-spacing:0.06em;
  text-transform:uppercase;color:var(--musgo);
}
.cs-tecs{display:flex;flex-wrap:wrap;align-items:center;gap:14px;margin-top:16px;}
.cs-tec{
  display:inline-flex;align-items:center;justify-content:center;height:26px;
  filter:grayscale(1);opacity:0.55;transition:filter 300ms ease,opacity 300ms ease,transform 300ms ease;
}
.cs-tec img{height:26px;width:26px;display:block;object-fit:contain;}
.cs-tec.cs-tec-largo img{width:auto;height:20px;}
.cs-obra:hover .cs-tec{filter:none;opacity:1;}
.cs-obra:hover .cs-tec:hover{transform:translateY(-3px);}
.cs-obra-seta{
  font-family:var(--mono);font-size:12px;color:var(--musgo);white-space:nowrap;padding-top:4px;
  transition:color 220ms ease,transform 260ms ease;
}
.cs-obra:hover .cs-obra-seta{color:var(--tinta);transform:translateX(5px);}
.cs-todos{
  display:inline-flex;align-items:center;gap:10px;margin-top:32px;font-family:var(--mono);
  font-size:12.5px;letter-spacing:0.04em;color:var(--tinta);
  border-bottom:1.5px solid var(--manga);padding-bottom:5px;
}
.cs-todos:hover{color:var(--sage);}

/* ---- trajetória ---- */
.cs-trilha{margin-top:52px;position:relative;}
.cs-trilha::before{content:"";position:absolute;left:0;top:6px;bottom:6px;width:1px;background:var(--linha);}
.cs-parada{position:relative;padding:0 0 34px 30px;}
.cs-parada::before{
  content:"";position:absolute;left:-4.5px;top:9px;width:10px;height:10px;border-radius:50%;
  background:var(--papel);border:1.5px solid var(--tinta);
}
.cs-parada.cs-agora::before{background:var(--manga);border-color:var(--manga);}
.cs-parada-topo{display:flex;flex-wrap:wrap;align-items:baseline;gap:6px 14px;}
.cs-parada h3{font-family:var(--display);font-weight:700;font-size:20px;letter-spacing:-0.02em;color:var(--tinta);}
.cs-parada .cs-cargo{font-size:17px;color:var(--texto);}
.cs-parada .cs-meta{font-family:var(--mono);font-size:11.5px;letter-spacing:0.08em;text-transform:uppercase;color:var(--musgo);margin-top:6px;}
.cs-parada p{margin-top:10px;color:var(--musgo);font-size:17px;max-width:62ch;}

/* ---- contato ---- */
.cs-contato{background:var(--tinta);color:#F6EFE2;position:relative;overflow:hidden;}
.cs-contato .cs-h2{color:#FBF6EC;}
.cs-contato .cs-eyebrow{color:rgba(227,154,46,0.95);}
.cs-contato-grade{position:relative;display:grid;grid-template-columns:1fr 1fr;gap:clamp(28px,5vw,64px);align-items:end;}
.cs-contato-lead{margin-top:20px;font-size:19px;color:rgba(246,239,226,0.78);max-width:40ch;line-height:1.6;}
.cs-canais{display:flex;flex-direction:column;gap:2px;}
.cs-canal{
  display:flex;align-items:baseline;justify-content:space-between;gap:20px;
  padding:18px 0;border-bottom:1px solid rgba(246,239,226,0.18);transition:padding-left 240ms ease;
}
.cs-canal:first-child{border-top:1px solid rgba(246,239,226,0.18);}
.cs-canal:hover{padding-left:10px;}
.cs-canal .cs-rot{font-family:var(--mono);font-size:11px;letter-spacing:0.14em;text-transform:uppercase;color:rgba(246,239,226,0.5);}
.cs-canal .cs-val{font-size:17px;color:#F6EFE2;word-break:break-all;text-align:right;}
.cs-rodape{
  position:relative;margin-top:clamp(48px,7vw,80px);padding-top:24px;
  border-top:1px solid rgba(246,239,226,0.18);display:flex;flex-wrap:wrap;gap:10px 20px;
  justify-content:space-between;font-family:var(--mono);font-size:11px;letter-spacing:0.08em;
  text-transform:uppercase;color:rgba(246,239,226,0.45);
}

/* ---- revelação ---- */
.cs-reveal{opacity:0;transform:translateY(22px);transition:opacity 750ms cubic-bezier(.22,.61,.36,1),transform 750ms cubic-bezier(.22,.61,.36,1);}
.cs-reveal.cs-visivel{opacity:1;transform:none;}
.cs-entra{opacity:0;transform:translateY(20px);animation:cs-sobe 850ms cubic-bezier(.22,.61,.36,1) forwards;}
@keyframes cs-sobe{to{opacity:1;transform:none;}}

/* ---- responsivo ---- */
@media (max-width:900px){
  .cs-hero-grade{grid-template-columns:1fr;gap:44px;}
  .cs-retrato-caixa{margin:0 auto;max-width:300px;}
  .cs-legenda{text-align:center;}
  .cs-sobre-grade,.cs-contato-grade{grid-template-columns:1fr;}
  .cs-obra{grid-template-columns:1fr;gap:12px;padding:26px 0;}
  .cs-obra:hover{padding-left:0;background:transparent;}
  .cs-obra-seta{padding-top:0;}
  .cs-menu{display:none;}
  .cs-hamburguer{display:block;}
  .cs-gaveta.cs-aberta{display:block;}
}
@media (prefers-reduced-motion:reduce){
  .cs-root *{animation-duration:0.01ms !important;transition-duration:0.01ms !important;}
  .cs-reveal{opacity:1;transform:none;}
  .cs-entra{opacity:1;transform:none;}
}
`;

function Reveal({ children, delay = 0, className = "" }) {
  const ref = useRef(null);
  const [visivel, setVisivel] = useState(false);
  useEffect(() => {
    const alvo = ref.current;
    if (!alvo) return;
    const obs = new IntersectionObserver(
      ([entrada]) => {
        if (entrada.isIntersecting) {
          setVisivel(true);
          obs.disconnect();
        }
      },
      { threshold: 0.12, rootMargin: "0px 0px -60px 0px" }
    );
    obs.observe(alvo);
    return () => obs.disconnect();
  }, []);
  return (
    <div
      ref={ref}
      className={`cs-reveal ${visivel ? "cs-visivel" : ""} ${className}`}
      style={{ transitionDelay: `${delay}ms` }}
    >
      {children}
    </div>
  );
}

export default function SiteCamila() {
  const [preso, setPreso] = useState(false);
  const [aberto, setAberto] = useState(false);
  const [ativo, setAtivo] = useState("inicio");

  useEffect(() => {
    const aoRolar = () => setPreso(window.scrollY > 40);
    aoRolar();
    window.addEventListener("scroll", aoRolar, { passive: true });
    return () => window.removeEventListener("scroll", aoRolar);
  }, []);

  useEffect(() => {
    const secoes = NAV.map((n) => document.getElementById(n.id)).filter(Boolean);
    const obs = new IntersectionObserver(
      (entradas) => {
        const visiveis = entradas
          .filter((e) => e.isIntersecting)
          .sort((a, b) => b.intersectionRatio - a.intersectionRatio);
        if (visiveis[0]) setAtivo(visiveis[0].target.id);
      },
      { rootMargin: "-45% 0px -45% 0px", threshold: [0, 0.25, 0.5] }
    );
    secoes.forEach((s) => obs.observe(s));
    return () => obs.disconnect();
  }, []);

  const irPara = useCallback((e, id) => {
    e.preventDefault();
    setAberto(false);
    const alvo = document.getElementById(id);
    if (!alvo) return;
    const suave = !window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    alvo.scrollIntoView({ behavior: suave ? "smooth" : "auto", block: "start" });
  }, []);

  return (
    <div className="cs-root">
      <style>{CSS}</style>

      {/* ---------------- cabeçalho ---------------- */}
      <header className={`cs-topo ${preso ? "cs-preso" : ""}`}>
        <div className="cs-limite">
          <div className="cs-topo-interno">
            <a href="#inicio" className="cs-marca" onClick={(e) => irPara(e, "inicio")}>
              <span className="cs-marca-ponto" />
              Camila Soares
            </a>

            <nav className="cs-menu" aria-label="Navegação principal">
              {NAV.map((item) => (
                <a
                  key={item.id}
                  href={`#${item.id}`}
                  onClick={(e) => irPara(e, item.id)}
                  className={`cs-link ${ativo === item.id ? "cs-ativo" : ""}`}
                  aria-current={ativo === item.id ? "true" : undefined}
                >
                  {item.rotulo}
                </a>
              ))}
            </nav>

            <button
              className={`cs-hamburguer ${aberto ? "cs-x" : ""}`}
              onClick={() => setAberto((v) => !v)}
              aria-expanded={aberto}
              aria-label={aberto ? "Fechar menu" : "Abrir menu"}
            >
              <span /><span /><span />
            </button>
          </div>
        </div>

        <div className={`cs-gaveta ${aberto ? "cs-aberta" : ""}`}>
          <div className="cs-limite">
            {NAV.map((item) => (
              <a key={item.id} href={`#${item.id}`} onClick={(e) => irPara(e, item.id)}>
                {item.rotulo}
              </a>
            ))}
          </div>
        </div>
      </header>

      {/* ---------------- hero ---------------- */}
      <section id="inicio" className="cs-faixa cs-hero">
        <div className="cs-hero-textura cs-azulejo" aria-hidden="true" />
        <div className="cs-limite">
          <div className="cs-hero-grade">
            <div>
              <p className="cs-eyebrow cs-entra" style={{ animationDelay: "80ms" }}>
                {CONTATO.cidade} · Desenvolvedora full stack há 9 anos
              </p>
              <h1 className="cs-entra" style={{ animationDelay: "180ms" }}>
                Escrevo código pensando em <em>quem vem depois</em> de mim.
              </h1>
              <p className="cs-hero-sub cs-entra" style={{ animationDelay: "300ms" }}>
                Sou a Camila. Passo os meus dias em Java e Spring Boot, tirando sistemas
                antigos do lugar sem quebrar o que já funciona — e explicando o caminho
                para quem está do lado.
              </p>
              <div className="cs-acoes cs-entra" style={{ animationDelay: "420ms" }}>
                <a
                  href="#habilidades"
                  className="cs-btn cs-btn-cheio"
                  onClick={(e) => irPara(e, "habilidades")}
                >
                  Ver minhas habilidades
                </a>
                <a
                  href="#sobre"
                  className="cs-btn cs-btn-vazio"
                  onClick={(e) => irPara(e, "sobre")}
                >
                  Me conhecer primeiro
                </a>
              </div>
            </div>

            <figure className="cs-entra" style={{ animationDelay: "260ms" }}>
              <div className="cs-retrato-caixa">
                <div className="cs-retrato">
                  {FOTO ? (
                    <img src={FOTO} alt="Retrato de Camila Soares" />
                  ) : (
                    <>
                      <div className="cs-azulejo" style={{ position: "absolute", inset: 0, opacity: 0.16 }} />
                      <div className="cs-retrato-vazio">
                        <span className="cs-iniciais">CS</span>
                        <span className="cs-dica">sua foto aqui</span>
                      </div>
                    </>
                  )}
                </div>
              </div>
              <figcaption className="cs-legenda">Backend · Recife · café preto</figcaption>
            </figure>
          </div>

          <div className="cs-tira">
            <span>Java &amp; Spring Boot</span>
            <span>Angular &amp; React</span>
            <span>Python para automação</span>
            <span>Oracle, SQL Server, MySQL</span>
            <span>Tech Lead</span>
          </div>
        </div>
      </section>

      {/* ---------------- sobre mim ---------------- */}
      <section id="sobre" className="cs-faixa cs-secao">
        <div className="cs-limite">
          <div className="cs-sobre-grade">
            <Reveal>
              <p className="cs-eyebrow">Sobre mim</p>
              <h2 className="cs-h2" style={{ marginTop: "14px" }}>
                Gente primeiro,<br />depois o sistema.
              </h2>
              <p className="cs-lead" style={{ marginTop: "22px" }}>
                Software é conversa registrada. Se o time não entende, o código não
                resolve.
              </p>
            </Reveal>

            <Reveal delay={120}>
              <div className="cs-sobre-texto">
                <p>
                  Comecei em 2017 como analista de desenvolvimento Java e nunca saí do
                  backend por muito tempo. O que mudou foi o tamanho da conversa: hoje boa
                  parte do meu trabalho é decidir junto com o time como um sistema vai
                  envelhecer.
                </p>
                <p>
                  Já peguei muito legado — Struts, JSF, Oracle Forms — e ajudei a
                  transformar em microsserviços que o time consegue manter. Também já fui a
                  pessoa que ficou acordada quando a coisa quebrou às duas da manhã. As
                  duas experiências me ensinaram a mesma coisa: clareza vale mais que
                  esperteza.
                </p>
                <p>
                  Trabalho melhor perto de quem usa o sistema. Gosto de fazer a pergunta
                  chata na reunião, de desenhar o fluxo no papel antes de abrir a IDE e de
                  deixar o próximo commit fácil para quem vier.
                </p>
                <p>
                  Fora do Java, ando aprendendo Kotlin, uso Python para automatizar o que
                  me irrita e olho Flutter com carinho. Sou de Recife, e é daqui que
                  trabalho.
                </p>

                <div className="cs-citacao">
                  <p>“Se eu preciso explicar o código duas vezes, o problema é o código.”</p>
                  <span className="cs-eyebrow">minha regra de ouro</span>
                </div>
              </div>
            </Reveal>
          </div>
        </div>
      </section>

      {/* ---------------- habilidades ---------------- */}
      <section id="habilidades" className="cs-faixa cs-secao" style={{ background: "var(--papel)" }}>
        <div className="cs-limite">
          <Reveal>
            <p className="cs-eyebrow">Minhas habilidades</p>
            <h2 className="cs-h2" style={{ marginTop: "14px" }}>
              O que eu levo<br />para o time.
            </h2>
            <p className="cs-lead" style={{ marginTop: "22px" }}>
              Agrupei por como cada coisa aparece no meu dia, não por nível. Em destaque, o
              que eu uso todo dia.
            </p>
          </Reveal>

          <div className="cs-cards">
            {HABILIDADES.map((g, i) => (
              <Reveal key={g.grupo} delay={i * 70}>
                <article className="cs-card">
                  <h3>{g.grupo}</h3>
                  <p className="cs-card-nota">{g.nota}</p>
                  <ul className="cs-chips">
                    {g.itens.map((item) => (
                      <li
                        key={item.nome}
                        className={`cs-chip ${item.destaque ? "cs-destaque" : ""}`}
                      >
                        {item.nome}
                        {item.nota && <small>{item.nota}</small>}
                      </li>
                    ))}
                  </ul>
                </article>
              </Reveal>
            ))}
          </div>

          <div className="cs-jeito">
            <Reveal>
              <p className="cs-eyebrow">E o que não cabe em chip</p>
              <h2 className="cs-h2" style={{ fontSize: "clamp(28px,3.6vw,40px)", marginTop: "12px" }}>
                O jeito de trabalhar
              </h2>
            </Reveal>
            <Reveal delay={100}>
              <ul className="cs-jeito-lista">
                {JEITO.map((j) => (
                  <li key={j.titulo} className="cs-jeito-item">
                    <h4>{j.titulo}</h4>
                    <p>{j.texto}</p>
                  </li>
                ))}
              </ul>
            </Reveal>
          </div>
        </div>
      </section>

      {/* ---------------- projetos ---------------- */}
      <section id="projetos" className="cs-faixa cs-secao">
        <div className="cs-limite">
          <Reveal>
            <p className="cs-eyebrow">Projetos</p>
            <h2 className="cs-h2" style={{ marginTop: "14px" }}>
              Código que eu<br />escrevo por vontade.
            </h2>
            <p className="cs-lead" style={{ marginTop: "22px" }}>
              Sete dos que eu abriria numa entrevista. Todos rodam, todos têm um problema
              real por trás.
            </p>
          </Reveal>

          <div className="cs-indice">
            {PROJETOS.map((p, i) => (
              <Reveal key={p.nome} delay={Math.min(i * 60, 240)}>
                <a
                  className="cs-obra"
                  href={p.repo}
                  target="_blank"
                  rel="noreferrer"
                  aria-label={`${p.titulo} — abrir no GitHub`}
                >
                  <div>
                    <h3 className="cs-obra-nome">{p.titulo}</h3>
                    <span className="cs-obra-repo">{p.nome}</span>
                  </div>
                  <div>
                    <p className="cs-obra-texto">{p.texto}</p>
                    <span className="cs-obra-stack">{p.stack}</span>
                    <div className="cs-tecs">
                      {p.tecs.map((chave) => {
                        const t = TEC[chave];
                        if (!t) return null;
                        return (
                          <span
                            key={chave}
                            className={`cs-tec ${t.largo ? "cs-tec-largo" : ""}`}
                            title={t.nome}
                          >
                            <img src={t.src} alt={t.nome} loading="lazy" />
                          </span>
                        );
                      })}
                    </div>
                  </div>
                  <span className="cs-obra-seta" aria-hidden="true">
                    ver no GitHub →
                  </span>
                </a>
              </Reveal>
            ))}
          </div>

          <Reveal>
            <a
              className="cs-todos"
              href={`https://${CONTATO.github}?tab=repositories`}
              target="_blank"
              rel="noreferrer"
            >
              Ver os 32 repositórios no GitHub →
            </a>
          </Reveal>
        </div>
      </section>

      {/* ---------------- trajetória ---------------- */}
      <section id="trajetoria" className="cs-faixa cs-secao">
        <div className="cs-limite">
          <Reveal>
            <p className="cs-eyebrow">Trajetória</p>
            <h2 className="cs-h2" style={{ marginTop: "14px" }}>
              Nove anos,<br />uma linguagem de base.
            </h2>
          </Reveal>

          <div className="cs-trilha">
            {TRAJETORIA.map((t, i) => (
              <Reveal key={t.empresa + t.periodo} delay={Math.min(i * 50, 250)}>
                <div className={`cs-parada ${i === 0 ? "cs-agora" : ""}`}>
                  <div className="cs-parada-topo">
                    <h3>{t.empresa}</h3>
                    <span className="cs-cargo">{t.cargo}</span>
                  </div>
                  <div className="cs-meta">
                    {t.periodo}
                    {t.local && t.local !== "—" ? ` · ${t.local}` : ""}
                  </div>
                  {t.texto && <p>{t.texto}</p>}
                </div>
              </Reveal>
            ))}
          </div>
        </div>
      </section>

      {/* ---------------- contato ---------------- */}
      <section id="contato" className="cs-faixa cs-secao cs-contato">
        <div className="cs-hero-textura cs-azulejo" aria-hidden="true" style={{ opacity: 0.07 }} />
        <div className="cs-limite">
          <div className="cs-contato-grade">
            <Reveal>
              <div>
                <p className="cs-eyebrow">Contato</p>
                <h2 className="cs-h2" style={{ marginTop: "14px" }}>
                  Vamos conversar?
                </h2>
                <p className="cs-contato-lead">
                  Respondo e-mail no mesmo dia. Se for sobre um sistema legado que ninguém
                  quer tocar, respondo mais rápido ainda.
                </p>
              </div>
            </Reveal>

            <Reveal delay={120}>
              <div className="cs-canais">
                <a className="cs-canal" href={`mailto:${CONTATO.email}`}>
                  <span className="cs-rot">E-mail</span>
                  <span className="cs-val">{CONTATO.email}</span>
                </a>
                <a
                  className="cs-canal"
                  href={`https://${CONTATO.linkedin}`}
                  target="_blank"
                  rel="noreferrer"
                >
                  <span className="cs-rot">LinkedIn</span>
                  <span className="cs-val">{CONTATO.linkedin}</span>
                </a>
                <a
                  className="cs-canal"
                  href={`https://${CONTATO.github}`}
                  target="_blank"
                  rel="noreferrer"
                >
                  <span className="cs-rot">GitHub</span>
                  <span className="cs-val">{CONTATO.github}</span>
                </a>
              </div>
            </Reveal>
          </div>

          <div className="cs-rodape">
            <span>Camila Soares © {new Date().getFullYear()}</span>
            <span>Feito em React · Recife, PE</span>
          </div>
        </div>
      </section>
    </div>
  );
}
