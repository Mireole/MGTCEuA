package io.github.mireole.mgtceua.mixin.cover;

import appeng.api.networking.IGridNode;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import gregtech.api.cover.Cover;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import io.github.mireole.mgtceua.api.IAECover;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

}
