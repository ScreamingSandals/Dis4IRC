/*
 * This file is part of Dis4IRC.
 *
 * Copyright (c) 2018-2021 Dis4IRC contributors
 *
 * MIT License
 */

package io.zachbr.dis4irc.bridge.mutator.mutators

import io.zachbr.dis4irc.bridge.message.Message
import io.zachbr.dis4irc.bridge.message.PlatformType
import io.zachbr.dis4irc.bridge.mutator.api.Mutator

// http://www.kamishinryu.com/ircemoticons.htm & http://www.ircbeginner.com/ircinfo/emoticons.html without enterprise + emojis on libera's webchat
val emoticonsTable: Map<String, String> = mapOf(
    ":)" to ":slight_smile:",
    ":-)" to ":slight_smile:",
    ";)" to ":wink:",
    ";-)" to ":wink:",
    ":D" to ":smile:",
    ":P" to ":stuck_out_tongue:",
    ":*" to ":kissing:",
    ":**:" to ":kissing_heart:", // is this correct?
    ":I" to ":thinking:", // described as Hmm, so is this the correct equivalent?
    ":(" to ":frowning:",
    ">:(" to ":angry:",
    ":[" to ":frowning:",
    ":-<" to ":frowning:", // maybe?
    ":x" to ":no_mouth:",
    ":-o" to ":open_mouth:",
    //":-!" to "", // gimme good discord alternative
    //"P*" to "", // gimme good discord alternative
    "O:)" to ":innocent:",
    "):[" to ":angry:",
    //"[:-|" to "", // Frankenstein, wtf
    "^5" to ":raised_hands:",
    ":/" to ":confused:",
    "=:-O" to ":fearful:",
    "(@_@)" to ":heart_eyes:",
    "(o_o)" to ":hushed:",
    "~\\0/~" to ":wave:",
    ":-)))" to ":smiley:",
    "&:-)" to ":person_curly_hair:",
    //"#:-)" to "", // find alternative
    ":>" to ":blush:",
    ":->" to ":blush:",
    "*<|:-)" to ":santa:",
    "=^.^=" to ":smiley_cat:",
    "~:-(" to ":triumph:",
    "\\_/" to ":milk:", // seems like we don't have empty glass
    "\\~/" to ":milk:",
    "|_|}" to ":coffee:",
    "<:-)" to ":innocent:", // find alternative ig,
    "-_-" to ":neutral_face:",
    ":')" to ":joy:",
    "'=D" to ":sweat_smile:",
    "XD" to ":laughing:",
    "'=(" to ":sweat:", // idk
    ":@" to ":rage:",
    ";(" to ":sob:",
    "O:3" to ":innocent:",
    ":b" to ":stuck_out_tongue:",
    "<3" to ":heart:",
    "</3" to ":broken_heart:",
    "=D" to ":smiley:",
    ";D" to ":wink:",
    "=*" to ":kissing:",
    "X-P" to ":stuck_out_tongue_winking_eye:",
    "=(" to ":frowning:",
    ">.<" to ":persevere:",
    "X)" to ":face_with_spiral_eyes:",
    "\\O/" to ":man_gesturing_ok:",
    "8)" to ":sunglasses:",
    "=L" to ":confused:",
    ":O" to ":open_mouth:",
    "=#" to ":no_mouth:",
    ":]" to ":slight_smile:",
    "(y)" to ":thumbsup:",
    "D:" to ":astonished:", // idk
    "=\$" to ":flushed:"
)

class IrcEmoticons: Mutator {
    override fun mutate(message: Message): Mutator.LifeCycle {
        if (message.source.type == PlatformType.IRC) {
            message.contents = message.contents.replace("[^\\s]+".toRegex()) {
                emoticonsTable[it.value] ?: it.value
            }
        }

        return Mutator.LifeCycle.CONTINUE
    }
}