/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_20_R4;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class EntityTeleportNMSPacket extends VersionNMSPacket {

    private Packet<?> rawPacket;

    EntityTeleportNMSPacket(EntityID entityID, PositionCoordinates position, double positionOffsetY) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());

        // Position
        packetByteBuffer.writeDouble(position.getX());
        packetByteBuffer.writeDouble(position.getY() + positionOffsetY);
        packetByteBuffer.writeDouble(position.getZ());

        // Rotation
        packetByteBuffer.writeByte(0);
        packetByteBuffer.writeByte(0);

        // On ground
        packetByteBuffer.writeBoolean(false);

        try {
            Class<?> packetClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport");
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
