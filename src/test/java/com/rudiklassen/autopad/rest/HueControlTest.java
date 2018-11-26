package com.rudiklassen.autopad.rest;

import com.rudiklassen.autopad.services.HueControl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HueControlTest {

    @Mock
    private HueRestService hueRestServiceMock;

    private HueControl testSubject;

    @Before
    public void setUp() {
        testSubject = new HueControl(hueRestServiceMock);
    }

    @Test
    public void returnsAvailableLightIds() throws IOException {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(hueRestServiceMock.getAllLightIds()).thenReturn(ids);

        List<Long> result = testSubject.getAllLightIds();
        assertThat(result).isEqualTo(ids);
    }

    @Test
    public void returnsEmptyListIfNoLightsAvailable() throws IOException {
        when(hueRestServiceMock.getAllLightIds()).thenReturn(new ArrayList<>());

        List<Long> result = testSubject.getAllLightIds();
        assertThat(result).isEmpty();
    }

    @Test
    public void switchLight() throws IOException {
        long lightId = 2L;
        boolean on = true;

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(hueRestServiceMock.getAllLightIds()).thenReturn(ids);

        testSubject.switchLight(lightId, on);
        verify(hueRestServiceMock).switchLight(2L, true);
    }

    @Test
    public void doNothingIfLightIdsCantBeFetched() throws IOException {
        long lightId = 1L;

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(hueRestServiceMock.getAllLightIds()).thenThrow(new IOException());

        testSubject.switchLight(lightId, true);
        verify(hueRestServiceMock, never()).switchLight(anyLong(), anyBoolean());
    }

    @Test
    public void throwExceptionIfLightIdIsNotAvailable() throws IOException {
        long lightId = 4L;

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(hueRestServiceMock.getAllLightIds()).thenReturn(ids);

        assertThatThrownBy(() -> {
            testSubject.switchLight(lightId, true);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The given lamps id could not be found");
    }

    @Test
    public void switchAllLightsOn() throws IOException {
        boolean on = true;

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(hueRestServiceMock.getAllLightIds()).thenReturn(ids);

        testSubject.switchAllLights( on);
        verify(hueRestServiceMock).switchLight(1L, true);
        verify(hueRestServiceMock).switchLight(2L, true);
        verify(hueRestServiceMock).switchLight(3L, true);
    }

    @Test
    public void switchAllLightsOff() throws IOException {
        boolean on = false;

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(hueRestServiceMock.getAllLightIds()).thenReturn(ids);

        testSubject.switchAllLights( on);
        verify(hueRestServiceMock).switchLight(1L, false);
        verify(hueRestServiceMock).switchLight(2L, false);
        verify(hueRestServiceMock).switchLight(3L, false);
    }

    @Test
    public void switchAllLightsDoNothingIfLightIdsCantBeFetched() throws IOException {
        boolean on = false;

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(hueRestServiceMock.getAllLightIds()).thenThrow(new IOException());

        testSubject.switchAllLights( on);
        verify(hueRestServiceMock, never()).switchLight(anyLong(), anyBoolean());
    }
}