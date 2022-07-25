package com.github.bun133.catcheyeplugin

import dev.kotx.flylib.command.Command
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import javax.swing.text.html.parser.Entity

class ClearCommand : Command("clear") {
    init {
        description("ターゲットをクリアする")
        usage {
            entityArgument("player")
            executes {
                val e = typedArgs[0] as List<*>
                val player = e.filterIsInstance<Player>().firstOrNull()
                if (player != null) {
                    success("${player.name}をクリアしました")
                    (plugin as CatcheyePlugin).clearPlayer(player)
                }else{
                    fail("プレイヤーが見つかりませんでした")
                }
            }
        }
    }
}