package com.github.bun133.catcheyeplugin

import com.google.gson.Gson
import dev.kotx.flylib.flyLib
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import java.nio.ByteBuffer

class CatcheyePlugin : JavaPlugin() {

    init {
        flyLib {
            command(CatchEyeCommand())
        }
    }

    private lateinit var rejector: ModRejector
    override fun onEnable() {
        rejector = ModRejector.getInstance(this, rejectTick)
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, channel)
        Bukkit.getMessenger()
            .registerIncomingPluginChannel(this, channel) { _, player, message ->
                val buf = PacketBuffer(ByteBuffer.wrap(message))
                val packetId = buf.readByte() // packet id
                val str = buf.readString()
                val gson = Gson()
                try {
                    val obj = gson.fromJson(str, HelloPacket::class.java)
                    if (obj.key == joinMagic) {
                        // Join Packet
                        rejector.resolve(player)
                    }
                } catch (e: Exception) {
                    // Ignore
                    e
                }
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
        const val joinMagic = "joined!"
        const val rejectTick = 20 * 5
    }
}

private fun PacketBuffer.writeBytes(bytes: ByteArray) {
    bytes.forEach {
        this.buf.put(it)
    }
}
