import type { Metadata } from "next";
import "./globals.css";
import { Header } from "@/components/header";
import { QueryProvider } from "@/components/query-provider";
import { StoreProvider } from "@/components/store-provider";

export const metadata: Metadata = {
  title: { default: "VaultGame", template: "%s | VaultGame" },
  description: "Sua próxima aventura começa aqui.",
};

export default function RootLayout({ children }: LayoutProps<"/">) {
  return (
    <html lang="pt-BR">
      <body>
        <QueryProvider>
          <StoreProvider>
            <Header />
            <main>{children}</main>
            <footer className="footer">
              <div className="container footer-grid">
                <div><b className="brand"><span>V</span>VaultGame</b><p>Jogos, hardware e diversão em um só lugar.</p></div>
                <div><b>Atendimento</b><p>Seg–Sex, 9h às 18h</p><p>suporte@vaultgame.local</p></div>
                <div><b>Ambiente de desenvolvimento</b><p>Pagamentos e e-mails são simulados.</p></div>
              </div>
            </footer>
          </StoreProvider>
        </QueryProvider>
      </body>
    </html>
  );
}
