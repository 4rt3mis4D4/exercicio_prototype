import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Supplier;

public class PedidosDemo {
    private static final int COPIAS = 100_000;

    public static void main(String[] args) {
        
        CatalogoPedidos catalogo = new CatalogoPedidos();
        Pedido modelo = new Pedido("(modelo)", "-");
        modelo.adicionarItem(new ItemPedido("Café 500g", 10, new BigDecimal("18.90")));
        modelo.adicionarItem(new ItemPedido("Açúcar 1kg", 20, new BigDecimal("4.50")));
        modelo.adicionarItem(new ItemPedido("Filtro de papel", 5, new BigDecimal("7.20")));
        catalogo.registrar("reposicao-mensal", modelo);
 
        // Pedido A
        Pedido a = catalogo.criar("reposicao-mensal");
        a.setCliente("Mercado Central");
        a.setEnderecoEntrega("Rua das Flores, 120");
 
        // Pedido B
        Pedido b = catalogo.criar("reposicao-mensal");
        b.setCliente("Padaria Boa Massa");
        b.setEnderecoEntrega("Av. Paraná, 800");
        b.getItens().get(0).setQuantidade(20);
 
        // Pedido C
        Pedido c = catalogo.criar("reposicao-mensal");
        c.setCliente("Restaurante do Porto");
        c.setEnderecoEntrega("Rua do Cais, 45");
        c.adicionarItem(new ItemPedido("Adoçante 100ml", 12, new BigDecimal("6.00")));
 
        imprimir(modelo);
        imprimir(a);
        imprimir(b);
        imprimir(c);
        System.out.println();
 
        // Critérios de aceitação
        boolean ca01 = modelo.getId() != a.getId() && modelo.getId() != b.getId()
                && modelo.getId() != c.getId() && a.getId() != b.getId()
                && a.getId() != c.getId() && b.getId() != c.getId();
        verificar("CA-01 ids diferentes", ca01);
 
        verificar("CA-02 total do modelo continua 315,00",
                modelo.valorTotal().compareTo(new BigDecimal("315.00")) == 0);
 
        verificar("CA-03 totais A = 315,00 | B = 504,00 | C = 387,00",
                a.valorTotal().compareTo(new BigDecimal("315.00")) == 0
                && b.valorTotal().compareTo(new BigDecimal("504.00")) == 0
                && c.valorTotal().compareTo(new BigDecimal("387.00")) == 0);
 
        verificar("CA-04 modelo continua com 3 itens", modelo.getItens().size() == 3);
 
        boolean ca05 = true;
        for (Pedido copia : new Pedido[]{a, b, c}) {
            for (ItemPedido itemCopia : copia.getItens()) {
                for (ItemPedido itemModelo : modelo.getItens()) {
                    if (itemCopia == itemModelo) {
                        ca05 = false;
                    }
                }
            }
        }
        verificar("CA-05 nenhum item das cópias é do modelo", ca05);
 
        boolean ca06 = false;
        try {
            catalogo.criar("inexistente");
        } catch (IllegalArgumentException e) {
            ca06 = true;
        }
        verificar("CA-06 criar(\"inexistente\") lança IllegalArgumentException", ca06);
 
        verificar("CA-07 cliente do modelo continua \"(modelo)\"",
                modelo.getCliente().equals("(modelo)"));
 
        
        LocalDate hoje = LocalDate.now();
        verificar("RF07 cópias criadas com a data de hoje (" + hoje + ")",
                a.getCriadoEm().equals(hoje)
                && b.getCriadoEm().equals(hoje)
                && c.getCriadoEm().equals(hoje));

        // RF12
        Pedido s = modelo.clonarPorSerializacao();
        s.setCliente("Teste");
        s.getItens().get(0).setQuantidade(99);
        verificar("RF12 cópia por serialização é profunda, com novo id e data de hoje",
                s.getId() != modelo.getId()
                && s.getItens().get(0) != modelo.getItens().get(0)
                && s.getCriadoEm().equals(hoje)
                && modelo.getCliente().equals("(modelo)")
                && modelo.valorTotal().compareTo(new BigDecimal("315.00")) == 0);

        // RF12
        System.out.println();
        System.out.println("Tempo de " + COPIAS + " cópias:");
        System.out.println("clonar(): " + medir(modelo::clonar) + " ms");
        System.out.println("clonarPorSerializacao(): " + medir(modelo::clonarPorSerializacao) + " ms");
    }

    private static long medir(Supplier<Pedido> copia) {
        long inicio = System.nanoTime();
        for (int i = 0; i < COPIAS; i++) {
            copia.get();
        }
        return (System.nanoTime() - inicio) / 1_000_000;
    }
 
    private static void imprimir(Pedido p) {
        System.out.println("Pedido #" + p.getId() + " │ " + p.getCliente()
                + " │ " + p.getEnderecoEntrega()
                + " │ itens = " + p.getItens().size()
                + " │ total = R$ " + p.valorTotal().toString().replace('.', ','));
    }
 
    private static void verificar(String descricao, boolean ok) {
        System.out.println(descricao + ": " + (ok ? "OK" : "FALHA"));
    }
}