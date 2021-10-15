package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.text.DecimalFormat;

public class Controller {
    @FXML private Label bartLaranjaCamisa;
    @FXML private Label bartAzulCalcao;
    @FXML private Label bartAzulSapato;
    @FXML private Label homerAzulCalca;
    @FXML private Label homerMarromBoca;
    @FXML private Label homerPretoSapato;
    @FXML private ImageView imageView;
    @FXML private Label naiveBayesBart;
    @FXML private Label naiveBayesHomer;

    double[] caracteristicasImgSel = {0,0,0,0,0,0};
    DecimalFormat df = new DecimalFormat("##0.0000");

    @FXML
    public void extrair() {
        ExtratorCaracteristicas.extrair(false);
    }

    //MÉTODO QUE CHAMA O ALGORITMO
    @FXML
    public void classificar() {
        //*********Naive Bayes
        double[] nb = AprendizagemBayesiana.naiveBayes(caracteristicasImgSel);
        naiveBayesBart.setText("Limao: "+df.format(nb[0])+"%");
        naiveBayesHomer.setText("Laranja: "+df.format(nb[1])+"%");
    }

    //gui para carregar imagem na tela (e extrair caracteristicas dela)*********************************
    @FXML
    public void selecionaImagem() {
        File f = buscaImg();
        if(f != null) {
            Image img = new Image(f.toURI().toString());
            imageView.setImage(img);
            imageView.setFitWidth(img.getWidth());
            imageView.setFitHeight(img.getHeight());
            caracteristicasImgSel = ExtratorCaracteristicas.extraiCaracteristicas(f, false);
            bartLaranjaCamisa.setText("Laranja Laranja: "+df.format(caracteristicasImgSel[0]));
            bartAzulCalcao.setText("amareloLimao: "   +df.format(caracteristicasImgSel[1]));
            bartAzulSapato.setText("verdeLaranja: "   +df.format(caracteristicasImgSel[2]));
            homerAzulCalca.setText("verdeLimao: "    +df.format(caracteristicasImgSel[3]));
            homerMarromBoca.setText("brancoLaranja: "   +df.format(caracteristicasImgSel[4]));
            homerPretoSapato.setText("brancoLimao: "  +df.format(caracteristicasImgSel[5]));
        }
    }

    private File buscaImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new
                FileChooser.ExtensionFilter(
                "Imagens", "*.jpg", "*.JPG",
                "*.png", "*.PNG", "*.gif", "*.GIF",
                "*.bmp", "*.BMP"));
        fileChooser.setInitialDirectory(new File("src/imagens"));
        File imgSelec = fileChooser.showOpenDialog(null);
        try {
            if (imgSelec != null) {
                return imgSelec;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
