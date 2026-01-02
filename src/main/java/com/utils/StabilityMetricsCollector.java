package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Colector de métricas de estabilidad para tests automatizados.
 */
public class StabilityMetricsCollector {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String suiteName;
    private final List<TestRun> testRuns;
    private final Map<String, TestStability> testStabilityMap;
    
    public StabilityMetricsCollector(String suiteName) {
        this.suiteName = suiteName;
        this.testRuns = new ArrayList<>();
        this.testStabilityMap = new HashMap<>();
    }

    public void recordRun(int runNumber, Map<String, Boolean> testResults, long executionTimeMs) {
        TestRun run = new TestRun(runNumber, testResults, executionTimeMs);
        testRuns.add(run);
        
        for (Map.Entry<String, Boolean> entry : testResults.entrySet()) {
            String testName = entry.getKey();
            boolean passed = entry.getValue();
            
            testStabilityMap.putIfAbsent(testName, new TestStability(testName));
            testStabilityMap.get(testName).recordResult(passed);
        }
    }

    public double calculateErrorRate() {
        if (testRuns.isEmpty()) return 0.0;
        
        int totalFailures = 0;
        int totalExecutions = 0;
        
        for (TestRun run : testRuns) {
            for (boolean passed : run.testResults.values()) {
                totalExecutions++;
                if (!passed) totalFailures++;
            }
        }
        
        return totalExecutions > 0 ? (double) totalFailures / totalExecutions : 0.0;
    }

    public double calculateInstabilityRate() {
        if (testStabilityMap.isEmpty()) return 0.0;
        
        int flakyTests = 0;
        for (TestStability stability : testStabilityMap.values()) {
            if (stability.isFlaky()) {
                flakyTests++;
            }
        }
        
        return (double) flakyTests / testStabilityMap.size();
    }

    public String generateStabilityReport() {
        StringBuilder report = new StringBuilder();

        report.append(" FECHA: ").append(LocalDateTime.now().format(TIMESTAMP_FORMATTER)).append("\n");
        report.append(" SUITE: ").append(suiteName).append("\n");
        report.append(" CORRIDAS: ").append(testRuns.size()).append("\n\n");

        double errorRate = calculateErrorRate();
        double instabilityRate = calculateInstabilityRate();
        
        report.append(String.format(" Tasa de Error: %.1f%%\n", errorRate * 100));
        report.append(String.format("  Tasa de Inestabilidad: %.1f%%\n", instabilityRate * 100));
        report.append(String.format(" Tests Estables: %d de %d\n\n", 
            testStabilityMap.size() - (int)(instabilityRate * testStabilityMap.size()),
            testStabilityMap.size()));
        
        for (TestRun run : testRuns) {
            long passedCount = run.testResults.values().stream().filter(p -> p).count();
            long failedCount = run.testResults.size() - passedCount;
            
            report.append(String.format("Corrida #%d: ✓ %d  ❌ %d  (%.1fs)\n", 
                run.runNumber, passedCount, failedCount, run.executionTimeMs / 1000.0));
        }
        
        for (TestStability stability : testStabilityMap.values()) {
            report.append(stability.getDetailedReport()).append("\n");
        }
        
        if (instabilityRate == 0) {
            report.append(" Suite 100% estable - Excelente trabajo\n");
        } else if (instabilityRate < 0.1) {
            report.append("Suite mayormente estable (<10% flaky)\n");
            report.append("   Investigar tests flaky para eliminar causas\n");
        } else if (instabilityRate < 0.3) {
            report.append(" Suite con inestabilidad moderada (10-30% flaky)\n");
            report.append(" Priorizar corrección de tests flaky\n");
            report.append("  Revisar waits, sincronización, datos de prueba\n");
        } else {
            report.append(" Suite altamente inestable (>30% flaky)\n");
            report.append("  ACCIÓN URGENTE: Refactorizar tests problemáticos\n");
            report.append(" Considerar rediseño de estrategia de testing\n");
        }

        return report.toString();
    }

    /**
     * Identifica el test más problemático 
     */
    public String identifyRootCauseFocus() {
        TestStability mostProblematic = null;
        double worstStability = 1.0;
        
        for (TestStability stability : testStabilityMap.values()) {
            double stabilityRate = stability.getStabilityRate();
            if (stabilityRate < worstStability) {
                worstStability = stabilityRate;
                mostProblematic = stability;
            }
        }
        
        if (mostProblematic != null && worstStability < 1.0) {
            return String.format(" FOCO DE CAUSA RAÍZ: %s (%.1f%% estable)\n" +
                    "   Falló %d de %d veces\n" +
                    "   Tipo de inestabilidad: %s",
                    mostProblematic.testName,
                    worstStability * 100,
                    mostProblematic.failures,
                    mostProblematic.totalRuns,
                    mostProblematic.isFlaky() ? "FLAKY (intermitente)" : "CONSISTENTE");
        }
        
        return " No se identificaron problemas de estabilidad";
    }

    // Clases internas
    
    private static class TestRun {
        final int runNumber;
        final Map<String, Boolean> testResults;
        final long executionTimeMs;
        
        TestRun(int runNumber, Map<String, Boolean> testResults, long executionTimeMs) {
            this.runNumber = runNumber;
            this.testResults = new HashMap<>(testResults);
            this.executionTimeMs = executionTimeMs;
        }
    }

    private static class TestStability {
        final String testName;
        int totalRuns = 0;
        int passes = 0;
        int failures = 0;
        final List<Boolean> resultHistory = new ArrayList<>();
        
        TestStability(String testName) {
            this.testName = testName;
        }
        
        void recordResult(boolean passed) {
            totalRuns++;
            if (passed) {
                passes++;
            } else {
                failures++;
            }
            resultHistory.add(passed);
        }
        
        boolean isFlaky() {
            return passes > 0 && failures > 0;
        }
        
        double getStabilityRate() {
            return totalRuns > 0 ? (double) passes / totalRuns : 1.0;
        }
        
        String getDetailedReport() {
            StringBuilder detail = new StringBuilder();
            
            String icon = getStabilityRate() == 1.0 ? "bien" : 
                         isFlaky() ? "peligro" : "fail";
            
            detail.append(String.format("%s %s\n", icon, testName));
            detail.append(String.format("   Estabilidad: %.1f%% (%d✓ / %d❌ de %d corridas)\n",
                    getStabilityRate() * 100, passes, failures, totalRuns));
            
            // Mostrar patrón de fallas
            if (isFlaky()) {
                detail.append("   Patrón: ");
                for (int i = 0; i < resultHistory.size(); i++) {
                    detail.append(resultHistory.get(i) ? "bien" : "fail");
                    if (i < resultHistory.size() - 1) detail.append(" ");
                }
                detail.append(" (FLAKY - Intermitente)");
            } else if (failures > 0) {
                detail.append("   Patrón: Falla consistente - Posible defecto real");
            }
            
            return detail.toString();
        }
    }
    
    public Map<String, TestStability> getTestStabilityMap() {
        return new HashMap<>(testStabilityMap);
    }
    
    public List<TestRun> getTestRuns() {
        return new ArrayList<>(testRuns);
    }
}
