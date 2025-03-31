package org.yc.gnosdrasil.gdboardscraperservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.SeleniumHelper;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class GlassdoorScraperImpl {
    private final SeleniumHelper seleniumHelper;

    public void scrape() {
        seleniumHelper.goToPage("https://www.glassdoor.com/Salaries/index.htm");

//        seleniumHelper.waitUntilElementLoadsByClassName("");
    }

    public void humanMoveMouse(int targetX, int targetY) {
        Actions actions = new Actions(seleniumHelper.getDriver());
        Random rand = new Random();
        int startX = 0, startY = 0; // Start from (0,0) or current position
        int steps = 50 + rand.nextInt(30); // Random steps (50-80)

        for (int i = 0; i < steps; i++) {
            // Calculate intermediate position with randomness
            double t = (double) i / steps;
            int currentX = (int) (startX + (targetX - startX) * t);
            int currentY = (int) (startY + (targetY - startY) * t);

            // Add random offsets
            currentX += rand.nextInt(5) - 2; // -2 to +2
            currentY += rand.nextInt(5) - 2;

            // Move to intermediate position
            actions.moveByOffset(currentX, currentY).pause(10 + rand.nextInt(20)).build().perform();
        }
    }

    public void humanClick(WebElement element) {
        Actions actions = new Actions(seleniumHelper.getDriver());
        Random rand = new Random();

        // Click with delay between mousedown and mouseup
        actions.clickAndHold(element)
                .pause(100 + rand.nextInt(150)) // 100-250ms delay
                .release()
                .build()
                .perform();
    }
}
