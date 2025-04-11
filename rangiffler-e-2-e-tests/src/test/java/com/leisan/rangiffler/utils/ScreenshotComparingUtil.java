package com.leisan.rangiffler.utils;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.OutputType;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.sleep;
import static io.qameta.allure.Allure.step;
import static ru.yandex.qatools.ashot.util.ImageTool.toByteArray;

public final class ScreenshotComparingUtil {

    private static final String EXPECTED_DIR = "src/test/resources/img/";

    private ScreenshotComparingUtil() {}

    @Step("Сделать скриншот элемента и проверить, что он соответствует ожидаемому")
    public static void takeElementScreenshot(String screenshotName, SelenideElement elementForScreenshot) {
        elementForScreenshot.shouldBe(visible);
        sleep(1000);
        step("Сравнить актуальный и ожидаемый скриншот с 'запасом прочности' в 10000", () ->
                compareElementScreenshotsWithMargin(getExpectedElementScreenshot(screenshotName),
                        getActualElementScreenshot(elementForScreenshot), 10_000));
    }

    public static BufferedImage getActualElementScreenshot(SelenideElement elementForScreenshot) throws IOException {
        File screenshot = elementForScreenshot.getScreenshotAs(OutputType.FILE);
        return ImageIO.read(screenshot);
    }

    public static BufferedImage getExpectedElementScreenshot(String screenshotName) throws IOException {
        return ImageIO.read(getExpectedFile(screenshotName));
    }

    private static File getExpectedFile(String screenshotName) {
        return new File(EXPECTED_DIR + screenshotName + "Expected.png");
    }

    /**
     * В этом методе сравниваются обрезанные скриншоты.
     * @param expectedImage     обрезанный ожидаемый скриншот
     * @param actualImage       обрезанный актуальный скриншот
     */
    public static void compareElementScreenshotsWithMargin(
            BufferedImage expectedImage, BufferedImage actualImage, int safetyMargin) throws IOException {
        ImageDiff diff = new ImageDiffer().makeDiff(expectedImage, actualImage);
        int size = diff.getDiffSize();

        if (size > safetyMargin) {
            Allure.addAttachment("Actual screenshot", new ByteArrayInputStream(toByteArray(actualImage)));
            Allure.addAttachment("Expected screenshot", new ByteArrayInputStream(toByteArray(expectedImage)));
            Allure.addAttachment("Comparing result", new ByteArrayInputStream(toByteArray(diff.getMarkedImage())));
            Assertions.fail("Actual and expected element screenshots are different, diff size is " + size);
        }

        Allure.addAttachment("Actual screenshot", new ByteArrayInputStream(toByteArray(actualImage)));
        Allure.addAttachment("Expected screenshot", new ByteArrayInputStream(toByteArray(expectedImage)));
    }
}
