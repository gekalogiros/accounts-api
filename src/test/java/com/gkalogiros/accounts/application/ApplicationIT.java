package com.gkalogiros.accounts.application;

import com.gkalogiros.accounts.domain.TransferStatus;
import com.gkalogiros.accounts.web.rest.AccountResponseBody;
import com.gkalogiros.accounts.web.rest.TransferResponseBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ApplicationIT {

    private static final String ACCOUNT_ENDPOINT = "/accounts";
    private static final String GET_ACCOUNT_ENDPOINT = "/accounts/{account}";
    private static final String GET_TRANSFER_ENDPOINT = "/accounts/{account}/transfers/{transfer}";
    private static final String TRANSFERS_ENDPOINT = "/accounts/{account}/transfers";

    private static final String TRANSFER_PAYLOAD_TEMPLATE = "{\"externalReference\":\"%s\", \"targetAccount\":\"%s\", \"amount\":\"%s\"}";
    private static final String ACCOUNT_PAYLOAD_TEMPLATE = "{\"initialBalance\":\"%s\"}";

    private static final BigDecimal _53_2 = new BigDecimal("53.2");
    private static final BigDecimal _82_5 = new BigDecimal("82.5");
    private static final BigDecimal _12_5 = new BigDecimal("12.5");
    private static final BigDecimal _8_5 = new BigDecimal("8.5");

    private static final boolean DISABLE_TRANSFERS_CONSUMER = false;

    private static final String JSON_FIELD_UUID = "uuid";
    private static final String JSON_FIELD_BALANCE = "balance";
    private static final String JSON_FIELD_TO = "to";
    private static final String JSON_FIELD_EXTERNAL_REFERENCE = "externalReference";
    private static final String JSON_FIELD_STATUS = "status";
    private static final String JSON_FIELD_AMOUNT = "amount";
    private static final String JSON_ROOT = "$";

    private static Application underTest = new Application(DISABLE_TRANSFERS_CONSUMER);

    @BeforeClass
    public static void setUp() throws Exception {
        RestAssured.port = underTest.run(0);
        RestAssured.config = newConfig().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        underTest.stop();
    }

    @Test
    public void shouldCreateAccount() {
        createAccountWithBalance(_53_2);
    }

    @Test
    public void shouldRetrieveAllAccounts() {

        final AccountResponseBody account = createAccountWithBalance(_53_2);

        when()
            .get(ACCOUNT_ENDPOINT)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$", hasSize(1))
            .body("[0]", hasKey(JSON_FIELD_UUID))
            .body("[0].uuid", is(account.uuid))
            .body("[0]", hasKey(JSON_FIELD_BALANCE))
            .body("[0].balance", is(account.balance));
    }

    @Test
    public void shouldRetrieveAccountForUUID() {

        final AccountResponseBody account = createAccountWithBalance(_53_2);

        when()
            .get(GET_ACCOUNT_ENDPOINT, account.uuid)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$", hasKey(JSON_FIELD_UUID))
            .body(JSON_FIELD_UUID, is(account.uuid))
            .body("$", hasKey(JSON_FIELD_BALANCE))
            .body(JSON_FIELD_BALANCE, is(account.balance));
    }


    @Test
    public void shouldCreateTransferForAccount() {

        final AccountResponseBody account1 = createAccountWithBalance(_82_5);

        final AccountResponseBody account2 = createAccountWithBalance(_53_2);

        createTransferWithAmount(account1.uuid, account2.uuid, _12_5);
    }

    @Test
    public void shouldRetrieveTransferAssociatedWithAccount() {

        final AccountResponseBody account1 = createAccountWithBalance(_53_2);

        final AccountResponseBody account2 = createAccountWithBalance(_82_5);

        final TransferResponseBody transfer = createTransferWithAmount(account1.uuid, account2.uuid, _8_5);

        when()
            .get(GET_TRANSFER_ENDPOINT, account1.uuid, transfer.uuid)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body(JSON_ROOT, hasKey(JSON_FIELD_UUID))
            .body("$", hasKey(JSON_FIELD_TO))
            .body("$", hasKey(JSON_FIELD_EXTERNAL_REFERENCE))
            .body("$", hasKey(JSON_FIELD_STATUS))
            .body("$", hasKey(JSON_FIELD_AMOUNT))
            .body(JSON_FIELD_UUID, is(transfer.uuid))
            .body(JSON_FIELD_TO, is(account2.uuid))
            .body(JSON_FIELD_EXTERNAL_REFERENCE, is(transfer.externalReference))
            .body(JSON_FIELD_STATUS, is(TransferStatus.PENDING.name()))
            .body(JSON_FIELD_AMOUNT, is(_8_5));
    }

    @Test
    public void shouldRetrieveTransfersAssociatedWithAccount() {

        final AccountResponseBody account1 = createAccountWithBalance(_53_2);

        final AccountResponseBody account2 = createAccountWithBalance(_82_5);

        final TransferResponseBody transfer = createTransferWithAmount(account1.uuid, account2.uuid, _8_5);

        when()
            .get(TRANSFERS_ENDPOINT, account1.uuid)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$", hasSize(1))
            .body(JSON_FIELD_UUID, containsInAnyOrder(transfer.uuid))
            .body(JSON_FIELD_TO, containsInAnyOrder(account2.uuid))
            .body(JSON_FIELD_EXTERNAL_REFERENCE, containsInAnyOrder(transfer.externalReference))
            .body(JSON_FIELD_STATUS, containsInAnyOrder(transfer.status))
            .body(JSON_FIELD_AMOUNT, containsInAnyOrder(transfer.amount));
    }

    @Test
    public void shouldReturnBadRequestWhenInvalidInformationProvided() {
        given()
            .accept(ContentType.JSON)
            .body(String.format(ACCOUNT_PAYLOAD_TEMPLATE, -10L))
        .when()
            .post(ACCOUNT_ENDPOINT)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("$", hasKey("message"))
            .body("message", is("Initial Balance must be greater or equal to zero"));
    }

    @Test
    public void shouldReturnNotFoundWhenBusinessEntityDoesNotExist() {
        when()
            .post(GET_ACCOUNT_ENDPOINT, UUID.randomUUID())
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    private static AccountResponseBody createAccountWithBalance(final BigDecimal balance) {

        String json = String.format(ACCOUNT_PAYLOAD_TEMPLATE, balance.doubleValue());

        return given()
                    .accept(ContentType.JSON).body(json)
                .when()
                    .post(ACCOUNT_ENDPOINT)
                .then()
                    .log().ifError()
                    .statusCode(HttpStatus.SC_OK)
                    .body("$", hasKey(JSON_FIELD_UUID))
                    .body("$", hasKey(JSON_FIELD_BALANCE))
                    .body(JSON_FIELD_BALANCE, is(balance))
                    .extract()
                    .response()
                    .as(AccountResponseBody.class);
    }

    private static TransferResponseBody createTransferWithAmount(final String sourceAccount,
                                                                 final String targetAccount,
                                                                 final BigDecimal amount) {

        final String json = String.format(TRANSFER_PAYLOAD_TEMPLATE, UUID.randomUUID(), targetAccount, amount.doubleValue());

        return given()
                    .accept(ContentType.JSON).body(json)
                .when()
                    .post(TRANSFERS_ENDPOINT, sourceAccount)
                .then()
                    .log().ifError()
                    .statusCode(HttpStatus.SC_ACCEPTED)
                    .body("$", hasKey(JSON_FIELD_UUID))
                    .body("$", hasKey(JSON_FIELD_TO))
                    .body("$", hasKey(JSON_FIELD_AMOUNT))
                    .body(JSON_FIELD_TO, is(targetAccount))
                    .body(JSON_FIELD_AMOUNT, is(amount))
                    .extract()
                    .response()
                    .as(TransferResponseBody.class);
    }
}