package com.example.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import com.example.repositories.FileWordSeacher;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageProcessor implements FileWordSeacher {
    
    private static final double BACKGROUND_THRESHOLD = 0.65; // 65% of pixels considered background

	public List<String> search(Path filePath, String word) throws IOException, TesseractException {
        return searchWordInImage(filePath, word);
    }
    
    private static BufferedImage preprocessDocument(BufferedImage original) {
        // 1. Auto-detect and invert dark backgrounds
        BufferedImage inverted = autoInvertBackground(original);
        
        // 2. Binarize with adaptive threshold
        BufferedImage binary = binarizeOtsu(inverted);
        
        // 3. Thin text using morphological erosion
        BufferedImage thinned = thinTextLines(binary);
        
        return thinned;
    }

    private static BufferedImage autoInvertBackground(BufferedImage img) {
        // 1. Calculate percentage of pixels that are "light"
        double lightPixelPercentage = calculateLightPixelPercentage(img);
        
        // 2. If most pixels are dark (below threshold), invert
        if (lightPixelPercentage < BACKGROUND_THRESHOLD) {
            return invertImage(img);
        }
        return img;
    }

    private static double calculateLightPixelPercentage(BufferedImage img) {
        int lightPixels = 0;
        int totalPixels = img.getWidth() * img.getHeight();
        
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                double luminance = 0.299 * r + 0.587 * g + 0.114 * b;
                
                // Consider pixels with luminance > 150 as "light"
                if (luminance > 150) {
                    lightPixels++;
                }
            }
        }
        
        return (double) lightPixels / totalPixels;
    }

    // Image inversion
    private static BufferedImage invertImage(BufferedImage img) {
        BufferedImage inverted = new BufferedImage(
            img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for(int y = 0; y < img.getHeight(); y++) {
            for(int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                inverted.setRGB(x, y, ~rgb);
            }
        }
        return inverted;
    }

    // Binarization using Otsu's method
    private static BufferedImage binarizeOtsu(BufferedImage gray) {
        int threshold = calculateOtsuThreshold(gray);
        BufferedImage binary = new BufferedImage(
            gray.getWidth(), gray.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        
        for(int y = 0; y < gray.getHeight(); y++) {
            for(int x = 0; x < gray.getWidth(); x++) {
                int rgb = gray.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int luminance = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                binary.setRGB(x, y, luminance > threshold ? 0xFFFFFF : 0x000000);
            }
        }
        return binary;
    }

    // Otsu's threshold calculation
    private static int calculateOtsuThreshold(BufferedImage image) {
        int[] histogram = new int[256];
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int luminance = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                histogram[luminance]++;
            }
        }

        // Otsu's algorithm implementation
        double total = image.getWidth() * image.getHeight();
        double sum = 0, sumB = 0, wB = 0, wF, max = 0;
        int threshold = 0;

        for(int i = 0; i < 256; i++) sum += i * histogram[i];

        for(int t = 0; t < 256; t++) {
            wB += histogram[t];
            if(wB == 0) continue;
            wF = total - wB;
            if(wF == 0) break;
            
            sumB += t * histogram[t];
            double mB = sumB / wB;
            double mF = (sum - sumB) / wF;
            double var = wB * wF * (mB - mF) * (mB - mF);
            
            if(var > max) {
                max = var;
                threshold = t;
            }
        }
        return threshold;
    }

    // Text thinning using Zhang-Suen algorithm
    private static BufferedImage thinTextLines(BufferedImage binary) {
        BufferedImage thinned = new BufferedImage(
            binary.getWidth(), binary.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        
        // Copy original image
        for(int y = 0; y < binary.getHeight(); y++) {
            for(int x = 0; x < binary.getWidth(); x++) {
                thinned.setRGB(x, y, binary.getRGB(x, y));
            }
        }

        return thinned;
    }
    
    public static List<String> searchWordInImage(Path filePath, String searchWord) throws IOException, TesseractException {
       	List<String> status = new LinkedList<>();
        BufferedImage original = ImageIO.read(filePath.toFile());
        BufferedImage processed = ImageProcessor.preprocessDocument(original);
        ImageIO.write(processed, "png", new File("preprocessed.png"));
        // OCR with Tesseract
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage("por");
        tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
        String result = tesseract.doOCR(processed);
        if (result.contains(searchWord)) {
        	status.add("Word found in this image");
       	}
        return status;
    }

//    // Main execution
//    public static void main(String[] args) {
//        try {
//            BufferedImage original = ImageIO.read(new File("/home/francisco/Documentos/workspace_pessoal_2/arquivos/teste4.jpg"));
//            BufferedImage processed = preprocessDocument(original);
//            ImageIO.write(processed, "png", new File("/home/francisco/Documentos/workspace_pessoal_2/arquivos/preprocessed.png"));
//            
//            // OCR with Tesseract
//            ITesseract tesseract = new Tesseract();
//            tesseract.setLanguage("por");
//            tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
//            String result = tesseract.doOCR(processed);
//            System.out.println("OCR Result: " + result);
//            
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}