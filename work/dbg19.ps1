$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
Write-Host '--- Level: methods with BlockUpdated / send ---'
javap -cp $cp net.minecraft.world.level.Level 2>&1 | Select-String -Pattern 'BlockUpdated|send|Updated' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- ListTag ---'
javap -cp $cp net.minecraft.nbt.ListTag 2>&1 | Select-String -Pattern 'm_14716_|append|m_14728_' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- CompoundTag getList/put ---'
javap -cp $cp net.minecraft.nbt.CompoundTag 2>&1 | Select-String -Pattern 'm_128437_|m_128365_' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- ItemStack copyWithCount m_41620_ ---'
javap -cp $cp net.minecraft.world.item.ItemStack 2>&1 | Select-String -Pattern 'm_41620_' | ForEach-Object { Write-Host $_.Line.Trim() }
