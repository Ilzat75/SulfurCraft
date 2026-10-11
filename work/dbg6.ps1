$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
J 'net.minecraft.world.level.block.EntityBlock' $null
Write-Host '--- GuiGraphics blit overloads ---'
J 'net.minecraft.client.gui.GuiGraphics' 'blit|m_280|m_280179_|m_280296_'
Write-Host '--- AbstractContainerScreen all ---'
J 'net.minecraft.client.gui.screens.inventory.AbstractContainerScreen' $null
Write-Host '--- MenuScreens ---'
J 'net.minecraft.client.gui.screens.MenuScreens' $null
Write-Host '--- ScreenConstructor ---'
J 'net.minecraft.client.gui.screens.MenuScreens$ScreenConstructor' $null
Write-Host '--- search RegisterMenuScreens/IContainerFactory ---'
& jar tf $cp | Select-String -Pattern 'IContainerFactory|RegisterContainerScreens|RegisterMenu' | ForEach-Object { Write-Host $_.Line }
Write-Host '--- Level play sound ---'
J 'net.minecraft.world.level.Level' 'playLocalSound|m_7765_|playSound|m_260798_|m_7785_'
