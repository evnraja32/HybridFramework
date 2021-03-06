package com.tad.framework.new_tours.pages;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

public class Scenario3_Amazon {

	public static String result = null;
	public static String stepDesc = null;
	public static String PASS = "PASS", FAIL = "FAIL";
	public static ArrayList<String> resultArray = new ArrayList<String>();

	public static RemoteWebDriver driver = null;
	public static WebElement element = null;
	public static FluentWait<RemoteWebDriver> wait = null;

	/**
	 * 
	 */
	public static void main(String[] args) {

		Scenario3_Amazon app = new Scenario3_Amazon();
		try {
			// 1. Open Amazon.in website
			app.laucnApp("https://www.amazon.in/");
			app.takeScreenShot("1");

			// 2) Click on All and Choose Electronics
			app.selectListItem("Electronics");
			app.takeScreenShot("2");

			// 3) Type Smart TV on search bar and choose 40 inches in the search display
			// list
			app.findElement("id", "twotabsearchtextbox");
			app.typeText("Smart TV");
			app.takeScreenShot("3.1");
			app.findElement("xpath", "//div[@id='suggestions']//span[contains(text(), '40 inches')]");
			app.clickElement();

			// 4) Confirm the result displays on 40 inches and get the overall count
			// spotting the search results then pointing to the parent to get the complete
			// string : 1-24 of 191 results for Electronics : "smart tv 40 inches"

			app.checkFor40InchesTextInSearchResults();

			/*
			 * 5) Find the brand name of TV that is displayed in Page 1 a. With highest
			 * price (if there are multiple, print the first matching result) b. With lowest
			 * discount price (if there are multiple, print the last matching result)
			 */

			List<WebElement> brandNames = app.findElements("xpath",
					"//div[@class='sg-col-inner']//span[@class='a-size-medium a-color-base a-text-normal']");
			List<WebElement> price = app.findElements("xpath",
					"//div[@class='sg-col-inner']//span[@class='a-price-whole']");
			List<WebElement> discount = app.findElements("xpath",
					"//div[@class='sg-col-inner']//span[contains(text(), '%)')]");

			stepDesc = ""; // reset of step description
			String highestBrandName = "";
			int hightestPrice = 0;
			String lowestBrandName = "";
			int lowestDiscount = 100;

			ListIterator<WebElement> itBrand = brandNames.listIterator();
			ListIterator<WebElement> itPrice = price.listIterator();
			ListIterator<WebElement> itDiscount = discount.listIterator();

			while (itBrand.hasNext() && itPrice.hasNext() && itDiscount.hasNext()) {
				String brandName = itBrand.next().getText();
				int priceValue = Integer.parseInt(itPrice.next().getText().replaceAll(",", ""));
				String temp = itDiscount.next().getText().split("(")[1].split("%")[0];
				int discountValue = Integer.parseInt(temp);
				if (priceValue > hightestPrice) {
					hightestPrice = priceValue;
					highestBrandName = brandName;
				}
				if (discountValue < lowestDiscount) {
					lowestDiscount = discountValue;
					lowestBrandName = brandName;
				}
			}

			app.reportResult(PASS, "Highest brand value : " + highestBrandName);
			app.reportResult(PASS, "Lowest brand value : " + lowestBrandName);
			app.takeScreenShot("5");

			// 6) Choose the Avg Customer Review Option from the Sorting list
			app.findElement("css", "span.a-button-text.a-declarative");
			app.clickElement();
			app.takeScreenShot("6.1");

			app.findElement("xpath", "//*[contains(text(), 'Avg. Customer Review')]");
			app.clickElement();
			app.takeScreenShot("6.2");

			// 7) From the left filter panel, choose the brand – Samsung
			app.findElement("xpath",
					"//ul[@class='a-unordered-list a-nostyle a-vertical a-spacing-medium']//li[@aria-label='Samsung']");
			app.clickElement();
			app.takeScreenShot("7");

			// 8) Confirm the display list contains only 40 inches, if else -> print error
			// message with names of the TV that has different inches
			app.checkFor40InchesTextInSearchResults();
			app.takeScreenShot("8");

			// 9) Click on the first resulting TV in search list
			app.findElement("xpath",
					"//div[@class='sg-col-inner']//span[@class='a-size-medium a-color-base a-text-normal']");
			String brandName = element.getText();
			app.clickElement();

			// 10) Confirm a new window is opened with the title matching the TV name
			String parentWindow = driver.getWindowHandle();

			for (String window : driver.getWindowHandles()) {
				driver.switchTo().window(window);
			}
			app.takeScreenShot("10");

			String result = driver.getTitle().contains(brandName) ? PASS : FAIL;
			app.reportResult(result, "Confirm a new window is opened with the title matching the TV name");

			// 11) Mouse over on the EMI Options
			app.findElement("link", "EMI Options");
			app.clickElement();
			app.takeScreenShot("11");

			// 12) Click on the Switch
			app.findElement("css", "div.a-switch.a-declarative > a");
			app.clickElement();
			app.takeScreenShot("12");

			// 13) Click on Amazon Pay ICICI card and find the lowest interest rate’s EMI
			// Plan
			app.findElement("xpath", "//p[contains(text(), 'Amazon Pay ICICI Card')]");
			app.clickElement();
			app.takeScreenShot("13");

			// 14) Go back to main window and clear existing search and type “Speakers with
			// Bluetooth”
			driver.close();
			driver.switchTo().window(parentWindow);
			app.findElement("id", "twotabsearchtextbox");
			app.typeText("Speakers with Bluetooth\n");
			app.takeScreenShot("14");

			// 15) Select Today’s deal and New from the filters
			app.findElement("xpath",
					"//ul[@class='a-unordered-list a-nostyle a-vertical a-spacing-medium']//li[@aria-label='Today\' Deals']");
			app.clickElement();
			app.findElement("link", "New");
			app.clickElement();
			app.takeScreenShot("15");

			// 16) Click on the last result of the first page
			brandNames = app.findElements("xpath",
					"//div[@class='sg-col-inner']//span[@class='a-size-medium a-color-base a-text-normal']");
			element = brandNames.get(brandNames.size() - 1);
			app.clickElement();
			app.takeScreenShot("16");

			// 17) Go to the newly Opened tab and Check if Add to Cart is enabled
			parentWindow = driver.getWindowHandle();

			for (String window : driver.getWindowHandles()) {
				driver.switchTo().window(window);
			}
			app.takeScreenShot("17");

			// 18) Click on Add to Cart
			app.findElement("xpath", "//a[contains(text(), 'Add to Cart')]");
			app.clickElement();
			app.takeScreenShot("18");

			// 19) Confirm Login Page is displayed
			result = driver.getTitle().contains("Amazon Sing In") ? PASS : FAIL;
			app.takeScreenShot("19");
			app.reportResult(result, "Confirm Login Page is displayed");

		} catch (Exception e) {

			app.printException(e, stepDesc);
		} finally {
			String finalResult = (resultArray.contains(FAIL) ? FAIL : PASS);

			// 20) Close all the opened browsers
			app.closeApp();
			app.reportResult(finalResult, "=== Final Result ===");
		}

	}

	/**
	 * Use this method verify the 40 inches text in the search result
	 */
	public void checkFor40InchesTextInSearchResults() throws Exception {
		findElement("xpath", "//span[contains(text(), 'results for')]/..");
		String searchResult = element.getText();
		String overAllCount = searchResult.split(" ")[2];
		System.out.println("Overall search result count : " + overAllCount);
		String verificationStepDesc = "Verifying the string : '40 inches' presence in the search result";
		if (searchResult.contains("40 inches"))
			reportResult(PASS, verificationStepDesc);
		else
			reportResult(FAIL, verificationStepDesc);
	}

	/**
	 * Use this method to launch the web application.
	 * 
	 * @param browser pass the value to choose the browser to automate the app
	 * @param url     pass the application url her.
	 */
	public void laucnApp(String url) throws Exception {
		stepDesc = "Launching web application: " + url;
		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
		driver = new FirefoxDriver();

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		wait = new FluentWait<RemoteWebDriver>(driver).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(5));

		driver.get(url);
		reportResult(PASS, stepDesc);
	}

	/**
	 * Closes the browser
	 */
	public void closeApp() {
		driver.close();
	}

	/**
	 * creates a By object to be used inside findElement(s) method
	 * 
	 * @param locatorType id, css, class, link, tag, xpath
	 * @param url         pass the application url here.
	 */
	private By getBy(String locatorType, String locatorValue) throws Exception {

		stepDesc = "WebElement: [" + locatorType + ", " + locatorValue + "]";
		By by = null;
		switch (locatorType) {
		case "id":
			by = By.id(locatorValue);
			break;
		case "css":
			by = By.cssSelector(locatorValue);
			break;
		case "class":
			by = By.className(locatorValue);
			break;
		case "link":

			// use of partial link text can address the dual requirements of link and
			// partial link
			by = By.partialLinkText(locatorValue);
			break;
		case "tag":
			by = By.tagName(locatorValue);
			break;
		case "xpath":
			by = By.xpath(locatorValue);
			break;
		default:
			throw new InvalidArumentExceptions(locatorType);
		}
		return by;
	}

	/**
	 * finds the web element
	 * 
	 * @param locatorType id, css, class, link, tag, xpath
	 * @param url         pass the application url here.
	 */
	public void findElement(String locatorType, String locatorValue) throws Exception {
		element = driver.findElement(getBy(locatorType, locatorValue));
	}

	/**
	 * finds the web elements
	 * 
	 * @param locatorType id, css, class, link, tag, xpath
	 * @param url         pass the application url here.
	 */
	public List<WebElement> findElements(String locatorType, String locatorValue) throws Exception {
		return driver.findElements(getBy(locatorType, locatorValue));
	}

	/**
	 * This method can be used to type text
	 * 
	 * @param fieldName  the label name of the field
	 * @param textToType text to be entered into the field
	 */
	public void typeText(String textToType) throws Exception {
		stepDesc = "Type text: '" + textToType + "'";
		element.sendKeys(textToType);
		reportResult(PASS, stepDesc);
	}

	/**
	 * clicks on element
	 */
	public void clickElement() {
		stepDesc += " | Click on web element";
		element.click();
		reportResult(PASS, stepDesc);
	}

	/**
	 * selects an element from the drop down list
	 * 
	 * @param dropDownText visible text in the drop down
	 * @throws Exception
	 */
	public void selectListItem(String dropDownText) throws Exception {
		findElement("id", "searchDropdownBox");
		stepDesc += " | Selecting dd option: '" + dropDownText + "'";
		Select dd = new Select(element);
		dd.selectByVisibleText(dropDownText);
		reportResult(PASS, stepDesc);
	}

	/**
	 * prints the exception and makes the result as FAIL
	 * @param e Throwable's object
	 * @param errorMessage
	 */
	public void printException(Throwable e, String errorMessage) {
		stepDesc += "\tError: " + e.getMessage();
		reportResult(FAIL, stepDesc);
	}

	/**
	 * helps to capture a screenshot
	 * @param stepId the id of the step
	 */
	public void takeScreenShot(String stepId) {
		String imageName = "" + System.currentTimeMillis() + "";
		try {
			FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE),
					new File("./target/" + stepId + ".png"));

		} catch (IOException e) {
			printException(e, "Failed to take Screen Shot: " + imageName);
		}

	}

	private void reportResult(String result, String stepDesc) {

		resultArray.add(result);

		String resultMsg = "[" + result + "]::" + stepDesc;

		if (result != PASS) {
			System.err.println(resultMsg);
		} else {
			System.out.println(resultMsg);
		}
	}

}

@SuppressWarnings("serial")
class InvalidArumentExceptions extends Exception {

	public InvalidArumentExceptions(String argument) {
		super("Invalid Arugument: '" + argument + "'");
	}
}
