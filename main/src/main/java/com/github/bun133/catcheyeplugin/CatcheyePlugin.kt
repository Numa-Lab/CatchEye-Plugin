package com.github.bun133.catcheyeplugin

import com.google.gson.Gson
import dev.kotx.flylib.flyLib
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

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
    }

    fun updateTarget(target:Player) = updateTarget(UpdatePacket(target.name,target.location.toVector()))

    fun updateTarget(data:UpdatePacket){
        Bukkit.getOnlinePlayers().forEach {
            sendPacket(it, data)
        }
    }

    private val buf: PacketBuffer = PacketBuffer()
    private val gson = Gson()

    private fun sendPacket(to: Player, data:UpdatePacket) {
        buf.clear()
        buf.writeByte(0)
        buf.writeString(gson.toJson(data))
        to.sendPluginMessage(this, channel, buf.toBytes())
        println("Send packet to ${to.name}")
    }

    companion object {
        const val channel = "catcheye:position"
    }
}