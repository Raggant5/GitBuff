package use_case.log_workout.exercise_performed.delete_exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DeleteExerciseInteractorTest {

    @Test
    public void executePassesIdThroughToPresenter() {
        final DeleteExerciseOutputData[] captured = new DeleteExerciseOutputData[1];
        final DeleteExerciseOutputBoundary presenter = outputData -> captured[0] = outputData;

        new DeleteExerciseInteractor(presenter).execute(new DeleteExerciseInputData(9));

        assertEquals(9, captured[0].getId());
    }

    @Test
    public void executeWithTempNegativeIdPassesThroughUnchanged() {
        final DeleteExerciseOutputData[] captured = new DeleteExerciseOutputData[1];
        final DeleteExerciseOutputBoundary presenter = outputData -> captured[0] = outputData;

        new DeleteExerciseInteractor(presenter).execute(new DeleteExerciseInputData(-1));

        assertEquals(-1, captured[0].getId());
    }
}
