import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Prototype<Pedido> {
    private static long proximoId = 1;

    private long id;
    private String cliente;
    private String enderecoEntrega;
    private List<ItemPedido> itens = new ArrayList<>();
    private LocalDate criadoEm;

    public Pedido(String cliente, String enderecoEntrega){
        this.id = proximoId++;
        this.cliente = cliente;
        this.enderecoEntrega = enderecoEntrega;
        this.criadoEm = LocalDate.now();
    }

    public void adicionarItem(ItemPedido item){
        itens.add(item);
    }

    public BigDecimal valorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for(ItemPedido item : itens){
            total = total.add(item.subtotal());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    // Cópia profunda
    @Override 
    public Pedido clonar(){
        Pedido copia = new Pedido(this.cliente, this.enderecoEntrega);
        for(ItemPedido item : this.itens){
            copia.itens.add(new ItemPedido(item));
        }
        return copia;
    }

    public long getId() { return id; }
    public LocalDate getCriadoEm() { return criadoEm; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String endereco) { this.enderecoEntrega = endereco; }
    public List<ItemPedido> getItens(){ return itens; }
}