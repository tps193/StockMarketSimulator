package com.shadrin.stockmarket.cli.actions;

public class QuitAction extends Action {
    @Override
    public void run() {
        System.out.println("Bye!");
        System.exit(0);
    }
}
