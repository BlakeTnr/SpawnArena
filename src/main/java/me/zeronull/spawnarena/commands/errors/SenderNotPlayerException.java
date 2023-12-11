package me.zeronull.spawnarena.commands.errors;

public class SenderNotPlayer extends Exception {
    public SenderNotPlayer(String message) {
        super(message);
    }
}
