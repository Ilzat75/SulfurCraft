$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_.Trim() } }
}
J 'net.minecraft.world.item.ContainerHelper' $null
J 'net.minecraft.world.level.block.Containers' 'm_18991_|m_18994_|removeItem'
J 'net.minecraft.world.level.Level' 'm_46819_|sendBlockUpdated'
J 'net.minecraft.client.sounds.SoundManager' 'm_120372_|play'
Write-Host '--- BlockEntity ctor ---'
javap -cp $cp net.minecraft.world.level.block.entity.BlockEntity 2>&1 | Select-String -Pattern 'BlockEntity\(' | ForEach-Object { Write-Host $_.Line.Trim() }
