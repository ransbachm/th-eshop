package moe.pgnhd.theshop;

import com.braintreegateway.*;
import moe.pgnhd.theshop.handlers.BasketHandler;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Payments {
    private BraintreeGateway gateway;


    public Payments() {
        gateway = new BraintreeGateway(
                Environment.SANDBOX,
                Main.dotenv.get("BRAINTREE_MERCHANT_ID"),
                Main.dotenv.get("BRAINTREE_PUBLIC_KEY"),
                Main.dotenv.get("BRAINTREE_PRIVATE_KEY")
        );
        Configuration config = gateway.getConfiguration();
        Logger logger = config.getLogger();
        //TODO: Integrate slf4j logging
        logger.setLevel(Level.SEVERE);
    }

    public String generate_client_token() {
        return gateway.clientToken().generate();
    }

    public Result<Transaction> sale(String nonce, BigDecimal amount) {
        TransactionRequest t_req = new TransactionRequest()
                .amount(amount)
                .paymentMethodNonce(nonce)
                .options()
                .submitForSettlement(true)
                .done();
        return gateway.transaction().sale(t_req);
    }
}
