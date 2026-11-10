package com.example.student_registration;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StudentRegistrationTest {

    WebDriver driver;

    // Runs before every test case
    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        // Run headless only in GitHub Actions
        if ("true".equalsIgnoreCase(System.getenv("CI"))) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        // Load HTML file from Maven resources
        URL resource = getClass()
                .getClassLoader()
                .getResource("webpage/registration.html");

        if (resource == null) {
            throw new IllegalArgumentException(
                "registration.html not found!"
            );
        }

        driver.get(resource.toExternalForm());

        System.out.println("Registration page opened successfully!");
    }

    // Helper method to enter valid student details
    public void enterValidDetails() {

        driver.findElement(By.id("fullName"))
                .sendKeys("Sujan");

        driver.findElement(By.id("usn"))
                .sendKeys("4VV23CS001");

        driver.findElement(By.id("email"))
                .sendKeys("sujan@gmail.com");

        driver.findElement(By.id("department"))
                .sendKeys("AIML");
    }

    // TC01: Enter valid details and submit the form
    @Test
    public void validStudentRegistrationTest() {

        enterValidDetails();

        driver.findElement(By.id("registerButton")).click();

        String success = driver.findElement(
                By.id("successMessage")).getText();

        Assert.assertEquals(
                success,
                "Registration Successful!"
        );

        System.out.println("TC01: Valid registration submitted!");
    }

    // TC02: Submit form with empty fields
    @Test
    public void emptyFieldsValidationTest() {

        driver.findElement(By.id("registerButton")).click();

        String nameError = driver.findElement(
                By.id("nameError")).getText();

        String usnError = driver.findElement(
                By.id("usnError")).getText();

        String emailError = driver.findElement(
                By.id("emailError")).getText();

        String departmentError = driver.findElement(
                By.id("departmentError")).getText();

        Assert.assertEquals(nameError, "Name is required.");
        Assert.assertEquals(usnError, "USN is required.");
        Assert.assertEquals(emailError, "Email is required.");
        Assert.assertEquals(
                departmentError,
                "Please select a department."
        );

        System.out.println("TC02: Empty field validations verified!");
    }

    // TC03: Enter invalid email address
    @Test
    public void invalidEmailValidationTest() {

        driver.findElement(By.id("fullName"))
                .sendKeys("Sujan");

        driver.findElement(By.id("usn"))
                .sendKeys("4VV23CS001");

        driver.findElement(By.id("email"))
                .sendKeys("sujan123");

        driver.findElement(By.id("department"))
                .sendKeys("AIML");

        driver.findElement(By.id("registerButton")).click();

        String emailError = driver.findElement(
                By.id("emailError")).getText();

        Assert.assertEquals(
                emailError,
                "Please enter a valid email address."
        );

        System.out.println("TC03: Invalid email validation verified!");
    }

    // TC04: Verify registration success message
    @Test
    public void registrationSuccessMessageTest() {

        enterValidDetails();

        driver.findElement(By.id("registerButton")).click();

        WebElement message = driver.findElement(
                By.id("successMessage"));

        Assert.assertTrue(message.isDisplayed());

        Assert.assertEquals(
                message.getText(),
                "Registration Successful!"
        );

        System.out.println("TC04: Success message verified!");
    }

    // TC05: Verify form background colour
    @Test
    public void formBackgroundColorTest() {

        String backgroundColor = driver.findElement(
                By.id("registrationForm"))
                .getCssValue("background-color");

        System.out.println(
                "Form Background Color: " + backgroundColor
        );

        Assert.assertEquals(
                backgroundColor,
                "rgba(173, 216, 230, 1)"
        );

        System.out.println("TC05: Background colour verified!");
    }

    // Runs after every test case
    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
