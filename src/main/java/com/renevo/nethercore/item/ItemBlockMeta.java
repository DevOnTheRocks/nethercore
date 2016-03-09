package com.renevo.nethercore.item;

import com.renevo.nethercore.Util;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ItemBlockMeta extends ItemColored {

    protected IProperty mappingProperty;

    public ItemBlockMeta(Block block) {
        super(block, true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if(mappingProperty == null) return super.getUnlocalizedName(stack);

        IBlockState state = block.getStateFromMeta(stack.getMetadata());
        String name = state.getValue(mappingProperty).toString().toLowerCase(Locale.US);
        return super.getUnlocalizedName(stack) + "." + name;
    }

    @SuppressWarnings("unchecked")
    public static void setMappingProperty(Block block, IProperty<?> property) {
        ((ItemBlockMeta) Item.getItemFromBlock(block)).mappingProperty = property;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(StatCollector.canTranslate(this.getUnlocalizedName(stack) + ".tooltip")) {
            tooltip.add(EnumChatFormatting.GRAY.toString() + Util.translateRecursive(this.getUnlocalizedName(stack) + ".tooltip"));
        }
        else if(StatCollector.canTranslate(super.getUnlocalizedName(stack) + ".tooltip")) {
            tooltip.add(EnumChatFormatting.GRAY.toString() + Util.translateRecursive(super.getUnlocalizedName(stack) + ".tooltip"));
        }
        super.addInformation(stack, playerIn, tooltip, advanced);
    }
}