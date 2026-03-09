package com.ebay.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class ProductPage {
    //  CSS / XPath selectors for the Best Sellers / Related section
    private static final String BEST_SELLER_SECTION_HEADING =
            "//h2[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," +
                    "'abcdefghijklmnopqrstuvwxyz'),'best seller') or " +
                    "contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," +
                    "'abcdefghijklmnopqrstuvwxyz'),'related')]";

    private static final String RELATED_PRODUCT_ITEMS =
            "//section[contains(@class,'merch') or contains(@id,'merch')]" +
                    "//li[contains(@class,'item')]";
    private static final String PEOPLE_ALSO_VIEWED =
            "//h2[contains(text(),'People also viewed') or " +
                    "contains(text(),'Related Items')]";
    private static final String PRODUCT_CARDS =
            "(//*[contains(@class,'x-mweb-carousal__item') or " +
                    "contains(@class,'merch-item') or " +
                    "contains(@class,'ux-carousel-item')])[position() <= 6]";

    private final Page page;
    public ProductPage(Page page) {
        this.page = page;
    }

    public String getCurrentUrl() {
        return page.url();
    }
    //Scrolls down to the related products section
    public void scrollToRelatedSection() {
        // Scroll to 70% of the page
        page.evaluate("window.scrollTo(0, document.body.scrollHeight * 0.7)");
        page.waitForTimeout(2000); // wait 2 seconds for content to load
        System.out.println("Scrolled to related products section");
    }
    // Check Related section heading is visible
    public boolean isBestSellerSectionVisible() {
        try {
            // Try primary selector
            Locator heading = page.locator(BEST_SELLER_SECTION_HEADING).first();
            if (heading.isVisible()) {
                System.out.println("Best Seller section found: " + heading.innerText());
                return true;
            }
        } catch (Exception e) {
            System.out.println("Primary selector not found, trying fallback...");
        }

        // Try fallback selector
        try {
            Locator fallback = page.locator(PEOPLE_ALSO_VIEWED).first();
            if (fallback.isVisible()) {
                System.out.println("Related section found (fallback): " + fallback.innerText());
                return true;
            }
        } catch (Exception e) {
            System.out.println("Related section not found");
        }

        return false;
    }
    //Returns the count of related product cards displayed

    public int getRelatedProductCount() {
        scrollToRelatedSection();

        try {
            List<Locator> items = page.locator(PRODUCT_CARDS).all();
            System.out.println("Found " + items.size() + " related product cards");
            return items.size();
        } catch (Exception e) {
            System.out.println("Could not count related products: " + e.getMessage());
            return 0;
        }
    }
    //Checks that the number of related product products

    public boolean isProductCountWithinLimit() {
        int count = getRelatedProductCount();
        System.out.println("Related product count: " + count);
        return count >= 1 && count <= 6;
    }
    //Gets the title of the main product being viewed
    public String getMainProductTitle() {
        try {
            String title = page.locator("h1.x-item-title__mainTitle").innerText();
            System.out.println("Main product title: " + title);
            return title;
        } catch (Exception e) {
            // Fallback
            try {
                return page.locator("h1").first().innerText();
            } catch (Exception ex) {
                return "";
            }
        }
    }
    //Gets the main product's price text.

    public String getMainProductPrice() {
        try {
            return page.locator(".x-price-primary").first().innerText();
        } catch (Exception e) {
            return "0";
        }
    }
}
