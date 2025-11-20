package com.example.lojabumi.Controllers.Produtos;

import com.example.lojabumi.UserDatabase;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.usuario.tipoConta.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.Map;
import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class ProdutosController {

    @FXML
    private Button sair;

    @FXML
    private Button estoque;

    @FXML
    private Button btncarrinho;

    @FXML
    private GridPane gridProdutos;



    private void carregarProdutos() {

        Map<Integer, Produto> produtos = Estoque.getProdutos();

        gridProdutos.getChildren().clear();

        int col = 0, row = 0;
        for (Map.Entry<Integer, Produto> entry : produtos.entrySet()) {
            int id = entry.getKey();
            Produto p = entry.getValue();
            int quantidade = Estoque.getQuantidade(id);

            VBox box = new VBox(10);
            box.setStyle("-fx-alignment: center;");

            Label nomeLabel = new Label(p.getNome());
            nomeLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");

            Label precoLabel = new Label("R$ " + String.format("%.2f", p.getPreco()));
            precoLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: lightgreen;");

            Label qtdLabel = new Label("Qtd: " + quantidade);
            qtdLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #007944; -fx-background-color: lightgreen; -fx-padding: 4 8 4 8;");

            Button comprar = new Button("Adicionar ao Carrinho");
            comprar.setStyle("-fx-background-color: #334155; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 6;");
            comprar.setOnAction(e -> adicionarAoCarrinho(id));

            box.getChildren().addAll(nomeLabel, precoLabel, qtdLabel, comprar);

            gridProdutos.add(box, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
    }

    private void adicionarAoCarrinho(int idProduto) {
        Produto produto = Estoque.getProdutos().get(idProduto);
        Usuario usuarioLogado = UserDatabase.getUsuarioLogado();
        if (produto == null) {
            System.out.println("Erro: produto não encontrado!");
            return;
        }

        if (usuarioLogado instanceof Cliente) {
            Cliente clienteLogado = (Cliente) usuarioLogado;
            clienteLogado.addProduto(produto);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Apenas clientes podem adicionar produtos ao carrinho!");
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        sair.setOnAction(e -> {
                    mudarTela(sair, "/view/Cadastro.fxml");
                }
        );
        btncarrinho.setOnAction(e -> {
                    mudarTela(btncarrinho, "/view/Carrinho.fxml");
                }
        );
        carregarProdutos();
    }

    public void entrarEstoque() {

        Usuario usuario = UserDatabase.getUsuarioLogado();

        if (!usuario.alterarEstoque()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Acesso Negado");
            alert.setHeaderText(null);
            alert.setContentText("Você não tem permissão para acessar o estoque!");
            alert.showAndWait();
            return;
        }
        System.out.println("vc podeee hahaha");
        mudarTela(estoque, "/view/Estoque.fxml");
    }


}
