package io.github.mireole.mgtceua.client.widgets;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.widgets.*;
import gregtech.api.util.Position;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class WidgetGroupLevelEmitter extends AbstractWidgetGroup {
    protected final IntConsumer setter;
    protected final IntSupplier getter;
    protected final TextFieldWidget2 textField;

    public WidgetGroupLevelEmitter(int yPosition, IntConsumer setter, IntSupplier getter) {
        super(new Position(0, yPosition));
        this.setter = setter;
        this.getter = getter;
        this.textField = new TextFieldWidget2(42, 6, 96, 20, () -> String.valueOf(getter.getAsInt()), (s) -> {
            if (s != null && !s.isEmpty()) setter.accept(Integer.parseInt(s));
        })
                .setNumbersOnly(0, Integer.MAX_VALUE)
                .setMaxLength(10)
                .setPostFix("")
                .setTextColor(0xFFFFFF);
        this.addWidget(new ImageWidget(40, 0, 96, 20, GuiTextures.DISPLAY));
        this.addWidget(this.textField);
        this.addWidget(new IncrementButtonWidget(136, 0, 30, 20, 1, 16, 128, 1024, this::adjustAmount)
                .setDefaultTooltip()
                .setShouldClientCallback(false)
        );
        this.addWidget(new IncrementButtonWidget(10, 0, 30, 20, -1, -16, -128, -1024, this::adjustAmount)
                .setDefaultTooltip()
                .setShouldClientCallback(false)
        );


    }

    private void adjustAmount(int amount) {
        this.setter.accept(Math.max(0, this.getter.getAsInt() + amount));
    }
}
