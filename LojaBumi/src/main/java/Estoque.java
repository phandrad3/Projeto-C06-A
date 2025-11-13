import java.util.HashMap;
import java.util.Map;

public class Estoque {
    private static Map<Integer, Integer> estoque = new HashMap<>();
    private static Map<Integer, Produto> produtos = new HashMap<>();

    public static void adicionarEstoque(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) {
            System.err.println("Erro: Produto inválido ou quantidade não positiva.");
            return;
        }

        int idProduto = produto.getId();
        estoque.put(idProduto, estoque.getOrDefault(idProduto, 0) + quantidade);
        produtos.put(idProduto, produto);

        System.out.println("Estoque atualizado: " + quantidade + " unidade(s) do produto '" +
                produto.getNome() + "' (ID: " + idProduto + ") adicionadas.");
    }

    public static int getEstoque(Produto produto) {
        if (produto == null) return 0;
        return estoque.getOrDefault(produto.getId(), 0);
    }

    public static boolean removerEstoque(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) return false;

        int idProduto = produto.getId();
        int estoqueAtual = getEstoque(produto);

        if (estoqueAtual >= quantidade) {
            estoque.put(idProduto, estoqueAtual - quantidade);
            return true;
        }
        return false;
    }

    public static void exibirEstoque() {
        System.out.println("\n--- ESTOQUE COMPLETO ---");
        if (estoque.isEmpty()) {
            System.out.println("O estoque está vazio.");
        } else {
            System.out.println("ID | Nome | Preço | Quantidade");
            System.out.println("---------------------------------");
            for (Map.Entry<Integer, Integer> entry : estoque.entrySet()) {
                int idProduto = entry.getKey();
                int quantidade = entry.getValue();
                Produto produto = produtos.get(idProduto);

                if (produto != null) {
                    System.out.println(idProduto + " | " + produto.getNome() + " | R$ " +
                            String.format("%.2f", produto.getPreco()) + " | " + quantidade);
                }
            }
        }
        System.out.println("------------------------\n");
    }
}