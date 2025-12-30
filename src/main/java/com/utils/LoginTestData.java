package com.utils;

/**
 * DTO (Data Transfer Object) para los datos de prueba de login.
 * Representa una fila del archivo CSV de datasets.
 */
public class LoginTestData {
    
    private String datasetName;
    private String usuario;
    private String password;
    private String expectedOutcome;
    private String nota;

    public LoginTestData(String datasetName, String usuario, String password, 
                         String expectedOutcome, String nota) {
        this.datasetName = datasetName;
        this.usuario = usuario != null ? usuario : "";
        this.password = password != null ? password : "";
        this.expectedOutcome = expectedOutcome;
        this.nota = nota;
    }

    public String getDatasetName() { return datasetName; }
    public String getUsuario() { return usuario; }
    public String getPassword() { return password; }
    public String getExpectedOutcome() { return expectedOutcome; }
    public String getNota() { return nota; }

    public boolean isValid() {
        if (datasetName == null || datasetName.trim().isEmpty()) {
            return false;
        }
        
        if (!"SUCCESS".equals(expectedOutcome) && !"ERROR".equals(expectedOutcome)) {
            return false;
        }
        
        return true;
    }

    public Object[] toArray() {
        return new Object[] { datasetName, usuario, password, expectedOutcome, nota };
    }

    @Override
    public String toString() {
        return "LoginTestData{" +
                "datasetName='" + datasetName + '\'' +
                ", usuario='" + usuario + '\'' +
                ", expectedOutcome='" + expectedOutcome + '\'' +
                ", nota='" + nota + '\'' +
                '}';
    }
}
