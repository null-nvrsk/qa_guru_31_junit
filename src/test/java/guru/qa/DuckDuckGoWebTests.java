package guru.qa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class DuckDuckGoWebTests {

    @BeforeEach
    void setUp() {
        open("https://duckduckgo.com/");
    }

    @ValueSource(strings = {
            "selenide", "junit 5", "Allure"
    })
    @ParameterizedTest(name = "Для поискового запроса \"{0}\" должен отдавать не пустой список карточек")
    @Tag("BLOCKER")
    void searchResultShouldNotBeEmpty(String searchQuery) {
        $("[name=q]").setValue(searchQuery).pressEnter();
        $$("[data-testid='result']")
                .shouldBe(sizeGreaterThan(0));
    }

//    @CsvSource(value = {
//            "selenide, https://selenide.org",
//            "junit 5, https://junit.org"
//    })
    @CsvFileSource(resources = "/test_data/searchResultShouldContainExpectedUrl.csv")
    @ParameterizedTest(name = "Для поискового запроса \"{0}\" в первой карточке должна быть ссылка {1}")
    @Tag("BLOCKER")
    void searchResultShouldContainExpectedUrl(String searchQuery, String expectedLink) {
        $("[name=q]").setValue(searchQuery).pressEnter();
        $("[data-layout='organic']").shouldHave(text(expectedLink));
    }

    @Test
    @Tag("BLOCKER")
    @DisplayName("Для поискового запроса 'selenide' должен отдаваться список картинок")
    void successfulSearchPhotoTest() {
        $("[name=q]").setValue("selenide").pressEnter();
        $$("[data-testid='duckbar'] a").find(text("Изображения")).click();


        $$("[data-testid='zci-images'] .tile--img")
                .shouldBe(sizeGreaterThan(0));
    }
}
