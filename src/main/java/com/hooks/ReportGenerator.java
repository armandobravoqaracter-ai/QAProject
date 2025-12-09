package com.hooks;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ReportGenerator {

    private static final String SCREENSHOT_FOLDER = "target/screenshots/";
    private static final String REPORT_FOLDER = "target/screenshots/reportes/";

    public static void generateScenarioReport(String scenarioName, boolean failed) {

        try {
            new File(REPORT_FOLDER).mkdirs();

            String normalized = scenarioName.replaceAll(" ", "_");

            File[] images = new File(SCREENSHOT_FOLDER)
                    .listFiles((dir, name) -> name.contains(normalized) && name.endsWith(".png"));

            if (images == null || images.length == 0) {
                System.out.println("⚠️ No hay capturas para: " + scenarioName);
                return;
            }

            Arrays.sort(images);

            String reportPath = REPORT_FOLDER + normalized + ".html";
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            try (PrintWriter writer = new PrintWriter(reportPath)) {

                writer.println("""
                    <html>
                    <head>
                        <title>Reporte Escenario</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background: #1e1e1e;
                                color: #e0e0e0;
                                margin: 0;
                                padding: 20px;
                            }
                            h1 {
                                color: #00e676;
                            }
                            .failed {
                                color: #ff5252;
                            }
                            .step-card {
                                background: #2c2c2c;
                                padding: 15px;
                                margin-bottom: 12px;
                                border-radius: 10px;
                                border-left: 6px solid #00e676;
                            }
                            .step-card.failed {
                                border-left: 6px solid #ff5252;
                            }
                            .step-title {
                                font-size: 18px;
                                font-weight: bold;
                                cursor: pointer;
                            }
                            .step-content {
                                display: none;
                                margin-top: 10px;
                            }
                            img {
                                width: 650px;
                                border-radius: 8px;
                                margin-top: 10px;
                            }
                            .timestamp {
                                font-size: 12px;
                                opacity: 0.7;
                            }
                        </style>

                        <script>
                            function toggleContent(id) {
                                var content = document.getElementById(id);
                                content.style.display = content.style.display === "block" ? "none" : "block";
                            }
                        </script>
                    </head>
                    <body>
                """);

                writer.println("<h1>Reporte del escenario</h1>");
                writer.println("<h2>" + scenarioName + "</h2>");
                writer.println("<p class='timestamp'>Generado: " + timestamp + "</p>");

                boolean scenarioHasFailStep = false;
                int stepNumber = 1;

                // --- DETECTAR FAIL GLOBAL Y PINTAR STEPS ---
                for (File img : images) {

                    String fileName = img.getName();
                    String lower = fileName.toLowerCase();

                    // Detecta si un step falló por "_FAIL"
                    boolean isFailStep = lower.contains("_fail");

                    if (isFailStep) {
                        scenarioHasFailStep = true;
                    }
                }

                boolean finalStatusFailed = failed || scenarioHasFailStep;

                writer.println("<h2>Estado: <span class='" +
                        (finalStatusFailed ? "failed" : "") + "'>" +
                        (finalStatusFailed ? "❌ Falló" : "✔️ Pasó") +
                        "</span></h2><hr>");

                // --- GENERAR TARJETAS DE STEPS ---
                stepNumber = 1;

                for (File img : images) {

                    String fileName = img.getName();
                    String lower = fileName.toLowerCase();
                    boolean isFailStep = lower.contains("_fail");

                    // Extraer nombre del paso del nombre del archivo
                    String stepText = fileName
                            .replace(normalized + "_", "")
                            .replace(".png", "")
                            .replaceAll("(?i)_fail", "")
                            .replaceAll("^[0-9]+_", "")
                            .replaceAll("_", " ")
                            .trim();

                    writer.println(String.format("""
                        <div class='step-card %s'>
                            <div class='step-title' onclick="toggleContent('step%d')">
                                %s %s
                            </div>
                            <div class='step-content' id='step%d'>
                                <img src='../%s'>
                                <p class='timestamp'>%s</p>
                            </div>
                        </div>
                        """,
                            (isFailStep ? "failed" : ""),
                            stepNumber,
                            (isFailStep ? "❌" : "✔️"),
                            stepText,
                            stepNumber,
                            img.getName(),
                            img.getName()
                    ));

                    stepNumber++;
                }

                writer.println("</body></html>");
            }

            System.out.println("📄 Reporte HTML generado en: " + reportPath);

        } catch (Exception e) {
            System.out.println("❌ Error generando reporte: " + e.getMessage());
        }
    }

}
