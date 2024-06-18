package io.github.mireole.mgtceua.mixin.cover;

import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import gregtech.api.cover.Cover;
import gregtech.api.gui.IUIHolder;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.TickableTileEntityBase;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import io.github.mireole.mgtceua.MGTCEuA;
import io.github.mireole.mgtceua.api.IAECover;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IWorldNameable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MetaTileEntityHolder.class, remap = false)
public abstract class MetaTileEntityHolderMixin {

    @Shadow public abstract MetaTileEntity getMetaTileEntity();

    @Inject(at = @At("HEAD"), method = "getGridNode", cancellable = true)
    private void getGridNode(AEPartLocation part, CallbackInfoReturnable<IGridNode> callback) {
        MetaTileEntity te = this.getMetaTileEntity();
        if (te != null && te.getCoverAtSide(part.getFacing()) != null) {
            Cover cover = te.getCoverAtSide(part.getFacing());
            if (cover instanceof IAECover aeCover && aeCover.getGridNode(part) != null) {
                callback.setReturnValue(aeCover.getGridNode(part));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getCableConnectionType", cancellable = true)
    private void getCableConnectionType(AEPartLocation part, CallbackInfoReturnable<AECableType> callback) {
        MetaTileEntity te = this.getMetaTileEntity();
        if (te != null && te.getCoverAtSide(part.getFacing()) != null) {
            Cover cover = te.getCoverAtSide(part.getFacing());
            if (cover instanceof IAECover aeCover && aeCover.getCableConnectionType(part) != AECableType.NONE) {
                callback.setReturnValue(aeCover.getCableConnectionType(part));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getActionableNode")
    private void getActionableNode(CallbackInfoReturnable<IGridNode> callback) {

    }

    @Inject(at = @At("HEAD"), method = "getProxy")
    private void getProxy(CallbackInfoReturnable<AENetworkProxy> callback) {

    }


}
