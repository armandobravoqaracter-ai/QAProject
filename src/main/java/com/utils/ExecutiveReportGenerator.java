package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generador de reportes ejecutivos para management/stakeholders.
 */
public class ExecutiveReportGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static String generateReport(TestExecutionStats stats) {
        StringBuilder report = new StringBuilder();
        
        report.append("\n╔════════════════════════════════════════════════════════════╗\n");
        report.append("║        REPORTE EJECUTIVO - PRUEBAS AUTOMATIZADAS         ║\n");
        report.append("╚════════════════════════════════════════════════════════════╝\n\n");
        
        report.append("📅 FECHA: ").append(LocalDateTime.now().format(DATE_FORMATTER)).append("\n");
        report.append("🏷️  SUITE: ").append(stats.getSuiteName()).append("\n");
        report.append("🌍 AMBIENTE: ").append(stats.getEnvironment()).append("\n");
        report.append("📦 VERSIÓN: ").append(stats.getAppVersion()).append("\n\n");
        
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        report.append("📊 RESUMEN DE EJECUCIÓN\n");
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        
        int total = stats.getTotalTests();
        int passed = stats.getPassedTests();
        int failed = stats.getFailedTests();
        int skipped = stats.getSkippedTests();
        double passRate = total > 0 ? (passed * 100.0 / total) : 0.0;
        
        report.append("✓ Total de pruebas: ").append(total).append("\n");
        report.append("✓ Exitosas: ").append(passed).append(" (").append(String.format("%.1f%%", passRate)).append(")\n");
        report.append("✗ Fallidas: ").append(failed).append("\n");
        report.append("⊝ Omitidas: ").append(skipped).append("\n");
        report.append("⏱ Tiempo total: ").append(stats.getExecutionTimeMinutes()).append(" min\n\n");
        
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        report.append("📈 COBERTURA\n");
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        
        report.append("• Login: ").append(stats.getCoverageLogin()).append("%\n");
        report.append("• Inventario: ").append(stats.getCoverageInventory()).append("%\n");
        report.append("• Carrito: ").append(stats.getCoverageCart()).append("%\n");
        report.append("• Total: ").append(stats.getCoverageTotal()).append("%\n\n");
        
        if (failed > 0) {
            report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            report.append("🐛 DEFECTOS ENCONTRADOS\n");
            report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            
            report.append("P0 (Bloqueante): ").append(stats.getDefectsP0()).append("\n");
            report.append("P1 (Crítico): ").append(stats.getDefectsP1()).append("\n");
            report.append("P2 (Mayor): ").append(stats.getDefectsP2()).append("\n");
            report.append("P3 (Menor): ").append(stats.getDefectsP3()).append("\n\n");
        }
        
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        report.append("💰 VALOR ENTREGADO\n");
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        
        int manualTimeMinutes = stats.getEstimatedManualTimeMinutes();
        int automatedTimeMinutes = stats.getExecutionTimeMinutes();
        double roi = manualTimeMinutes > 0 ? (double) manualTimeMinutes / automatedTimeMinutes : 0.0;
        
        report.append("⏱ Tiempo manual estimado: ").append(manualTimeMinutes).append(" min\n");
        report.append("🤖 Tiempo automatizado: ").append(automatedTimeMinutes).append(" min\n");
        report.append("📊 ROI: ").append(String.format("%.1fx", roi)).append(" más rápido\n");
        report.append("🎯 Estabilidad: ").append(String.format("%.1f%%", stats.getStability())).append("\n\n");
        
        if (!stats.getRisks().isEmpty()) {
            report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            report.append("⚠️  RIESGOS IDENTIFICADOS\n");
            report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            
            int riskNum = 1;
            for (String risk : stats.getRisks()) {
                report.append(riskNum++).append(". ").append(risk).append("\n");
            }
            report.append("\n");
        }
        
        report.append("═══════════════════════════════════════════════════════════\n");
        report.append("📌 CONCLUSIÓN: ");
        if (passRate >= 95) {
            report.append("✅ Suite estable y lista para producción\n");
        } else if (passRate >= 80) {
            report.append("⚠️ Suite con fallas menores, revisar antes de release\n");
        } else {
            report.append("❌ Suite inestable, requiere atención inmediata\n");
        }
        report.append("═══════════════════════════════════════════════════════════\n\n");
        
        return report.toString();
    }

    public static class TestExecutionStats {
        private String suiteName = "QAProject - Selenium Tests";
        private String environment = "QA";
        private String appVersion = "1.0.0";
        private int totalTests = 0;
        private int passedTests = 0;
        private int failedTests = 0;
        private int skippedTests = 0;
        private int executionTimeMinutes = 0;
        private int estimatedManualTimeMinutes = 0;
        private double stability = 100.0;
        
        private int coverageLogin = 80;
        private int coverageInventory = 70;
        private int coverageCart = 50;
        private int coverageTotal = 67;
        
        private int defectsP0 = 0;
        private int defectsP1 = 0;
        private int defectsP2 = 0;
        private int defectsP3 = 0;
        
        private java.util.List<String> risks = new java.util.ArrayList<>();

        public String getSuiteName() { return suiteName; }
        public void setSuiteName(String suiteName) { this.suiteName = suiteName; }
        
        public String getEnvironment() { return environment; }
        public void setEnvironment(String environment) { this.environment = environment; }
        
        public String getAppVersion() { return appVersion; }
        public void setAppVersion(String appVersion) { this.appVersion = appVersion; }
        
        public int getTotalTests() { return totalTests; }
        public void setTotalTests(int totalTests) { this.totalTests = totalTests; }
        
        public int getPassedTests() { return passedTests; }
        public void setPassedTests(int passedTests) { this.passedTests = passedTests; }
        
        public int getFailedTests() { return failedTests; }
        public void setFailedTests(int failedTests) { this.failedTests = failedTests; }
        
        public int getSkippedTests() { return skippedTests; }
        public void setSkippedTests(int skippedTests) { this.skippedTests = skippedTests; }
        
        public int getExecutionTimeMinutes() { return executionTimeMinutes; }
        public void setExecutionTimeMinutes(int executionTimeMinutes) { 
            this.executionTimeMinutes = executionTimeMinutes; 
        }
        
        public int getEstimatedManualTimeMinutes() { return estimatedManualTimeMinutes; }
        public void setEstimatedManualTimeMinutes(int estimatedManualTimeMinutes) { 
            this.estimatedManualTimeMinutes = estimatedManualTimeMinutes; 
        }
        
        public double getStability() { return stability; }
        public void setStability(double stability) { this.stability = stability; }
        
        public int getCoverageLogin() { return coverageLogin; }
        public void setCoverageLogin(int coverageLogin) { this.coverageLogin = coverageLogin; }
        
        public int getCoverageInventory() { return coverageInventory; }
        public void setCoverageInventory(int coverageInventory) { 
            this.coverageInventory = coverageInventory; 
        }
        
        public int getCoverageCart() { return coverageCart; }
        public void setCoverageCart(int coverageCart) { this.coverageCart = coverageCart; }
        
        public int getCoverageTotal() { return coverageTotal; }
        public void setCoverageTotal(int coverageTotal) { this.coverageTotal = coverageTotal; }
        
        public int getDefectsP0() { return defectsP0; }
        public void setDefectsP0(int defectsP0) { this.defectsP0 = defectsP0; }
        
        public int getDefectsP1() { return defectsP1; }
        public void setDefectsP1(int defectsP1) { this.defectsP1 = defectsP1; }
        
        public int getDefectsP2() { return defectsP2; }
        public void setDefectsP2(int defectsP2) { this.defectsP2 = defectsP2; }
        
        public int getDefectsP3() { return defectsP3; }
        public void setDefectsP3(int defectsP3) { this.defectsP3 = defectsP3; }
        
        public java.util.List<String> getRisks() { return risks; }
        public void addRisk(String risk) { this.risks.add(risk); }
    }
}
