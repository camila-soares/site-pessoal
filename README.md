# Site pessoal — Camila Soares

Site em React + Vite, feito com a lista de habilidades do currículo e os projetos
do GitHub (github.com/camila-soares).

## Rodar localmente

```bash
npm install
npm run dev
```

Abre em `http://localhost:5173`.

## Gerar build de produção

```bash
npm run build
```

Gera a pasta `dist/` — é isso que qualquer serviço de hospedagem estática publica.

## Antes de publicar

- Troque `const FOTO = ""` em `src/App.jsx` pela URL da sua foto.
- Revise os textos de "Sobre mim".
- Se for hospedar num domínio do tipo `usuario.github.io/nome-do-repo`, ajuste
  `base` em `vite.config.js` para `"/nome-do-repo/"`.
