
package com.udacity.jwdnd.course1.cloudstorage.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.JavascriptExecutor;

public class CredentialPageObj {
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialTab;

    @FindBy(id = "new-credential-button")
    private WebElement addCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement inputCredentialUrl;

    @FindBy(id = "credential-username")
    private WebElement inputCredentialUsername;

    @FindBy(id = "credential-password")
    private WebElement inputCredentialPassword;

    @FindBy(id = "credentialSubmit")
    private WebElement saveCredential;

    @FindBy(id = "credDisplayUrl")
    private WebElement credDisplayUrl;

    @FindBy(id = "credDisplayUsername")
    private WebElement credDisplayUsername;

    @FindBy(id = "credDisplayPassword")
    private WebElement credDisplayPassword;

    @FindBy(id = "credential-edit")
    private WebElement editCredential;

    @FindBy(id = "credential-delete")
    private WebElement deletCredential;

    private WebDriver driver;

    public CredentialPageObj(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /* Reduce repetitive code and actions using helper methods in the page object */
    public void openCredentialTab() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.credentialTab);
    }

    public void createCredential(String url, String username, String password) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.credentialTab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.addCredentialButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value ='" + url + "'", this.inputCredentialUrl);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "'", this.inputCredentialUsername);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "'", this.inputCredentialPassword);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.saveCredential);
    }

    public void viewCredential() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.editCredential);
    }

    public void editPassword(String password) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "'", this.inputCredentialPassword);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.saveCredential);
    }

    public void deleteCredential() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.credentialTab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.deletCredential);
    }

    public String getUrlText() {
        return credDisplayUrl.getAttribute("innerHTML");
    }

}
