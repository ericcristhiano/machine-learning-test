package sample;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;


public class ExtratorCaracteristicas {
    public static double[] extraiCaracteristicas(File f, boolean exibeImagem) {

        double[] caracteristicas = new double[7];

    	double laranjaLaranja = 0;
        double amareloLimao = 0;
        double verdeLaranja = 0;
        double verdeLimao = 0;
    	
        Image img = new Image(f.toURI().toString());
        PixelReader pr = img.getPixelReader();

        Mat imagemOriginal = Imgcodecs.imread(f.getPath());
        Mat imagemProcessada = imagemOriginal.clone();

        int w = (int)img.getWidth();
        int h = (int)img.getHeight();

        for(int i=0; i<h; i++) {
            for(int j=0; j<w; j++) {

                Color cor = pr.getColor(j,i);

                double r = cor.getRed()*255;
                double g = cor.getGreen()*255;
                double b = cor.getBlue()*255;

                float[] hsb = java.awt.Color.RGBtoHSB((int) r, (int) g, (int) b, null);
                
                float hsbh = hsb[0] * 360;
            	float hsbb = hsb[2] * 100;
            	
                double[] processadoCorLaranja = new double[]{0, 255, 128};
                double[] processadoCorLimao = new double[]{0, 255, 255};
                
            	if (isLaranjaLaranja(hsbh, hsbb)) {
            		laranjaLaranja++;
            		imagemProcessada.put(i, j, processadoCorLaranja);
            	}
            	
            	if (isAmareloLimao(hsbh, hsbb)) {
            		amareloLimao++;
            		imagemProcessada.put(i, j, processadoCorLimao);
            	}
            	
            	if (isVerdeLaranja(hsbh, hsbb)) {
            		verdeLaranja++;
            		imagemProcessada.put(i, j, processadoCorLaranja);
            	}
            	
    			if (isVerdeLimao(hsbh, hsbb)) {
    				verdeLimao++;
    				imagemProcessada.put(i, j, processadoCorLimao);
    			}
            }
        }

        // Normaliza as caracterÃ­sticas pelo nÃºmero de pixels totais da imagem para %
        laranjaLaranja = (laranjaLaranja / (w * h)) * 100;
        amareloLimao = (amareloLimao / (w * h)) * 100;
        verdeLaranja = (verdeLaranja / (w * h)) * 100;
        verdeLimao = (verdeLimao / (w * h)) * 100;

        caracteristicas[0] = laranjaLaranja;
        caracteristicas[1] = amareloLimao;
        caracteristicas[2] = verdeLaranja;
        caracteristicas[3] = verdeLimao;
        //APRENDIZADO SUPERVISIONADO - JÃ� SABE QUAL A CLASSE NAS IMAGENS DE TREINAMENTO
        caracteristicas[6] = f.getName().charAt(0)=='l'?0:1;

        if(exibeImagem) {
            Imshow im = new Imshow("Title");
            im.showImage(imagemOriginal);
            HighGui.imshow("Imagem original", imagemOriginal);
            HighGui.imshow("Imagem processada", imagemProcessada);
            HighGui.waitKey(0);
        }

        return caracteristicas;
    }

    public static boolean isLaranjaLaranja(float h, float b) {
        return h < 26 && h > 15 && b > 50; 
    }

    public static boolean isAmareloLimao(float h, float b) {
        return h < 75 && h > 37 && b > 50;
    }
    
    public static boolean isVerdeLimao(float h, float b) {
    	return h > 65 && h < 88 && b > 55 && b < 70;
    }
    
    public static boolean isVerdeLaranja(float h, float b) {
    	return h > 65 && h < 118 && b < 50 && b > 28;
    }
    
    public static void extrair(boolean exibeImagem) {

        // Cabeçalho do arquivo Weka
        String exportacao = "@relation caracteristicas\n\n";
        exportacao += "@attribute laranjaLaranja real\n";
        exportacao += "@attribute amareloLimao real\n";
        exportacao += "@attribute verdeLaranja real\n";
        exportacao += "@attribute verdeLimao real\n";
        exportacao += "@attribute classe {Limao, Laranja}\n\n";
        exportacao += "@data\n";

        // DiretÃ³rio onde estÃ£o armazenadas as imagens
        File diretorio = new File("src/imagens");
        File[] arquivos = diretorio.listFiles();

        System.out.println(arquivos.length);

        // DefiniÃ§Ã£o do vetor de caracterÃ­sticas
        double[][] caracteristicas = new double[293][7];

        // Percorre todas as imagens do diretÃ³rio
        int cont = -1;
        for (File imagem : arquivos) {
            cont++;
            System.out.println(cont);
           
            caracteristicas[cont] = extraiCaracteristicas(imagem, exibeImagem);

            String classe = caracteristicas[cont][6] == 0 ?"Limao":"Laranja";

            System.out.println("laranjaLaranja: " + caracteristicas[cont][0]
                    + " - amareloLimao: " + caracteristicas[cont][1]
                    + " - verdeLaranja: " + caracteristicas[cont][2]
                    + " - verdeLimao: " + caracteristicas[cont][3]
                    + " - Classe: " + classe);

            exportacao += caracteristicas[cont][0] + ","
                    + caracteristicas[cont][1] + ","
                    + caracteristicas[cont][2] + ","
                    + caracteristicas[cont][3] + ","
                    + classe + "\n";
        }

        // Grava o arquivo ARFF no disco
        try {
            File arquivo = new File("caracteristicas.arff");
            FileOutputStream f = new FileOutputStream(arquivo);
            f.write(exportacao.getBytes());
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showResult(Mat img) {
        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
