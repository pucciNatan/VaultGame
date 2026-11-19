# VaultGame Web

Storefront Next.js da VaultGame, com visual dark gamer e uma camada BFF que integra catálogo, customer, order e inventory sem expor JWT ou portas internas ao navegador.

No browser, o acesso ao BFF usa uma instância central de **Axios**. O estado
remoto, cache, loading, mutations e invalidações são gerenciados pelo
**TanStack Query**. Os Route Handlers mantêm `fetch` server-side para conversar
com os microsserviços, aproveitando as APIs nativas de `Request`/`Response` do
Next.js.

## Executar

```bash
copy .env.example .env.local
npm install
npm run dev
```

Variáveis server-only: `CATALOG_API_URL`, `CUSTOMER_API_URL`, `ORDER_API_URL` e `INVENTORY_API_URL`.

O JWT fica em cookie `httpOnly`; o carrinho visitante usa um UUID em cookie e é mesclado após o login.

## Qualidade

```bash
npm run lint
npm test
npm run build
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

You can start editing the page by modifying `app/page.tsx`. The page auto-updates as you edit the file.

This project uses [`next/font`](https://nextjs.org/docs/app/building-your-application/optimizing/fonts) to automatically optimize and load [Geist](https://vercel.com/font), a new font family for Vercel.

## Learn More

To learn more about Next.js, take a look at the following resources:

- [Next.js Documentation](https://nextjs.org/docs) - learn about Next.js features and API.
- [Learn Next.js](https://nextjs.org/learn) - an interactive Next.js tutorial.

You can check out [the Next.js GitHub repository](https://github.com/vercel/next.js) - your feedback and contributions are welcome!

## Deploy on Vercel

The easiest way to deploy your Next.js app is to use the [Vercel Platform](https://vercel.com/new?utm_medium=default-template&filter=next.js&utm_source=create-next-app&utm_campaign=create-next-app-readme) from the creators of Next.js.

Check out our [Next.js deployment documentation](https://nextjs.org/docs/app/building-your-application/deploying) for more details.
