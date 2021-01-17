package svenhjol.charm.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.storage.MapData;
import svenhjol.charm.Charm;
import svenhjol.charm.base.helper.MapRenderHelper;
import svenhjol.charm.container.AtlasInventory;
import svenhjol.charm.module.Atlas;

public class AtlasRenderer {
    public final RenderType ATLAS_BACKGROUND = RenderType.getText(new ResourceLocation(Charm.MOD_ID, "textures/map/atlas_background.png"));
    private final MapItemRenderer mapItemRenderer;
    private final EntityRendererManager renderManager;
    private final TextureManager textureManager;

    public AtlasRenderer() {
        Minecraft minecraft = Minecraft.getInstance();
        mapItemRenderer = minecraft.gameRenderer.getMapItemRenderer();
        renderManager = minecraft.getRenderManager();
        textureManager = minecraft.getTextureManager();
    }


    public void renderAtlas(MatrixStack matrixStack, IRenderTypeBuffer buffers, int light, Hand hand, float equip, float swing, ItemStack stack) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientWorld world = minecraft.world;
        ClientPlayerEntity player = minecraft.player;
        if (player == null) return;
        AtlasInventory inventory = Atlas.getInventory(world, stack);

        matrixStack.push(); // needed so that parent renderer isn't affect by what we do here

        HandSide handSide = hand == Hand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
        // copypasta from renderMapFirstPersonSide
        float e = handSide == HandSide.RIGHT ? 1.0F : -1.0F;
        matrixStack.translate(e * 0.125F, -0.125D, 0.0D);

        // render player arm
        if (!player.isInvisible()) {
            matrixStack.push();
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(e * 10.0F));
            renderArm(player, matrixStack, buffers, light, equip, swing, handSide);
            matrixStack.pop();
        }

        // transform page based on the hand it is held and render it
        matrixStack.push();
        transformPageForHand(matrixStack, swing, equip, handSide);
        renderAtlasMap(inventory.getActiveMap(world), matrixStack, buffers, light);
        matrixStack.pop();

        matrixStack.pop(); // close
    }

    public void renderArm(ClientPlayerEntity player, MatrixStack matrices, IRenderTypeBuffer buffer, int light, float equip, float swing, HandSide side) {
        // copypasta from renderArmFirstPerson
        boolean flag = side != HandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(swing);
        float f2 = -0.3F * MathHelper.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(swing * (float)Math.PI);
        matrices.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equip * -0.6F, f4 + -0.71999997F);
        matrices.rotate(Vector3f.YP.rotationDegrees(f * 45.0F));
        float f5 = MathHelper.sin(swing * swing * (float)Math.PI);
        float f6 = MathHelper.sin(f1 * (float)Math.PI);
        matrices.rotate(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
        matrices.rotate(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
        textureManager.bindTexture(player.getLocationSkin());
        matrices.translate(f * -1.0F, 3.6F, 3.5D);
        matrices.rotate(Vector3f.ZP.rotationDegrees(f * 120.0F));
        matrices.rotate(Vector3f.XP.rotationDegrees(200.0F));
        matrices.rotate(Vector3f.YP.rotationDegrees(f * -135.0F));
        matrices.translate(f * 5.6F, 0.0D, 0.0D);
        PlayerRenderer playerrenderer = (PlayerRenderer)this.renderManager.<AbstractClientPlayerEntity>getRenderer(player);
        if (flag) {
            playerrenderer.renderRightArm(matrices, buffer, light, player);
        } else {
            playerrenderer.renderLeftArm(matrices, buffer, light, player);
        }
    }

    public void transformPageForHand(MatrixStack matrixStack, float swing, float equip, HandSide handSide) {
        float e = handSide == HandSide.RIGHT ? 1.0F : -1.0F;
        matrixStack.translate(e * 0.51F, -0.08F + equip * -1.2F, -0.75D);
        float f1 = MathHelper.sqrt(swing);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(swing * (float) Math.PI);
        matrixStack.translate(e * f3, f4 - 0.3F * f2, f5);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(f2 * -45.0F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(e * f2 * -30.0F));
    }

    public void renderAtlasMap(MapData mapData, MatrixStack matrixStack, IRenderTypeBuffer buffers, int light) {
        this.renderBackground(ATLAS_BACKGROUND, matrixStack, buffers, light);

        if (mapData != null) {
            matrixStack.push();
            mapItemRenderer.renderMap(matrixStack, buffers, mapData, false, light);
            matrixStack.pop();
        }
    }

    private void renderBackground(RenderType background, MatrixStack matrixStack, IRenderTypeBuffer buffers, int light) {
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.scale(0.38F, 0.38F, 0.38F);
        matrixStack.translate(-0.5D, -0.5D, 0.0D);
        matrixStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
        MapRenderHelper.drawBackgroundVertex(matrixStack, light, buffers.getBuffer(background));
    }
}
