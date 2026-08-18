package com.webdev.cosmo.cosmobackend.contract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapi4j.core.model.v3.OAI3;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.model.v3.OpenApi3;
import org.openapi4j.parser.model.v3.Schema;
import org.openapi4j.schema.validator.ValidationContext;
import org.openapi4j.schema.validator.ValidationData;
import org.openapi4j.schema.validator.v3.SchemaValidator;

import java.net.URL;

public final class OpenApiContract {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final OpenApi3 API = loadSpec();

    private OpenApiContract() {
    }

    public static void assertValid(String schemaName, String jsonBody) {
        Schema schema = API.getComponents().getSchema(schemaName);
        if (schema == null) {
            throw new AssertionError("Schema '" + schemaName + "' not found in schemas.yml");
        }
        try {
            JsonNode node = MAPPER.readTree(jsonBody);
            ValidationData<?> data = new ValidationData<>();
            ValidationContext<OAI3> context = new ValidationContext<>(API.getContext());
            new SchemaValidator(context, schemaName, schema.toNode()).validate(node, data);
            if (!data.results().isValid()) {
                throw new AssertionError("Response does not match contract '" + schemaName + "': " + data.results());
            }
        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            throw new AssertionError("Failed to validate response against contract '" + schemaName + "': " + e, e);
        }
    }

    private static OpenApi3 loadSpec() {
        URL specUrl = OpenApiContract.class.getResource("/schemas.yml");
        if (specUrl == null) {
            throw new IllegalStateException("schemas.yml not found on classpath");
        }
        try {
            return new OpenApi3Parser().parse(specUrl, null, false);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load schemas.yml", e);
        }
    }
}