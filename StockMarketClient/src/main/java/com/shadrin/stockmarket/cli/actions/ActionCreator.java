package com.shadrin.stockmarket.cli.actions;

import com.shadrin.stockmarket.cli.CommandLineParseException;
import okhttp3.OkHttpClient;

import java.util.Arrays;
import java.util.Iterator;

public class ActionCreator {
    private static final String ADD_ACTION = "add";
    private static final String CANCEL_ACTION = "cancel";
    private static final String ADD_SYMBOL_ACTION = "addSymbol";
    private static final String QUIT_ACTION = "quit";

    public static final String[] actions = new String[] {
            ADD_ACTION,
            CANCEL_ACTION,
            ADD_SYMBOL_ACTION,
            QUIT_ACTION
    };

    private final String url;
    private final OkHttpClient okHttpClient;

    public ActionCreator(String url, OkHttpClient okHttpClient) {
        this.url = url;
        this.okHttpClient = okHttpClient;
    }

    public Action getAction(String userInput) throws CommandLineParseException {
        if (userInput != null) {
            var tokenized = userInput.split(" ");
            Iterator<String> it = Arrays.stream(tokenized).iterator();
            if (it.hasNext()) {
                var actionName = it.next();
                var action = switch(actionName) {
                    case ADD_ACTION -> createAddAction(it);
                    case CANCEL_ACTION -> createCancelAction(it);
                    case ADD_SYMBOL_ACTION -> createAddSymbolAction(it);
                    case QUIT_ACTION -> new QuitAction();
                    default -> throw new CommandLineParseException(String.format("Unknown command %s. Following commands are allowed: %s", actionName, Arrays.toString(actions)));
                };
                action.setUrl(url);
                action.setClient(okHttpClient);
                return action;
            } else {
                throw new CommandLineParseException("Missing action.");
            }
        } else {
            throw new CommandLineParseException("Missing input.");
        }
    }

    private AddSymbolAction createAddSymbolAction(Iterator<String> it) throws CommandLineParseException {
        if (it.hasNext()) {
            var symbol = it.next();
            try {
                return new AddSymbolAction(symbol);
            } catch (NumberFormatException e) {
                throw new CommandLineParseException("Invalid symbol value.");
            }
        } else {
            throw new CommandLineParseException("Missing symbol value.");
        }
    }

    private CancelAction createCancelAction(Iterator<String> it) throws CommandLineParseException {
        if (it.hasNext()) {
            var idStr = it.next();
            try {
                return new CancelAction(Long.parseLong(idStr));
            } catch (NumberFormatException e) {
                throw new CommandLineParseException(String.format("Invalid order id %s. Integer value is required.", idStr));
            }
        } else {
            throw new CommandLineParseException("Missing order id.");
        }
    }

    private AddAction createAddAction(Iterator<String> it) throws CommandLineParseException {
        var action = new AddAction();
        setSymbol(action, it);
        setOrderType(action, it);
        setCount(action, it);
        setPrice(action, it);
        return action;
    }

    private void setSymbol(AddAction action, Iterator<String> it) throws CommandLineParseException {
        if (it.hasNext()) {
            action.setSymbol(it.next());
        } else {
            throw new CommandLineParseException("Missing order book symbol.");
        }
    }

    private void setOrderType(AddAction action, Iterator<String> it) throws CommandLineParseException {
        if (it.hasNext()) {
            var type = it.next();
            try {
                action.setOrderType(AddAction.OrderType.valueOf(type));
            } catch (IllegalArgumentException e) {
                throw new CommandLineParseException(String.format("Unknown order type %s. Valid values are %s", type, Arrays.toString(AddAction.OrderType.values())));
            }
        } else {
            throw new CommandLineParseException("Missing order type.");
        }
    }

    private void setCount(AddAction action, Iterator<String> it) throws CommandLineParseException {
        if (it.hasNext()) {
            var countStr = it.next();
            try {
                var count = Integer.parseInt(countStr);
                if (count < 1) {
                    throw new IllegalArgumentException("Count number must be positive");
                }
                action.setCount(count);
            } catch (IllegalArgumentException e) {
                throw new CommandLineParseException(String.format("Invalid stock count number %s. Positive integer is required.", countStr));
            }
        } else {
            throw new CommandLineParseException("Missing stock count number.");
        }
    }

    private void setPrice(AddAction action, Iterator<String> it) throws CommandLineParseException {
        if (it.hasNext()) {
            var priceStr = it.next();
            try {
                var price = Integer.parseInt(priceStr);
                if (price < 1) {
                    throw new IllegalArgumentException("Price number must be positive");
                }
                action.setPrice(price);
            } catch (IllegalArgumentException e) {
                throw new CommandLineParseException(String.format("Invalid stock price %s. Positive integer is required.", priceStr));
            }
        } else {
            throw new CommandLineParseException("Missing stock price.");
        }
    }
}
