$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
Write-Host '--- CompoundTag: int/bool/float/string/long put+get by signature ---'
javap -cp $cp net.minecraft.nbt.CompoundTag 2>&1 | Select-String -Pattern 'String, int\)|String\)|String, boolean|String, float|String, long|String, java.lang.String|String, net.minecraft.nbt' | ForEach-Object { Write-Host $_.Line.Trim() }
J 'net.minecraft.core.NonNullList' $null
J 'net.minecraft.util.RandomSource' 'm_216335_|create'
J 'net.minecraft.client.resources.sounds.SoundInstance$Attenuation' $null
Write-Host '--- ItemStack save (m_41739_?) ---'
javap -cp $cp net.minecraft.world.item.ItemStack 2>&1 | Select-String -Pattern 'ItemStack m_|void m_.*CompoundTag' | ForEach-Object { Write-Host $_.Line.Trim() }
