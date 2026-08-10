package interface_adapter.nutrition;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MealRecommendationDisplayDataTest {

    private static final int READY_IN_MINUTES = 25;

    @Test
    void gettersReturnConstructedValues() {
        final MealRecommendationDisplayData data = new MealRecommendationDisplayData(
                "Grilled Chicken Salad", READY_IN_MINUTES, "http://example.com/recipe");

        assertEquals("Grilled Chicken Salad", data.getTitle());
        assertEquals(READY_IN_MINUTES, data.getReadyInMinutes());
        assertEquals("http://example.com/recipe", data.getSourceUrl());
    }
}
