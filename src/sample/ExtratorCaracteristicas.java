package sample;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javafx.scene.image.ImageView;
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

import java.awt.*;


public class ExtratorCaracteristicas {
    public static double[] extraiCaracteristicas(File f, boolean exibeImagem) {

        double[] caracteristicas = new double[7];

    	double laranjaLaranja = 0;
        double amareloLimao = 0;
        double verdeLaranja = 0;
        double verdeLimao = 0;
        double brancoLaranja = 0;
        double brancoLimao = 0;
    	
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

                double[] processadoCorLaranja = new double[]{0, 255, 128};
                double[] processadoCorLimao = new double[]{0, 255, 255};
                
            	if (isLaranjaLaranja(r, g, b)) {
            		laranjaLaranja++;
            		imagemProcessada.put(i, j, processadoCorLaranja);
            	}
            	
            	if (isAmareloLimao(r, g, b)) {
            		amareloLimao++;
            		imagemProcessada.put(i, j, processadoCorLimao);
            	}
            	
            	if (isVerdeLaranja(r, g, b)) {
            		verdeLaranja++;
            		imagemProcessada.put(i, j, processadoCorLaranja);
            	}
            	
    			if (isVerdeLimao(r, g, b)) {
    				verdeLimao++;
    				imagemProcessada.put(i, j, processadoCorLimao);
    			}
    			
				if (isBrancoLaranja(r, g, b)) {
					brancoLaranja++;
					imagemProcessada.put(i, j, processadoCorLaranja);
				}
				
				if (isBrancoLimao(r, g, b)) {
					brancoLimao++;
					imagemProcessada.put(i, j, processadoCorLimao);
				}
            	
            }
        }

        // Normaliza as caracterÃ­sticas pelo nÃºmero de pixels totais da imagem para %
        laranjaLaranja = (laranjaLaranja / (w * h)) * 100;
        amareloLimao = (amareloLimao / (w * h)) * 100;
        verdeLaranja = (verdeLaranja / (w * h)) * 100;
        verdeLimao = (verdeLimao / (w * h)) * 100;
        brancoLaranja = (brancoLaranja / (w * h)) * 100;
        brancoLimao = (brancoLimao / (w * h)) * 100;

        caracteristicas[0] = laranjaLaranja;
        caracteristicas[1] = amareloLimao;
        caracteristicas[2] = verdeLaranja;
        caracteristicas[3] = verdeLimao;
        caracteristicas[4] = brancoLaranja;
        caracteristicas[5] = brancoLimao;
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

    public static boolean isLaranjaLaranja(double r, double g, double b) {
    	float[] hsb = java.awt.Color.RGBtoHSB((int) r, (int) g, (int) b, null);

    	float h = hsb[0] * 360;
    	float newb = hsb[2] * 100;
    	
        return h < 26 && h > 15 && newb > 50; 
    }

    public static boolean isAmareloLimao(double r, double g, double b) {
    	float[] hsb = java.awt.Color.RGBtoHSB((int) r, (int) g, (int) b, null);
    	float h = hsb[0] * 360;
    	float newb = hsb[2] * 100;
    	
        return h < 75 && h > 37 && newb > 50;
    }
    
    public static boolean isVerdeLaranja(double r, double g, double b) {
    	return false;
    }
    
    public static boolean isBrancoLaranja(double r, double g, double b) {
    	return false;
    }
    
    
    
    public static boolean isVerdeLimao(double r, double g, double b) {
    	return false;
    }
    
    public static boolean isBrancoLimao(double r, double g, double b) {
    	return false;
    }
    
    public static void extrair(boolean exibeImagem) {

        // Cabeçalho do arquivo Weka
        String exportacao = "@relation caracteristicas\n\n";
        exportacao += "@attribute laranjaLaranja real\n";
        exportacao += "@attribute amareloLimao real\n";
        exportacao += "@attribute verdeLaranja real\n";
        exportacao += "@attribute verdeLimao real\n";
        exportacao += "@attribute brancoLaranja real\n";
        exportacao += "@attribute brancoLimao real\n";
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
                    + " - brancoLaranja: " + caracteristicas[cont][4]
                    + " - brancoLimao: " + caracteristicas[cont][5]
                    + " - Classe: " + classe);

            exportacao += caracteristicas[cont][0] + ","
                    + caracteristicas[cont][1] + ","
                    + caracteristicas[cont][2] + ","
                    + caracteristicas[cont][3] + ","
                    + caracteristicas[cont][4] + ","
                    + caracteristicas[cont][5] + ","
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
