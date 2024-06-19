package io.github.mireole.mgtceua.common.covers;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnectionException;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridNode;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import gregtech.api.cover.CoverBase;
import gregtech.api.cover.CoverDefinition;
import gregtech.api.cover.CoverWithUI;
import gregtech.api.cover.CoverableView;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import io.github.mireole.mgtceua.MGTCEuA;
import io.github.mireole.mgtceua.api.IAECover;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.EnumSet;

abstract public class AbstractMeMachineController extends CoverBase implements IAECover, CoverWithUI {
    protected final AENetworkProxy networkProxy;
    protected IGridConnection gridConnection;
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
        this.tryConnectToTileEntity();
    }

    @Override
    public AENetworkProxy getProxy() {
        if (!this.networkProxy.isReady() && this.getWorld() != null) {
            this.networkProxy.onReady();
            this.tryConnectToTileEntity();
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

    // TODO rework the ui for MUI2 once GTCEu 2.8.9 is out
    @Override
    public ModularUI createUI(EntityPlayer player) {
        return null;
    }

    @Override
    public boolean canAttach(@NotNull CoverableView coverable, @NotNull EnumFacing side) {
        return true;
    }

    /*
        * Tries to connect the cover to the tile entity's node if it has one
     */
    private void tryConnectToTileEntity() {
        if (this.gridConnection != null) return;
        if (this.getCoverableView() instanceof MetaTileEntity tileEntity && tileEntity.getHolder() instanceof IGridProxyable proxyable) {
            if (proxyable.getProxy() == null) return;
            IGridNode nodeOther = proxyable.getProxy().getNode();
            IGridNode node = this.getProxy().getNode();
            if (nodeOther == null || node == null) return;

            try {
                this.gridConnection = AEApi.instance().grid().createGridConnection(node, nodeOther);
            } catch (FailedConnectionException exception) {
                MGTCEuA.LOGGER.error("Failed to connect ME machine controller to machine", exception);
            }
        }
    }

}
