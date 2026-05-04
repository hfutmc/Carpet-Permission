package whisperlyric.carpetpermissions.mixin;

import carpet.utils.CommandHelper;
import java.util.Objects;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({CommandHelper.class})
public class CommandHelperMixin {

   @Inject(
      method = {"canUseCommand"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void canUseCommand(ServerCommandSource source, Object commandLevel, CallbackInfoReturnable<Boolean> cir) {
      String permissionString = Thread.currentThread().getStackTrace()[3].getClassName();
      if (source.isExecutedByPlayer()) {
         cir.setReturnValue(Boolean.valueOf(Permissions.check((Entity)Objects.requireNonNull(source.getPlayer()), permissionString)));
      }
   }
}
