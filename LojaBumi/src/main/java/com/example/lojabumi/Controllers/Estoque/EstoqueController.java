package com.example.lojabumi.Controllers.Estoque;

import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class EstoqueController {

    public Button btnRemover;
    public Button btnRemoverProduto;
    @FXML
    private Button Voltar;
    @FXML
    private TableView<Produto> tableEstoque;
    @FXML
    private TableColumn<Produto, Integer> coliD;
    @FXML
    private TableColumn<Produto, String> colNome;
    @FXML
    private TableColumn<Produto, String> colPreco;
    @FXML
    private TableColumn<Produto, Integer> colQuantidade;
    @FXML
    private TableColumn<Produto, String> colTipo;
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnAdicionar;


    @FXML
    public void initialize() {
        tableEstoque.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        coliD.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject()
        );
        colNome.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("R$ %.2f", cellData.getValue().getPreco()))
        );
        colQuantidade.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(Estoque.getEstoque(cellData.getValue())).asObject()
        );
        colTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClass().getSimpleName())
        );

        atualizarTabela();

        Voltar.setOnAction(e -> mudarTela(Voltar, "/view/Produtos.fxml"));

        btnAdicionar.setOnAction(e -> mudarTela(btnAdicionar, "/view/AddProduto.fxml"));

        btnAtualizar.setOnAction(e -> mudarTela(btnAtualizar, "/view/AtualizarEstoque.fxml"));

        btnRemover.setOnAction(e -> mudarTela(btnRemover, "/view/RemoverEstoque.fxml"));

        btnRemoverProduto.setOnAction(e -> mudarTela(btnRemoverProduto, "/view/RemoverProduto.fxml"));

    }
    private void atualizarTabela() {
        tableEstoque.getItems().setAll(Estoque.getProdutos().values());
    }


}
