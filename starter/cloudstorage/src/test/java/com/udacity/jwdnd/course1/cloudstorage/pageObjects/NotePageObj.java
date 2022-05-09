package com.udacity.jwdnd.course1.cloudstorage.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.JavascriptExecutor;

public class NotePageObj {
    @FindBy(id = "nav-notes-tab")
    private WebElement noteTab;

    @FindBy(id = "new-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    private WebElement inputNoteDesc;

    @FindBy(id = "noteSubmit")
    private WebElement saveNote;

    @FindBy(id = "noteDisplayTitle")
    private WebElement noteDisplayTitle;

    @FindBy(id = "noteDisplayDesc")
    private WebElement noteDisplayDesc;

    @FindBy(id = "note-edit")
    private WebElement editNote;

    @FindBy(id = "note-delete")
    private WebElement deletNote;

    private WebDriver driver;

    public NotePageObj(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /* Reduce repetitive code and actions using helper methods in the page object */
    public void openNoteTab() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTab);
    }

    public void createNote(String title, String description) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.addNoteButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value ='" + title + "'", this.inputNoteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description + "'", this.inputNoteDesc);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.saveNote);
    }

    public void editNote(String description) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.editNote);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description + "'", this.inputNoteDesc);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.saveNote);
    }

    public void deleteNote() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.deletNote);
    }

    public String getTitleText() {
        return noteDisplayTitle.getAttribute("innerHTML");
    }

}
