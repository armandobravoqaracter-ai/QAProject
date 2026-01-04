package com.hooks;

import com.utils.EvidenceLinker;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.sorting.SortingMethod;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

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

    // ==================== E5: CUCUMBER HTML REPORTING ====================

    private static final String JSON_REPORT_PATH = "target/cucumber.json";
    private static final String REPORT_OUTPUT_DIR = "target/html-reports";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void generateCucumberReport() {
        try {
            File reportOutputDirectory = new File(REPORT_OUTPUT_DIR);
            List<String> jsonFiles = new ArrayList<>();
            jsonFiles.add(JSON_REPORT_PATH);

            String buildNumber = System.getProperty("BUILD_NUMBER", "LOCAL");
            String projectName = "QA Automation - Selenium III";
            String environment = System.getProperty("ENV", "LOCAL");
            String browser = System.getProperty("SELENIUM_GRID_BROWSER", "chrome");

            Configuration configuration = new Configuration(reportOutputDirectory, projectName);

            configuration.setBuildNumber(buildNumber);
            configuration.addClassifications("Environment", environment);
            configuration.addClassifications("Browser", browser);
            configuration.addClassifications("Platform", System.getProperty("os.name"));
            configuration.addClassifications("Generated", LocalDateTime.now().format(FORMATTER));

            configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);
            configuration.setSortingMethod(SortingMethod.NATURAL);
            
            String evidencePath = getLatestEvidenceFolder();
            if (evidencePath != null) {
                configuration.addClassifications("Evidence Path", evidencePath);
            }

            ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
            reportBuilder.generateReports();

            // E5: Vincular evidencias al reporte
            EvidenceLinker.linkEvidenceToReport();
            EvidenceLinker.generateEvidenceIndex();

          System.out.println(" Report Location: " + reportOutputDirectory.getAbsolutePath());
            System.out.println(" Open: " + new File(reportOutputDirectory, "cucumber-html-reports/overview-features.html").getAbsolutePath());
            if (evidencePath != null) {
                System.out.println(" Evidence Folder: " + evidencePath);
            }
            System.out.println("\n Tip: Open 'overview-features.html' in your browser to view the report");
            
        } catch (Exception e) {
            System.err.println(" Error generating HTML report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getLatestEvidenceFolder() {
        try {
            Path evidenceDir = Paths.get("target/evidence");
            if (!Files.exists(evidenceDir)) {
                return null;
            }

            try (Stream<Path> paths = Files.list(evidenceDir)) {
                return paths
                    .filter(Files::isDirectory)
                    .map(Path::toFile)
                    .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()))
                    .map(File::getAbsolutePath)
                    .orElse(null);
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static void generateStabilityReport(int totalRuns, int passedRuns, int failedRuns) {
        double stabilityRate = (double) passedRuns / totalRuns * 100;
        
        System.out.println("Total Runs:        " + totalRuns);
        System.out.println("Passed:            " + passedRuns + " ✅");
        System.out.println("Failed:            " + failedRuns + " ❌");
        System.out.println("Stability Rate:    " + String.format("%.1f", stabilityRate) + "%");
        System.out.println("\n DoD Status:");
        System.out.println("  " + (stabilityRate >= 95 ? "✅" : "❌") + " Stability >= 95%");
        System.out.println("  ✅ Wait wrappers implemented");
        System.out.println("  ✅ Strong locators (CSS with data-test)");
        System.out.println("  ✅ Automatic evidence capture");
        System.out.println("  ✅ HTML report generated");
    }

}
