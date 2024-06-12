/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_20_R4;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutMount;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class EntityMountNMSPacket extends VersionNMSPacket {

    private Packet<?> rawPacket;

    EntityMountNMSPacket(EntityID vehicleEntityID, EntityID passengerEntityID) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(vehicleEntityID.getNumericID());
        packetByteBuffer.writeVarIntArray(passengerEntityID.getNumericID());

        try {
            Class<?> packetClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutMount");
            Constructor<?> constructor = packetClass.getDeclaredConstructor(PacketDataSerializer.class);
            constructor.setAccessible(true);

            this.rawPacket = (Packet<?>) constructor.newInstance(packetByteBuffer.getInternalSerializer());

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
