$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
J 'net.minecraft.world.level.block.state.BlockBehaviour$BlockBehaviourState' $null
Write-Host '--- useWithoutItem / onRemove on BlockBehaviour ---'
javap -cp $cp net.minecraft.world.level.block.state.BlockBehaviour 2>&1 | Select-String -Pattern 'm_62621_|m_7787_|onRemove|useWithoutItem' | ForEach-Object { Write-Host $_.Line.Trim() }
J 'net.minecraft.world.level.block.Containers' 'dropContents|m_18983_|m_18982_'
J 'net.minecraft.world.level.block.entity.BlockEntityTicker' $null
J 'net.minecraft.world.level.block.entity.BlockEntityType' 'create|m_58952_|m_155244_'
Write-Host '--- updateNeighbourForOutputSignal ---'
javap -cp $cp net.minecraft.world.level.Level 2>&1 | Select-String -Pattern 'updateNeighbourForOutputSignal|m_46717_' | ForEach-Object { Write-Host $_.Line.Trim() }
