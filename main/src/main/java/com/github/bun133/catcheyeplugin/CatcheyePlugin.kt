package com.github.bun133.catcheyeplugin

import com.google.gson.Gson
import dev.kotx.flylib.flyLib
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector

class CatcheyePlugin : JavaPlugin() {

    init {
        flyLib {
            command(CatchEyeCommand())
        }
    }


    override fun onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, channel)
        Bukkit.getMessenger()
            .registerIncomingPluginChannel(this, channel) { _, _, _ ->
                // Nothing to do
            }

        Bukkit.getServer().scheduler.runTaskTimer(this, Runnable {
            Bukkit.getOnlinePlayers().forEach {
                val tp = data[it]
                if (tp != null) {
                    sendPacket(it, UpdatePacket(tp.name, tp.location.toVector()))       // Update Packet
                } else {
                    sendPacket(it, UpdatePacket("", Vector(0, 0, 0)))   // Clear Packet
                }
            }
        }, 0, 20)
    }

    // [見てる側のぷれいやー] -> [見られている側のぷれいやー]
    private val data = mutableMapOf<Player, Player>()
    fun updateTarget(sendTo: Player, target: Player) {
        this.data[sendTo] = target
    }

    fun clearPlayer(op: Player) {
        data.remove(op)
    }

    private val buf: PacketBuffer = PacketBuffer()
    private val gson = Gson()

    private fun sendPacket(to: Player, data: UpdatePacket) {
        buf.clear()
        buf.writeByte(0)
        buf.writeString(gson.toJson(data))
        to.sendPluginMessage(this, channel, buf.toBytes())
    }

    companion object {
        const val channel = "catcheye:position"
    }
}