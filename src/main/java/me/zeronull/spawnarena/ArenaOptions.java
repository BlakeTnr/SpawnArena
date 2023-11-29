package me.zeronull.spawnarena;

public abstract class ArenaOptions {
    private boolean shouldClearItems;
    private boolean allowDamage;
    private boolean allowPvp;

    public boolean isShouldClearItems() {
        return this.shouldClearItems;
    }

    public boolean isAllowDamage() {
        return this.allowDamage;
    }

    public boolean isAllowPvp() {
        return this.allowPvp;
    }

    public ArenaOptions setShouldClearItems(final boolean value) {
        this.shouldClearItems = value;
        return this;
    }

    public ArenaOptions setAllowDamage(final boolean value) {
        this.allowDamage = value;
        return this;
    }

    public ArenaOptions setAllowPvp(final boolean value) {
        this.allowPvp = value;
        return this;
    }
}