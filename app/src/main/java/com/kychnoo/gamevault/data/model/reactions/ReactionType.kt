package com.kychnoo.gamevault.data.model.reactions

import com.kychnoo.gamevault.data.remote.dto.model.ReactionsDto

enum class ReactionType(val emoji: String, val apiKey: String) {
    FIRE("🔥", "1"),
    THUMBS_UP("👍", "2"),
    THUMBS_DOWN("👎", "3"),
    MIND_BLOWN("🤯", "4"),
    ROCKET("🚀", "5"),
    BUG("🐛", "6"),
    POOP("💩", "7"),
    HEART("❤️", "8"),
    GAMEPAD("🎮", "9"),
    TARGET("🎯", "10"),
    MONEY("💰", "11"),
    CLOCK("🕐", "12"),
    SLEEPY("😴", "13"),
    ART("🎨", "14"),
    MUSIC("🎵", "15"),
    BOOK("📖", "16"),
    TROPHY("🏆", "18"),
    HANDSHAKE("🤝", "20"),
    GLOBE("🌐", "21");

    companion object {
        fun fromApiKey(key: String): ReactionType? = entries.find { it.apiKey == key }
    }
}

fun ReactionsDto.toDomain(): List<Reaction> {
    return listOfNotNull(
        fire?.let { Reaction(ReactionType.FIRE, it) },
        thumbsUp?.let { Reaction(ReactionType.THUMBS_UP, it) },
        thumbsDown?.let { Reaction(ReactionType.THUMBS_DOWN, it) },
        mindBlown?.let { Reaction(ReactionType.MIND_BLOWN, it) },
        rocket?.let { Reaction(ReactionType.ROCKET, it) },
        bug?.let { Reaction(ReactionType.BUG, it) },
        poop?.let { Reaction(ReactionType.POOP, it) },
        heart?.let { Reaction(ReactionType.HEART, it) },
        gamepad?.let { Reaction(ReactionType.GAMEPAD, it) },
        target?.let { Reaction(ReactionType.TARGET, it) },
        money?.let { Reaction(ReactionType.MONEY, it) },
        clock?.let { Reaction(ReactionType.CLOCK, it) },
        sleepy?.let { Reaction(ReactionType.SLEEPY, it) },
        art?.let { Reaction(ReactionType.ART, it) },
        music?.let { Reaction(ReactionType.MUSIC, it) },
        book?.let { Reaction(ReactionType.BOOK, it) },
        trophy?.let { Reaction(ReactionType.TROPHY, it) },
        handshake?.let { Reaction(ReactionType.HANDSHAKE, it) },
        globe?.let { Reaction(ReactionType.GLOBE, it) }
    )
}