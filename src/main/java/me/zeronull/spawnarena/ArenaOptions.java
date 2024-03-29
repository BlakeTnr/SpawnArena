package me.zeronull.spawnarena;

public abstract class ArenaOptions {
    private boolean shouldClearItems;
    private boolean allowDamage;
    private boolean allowPvp;
    private boolean deathOnTouchLiquid;
    private boolean winOnStepStonePressurePlate;
    private boolean giveKnockBackStick;
    private ArenaMode arenaMode;

    public boolean isShouldClearItems() {
        return this.shouldClearItems;
    }

    public ArenaOptions setShouldClearItems(final boolean value) {
        this.shouldClearItems = value;
        return this;
    }

    public boolean isAllowDamage() {
        return this.allowDamage;
    }

    public ArenaOptions setAllowDamage(final boolean value) {
        this.allowDamage = value;
        return this;
    }

    public boolean isAllowPvp() {
        return this.allowPvp;
    }

    public ArenaOptions setAllowPvp(final boolean value) {
        this.allowPvp = value;
        return this;
    }

    public boolean isDeathOnTouchLiquid() {
        return this.deathOnTouchLiquid;
    }

    public ArenaOptions setDeathOnTouchLiquid(final boolean value) {
        this.deathOnTouchLiquid = value;
        return this;
    }

    public boolean isWinOnStepStonePressurePlate() {
        return this.winOnStepStonePressurePlate;
    }

    public ArenaOptions setWinOnStepStonePressurePlate(final boolean value) {
        this.winOnStepStonePressurePlate = value;
        return this;
    }

    public boolean isGiveKnockBackStick() {
        return this.giveKnockBackStick;
    }

    public ArenaOptions setGiveKnockBackStick(final boolean value) {
        this.giveKnockBackStick = value;
        return this;
    }

    public ArenaMode getArenaMode() {
        return this.arenaMode;
    }

    public ArenaOptions setArenaMode(final String arenaMode) {
        this.arenaMode = ArenaMode.valueOf(arenaMode);
        return this;
    }
}