package me.zeronull.spawnarena.events.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public final class PlayerLiquidEnterEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public PlayerLiquidEnterEvent(Player who) {
        super(who);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}