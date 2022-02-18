/*
 * This file is part of Dis4IRC.
 *
 * Copyright (c) 2018-2021 Dis4IRC contributors
 *
 * MIT License
 */

package io.zachbr.dis4irc.bridge.command.executors

import io.zachbr.dis4irc.bridge.Bridge
import io.zachbr.dis4irc.bridge.command.api.Executor
import io.zachbr.dis4irc.bridge.message.Message
import io.zachbr.dis4irc.bridge.message.PlatformType

class ChannelsCommand(private val bridge: Bridge): Executor {
    override val helpMessage: String = "Shows list of all channels"

    override fun onCommand(command: Message): String? {
        if (command.source.type != PlatformType.IRC) {
            bridge.logger.debug("Ignoring request for channel list because it originates from Discord")
            return null
        }

        bridge.ircConn.sendNotice(command.sender.displayName, "Channels of this network:")

        for (channel in bridge.config.channelMappings) {
            bridge.ircConn.sendNotice(command.sender.displayName, "- ${channel.ircChannel}")
        }

        return null // don't send a message publicly
    }
}