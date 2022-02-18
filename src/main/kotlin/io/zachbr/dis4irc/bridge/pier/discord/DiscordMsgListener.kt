/*
 * This file is part of Dis4IRC.
 *
 * Copyright (c) 2018-2021 Dis4IRC contributors
 *
 * MIT License
 */

package io.zachbr.dis4irc.bridge.pier.discord

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * Responsible for listening to incoming discord messages and filtering garbage
 */
class DiscordMsgListener(private val pier: DiscordPier) : ListenerAdapter() {
    private val logger = pier.logger

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        // dont bridge itself
        val source = event.channel.asBridgeSource()
        if (pier.isThisBot(source, event.author.idLong)) {
            return
        }

        // don't bridge empty messages (discord does this on join)
        if (event.message.contentDisplay.isEmpty() && event.message.attachments.isEmpty() && event.message.stickers.isEmpty()) {
            return
        }

        val receiveTimestamp = System.nanoTime()
        logger.debug("DISCORD MSG ${event.channel.name} ${event.author.name}: ${event.message.contentStripped}")

        val message = event.message.toBridgeMsg(logger, receiveTimestamp)
        pier.sendToBridge(message)
    }

    override fun onGuildMessageUpdate(event: GuildMessageUpdateEvent) {
        if (!pier.bridge.config.forwardEdits) {
            return
        }
        // dont bridge itself
        val source = event.channel.asBridgeSource()
        if (pier.isThisBot(source, event.author.idLong)) {
            return
        }

        // don't bridge empty messages (discord does this on join)
        if (event.message.contentDisplay.isEmpty() && event.message.attachments.isEmpty() && event.message.stickers.isEmpty()) {
            return
        }

        val receiveTimestamp = System.nanoTime()
        logger.debug("DISCORD MSG ${event.channel.name} ${event.author.name}: ${event.message.contentStripped}")

        val message = event.message.toBridgeMsg(logger, receiveTimestamp)
        pier.sendToBridge(message)
    }
}
