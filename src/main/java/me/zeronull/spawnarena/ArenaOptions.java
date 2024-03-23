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

    public boolean isAllowDamage() {
        return this.allowDamage;
    }

    public boolean isAllowPvp() {
        return this.allowPvp;
    }

    public boolean isDeathOnTouchLiquid() { return this.deathOnTouchLiquid; }

    public boolean isWinOnStepStonePressurePlate() { return this.winOnStepStonePressurePlate; }

    public boolean isGiveKnockBackStick() { return this.giveKnockBackStick; }

    public ArenaMode getArenaMode() { return this.arenaMode; }

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

    public ArenaOptions setGiveKnockBackStick(final boolean value) {
        this.giveKnockBackStick = value;
        return this;
    }

    public ArenaOptions setArenaMode(final String arenaMode) {
        this.arenaMode = ArenaMode.valueOf(arenaMode);
        return this;
    }
}