package com.example.lojabumi.Controllers.Carrinho;

import com.example.lojabumi.Controllers.MudarTela;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.usuario.tipoConta.Cliente;
import com.example.lojabumi.utilitarios.Compra;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;

public class CarrinhoController {

    @FXML
    private TableView<Produto> tableCarrinho;

    @FXML
    private TableColumn<Produto, String> colNome;
    @FXML
    private TableColumn<Produto, Double> colPreco;
    @FXML
    private TableColumn<Produto, Integer> colQuantidade;
    @FXML
    private TableColumn<Produto, String> colTotalProduto;
    @FXML
    private TableColumn<Produto, String> colDesconto;
    @FXML
    private Label labelTotal;
    @FXML
    private Button btnVoltarProdutos;
    @FXML
    private Button btnFinalizarCompra;
    @FXML
    private Button btnLimparCarrinho;

    private Cliente clienteLogado;

    // tipo de lista na qual, notifica qualquer mudança na lista, por isso foi associada ao tablecarrinho
    private ObservableList<Produto> carrinhoObservable = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Usuario usuario = Usuario.getUsuarioLogado();
        if (usuario instanceof Cliente) {
            clienteLogado = (Cliente) usuario;
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Acesso negado");
            alert.setHeaderText(null);
            alert.setContentText("Apenas clientes podem acessar o carrinho!");
            alert.showAndWait();
        }
        tableCarrinho.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        colQuantidade.setCellValueFactory(cellData -> {
            Produto p = cellData.getValue();
            int qtd = clienteLogado.getCarrinho().getOrDefault(p, 0);
            return new SimpleIntegerProperty(qtd).asObject();
        });

        colTotalProduto.setCellValueFactory(cellData -> {
            Produto p = cellData.getValue();
            int qtd = clienteLogado.getCarrinho().getOrDefault(p, 0);
            double total = p.getPreco() * qtd;
            return new SimpleStringProperty(String.format("R$ %.2f", total));
        });

        colDesconto.setCellValueFactory(new PropertyValueFactory<>("Desconto"));

        tableCarrinho.setItems(carrinhoObservable);

        btnVoltarProdutos.setOnAction(e -> MudarTela.mudarTela(btnVoltarProdutos, "/view/Produtos.fxml"));

        btnFinalizarCompra.setOnAction(e -> finalizarCarrinho());

        btnLimparCarrinho.setOnAction(e -> {
            clienteLogado.getCarrinho().clear();
            atualizarCarrinho();
        });

        atualizarCarrinho();
    }

    public void atualizarCarrinho() {
        Map<Produto, Integer> carrinhoMap = clienteLogado.getCarrinho();
        carrinhoObservable.setAll(carrinhoMap.keySet());
        atualizarTotal();
    }

    private void atualizarTotal() {
        double total = Compra.calcularTotal(clienteLogado.getCarrinho());
        labelTotal.setText("Total: R$ " + String.format("%.2f", total));
    }

    private void finalizarCarrinho() {
        if (clienteLogado.getCarrinho().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Carrinho vazio");
            alert.setHeaderText(null);
            alert.setContentText("Seu carrinho está vazio!");
            alert.showAndWait();
            return;
        }

        boolean sucesso = Compra.finalizarCompra(clienteLogado.getCarrinho());

        if (sucesso) {
            clienteLogado.getCarrinho().clear();
            atualizarCarrinho();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Compra finalizada");
            alert.setHeaderText(null);
            alert.setContentText("Compra realizada com sucesso!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro na compra");
            alert.setHeaderText(null);
            alert.setContentText("Não foi possível finalizar a compra. Verifique a quantidade de produtos no estoque.");
            alert.showAndWait();
        }
    }
}
