package com.github.bun133.catcheyeplugin

import org.bukkit.util.Vector

data class UpdatePacket(
    val target:String,
    val location:Vector
)