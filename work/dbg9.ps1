$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
Write-Host '--- NetworkHooks openGui ---'
& jar tf $cp | Select-String -Pattern 'NetworkHooks' | ForEach-Object { Write-Host $_.Line }
J 'net.minecraftforge.network.NetworkHooks' 'openGui|openScreen'
J 'net.minecraft.world.MenuProvider' $null
J 'net.minecraft.world.level.block.entity.BlockEntity' 'm_58904_|m_142466_|m_183515_|m_6596_|m_58899_|m_58900_|m_5995_|m_58483_|m_58903_|m_142466_'
J 'net.minecraft.world.inventory.AbstractContainerMenu' 'AbstractContainerMenu\(|m_38897_|m_38884_|m_6875_|m_7648_|m_38889_|m_38895_|m_38893_'
J 'net.minecraft.world.inventory.DataSlot' 'm_39403_|m_39401_|m_39406_'
J 'net.minecraft.world.inventory.ContainerLevelAccess' 'f_39287_|m_39289_|m_39292_|m_39299_'
Write-Host '--- HorizontalDirectionalBlock FACING field ---'
J 'net.minecraft.world.level.block.HorizontalDirectionalBlock' $null
