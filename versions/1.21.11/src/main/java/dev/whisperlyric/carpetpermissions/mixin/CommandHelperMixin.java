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
        // Non-player sources: let Carpet handle it normally
        if (!source.isPlayer()) {
            return;
        }

        // When commandLevel is a literal true/false, respect Carpet's intent:
        //   true  → Carpet wants the command always available → fall through (return true)
        //   false → Carpet wants the command disabled → fall through (return false)
        //   We only intercept level-based checks (ops, 0-4) where permission nodes apply.
        if (commandLevel instanceof Boolean) {
            return;
        }
        String levelStr = commandLevel.toString();
        if ("true".equals(levelStr) || "false".equals(levelStr)) {
            return;
        }

        // Derive the permission node from the calling Carpet command class.
        // Stack trace layout:
        //   [0] = java.lang.Thread.getStackTrace()
        //   [1] = this mixin method
        //   [2] = CommandHelper.canUseCommand() (original, redirected by Mixin)
        //   [3] = the Carpet command class that called canUseCommand
        String permissionNode;
        try {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            if (stack.length < 4) {
                LOGGER.warn("[carpet-permissions] Stack trace too short to derive permission node");
                return;
            }
            permissionNode = stack[3].getClassName();
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

        LOGGER.debug("[carpet-permissions] {} | {} | node='{}' | level='{}'",
                allowed ? "ALLOWED" : "DENIED",
                player.getName().getString(), permissionNode, levelStr);

        if (!allowed) {
            LOGGER.info("[carpet-permissions] DENIED | {} | node='{}' | Grant: /lp user {} permission set {} true",
                    player.getName().getString(), permissionNode,
                    player.getName().getString(), permissionNode);
        }

        cir.setReturnValue(allowed);
    }
}
