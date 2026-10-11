$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
J 'net.minecraft.world.inventory.MenuConstructor' $null
Write-Host '--- ItemStack fields (EMPTY) ---'
javap -cp $cp net.minecraft.world.item.ItemStack 2>&1 | Select-String -Pattern 'static final' | ForEach-Object { Write-Host $_.Line.Trim() }
J 'net.minecraft.core.BlockPos' 'm_123341_|m_123342_|m_123343_|m_121924_|m_7495_|m_7494_|m_14192_|getX|getY|getZ|relative|below|above|offset'
J 'net.minecraft.world.level.Level' 'm_141988_|getBlockEntity|m_5776_|m_8055_|m_46849_|m_7731_|m_6262_'
J 'net.minecraft.world.level.block.state.BlockState' 'm_61143_|m_61124_|m_60713_|getValue|setValue|m_60795_|m_5275_'
J 'net.minecraft.core.Direction' 'm_122410_|m_122424_|m_122436_|getOpposite|getClockWise'
