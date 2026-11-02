import java.math.BigDecimal;

public class ItemPedido {
    private String produto;
    private int quantidade;
    private BigDecimal precoUnitario;

    public ItemPedido(String produto, int quantidade, BigDecimal precoUnitario){
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public ItemPedido(ItemPedido origem){
        this.produto = origem.produto;
        this.quantidade = origem.quantidade;
        this.precoUnitario = origem.precoUnitario;
    }

    public BigDecimal subtotal(){
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }
}