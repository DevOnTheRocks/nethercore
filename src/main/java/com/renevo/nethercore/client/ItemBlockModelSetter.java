package com.renevo.nethercore.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

import slimeknights.mantle.client.model.BlockItemModelWrapper;
import com.renevo.nethercore.Util;
import com.renevo.nethercore.item.ItemBlockMeta;

@SideOnly(Side.CLIENT)
public class ItemBlockModelSetter {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBake(ModelBakeEvent event) {
        Map<IBlockState, ModelResourceLocation> stateMap = event.modelManager.getBlockModelShapes().getBlockStateMapper().putAllStateModelLocations();

        // go through all items and if they're itemblockmodels we give them a wrapped block model if there is none set
        for(ResourceLocation identifier : Item.itemRegistry.getKeys()) {
            // only our own stuff
            if(!identifier.getResourceDomain().equals(Util.RESOURCE)) {
                continue;
            }
            // only item blocks that use this class
            Object o = Item.itemRegistry.getObject(identifier);
            if(!(o instanceof ItemBlock)) {
                continue;
            }
            ItemBlock item = (ItemBlock) o;

            ModelResourceLocation loc = new ModelResourceLocation(identifier, "inventory");

            // does it have an inventory model set?
            Object model = event.modelRegistry.getObject(loc);
            if(model != null) {
                // yes it does, nothing to do for us
                continue;
            }

            // ok, here's what we're going to do: we wrap all blockmodels in a wrapper that does the perspective in 3rd person for us
            // and then set each item-meta combination to the corresponding block model

            Block block = item.block;
            if(block instanceof BlockSapling || block instanceof BlockVine || block instanceof BlockBush) continue; // has custom itemmodel
            boolean first = true;

            // cycle through all metadatas
            for(int i = 0; i < 16; i++) {
                IBlockState state = block.getStateFromMeta(i);
                int meta = block.getMetaFromState(state);
                // invalid metadata, didn't return the same
                if(meta != i) {
                    continue;
                }

                ModelResourceLocation blockLoc = stateMap.get(state);
                if(blockLoc != null) {
                    // update the model to do perspective transformation
                    IFlexibleBakedModel bakedBlockModel = (IFlexibleBakedModel) event.modelRegistry.getObject(blockLoc);
                    if(bakedBlockModel != null) {
                        bakedBlockModel = new BlockItemModelWrapper(bakedBlockModel);
                        event.modelRegistry.putObject(blockLoc, bakedBlockModel);

                        if(first) {
                            // silence the error
                            event.modelRegistry.putObject(loc, bakedBlockModel);
                            first = false;
                        }
                    }

                    // map the item-meta to the updated block model
                    ModelLoader.setCustomModelResourceLocation(item, meta, blockLoc);
                }
            }
        }
    }
}
