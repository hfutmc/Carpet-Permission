package dev.whisperlyric.carpetpermissions;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CarpetPermissions implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("carpet-permissions");

    @Override
    public void onInitialize() {
        LOGGER.info("[carpet-permissions] Loaded! Permission checks active for Carpet commands.");
        LOGGER.info("[carpet-permissions] Players must have explicit LuckPerms permissions to use Carpet commands.");
        LOGGER.info("[carpet-permissions] Console and command blocks use Carpet's default permission system.");
        LOGGER.info("[carpet-permissions] Check the server log when a player runs a Carpet command to see the required permission node.");
    }
}
