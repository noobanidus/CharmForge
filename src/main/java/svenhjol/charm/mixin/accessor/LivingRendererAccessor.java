package svenhjol.charm.mixin.accessor;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LivingRenderer.class)
public interface LivingRendererAccessor<T extends LivingEntity, M extends EntityModel<T>> {
    @Accessor
    List<LayerRenderer<T, M>> getLayerRenderers();
}
