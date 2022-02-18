/*
 * This file is part of Dis4IRC.
 *
 * Copyright (c) 2018-2021 Dis4IRC contributors
 *
 * MIT License
 */

package io.zachbr.dis4irc.bridge.mutator.mutators

import io.zachbr.dis4irc.bridge.BridgeConfiguration
import io.zachbr.dis4irc.bridge.message.Message
import io.zachbr.dis4irc.bridge.message.PlatformType
import io.zachbr.dis4irc.bridge.mutator.api.Mutator

class ChannelMention(private val configuration: BridgeConfiguration) : Mutator {
    override fun mutate(message: Message): Mutator.LifeCycle {
        var out = message.contents

        if (message.source.type == PlatformType.IRC) {
            out = out.replace("[#|&][^\\s\\a,]{1,200}".toRegex()) { result ->
                val channel = configuration.channelMappings.find { it.ircChannel == result.value }
                if (channel != null) {
                    return@replace "<#${channel.discordChannel}>"
                }
                result.value
            }
        } else if (message.source.type == PlatformType.DISCORD && message.mentionedChannels?.isNotEmpty() == true) {
            message.mentionedChannels!!.forEach { mention ->
                out = out.replace("#${mention.key}", configuration.channelMappings.find { it.discordChannel == mention.value }?.ircChannel ?: "# ${mention.key}")
            }
        }

        message.contents = out

        return Mutator.LifeCycle.CONTINUE
    }
}