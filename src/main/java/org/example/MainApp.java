package org.example;

import org.apache.camel.main.Main;

public class MainApp {

    public static void main(String[] args) throws Exception {
        // Inicializamos el motor principal de Apache Camel
        Main main = new Main();

        // Le indicamos a Camel qué ruta debe cargar
        main.configure().addRoutesBuilder(new FileIntegrationRoute());

        System.out.println("=================================================");
        System.out.println("Iniciando integración de Clínica SaludVital...");
        System.out.println("Escuchando archivos en la carpeta: data/input");
        System.out.println("Presiona Ctrl+C en la consola para detener.");
        System.out.println("=================================================");

        // Arrancamos el proceso (se queda en ejecución continua)
        main.run(args);
    }
}