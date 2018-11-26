package com.rudiklassen.autopad.shell;

import com.rudiklassen.autopad.services.HueControl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;


@RunWith(MockitoJUnitRunner.class)
public class HueShellControllerTest {

    @Mock
    private HueControl hueControlMock;

    private HueShellController testSubject;

    @Before
    public void setUp(){
        testSubject = new HueShellController(hueControlMock);
    }

    @Test
    public void commaSeparatedList() {
        String result = testSubject.toPrettyString(Arrays.asList(1L, 2L, 3L));
        Assertions.assertThat(result).isEqualTo("1, 2, 3");
    }

    @Test
    public void emptyCommaSeparatedList() {
        String result = testSubject.toPrettyString(new ArrayList<>());
        Assertions.assertThat(result).isEmpty();
    }
}