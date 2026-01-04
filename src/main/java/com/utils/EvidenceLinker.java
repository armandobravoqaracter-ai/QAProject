package com.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * Utilidad para vincular evidencias con reportes HTML
 */
public class EvidenceLinker {

    private static final String EVIDENCE_DIR = "target/evidence";
    private static final String REPORT_DIR = "target/html-reports/cucumber-html-reports";
    private static final String EVIDENCE_SUBDIR = "evidence";

    public static void linkEvidenceToReport() {
        try {
            Path evidenceSourceDir = Paths.get(EVIDENCE_DIR);
            Path reportEvidenceDir = Paths.get(REPORT_DIR, EVIDENCE_SUBDIR);

            if (!Files.exists(evidenceSourceDir)) {
                System.out.println("⚠️ No evidence folder found to link");
                return;
            }
            Files.createDirectories(reportEvidenceDir);

            Path latestEvidenceFolder = getLatestEvidenceFolder();
            if (latestEvidenceFolder == null) {
                System.out.println("⚠️ No evidence execution folder found");
                return;
            }

            // Copiar todas las evidencias al reporte
            try (Stream<Path> paths = Files.walk(latestEvidenceFolder)) {
                paths.filter(Files::isRegularFile)
                     .forEach(source -> {
                         try {
                             Path destination = reportEvidenceDir.resolve(source.getFileName());
                             Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                         } catch (IOException e) {
                             System.err.println("Error copying evidence: " + e.getMessage());
                         }
                     });
            }

            System.out.println(" Evidence linked to report: " + reportEvidenceDir.toAbsolutePath());

        } catch (IOException e) {
            System.err.println(" Error linking evidence to report: " + e.getMessage());
        }
    }

    private static Path getLatestEvidenceFolder() {
        try {
            Path evidenceDir = Paths.get(EVIDENCE_DIR);
            if (!Files.exists(evidenceDir)) {
                return null;
            }

            try (Stream<Path> paths = Files.list(evidenceDir)) {
                return paths
                    .filter(Files::isDirectory)
                    .map(Path::toFile)
                    .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()))
                    .map(File::toPath)
                    .orElse(null);
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static void generateEvidenceIndex() {
        try {
            Path latestEvidenceFolder = getLatestEvidenceFolder();
            if (latestEvidenceFolder == null) {
                return;
            }

            Path indexPath = latestEvidenceFolder.resolve("index.html");
            StringBuilder html = new StringBuilder();
            
            html.append("<!DOCTYPE html>\n");
            html.append("<html>\n<head>\n");
            html.append("<title>Evidence Index</title>\n");
            html.append("<style>\n");
            html.append("body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }\n");
            html.append("h1 { color: #333; }\n");
            html.append(".evidence-item { background: white; padding: 15px; margin: 10px 0; border-radius: 5px; border-left: 4px solid #ff5252; }\n");
            html.append(".screenshot { max-width: 600px; border: 1px solid #ddd; margin-top: 10px; }\n");
            html.append("</style>\n");
            html.append("</head>\n<body>\n");
            html.append("<h1>📸 Test Evidence Index</h1>\n");
            html.append("<p>Folder: " + latestEvidenceFolder.getFileName() + "</p>\n");
            html.append("<hr>\n");

            try (Stream<Path> paths = Files.list(latestEvidenceFolder)) {
                paths.filter(path -> path.toString().endsWith(".png"))
                     .sorted()
                     .forEach(screenshot -> {
                         try {
                             String fileName = screenshot.getFileName().toString();
                             String contextFile = fileName.replace(".png", ".txt");
                             Path contextPath = screenshot.getParent().resolve(contextFile);
                             
                             html.append("<div class='evidence-item'>\n");
                             html.append("<h3>").append(fileName).append("</h3>\n");
                             
                             if (Files.exists(contextPath)) {
                                 String context = Files.readString(contextPath);
                                 html.append("<pre>").append(context).append("</pre>\n");
                             }
                             
                             html.append("<a href='").append(fileName).append("' target='_blank'>");
                             html.append("<img class='screenshot' src='").append(fileName).append("' alt='Screenshot'>");
                             html.append("</a>\n");
                             html.append("</div>\n");
                             
                         } catch (IOException e) {
                             System.err.println("Error processing screenshot: " + e.getMessage());
                         }
                     });
            }

            html.append("</body>\n</html>");
            Files.writeString(indexPath, html.toString());
            
            System.out.println(" Evidence index generated: " + indexPath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println(" Error generating evidence index: " + e.getMessage());
        }
    }
}
