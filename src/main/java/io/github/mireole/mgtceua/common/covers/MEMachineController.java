package io.github.mireole.mgtceua.common.covers;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.cover.CoverDefinition;
import gregtech.api.cover.CoverableView;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.CycleButtonWidget;
import io.github.mireole.mgtceua.MGTCEuA;
import io.github.mireole.mgtceua.client.MGATextures;
import io.github.mireole.mgtceua.client.widgets.PhantomDualWidget;
import io.github.mireole.mgtceua.client.widgets.WidgetGroupLevelEmitter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class MEMachineController extends AbstractMeMachineController {
    protected int amount = 0;
    protected Object stack;
    protected LevelEmitterMode mode;

    public MEMachineController(@NotNull CoverDefinition definition, @NotNull CoverableView coverableView, @NotNull EnumFacing attachedSide) {
        super(definition, coverableView, attachedSide);
        this.mode = LevelEmitterMode.MORE_THAN;
    }

    @Override
    public void renderCover(@NotNull CCRenderState renderState, @NotNull Matrix4 translation, IVertexOperation[] pipeline, @NotNull Cuboid6 plateBox, @NotNull BlockRenderLayer layer) {
        MGATextures.ME_ITEM_MACHINE_CONTROLLER.renderSided(this.getAttachedSide(), plateBox, renderState, pipeline, translation);
    }

    @Override
    public ModularUI createUI(EntityPlayer player) {
        return ModularUI.builder(GuiTextures.BACKGROUND, 176, 69 + 82)
                .label(30, 5, "cover.me_machine_controller.title")
                .widget(new PhantomDualWidget(31, 21, this.stack).setChangeListener(this::onStackChange))
                .widget(new CycleButtonWidget(68, 20, 88, 20, LevelEmitterMode.class,
                        this::getLevelEmitterMode, this::setLevelEmitterMode))
                .widget(new WidgetGroupLevelEmitter(45, (i) -> {
                            this.amount = i;
                            this.markDirty();
                        },
                        () -> this.amount))
                .bindPlayerInventory(player.inventory, GuiTextures.SLOT, 7, 69)
                .build(this, player);
    }

    public LevelEmitterMode getLevelEmitterMode() {
        return this.mode;
    }

    public void setLevelEmitterMode(LevelEmitterMode mode) {
        this.mode = mode;
        this.writeCustomData(GregtechDataCodes.UPDATE_COVER_MODE, buf -> buf.writeEnumValue(mode));
        this.markDirty();
    }

    @Override
    public void readCustomData(int discriminator, @NotNull PacketBuffer buf) {
        super.readCustomData(discriminator, buf);
        if (discriminator == GregtechDataCodes.UPDATE_COVER_MODE) {
            this.mode = buf.readEnumValue(LevelEmitterMode.class);
        }
    }

    protected void writeStackToNBT(NBTTagCompound tag) {
        if (this.stack instanceof ItemStack itemStack) {
            NBTTagCompound tag1 = new NBTTagCompound();
            itemStack.writeToNBT(tag1);
            tag.setTag("item", tag1);
        }
        else if (this.stack instanceof FluidStack fluidStack) {
            NBTTagCompound tag1 = new NBTTagCompound();
            fluidStack.writeToNBT(tag1);
            tag.setTag("fluid", tag1);
        }
    }

    protected void readStackFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("item")) {
            this.stack = new ItemStack(tag.getCompoundTag("item"));
            return;
        }
        else if (tag.hasKey("fluid")) {
            this.stack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("fluid"));
            return;
        }
        this.stack = null;
    }

    @Override
    public void writeToNBT(@NotNull NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("amount", this.amount);

        NBTTagCompound tag = new NBTTagCompound();
        this.writeStackToNBT(tag);
        tagCompound.setTag("stack", tag);

        tagCompound.setInteger("mode", this.mode.ordinal());
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.amount = tagCompound.getInteger("amount");

        NBTTagCompound tag = tagCompound.getCompoundTag("stack");
        this.readStackFromNBT(tag);

        this.mode = LevelEmitterMode.values()[tagCompound.getInteger("mode")];
    }

    @Override
    public void writeInitialSyncData(@NotNull PacketBuffer packetBuffer) {
        super.writeInitialSyncData(packetBuffer);
        NBTTagCompound tag = new NBTTagCompound();
        this.writeStackToNBT(tag);
        packetBuffer.writeCompoundTag(tag);

        packetBuffer.writeEnumValue(this.mode);
    }

    @Override
    public void readInitialSyncData(@NotNull PacketBuffer packetBuffer) {
        super.readInitialSyncData(packetBuffer);
        try {
            NBTTagCompound tag = packetBuffer.readCompoundTag();
            if (tag != null) this.readStackFromNBT(tag);

            this.mode = packetBuffer.readEnumValue(LevelEmitterMode.class);
        } catch (IOException e) {
            MGTCEuA.LOGGER.error("Could not read initial sync data", e);
        }
    }

    protected void onStackChange(Object newStack) {
        this.stack = newStack;
        this.markDirty();
    }

    public enum LevelEmitterMode implements IStringSerializable {
        MORE_THAN("cover.me_machine_controller.mode.more_than"),
        LESS_THAN("cover.me_machine_controller.mode.less_than")
        ;
        public final String localeName;

        LevelEmitterMode(String localeName) {
            this.localeName = localeName;
        }

        @Override
        public @NotNull String getName() {
            return localeName;
        }
    }

}
