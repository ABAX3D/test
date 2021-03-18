/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package update_test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.security.auth.login.Configuration;

/**
 *
 * @author Usuario
 */
public class Update_test extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                System.out.println("iniciando descarga");
                
                try {
                    descargar();
                    descomprimir();
                } catch (IOException ex) {
                    System.out.println("error IO");
                    Logger.getLogger(Update_test.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("descarga terminada");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch(args);
    }
    
    public void descargar() throws IOException{
        try {
            ReadableByteChannel readChannel = Channels.newChannel(new URL("https://github.com/ABAX3D/test/blob/main/src/update_test/Update_test.java").openStream());
            FileOutputStream fileOS = new FileOutputStream("C:/Users/Usuario/Desktop/test update/Update_test.java");
            FileChannel writeChannel = fileOS.getChannel();
            writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
        } catch (MalformedURLException ex) {
            System.out.println("error MalforedURl");
            Logger.getLogger(Update_test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void descomprimir() throws IOException{
        String fileZip = "src/main/resources/unzipTest/compressed.zip";
        File destDir = new File("Source Packages/main/resources/unzipTest");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
           File newFile = newFile(destDir, zipEntry);
     if (zipEntry.isDirectory()) {
         if (!newFile.isDirectory() && !newFile.mkdirs()) {
             throw new IOException("Failed to create directory " + newFile);
         }
     } else {
         // fix for Windows-created archives
         File parent = newFile.getParentFile();
         if (!parent.isDirectory() && !parent.mkdirs()) {
             throw new IOException("Failed to create directory " + parent);
         }
         
         // write file content
         FileOutputStream fos = new FileOutputStream(newFile);
         int len;
         while ((len = zis.read(buffer)) > 0) {
             fos.write(buffer, 0, len);
         }
         fos.close();
     }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }
    
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
    File destFile = new File(destinationDir, zipEntry.getName());

    String destDirPath = destinationDir.getCanonicalPath();
    String destFilePath = destFile.getCanonicalPath();

    if (!destFilePath.startsWith(destDirPath + File.separator)) {
        throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
    }

    return destFile;
}
    
}
