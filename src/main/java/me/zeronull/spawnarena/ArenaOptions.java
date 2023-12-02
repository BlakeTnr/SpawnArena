package me.zeronull.spawnarena;

public abstract class ArenaOptions {
    private boolean shouldClearItems;
    private boolean allowDamage;
    private boolean allowPvp;
    private boolean deathOnTouchLiquid;
    private boolean winOnStepStonePressurePlate;

    public boolean isShouldClearItems() {
        return this.shouldClearItems;
    }

    public boolean isAllowDamage() {
        return this.allowDamage;
    }

    public boolean isAllowPvp() {
        return this.allowPvp;
    }

    public boolean isDeathOnTouchLiquid() { return this.deathOnTouchLiquid; }

    public boolean isWinOnStepStonePressurePlate() { return this.winOnStepStonePressurePlate; }

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

    public ArenaOptions setDeathOnTouchLiquid(final boolean value) {
        this.deathOnTouchLiquid = value;
        return this;
    }

    public ArenaOptions setWinOnStepStonePressurePlate(final boolean value) {
        this.winOnStepStonePressurePlate = value;
        return this;
    }
}