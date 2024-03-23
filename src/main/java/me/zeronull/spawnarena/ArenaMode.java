package me.zeronull.spawnarena;

public enum ArenaMode {
    SINGLE, // Only 1 fight at a time is supported
    MULTI; // Multi fights at a time is supported whilst hiding players from other fights

    public ArenaMode next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}