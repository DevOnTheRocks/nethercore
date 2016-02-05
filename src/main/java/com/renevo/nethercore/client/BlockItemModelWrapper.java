package com.renevo.nethercore.client;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.TRSRTransformation;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.List;

public class BlockItemModelWrapper implements IPerspectiveAwareModel, IFlexibleBakedModel {

    // copied from the forge variants V1 default-block transform
    public static final Matrix4f THIRD_PERSON_BLOCK_TRANSFORM = new TRSRTransformation(
            new Vector3f(0, 1.5f / 16, -2.75f / 16), // translation
            TRSRTransformation.quatFromYXZDegrees(new Vector3f(10, -45, 170)), // rotation
            new Vector3f(0.375f, 0.375f, 0.375f), // scale
            null).getMatrix();

    private final IFlexibleBakedModel parent;

    public BlockItemModelWrapper(IFlexibleBakedModel parent) {
        this.parent = parent;
    }

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Matrix4f matrix = null;
        // fix transformation in hand
        if(cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON) {
            matrix = THIRD_PERSON_BLOCK_TRANSFORM;
        }

        return Pair.of(this, matrix);
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) {
        return parent.getFaceQuads(p_177551_1_);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return parent.getGeneralQuads();
    }

    @Override
    public VertexFormat getFormat() {
        return parent.getFormat();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return parent.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return parent.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return parent.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return parent.getItemCameraTransforms();
    }
}
