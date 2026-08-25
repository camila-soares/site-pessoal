import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// Se for publicar em GitHub Pages num repositório que NÃO é <usuario>.github.io,
// troque "base" abaixo para "/nome-do-repositorio/". Para Vercel e Netlify, deixe "/".
export default defineConfig({
  plugins: [react()],
  base: "/",
});
