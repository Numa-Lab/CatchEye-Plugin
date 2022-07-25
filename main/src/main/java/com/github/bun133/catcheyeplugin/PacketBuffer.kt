package com.github.bun133.catcheyeplugin

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets


class PacketBuffer private constructor(private val buf: ByteBuffer) {
    @JvmOverloads
    constructor(length: Int = maxLength) : this(ByteBuffer.allocate(length))

    fun clear() {
        buf.clear()
    }

    fun fromBytes(bytes: ByteArray?) {
        buf.clear()
        buf.put(bytes)
        buf.flip()
    }

    fun toBytes(): ByteArray {
        buf.flip()
        val bytes = ByteArray(buf.remaining())
        buf[bytes]
        buf.clear()
        return bytes
    }

    fun writeByte(input: Int) {
        buf.put(input.toByte())
    }

    fun readByte(): Byte {
        return buf.get()
    }

    fun writeVarInt(inp: Int) {
        var input = inp
        while (input and -128 != 0) {
            buf.put((input and 127 or 128).toByte())
            input = input ushr 7
        }
        buf.put(input.toByte())
    }

    fun readVarInt(): Int {
        var i = 0
        var j = 0
        while (true) {
            val b0 = buf.get()
            i = i or (b0.toInt() and 127 shl j++) * 7
            if (j > 5) {
                throw RuntimeException("VarInt too big")
            }
            if (b0.toInt() and 128 != 128) {
                break
            }
        }
        return i
    }

    fun writeString(string: String) {
        val abyte = string.toByteArray(StandardCharsets.UTF_8)
        if (abyte.size > maxLength) {
            throw RuntimeException("String too big (was " + abyte.size + " bytes encoded, max " + maxLength + ")")
        } else {
            writeVarInt(abyte.size)
            buf.put(abyte)
        }
    }

    fun readString(): String {
        val i = readVarInt()
        return if (i > maxLength * 4) {
            throw RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")")
        } else if (i < 0) {
            throw RuntimeException("The received encoded string buffer length is less than zero! Weird string!")
        } else {
            val bytes = ByteArray(i)
            buf[bytes, 0, i]
            val s = String(bytes, StandardCharsets.UTF_8)
            if (s.length > maxLength) {
                throw RuntimeException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")")
            } else {
                s
            }
        }
    }

    companion object {
        const val maxLength = 32767
    }
}