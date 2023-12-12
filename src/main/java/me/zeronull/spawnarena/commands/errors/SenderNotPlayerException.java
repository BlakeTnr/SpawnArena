package me.zeronull.spawnarena.commands.errors;

public class SenderNotPlayerException extends Exception {
    public SenderNotPlayerException(String message) {
        super(message);
    }
}