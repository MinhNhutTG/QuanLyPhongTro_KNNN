package com.quanlyphongtro;



import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import io.github.cdimascio.dotenv.Dotenv;

import java.awt.EventQueue;

@SpringBootApplication(scanBasePackages = "com.quanlyphongtro")
public class MainApplication {

    public static void main(String[] args) {


        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(e ->
            System.setProperty(e.getKey(), e.getValue())
        );
        
        ConfigurableApplicationContext context = new SpringApplicationBuilder(MainApplication.class)
                .headless(false) 
                .run(args);
        System.out.println(context.getEnvironment().getProperty("test.value"));
        EventQueue.invokeLater(() -> {
            try {
                com.quanlyphongtro.ui.Login loginGui = context.getBean(com.quanlyphongtro.ui.Login.class);
                loginGui.setVisible(true);
                
                System.out.println("Ứng dụng đã sẵn sàng!");
                    } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}