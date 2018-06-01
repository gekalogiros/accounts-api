package com.gkalogiros.accounts.application;

import com.gkalogiros.accounts.exceptions.BusinessEntityNotFound;
import com.gkalogiros.accounts.exceptions.BusinessRuleException;
import com.gkalogiros.accounts.infrastructure.JsonConverter;
import com.gkalogiros.accounts.operations.Accounts;
import com.gkalogiros.accounts.operations.AccountsFacade;
import com.gkalogiros.accounts.operations.Transfers;
import com.gkalogiros.accounts.operations.TransfersFacade;
import com.gkalogiros.accounts.operations.TransfersScheduler;
import com.gkalogiros.accounts.storage.Datastore;
import com.gkalogiros.accounts.storage.InMemoryDatastore;
import com.gkalogiros.accounts.web.CreateAccountRoute;
import com.gkalogiros.accounts.web.CreateTransferRoute;
import com.gkalogiros.accounts.web.RestExceptionHandler;
import com.gkalogiros.accounts.web.GetAccountRoute;
import com.gkalogiros.accounts.web.GetAllAccountsRoute;
import com.gkalogiros.accounts.web.GetAllTransfersRoute;
import com.gkalogiros.accounts.web.GetTransferRoute;
import org.eclipse.jetty.http.HttpStatus;
import spark.Spark;

import static spark.Spark.after;
import static spark.Spark.awaitInitialization;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

class Application
{
    private static final int INITIAL_DELAY = 0;
    private static final int PERIOD = 10;

    public static void main(String[] args)
    {
       new Application(true).run(4567);
    }

    private final boolean enableTransfersConsumer;

    Application(final boolean enableTransfersConsumer) {
        this.enableTransfersConsumer = enableTransfersConsumer;
    }

    int run(int port){

        final JsonConverter jsonConverter = new JsonConverter();

        final Datastore datastore = new InMemoryDatastore();

        final Accounts customerAccounts = new AccountsFacade(datastore);

        final Transfers transfers = new TransfersFacade(datastore);

        if (enableTransfersConsumer){

            final TransfersScheduler transfersScheduler = new TransfersScheduler(transfers, INITIAL_DELAY, PERIOD);

            transfersScheduler.run();
        }

        port(port);

        post("/accounts", new CreateAccountRoute(jsonConverter, customerAccounts));

        get("/accounts", new GetAllAccountsRoute(jsonConverter, customerAccounts));

        get("/accounts/:account", new GetAccountRoute(jsonConverter, customerAccounts));

        post("/accounts/:account/transfers", new CreateTransferRoute(jsonConverter, transfers));

        get("/accounts/:account/transfers", new GetAllTransfersRoute(jsonConverter, transfers));

        get("/accounts/:account/transfers/:transfer", new GetTransferRoute(jsonConverter, transfers));

        after((request, response) -> response.type("application/json"));

        registerExceptionHandlers(jsonConverter);

        awaitInitialization();

        return port();
    }

    void stop() {
        Spark.stop();
    }

    private void registerExceptionHandlers(final JsonConverter jsonConverter){

        final RestExceptionHandler badRequestRestExceptionHandler = new RestExceptionHandler(jsonConverter, HttpStatus.BAD_REQUEST_400);

        final RestExceptionHandler notFoundRestExceptionHandler = new RestExceptionHandler(jsonConverter, HttpStatus.NOT_FOUND_404);

        exception(NullPointerException.class, badRequestRestExceptionHandler);

        exception(IllegalArgumentException.class, badRequestRestExceptionHandler);

        exception(BusinessRuleException.class, badRequestRestExceptionHandler);

        exception(BusinessEntityNotFound.class, notFoundRestExceptionHandler);
    }
}
