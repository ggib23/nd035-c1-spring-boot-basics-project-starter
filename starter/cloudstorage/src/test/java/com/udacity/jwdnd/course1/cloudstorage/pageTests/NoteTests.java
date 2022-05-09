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

import com.udacity.jwdnd.course1.cloudstorage.pageObjects.NotePageObj;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private NotePageObj notePage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();

		notePage = new NotePageObj(driver);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
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
	private void addNote() {
		// Attempt to create new note
		notePage.createNote("HelloWorld!", "This is my first note.");

		// Redirect home from result page
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * Test that creates a note, and verifies it is displayed
	 **/
	@Test
	public void createNote() {
		// Create a test account and login
		doMockSignUp("Temporary","User4","TU4","123");
		doLogIn("TU4", "123");

		// Attempt to create new note
		this.addNote();
		
		// Verify that it is displayed
		notePage.openNoteTab();

		WebElement noteDisplayTitle = driver.findElement(By.id("noteDisplayTitle"));
		Assertions.assertEquals("HelloWorld!", noteDisplayTitle.getAttribute("innerText"));

	}

	/**
	 * Test that edits an existing note and verifies that the changes are displayed
	 **/
	@Test
	public void editNote() {
		// Create a test account and login
		doMockSignUp("Temporary","User5","TU5","123");
		doLogIn("TU5", "123");

		// Attempt to create new note
		this.addNote();

		// Attempt to edit new note
		notePage.openNoteTab();
		notePage.editNote("This is a change to my original note.");

		// Redirect home from result page
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// Verify the changes are displayed
		WebElement noteDisplayDesc = driver.findElement(By.id("noteDisplayDesc"));
		Assertions.assertEquals("This is a change to my original note.", noteDisplayDesc.getAttribute("innerText"));
	
	}

	/**
	 * Test that deletes a note and verifies that the note is no longer displayed
	 **/
	@Test
	public void deleteNote() {
		// Create a test account and login
		doMockSignUp("Temporary","User6","TU6","123");
		doLogIn("TU6", "123");

		// Attempt to create new note
		this.addNote();

		// Attempt to delete new note
		notePage.openNoteTab();
		notePage.deleteNote();

		// Redirect home from result page
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// Verify the changes are displayed
		Assertions.assertThrows(NoSuchElementException.class, notePage::getTitleText);
		
	}

}
