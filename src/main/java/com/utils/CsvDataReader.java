package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Lector de archivos CSV para datasets de pruebas.
 */
public class CsvDataReader {

    public static List<LoginTestData> readLoginData(String resourcePath) throws IOException {
        List<LoginTestData> testDataList = new ArrayList<>();
        Set<String> datasetNames = new HashSet<>();
        
        InputStream inputStream = CsvDataReader.class.getClassLoader().getResourceAsStream(resourcePath);
        
        if (inputStream == null) {
            throw new IOException("No se pudo encontrar el archivo: " + resourcePath + 
                " en resources/. Verifica que el archivo existe y la ruta es correcta.");
        }
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            
            String line;
            int lineNumber = 0;
            String[] headers = null;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                if (lineNumber == 1) {
                    headers = line.split(",");
                    validateHeaders(headers, resourcePath);
                    continue;
                }
                
                String[] values = line.split(",", -1); // -1 para preservar campos vacíos
                
                if (values.length != 5) {
                    throw new IllegalArgumentException(
                        String.format("Línea %d del archivo %s tiene %d columnas, se esperan 5",
                            lineNumber, resourcePath, values.length));
                }
                
                LoginTestData testData = new LoginTestData(
                    values[0].trim(),  
                    values[1].trim(),  
                    values[2].trim(),  
                    values[3].trim(),  
                    values[4].trim()   
                );
                
                if (!testData.isValid()) {
                    throw new IllegalArgumentException(
                        String.format("Línea %d del archivo %s contiene datos inválidos: %s",
                            lineNumber, resourcePath, testData));
                }
                
                if (datasetNames.contains(testData.getDatasetName())) {
                    throw new IllegalArgumentException(
                        String.format("Línea %d del archivo %s: datasetName '%s' duplicado",
                            lineNumber, resourcePath, testData.getDatasetName()));
                }
                
                datasetNames.add(testData.getDatasetName());
                testDataList.add(testData);
            }
            
            if (testDataList.isEmpty()) {
                throw new IllegalArgumentException(
                    "El archivo " + resourcePath + " no contiene datos de prueba");
            }
            
            System.out.println("Archivo CSV leído correctamente: " + resourcePath);
            System.out.println("Total de datasets cargados: " + testDataList.size());
            
            return testDataList;
            
        } catch (IOException e) {
            throw new IOException("Error al leer el archivo " + resourcePath + ": " + e.getMessage(), e);
        }
    }

    /**
     * Valida que los headers del CSV sean los esperados.
     */
    private static void validateHeaders(String[] headers, String resourcePath) {
        String[] expectedHeaders = {"datasetName", "usuario", "password", "expectedOutcome", "nota"};
        
        if (headers.length != expectedHeaders.length) {
            throw new IllegalArgumentException(
                String.format("El archivo %s tiene %d columnas, se esperan %d. Headers esperados: %s",
                    resourcePath, headers.length, expectedHeaders.length, 
                    String.join(", ", expectedHeaders)));
        }
        
        for (int i = 0; i < expectedHeaders.length; i++) {
            if (!headers[i].trim().equals(expectedHeaders[i])) {
                throw new IllegalArgumentException(
                    String.format("El archivo %s tiene header incorrecto en columna %d: '%s'. Se esperaba: '%s'",
                        resourcePath, i + 1, headers[i].trim(), expectedHeaders[i]));
            }
        }
    }

    /**
     * Convierte una lista de LoginTestData a un array 2D para TestNG DataProvider.
     * 
     * @param testDataList lista de datos de prueba
     * @return Object[][] compatible con TestNG DataProvider
     */
    public static Object[][] toDataProviderArray(List<LoginTestData> testDataList) {
        Object[][] dataArray = new Object[testDataList.size()][];
        
        for (int i = 0; i < testDataList.size(); i++) {
            dataArray[i] = testDataList.get(i).toArray();
        }
        
        return dataArray;
    }
}
