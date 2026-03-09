package com.ebay.automation.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public class SearchPage {
    private static final String SEARCH_INPUT = "#gh-ac";
    private static final String SEARCH_BUTTON = "#gh-search-btn";
    //private static final String FIRST_RESULT = "(//li[contains(@class,'s-item')]//a[contains(@class,'s-item__link')])[1]";

//            "(//li[contains(@class,'s-item')]//img[contains(@class,'s-item__image-img')])[1]";

    private Page page = null;
    public SearchPage(Page page) { //create constructor
        this.page = page;
    }
    public void navigateToEbay(){
       page.navigate("https://www.ebay.com");
       System.out.println("Navigated to ebay");
    }
    public void searchFor(String keyword) {
        page.locator(SEARCH_INPUT).fill(keyword);
        page.locator(SEARCH_BUTTON).click();
        page.waitForSelector(".srp-results");

        System.out.println("Searched for: " + keyword);
    }
    public void clickFirstProduct() {
//        page.locator(FIRST_PRODUCT_LINK).first().click();
//        page.waitForLoadState();
//        System.out.println("Clicked on the first product");
//

        String selector = ".s-item__link";

        // Wait for the page to load completely
        page.waitForLoadState(LoadState.NETWORKIDLE);

        page.waitForSelector(selector);

        // Check for the count of elements found
        int count = page.locator(selector).count();
        System.out.println("Found " + count + " elements matching " + selector);

        if (count > 0) {
            page.locator(selector).first().click();
            System.out.println("Clicked on the first product link.");
        } else {
            System.out.println("No products found.");
        }

    }


}
