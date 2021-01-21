package svenhjol.charm.render;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.client.VariantMobTexturesClient;
import svenhjol.charm.mixin.accessor.LayerRendererAccessor;
import svenhjol.charm.mixin.accessor.LivingRendererAccessor;

import java.util.List;

public class VariantMobRenderer {
    private static <T extends LivingEntity, M extends EntityModel<T>> void fillLayersFromOld(EntityRendererManager manager, LivingRenderer<T, M> renderer,
                                                                                             EntityType<T> type) {
        EntityRenderer<?> old = manager.renderers.get(type);
        if (old != null) {
            List<LayerRenderer<T, M>> layerRenderers = ((LivingRendererAccessor<T, M>) renderer).getLayerRenderers();
            layerRenderers.clear();
            ((LivingRendererAccessor<T, M>) old).getLayerRenderers()
                    .stream()
                    .peek(it -> ((LayerRendererAccessor<T, M>)it).setEntityRenderer(renderer))
                    .forEach(layerRenderers::add);
        }
    }

    public static class Chicken extends ChickenRenderer {
        public Chicken(EntityRendererManager dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.CHICKEN);
        }

        @Override
        public ResourceLocation getEntityTexture(ChickenEntity entity) {
            return VariantMobTexturesClient.getChickenTexture(entity);
        }
    }

    public static class Cow extends CowRenderer {
        public Cow(EntityRendererManager dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.COW);
        }

        @Override
        public ResourceLocation getEntityTexture(CowEntity entity) {
            return VariantMobTexturesClient.getCowTexture(entity);
        }
    }

    public static class Pig extends PigRenderer {
        public Pig(EntityRendererManager dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.PIG);
        }

        @Override
        public ResourceLocation getEntityTexture(PigEntity entity) {
            return VariantMobTexturesClient.getPigTexture(entity);
        }
    }

    public static class Sheep extends SheepRenderer {
        public Sheep(EntityRendererManager dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.SHEEP);
        }

        @Override
        public ResourceLocation getEntityTexture(SheepEntity entity) {
            return VariantMobTexturesClient.getSheepTexture(entity);
        }
    }

    public static class SnowGolem extends SnowManRenderer {
        public SnowGolem(EntityRendererManager dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.SNOW_GOLEM);
        }

        @Override
        public ResourceLocation getEntityTexture(SnowGolemEntity entity) {
            return VariantMobTexturesClient.getSnowGolemTexture(entity);
        }
    }

    public static class Squid extends SquidRenderer {
        public Squid(EntityRendererManager dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.SQUID);
        }

        @Override
        public ResourceLocation getEntityTexture(SquidEntity entity) {
            return VariantMobTexturesClient.getSquidTexture(entity);
        }
    }

    public static class Wolf extends WolfRenderer {
        public Wolf(EntityRendererManager dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.WOLF);
        }

        @Override
        public ResourceLocation getEntityTexture(WolfEntity entity) {
            return VariantMobTexturesClient.getWolfTexture(entity);
        }
    }
}
