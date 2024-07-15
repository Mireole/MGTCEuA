package io.github.mireole.mgtceua.client.widgets;

import gregtech.api.gui.widgets.PhantomFluidWidget;
import gregtech.api.gui.widgets.PhantomSlotWidget;
import gregtech.common.terminal.app.recipechart.widget.PhantomWidget;
import io.github.mireole.mgtceua.MGTCEuA;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class PhantomDualWidget extends PhantomWidget {
    protected Object object;
    protected PhantomFluidWidget fluidWidget;
    protected PhantomSlotWidget slotWidget;
    public PhantomDualWidget(int x, int y, Object defaultObj) {
        super(x, y, defaultObj);
        this.object = defaultObj;
        // ATs don't work on other mods, so we have to use reflection instead
        try {
            Field f = this.getClass().getSuperclass().getDeclaredField("fluidWidget");
            f.setAccessible(true);
            this.fluidWidget = (PhantomFluidWidget) f.get(this);
            this.fluidWidget.showTip(false);

            Field f1 = this.getClass().getSuperclass().getDeclaredField("slotWidget");
            f1.setAccessible(true);
            this.slotWidget = (PhantomSlotWidget) f1.get(this);
        }
        catch (Exception e) {
            MGTCEuA.LOGGER.error("Could not access fields in PhantomDualWidget", e);
        }

        this.setChangeListener((obj) -> {});
    }

    @Override
    public PhantomWidget setChangeListener(Consumer<Object> onChanged) {
        return super.setChangeListener(
            (obj) -> {
                this.object = obj;
                onChanged.accept(obj);
            }
        );
    }

    // Overriding to allow proper client to server sync and support clearing the item
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            ItemStack itemStack = gui.entityPlayer.inventory.getItemStack();
            if (!itemStack.isEmpty()) {
                IFluidHandler handlerItem = itemStack
                        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (handlerItem != null && handlerItem.getTankProperties().length > 0) {
                    FluidStack fluidStack = handlerItem.getTankProperties()[0].getContents();
                    if (fluidStack != null) {
                        this.setObject(fluidStack);
                        return this.fluidWidget.mouseClicked(mouseX, mouseY, button);
                    }
                }
            }
            this.setObject(itemStack);
            return this.slotWidget.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}