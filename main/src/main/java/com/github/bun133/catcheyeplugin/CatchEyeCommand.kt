package com.github.bun133.catcheyeplugin

import dev.kotx.flylib.command.Command
import org.bukkit.util.Vector


class CatchEyeCommand : Command("catch") {
    init {
        description("CatchEyeコマンド")
        children(UpdateTargetCommand(),ClearCommand())
    }
}