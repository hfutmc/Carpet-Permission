package dev.whisperlyric.carpetpermissions.mixin;

import carpet.utils.CommandHelper;
import java.util.Objects;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandHelper.class)
public class CommandHelperMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("carpet-permissions");

    @Inject(
        method = "canUseCommand",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void injectPermissionCheck(CommandSourceStack source, Object commandLevel, CallbackInfoReturnable<Boolean> cir) {
        if (!source.isPlayer()) {
            return;
        }

        String permissionNode;
        try {
            permissionNode = Thread.currentThread().getStackTrace()[3].getClassName();
        } catch (Exception e) {
            LOGGER.warn("[carpet-permissions] Failed to derive permission node from stack trace", e);
            return;
        }

        Entity player;
        try {
            player = Objects.requireNonNull(source.getPlayer());
        } catch (Exception e) {
            LOGGER.warn("[carpet-permissions] source.isPlayer()=true but getPlayer() returned null", e);
            return;
        }

        boolean allowed;
        try {
            allowed = Permissions.check(player, permissionNode);
        } catch (Exception e) {
            LOGGER.error("[carpet-permissions] Permissions.check() threw for node '{}'", permissionNode, e);
            return;
        }

        if (allowed) {
            LOGGER.info("[carpet-permissions] ALLOWED  | {} | node='{}'",
                    player.getName().getString(), permissionNode);
        } else {
            LOGGER.info("[carpet-permissions] DENIED   | {} | node='{}' | Grant in LuckPerms: /lp user {} permission set {} true",
                    player.getName().getString(), permissionNode,
                    player.getName().getString(), permissionNode);
        }
        cir.setReturnValue(allowed);
    }
}
