$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
J 'net.minecraft.world.level.block.Block' 'm_62621_|useWithoutItem|m_7246_|getMenuProvider|m_6262_|useItemOn'
J 'net.minecraft.world.InteractionResult' $null
J 'net.minecraft.world.level.block.state.BlockBehaviour' 'm_62621_|m_7246_'
Write-Host '--- BlockPos instance relative (search all Direction methods) ---'
javap -cp $cp net.minecraft.core.BlockPos 2>&1 | Select-String -Pattern 'Direction' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- ItemStack setCount/isEmpty/getItem confirm ---'
javap -cp $cp net.minecraft.world.item.ItemStack 2>&1 | Select-String -Pattern 'void m_41764_|boolean m_41619_|m_41720_|int m_41613_|m_41777_|m_41783_|m_41751_|m_41611_|m_41720_' | ForEach-Object { Write-Host $_.Line.Trim() }
