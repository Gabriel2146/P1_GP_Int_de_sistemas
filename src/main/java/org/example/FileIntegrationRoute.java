package org.example;

import org.apache.camel.builder.RouteBuilder;

public class FileIntegrationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

    from("file:data/input?include=.*\\.csv&delete=true")
                .routeId("ProcesamientoAdmisionesCSV")
                .log("=========================================================")
                .log("Iniciando procesamiento del archivo: ${file:name}")
                .convertBodyTo(String.class)
                .process(new ValidatorProcessor())
                .choice()
                .when(header("isValidCsv").isEqualTo(true))
                .log("RESULTADO: Válido. Moviendo a output y archive...")
                .to("file:data/output")
                .to("file:data/archive?fileName=${file:name.noext}_${date:now:yyyy-MM-dd_HHmmss}.${file:ext}")
                .otherwise()
                .log("RESULTADO: Inválido. Moviendo a la carpeta de error...")
                .to("file:data/error")
                .end()
                .log("Procesamiento finalizado para: ${file:name}")
                .log("=========================================================\n");
    }
}