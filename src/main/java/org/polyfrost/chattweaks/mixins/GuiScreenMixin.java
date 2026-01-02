package org.polyfrost.chattweaks.mixins;

import org.polyfrost.chattweaks.ChatTweaks;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC<1.17
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin extends Gui {

    @ModifyArg(method = "handleComponentClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;sendChatMessage(Ljava/lang/String;Z)V"), index = 1)
    public boolean patcher$handleComponentClick(boolean addToChat) {
        return addToChat || (ChatTweaks.config.safeChatClicksHistory && ((Object) this) instanceof GuiChat);
    }
}
//#else
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//$$ import net.minecraft.client.Minecraft;
//$$ import net.minecraft.client.gui.screens.Screen;
//$$ import net.minecraft.client.player.LocalPlayer;
//$$
//$$ @Mixin(net.minecraft.client.gui.screens.Screen.class)
//$$ public class GuiScreenMixin {
//$$
//$$     @WrapOperation(
//$$             method = "defaultHandleGameClickEvent",
//$$             at = @At(
//$$                     value = "INVOKE",
//$$                     target = "Lnet/minecraft/client/gui/screens/Screen;clickCommandAction(Lnet/minecraft/client/player/LocalPlayer;Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;)V"
//$$             )
//$$     )
//$$     private static void onClickCommandAction(LocalPlayer localPlayer, String s, Screen screen, Operation<Void> original) {
//$$         original.call(localPlayer, s, screen);
//$$         if (ChatTweaks.config.safeChatClicksHistory && screen instanceof GuiChat) Minecraft.getInstance().gui.getChat().addRecentChat(s);
//$$     }
//$$ }
//#endif