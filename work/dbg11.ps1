$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
J 'net.minecraft.core.Vec3i' 'm_123341_|m_123342_|m_123343_|getX|getY|getZ'
J 'net.minecraft.core.BlockPos' 'm_121924_|m_121925_|relative|m_215079_|m_14192_'
J 'net.minecraft.world.level.Level' 'm_141988_|getBlockEntity|m_215265_'
Write-Host '--- Level interfaces for getBlockEntity ---'
javap -cp $cp net.minecraft.world.level.BlockGetter 2>&1 | Select-String -Pattern 'BlockEntity|getBlockEntity|m_141988_' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- ServerPlayer openMenu ---'
J 'net.minecraft.server.level.ServerPlayer' 'm_7699_|openMenu|m_6348_'
