package io.github.nan8279.smcs.CPE;

import io.github.nan8279.smcs.CPE.extensions.*;

public enum Extension {
    HACK_CONTROL(new HackControlExtension()),
    BLOCK_PERMISSIONS(new BlockPermissionsExtension()),
    HELD_BLOCK(new HeldBlockExtension()),
    TWO_WAY_PING(new TwoWayPingExtension()),
    ENV_COLORS(new EnvColorsExtension());

    final private AbstractExtension extension;
    Extension(AbstractExtension extension) {
        this.extension = extension;
    }

    public AbstractExtension getExtension() {
        return extension;
    }
}
