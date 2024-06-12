/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_20_R4;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class EntityMetadataNMSPacket extends VersionNMSPacket {

    private Packet<?> rawPacket = null;

    private EntityMetadataNMSPacket(PacketByteBuffer packetByteBuffer) {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata");
            Constructor<?> constructor = packetClass.getDeclaredConstructor(PacketDataSerializer.class);
            constructor.setAccessible(true);

            this.rawPacket = (Packet<?>) constructor.newInstance(packetByteBuffer.getInternalSerializer());

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

    public static DataWatcherPacketBuilder<EntityMetadataNMSPacket> builder(EntityID entityID) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();
        packetByteBuffer.writeVarInt(entityID.getNumericID());
        return new Builder(packetByteBuffer);
    }


    private static class Builder extends DataWatcherPacketBuilder<EntityMetadataNMSPacket> {

        private Builder(PacketByteBuffer packetByteBuffer) {
            super(packetByteBuffer);
        }

        @Override
        EntityMetadataNMSPacket createPacket(PacketByteBuffer packetByteBuffer) {
            return new EntityMetadataNMSPacket(packetByteBuffer);
        }
    }
}
