package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Plantilla para registro de fallas compatible con Azure DevOps.
 */
public class AzureDevOpsDefectTemplate {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Genera un ID único para la falla.
     * Formato: F-YYYYMMDD-numero
     */
    public static String generateDefectId(int numero) {
        return "F-" + LocalDateTime.now().format(DATE_FORMATTER) + "-" + String.format("%03d", numero);
    }


    public static String generateDefectTemplate(
            String testName,
            String datasetName,
            String errorMessage,
            String screenshotPath,
            FailureType failureType,
            Priority priority) {
        
        StringBuilder template = new StringBuilder();
        
        template.append("═══════════════════════════════════════════════════════════\n");
        template.append("          REGISTRO DE DEFECTO - AZURE DEVOPS              \n");
        template.append("═══════════════════════════════════════════════════════════\n\n");
        
        // Campos mínimos requeridos
        template.append("FALLA ID: ").append(generateDefectId(1)).append("\n");
        template.append("Caso de Prueba: ").append(testName).append("\n");
        template.append("Dataset: ").append(datasetName).append("\n");
        template.append("Timestamp: ").append(LocalDateTime.now().format(TIMESTAMP_FORMATTER)).append("\n");
        template.append("Ambiente: ").append(System.getProperty("test.environment", "QA")).append("\n");
        template.append("Versión: ").append(System.getProperty("app.version", "1.0.0")).append("\n");
        template.append("Azure DevOps ID: #").append("{work-item-id}").append(" (pendiente)\n\n");
        
        // Tipo de falla
        template.append("TIPO DE FALLA:\n");
        template.append(failureType == FailureType.SCRIPT_ERROR ? "[X]" : "[ ]")
            .append(" Script Error - Error en el código del test\n");
        template.append(failureType == FailureType.APP_DEFECT ? "[X]" : "[ ]")
            .append(" Application Defect - Bug real de la aplicación\n");
        template.append(failureType == FailureType.ENVIRONMENT_ISSUE ? "[X]" : "[ ]")
            .append(" Environment Issue - Problema de infraestructura\n");
        template.append(failureType == FailureType.TEST_DATA_ISSUE ? "[X]" : "[ ]")
            .append(" Test Data Issue - Datos de prueba incorrectos\n\n");
        
        // Prioridad
        template.append("PRIORIDAD:\n");
        template.append(priority == Priority.P0 ? "[X]" : "[ ]")
            .append(" P0 - Bloqueante (no se puede continuar)\n");
        template.append(priority == Priority.P1 ? "[X]" : "[ ]")
            .append(" P1 - Crítica (funcionalidad principal rota)\n");
        template.append(priority == Priority.P2 ? "[X]" : "[ ]")
            .append(" P2 - Mayor (funcionalidad secundaria)\n");
        template.append(priority == Priority.P3 ? "[X]" : "[ ]")
            .append(" P3 - Menor (cosmético/edge case)\n\n");
        
        // Evidencia
        template.append("EVIDENCIA:\n");
        template.append("- Screenshot: ").append(screenshotPath).append("\n");
        template.append("- Log: ").append("Ver consola de ejecución").append("\n\n");
        
        // Error capturado
        template.append("MENSAJE DE ERROR:\n");
        template.append(errorMessage).append("\n\n");
        
        // Causa raíz (ejemplo)
        template.append("CAUSA RAÍZ (Ejemplo):\n");
        template.append(getCausaRaizExample(failureType)).append("\n\n");
        
        template.append("═══════════════════════════════════════════════════════════\n");
        template.append("NOTAS: Este defecto fue detectado por pruebas automatizadas\n");
        template.append("       Requiere análisis manual para clasificación final\n");
        template.append("═══════════════════════════════════════════════════════════\n");
        
        return template.toString();
    }

    /**
     * Proporciona un ejemplo de causa raíz según el tipo de falla.
     */
    private static String getCausaRaizExample(FailureType type) {
        switch (type) {
            case SCRIPT_ERROR:
                return "Ejemplo: El locator cambió en la aplicación y el test no se actualizó.\n" +
                       "Solución: Actualizar locator en Page Object.";
            case APP_DEFECT:
                return "Ejemplo: El botón de login no funciona con credenciales válidas.\n" +
                       "Solución: Reportar a desarrollo para corrección.";
            case ENVIRONMENT_ISSUE:
                return "Ejemplo: El servidor de QA está caído o el servicio no responde.\n" +
                       "Solución: Reiniciar servicios o verificar infraestructura.";
            case TEST_DATA_ISSUE:
                return "Ejemplo: Las credenciales de prueba expiraron o son inválidas.\n" +
                       "Solución: Actualizar CSV de datos con credenciales válidas.";
            default:
                return "Pendiente de análisis manual.";
        }
    }

    /**
     * Enumeración de tipos de falla.
     */
    public enum FailureType {
        SCRIPT_ERROR("Script Error"),
        APP_DEFECT("Application Defect"),
        ENVIRONMENT_ISSUE("Environment Issue"),
        TEST_DATA_ISSUE("Test Data Issue");

        private final String displayName;

        FailureType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Enumeración de prioridades.
     */
    public enum Priority {
        P0("Bloqueante"),
        P1("Crítico"),
        P2("Mayor"),
        P3("Menor");

        private final String displayName;

        Priority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
