**Project Overview**

This project automates the validation of the Best Seller / Related Products section on an eBay product page using Playwright (Java) with TestNG.

The automation framework follows the Page Object Model (POM) design pattern to keep test code modular, maintainable, and scalable.

The test suite performs the following actions:

Navigate to the eBay homepage
Search for a product
Open the first product from the search results
Verify the Best Seller Products section
Validate the number of related products displayed

Technologies Used :

Java
Playwright for Java
TestNG
Maven
Page Object Model (POM)
IntelliJ IDEA

Test Scenarios
TC-001 - Verify that the user can navigate to the eBay homepage and perform a product search.

TC-002 - Verify that the product page loads successfully after clicking a search result.

TC-003 - Verify that the Best Seller / Related Products section is visible on the product page.

TC-004 - Verify that a maximum of 6 related products are displayed.

TC-005 - Verify that the number of related products is within the valid range (1–6).

TC-006 (Negative Test) - Verify that the Best Seller section is not empty.

TC-007 (Negative Test) - Verify that the number of related products does not exceed 6.
