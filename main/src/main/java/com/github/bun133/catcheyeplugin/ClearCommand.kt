package com.github.bun133.catcheyeplugin

import dev.kotx.flylib.command.Command
import org.bukkit.entity.Player

class ClearCommand : Command("clear") {
    init {
        description("ターゲットをクリアする")
        usage {
            entityArgument("player")
            executes {
                val e = typedArgs[0] as List<*>
                val player = e.filterIsInstance<Player>()
                if (player.isNotEmpty()) {
                    success("${player.joinToString(prefix = "{", postfix = "}") { it.name }}をクリアしました")
                    player.forEach { p ->
                        (plugin as CatcheyePlugin).clearPlayer(p)
                    }
                } else {
                    fail("プレイヤーが見つかりませんでした")
                }
            }
        }
    }
}