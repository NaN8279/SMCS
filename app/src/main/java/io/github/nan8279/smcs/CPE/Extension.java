package io.github.nan8279.smcs.CPE;

import io.github.nan8279.smcs.CPE.extensions.*;

/**
 * Lists all the extensions the server has.
 */
public enum Extension {
    HACK_CONTROL(new HackControlExtension()),
    BLOCK_PERMISSIONS(new BlockPermissionsExtension()),
    HELD_BLOCK(new HeldBlockExtension()),
    TWO_WAY_PING(new TwoWayPingExtension()),
    ENV_COLORS(new EnvColorsExtension()),
    BULK_BLOCK_UPDATE(new BulkBlockUpdateExtension());

    final private AbstractExtension extension;
    Extension(AbstractExtension extension) {
        this.extension = extension;
    }

    /**
     * @return the extension.
     */
    public AbstractExtension getExtension() {
        return extension;
    }
}
