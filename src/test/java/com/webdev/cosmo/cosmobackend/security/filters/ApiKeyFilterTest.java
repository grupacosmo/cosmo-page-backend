package com.webdev.cosmo.cosmobackend.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webdev.cosmo.cosmobackend.error.ErrorResponseWriter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApiKeyFilterTest {

    private ApiKeyFilter filter(String apiKeys, boolean required) throws Exception {
        ApiKeyFilter filter = new ApiKeyFilter((Predicate<String>) key -> true, new ErrorResponseWriter(new ObjectMapper()));
        setField(filter, "apiKeys", apiKeys);
        setField(filter, "apiKeysRequired", required);
        return filter;
    }

    @Test
    void failsStartupWhenRequiredButNoKeysConfigured() throws Exception {
        ApiKeyFilter filter = filter("  ", true);

        assertThatThrownBy(filter::validateConfiguration)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("API_KEYS");
    }

    @Test
    void rejectsEveryRequestWhenRequiredButKeysAreBlank() throws Exception {
        ApiKeyFilter filter = filter("", true);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(new MockHttpServletRequest(), response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void passesThroughWhenApiKeysNotRequired() throws Exception {
        ApiKeyFilter filter = filter("", false);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(new MockHttpServletRequest(), response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void rejectsInvalidApiKeyWhenRequired() throws Exception {
        ApiKeyFilter filter = new ApiKeyFilter((Predicate<String>) key -> "secret".equals(key), new ErrorResponseWriter(new ObjectMapper()));
        setField(filter, "apiKeys", "secret");
        setField(filter, "apiKeysRequired", true);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("apiKey", "wrong");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(401);
    }

    private void setField(Object target, String name, Object value) throws Exception {
        Field field = ApiKeyFilter.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}