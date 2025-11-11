public class Main {
    public static void main(String[] args) {
        Produto notebook = new Produto(1, "Notebook Pro", 3500.00);
        Produto mouse = new Produto(2, "Mouse Wireless", 150.00);

        Estoque.adicionarEstoque(notebook, 5);
        Estoque.adicionarEstoque(mouse, 10);

        Cliente cliente1 = new Cliente(101, "João", "01/01/1990", "joao@email.com", "12345");
        cliente1.addProduto(notebook);
        cliente1.addProduto(mouse);
        cliente1.addProduto(notebook);

        //Estoque.removerEstoque(notebook, 4);
        //cliente1.verCarrinho();

        //System.out.println(Compra.calcularTotal(cliente1.getCarrinho()));

        Compra.finalizarCompra(cliente1.getCarrinho());


        System.out.println(Estoque.getEstoque(notebook));
    }
}