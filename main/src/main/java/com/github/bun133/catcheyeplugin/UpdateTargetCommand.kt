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
                val op = o.filterIsInstance<Player>()
                val t = typedArgs[1] as List<*>
                val tp = t.filterIsInstance<Player>().firstOrNull()
                val toOp = op.filter { p -> p != tp }// 自分自身を除く
                if (toOp.isEmpty() || tp == null) {
                    fail("プレイヤーが見つかりませんでした")
                    return@executes
                } else {
                    success("${toOp.joinToString(prefix = "{", postfix = "}") { it.name }}のターゲットを${tp.name}に更新しました")
                    toOp.forEach { p ->
                        (plugin as CatcheyePlugin).updateTarget(p, tp)
                    }
                }
            }
        }
    }
}