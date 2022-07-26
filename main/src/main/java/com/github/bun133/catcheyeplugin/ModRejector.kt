package com.github.bun133.catcheyeplugin

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ModRejector(plugin: CatcheyePlugin, private val rejectTick: Int) : Listener {
    companion object {
        private var instance: ModRejector? = null
        fun getInstance(plugin: CatcheyePlugin,rejectTick: Int): ModRejector {
            if (instance == null) {
                instance = ModRejector(plugin, rejectTick)
            }
            return instance!!
        }
    }

    private val waiting = mutableMapOf<Player, Int>()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        Bukkit.getServer().scheduler.runTaskTimer(plugin, this::tick, 0, 1)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        addQueue(e.player)
    }

    private fun addQueue(player: Player) {
        waiting[player] = Bukkit.getServer().currentTick + rejectTick
    }

    private fun tick() {
        waiting.filterValues { it < Bukkit.getServer().currentTick }.forEach {
            // パケットが来なかった
            it.key.kick(Component.text("MODを正しく導入してください!"))
        }
    }

    fun resolve(p: Player) {
        waiting.remove(p)
    }
}