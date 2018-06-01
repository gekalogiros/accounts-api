package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.infrastructure.JsonConverter;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;

public class RestExceptionHandlerTest {

    private static final String ERROR_FAILURE_MESSAGE = "{\"message\":\"FailureMessage\"}";
    private RestExceptionHandler underTest;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @Captor
    private ArgumentCaptor<Integer> statusCodeArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> bodyArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.underTest = new RestExceptionHandler(new JsonConverter(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void shouldConvertExceptionToErrorMessage() {
        doNothing().when(response).status(statusCodeArgumentCaptor.capture());
        doNothing().when(response).body(bodyArgumentCaptor.capture());

        underTest.handle(new RuntimeException("FailureMessage"), request, response);

        assertThat(statusCodeArgumentCaptor.getAllValues()).containsExactly(HttpStatus.BAD_REQUEST_400);

        final List<String> bodyArgumentsCaptured =
                bodyArgumentCaptor.getAllValues().stream().map(this::cleanse).collect(Collectors.toList());

        assertThat(bodyArgumentsCaptured).containsExactly(ERROR_FAILURE_MESSAGE);
    }

    private String cleanse(final String errorMessage){
        return errorMessage.replaceAll("\\s+", "");
    }
}