/*
 * This file is part of Dis4IRC.
 *
 * Copyright (c) 2018-2021 Dis4IRC contributors
 *
 * MIT License
 */

package io.zachbr.dis4irc.bridge.command.executors

import io.zachbr.dis4irc.bridge.Bridge
import io.zachbr.dis4irc.bridge.command.COMMAND_PREFIX
import io.zachbr.dis4irc.bridge.command.CommandManager
import io.zachbr.dis4irc.bridge.command.api.Executor
import io.zachbr.dis4irc.bridge.message.Message
import io.zachbr.dis4irc.bridge.message.PlatformType

class HelpCommand(private val bridge: Bridge, private val commandManager: CommandManager): Executor {
    override val helpMessage: String = "Shows this help"

    override fun onCommand(command: Message): String? {
        if (command.source.type != PlatformType.IRC) {
            bridge.logger.debug("Ignoring request for channel list because it originates from Discord")
            return null
        }

        bridge.ircConn.sendNotice(command.sender.displayName, "Help - Commands:")

        for ((definedCommand, executor) in commandManager.getCommands()) {
            bridge.ircConn.sendNotice(command.sender.displayName, "- $COMMAND_PREFIX${definedCommand}${if (executor.helpMessage != null) " - " + executor.helpMessage else ""}")
        }

        return null // don't send a message publicly
    }
}