$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
Write-Host '--- find RegisterMenuScreensEvent path ---'
& jar tf $cp | Select-String -Pattern 'RegisterMenuScreensEvent|ScreenManager|MenuScreens' | ForEach-Object { Write-Host $_.Line }
J 'net.minecraft.network.FriendlyByteBuf' 'BlockPos|readBlock|m_130'
J 'net.minecraft.sounds.SoundSource' $null
J 'net.minecraftforge.common.extensions.IForgeBlock' $null
J 'net.minecraft.world.level.block.Block' 'm_155232_|getTicker|m_155231_|newBlockEntity|m_142466_|m_58902_'
J 'net.minecraft.world.level.block.BaseEntityBlock' $null
Write-Host '--- GuiGraphics blit ---'
J 'net.minecraft.client.gui.GuiGraphics' 'm_280179_|blit|m_280296_|m_280462_'
Write-Host '--- ItemStack set/count methods ---'
J 'net.minecraft.world.item.ItemStack' 'm_41764_|setCount|m_41751_|save|m_41783_|m_41613_|getCount|m_41720_'
