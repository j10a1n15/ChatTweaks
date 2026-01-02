package org.polyfrost.chattweaks.mixins;

import net.minecraft.client.gui.GuiChat;
//#if MC >= 1.17
//$$ import net.minecraft.client.gui.screens.ChatScreen;
//#else
import net.minecraft.client.gui.GuiScreen;
//#endif
import net.minecraft.client.gui.GuiTextField;
import org.polyfrost.chattweaks.ChatTweaks;
import org.polyfrost.chattweaks.features.GuiNewChatHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class GuiChatMixin {

    @Unique
    boolean ct$isShiftKeyDown() {
        //#if MC >= 1.17
        //$$ return ChatScreen.isShiftKeyDown();
        //#else
        return GuiScreen.isShiftKeyDown();
        //#endif
    }

    @Shadow
    protected GuiTextField inputField;

    //#if MC <= 1.17
    @Inject(
            method = "keyTyped",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", ordinal = 1),
            cancellable = true
    )
    void chattweaks$keytypes(char typedChar, int keyCode, CallbackInfo ci) {
    //#else
    //$$    @Inject(
    //$$            method = "keyPressed",
    //$$            at = @At(
    //$$                    value = "INVOKE",
    //$$                    target = "Lnet/minecraft/client/gui/screens/ChatScreen;handleChatInput(Ljava/lang/String;Z)V"
    //$$            )
    //$$    )
        //#if MC > 1.21.8
        //$$ void onKeyPressed(net.minecraft.client.input.KeyEvent event, CallbackInfo ci) {
        //#else
        //$$ void chattweaks$keytypes(int keyCode, int scanCode, int modifiers, CallbackInfo ci) {
        //#endif
    //#endif
        if (ct$isShiftKeyDown() && ChatTweaks.config.shiftChat) {
            ci.cancel();
            this.inputField.setText("");
        }
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;handleComponentClick(Lnet/minecraft/util/IChatComponent;)Z"))
    void chattweaks$mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        GuiNewChatHook.mouseClicked();
    }
}
