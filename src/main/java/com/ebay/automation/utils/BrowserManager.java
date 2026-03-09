package com.ebay.automation.utils;

import com.microsoft.playwright.*;

public class BrowserManager {
    private static BrowserContext context;
    private static Browser browser;
    private static Playwright playwright;


    public static Page launchBrowser(boolean b){
        Playwright playwright = Playwright.create();
        // To watch the browser run
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false) // To watch the browser run
        );
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(1280, 800)
                        .setUserAgent(
                                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                        "Chrome/120.0.0.0 Safari/537.36"
                        )
        );
        Page page = context.newPage();
        return page;
    }
    public static void closeBrowser() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
//        Page page = browser.newPage();
//        page.navigate("https://www.ebay.com/");



