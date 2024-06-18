package io.github.mireole.mgtceua.common.covers;

import appeng.api.networking.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import gregtech.api.cover.CoverDefinition;
import gregtech.api.cover.CoverableView;
import gregtech.common.covers.CoverMachineController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.EnumSet;

abstract public class AbstractMeMachineController extends CoverMachineController implements IGridProxyable, ITickable {
    protected AENetworkProxy networkProxy;
    private TileEntity previousNeighbor;
    private boolean shouldUpdate;
    public AbstractMeMachineController(@NotNull CoverDefinition definition, @NotNull CoverableView coverableView, @NotNull EnumFacing attachedSide) {
        super(definition, coverableView, attachedSide);
        this.networkProxy = new AENetworkProxy(this, "mte_proxy", this.getPickItem(), true);
        this.networkProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
        this.networkProxy.setIdlePowerUsage(1);
        this.networkProxy.setValidSides(EnumSet.of(this.getAttachedSide()));
    }

    @Override
    public void onAttachment(@NotNull CoverableView coverableView, @NotNull EnumFacing side, @Nullable EntityPlayer player, @NotNull ItemStack itemStack) {
        super.onAttachment(coverableView, side, player, itemStack);
        this.getProxy().setOwner(player);
    }

    @Override
    public AENetworkProxy getProxy() {
        if (!this.networkProxy.isReady() && this.getWorld() != null) {
            this.networkProxy.onReady();
        }
        return this.networkProxy;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this.getWorld(), this.getPos());
    }

    @Override
    public void gridChanged() {

    }

    @Nullable
    @Override
    public IGridNode getGridNode(@NotNull AEPartLocation aePartLocation) {
        return this.networkProxy == null ? null : this.networkProxy.getNode();
    }

    @NotNull
    @Override
    public AECableType getCableConnectionType(@NotNull AEPartLocation aePartLocation) {
        return aePartLocation.getFacing() == this.getAttachedSide() ? AECableType.SMART : AECableType.NONE;
    }

    @Override
    public void securityBreak() {
        this.getWorld().destroyBlock(this.getPos(), true);
    }

    @Override
    public void writeInitialSyncData(@NotNull PacketBuffer packetBuffer) {
        super.writeInitialSyncData(packetBuffer);
        if (this.networkProxy != null) {
            packetBuffer.writeBoolean(true);
            NBTTagCompound tag = new NBTTagCompound();
            this.networkProxy.writeToNBT(tag);
            packetBuffer.writeCompoundTag(tag);
        } else {
            packetBuffer.writeBoolean(false);
        }
    }

    @Override
    public void readInitialSyncData(@NotNull PacketBuffer packetBuffer) {
        super.readInitialSyncData(packetBuffer);
        if (packetBuffer.readBoolean()) {
            NBTTagCompound tag = null;
            try {
                tag = packetBuffer.readCompoundTag();
            } catch (IOException ignored) {

            }
            if (this.networkProxy != null && tag != null) {
                this.networkProxy.readFromNBT(tag);
            }
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (this.networkProxy != null) {
            this.networkProxy.invalidate();
        }
    }

    @Override
    public void update() {
        // We have to check the neighbor every tick (it's cached and only updates on block updates) to schedule an update for the next tick
        // Because there is no way for covers to be notified of block updates with the GTCEu API
        // TODO mixin or make a PR ?
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            if (shouldUpdate && this.getProxy().getNode() != null) {
                this.shouldUpdate = false;
                this.networkProxy.getNode().updateState();
            }

            // the getNeighbor should be cached by GTCEu and only updated on block updates
            TileEntity neighbor = this.getNeighbor(this.getAttachedSide());
            if (neighbor != this.previousNeighbor) {
                this.previousNeighbor = neighbor;
                this.shouldUpdate = true;
            }
        }
    }
}
