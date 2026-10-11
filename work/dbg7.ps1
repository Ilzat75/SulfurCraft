$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
function J($name, $pat) {
  Write-Host "===== $name ====="
  $out = javap -cp $cp $name 2>&1
  if ($pat) { $out | Select-String -Pattern $pat | ForEach-Object { Write-Host $_.Line.Trim() } }
  else { $out | ForEach-Object { Write-Host $_ } }
}
J 'net.minecraft.client.resources.sounds.AbstractTickableSoundInstance' $null
J 'net.minecraft.client.resources.sounds.AbstractSoundInstance' $null
J 'net.minecraft.client.resources.sounds.SoundInstance' 'f_119|m_79075_|m_119814_'
J 'net.minecraft.nbt.CompoundTag' 'putInt|getInt|putBoolean|getBoolean|putFloat|getFloat|putString|getString|putLong|m_1290|put\(|get\('
J 'net.minecraft.core.NonNullList' 'withSize|of|m_183241_'
J 'net.minecraft.world.item.ItemStack' 'm_41619_|isEmpty|m_41613_|getCount|m_41720_|m_41777_|copy|m_41751_|m_41783_|m_41784_|m_41611_|getHoverName|m_41799_|m_41720_'
Write-Host '--- search RegisterMenuScreensEvent in all forge ---'
& jar tf $cp | Select-String -Pattern 'client/event/Register.*Screen|client/event/.*Menu' | ForEach-Object { Write-Host $_.Line }
J 'net.minecraft.client.Minecraft' 'm_91087_|getInstance'
