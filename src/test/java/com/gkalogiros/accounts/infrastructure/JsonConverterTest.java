package com.gkalogiros.accounts.infrastructure;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonConverterTest {

    private static final LocalDate DATE = LocalDate.now();

    private static final String TEXT = "text";

    private static final Body BODY = new Body(TEXT, DATE);

    private static final String JSON = String.format(
            "{\"text\":\"%s\", \"date\":\"%s\"}", TEXT, DATE.format(DateTimeFormatter.BASIC_ISO_DATE)
    );
    private static final String NEW_LINES_AND_SPACES_REGEX = "\\s+";
    private static final String EMPTY = "";

    private JsonConverter underTest;

    @Before
    public void setUp()  {
        this.underTest = new JsonConverter();
    }

    @Test
    public void fromJson() {

        final Body body = underTest.fromJson(JSON, Body.class);

        assertThat(body).isEqualTo(BODY);
    }

    @Test
    public void toJson() {

        final String json = underTest.toJson(BODY);

        assertThat(cleanseJson(json)).isEqualTo(cleanseJson(JSON));

    }

    private String cleanseJson(final String json){
        return json.replaceAll(NEW_LINES_AND_SPACES_REGEX, EMPTY);
    }

    private static final class Body {
        private final String text;
        private final LocalDate date;


        private Body(final String text,
                     final LocalDate date) {
            this.text = text;
            this.date = date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Body body = (Body) o;

            if (text != null ? !text.equals(body.text) : body.text != null) return false;
            return date != null ? date.equals(body.date) : body.date == null;
        }

        @Override
        public int hashCode() {
            int result = text != null ? text.hashCode() : 0;
            result = 31 * result + (date != null ? date.hashCode() : 0);
            return result;
        }
    }

}