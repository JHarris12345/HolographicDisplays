/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.placeholder;

import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderFactory;
import me.filoghost.holographicdisplays.plugin.bridge.placeholderapi.PlaceholderAPIHook;
import me.filoghost.holographicdisplays.plugin.config.Settings;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIPlaceholderFactory implements IndividualPlaceholderFactory {

    @Override
    public @Nullable IndividualPlaceholder getPlaceholder(@Nullable String argument) {
        if (!Settings.placeholderAPIEnabled) {
            return new ImmutablePlaceholder("[PlaceholderAPI not enabled in configuration]");
        }

        if (argument == null) return null;

        String placeholder = argument;
        int refreshRateTicks = 200;

        try {
            placeholder = argument.split(":")[0];
            refreshRateTicks = Integer.valueOf(argument.split(":")[1]);
        } catch (ArrayIndexOutOfBoundsException ex) { }

        return new PlaceholderAPIPlaceholder("%" + placeholder + "%", refreshRateTicks);
    }


    private static class PlaceholderAPIPlaceholder implements IndividualPlaceholder {

        private final String content;
        private final int refreshRateTicks;

        PlaceholderAPIPlaceholder(String content, int refreshRateTicks) {
            this.content = content;
            this.refreshRateTicks = refreshRateTicks;
        }

        @Override
        public int getRefreshIntervalTicks() {
            return refreshRateTicks;
        }

        @Override
        public @Nullable String getReplacement(@NotNull Player player, @Nullable String argument) {
            if (!PlaceholderAPIHook.isEnabled()) {
                return null;
            }

            return PlaceholderAPIHook.replacePlaceholders(player, content);
        }
    }

}
