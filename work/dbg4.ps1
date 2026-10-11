$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
J 'net.minecraft.network.FriendlyByteBuf' 'readBlockPos|writeBlockPos|m_130141_'
J 'net.minecraft.world.inventory.DataSlot' $null
J 'net.minecraft.world.inventory.ContainerData' $null
J 'net.minecraftforge.common.ForgeHooks' 'getBurnTime'
J 'net.minecraft.client.resources.sounds.SimpleSoundInstance' 'SimpleSoundInstance\(|forRecord|forBlockEntity|forUI|m_11986|m_11987'
J 'net.minecraft.sounds.SoundSource' 'f_1191'
J 'net.minecraft.client.gui.screens.inventory.AbstractContainerScreen' 'AbstractContainerScreen\(|m_7856_|m_7286_|render|m_181709_|m_9779_|m_9770_'
J 'net.minecraftforge.client.event.RegisterMenuScreensEvent' $null
J 'net.minecraftforge.common.extensions.IForgeBlock' 'Ticker|BlockEntity|newBlockEntity|getTicker'
