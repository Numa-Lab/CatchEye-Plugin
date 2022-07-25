package com.github.bun133.catcheyeplugin

import dev.kotx.flylib.command.Command
import org.bukkit.entity.Player

class UpdateTargetCommand : Command("update") {
    init {
        description("ターゲットを更新する")
        usage {
            entityArgument("operatePlayer")
            entityArgument("targetPlayer")

            executes {
                val o = typedArgs[0] as List<*>
                val op = o.filterIsInstance<Player>().firstOrNull()
                val t = typedArgs[1] as List<*>
                val tp = t.filterIsInstance<Player>().firstOrNull()
                if (op == null || tp == null) {
                    fail("プレイヤーが見つかりませんでした")
                    return@executes
                } else {
                    success("${op.name}のターゲットを${tp.name}に更新しました")
                    (plugin as CatcheyePlugin).updateTarget(op,tp)
                }
            }
        }
    }
}