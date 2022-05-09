package com.udacity.jwdnd.course1.cloudstorage.pageTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import org.openqa.selenium.NoSuchElementException;

import com.udacity.jwdnd.course1.cloudstorage.pageObjects.CredentialPageObj;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private CredentialPageObj credentialPage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();

		credentialPage = new CredentialPageObj(driver);
	}

	@AfterEach
	public void afterEach() throws InterruptedException {
		if (this.driver != null) {
            Thread.sleep(2000);
			driver.quit();
		}
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogOut()
	{
		// Logout of the dummy account
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
		WebElement logoutButton = driver.findElement(By.id("logout-button"));
		logoutButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Login"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void addCredential() {
		// Attempt to create new credential
		credentialPage.createCredential("https://www.google.com/", "testUser", "1234");

		// Redirect home from result page
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * Test that creates a set of credentials, verifies that they are displayed, 
     * and verifies that the displayed password is encrypted
	 **/
	@Test
	public void createCredential() {
		// Create a test account and login
		doMockSignUp("Temporary","User1","TU1","123");
		doLogIn("TU1", "123");

		// Attempt to create new credential
		this.addCredential();
		
		// Verify that it is displayed
		credentialPage.openCredentialTab();

		WebElement credDisplayUrl = driver.findElement(By.id("credDisplayUrl"));
		Assertions.assertEquals("https://www.google.com/", credDisplayUrl.getAttribute("innerText"));

        WebElement credDisplayUsername = driver.findElement(By.id("credDisplayUsername"));
		Assertions.assertEquals("testUser", credDisplayUsername.getAttribute("innerText"));

        WebElement credDisplayPassword = driver.findElement(By.id("credDisplayPassword"));
		Assertions.assertNotEquals("1234", credDisplayPassword.getAttribute("innerText"));

	}

	/**
	 * Test that views an existing set of credentials, verifies that the viewable password is unencrypted, 
     * edits the credentials, and verifies that the changes are displayed
	 **/
	@Test
	public void editCredential() {
		// Create a test account and login
		doMockSignUp("Temporary","User2","TU2","123");
		doLogIn("TU2", "123");

		// Attempt to create new credential
		this.addCredential();

		// Attempt to view existing credential
		credentialPage.openCredentialTab();
		credentialPage.viewCredential();
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2 );
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));

        // Verifies that the viewable password is unencrypted
        WebElement viewPassword = driver.findElement(By.id("credential-password"));
		System.out.println(viewPassword.getAttribute("value"));
        Assertions.assertEquals("1234", viewPassword.getAttribute("value"));

		// Attempts to edit the credentials
		credentialPage.editPassword("new1234");

        // Redirect home from result page
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// Verify the changes are displayed
        credentialPage.openCredentialTab();
		credentialPage.viewCredential();
		WebElement viewNewPassword = driver.findElement(By.id("credential-password"));
		Assertions.assertEquals("new1234", viewNewPassword.getAttribute("value"));
	
	}

	/**
	 * Test that deletes an existing set of credentials and verifies that the credentials 
     * are no longer displayed
	 **/
	@Test
	public void deleteCredential() {
		// Create a test account and login
		doMockSignUp("Temporary","User3","TU3","123");
		doLogIn("TU3", "123");

		// Attempt to create new credential
		this.addCredential();

		// Attempt to delete new credential
		credentialPage.openCredentialTab();
		credentialPage.deleteCredential();

		// Redirect home from result page
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// Verify the changes are displayed
		Assertions.assertThrows(NoSuchElementException.class, credentialPage::getUrlText);
		
	}

}
