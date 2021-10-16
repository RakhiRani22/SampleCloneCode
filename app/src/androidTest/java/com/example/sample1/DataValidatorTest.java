package com.example.sample1;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import com.example.sample1.model.repoinfo.RepoInstance;
import com.example.sample1.util.DataValidator;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DataValidatorTest {
    @Test
    public void validateEmptyUserInput() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals(false, DataValidator.isUserInputValid(""));
    }

    @Test
    public void validateUserInput() {
        assertEquals(true, DataValidator.isUserInputValid("Rakhirani22"));
    }

    @Test
    public void validateRepositoryNotFound() {
        ArrayList<RepoInstance> list = new ArrayList<>();
        list.add(new RepoInstance("Sampl"));
        list.add(new RepoInstance("Samp"));
        list.add(new RepoInstance("Samplee"));
        assertEquals(false, DataValidator.isRepositoryFound(list, "Sample"));
    }

    @Test
    public void validateRepositoryFound() {
        ArrayList<RepoInstance> list = new ArrayList<>();
        list.add(new RepoInstance("Sampl"));
        list.add(new RepoInstance("Sample"));
        list.add(new RepoInstance("Samplee"));
        assertEquals(true, DataValidator.isRepositoryFound(list, "Sample"));
    }

    @Test
    public void validateRepositoryNotFoundWhenListIsEmptyFound() {
        ArrayList<RepoInstance> list = new ArrayList<>();
        assertEquals(false, DataValidator.isRepositoryFound(list, "Sample"));
    }

    @Test
    public void validateRepositoryNotFoundWhenListIsNull() {
        ArrayList<RepoInstance> list = null;
        assertEquals(false, DataValidator.isRepositoryFound(list, "Sample"));
    }
}
