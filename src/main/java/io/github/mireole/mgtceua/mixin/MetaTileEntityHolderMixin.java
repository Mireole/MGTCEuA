package io.github.mireole.mgtceua.mixin;

import appeng.api.networking.IGridNode;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.me.helpers.AENetworkProxy;
import gregtech.api.cover.Cover;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import io.github.mireole.mgtceua.MGTCEuA;
import io.github.mireole.mgtceua.api.IAECover;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MetaTileEntityHolder.class)
public abstract class MetaTileEntityHolderMixin {
    @Shadow
    MetaTileEntity metaTileEntity;

    @Shadow(remap = false) public abstract MetaTileEntity getMetaTileEntity();

    @Inject(at = @At("HEAD"), method = "getGridNode", cancellable = true, remap = false)
    private void getGridNode(AEPartLocation part, CallbackInfoReturnable<IGridNode> callback) {
        MGTCEuA.LOGGER.info("getGridNode");
        if (this.metaTileEntity != null && this.getMetaTileEntity().getCoverAtSide(part.getFacing()) != null) {
            Cover cover = this.getMetaTileEntity().getCoverAtSide(part.getFacing());
            if (cover instanceof IAECover aeCover && aeCover.getGridNode(part) != null) {
                callback.setReturnValue(aeCover.getGridNode(part));
                callback.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getCableConnectionType", cancellable = true, remap = false)
    private void getCableConnectionType(AEPartLocation part, CallbackInfoReturnable<AECableType> callback) {
        MGTCEuA.LOGGER.info("getCableConnectionType");
        if (this.metaTileEntity != null && this.getMetaTileEntity().getCoverAtSide(part.getFacing()) != null) {
            Cover cover = this.getMetaTileEntity().getCoverAtSide(part.getFacing());
            if (cover instanceof IAECover aeCover && aeCover.getCableConnectionType(part) != AECableType.NONE) {
                callback.setReturnValue(aeCover.getCableConnectionType(part));
                callback.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getActionableNode", cancellable = true, remap = false)
    private void getActionableNode(CallbackInfoReturnable<IGridNode> callback) {
        MGTCEuA.LOGGER.info("getActionableNode");
    }

    @Inject(at = @At("HEAD"), method = "getProxy", cancellable = true, remap = false)
    private void getProxy(CallbackInfoReturnable<AENetworkProxy> callback) {
        MGTCEuA.LOGGER.info("getProxy");
    }


}
