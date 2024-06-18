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
import io.github.mireole.mgtceua.api.IAECover;
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

abstract public class AbstractMeMachineController extends CoverMachineController implements IAECover {
    protected final AENetworkProxy networkProxy;
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
        return this.getProxy().getNode();
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
        NBTTagCompound tag = new NBTTagCompound();
        this.networkProxy.writeToNBT(tag);
        packetBuffer.writeCompoundTag(tag);
    }

    @Override
    public void readInitialSyncData(@NotNull PacketBuffer packetBuffer) {
        super.readInitialSyncData(packetBuffer);
        NBTTagCompound tag = null;
        try {
            tag = packetBuffer.readCompoundTag();
        } catch (IOException ignored) {

        }
        if (tag != null) {
            this.networkProxy.readFromNBT(tag);
        }
    }

    @Override
    public void writeToNBT(@NotNull NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        this.networkProxy.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.networkProxy.readFromNBT(tagCompound);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (this.networkProxy != null) {
            this.networkProxy.invalidate();
        }
    }
}
