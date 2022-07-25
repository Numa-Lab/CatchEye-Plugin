package com.github.bun133.catcheyeplugin

import dev.kotx.flylib.command.Command
import org.bukkit.entity.Player

class UpdateTargetCommand : Command("update") {
    init {
        description("ターゲットを更新する")
        usage {
            entityArgument("Player")

            executes {
                val list = typedArgs[0] as List<*>
                val p = list.filterIsInstance<Player>().firstOrNull()
                if (p == null) {
                    fail("プレイヤーが見つかりませんでした")
                    return@executes
                } else {
                    success("ターゲットを${p.name}に更新しました")
                    (plugin as CatcheyePlugin).updateTarget(p)
                }
            }
        }
    }
}