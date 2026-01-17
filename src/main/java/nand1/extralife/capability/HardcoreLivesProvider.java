package nand1.extralife.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class HardcoreLivesProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<IHardcoreLives> CAPABILITY =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final HardcoreLives backend = new HardcoreLives();

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CAPABILITY ? LazyOptional.of(() -> backend).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Lives", backend.getLives());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        backend.setLives(tag.getInt("Lives"));
    }
}

