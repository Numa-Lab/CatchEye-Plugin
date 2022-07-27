package com.github.bun133.catcheyeplugin

import dev.kotx.flylib.command.Command


class CatchEyeCommand : Command("catch") {
    init {
        description("CatchEyeコマンド")
        children(UpdateTargetCommand(),ClearCommand())
    }
}