package com.hooks;

import io.cucumber.java.*;
import io.cucumber.messages.types.TestStep;
import io.cucumber.plugin.event.PickleStepTestStep;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.reflect.Field;
import java.util.List;

public class ScreenshotHooks {

    private final BaseTest baseTest;
    private WebDriver driver;
    private int stepCounter = 1;
    private static final String SCREENSHOT_FOLDER = "target/screenshots/";

    public ScreenshotHooks(BaseTest baseTest) {
        this.baseTest = baseTest;
    }

    @BeforeAll
    public static void cleanScreenshotFolder() {
        File folder = new File(SCREENSHOT_FOLDER);
        if (folder.exists()) {
            for (File f : folder.listFiles()) f.delete();
        } else {
            folder.mkdirs();
        }
        System.out.println("📁 Carpeta de capturas limpia");
    }

    @Before
    public void init(Scenario scenario) {
        this.driver = baseTest.getDriver();
        System.out.println("▶️ Iniciando escenario: " + scenario.getName());
    }

    @AfterStep
    public void captureAfterStep(Scenario scenario) {
        try {
            String scenarioName = scenario.getName().replaceAll(" ", "_");
            String stepStatus = scenario.isFailed() ? "_FAIL" : ""; // marca si falló
            String imageName = scenarioName + "_Step_" + stepCounter++ + stepStatus + ".png";

            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Step " + stepCounter + stepStatus);
            Files.write(Paths.get(SCREENSHOT_FOLDER + imageName), screenshot);

            System.out.println("📸 Captura guardada: " + imageName);
        } catch (IOException e) {
            System.out.println("❌ Error al tomar screenshot: " + e.getMessage());
        }
    }



    @After
    public void generateReport(Scenario scenario) {
        ReportGenerator.generateScenarioReport(
                scenario.getName(),
                scenario.isFailed()
        );
    }

    // ======= Método para obtener el paso actual del Scenario =======
    private String getStepName(Scenario scenario) {
        try {
            Field stepField = scenario.getClass().getDeclaredField("step");
            stepField.setAccessible(true);
            Object step = stepField.get(scenario);
            if (step != null) {
                Field textField = step.getClass().getDeclaredField("text");
                textField.setAccessible(true);
                return (String) textField.get(step);
            }
        } catch (Exception ignored) {}
        return "UnknownStep";
    }
}
