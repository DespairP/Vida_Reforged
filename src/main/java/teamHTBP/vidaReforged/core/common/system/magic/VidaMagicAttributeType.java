package teamHTBP.vidaReforged.core.common.system.magic;

import teamHTBP.vidaReforged.core.utils.codec.EnumCodec;

public enum VidaMagicAttributeType {
        MAGIC, TOOL, EQUIPMENT, UNDEFINED;

        public final static EnumCodec<VidaMagicAttributeType> CODEC = new EnumCodec<>(VidaMagicAttributeType.class);
}