/*
 * This file is part of Dis4IRC.
 *
 * Copyright (c) 2018-2021 Dis4IRC contributors
 *
 * MIT License
 */

package io.zachbr.dis4irc.bridge.mutator

import io.zachbr.dis4irc.bridge.Bridge
import io.zachbr.dis4irc.bridge.message.Message
import io.zachbr.dis4irc.bridge.mutator.api.Mutator
import io.zachbr.dis4irc.bridge.mutator.mutators.*
import org.spongepowered.configurate.CommentedConfigurationNode

class MutatorManager(bridge: Bridge, config: CommentedConfigurationNode) {
    private val mutators = HashMap<Class<out Mutator>, Mutator>()

    init {
        registerMutator(StripAntiPingCharacters())
        registerMutator(BlockHereEveryone())
        registerMutator(PasteLongMessages(bridge, config.node("paste-service")))
        registerMutator(TranslateFormatting())
        registerMutator(ChannelMention(bridge.config))
        if (bridge.config.translateIrcEmoticonsToDiscordEmojis) {
            registerMutator(IrcEmoticons())
        }
    }

    private fun registerMutator(mutator: Mutator) {
        mutators[mutator.javaClass] = mutator
    }

    internal fun applyMutators(message: Message): Message? {
        // Run mutators on the referenced message, if it exists
        // Limiting recursion depth is handled at message creation in the discord listener
        if (message.referencedMessage != null) {
            applyMutators(message.referencedMessage)
        }

        val iterator = mutators.values.iterator()

        loop@ while (iterator.hasNext()) {
            val mutator = iterator.next()
            if (message.hasAlreadyApplied(mutator.javaClass)) {
                continue
            }

            val state = mutator.mutate(message)
            message.markMutatorApplied(mutator.javaClass)

            return when (state) {
                Mutator.LifeCycle.CONTINUE -> continue@loop
                Mutator.LifeCycle.STOP_AND_DISCARD -> null
                Mutator.LifeCycle.RETURN_EARLY -> message
            }
        }

        return message
    }

    internal fun applyMutator(clazz: Class<out Mutator>, message: Message) {
        val mutator = mutators[clazz] ?: throw NoSuchElementException("No mutator with class type: ${clazz.simpleName}")
        mutator.mutate(message)
        message.markMutatorApplied(mutator.javaClass)
    }
}
