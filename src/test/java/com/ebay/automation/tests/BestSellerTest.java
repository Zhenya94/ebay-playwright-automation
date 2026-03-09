package com.ebay.automation.tests;

import com.ebay.automation.pages.ProductPage;
import com.ebay.automation.pages.SearchPage;
import com.ebay.automation.utils.BrowserManager;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BestSellerTest {
    private Page page;
    private SearchPage searchPage;
    private ProductPage productPage;

    private static final String SEARCH_KEYWORD = "wallet";
    private static final int MAX_BEST_SELLER_COUNT = 6;

//  SETUP: Runs ONCE before all tests in this class
    @BeforeClass
    public void setUp() {
        System.out.println("Setting up browser...");

        // Set headless = false so you can WATCH the browser.
        // Change to true when you want it to run in the background.
        page = BrowserManager.launchBrowser(false);

        searchPage = new SearchPage(page);
        productPage = new ProductPage(page);

        System.out.println("Browser launched successfully");
    }

    //  TEARDOWN: Runs ONCE after all tests in this class

    @AfterClass
    public void tearDown() {
        System.out.println("Closing browser...");
        BrowserManager.closeBrowser();
        System.out.println("Browser closed");
    }

    //  TC-001: Verify user can navigate to eBay and search
    // =========================================================
    @Test(priority = 1, description = "TC-001: Verify eBay homepage loads and search works")
    public void testEbaySearchNavigation() {
        System.out.println("\n--- TC-001: Testing eBay search navigation ---");

        searchPage.navigateToEbay();
        searchPage.searchFor(SEARCH_KEYWORD);

        String currentUrl = productPage.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);

        Assert.assertTrue(
                currentUrl.contains("ebay.com"),
                "TC-001 FAILED: URL should contain 'ebay.com'"
        );
        System.out.println("TC-001 PASSED: eBay search navigation works");
    }

    // =========================================================
    //  TC-002: Verify product page loads after clicking result
    // =========================================================
    @Test(priority = 2, description = "TC-002: Verify main product page loads correctly",
            dependsOnMethods = "testEbaySearchNavigation")
    public void testMainProductPageLoads() {
        System.out.println("\n--- TC-002: Testing main product page load ---");

        searchPage.clickFirstProduct();

        String currentUrl = productPage.getCurrentUrl();
        System.out.println("Product URL: " + currentUrl);

        Assert.assertTrue(
                currentUrl.contains("ebay.com"),
                "TC-002 FAILED: Should be on eBay product page"
        );

        String productTitle = productPage.getMainProductTitle();
        Assert.assertFalse(
                productTitle.isEmpty(),
                "TC-002 FAILED: Product title should not be empty"
        );

        System.out.println("TC-002 PASSED: Product page loaded. Title: " + productTitle);
    }

    // =========================================================
    //  TC-003: Verify Best Sellers section is visible
    // =========================================================
    @Test(priority = 3, description = "TC-003: Verify Best Sellers section is displayed on product page",
            dependsOnMethods = "testMainProductPageLoads")
    public void testBestSellerSectionIsVisible() {
        System.out.println("\n--- TC-003: Checking Best Sellers section visibility ---");

        productPage.scrollToRelatedSection();
        boolean isVisible = productPage.isBestSellerSectionVisible();

        Assert.assertTrue(
                isVisible,
                "TC-003 FAILED: Best Sellers section should be visible on product page"
        );
        System.out.println("TC-003 PASSED: Best Sellers section is visible");
    }


    //  TC-004: Verify max 6 best-seller products are displayed

    @Test(priority = 4, description = "TC-004: Verify up to 6 best seller products are shown",
            dependsOnMethods = "testBestSellerSectionIsVisible")
    public void testMaxSixBestSellerProducts() {
        System.out.println("\n--- TC-004: Checking best seller product count ---");

        int productCount = productPage.getRelatedProductCount();

        System.out.println("Best seller products found: " + productCount);

        // Must show at least 1
        Assert.assertTrue(
                productCount >= 1,
                "TC-004 FAILED: At least 1 best seller product should appear. Found: " + productCount
        );

        // Must NOT exceed 6
        Assert.assertTrue(
                productCount <= MAX_BEST_SELLER_COUNT,
                "TC-004 FAILED: Max " + MAX_BEST_SELLER_COUNT +
                        " best seller products allowed. Found: " + productCount
        );

        System.out.println("TC-004 PASSED: Product count " + productCount + " is within limit (1-6)");
    }


    //  TC-005: Verify product count is within limit (combined)

    @Test(priority = 5, description = "TC-005: Verify best seller product count is within acceptable range",
            dependsOnMethods = "testBestSellerSectionIsVisible")
    public void testProductCountWithinLimit() {
        System.out.println("\n--- TC-005: Validating product count within limit ---");

        boolean withinLimit = productPage.isProductCountWithinLimit();

        Assert.assertTrue(
                withinLimit,
                "TC-005 FAILED: Product count should be between 1 and 6"
        );
        System.out.println("TC-005 PASSED: Product count is within the acceptable limit");
    }


    //  TC-006: NEGATIVE — Verify section doesn't show 0 products

    @Test(priority = 6, description = "TC-006 NEGATIVE: Best seller section should not be empty",
            dependsOnMethods = "testBestSellerSectionIsVisible")
    public void testBestSellerSectionNotEmpty() {
        System.out.println("\n--- TC-006 (NEGATIVE): Checking section is not empty ---");

        int count = productPage.getRelatedProductCount();

        Assert.assertNotEquals(
                count, 0,
                "TC-006 FAILED: Best seller section is visible but has 0 products"
        );
        System.out.println("TC-006 PASSED: Best seller section is not empty");
    }


    //  TC-007: NEGATIVE — Verify count does NOT exceed 6

    @Test(priority = 7, description = "TC-007 NEGATIVE: More than 6 products should NOT appear",
            dependsOnMethods = "testBestSellerSectionIsVisible")
    public void testBestSellerDoesNotExceedLimit() {
        System.out.println("\n--- TC-007 (NEGATIVE): Ensuring count never exceeds 6 ---");

        int count = productPage.getRelatedProductCount();

        Assert.assertFalse(
                count > MAX_BEST_SELLER_COUNT,
                "TC-007 FAILED: " + count + " products shown — exceeds max limit of 6"
        );
        System.out.println("TC-007 PASSED: Count " + count + " does not exceed the max limit of 6");
    }
}
