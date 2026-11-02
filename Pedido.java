import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Prototype<Pedido>, Serializable {
    private static final long serialVersionUID = 1L;

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

    public Pedido clonarPorSerializacao(){
        try{
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bytes)){
                out.writeObject(this);
            }
            try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))){
                Pedido copia = (Pedido) in.readObject();
                copia.id = proximoId++;
                copia.criadoEm = LocalDate.now();
                return copia;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Falha ao copiar o pedido por serialização", e);
        }
    }

    public long getId() { return id; }
    public LocalDate getCriadoEm() { return criadoEm; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String endereco) { this.enderecoEntrega = endereco; }
    public List<ItemPedido> getItens(){ return itens; }
}