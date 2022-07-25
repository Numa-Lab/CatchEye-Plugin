package com.github.bun133.catcheyeplugin

import dev.kotx.flylib.command.Command
import org.bukkit.util.Vector


class CatchEyeCommand : Command("catch") {
    init {
        description("CatchEyeコマンド")
        children(UpdateTargetCommand())
        usage {
            selectionArgument("Operation","clear")
            executes {
                when(typedArgs[0] as String){
                    "clear" -> {
                        success("ターゲットをクリアしました")
                        (plugin as CatcheyePlugin).updateTarget(UpdatePacket("", Vector(0,0,0)))
                    }
                }
            }
        }
    }
}